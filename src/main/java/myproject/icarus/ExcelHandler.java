package myproject.icarus;

import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ExcelHandler
{
    public static void createExcelFile(XSSFWorkbook output) throws URISyntaxException, IOException
    {
        File excelFileFolderPath = new File( System.getProperty("user.dir") + "\\ExcelFiles");    //Creates the folder in the project folder
        System.out.println(excelFileFolderPath.getAbsolutePath());
        System.out.println(excelFileFolderPath.mkdir());

        String filePath = excelFileFolderPath + "\\VideoData.xlsx";
        FileOutputStream out = new FileOutputStream(filePath);
        output.write(out);
    }
}