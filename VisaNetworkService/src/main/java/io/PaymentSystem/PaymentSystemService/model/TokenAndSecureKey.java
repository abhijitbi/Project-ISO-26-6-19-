package io.PaymentSystem.PaymentSystemService.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("tokensecurekey")
public class TokenAndSecureKey
{
    private String dateAsToken;
    private String securekey;

    public TokenAndSecureKey() {
    }

    public TokenAndSecureKey(String dateAsToken, String securekey) {
        this.dateAsToken = dateAsToken;
        this.securekey = securekey;
    }

    public String getDateAsToken() {
        return dateAsToken;
    }

    public void setDateAsToken(String dateAsToken) {
        this.dateAsToken = dateAsToken;
    }

    public String getSecurekey() {
        return securekey;
    }

    public void setSecurekey(String securekey) {
        this.securekey = securekey;
    }
}
