package vn.edu.nlu.fit.thuctapltw.Util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    // Hash password
    public static String hash(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }

    // Check password
    public static boolean verify(String plainPassword, String hashedPassword) {
        if (hashedPassword == null) return false;
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    public static boolean checkOldPass(String oldPass, String hashPass) {
        return  verify(oldPass,hashPass);
    }
}
