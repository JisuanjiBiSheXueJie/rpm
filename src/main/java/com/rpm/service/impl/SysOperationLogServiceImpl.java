package com.rpm.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rpm.domain.SysOperationLog;
import com.rpm.mapper.SysOperationLogMapper;
import com.rpm.service.ISysOperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author rpm
 * @since 2021-09-11
 */
@Service
public class SysOperationLogServiceImpl extends ServiceImpl<SysOperationLogMapper, SysOperationLog> implements ISysOperationLogService {

    @Autowired
    private SysOperationLogMapper sysOperationLogMapper;

    @Override
    public IPage<SysOperationLog> getOperationLog(Page<SysOperationLog> page, Wrapper wrapper) {
        return sysOperationLogMapper.getOperationLog(page,wrapper);
    }
}
