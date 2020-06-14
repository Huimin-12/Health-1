package cn;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.IOException;

public class POITest {
   // @Test
    public void test1() throws IOException {
        //创建工作蒲
        XSSFWorkbook workbook = new XSSFWorkbook("F:\\private\\最新学校名单(8)(2).xlsx");
        //获取工作表
        XSSFSheet sheetAt = workbook.getSheetAt(0);
        //遍历工作表，获得每一行
        for (Row row : sheetAt) {
            //遍历每一行，获取每一个单元格
            for (Cell cell : row) {
                System.out.println(cell.getStringCellValue());
            }
        }
    }
}
