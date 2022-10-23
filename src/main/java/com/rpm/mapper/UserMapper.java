package com.rpm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rpm.domain.User;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.conditions.Wrapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author rpm
 * @since 2021-09-01
 */
public interface UserMapper extends BaseMapper<User> {

    IPage<User> getUserPage(Page<User> page, @Param(Constants.WRAPPER) Wrapper wrapper);

    List<User> getDelUser(@Param(Constants.WRAPPER) Wrapper wrapper);

    boolean refreshUser(@Param("code") Integer code,@Param(Constants.WRAPPER) Wrapper wrapper);

    boolean thawUser(@Param("code") Integer code,@Param(Constants.WRAPPER) Wrapper wrapper);
}
