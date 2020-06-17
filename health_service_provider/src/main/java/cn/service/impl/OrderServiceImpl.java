package cn.service.impl;

import cn.constant.MessageConstant;
import cn.dao.OrderSettingMapper;
import cn.domain.OrderSetting;
import cn.entity.Result;
import cn.service.OrderService;
import cn.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Map;

public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderSettingMapper orderSettingMapper;

    @Override
    public Result order(Map map) throws Exception {

//        1、检查用户所选择的预约日期是否已经提前进行了预约设置，如果没有设置则无法进行 预约
        String orderDate = (String) map.get("orderDate");
        //调用date工具类将日期字符串转换成date
        Date date = DateUtils.parseString2Date(orderDate);
        OrderSetting orderSetting = orderSettingMapper.findByOrderDate(date);
        if (orderSetting == null) {
            //说明当前日期不可以进行预约
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
//        2、检查用户所选择的预约日期是否已经约满，如果已经约满则无法预约
        int number = orderSetting.getNumber();//可预约人数
        int reservations = orderSetting.getReservations();//已预约人数
        if (reservations>=number){
            //说明当前日期可预约人数已满
            return new Result(false,MessageConstant.ORDER_FULL);
        }
//        3、检查用户是否重复预约（同一个用户在同一天预约了同一个套餐),如果是重复预约 则无法完成再次预约

//        4、检查当前用户是否为会员，如果是会员则直接完成预约，如果不是会员则自动完成注 册并进行预约

//        5、预约成功，更新当日的已预约人数

        return null;
    }
}
