package myproject.icarus;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class myMain
{
    public static void main(String[] args) throws IOException, URISyntaxException
    {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = ExcelHandler.initExcelSheetForComments(workbook);
        List<Comment> list = YouTubeApiClient.getCommentsData("YD55wE2k2FQ");
        for (Comment c : list)
        {
            ExcelHandler.inputCommentToExcelSheet(sheet, c);
        }
        ExcelHandler.createExcelFile(workbook);
    }
    public static void writeJsonToFile(String json) throws IOException
    {
        File file = new File("D:\\coding\\JavaRep\\ytData.json");
        file.createNewFile();
        FileWriter fw = new FileWriter(file);
        fw.write(json);
        fw.close();
    }
}