package monkey.d.luffy.passwordmanager.util;

import se.simbio.encryption.Encryption;

/**
 * Created by shasha on 28/5/18.
 */

public class EncryDercy {
    private static final String key = "IHateHarryPotter";
    private static final String salt = "there'sNoSalt.Eh!";
    private static final byte[] iv = new byte[16];
    private static final Encryption encryption = Encryption.getDefault(key, salt, iv);

    public static String encrypt (String data) {
        return encryption.encryptOrNull(data);
    }

    public static String decrypt (String data) {
        return encryption.decryptOrNull(data);
    }
}
