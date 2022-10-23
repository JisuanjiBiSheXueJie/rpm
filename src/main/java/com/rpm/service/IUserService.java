package com.rpm.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rpm.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author rpm
 * @since 2021-09-01
 */
public interface IUserService extends IService<User> {
    public User findUserByUsername(String username);

    IPage<User> getUserPage(Page<User> page, @Param(Constants.WRAPPER) Wrapper wrapper);

    List<User> getDelUser(Wrapper wrapper);

    boolean refreshUser(Integer code,Wrapper wrapper);

    boolean thawhUser(Integer code,Wrapper wrapper);
}
