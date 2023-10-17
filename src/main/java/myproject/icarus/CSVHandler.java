package myproject.icarus;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * I fucked up with how I was supposed to handle this class. will format later. the file is mostly to output data to a file. currently CSV files only.
 */
class OutputHandler
{
    /*public static void main() throws IOException, URISyntaxException
    {
        List<VideoData> Videos = YoutubeApiClient.search("Beedi-Jalaile");
    }*/
}

/**
 * Outputs data to a CSV file locally.
 */
public class CSVHandler
{
    /**
     * Takes a list of videos, creates a folder [searchTerm] with a file for each video in the list with name [v.videoId].csv, stores all comments from that video line by line in it.
     * @param searchTerm The query to which the list of videos was retrieved to.
     * @param videos List of videos that has to be outputted.
     * @throws IOException idk
     */
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
     * @throws IOException idk
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
     * @throws IOException idk
     */
    public static void writeCSVFile(String searchTerm, String fileName, List<String[]> data) throws IOException
    {
        String[] headerRow = {"Comment"};       //To add more columns change this.

        File file = new File(searchTerm);
        file.mkdir();
        CSVWriter writer = new CSVWriter(new FileWriter(searchTerm + "/" + fileName + ".csv"));
        writer.writeNext(headerRow);
        for (String[] s : data)
        {
            writer.writeNext(s);
        }
        writer.close();
    }

    /**
     * Converts a single CommentData object to a String[] to be used as an argumentto writeNext function of CSVWriter class.
     * @param commentData comment to be converted to a CSVItem.
     * @return String[] that is a CSV item formed by the comment.
     */
    public static String[] commentDataToCSVItem(CommentData commentData)
    {
        String[] csvItem = new String[1];
        csvItem[0] = commentData.textOriginal;
        return csvItem;
    }
}