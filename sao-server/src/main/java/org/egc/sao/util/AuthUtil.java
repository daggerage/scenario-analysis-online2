package org.egc.sao.util;

import org.egc.sao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public class AuthUtil {
    public static String createJwt(String name){
        return name;
    }
    public static boolean isJwtValide(String auth){
        if(auth==null||auth.isEmpty()){
            return false;
        }
        return true;
    }
    public static UUID getIdByToken(String token){
        return UUID.fromString(token);
    }
}
