package com.gas.mapper;

import com.gas.entity.ManagerReview;
import org.apache.catalina.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerReviewMapper {
    int addReviewInfo(@Param("review") ManagerReview review);
}
