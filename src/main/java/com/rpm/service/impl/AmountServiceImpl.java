package com.rpm.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rpm.domain.Amount;
import com.rpm.mapper.AmountMapper;
import com.rpm.service.IAmountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rpm.vo.AmountNumVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author rpm
 * @since 2021-09-17
 */
@Service
public class AmountServiceImpl extends ServiceImpl<AmountMapper, Amount> implements IAmountService {

    @Autowired
    private AmountMapper amountMapper;

    @Override
    public IPage<Amount> getAmountList(Page<Amount> page, Wrapper wrapper) {
        return amountMapper.getAmountList(page, wrapper);
    }

    @Override
    public List<AmountNumVo> getAmountNum() {
        return amountMapper.getAmountNum();
    }
}
