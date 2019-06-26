package io.AcquirerBank.AquirerBankService.controller;

import io.AcquirerBank.AquirerBankService.model.*;
import io.AcquirerBank.AquirerBankService.service.AcquirerService;
import io.AcquirerBank.AquirerBankService.service.EncryptionService;
import org.jpos.iso.packager.GenericPackager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;


import java.util.Base64;

@RestController
@RequestMapping(value = "/Acquirer")
public class AcquirerRestController extends  KeyAndData
{
    private RestTemplate restTemplate;
    private int iin=6521;

    @Autowired
    private AcquirerService acquirerService;

    @Autowired
    private IINRepository iinRepository;

    @Autowired
    private TokenAndKeyRepository tokenAndKeyRepository;

    private String network=null;

    private String keyEncodedInSystem=null;

    private TokenAndKey  tokenAndKey;

    @RequestMapping("/iinNumber")
    public IINumber getBankData(){
        IINumber systemDetails=iinRepository.getNetworkByIin(iin);
        network=systemDetails.getNetwork();
        System.out.println("Networks:::::"+network);
        return systemDetails;
    }

  @RequestMapping(value = "/jposData")
   private TokenAndKey logISOMsg() throws Exception {

      GenericPackager packager = new GenericPackager("src/main/resources/basic.xml");
      String posData = "0200B2200000001000000000000000800000201234000000010000011072218012345606A5DFGR031VETEALDEMONIOISO8583 1234567890";
      System.out.println("DATA : " + posData);


      IINumber systemDetails=iinRepository.getNetworkByIin(iin);
      network=systemDetails.getNetwork();
      System.out.println("Network::"+network);
      RestTemplate restTemplate = new RestTemplate();
      String uri = "http://localhost:8086/"+network+"/secKey";
      tokenAndKey=restTemplate.getForObject(uri,TokenAndKey.class);
      tokenAndKey.getKey();
      tokenAndKey.getToken();

      
     /* byte[] keyDecodedInAcquirer = Base64.getDecoder().decode(tokenAndKey.getKey());

    System.out.println("Token::"+tokenAndKey.getToken());
    System.out.println("Key::"+tokenAndKey.getKey());

    SecretKey secretKeyOfSystem = new SecretKeySpec(keyDecodedInAcquirer, 0,keyDecodedInAcquirer.length, "AES");

    //SecretKey secKey = EncryptionService.getSecretEncryptionKey();
      byte[] cipherTextOfDataAndKey = EncryptionService.encryptText(posData, secretKeyOfSystem);
      System.out.println("cipherTextOfDataAndKey Text:" + cipherTextOfDataAndKey);

      String cipherTextInString=cipherTextOfDataAndKey.toString();
      String decryptedText = EncryptionService.decryptText(cipherTextOfDataAndKey, secretKeyOfSystem);
      System.out.println("Descrypted Text:" + decryptedText);




   // String decryptedText = EncryptionService.decryptText(cipherTextOfDataAndKey, secretKeyOfSystem);
    //System.out.println("AES Key (Hex Form):" + bytesToHex(secKey.getEncoded()));
      System.out.println("Encrypted Text (Hex Form):" + cipherTextOfDataAndKey);
 //   System.out.println("Descrypted Text:" + decryptedText);
      String encryptedText=bytesToHex(cipherTextOfDataAndKey);*/

      TokenAndKey keyAndData=new TokenAndKey(tokenAndKey.getToken(),null);
      tokenAndKeyRepository.save(keyAndData);


      return  keyAndData;
    }
      private static String bytesToHex(byte[] hash) {

        return DatatypeConverter.printHexBinary(hash);
    }
}
