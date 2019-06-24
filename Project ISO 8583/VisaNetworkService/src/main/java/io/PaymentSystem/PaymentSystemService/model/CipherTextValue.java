package io.PaymentSystem.PaymentSystemService.model;

public class CipherTextValue
{
    private String cipheredText;

    public CipherTextValue() {
    }

    public CipherTextValue(String cipheredText) {
        this.cipheredText = cipheredText;
    }

    public String getCipheredText() {
        return cipheredText;
    }

    public void setCipheredText(String cipheredText) {
        this.cipheredText = cipheredText;
    }
}
