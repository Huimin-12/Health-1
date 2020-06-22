package cn.service.impl;

import cn.dao.MemberMapper;
import cn.dao.OrderMapper;
import cn.service.ReportService;
import cn.utils.DateUtils;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Override
    public Map<String, Object> getBusinessReportData() throws Exception {
        //创建出一个map集合
        Map<String, Object> map = new HashMap<>();
        //当前报表日期
        String reportDate = DateUtils.parseDate2String(new Date());
        //查询今日会员数
        Integer todayNewMember = memberMapper.findMemberCountByDate(reportDate);
        //查询总会员数
        Integer totalMember = memberMapper.findMemberTotalCount();
        //获取本周新增会员数
        Date  weekMonday= DateUtils.getThisWeekMonday();//获取本周一的日期
        String thisWeekMonday = DateUtils.parseDate2String(weekMonday);
        Integer thisWeekNewMember = memberMapper.findMemberCountAfterDate(thisWeekMonday);
        //获取本月新增会员数
        String firstDay4ThisMonth = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());//获取本月一号的日期
        Integer thisMonthNewMember = memberMapper.findMemberCountAfterDate(firstDay4ThisMonth);
        //获取今日预约数
        Integer todayOrderNumber = orderMapper.findOrderCountByDate(reportDate);
        //获取今日到诊数
        Integer todayVisitsNumber = orderMapper.findVisitsCountByDate(reportDate);
        //获取本周预约数
        Integer thisWeekOrderNumber = orderMapper.findOrderCountAfterDate(thisWeekMonday);
        //获取本周到诊数
        Integer thisWeekVisitsNumber = orderMapper.findVisitsCountAfterDate(thisWeekMonday);
        //获取本月预约数
        Integer thisMonthOrderNumber = orderMapper.findOrderCountAfterDate(firstDay4ThisMonth);
        //获取本月到诊数
        Integer thisMonthVisitsNumber = orderMapper.findVisitsCountAfterDate(firstDay4ThisMonth);
        //获取热门套餐
        List<Map> hotSetmeal = orderMapper.findHotSetmeal();

        map.put("reportDate", reportDate);//当前日期
        map.put("todayNewMember",todayNewMember);//今日会员数
        map.put("totalMember", totalMember);//总会员数
        map.put("thisWeekNewMember",thisWeekNewMember);//本周新增会员数
        map.put("thisMonthNewMember", thisMonthNewMember);//本月新增会员数
        map.put("todayOrderNumber", todayOrderNumber);//今日预约数
        map.put("todayVisitsNumber", todayVisitsNumber);//今日到诊数
        map.put("thisWeekOrderNumber", thisWeekOrderNumber);//本周预约数
        map.put("thisWeekVisitsNumber", thisWeekVisitsNumber);//本周到诊数
        map.put("thisMonthOrderNumber", thisMonthOrderNumber);//本月预约数
        map.put("thisMonthVisitsNumber", thisMonthVisitsNumber);//本月到诊数
        map.put("hotSetmeal", hotSetmeal);//热门套餐
        return map;
    }
}
