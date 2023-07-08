package com.gas.utils;
import com.gas.entity.ExcessGas;
import com.gas.entity.HarmfulGas;
import com.gas.mapper.ExcessGasMapper;
import com.gas.mapper.HarmfulGasMapper;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;
@Component
public class CreateDataUtils {
    private static SqlSessionFactory sqlSessionFactory;
    @Autowired
    private SqlSessionFactory sqlSessionFactory1;
    @PostConstruct
    public void initSqlSessionFactory(){
        sqlSessionFactory=sqlSessionFactory1;
    }

    private static String deviceIdArr[]={"23BG-67TY-91KL","09TH-56RE-23FG","76FR-12GT-88HY","45UY-87JK-09DF","33RT-65FH-11DX",
            "54GB-28HN-94KL","87TH-06UY-77PL","22SI-09DG-63KJ","99RS-44UI-12RE","71HI-65OP-91KL"};
    private static String gasNameArr[] = {"PM2.5","PM10","SO2","NO2","CO","O3"};

    public static void createExcessGasData(int n){
        ArrayList<ExcessGas> excessGasList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            ExcessGas gas = new ExcessGas(0, gasNameArr[random.nextInt(6)],
                    decimalFormat.format(random.nextDouble() * 10),
                    "有害气体",
                    DateTimeUtil.getNowFormatDateTimeString(DateTimeUtil.DATETIMEFORMAT),
                    deviceIdArr[random.nextInt(10)]);
            excessGasList.add(gas);
        }
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        ExcessGasMapper mapper = sqlSession.getMapper(ExcessGasMapper.class);
        excessGasList.stream().forEach(excessGas -> mapper.insertAlarmGasInfo(excessGas));
        sqlSession.commit();
        sqlSession.clearCache();
    }

    public static void createHarmFulGas(int n){
        ArrayList<HarmfulGas> harmfulGasList = new ArrayList<>();
        Random random = new Random();
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        for (int i = 0; i <n; i++) {
            HarmfulGas harmfulGas = new HarmfulGas(0, gasNameArr[random.nextInt(6)],
                    Double.parseDouble(decimalFormat.format(random.nextDouble() * 10)),
                    DateTimeUtil.getNowFormatDateTimeString(DateTimeUtil.DATETIMEFORMAT),
                    "22SI-09DG-63KJ");
            harmfulGasList.add(harmfulGas);
        }
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        HarmfulGasMapper mapper = sqlSession.getMapper(HarmfulGasMapper.class);
        harmfulGasList.stream().forEach(harmfulGas -> mapper.addHarmfulGasDateTest(harmfulGas));
        sqlSession.commit();
        sqlSession.clearCache();

    }

}
