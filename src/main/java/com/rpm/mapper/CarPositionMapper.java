package com.rpm.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rpm.domain.CarPosition;
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
public interface CarPositionMapper extends BaseMapper<CarPosition> {

    IPage<CarPositionVo> getCarPositionList(Page<CarPositionVo> page, @Param(Constants.WRAPPER) Wrapper wrapper);

}
