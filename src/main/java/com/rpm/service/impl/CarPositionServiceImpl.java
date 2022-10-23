package com.rpm.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rpm.domain.CarPosition;
import com.rpm.mapper.CarPositionMapper;
import com.rpm.service.ICarPositionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rpm.vo.CarPositionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author rpm
 * @since 2021-09-17
 */
@Service
public class CarPositionServiceImpl extends ServiceImpl<CarPositionMapper, CarPosition> implements ICarPositionService {

    @Autowired
    private CarPositionMapper carPositionMapper;

    @Override
    public IPage<CarPositionVo> getCarPositionList(Page<CarPositionVo> page, Wrapper wrapper) {
        return carPositionMapper.getCarPositionList(page,wrapper);
    }
}
