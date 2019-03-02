package info.aoki.surplus.system;

import java.util.UUID;

/**
 * <h3>UUID util</h3>
 * <p>Create time:12/5/18 9:49 AM</p>
 *
 * @author wong
 * @version 1.0
 */
public class UuidUtil {
    public static String getRandomUUID() {
        UUID uuid = UUID.randomUUID();
        String uuidStr = uuid.toString();
        uuidStr = uuidStr.replace("-", "");
        uuidStr = uuidStr.toUpperCase();
        return uuidStr;
    }
}
