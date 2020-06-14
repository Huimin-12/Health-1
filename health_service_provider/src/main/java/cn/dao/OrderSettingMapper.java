package cn.dao;

import cn.domain.OrderSetting;

import java.util.Date;

public interface OrderSettingMapper {
    //执行批量插入操作
    void add(OrderSetting orderSetting);
    //查询当天有没有信息插入
    Long findCountByOrderDate(Date orderDate);
    //更新表数据
    void editNumberByOrderDate(OrderSetting orderSetting);
}
