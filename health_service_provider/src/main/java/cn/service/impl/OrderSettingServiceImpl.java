package cn.service.impl;

import cn.dao.OrderSettingMapper;
import cn.domain.OrderSetting;
import cn.service.OrderSettingService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 预约设置服务
 */
@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    private OrderSettingMapper orderSettingMapper;
    //批量添加
    @Override
    public void add(List<OrderSetting> orderSettingList) {
        //遍历list集合
        for (OrderSetting orderSetting : orderSettingList) {
            //检查此数据（日期）是否存在
            Long count = orderSettingMapper.findCountByOrderDate(orderSetting.getOrderDate());
            if (count>0) {
                //已经存在，执行更新操作
                orderSettingMapper.editNumberByOrderDate(orderSetting);
            }else {
                //不存在，执行添加操作
                orderSettingMapper.add(orderSetting);
            }
        }


    }

    @Override
    public List<Map> getOrderSettingByMonth(String date) {
        String dateBegin = date+"-1";
        String dateEnd = date+"-31";
        //创建一个map集合，用来存储开始日期，结束日期
        Map<String,String> map = new HashMap<>();
        map.put("dateBegin",dateBegin);
        map.put("dateEnd",dateEnd);
        List<OrderSetting> orderSettingList = orderSettingMapper.getOrderSettingByMonth(map);
        List<Map> data = new ArrayList<>();
        for (OrderSetting orderSetting : orderSettingList) {
            //创建一个map用来存储orderSetting数据
            Map<String,Object> m = new HashMap<>();
            m.put("date",orderSetting.getOrderDate().getDate());//获取日期
            m.put("number",orderSetting.getNumber());//获取可预约人数
            m.put("reservations",orderSetting.getReservations());//获取已预约人数
            //添加到list集合当中
            data.add(m);
        }
        return data;
    }

    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        Long count = orderSettingMapper.findCountByOrderDate(orderSetting.getOrderDate());
        if (count>0){
            //说明数据已经存在，执行修改操作
            orderSettingMapper.editNumberByOrderDate(orderSetting);
        }else {
            //说明数据没有，需要执行添加操作
            orderSettingMapper.add(orderSetting);
        }
    }
}
