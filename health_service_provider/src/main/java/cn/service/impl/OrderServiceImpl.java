package cn.service.impl;

import cn.constant.MessageConstant;
import cn.dao.MemberMapper;
import cn.dao.OrderMapper;
import cn.dao.OrderSettingMapper;
import cn.domain.Member;
import cn.domain.Order;
import cn.domain.OrderSetting;
import cn.entity.Result;
import cn.service.OrderService;
import cn.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderSettingMapper orderSettingMapper;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private OrderMapper orderMapper;

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
        if (reservations >= number) {
            //说明当前日期可预约人数已满
            return new Result(false, MessageConstant.ORDER_FULL);
        }
//        3、检查用户是否重复预约（同一个用户在同一天预约了同一个套餐),如果是重复预约 则无法完成再次预约
        //根据手机号来确定是否是会员
        String telephone = (String) map.get("telephone");
        Member member = memberMapper.findByTelephone(telephone);
        if (member != null) {
            //判断用户是否重复预约
            Integer memberId = member.getId();//获取用户的会员id
            Date order_Date = DateUtils.parseString2Date(orderDate);//获取日期
            String setmealId = (String) map.get("setmealId");//套餐id
            //根据这三个条件封装一个order对象，用于查询
            Order order = new Order(memberId, order_Date, Integer.parseInt(setmealId));
            //调用orderMapper进行数据库查询
            List<Order> orderList = orderMapper.findByCondition(order);
            if (orderList != null && orderList.size() > 0) {
                //判断成立说明根据上面的信息查询到了数据，用户正在重复提交数据,直接返回提示信息
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
        }
//        4、检查当前用户是否为会员，如果是会员则直接完成预约，如果不是会员则自动完成注 册并进行预约
        //调用orderSetting方法，当前预约人数进行+1
        orderSetting.setReservations(orderSetting.getReservations() + 1);
        orderSettingMapper.editNumberByOrderDate(orderSetting);

        if (member == null) {
            //当前用户不是会员，需要添加到会员列表
            member = new Member();
            member.setName((String) map.get("name"));
            member.setSex((String) map.get("sex"));
            member.setPhoneNumber(telephone);
            member.setIdCard((String) map.get("idCard"));
            member.setRegTime(new Date());
            //调用添加方法，进行数据的添加
            memberMapper.add(member);
        }
//        5、预约成功，更新当日的已预约人数
        Order order = new Order(member.getId(), date, (String) map.get("orderType"), Order.ORDERSTATUS_NO, Integer.parseInt((String) map.get("setmealId")));
        orderMapper.add(order);
        return new Result(true,MessageConstant.ORDER_SUCCESS,order.getId());
    }
    //根据用户的id进行预约信息的查询
    @Override
    public Map findById(int id) throws Exception {
        Map map = orderMapper.findById4Detail(id);

        if (map!=null){
            //处理日期格式，转换为字符串格式返回
            Date orderDate = (Date) map.get("orderDate");
            map.put("orderDate",DateUtils.parseDate2String(orderDate));
        }
        return map;
    }


}
