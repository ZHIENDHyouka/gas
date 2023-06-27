package com.gas.mapper;

import com.gas.entity.ManagerReview;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManagerReviewMapper {
    int addReviewInfo(@Param("review") ManagerReview review);

    List<ManagerReview> getAllManagerReviewList();

    Integer updateById(String id, String date, String status);

    ManagerReview selectById(String id);
}
