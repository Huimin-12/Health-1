package cn.controller;

import cn.constant.MessageConstant;
import cn.domain.Order;
import cn.entity.Result;
import cn.service.OrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Reference
    private OrderService orderService;

    /**
     * 根据id进行用户预约后的个人信息查询
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(int id){

        try {
           Map map = orderService.findById(id);
           //查询预约信息成功
           return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            //查询预约信息失败
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}
