package com.rpm.config.shiro;

import com.rpm.domain.User;
import com.rpm.service.IUserService;
import com.rpm.utils.shiro.JWTToken;
import com.rpm.utils.shiro.JWTUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * shiro配置UserRealm
 */
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private IUserService userService;

    /**
     * 必须重写此方法，不然会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 执行认证方法
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("————身份认证方法————");
        String token = (String) authenticationToken.getCredentials();
        // 解密获得username，用于和数据库进行对比
        Map<String, Object> map = JWTUtil.getUsername(token);
        String username = (String) map.get("username");

        User user = userService.findUserByUsername(username);

        if (user == null || !JWTUtil.verify(token, user.getUsername(),user.getRole(),user.getPassword())) {
            throw new IncorrectCredentialsException();
        }
        return new SimpleAuthenticationInfo(token, token, getName());
    }

    /**
     * 执行授权方法
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("————权限认证————");
        Map<String, Object> map = JWTUtil.getUsername(principals.toString());
        //通过token获取用户的角色
        String userRole = (String) map.get("role");

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        //获得该用户角色
        String role = userRole;
        Set<String> roleSet = new HashSet<>();
        roleSet.add(role);

        //设置该用户拥有的角色和权限
        info.setRoles(roleSet);
        return info;
    }
}
