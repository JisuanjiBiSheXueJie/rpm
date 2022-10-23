package com.rpm.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rpm.domain.Msg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rpm.vo.CarPositionVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author rpm
 * @since 2021-09-17
 */
public interface MsgMapper extends BaseMapper<Msg> {

    IPage<Msg> getMsgList(Page<Msg> page, @Param(Constants.WRAPPER) Wrapper wrapper);

    Msg getMsgById(@Param(Constants.WRAPPER) Wrapper wrapper);
}
