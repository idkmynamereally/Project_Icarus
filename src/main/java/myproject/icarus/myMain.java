package myproject.icarus;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class myMain
{
    public static void main(String[] args) throws IOException, URISyntaxException
    {
        String searchTerm = "Pewdiepie";
        List<VideoData> l = YoutubeApiClient.search(searchTerm);
        CSVHandler.videosToCSVFile(searchTerm, l);
    }
}