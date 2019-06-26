package io.PaymentSystem.PaymentSystemService.controller;

import io.PaymentSystem.PaymentSystemService.model.KeyAndData;
import io.PaymentSystem.PaymentSystemService.model.TokenAndKey;
import io.PaymentSystem.PaymentSystemService.model.TokenAndKeyRepository;
import io.PaymentSystem.PaymentSystemService.service.EncryptionService;
import io.PaymentSystem.PaymentSystemService.service.SecurekeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/RuPay")
public class RuPayRestController
{
    private RestTemplate restTemplate;

    private static SecretKey secKey;
    private static String token=null;

    @Autowired
    private TokenAndKeyRepository tokenAndKeyRepository;

    private static String encodedKey;

    //Create Encrypted Secret Key and send it back to AcquirerBankService
    @RequestMapping(value = "/secKey")
    public  TokenAndKey createSecureKey() throws Exception{

        System.out.println("************RuPay************");

        secKey= SecurekeyService.getSecretEncryptionKey();// Generate in Secret Key
        System.out.println("Secret key::"+secKey.getEncoded());
        //byte[] encoded=secKey.getEncoded();//Get the encoded data in Bytes
        //encodedKey= Base64.getEncoder().encodeToString(secKey);//Convert the encoded key into the String
        //encodedKey = encoded.toString();
        //System.out.println("EncodedKey::"+encodedKey);
        Date date=new Date();
        token=date.toString();
        System.out.println("Date::"+token);
        return  new TokenAndKey(date.toString(),secKey.getEncoded().toString());
    }

    @RequestMapping(value="/keyAndData")
    public TokenAndKey getDataAndKey() throws Exception {


       TokenAndKey getKeyAndData=tokenAndKeyRepository.getSecurekeyByToken(token);

        System.out.println("Token::"+token);
        System.out.println("Key::"+getKeyAndData.getKey());
/*
        byte[] decodedKey = getKeyAndData.getKey().getBytes();
        System.out.println("Length::"+decodedKey);
        String decryptedText = EncryptionService.decryptText( decodedKey, secKey);
        System.out.println("Descrypted Text:" + decryptedText);*/

        return getKeyAndData;





        /*System.out.println("EncodedKey::"+encodedKey);
        byte[] keyDecodedInAcquirer = Base64.getDecoder().decode(encodedKey);
        SecretKey secretKeyOfSystem = new SecretKeySpec(keyDecodedInAcquirer, 0,keyDecodedInAcquirer.length, "AES");
        KeyAndData s=keyAndDataRepository.getSecurekeyByEncoded(encodedKey);
        s.getEncoded();

        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");*/






      /*  String decryptedText = EncryptionService.decryptText(s.getEncoded().getBytes(), secretKeyOfSystem);
        System.out.println("Descrypted Text:" + decryptedText);
        return  keyAndDataRepository.findAll();*/

    }
}
