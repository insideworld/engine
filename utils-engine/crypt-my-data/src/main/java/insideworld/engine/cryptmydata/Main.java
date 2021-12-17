package insideworld.engine.cryptmydata;


import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.Scanner;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class Main {

    public static void main(String[] args) throws DecoderException, GeneralSecurityException {
        final String s1 = "qwe";
        final String qwe1 = (new StringBuilder()).append("qwe").toString();
        qwe1.intern();
        final String s2 = "qwe";
        final String qwe2 = (new StringBuilder()).append("qwe").toString();
        System.out.println(s1 == s2);
        System.out.println(qwe1 == qwe2);
        System.out.println(qwe1);
        System.out.println(qwe2);

//        System.out.println("Please select:");
//        System.out.println("1. Prepare HTTP basic header");
//        System.out.println("2. Just crypt some string");
//        httpCrypt();
//        justCrypt();
    }

    public static void httpCrypt() throws DecoderException, GeneralSecurityException {
        Scanner in = new Scanner(System.in);
        System.out.println("Write a username:");
        var user = in.nextLine();
        System.out.println("Write a password:");
        var password = in.nextLine();
        String auth = user + ":" + password;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
        String authHeaderValue = "Basic " + new String(encodedAuth);
        cryptPassword(authHeaderValue);
    }

    public static void justCrypt() throws DecoderException, GeneralSecurityException {
        Scanner in = new Scanner(System.in);
        System.out.println("Write your secrets:");
        var secret = in.nextLine();
        cryptPassword(secret);
    }

    public static void cryptPassword(String crypt)
        throws DecoderException, GeneralSecurityException {
        Scanner in = new Scanner(System.in);
        System.out.println("Secret key:");
        final SecretKeySpec aes = new SecretKeySpec(Hex.decodeHex(in.nextLine()), "AES");
        final Cipher encode = Cipher.getInstance("AES");
        encode.init(Cipher.ENCRYPT_MODE, aes);
        final byte[] crypted = encode.doFinal(crypt.getBytes());
        System.out.println(Hex.encodeHex(crypted));
    }

}
