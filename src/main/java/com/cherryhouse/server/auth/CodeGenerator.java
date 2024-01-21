package com.cherryhouse.server.auth;

import com.cherryhouse.server._core.exception.ApiException;
import com.cherryhouse.server._core.exception.ExceptionCode;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class CodeGenerator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789!@#$%";
    private static final int CODE_LENGTH = 8;

    public static String generateCode(){
        try {
            SecureRandom secureRandom = SecureRandom.getInstanceStrong();
            StringBuilder code = new StringBuilder(CODE_LENGTH);

            for(int i = 0; i < CODE_LENGTH; i++){
                int index = secureRandom.nextInt(CHARACTERS.length());
                code.append(CHARACTERS.charAt(index));
            }

            return code.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new ApiException(ExceptionCode.EMAIL_CODE_CREATION_FAILED);
        }
    }
}
