package com.company.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.company.constant.MessageConstant;
import com.company.entity.Result;
import com.company.service.MemberService;
import com.company.service.ReportService;
import com.company.service.SetmealService;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/report")
public class ReportController {
    @Reference
    MemberService memberService;
   @Reference
    SetmealService setmealService;


    @Reference
    ReportService reportService;

    /**
     * 会员数量统计
     *
     */
    @RequestMapping("/getMemberReport")
    public Result getMemberReport(){
        Map<String,Object> map=new HashMap<String, Object>();
        List<String> months=new ArrayList<String>();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,-12);//获取当前日期的前第12个月的日期
        for (int i = 0; i < 12; i++) {
            calendar.add(Calendar.MONTH,1);
            months.add(new SimpleDateFormat("yyyy-MM").format(calendar.getTime()));
        }
        map.put("months",months);//前12个月月份
        List<Integer> memberCount=memberService.getMemberCountByMonth(months);
        map.put("memberCount",memberCount);//前12个月的会员数量

        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
    }


    @RequestMapping("/getSetmealReport.do")
    public Result getSetmealReport(){
        Map<String,Object> map=new HashMap<String, Object>();
        //套餐的名字和每个套餐的数量
        List<Map> listMap= setmealService.querySetmeal();


        List<String> setmealNames=new ArrayList<String>();//所有的套餐名字
        for (Map map1 : listMap) {
            String setmealName= map1.get("name").toString();
            setmealNames.add(setmealName);
        }
        map.put("setmealNames",setmealNames);
        map.put("setmealCount",listMap);
        return new Result(true,"统计套餐的占比成功",map);
    }


    // 运营数据统计（页面）
    @RequestMapping(value = "/getBusinessReportData")
    public Result getBusinessReportData(){
        try {
            Map<String,Object> map = reportService.getBusinessReportData();
            // 成功
            return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            // 成功
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }


    /**
     * 导出Excel报表
     * @return
     */
    @RequestMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response) {
        try {
            //远程调用报表服务获取报表数据
            Map<String, Object> result = reportService.getBusinessReportData();

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

            //获得Excel模板文件绝对路径
            //file.separator这个代表系统目录中的间隔符，说白了就是斜线。
            String temlateRealPath = request.getSession().getServletContext().getRealPath("template") +
                    File.separator + "report_template.xlsx";

            //读取模板文件创建Excel表格对象
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(temlateRealPath)));
            XSSFSheet sheet = workbook.getSheetAt(0);

            XSSFRow row = sheet.getRow(2);
            row.getCell(5).setCellValue(reportDate);//日期

            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);//总会员数

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日出游数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周出游数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月出游数

            int rowNum = 12;
            for (Map map : hotSetmeal) {//热门套餐
                String name = (String) map.get("name");
                Long setmeal_count = (Long) map.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map.get("proportion");
                row = sheet.getRow(rowNum++);
                row.getCell(4).setCellValue(name);//套餐名称
                row.getCell(5).setCellValue(setmeal_count);//预约数量
                row.getCell(6).setCellValue(proportion.doubleValue());//占比
            }

            //通过输出流进行文件下载
            ServletOutputStream out = response.getOutputStream();
            // 下载的数据类型（excel类型）
            response.setContentType("application/vnd.ms-excel");
            // 设置下载形式(通过附件的形式下载)
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");
            workbook.write(out);

            out.flush();
            out.close();
            workbook.close();

            return null;
        } catch (Exception e) {
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL, null);
        }

    }
}
