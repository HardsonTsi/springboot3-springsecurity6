package com.hardtech.security.auth.config;

public class JWTUtils {
    //https://www.allkeysgenerator.com/Random/Security-Encryption-key-generator.aspx
    public static final String SECRET_KEY = "33743677397A24432646294A404E635266546A576E5A7234753778214125442A";
    public static final String PREFIX = "Bearer ";
    public static final String AUTH_HEADER = "Authorization";
    public static final long EXPIRATION_ACCESS_TOKEN = 31557600L * 1000;
    public static final long EXPIRATION_REFRESH_TOKEN = 63072000L * 1000;

}
