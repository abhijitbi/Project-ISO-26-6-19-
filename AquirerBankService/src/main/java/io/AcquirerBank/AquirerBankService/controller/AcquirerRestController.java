package io.AcquirerBank.AquirerBankService.controller;

import io.AcquirerBank.AquirerBankService.model.*;
import io.AcquirerBank.AquirerBankService.service.AcquirerService;
import org.jpos.iso.packager.GenericPackager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@RestController
@RequestMapping(value = "/Acquirer")
public class AcquirerRestController extends  KeyAndData
{
    private int iin=6521;

    static Cipher cipher;

    private TokenAndKey tokenAndKey;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AcquirerService acquirerService;

    @Autowired
    private IINRepository iinRepository;

    @Autowired
    private TokenAndKeyRepository tokenAndKeyRepository;

    @RequestMapping(value = "/jposData")
    private TokenAndKey logISOMsg() throws Exception {

          GenericPackager packager = new GenericPackager("src/main/resources/basic.xml");
          String posData = "0200B2200000001000000000000000800000201234000000010000011072218012345606A5DFGR031VETEALDEMONIOISO8583 1234567890";
          System.out.println("Jpos Data:: " + posData);

          IINumber systemDetails=iinRepository.getNetworkByIin(iin);
          String network=systemDetails.getNetwork();                                          //Retrieve payment Sytem network from IINumber Database
          System.out.println("Network::"+network);                                    //Check the Payment System for given IIN number

          RestTemplate restTemplate = new RestTemplate();                             //Create instance of rest template
          String uri = "http://localhost:8086/"+network+"/secKey";
          tokenAndKey=restTemplate.getForObject(uri,TokenAndKey.class);               //Receive Token and Key Object from Payment System
          tokenAndKey.getKey();                                                       //Retrieve Key from Payment System(e.g. RuPay)
          tokenAndKey.getToken();
          cipher = Cipher.getInstance("AES");                                        //getInstance of Cipher
          byte[] decodedKey = Base64.getDecoder().decode(tokenAndKey.getKey());      // decode the base64 encoded string
                                                                                    // rebuild key using SecretKeySpec
          SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
          byte[] plainTextByte = posData.getBytes();                                  //Convert Jpos Data in bytes
          cipher.init(Cipher.ENCRYPT_MODE,originalKey);                               //Add secret key which we received from Payment System
          byte[] encryptedByte = cipher.doFinal(plainTextByte);                       //Encypt Data into Bytes
          Base64.Encoder encoder = Base64.getEncoder();                               //Encode Data
          String encryptedText = encoder.encodeToString(encryptedByte);                //Convert the Encoded into String
          TokenAndKey keyAndData=new TokenAndKey(tokenAndKey.getToken(),encryptedText);
          tokenAndKeyRepository.save(keyAndData);                                       //Save the Encypted key and Token into the Database

          return  keyAndData;

    }
}
