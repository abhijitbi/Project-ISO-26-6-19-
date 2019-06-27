package io.AcquirerBank.AquirerBankService.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("keyandata")
public class KeyAndData
{

   private String encoded;
    private String token;

    public KeyAndData() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        token = token;
    }

    public KeyAndData(String token,String encoded) {
        this.encoded = encoded;
        token = token;
    }

    public String getEncoded() {
        return encoded;
    }

    public void setEncoded(String encoded) {
        this.encoded = encoded;
    }
}
