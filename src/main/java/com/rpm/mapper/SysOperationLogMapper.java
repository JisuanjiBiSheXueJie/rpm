package com.rpm.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rpm.domain.SysOperationLog;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author rpm
 * @since 2021-09-11
 */
public interface SysOperationLogMapper extends BaseMapper<SysOperationLog> {

    IPage<SysOperationLog> getOperationLog(Page<SysOperationLog> page, @Param(Constants.WRAPPER) Wrapper wrapper);

}
