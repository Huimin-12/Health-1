package cn.controller;

import cn.constant.MessageConstant;
import cn.entity.Result;
import cn.service.MemberService;
import cn.service.SetmealService;
import cn.utils.DateUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/report")
public class ReportController {
    @Reference
    private MemberService memberService;
    @Reference
    private SetmealService setmealService;
    @RequestMapping("/getMemberReport")
   public Result getMemberReport() throws Exception{

        Calendar calendar = Calendar.getInstance();//获得日历对象。获得当前日期时间
        calendar.add(Calendar.MONTH,-12);//获得当前时间，往前推12个月
        Map<String,Object> map = new HashMap<>();
        List<String> months = new ArrayList<>();
        //遍历
        for (int i = 0; i < 12; i++) {
            calendar.add(Calendar.MONTH,1);//每遍历一次月份就往后+1月
            Date date = calendar.getTime();
            //使用日期转换格式进行日期的转换
            months.add(new SimpleDateFormat("yyyy.MM").format(date));
        }
        map.put("months",months);

        List<Integer> memberCount = memberService.findMemberCountByMonth(months);
        map.put("memberCount",memberCount);
        return new Result(true,MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
    }

    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport(){
        //查询出 每种套餐数量 和套餐名字
        List<Map<String,Object>> list = setmealService.findSetmealCount();
        Map<String,Object> map = new HashMap<>();
        //把查询出来的数据封装到map当中
        map.put("setmealCount",list);

        List<String> setmealNames = new ArrayList<>();
        for (Map<String, Object> objectMap : list) {
            String name = (String) objectMap.get("name");
            //遍历出来的名字，获取到之后存储到list集合当中
            setmealNames.add(name);
        }
        map.put("setmealNames",setmealNames);
        return new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,map);
    }
}
