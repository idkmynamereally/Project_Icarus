package myproject.icarus;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


/**
 * Class to contain Constant Values used in the program for easier modifications for later.
 */
class constValues
{
    //static final String API_KEY = "AIzaSyCG9m-cT3ugYXbx_TsPkJNfEnFH1GaaOvM";      //First Account
    static final String API_KEY = "AIzaSyDaybESiycR4Oh_O_FztGbeKulO5WFlrFs";        //Second Account
}

/**
 * Default parameters for the search function.
 */
class defaultSearchValues
{
    static int maxResults = 50;         //Number of items to return (5 to 50)
    static int pageCount = 4;           //Number of pages to parse when searching.
    //Total Videos that will be searched == maxResults*pageCount
    static String order = "relevance";
    static String relevanceLanguage = "en";
    static String type = "video";
    static String pubAfter = "1970-01-01T00:00:00Z";
    static filterMode currentFilter = filterMode.UNIQUE_CHANNELS;
}

/**
 * Parameters used when searching for comments.
 * Number of Comments retrieved -> <= maxResults*pageCount
 */
class commentDefaultValues
{
    static int maxResults = 100;
    static int pageCount = 1;
    static String moderationStatus = "published";
    static String order = "time";
    static String textFormat = "html";
}

public class YoutubeApiClient
{
    /**
     *
     * @param query Query to search YouTube for.
     * @return Filtered List of VideoData objects fully initialized.(Filter changed by class ConstValues.)
     */
    //1. Get Response 2. Parse to Incomplete video objects 3. Filter Duplicate channels 4. Complete the objects
    public static List<VideoData> search(String query) throws IOException, URISyntaxException
    {
        String nextPageToken = "null";
        List<VideoData> videos = new ArrayList<>();
        String searchUrl = "null";
        String response = "null";

        for (int i = 0; i < defaultSearchValues.pageCount; i++)     //Goes to x Pages and adds all the videos on the pages to the Videos list.
        {
            searchUrl = ApiUrlCreator.searchApiFunctionUrl(query, nextPageToken);
            response = SendApiRequest.sendGetRequest(searchUrl);
            if (response.startsWith("Error"))       //Any Response Code > 399
            {
                System.out.println(response);
                return videos;
            }
            videos.addAll(JSONResponseParser.parseSearchResponsePage(response));
            nextPageToken = JSONResponseParser.getNextPageToken(response);
            if (nextPageToken.equals("null"))
                break;
        }

        switch (defaultSearchValues.currentFilter)
        {
            case UNIQUE_CHANNELS -> FilterLists.filterUniqueChannels(videos);       //Remove Videos with duplicate channels
        }

        for (VideoData v : videos)      //Each Video in the list is now filled with information received from further API Calls for each video.
        {
            boolean success = completeVideoData(v);
            if (!success)               //This is the worst identifier my english has ever conjured.
            {
                System.out.println("Error Completing VideoData for VideoTitle : \"" + v.videoTitle + "\" VideoId : " + v.videoId);
            }
        }

        return videos;
    }

    /**
     * Completes a partially initialized VideoObject from Search response(YouTubeApi-v3) with additional information and Comments.
     * @param v VideoObject reference of partial VideoData object.
     */
    private static boolean completeVideoData(VideoData v) throws IOException, URISyntaxException        //Called everytime for a new video
    {
        String videoUrl = ApiUrlCreator.VideoApiFunctionUrl(v.videoId);
        String videoResponse = SendApiRequest.sendGetRequest(videoUrl);
        if (videoResponse.startsWith("Error"))      //Any Response code > 399
        {
            System.out.println(videoResponse);
            return false;
        }
        JSONResponseParser.parseToVideoData(v, videoResponse);      //All the data received from the Video function(No Comments).

        String pageToken = "null";
        String commentsUrl = "null";
        String commentResponse = "null";

        for (int i = 0; i < commentDefaultValues.pageCount; i++)    //For the given video we will go to x pages while adding all comments on each page to a list.
        {
            commentsUrl = ApiUrlCreator.commentThreadApiPageTokenUrl(v.videoId, pageToken);
            commentResponse = SendApiRequest.sendGetRequest(commentsUrl);
            if (commentResponse.startsWith("Error"))
            {
                System.out.println(commentResponse);
                return false;
            }
            v.comments.addAll(JSONResponseParser.parseCommentThreadResponse(commentResponse));
            pageToken = JSONResponseParser.getNextPageToken(commentResponse);
            if (pageToken.equals("null"))
                break;
        }
        return true;
    }
}

/**
 * enum for different filterModes.(used in YoutubeApiClient.search)
 */
enum filterMode
{
    UNIQUE_CHANNELS,
    NOTHING,
}