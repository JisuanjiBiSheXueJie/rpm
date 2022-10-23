package com.rpm.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rpm.domain.SysLogLogin;
import com.rpm.domain.User;
import com.rpm.mapper.SysLogLoginMapper;
import com.rpm.service.ISysLogLoginService;
import com.rpm.vo.LoginNumVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 登录日志 服务实现类
 * </p>
 *
 * @author rpm
 * @since 2021-09-11
 */
@Service
public class SysLogLoginServiceImpl extends ServiceImpl<SysLogLoginMapper, SysLogLogin> implements ISysLogLoginService {

    @Autowired
    private SysLogLoginMapper sysLogLoginMapper;

    @Override
    public IPage<SysLogLogin> getLogPage(Page<SysLogLogin> page, Wrapper wrapper) {
        return sysLogLoginMapper.getLogPage(page,wrapper);
    }

    @Override
    public List<LoginNumVo> getLoginNum(String loginId) {
        return sysLogLoginMapper.getLoginNum(loginId);
    }
}
