package cn.service.impl;

import cn.dao.OrderSettingMapper;
import cn.domain.OrderSetting;
import cn.service.OrderSettingService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
}
