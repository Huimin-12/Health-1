package cn.controller;

import cn.constant.MessageConstant;
import cn.entity.Result;
import cn.service.MemberService;
import cn.service.ReportService;
import cn.service.SetmealService;
import cn.utils.DateUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.io.File;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/report")
public class ReportController {
    @Reference
    private MemberService memberService;
    @Reference
    private SetmealService setmealService;
    @Reference
    private ReportService reportService;
    //用户会员数统计表
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
    //套餐图形数据展示
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
    // 运营数据统计
    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData(){

        try {
            Map<String,Object> dataMap = reportService.getBusinessReportData();
            return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS,dataMap);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    //到处excel
    @RequestMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request,HttpServletResponse response){

        try {
            Map<String, Object> result = reportService.getBusinessReportData();
            //获取集合当中的数据
            //取出返回结果数据，准备将报表数据写入到Excel文件中
            String reportDate = (String) result.get("reportDate");
            Integer todayNewMember = (Integer) result.get("todayNewMember");
            Integer totalMember = (Integer) result.get("totalMember");
            Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");

            //获取模板文件绝对路径
            String filePast = request.getSession().getServletContext().getRealPath("template")+ File.separator+"report_template.xlsx";
            //读取excel模板文件
            XSSFWorkbook excel = new XSSFWorkbook(filePast);
            //获取表
            XSSFSheet sheet = excel.getSheetAt(0);

            XSSFRow row = sheet.getRow(2);//获取行
            row.getCell(5).setCellValue(reportDate);//获取单元格，并存入数据
            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);//总会员数

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数

            int rowNum = 12;
            for(Map map : hotSetmeal){//热门套餐
                String name = (String) map.get("name");
                Long setmeal_count = (Long) map.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map.get("proportion");
                row = sheet.getRow(rowNum ++);
                row.getCell(4).setCellValue(name);//套餐名称
                row.getCell(5).setCellValue(setmeal_count);//预约数量
                row.getCell(6).setCellValue(proportion.doubleValue());//占比
            }

            //创建输出流，用于服务器写数据到客户端浏览器
            OutputStream out = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");
            excel.write(out);

            out.flush();
            out.close();
            excel.close();

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }

    }
}
