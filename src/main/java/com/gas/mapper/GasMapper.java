package com.gas.mapper;

import com.gas.entity.Gas;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GasMapper {
    List<Gas> queryAllGasName();

}
