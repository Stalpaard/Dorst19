package utilities;

import utilities.jbcrypt.BCrypt;

public class PasswordHasher {
    private static int workload = 12;

    public static String hashPw(String password) {
        String salt = BCrypt.gensalt(workload);
        return BCrypt.hashpw(password, salt);
    }

    public static boolean checkPw(String password, String hash) {
        if (hash == null || !(hash.startsWith("$2a$")))
            throw new IllegalArgumentException("Invalid hash");

        return BCrypt.checkpw(password, hash);
    }
}
