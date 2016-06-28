import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;


public class ExchangeRate {

    private double dollar;
    private double euro;
    private double hk;



    //下载人民币汇率数据，存入Data.xls

    public void getWebData() {
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        URLConnection urlConnection = null;

        Calendar calendar = Calendar.getInstance();
        Date endDate = calendar.getTime();
        calendar.add(Calendar.DATE,-30);
        Date startDate = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String startDateStr = simpleDateFormat.format(startDate);
        String endDateStr = simpleDateFormat.format(endDate);
        

        //发送“中国外汇管理局”的导出表单请求
        String url = "http://www.safe.gov.cn/AppStructured/view/project_exportRMBExcel.action?projectBean.startDate="
                + startDateStr + "projectBean.endDate=" + endDateStr + "queryYN=true";
        System.out.println(url);
        try {
            urlConnection = new URL(url).openConnection();
            inputStream = urlConnection.getInputStream();
            fileOutputStream = new FileOutputStream("Data.xls");

            byte [] bytes = new byte[1024];
            int len = -1;
            while ((len = inputStream.read(bytes)) != -1) {
                fileOutputStream.write(bytes,0,len);
                fileOutputStream.flush();
            }
            inputStream.close();
            fileOutputStream.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //从Data.xls中读入数据，进行计算
    public void readExcel() {
        Workbook workbook = null;
        try {
            InputStream inputStream = new FileInputStream("Data.xls");
            workbook = Workbook.getWorkbook(inputStream);
            Sheet sheet = workbook.getSheet(0);
            int rows = sheet.getRows();

            //计算美元、欧元、港元汇率
            for (int i = 1; i < rows; ++i) {
                Cell dollar_cell = sheet.getCell(1,i);
                dollar += Double.parseDouble(dollar_cell.getContents());

                Cell euro_cell = sheet.getCell(2,i);
                euro += Double.parseDouble(euro_cell.getContents());

                Cell hk_cell = sheet.getCell(4,i);
                hk += Double.parseDouble(hk_cell.getContents());
            }
            dollar = dollar/rows;
            euro = euro/rows;
            hk = hk/rows;

            inputStream.close();
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //结果输出

    public void writeExcel() {
        try {
            OutputStream os = new FileOutputStream(new File("result.xls"));
            WritableWorkbook writableWorkbook = Workbook.createWorkbook(os);
            WritableSheet writableSheet = writableWorkbook.createSheet("人民币汇率分析",0);

            Label jieshao = new Label(0,0,"人民币汇率中间价");
            writableSheet.addCell(jieshao);
            Label meiyuan = new Label(1,0,"美元");
            writableSheet.addCell(meiyuan);
            Label ouyuan = new Label(2,0,"欧元");
            writableSheet.addCell(ouyuan);
            Label gangyuan = new Label(3,0,"港元");
            writableSheet.addCell(gangyuan);

            Label l_dollar = new Label(1,1,""+dollar);
            writableSheet.addCell(l_dollar);
            Label l_euro = new Label(2,1,""+euro);
            writableSheet.addCell(l_euro);
            Label l_hk = new Label(3,1,""+hk);
            writableSheet.addCell(l_hk);

            writableWorkbook.write();
            writableWorkbook.close();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        ExchangeRate er = new ExchangeRate();
        er.getWebData();
        er.readExcel();
        er.writeExcel();
    }
}
