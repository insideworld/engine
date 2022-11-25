/*
 * Copyright (c) 2022 Anton Eliseev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
        System.out.println("Please select:");
        System.out.println("1. Prepare HTTP basic header");
        System.out.println("2. Just crypt some string");
        httpCrypt();
        justCrypt();
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
