package com.rpm.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rpm.domain.Amount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rpm.domain.Msg;
import com.rpm.vo.AmountNumVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author rpm
 * @since 2021-09-17
 */
public interface AmountMapper extends BaseMapper<Amount> {

    IPage<Amount> getAmountList(Page<Amount> page, @Param(Constants.WRAPPER) Wrapper wrapper);

    List<AmountNumVo> getAmountNum();

}
