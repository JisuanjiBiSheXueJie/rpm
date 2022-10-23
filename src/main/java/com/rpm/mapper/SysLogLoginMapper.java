package com.rpm.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rpm.domain.SysLogLogin;
import com.rpm.domain.User;
import com.rpm.vo.LoginNumVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 登录日志 Mapper 接口
 * </p>
 *
 * @author rpm
 * @since 2021-09-11
 */
public interface SysLogLoginMapper extends BaseMapper<SysLogLogin> {

    IPage<SysLogLogin> getLogPage(Page<SysLogLogin> page, @Param(Constants.WRAPPER) Wrapper wrapper);

    List<LoginNumVo> getLoginNum(@Param("loginId") String loginId);
}
