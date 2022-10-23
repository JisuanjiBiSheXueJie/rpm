package com.rpm.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rpm.domain.Amount;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rpm.vo.AmountNumVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author rpm
 * @since 2021-09-17
 */
public interface IAmountService extends IService<Amount> {

    IPage<Amount> getAmountList(Page<Amount> page, Wrapper wrapper);

    List<AmountNumVo> getAmountNum();
}
