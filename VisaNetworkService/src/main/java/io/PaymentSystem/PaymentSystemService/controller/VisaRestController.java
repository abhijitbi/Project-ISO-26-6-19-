package io.PaymentSystem.PaymentSystemService.controller;

import io.PaymentSystem.PaymentSystemService.model.TokenAndKey;
import io.PaymentSystem.PaymentSystemService.model.TokenAndKeyRepository;
import io.PaymentSystem.PaymentSystemService.service.SecurekeyService;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;


@RestController
@RequestMapping("/VISA")
public class VisaRestController
{
    private static SecretKey secretKey;

    private static String token=null;

    static Cipher cipher;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TokenAndKeyRepository tokenAndKeyRepository;

    private static String encodedKey;


    @RequestMapping(value = "/secKey")
    public TokenAndKey createSecureKey() throws Exception{                                                             //Create Encrypted Secret Key and send it back to AcquirerBankService

        System.out.println("************VISA************");

        secretKey = KeyGenerator.getInstance("AES").generateKey();                                                      //Generate Secret Key
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());                                 // get base64 encoded version of the key

        Date date=new Date();
        token=date.toString();
        System.out.println("Date As Token::"+token);                                                                    //Consider Date as a Token
        return  new TokenAndKey(date.toString(),encodedKey);
    }

    @RequestMapping(value="/keyAndData")
    public String decryptedReceivedData() throws Exception {

        TokenAndKey getKeyAndData = tokenAndKeyRepository.getSecurekeyByToken(token);                                     //Retrieve Key from database for given Token
        System.out.println("Token::" + token);
        System.out.println("Key::" + getKeyAndData.getKey());

        Base64.Decoder decoder = Base64.getDecoder();                                                                   //Get base64 decoded version of the key
        byte[] encryptedTextByte = decoder.decode(getKeyAndData.getKey());                                              //Decode the given Key
        cipher = Cipher.getInstance("AES");                                                                             //Get Cipher Instance for "AES:Advanced Encryption Standard"
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedByte = cipher.doFinal(encryptedTextByte);                                                       //Get Decrypted data in Bytes
        String decryptedText = new String(decryptedByte);                                                               //Store it into the String

        GenericPackager packager = new GenericPackager("src/main/resources/basic.xml");

        // Create ISO Message
        ISOMsg msg = new ISOMsg();
        msg.setPackager(packager);
        msg.unpack(decryptedText.getBytes());

        System.out.println("----ISO MESSAGE ParseISOMessage-----");
        try {
            System.out.println("  MTI : " + msg.getMTI());
            System.out.println("MaxFeild::" + msg.getMaxField());
            for (int i = 1; i <= msg.getMaxField(); i++) {

                if (msg.hasField(i)) {
                    System.out.println("    Field-" + i + " : " + msg.getString(i));
                }
            }
        } catch (ISOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("--------------------");
        }

        return decryptedText;


    }

}
