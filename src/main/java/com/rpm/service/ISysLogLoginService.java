package com.rpm.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rpm.domain.SysLogLogin;
import com.rpm.domain.User;
import com.rpm.vo.LoginNumVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 登录日志 服务类
 * </p>
 *
 * @author rpm
 * @since 2021-09-11
 */
public interface ISysLogLoginService extends IService<SysLogLogin> {

    IPage<SysLogLogin> getLogPage(Page<SysLogLogin> page, Wrapper wrapper);

    List<LoginNumVo> getLoginNum(String loginId);
}
