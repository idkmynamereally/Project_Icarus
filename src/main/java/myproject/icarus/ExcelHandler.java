package myproject.icarus;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

class ExcelConstValues
{
    static String[] videoDataCols = {"VideoTitle", "ChannelTitle", "VideoID", "UploadDate", "Likes", "Views", "CommentCount"};
    static String[] commentCols = {"Comment", "Author Name", "Likes"};
}

class CellStyleOptions
{
    public CellStyleOptions(XSSFWorkbook workbook)
    {
        columnHeader = workbook.createCellStyle();
        regularCell = workbook.createCellStyle();

        columnHeader.setFillForegroundColor(IndexedColors.GOLD.getIndex());
        columnHeader.setBorderRight(BorderStyle.THICK);
        columnHeader.setBorderLeft(BorderStyle.THICK);
        columnHeader.setBorderTop(BorderStyle.THICK);
        columnHeader.setBorderBottom(BorderStyle.THICK);
        columnHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        regularCell.setFillForegroundColor(IndexedColors.TURQUOISE.getIndex());
        regularCell.setBorderRight(BorderStyle.THIN);
        regularCell.setBorderTop(BorderStyle.THIN);
        regularCell.setBorderLeft(BorderStyle.THIN);
        regularCell.setBorderBottom(BorderStyle.THIN);
        regularCell.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }

    CellStyle columnHeader;
    CellStyle regularCell;
}

/**
 * Originally meant to output data to a Excel file currently not in use. Likely to merge into OutputHandler.
 */
public class ExcelHandler
{
    public static XSSFSheet initExcelSheetForVideoData(XSSFWorkbook workbook)
    {
        XSSFSheet sheet = workbook.createSheet("VideoData");
        XSSFRow rowIt = sheet.createRow(0);
        CellStyleOptions cellStyleOptions = new CellStyleOptions(workbook); //CellStyle Options
        String[] colms = ExcelConstValues.videoDataCols;    //Column Names
        for (int i = 0; i < ExcelConstValues.videoDataCols.length; i++)
        {
            sheet.setColumnWidth(i, 20 * 256);          //Widens The Column One by One
            Cell c = rowIt.createCell(i);
            c.setCellValue(colms[i]);
            c.setCellStyle(cellStyleOptions.columnHeader);
        }
        return sheet;
    }

    public static XSSFSheet initExcelSheetForComments(XSSFWorkbook workbook)
    {
        return initExcelSheetForComments(workbook, "Default");
    }
    
    public static XSSFSheet initExcelSheetForComments(XSSFWorkbook workbook, String videoName)
    {
        XSSFSheet sheet = workbook.createSheet(videoName);
        XSSFRow row = sheet.createRow(0);
        for (int i = 0; i < ExcelConstValues.commentCols.length; i++)
        {
            sheet.setColumnWidth(i, 20 * 256);          //Widens The Column One by One
            Cell cell = row.createCell(i);
            cell.setCellValue(ExcelConstValues.commentCols[i]);
            cell.setCellStyle(new CellStyleOptions(workbook).columnHeader);
        }
        return sheet;
    }

    public static void inputVideoDataToExcelSheet(XSSFSheet sheet, VideoData videoData)
    {
        int rowNum = sheet.getLastRowNum()+1;
        XSSFRow row = sheet.createRow(rowNum);
        CellStyle regularCellStyle = new CellStyleOptions(sheet.getWorkbook()).regularCell;

/*        row.createCell(0).setCellValue(videoData.getVideoTitle());
        row.createCell(1).setCellValue(videoData.getChannelTitle());
        row.createCell(2).setCellValue(videoData.getId());
        row.createCell(3).setCellValue(videoData.getUploadDate());
        row.createCell(4).setCellValue(videoData.getLikes());
        row.createCell(5).setCellValue(videoData.getViews());
        row.createCell(6).setCellValue(videoData.getCommentCount());*/

        for (int i = 0; i < 7; i++)
        {
            row.getCell(i).setCellStyle(regularCellStyle);
        }
    }

    public static void inputCommentToExcelSheet(XSSFSheet sheet, CommentData commentData)
    {
        int rowNum = sheet.getLastRowNum()+1;
        XSSFRow row = sheet.createRow(rowNum);
        CellStyle regularCellStyle = new CellStyleOptions(sheet.getWorkbook()).regularCell;

        row.createCell(0).setCellValue(commentData.textOriginal);
        row.createCell(1).setCellValue(commentData.authorName);
        row.createCell(2).setCellValue(commentData.likeCount);

        for (int i = 0; i < ExcelConstValues.commentCols.length; i++)
        {
            row.getCell(i).setCellStyle(regularCellStyle);
        }
    }

    public static void createExcelFile(XSSFWorkbook output) throws URISyntaxException, IOException  //Create Folder ExcelFiles and excel file
    {
        File excelFileFolderPath = new File( System.getProperty("user.dir") + "\\ExcelFiles");    //Creates the folder in the project folder
        if (excelFileFolderPath.mkdir())
        {
            System.out.println("Created Folder ExcelFiles Successfully!");
        }
        else
        {
            System.out.println("Folder Already Exists! Or Failed to create folder ExcelFiles");
        }
        String filePath = excelFileFolderPath + "\\VideoData.xlsx";
        FileOutputStream out = new FileOutputStream(filePath);
        output.write(out);
    }
}