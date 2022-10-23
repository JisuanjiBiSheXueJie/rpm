package com.rpm.service.impl;

import com.rpm.domain.Announcement;
import com.rpm.mapper.AnnouncementMapper;
import com.rpm.service.IAnnouncementService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class AnnouncementServiceImpl extends ServiceImpl<AnnouncementMapper, Announcement> implements IAnnouncementService {

}
