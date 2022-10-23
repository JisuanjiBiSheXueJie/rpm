package com.rpm.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rpm.domain.User;
import com.rpm.mapper.UserMapper;
import com.rpm.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author rpm
 * @since 2021-09-01
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 通过username查找用户
     * @param username
     * @return
     */
    @Override
    public User findUserByUsername(String username) {
        //mybatis-plus条件构造器
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public IPage<User> getUserPage(Page<User> page, Wrapper wrapper) {
        return userMapper.getUserPage(page,wrapper);
    }

    @Override
    public List<User> getDelUser(Wrapper wrapper) {
        return userMapper.getDelUser(wrapper);
    }

    @Override
    public boolean refreshUser(Integer code, Wrapper wrapper) {
        return userMapper.refreshUser(code,wrapper);
    }

    @Override
    public boolean thawhUser(Integer code, Wrapper wrapper) {
        return userMapper.thawUser(code,wrapper);
    }
}
