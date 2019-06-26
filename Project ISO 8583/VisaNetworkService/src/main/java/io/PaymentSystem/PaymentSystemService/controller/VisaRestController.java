package io.PaymentSystem.PaymentSystemService.controller;

import io.PaymentSystem.PaymentSystemService.service.SecurekeyService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import javax.crypto.SecretKey;


@RestController
@RequestMapping("/VISA")
public class VisaRestController
{
    private RestTemplate restTemplate;

    @RequestMapping(value = "/secKey")
    public  String createSecureKey() throws Exception{
        System.out.println("************VISA************");
        SecretKey secKey = SecurekeyService.getSecretEncryptionKey();
        System.out.println("Secret key::"+secKey.getEncoded());
        byte[] s=secKey.getEncoded();
         String keyEncodedInSystem=s.toString();
          //return secKey.getEncoded();
        return  keyEncodedInSystem;
    }




}
