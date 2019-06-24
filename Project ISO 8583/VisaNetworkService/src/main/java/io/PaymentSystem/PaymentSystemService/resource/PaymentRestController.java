package io.PaymentSystem.PaymentSystemService.resource;

import io.PaymentSystem.PaymentSystemService.model.CipherTextValue;
import org.jpos.iso.ISOMsg;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class PaymentRestController
{
    private RestTemplate restTemplate;

    @RequestMapping("/getToken/VISA")
    public CipherTextValue getBankDetails(){

       /* CipherTextValue cipherTextValue=restTemplate.getForObject("AcquirerBankService/jposData", CipherTextValue.class);
        System.out.println(cipherTextValue.getCipheredText());*/

        return  new CipherTextValue("Hello");
    }

}
