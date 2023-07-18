package com.gas.mapper;

import com.gas.entity.Gas;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GasMapper {
    List<Gas> queryAllGasName();

    Gas queryGasDBTable(@Param("name")String gasName);

    List<Gas> queryAllInfo();

}
