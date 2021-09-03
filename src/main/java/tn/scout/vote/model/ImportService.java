package tn.scout.vote.model;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Repository;

@Repository
public class ImportService {
	 private final static String excel2003 =".xls";
	    private final static String excel2007 =".xlsx";
	 
	    /**
	     * @param in
	     * @param fileName
	           * Process the uploaded excel file
	     *
	    * */
	    public  List<List<Object>> getBankListByExcel(InputStream in, String fileName) throws Exception{
	        List<List<Object>> list = null;
	                 //Create Excel workbook
	        Workbook work = this.getWorkbook(in,fileName);
	        if(null == work){
	                         throw new Exception("Create Excel workbook is empty!");
	        }
	        Sheet sheet = null;
	        Row row = null;
	        Cell cell = null;
	 
	        list = new ArrayList<List<Object>>();
	        for (int i = 0; i < work.getNumberOfSheets(); i++) {
	            sheet = work.getSheetAt(i);
	            if(sheet==null){continue;}
	 
	            for (int j = sheet.getFirstRowNum(); j <= sheet.getLastRowNum(); j++) {
	                row = sheet.getRow(j);
	                if(row==null||row.getFirstCellNum()==j){continue;}
	 
	                List<Object> li = new ArrayList<Object>();
	                for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
	                    cell = row.getCell(y);
	                    li.add(cell);
	                }
	                list.add(li);
	            }
	        }
	//        work.;
	        return list;
	    }
	         //Judging the format of the excel file
	    public  Workbook getWorkbook(InputStream inStr,String fileName) throws Exception{
	        Workbook wb = null;
	        String fileType = fileName.substring(fileName.lastIndexOf("."));
	        if(excel2003.equals(fileType)){
	            wb = new HSSFWorkbook(inStr);
	        }else if(excel2007.equals(fileType)){
	            wb = new XSSFWorkbook(inStr);
	        }else{
	                         throw new Exception("The format of the parsed file is wrong!");
	        }
	        return wb;
	    }

}
