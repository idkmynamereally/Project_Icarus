package myproject.icarus;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class OutputHandler
{
    /*public static void main() throws IOException, URISyntaxException
    {
        List<VideoData> Videos = YoutubeApiClient.search("Beedi-Jalaile");
    }*/
}

class CSVHandler
{
    public static void videosToCSVFile(String searchTerm, List<VideoData> videos) throws IOException
    {
        for (VideoData v : videos)
        {
            videoToCSVFile(searchTerm, v);
        }
    }

    /**
     * Takes a single VideoData Object and creates a CSV File by the name of its title with the first HeaderRow and subsequent rows as each comment of the video.
     * @param video VideoData video to create CSV file for.
     * @throws IOException
     */
    public static void videoToCSVFile(String searchTerm, VideoData video) throws IOException
    {
        List<String[]> csvItems = new ArrayList<>();
        for (CommentData c : video.comments)
        {
            csvItems.add(commentDataToCSVItem(c));
        }
        writeCSVFile(searchTerm, video.videoId, csvItems);
    }
    /**
     * Creates a file with parameter fileName and writes all the String Arrays in List data.
     * @param fileName Name of the file to be created
     * @param data List of String Arrays, each array represents one item.
     * @throws IOException
     */
    public static void writeCSVFile(String searchTerm, String fileName, List<String[]> data) throws IOException
    {
        File file = new File(searchTerm);
        file.mkdir();
        CSVWriter writer = new CSVWriter(new FileWriter(searchTerm + "/" + fileName + ".csv"));
        String[] headerRow = {"Comment"};
        writer.writeNext(headerRow);
        for (String[] s : data)
        {
            writer.writeNext(s);
        }
        writer.close();
    }

    public static String[] commentDataToCSVItem(CommentData commentData)
    {
        String[] csvItem = new String[1];
        csvItem[0] = commentData.textOriginal;
        return csvItem;
    }
}