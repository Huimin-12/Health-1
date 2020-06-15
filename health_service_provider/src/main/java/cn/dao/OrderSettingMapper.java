package cn.dao;

import cn.domain.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingMapper {
    //执行批量插入操作
    void add(OrderSetting orderSetting);
    //查询当天有没有信息插入
    Long findCountByOrderDate(Date orderDate);
    //更新表数据
    void editNumberByOrderDate(OrderSetting orderSetting);
    //根据时间段 查询这段时间的数据
    List<OrderSetting> getOrderSettingByMonth(Map<String,String> map);
}
