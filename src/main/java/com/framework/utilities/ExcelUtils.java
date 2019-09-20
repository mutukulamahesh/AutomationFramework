package com.framework.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.sl.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/**
 * <p>
 * <b> This Class provides the driver code for connecting with excel sheets .</b>
 * @author mmutukul
 * </p>
 */
public class ExcelUtils {
    public static String path;
    public static FileInputStream fis = null;
    public static FileOutputStream fileOut = null;
    private static XSSFWorkbook workbook = null;
    private static XSSFSheet sheet = null;
    private static XSSFRow row = null;
    private static XSSFCell cell = null;

    public static void setExcelFile(final String path) {

        ExcelUtils.path = path;
        try {
            ExcelUtils.fis = new FileInputStream(path);
            ExcelUtils.workbook = new XSSFWorkbook(ExcelUtils.fis);
            ExcelUtils.sheet = ExcelUtils.workbook.getSheetAt(0);
            System.out.println("excel intiated.." +path);
            ExcelUtils.fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static int getColumnCount(final String sheetName) {
        if (!isSheetExist(sheetName)) {
            return -1;
        }

        ExcelUtils.sheet = ExcelUtils.workbook.getSheet(sheetName);
        ExcelUtils.row = ExcelUtils.sheet.getRow(0);

        if (ExcelUtils.row == null) {
            return -1;
        }

        return ExcelUtils.row.getLastCellNum();


    }

    public static int getRowCount(final String sheetName) {
        int index = ExcelUtils.workbook.getSheetIndex(sheetName);
        if (index == -1) {
            return 0;
        } else {
            ExcelUtils.sheet = ExcelUtils.workbook.getSheetAt(index);
            int number = ExcelUtils.sheet.getLastRowNum() + 1;
            return number;
        }

    }

    public int getCellRowNum(final String sheetName, final String colName, final String cellValue) {

        for (int i = 2; i <= getRowCount(sheetName); i++) {
            if (getCellData(sheetName, colName, i).equalsIgnoreCase(cellValue)) {
                return i;
            }
        }
        return -1;

    }

    public static String getCellData(final String sheetName, final String colName, final int rowNum) {
        try {
            if (rowNum <= 0) {
                return "";
            }

            int index = ExcelUtils.workbook.getSheetIndex(sheetName);
            int col_Num = -1;
            if (index == -1) {
                return "";
            }

            ExcelUtils.sheet = ExcelUtils.workbook.getSheetAt(index);
            ExcelUtils.row = ExcelUtils.sheet.getRow(0);
            for (int i = 0; i < ExcelUtils.row.getLastCellNum(); i++) {
                if (ExcelUtils.row.getCell(i).getStringCellValue().trim().equals(colName.trim())) {
                    col_Num = i;
                }
            }
            if (col_Num == -1) {
                return "";
            }

            ExcelUtils.sheet = ExcelUtils.workbook.getSheetAt(index);
            ExcelUtils.row = ExcelUtils.sheet.getRow(rowNum - 1);
            if (ExcelUtils.row == null) {
                return "";
            }
            ExcelUtils.cell = ExcelUtils.row.getCell(col_Num);

            if (ExcelUtils.cell == null) {
                return "";
            }
            if (ExcelUtils.cell.getCellType() == Cell.CELL_TYPE_STRING) {
                return ExcelUtils.cell.getStringCellValue();
            } else if (ExcelUtils.cell.getCellType() == Cell.CELL_TYPE_NUMERIC
                || ExcelUtils.cell.getCellType() == Cell.CELL_TYPE_FORMULA) {

                String cellText = String.valueOf(ExcelUtils.cell.getNumericCellValue());
                if (DateUtil.isCellDateFormatted(ExcelUtils.cell)) {
                    // format in form of M/D/YY
                    double d = ExcelUtils.cell.getNumericCellValue();

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(DateUtil.getJavaDate(d));
                    cellText = (String.valueOf(cal.get(Calendar.YEAR))).substring(2);
                    cellText = cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + 1 + "/" + cellText;
                }

                return cellText;
            } else if (ExcelUtils.cell.getCellType() == Cell.CELL_TYPE_BLANK) {
                return "";
            } else {
                return String.valueOf(ExcelUtils.cell.getBooleanCellValue());
            }

        } catch (Exception e) {

            e.printStackTrace();
            return "row " + rowNum + " or column " + colName + " does not exist in xls";
        }
    }

    public String getCellData(final String sheetName, final int colNum, final int rowNum) {
        try {
            if (rowNum <= 0) {
                return "";
            }

            int index = ExcelUtils.workbook.getSheetIndex(sheetName);

            if (index == -1) {
                return "";
            }


            ExcelUtils.sheet = ExcelUtils.workbook.getSheetAt(index);
            ExcelUtils.row = ExcelUtils.sheet.getRow(rowNum - 1);
            if (ExcelUtils.row == null) {
                return "";
            }
            ExcelUtils.cell = ExcelUtils.row.getCell(colNum);
            if (ExcelUtils.cell == null) {
                return "";
            }

            if (ExcelUtils.cell.getCellType() == Cell.CELL_TYPE_STRING) {
                return ExcelUtils.cell.getStringCellValue();
            } else if (ExcelUtils.cell.getCellType() == Cell.CELL_TYPE_NUMERIC
                || ExcelUtils.cell.getCellType() == Cell.CELL_TYPE_FORMULA) {

                String cellText = String.valueOf(ExcelUtils.cell.getNumericCellValue());
                if (DateUtil.isCellDateFormatted(ExcelUtils.cell)) {
                    // format in form of M/D/YY
                    double d = ExcelUtils.cell.getNumericCellValue();

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(DateUtil.getJavaDate(d));
                    cellText = (String.valueOf(cal.get(Calendar.YEAR))).substring(2);
                    cellText = cal.get(Calendar.MONTH) + 1 + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cellText;
                }


                return cellText;
            } else if (ExcelUtils.cell.getCellType() == Cell.CELL_TYPE_BLANK) {
                return "";
            } else {
                return String.valueOf(ExcelUtils.cell.getBooleanCellValue());
            }
        } catch (Exception e) {

            e.printStackTrace();
            return "row " + rowNum + " or column " + colNum + " does not exist  in xls";
        }
    }

    public static boolean setCellData(final String sheetName, final String colName, final int rowNum, final String data) {
        try {
            ExcelUtils.fis = new FileInputStream(ExcelUtils.path);
            ExcelUtils.workbook = new XSSFWorkbook(ExcelUtils.fis);

            if (rowNum <= 0) {
                return false;
            }

            int index = ExcelUtils.workbook.getSheetIndex(sheetName);
            int colNum = -1;
            if (index == -1) {
                return false;
            }


            ExcelUtils.sheet = ExcelUtils.workbook.getSheetAt(index);


            ExcelUtils.row = ExcelUtils.sheet.getRow(0);
            for (int i = 0; i < ExcelUtils.row.getLastCellNum(); i++) {
                if (ExcelUtils.row.getCell(i).getStringCellValue().trim().equals(colName)) {
                    colNum = i;
                }
            }
            if (colNum == -1) {
                return false;
            }

            ExcelUtils.sheet.autoSizeColumn(colNum);
            ExcelUtils.row = ExcelUtils.sheet.getRow(rowNum - 1);
            if (ExcelUtils.row == null) {
                ExcelUtils.row = ExcelUtils.sheet.createRow(rowNum - 1);
            }

            ExcelUtils.cell = ExcelUtils.row.getCell(colNum);
            if (ExcelUtils.cell == null) {
                ExcelUtils.cell = ExcelUtils.row.createCell(colNum);
            }

            ExcelUtils.cell.setCellValue(data);

            ExcelUtils.fileOut = new FileOutputStream(ExcelUtils.path);

            ExcelUtils.workbook.write(ExcelUtils.fileOut);

            ExcelUtils.fileOut.close();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public boolean setCellData(final String sheetName, final String colName, final int rowNum, final String data,
        final String url) {

        try {
            ExcelUtils.fis = new FileInputStream(ExcelUtils.path);
            ExcelUtils.workbook = new XSSFWorkbook(ExcelUtils.fis);

            if (rowNum <= 0) {
                return false;
            }

            int index = ExcelUtils.workbook.getSheetIndex(sheetName);
            int colNum = -1;
            if (index == -1) {
                return false;
            }


            ExcelUtils.sheet = ExcelUtils.workbook.getSheetAt(index);

            ExcelUtils.row = ExcelUtils.sheet.getRow(0);
            for (int i = 0; i < ExcelUtils.row.getLastCellNum(); i++) {
                if (ExcelUtils.row.getCell(i).getStringCellValue().trim().equalsIgnoreCase(colName)) {
                    colNum = i;
                }
            }

            if (colNum == -1) {
                return false;
            }
            ExcelUtils.sheet.autoSizeColumn(colNum);
            ExcelUtils.row = ExcelUtils.sheet.getRow(rowNum - 1);
            if (ExcelUtils.row == null) {
                ExcelUtils.row = ExcelUtils.sheet.createRow(rowNum - 1);
            }

            ExcelUtils.cell = ExcelUtils.row.getCell(colNum);
            if (ExcelUtils.cell == null) {
                ExcelUtils.cell = ExcelUtils.row.createCell(colNum);
            }

            ExcelUtils.cell.setCellValue(data);
            XSSFCreationHelper createHelper = ExcelUtils.workbook.getCreationHelper();
            CellStyle hlink_style = ExcelUtils.workbook.createCellStyle();
            XSSFFont hlink_font = ExcelUtils.workbook.createFont();
            hlink_font.setUnderline(Font.U_SINGLE);
            hlink_font.setColor(IndexedColors.BLUE.getIndex());
            hlink_style.setFont(hlink_font);

            XSSFHyperlink link = createHelper.createHyperlink(Hyperlink.LINK_FILE);
            link.setAddress(url);
            ExcelUtils.cell.setHyperlink(link);
            ExcelUtils.cell.setCellStyle(hlink_style);

            ExcelUtils.fileOut = new FileOutputStream(ExcelUtils.path);
            ExcelUtils.workbook.write(ExcelUtils.fileOut);

            ExcelUtils.fileOut.close();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public static boolean isSheetExist(final String sheetName) {
        int index = ExcelUtils.workbook.getSheetIndex(sheetName);
        if (index == -1) {
            index = ExcelUtils.workbook.getSheetIndex(sheetName.toUpperCase());
            if (index == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
    
    public static int findRow(String sheetName, String value){
	    //to find row number so we can search through that specific column
	    int gotRow = 0;
	    sheet = ExcelUtils.workbook.getSheet(sheetName);
	    for (Row row: sheet){
	        for (Cell cell: row){
	            final DataFormatter df = new DataFormatter();
	            final XSSFCell cellVal = (XSSFCell) row.getCell(row.getRowNum());
	            String valueAsString = df.formatCellValue(cellVal);
	            if (valueAsString.trim().equals(value) ){
	                gotRow = row.getRowNum();
	            }
	        }
	    }
	    return gotRow;  
	}
    
    public static Sheet getSheet(String excelFilePath, String SheetName) {
		Workbook workBook = null;
		FileInputStream inputStream;
		try {
			inputStream = new FileInputStream(new File(excelFilePath));
			if (excelFilePath.endsWith("xlsx")) {
				workBook = new XSSFWorkbook(inputStream);
			} else if (excelFilePath.endsWith("xls")) {
				workBook = new HSSFWorkbook(inputStream);
			} else {
				inputStream.close();
				throw new IllegalArgumentException("The specified file " + excelFilePath + " is not Excel file");
			}
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return workBook.getSheet(SheetName);
	}
    
    public static int getColumnIndexByHeader(String excelFilePath, String SheetName, String headerValue) {
  		Sheet sheet;
  		int index = -1;
  		try {
  			sheet = getSheet(excelFilePath, SheetName);
  			Row headerRow = sheet.getRow(0);
  			Iterator<Cell> cellIterator = headerRow.cellIterator();
  			while (cellIterator.hasNext()) {
  				Cell cell = cellIterator.next();
  				if (cell.getStringCellValue().equals(headerValue)) {
  					index = cell.getColumnIndex();
  					break;
  				}
  			}
  		} catch (Exception e) {
  			//Logs.logger.error("Error reading " + excelFilePath);
  			e.printStackTrace();
  		}

  		return index;

  	}
    
    public static HashMap<String, String> getDataFromGivenColumns(String excelFilePath, String worksheetName,
			int col1Index, int col2Index) throws IOException {
		HashMap<String, String> excelData = new HashMap<String, String>();
		Sheet sheet = getSheet(excelFilePath, worksheetName);
		DataFormatter fmt = new DataFormatter();
		Iterator<?> rows = sheet.rowIterator();
		String key = "";
		String value = "";
		XSSFRow row;

		while (rows.hasNext()) {
			row = (XSSFRow) rows.next();
			String[] var = null;
//			for (int i = 0; i < row.getLastCellNum(); i++) {
				key = fmt.formatCellValue(row.getCell(col1Index)).trim();
				//getColumnIndexByHeader(excelFilePath, worksheetName, headerValue)
				value = fmt.formatCellValue(row.getCell(col2Index)).trim();

				if (value.contains(",")) {
					var = value.split(",");
				}
//			}

			if (!excelData.containsKey(key))
				if(value != ""){
					if (value.contains(",")) {
						for (int i = 0; i < var.length; i++) {
							excelData.put(key + i, var[i]);
						}
					} else {
						excelData.put(key, value);
					}
				}
			key = "";
			value = "";
		}
		//Logs.logger.info("Data read from excel " + excelData);
		return excelData;

	}
    
    public static HashMap<String, String> getDataFromGivenColumns(String excelFilePath, String worksheetName,
			int col1Index, String col2Index) throws IOException {
		HashMap<String, String> excelData = new HashMap<String, String>();
		Sheet sheet = getSheet(excelFilePath, worksheetName);
		DataFormatter fmt = new DataFormatter();
		Iterator<?> rows = sheet.rowIterator();
		String key = "";
		String value = "";
		XSSFRow row;

		while (rows.hasNext()) {
			row = (XSSFRow) rows.next();
			String[] var = null;
//			for (int i = 0; i < row.getLastCellNum(); i++) {
				key = fmt.formatCellValue(row.getCell(col1Index)).trim();
				int dataValue = getColumnIndexByHeader(excelFilePath, worksheetName, col2Index);
				value = fmt.formatCellValue(row.getCell(dataValue)).trim();

				if (value.contains(",")) {
					var = value.split(",");
				}
//			}

			if (!excelData.containsKey(key))
				if(value != ""){
					if (value.contains(",")) {
						for (int i = 0; i < var.length; i++) {
							excelData.put(key + i, var[i]);
						}
					} else {
						excelData.put(key, value);
					}
				}
			key = "";
			value = "";
		}
		//Logs.logger.info("Data read from excel " + excelData);
		return excelData;

	}
    
    public static HashMap<String, String> getRecordInSheetByRowIndex(String excelFilePath, String sheetName,
			Integer index) {
		HashMap<String, String> cellData = new HashMap<String, String>();

		Sheet sheet = getSheet(excelFilePath, sheetName);
		Row headerRow = sheet.getRow(0);
		Row row = sheet.getRow(index);
		if (row == null) {
			throw new NullPointerException("Specified Row Index Does not Exist in the Sheet");
		}
		Iterator<Cell> cellIterator = row.cellIterator();
		while (cellIterator.hasNext()) {
			Cell cell = cellIterator.next();
			// This is to deal with all types of Cell Data.
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_STRING:
				cellData.put(headerRow.getCell(cell.getColumnIndex()).getStringCellValue(), cell.getStringCellValue());
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				cellData.put(headerRow.getCell(cell.getColumnIndex()).getStringCellValue(),
						String.valueOf(cell.getBooleanCellValue()));
				break;
			case Cell.CELL_TYPE_NUMERIC:
				cellData.put(headerRow.getCell(cell.getColumnIndex()).getStringCellValue(),
						String.valueOf(cell.getNumericCellValue()));
				break;
			case Cell.CELL_TYPE_BLANK:
//				cellData.put(headerRow.getCell(cell.getColumnIndex()).getStringCellValue(),	String.valueOf(cell.getNumericCellValue()));
				break;
			}
		}

		return cellData;

	}
    
    public static HashMap<String, String> getDataByColoumn(String excelFilePath, String worksheetName, String key)
			throws Exception {
		HashMap<String, String> cellData = new HashMap<String, String>();
		int columnKeyIndex = getColumnIndexByHeader(excelFilePath, worksheetName, key);

		//Logs.logger.info("Data Set Column Number from data sheet: " + columnKeyIndex + 1);
		cellData = getDataFromGivenColumns(excelFilePath, worksheetName, columnKeyIndex,columnKeyIndex+1);
		return cellData;
	}
    
    public static HashMap<String, String> getDataByColoumn(String excelFilePath, String worksheetName, String key,String DataSet)
			throws Exception {
		HashMap<String, String> cellData = new HashMap<String, String>();
		int columnKeyIndex = getColumnIndexByHeader(excelFilePath, worksheetName, key);

		//Logs.logger.info("Data Set Column Number from data sheet: " + columnKeyIndex + 1);
		cellData = getDataFromGivenColumns(excelFilePath, worksheetName, columnKeyIndex,DataSet);
		return cellData;
	}
    
    public static HashMap<String, String> getDataByRow(String excelFile, String worksheetName, String key)
			throws Exception {
    	//String excelFilePath = OR.getConfig("Path_TestData") + excelFile + OR.getConfig("File_TestData");
    	String excelFilePath =  excelFile;//+ OR.getConfig("File_TestData");
    	System.out.println("excelFilePath: " +excelFilePath);
		HashMap<String, String> cellData = new HashMap<String, String>();
		Sheet sheet = getSheet(excelFilePath, worksheetName);
		Iterator<?> rows = sheet.rowIterator();
		String value = "";
		int rowIndex = 0;
		XSSFRow row;

		while (rows.hasNext()) {
			row = (XSSFRow) rows.next();
			value = row.getCell(0).getStringCellValue();
			if (value.equalsIgnoreCase(key)) {
				rowIndex = row.getRowNum();
				break;
			}
		}

		cellData = getRecordInSheetByRowIndex(excelFilePath, worksheetName, rowIndex);
		return cellData;
	}
    
    public static boolean setDataByRow(String excelFile, String worksheetName, String Script,String ColName, String data)
			throws Exception {
    	String excelFilePath = excelFile;// + OR.getConfig("File_TestData");
    	System.out.println("excelFilePath: " +excelFilePath);
		//HashMap<String, String> cellData = new HashMap<String, String>();
		Sheet sheet = getSheet(excelFilePath, worksheetName);
		Iterator<?> rows = sheet.rowIterator();
		String value = "";
		int rowIndex = 0;
		XSSFRow row;

		while (rows.hasNext()) {
			row = (XSSFRow) rows.next();
			value = row.getCell(0).getStringCellValue();
			if (value.equalsIgnoreCase(Script)) {
				rowIndex = row.getRowNum();
				break;
			}
		}
		setExcelFile(excelFilePath);
		setCellData(worksheetName, ColName, rowIndex+1, data);
		//cellData = getRecordInSheetByRowIndex(excelFilePath, worksheetName, rowIndex);
		return true;
	}
    
    public static String getCellData(String excelFile, String worksheetName,String Script,String ColName)
			throws Exception {
    	String excelFilePath = excelFile; //+ OR.getConfig("File_TestData");
    	System.out.println("excelFilePath: " +excelFilePath);
		//HashMap<String, String> cellData = new HashMap<String, String>();
		Sheet sheet = getSheet(excelFilePath, worksheetName);
		Iterator<?> rows = sheet.rowIterator();
		String value = "";
		int rowIndex = 0;
		XSSFRow row;

		while (rows.hasNext()) {
			row = (XSSFRow) rows.next();
			value = row.getCell(0).getStringCellValue();
			if (value.equalsIgnoreCase(Script)) {
				rowIndex = row.getRowNum();
				break;
			}
		}
		setExcelFile(excelFilePath);
		//setCellData(worksheetName, ColName, rowIndex+1, data);
		String data = getCellData(worksheetName, ColName, rowIndex+1);
		//cellData = getRecordInSheetByRowIndex(excelFilePath, worksheetName, rowIndex);
		return data;
	}


}
