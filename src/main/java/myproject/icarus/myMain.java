package myproject.icarus;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;

public class myMain
{
    public static void main(String[] args) throws IOException, URISyntaxException
    {
        XSSFWorkbook t = new XSSFWorkbook();
        t.createSheet("Nice");
        ExcelHandler.createExcelFile(t);
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