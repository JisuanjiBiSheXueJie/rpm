package com.rpm.utils.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * 重写方法，使token可被shiro识别
 */
public class JWTToken implements AuthenticationToken {
    private String token;

    public JWTToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
