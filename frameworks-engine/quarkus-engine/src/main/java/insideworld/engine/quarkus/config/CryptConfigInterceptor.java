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
