package cn.jasperreports;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JasperReportsTest {

    //基于jdbc数据源方式填充数据
    @Test
    public void test1() throws Exception{
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection =
                DriverManager.getConnection("jdbc:mysql://localhost:3306/user",
                        "root",
                        "root");
        String jrxmlPath = "E:\\IdeaProjects\\health_parent\\jasperreportsDemo\\src\\main\\resources\\demo1.jrxml";
        String jasperPath = "E:\\IdeaProjects\\health_parent\\jasperreportsDemo\\src\\main\\resources\\demo1.jasper";

        //模板编译，编译为后缀为jasper的二进制文件
        JasperCompileManager.compileReportToFile(jrxmlPath,jasperPath);

        //为模板文件准备数据，用于最终的PDF文件数据填充
        Map map = new HashMap();
        map.put("company","惠民集团");

        //填充数据
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperPath,map,connection);

        //输出文件
        String pdfPath = "D:\\test.pdf";
        JasperExportManager.exportReportToPdfFile(jasperPrint,pdfPath);
    }
    //基于Javabean数据源方式填充数据
    @Test
    public void test2() throws Exception{
        String jrxmlPath = "E:\\IdeaProjects\\health_parent\\jasperreportsDemo\\src\\main\\resources\\demo2.jrxml";
        String jasperPath = "E:\\IdeaProjects\\health_parent\\jasperreportsDemo\\src\\main\\resources\\demo2.jasper";

        //模板编译，编译为后缀为jasper的二进制文件
        JasperCompileManager.compileReportToFile(jrxmlPath,jasperPath);

        //为模板文件准备数据，用于最终的PDF文件数据填充
        Map map = new HashMap();
        map.put("company","solo");

        //Javabean数据源填充，用于填充列表数据
        List<Map> list = new ArrayList();
        Map map1 = new HashMap();
        map1.put("name","入职体检套餐");
        map1.put("code","RZTJ");
        map1.put("age","18-60");
        map1.put("sex","男");

        Map map2 = new HashMap();
        map2.put("name","阳光爸妈老年健康体检");
        map2.put("code","YGBM");
        map2.put("age","55-60");
        map2.put("sex","女");
        list.add(map1);
        list.add(map2);

        //填充数据
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperPath,map,new JRBeanCollectionDataSource(list));

        //输出文件
        String pdfPath = "D:\\test2.pdf";
        JasperExportManager.exportReportToPdfFile(jasperPrint,pdfPath);
    }
}
