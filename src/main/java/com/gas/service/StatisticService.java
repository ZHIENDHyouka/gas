package com.gas.service;

import com.gas.entity.Gas;
import com.gas.entity.ResultVO;
import com.gas.mapper.GasMapper;
import com.gas.mapper.StatisticMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

import static com.gas.utils.DateTimeUtil.*;
import static com.gas.utils.DateTimeUtil.getStringTimeStamp;

@Service
public class StatisticService {
    @Autowired
    private GasMapper gasMapper;
    @Autowired
    private StatisticMapper statisticMapper;

    public ResultVO queryGasNameList() {
        List<Gas> gases = gasMapper.queryAllGasName();
        ArrayList<Map<String, Object>> result = new ArrayList<>();
        if (gases.size() > 0) {
            //封装数据
            for (int i = 0; i < gases.size(); i++) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("index", i + "");
                map.put("name", gases.get(i).getName());
                result.add(map);
            }
            return new ResultVO(1, result, "数据获取成功");
        }
        return new ResultVO(0, result, "没有气体入库或者出现bug");
    }

    public ResultVO getStatisticData(Map param) {
        //气体名称
        String gasName = param.get("name").toString();
        //统计方式
        String satisticValue = param.get("satisticValue").toString();
        //统计时间跨度
        String unit = param.get("datetimeUnit").toString();
        //统计图表类型 如果为""空字符串则返回所有图表类型的封装数据
        String chartType = param.get("chartType").toString();
        List datetimeList = (List) param.get("datetime");
        //查询气体所对应的表
        String tableName = gasMapper.queryGasDBTable(gasName).getTableName();
        //生成查询表的字段
        String dataColumn = "";
        String indateColumn = "";
        if ("温度".equals(gasName)) {
            dataColumn = "t_data";
            indateColumn = "t_indate";
            gasName = null;
        } else if ("湿度".equals(gasName)) {
            dataColumn = "h_data";
            indateColumn = "h_indate";
            gasName = null;
        } else {
            dataColumn = "g_data";
            indateColumn = "g_indate";
        }
        List<Map<String, Object>> maps = statisticMapper.queryGasDate(tableName,
                datetimeList.get(0).toString()
                , datetimeList.get(1).toString()
                , gasName, dataColumn, indateColumn);
        //获取统计之后的数据
        List<Map<String, Object>> uintStatisticDataList = getUintStatisticDataList(maps, unit, satisticValue, dataColumn, indateColumn, datetimeList);
        int code = 0;
        if ("".equals(chartType)) {
            //封装所有的图
            HashMap<String, Object> drawAllData = new HashMap<>();
            Map<String, Object> drawLineAndAreaData = getDrawLineAndAreaData(uintStatisticDataList);//折线图和面积图数据是一样的用一个就好
            drawAllData.put("drawLineAndAreaData",drawLineAndAreaData);
            return new ResultVO(1, drawAllData, "");
        } else if ("line1".equals(chartType)||"line2".equals(chartType)) {
            //封装基本折线图和面积图
            Map<String, Object> result = getDrawLineAndAreaData(uintStatisticDataList);
            code=(int)result.get("code");
            return new ResultVO(code, result.get("data"), "");
        } else if ("xxx".equals(chartType)) {
        }
        return new ResultVO(code, null, "");
    }

    //折线图和面积图封装
    private Map<String, Object> getDrawLineAndAreaData(List<Map<String, Object>> unitStatisticData) {
        HashMap<String, Object> result = new HashMap<>();
        if (unitStatisticData.size() > 0) {
            HashMap<String, Object> resultDataMap = new HashMap<>();
            ArrayList<Double> dataList = new ArrayList<>();
            ArrayList<String> dateList = new ArrayList<>();
            resultDataMap.put("dataList", dataList);
            resultDataMap.put("dateList", dateList);
            unitStatisticData.stream().forEach(map -> {
                dateList.add(map.get("datetime").toString());
                if (((Double) map.get("data")) == null) {
                    dataList.add(0.00);
                } else {
                    dataList.add(new BigDecimal((Double) map.get("data")).setScale(2, BigDecimal.ROUND_DOWN).doubleValue());
                }
            });
            result.put("code", 1);
            result.put("data", resultDataMap);
            result.put("msg", "");
            return result;
        }
        result.put("code", 0);
        result.put("data", null);
        result.put("msg", "折线图面积图为获取到数据");
        return result;
    }

    //根据日期单位和统计计算方式处理数据
    private List<Map<String, Object>> getUintStatisticDataList(List<Map<String, Object>> dataList, String unit, String satisticValue, String dataColumn, String indateColumn, List datetimeList) {
        String start = datetimeList.get(0).toString();
        String end = datetimeList.get(1).toString();
        ArrayList<Map<String, Object>> result = new ArrayList<>();
        //存储计算数据List
        ArrayList<Double> calcList = new ArrayList<>();
        if ("day".equals(unit)) {
            //变成yyyy-MM-dd格式
            String startDateTimeFormat = start.substring(0, 10);
            int count = 0;
            for (Map m : dataList) {
                String datetime = m.get(indateColumn).toString().substring(0, 10) + " 00:00:00";
                String datetimeFormart = m.get(indateColumn).toString().substring(0, 10);
                //在同一天
                if (startDateTimeFormat.equals(datetimeFormart)) {
                    //添加数据
                    Double value = ((Float) m.get(dataColumn)).doubleValue();
                    calcList.add(value);
                } else {
                    //计算上一轮的数据
                    calcData(calcList, satisticValue, startDateTimeFormat, result);
                    //计算时间戳
                    long nextTimeStamp = getStringTimeStamp(datetime, DATETIMEFORMAT);
                    long previousTimeStamp = getStringTimeStamp(startDateTimeFormat + " 00:00:00", DATETIMEFORMAT);
                    //计算相差天数
                    Long intervalDay = (nextTimeStamp - previousTimeStamp) / 1000 / 60 / 60 / 24;
                    if (intervalDay != 1) {
                        for (int i = 0; i < intervalDay - 1; i++) {
                            HashMap<String, Object> lackMap = new HashMap<>();
                            lackMap.put("date", 0.0);
                            previousTimeStamp += 1000 * 60 * 60 * 24;
                            LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(previousTimeStamp / 1000, 0, ZoneOffset.ofHours(8));
                            String localDateTimeFormat = getLocalDateTimeFormat(localDateTime, DATEFORMAT);
                            lackMap.put("datetime", localDateTimeFormat);
                            result.add(lackMap);
                        }
                    }
                    startDateTimeFormat = datetimeFormart;
                }
                count++;
                if (count == dataList.size() - 1) {
                    calcData(calcList, satisticValue, startDateTimeFormat, result);
                }
            }
            //最后补齐
            addFinalData(result, end);
        } else if ("month".equals(unit)) {
            //获取起始时间的月
            String month = start.substring(5, 7);
            //存储上一个月的时间数据yyyy-MM-dd
            String tmepDateTime = start.substring(0, 7);
            int count = 0;
            for (Map m : dataList) {
                //当前数据时间的月
                String dataMonth = m.get(indateColumn).toString().substring(5, 7);
                //如果月不同则说明遍历进入了下一个月 对当前月进行数据处理即可
                if (!dataMonth.equals(month)) {
                    //计算上一个月份的数据
                    calcData(calcList, satisticValue, tmepDateTime, result);
                    //比较2月份是否连续
                    int i = stringNumberIsContinue(month, dataMonth);
                    if (i != 0) {
                        i = Integer.parseInt(m.get(indateColumn).toString().substring(0, 4)) - Integer.parseInt(tmepDateTime.substring(0, 4)) * 12 + i;
                        int intMonth = Integer.parseInt(month);
                        //不连续要补齐缺失的月份 数据为0即可
                        for (int j = 0; j < i - 1; j++) {
                            intMonth++;
                            String lackMonth = "";
                            if (intMonth >= 13) intMonth = 1;
                            if (intMonth >= 10) lackMonth = String.valueOf(intMonth);
                            else lackMonth = "0" + intMonth;
                            HashMap<String, Object> lackMap = new HashMap<>();
                            lackMap.put("data", 0.0);
                            lackMap.put("datetime", tmepDateTime.replace(tmepDateTime.substring(0, 7), lackMonth));
                            result.add(lackMap);
                        }
                    }
                    //更新月
                    month = dataMonth;
                    tmepDateTime = m.get(indateColumn).toString().substring(0, 7);
                }
                Double value = ((Float) m.get(dataColumn)).doubleValue();
                calcList.add(value);
                count++;
                if (count == dataList.size() - 1) {
                    calcData(calcList, satisticValue, month, result);
                }
            }
        } else if ("year".equals(unit)) {
            //获取起始年的数据
            String year = start.substring(0, 4);
            int count = 0;
            for (Map m : dataList) {
                //获取数据的年
                String dataYear = m.get(indateColumn).toString().substring(0, 4);
                if (!dataYear.equals(year)) {
                    //计算前一轮的数据
                    calcData(calcList, satisticValue, year, result);
                    int i = stringNumberIsContinue(year, dataYear);
                    if (i != 0) {
                        int intYear = Integer.parseInt(year);
                        for (int j = 0; j < i - 1; j++) {
                            intYear++;
                            HashMap<String, Object> lackMap = new HashMap<>();
                            lackMap.put("data", 0.0);
                            lackMap.put("datetime", intYear);
                            result.add(lackMap);
                        }
                    }
                    year = dataYear;
                }
                Double value = ((Float) m.get(dataColumn)).doubleValue();
                calcList.add(value);
                count++;
                if (count == dataList.size() - 1) {
                    calcData(calcList, satisticValue, year, result);
                }
            }
        }
        return result;
    }

    //求平均数
    private Double getGasListAverage(List<Double> dataList) {
        return dataList.stream().mapToDouble(Double::doubleValue).average().getAsDouble();
    }

    //求中位数
    private Double getGasListMedian(List<Double> dataList) {
        //排序
        dataList.sort(Comparator.naturalOrder());
        int size = dataList.size();
        if (size % 2 == 0) return dataList.get(size / 2);
        return (dataList.get(size / 2) + dataList.get(size / 2 + 1)) / 2;
    }

    //比较2字符串数字是否连续
    private int stringNumberIsContinue(String number, String nextNumber) {
        return Integer.parseInt(number) - Integer.parseInt(nextNumber) > 1 ? Integer.parseInt(number) - Integer.parseInt(nextNumber) : 0;
    }

    //计算上一轮的数据
    private void calcData(List calcList, String satisticValue, String startDateTimeFormat, List result) {
        Double calc = 0.0;
        HashMap<String, Object> dayDataMap = new HashMap<>();
        if (calcList.size() > 0) {
            if ("average".equals(satisticValue)) {
                calc = getGasListAverage(calcList);
            } else if ("median".equals(satisticValue)) {
                calc = getGasListMedian(calcList);
            }
        }
        dayDataMap.put("data", calc);
        dayDataMap.put("datetime", startDateTimeFormat);
        result.add(dayDataMap);
        //计算完一个时间段之后清空List
        calcList.clear();
    }

    private void addFinalData(List<Map<String, Object>> result, String end) {
        long listEndStamp = getStringTimeStamp(result.get(result.size() - 1).get("datetime").toString() + " 00:00:00", DATETIMEFORMAT);
        long endStamp = getStringTimeStamp(end, DATETIMEFORMAT);
        long stampIntervalDay = (endStamp - listEndStamp) / 1000 / 60 / 60 / 24;
        for (int i = 0; i < stampIntervalDay; i++) {
            HashMap<String, Object> lackMap = new HashMap<>();
            lackMap.put("date", 0.0);
            listEndStamp += 1000 * 60 * 60 * 24;
            LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(listEndStamp / 1000, 0, ZoneOffset.ofHours(8));
            String localDateTimeFormat = getLocalDateTimeFormat(localDateTime, DATEFORMAT);
            lackMap.put("datetime", localDateTimeFormat);
            result.add(lackMap);
        }
    }
}
