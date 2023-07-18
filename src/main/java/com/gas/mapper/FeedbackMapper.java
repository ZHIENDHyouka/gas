package com.gas.mapper;


import com.gas.entity.Device;
import com.gas.entity.Feedback;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ( feedback)表数据库访问层
 *
 * @author makejava
 * @since 2023-07-09 17:53:39
 */
@Repository
public interface FeedbackMapper {

    int addFeedback(Feedback feedback);

    List<Feedback> getAllFeedbackList();

    int updateFeedbackInfo(Integer id);

    List<Device> getFeedbackAllInfo(Integer id);
}

