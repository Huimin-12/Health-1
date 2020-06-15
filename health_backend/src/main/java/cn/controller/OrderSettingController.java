package cn.controller;

import cn.constant.MessageConstant;
import cn.domain.OrderSetting;
import cn.entity.Result;
import cn.service.OrderSettingService;
import cn.utils.POIUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 预约设置
 */
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {
    @Reference
    private OrderSettingService orderSettingService;
    /**
     * 接收文件，并解析文件内容保存到数据库
     * @param excelFile
     * @return
     */
    @RequestMapping("/upload")
    public Result upload(@RequestParam("excelFile") MultipartFile excelFile){

        try {
            List<String[]> strings = POIUtils.readExcel(excelFile);
            if (strings!=null && strings.size()>0){
                //创建一个list集合，用于批量插入数据
                List<OrderSetting> orderSettingList = new ArrayList<>();
                //遍历
                for (String[] string : strings) {
                    OrderSetting orderSetting = new OrderSetting(new Date(string[0]), Integer.parseInt(string[1]));
                    //添加到list集合当中
                    orderSettingList.add(orderSetting);
                }
                //调用service当中的方法，进行文件内容的传递
                orderSettingService.add(orderSettingList);
            }
            return new Result(true,MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }

    }

    /**
     * 根据日期查询预约设置的数据
     * @return
     */
    @RequestMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String data){ //参数传过来是 2020-06
        try {
            List<Map> mapList = orderSettingService.getOrderSettingByMonth(data);
            return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,mapList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }

    /**
     * 根据指定的日期修改预约人数
     * @param orderSetting
     * @return
     */
    @RequestMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting){

        try {
            orderSettingService.editNumberByDate(orderSetting);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }
        return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
    }
}
