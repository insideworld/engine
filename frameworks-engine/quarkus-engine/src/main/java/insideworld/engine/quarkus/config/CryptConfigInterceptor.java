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

package insideworld.engine.quarkus.config;

import io.smallrye.config.ConfigSourceInterceptor;
import io.smallrye.config.ConfigSourceInterceptorContext;
import io.smallrye.config.ConfigValue;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class CryptConfigInterceptor implements ConfigSourceInterceptor {

    private final ThreadLocal<Cipher> encode = new ThreadLocal<>();

    @Override
    public final ConfigValue getValue(
        final ConfigSourceInterceptorContext context,
        final String name) {
        final ConfigValue proceed = context.proceed(name);
        final ConfigValue result;
        if (proceed != null && proceed.getValue().startsWith("CRYPT_")) {
            result = this.decrypt(
                name,
                proceed.getValue().replace("CRYPT_", ""),
                context
            );
        } else {
            result = proceed;
        }
        return result;
    }

    private ConfigValue decrypt(
        final String name,
        final String secret,
        final ConfigSourceInterceptorContext context
    ) {
        try {
            final byte[] encoded = this.getCypher(context).doFinal(Hex.decodeHex(secret));
            return ConfigValue.builder()
                    .withName(name)
                    .withValue(new String(encoded))
                    .build();
        } catch (DecoderException | IllegalBlockSizeException | BadPaddingException exp) {
            throw new RuntimeException(exp);
        }
    }

    private Cipher getCypher(final ConfigSourceInterceptorContext context) {
        if (this.encode.get() == null) {
            final ConfigValue key = context.proceed("ENGINE_ENCRYPTION_KEY");
            final ConfigValue type = context.proceed("ENGINE_ENCRYPTION_TYPE");
            if (key == null || type == null) {
                throw new RuntimeException(
                    "??? You set a crypt property but properties for decrypt doesn't set ???"
                );
            }
            try {
                final SecretKeySpec secret = new SecretKeySpec(
                    Hex.decodeHex(key.getValue()),
                    type.getValue()
                );
                final Cipher cipher = Cipher.getInstance(type.getValue());
                cipher.init(Cipher.DECRYPT_MODE, secret);
                this.encode.set(cipher);
            } catch (final Exception exp) {
                throw new RuntimeException(exp);
            }
        }
        return this.encode.get();
    }
}
