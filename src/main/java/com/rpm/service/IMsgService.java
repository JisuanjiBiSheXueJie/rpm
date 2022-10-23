package com.rpm.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rpm.domain.Msg;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author rpm
 * @since 2021-09-17
 */
public interface IMsgService extends IService<Msg> {

    IPage<Msg> getMsgList(Page<Msg> page, Wrapper wrapper);

    Msg getMsgById(Wrapper wrapper);
}
