package io.AcquirerBank.AquirerBankService.resource;


import io.AcquirerBank.AquirerBankService.model.CipherTextValue;
import io.AcquirerBank.AquirerBankService.model.IINRepository;
import io.AcquirerBank.AquirerBankService.model.IINumber;
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
import javax.xml.bind.DatatypeConverter;


import java.util.HashMap;

@RestController
public class AcquirerRestController
{

    private RestTemplate restTemplate;

    private int iin=123456;

    @Autowired
    private AcquirerService acquirerService;

    @Autowired
    private IINRepository iinRepository;

    private String network=null;

    @RequestMapping("/iinNumber")
    public IINumber getBankData(){
        IINumber systemDetails=iinRepository.getNetworkByIin(iin);
        network=systemDetails.getNetwork();
        System.out.println("Networks:::::"+network);

        return systemDetails;
    }

  @RequestMapping("/jposData")
   // private  HashMap<Integer, String> logISOMsg() throws Exception {
  private String logISOMsg() throws Exception {
        // Create Packager based on XML that contain DE type
        GenericPackager packager = new GenericPackager("src/main/resources/basic.xml");
        String plainText = "0200B2200000001000000000000000800000201234000000010000011072218012345606A5DFGR031VETEALDEMONIOISO8583 1234567890";
        System.out.println("DATA : " + plainText);

      IINumber systemDetails=iinRepository.getNetworkByIin(iin);
      network=systemDetails.getNetwork();
      System.out.println("Networks:::::"+network);


      SecretKey secKey = getSecretEncryptionKey();
      byte[] cipherText = encryptText(plainText, secKey);
      String decryptedText = decryptText(cipherText, secKey);


      CipherTextValue cipherTextValue=restTemplate.getForObject("http://localhost:8086/getToken/VISA", CipherTextValue.class);

      System.out.println("Original Text:" + plainText);
      System.out.println("AES Key (Hex Form):"+bytesToHex(secKey.getEncoded()));
      System.out.println("Encrypted Text (Hex Form):"+bytesToHex(cipherText));
      System.out.println("Descrypted Text:"+decryptedText);


      System.out.println(cipherTextValue);
      return cipherTextValue.getCipheredText().toString();
  }

    /**
     * gets the AES encryption key. In your actual programs, this should be safely
     * stored.
     * @return
     * @throws Exception
     */
    public static SecretKey getSecretEncryptionKey() throws Exception{
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(128); // The AES key size in number of bits
        SecretKey secKey = generator.generateKey();
        return secKey;
    }
    /**
     * Encrypts plainText in AES using the secret key
     * @param plainText
     * @param secKey
     * @return
     * @throws Exception
     */
    public static byte[] encryptText(String plainText,SecretKey secKey) throws Exception{
        // AES defaults to AES/ECB/PKCS5Padding in Java 7
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.ENCRYPT_MODE, secKey);
        byte[] byteCipherText = aesCipher.doFinal(plainText.getBytes());
        return byteCipherText;
    }

    /**
     * Decrypts encrypted byte array using the key used for encryption.
     * @param byteCipherText
     * @param secKey
     * @return
     * @throws Exception
     */
    public static String decryptText(byte[] byteCipherText, SecretKey secKey) throws Exception {
        // AES defaults to AES/ECB/PKCS5Padding in Java 7
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.DECRYPT_MODE, secKey);
        byte[] bytePlainText = aesCipher.doFinal(byteCipherText);
        return new String(bytePlainText);
    }

    /**
     * Convert a binary byte array into readable hex form
     * @param hash
     * @return
     */
    private static String  bytesToHex(byte[] hash) {

        return DatatypeConverter.printHexBinary(hash);
    }

}
