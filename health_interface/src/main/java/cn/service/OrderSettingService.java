package cn.service;

import cn.domain.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {
    void add(List<OrderSetting> orderSettingList);

    List<Map> getOrderSettingByMonth(String data);

    void editNumberByDate(OrderSetting orderSetting);
}
