package com.rpm.utils.shiro;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * jwt工具类，用于生成，校验token
 */
@Component
public class JWTUtil {
    // 过期时间 24 小时
    private static final long EXPIRE_TIME = 60 * 24 * 60 * 1000;

    /**
     * 生成 token, 5min后过期
     *
     * @param username 用户名
     * @param secret 秘钥为用户登录时输入的密码
     * @param role 角色
     * @return 加密的token
     */
    public static String createToken(String username, String role, String secret, String avatar) {
        try {
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            // 附带username信息
            return JWT.create()
                    .withClaim("username", username)
                    .withClaim("role", role)
                    .withClaim("avatar", avatar)
                    //到期时间
                    .withExpiresAt(date)
                    //创建一个新的JWT，并使用给定的算法进行标记
                    .sign(algorithm);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 校验 token 是否正确
     *
     * @param token    token
     * @param username 用户名
     * @param role 角色
     * @return 是否正确
     */
    public static boolean verify(String token, String username, String role, String secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            //在token中附带了username信息
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("username", username)
                    .withClaim("role", role)
                    .build();
            //验证 token
            verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     *
     * @return token中包含的用户名
     */
    public static Map getUsername(String token) {
        HashMap<String, Object> map = new HashMap<>();
        try {
            DecodedJWT jwt = JWT.decode(token);
            map.put("username",jwt.getClaim("username").asString());
            map.put("role",jwt.getClaim("role").asString());
            map.put("avatar",jwt.getClaim("avatar").asString());
            return map;
        } catch (JWTDecodeException e) {
            return null;
        }
    }
}
