package myproject.icarus;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * enum for different filterModes.(used in YoutubeApiClient.search)
 */
enum filterMode
{
    UNIQUE_CHANNELS
}

/**
 * Class to contain Constant Values used in the program for easier modifications for later.
 */
class ConstValues
{
    static final String API_KEY = "AIzaSyCG9m-cT3ugYXbx_TsPkJNfEnFH1GaaOvM";
    static int pageCount = 2;                                        //To decide how many pages of the Search function to access to get videos.
    static filterMode currentFilter = filterMode.UNIQUE_CHANNELS;
}

/**
 * Class containing default values for the search functions.
 */
class defaultSearchValues
{
    static int maxResults = 100;
    static String order = "relevance";
    static String relevanceLanguage = "en";
    static String type = "video";
    static String pubAfter = "1970-01-01T00:00:00Z";
}

/**
 * Class to construct URLs with different parameters for accessing the YoutubeApi-v3.
 */
class ApiUrlCreator
{
    /**
     * Creates a URL to Search-list function(YoutubeApi-v3).
     * @param maxResults the maximum number of items that should be returned in the result set. Acceptable values are 0 to 50, inclusive
     * @param order the method that will be used to order resources in the API response. The default value is relevance. Values -> [date, rating, relevance, title, videoCount, viewCount]
     * @param pubAfter indicates that the API response should only contain resources created at or after the specified time. RFC 3339 format.
     * @param q specifies the query term to search for.
     * @param relevanceLanguage instructs the API to return search results that are most relevant to the specified language. The parameter value is typically an ISO 639-1 two-letter language code.
     * @param type restricts a search query to only retrieve a particular type of resource. Values -> [channel, playlist, video]
     * @return URL to Search-list function(YoutubeApi-v3) in string format.
     */
    static String SearchApiFunctionUrl(int maxResults, String order, String pubAfter, String q, String relevanceLanguage, String type)
    {
        String baseUrl = "https://youtube.googleapis.com/youtube/v3/search?part=" + "snippet" + "%2C" + "id";      //Only uses part Snippet & id
        baseUrl += "&maxResults=" + maxResults;
        baseUrl += "&order=" + order;
        baseUrl += "&publishedAfter=" + pubAfter;
        baseUrl += "&q=" + q;
        baseUrl += "&relevance=" + relevanceLanguage;
        baseUrl += "&type=" + type;
        baseUrl += "&key=" + ConstValues.API_KEY;
        return baseUrl;
    }

    /**
     * Minimizes the verbose SearchUrlCreator by only asking for query and using default values from DefaultSearchValues class.
     * @param q Specifies the query to search YouTube for.
     * @return URL to Search-list function(YoutubeApi-v3) in string format.
     */
    static String SearchApiFunctionUrl(String q)
    {
        return SearchApiFunctionUrl(defaultSearchValues.maxResults, defaultSearchValues.order, defaultSearchValues.pubAfter, q, defaultSearchValues.relevanceLanguage, defaultSearchValues.type);
    }

    /**
     * Creates a URL to Videos-list function(YoutubeApi-v3).
     * @param id YouTube Video ID to call Videos function for.
     * @return URL to Videos-list function(YoutubeApi-v3) in string format.
     */
    static String VideoApiFunctionUrl(String id)
    {
        String baseUrl = "https://youtube.googleapis.com/youtube/v3/videos?part=" + "snippet" + "%2C" + "statistics";
        baseUrl += "&id=" + id;
        baseUrl += "&key=" + ConstValues.API_KEY;
        return baseUrl;
    }

    /**
     * Creates a URL to a particular pageToken.
     * @param pageToken pageToken value to create URL for.
     * @return URL to a particular pageToken.
     */
    static String getPageResponseUrl(String pageToken)
    {
        String baseUrl = "https://youtube.googleapis.com/youtube/v3/search?part=snippet%2Cid";
        baseUrl += "&pageToken=" + pageToken;
        baseUrl += "&key=" + ConstValues.API_KEY;
        return baseUrl;
    }

    /**
     * Creates a URL to download a Thumbnail of YouTube video.
     * @param videoId VideoId to download thumbnail of.
     * @return URL to download a Thumbnail of YouTube video.
     */
    static String getThumbnail(String videoId)
    {
        String baseUrl = "https://i.ytimg.com/vi/" + videoId + "/maxresdefault.jpg";
        return baseUrl;
    }
}

/**
 *  Class to send API Request
 */
class SendApiRequest
{
    /**
     * Takes a URL makes a GET request and returns its response in String format.
     * @param url URL to send GET request to.
     * @return Response the URL provides.
     */
    static String sendGetRequest(String url) throws IOException, URISyntaxException     //Basic Function to send a URL Request.
    {
        URI u = new URI(url);
        HttpURLConnection connection = (HttpURLConnection) (u.toURL()).openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null)
            response.append(line);
        return response.toString();
    }
}

/**
 * Class with functions to filter Lists of VideoData on pre-defined basis.
 */
class FilterLists
{
    /**
     * Filters a VideoData list to only contain one video per Channel.
     * @param videos List of VideoData objects to filter.
     * @return Returns the same list with only unique channels.
     */
    public static List<VideoData> filterUniqueChannels(List<VideoData> videos)
    {
        Set<String> channelIds = new HashSet<>();
        for (int i = 0; i < videos.size(); i++)
        {
            VideoData v = videos.get(i);
            if (channelIds.contains(v.channel.channelId))
            {
                videos.remove(v);
                i--;
            }
            else
            {
                channelIds.add(v.channel.channelId);
            }
        }
        return videos;
    }
}

/**
 * Static Class to convert raw JSON reponses to java objects.
 * @author slash
 */
class JSONResponseParser
{
    /**
     *
     * @param jsonString raw jsonResponse of Search function(YoutubeApi-v3) in String.
     * @return List of VideoData objects with incomplete data(id, title, channelId, channelTitle, publishedAt, publishedAt).
     */
    static List<VideoData> parseSearchResponsePage(String jsonString)       //Extracts data from the response of the Search Function
    {
        List<VideoData> videos = new ArrayList<>();

        String nextPageToken;
        int totalResults;
        int resultsPerPage;

        JSONObject response = new JSONObject(jsonString);
        JSONObject pageInfo = response.getJSONObject("pageInfo");
        JSONArray items = response.getJSONArray("items");

        nextPageToken = response.getString("nextPageToken");
        totalResults = pageInfo.getInt("totalResults");
        resultsPerPage = pageInfo.getInt("resultsPerPage");

        for (int i = 0; i < resultsPerPage; i++)
        {
            JSONObject vidObj;
            vidObj = items.getJSONObject(i);
            videos.add(parseVideoObjectOfSearch(vidObj));
        }

        return videos;
    }

    /**
     * Takes the response of YouTube video function uses it to return a new VideoData object entirely.
     * @param jsonString raw JSON response of Video(YoutubeApi-v3) function.
     * @return New VideoData object with data parsed from Video function JSON response.
     */
    static VideoData parseVideoResponse(String jsonString)  //Overloaded
    {
        VideoData video = new VideoData();
        parseToVideoData(video, jsonString);
        return video;
    }

    /**
     * Takes the response of YouTube video function uses it to populate a VideoData object entirely.
     * @param video any VideoData Object
     * @param jsonString raw JSON response of Video function(YoutubeApi-v3)
     */
    static void parseToVideoData(VideoData video, String jsonString)       //VIDEO Method response Parsed to a VideoData Object
    {
        JSONObject response = new JSONObject(jsonString);
        JSONArray items = response.getJSONArray("items");
        JSONObject videoObj = items.getJSONObject(0);
        JSONObject snippet = videoObj.getJSONObject("snippet");
        JSONObject stats = videoObj.getJSONObject("statistics");

        video.videoId = videoObj.getString("id");
        video.videoTitle = snippet.getString("title");
        video.channel.channelId = snippet.getString("channelId");
        video.channel.channelName = snippet.getString("channelTitle");
        video.publishedAt = snippet.getString("publishedAt");
        video.description = snippet.getString("description");
        if (snippet.has("defaultAudioLanguage"))
            video.audioLanguage = snippet.getString("defaultAudioLanguage");
        video.likeCount = stats.getInt("likeCount");
        video.viewCount = stats.getInt("viewCount");
        video.commentCount = stats.getInt("commentCount");
    }

    /**
     * Creates a partially initialized VideoData object(initialized by parseSearchResponse(Only incomplete info from Search Function(YoutubeApi-v3)))
     * @param vidObject a JSONObject of the video from the Search Response JSONArray items.
     * @return new incomplete VideoData object
     */
    //WARNING : DOES NOT INITIALIZE VIDEODATA ENTIRELY, Used as an Early Step for FILTERING LIST.DO NOT USE ANYWHERE ELSE
    private static VideoData parseVideoObjectOfSearch(JSONObject vidObject)         //Creates an Incomplete VideoData Object, Extracts Data from a (Video Object of Search Response) to VideoData Object
    {
        VideoData vRet = new VideoData();

        String videoId;
        String publishedAt;
        String channelId;
        String channelTitle;
        String videoTitle;
        String description;
        String thumbnailLink;

        JSONObject idObj = vidObject.getJSONObject("id");
        JSONObject snippet = vidObject.getJSONObject("snippet");
        /*JSONObject thumbnailObj = vidObject.getJSONObject("thumbnails").getJSONObject("high");*/

        videoId = idObj.getString("videoId");
        videoTitle = snippet.getString("title");
        description = snippet.getString("description");
        channelId = snippet.getString("channelId");
        channelTitle = snippet.getString("channelTitle");
        publishedAt = snippet.getString("publishedAt");
        thumbnailLink = ApiUrlCreator.getThumbnail(videoId);

        vRet.videoTitle = videoTitle;
        vRet.videoId = videoId;
        vRet.description = description;
        vRet.publishedAt = publishedAt;
        vRet.thumbnailUrl = thumbnailLink;
        vRet.channel.channelName = channelTitle;
        vRet.channel.channelId = channelId;

        return vRet;
    }
}

/**
 * Class to be directly accessed outside to work with YoutubeApiClient
 */
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
        String searchUrl = ApiUrlCreator.SearchApiFunctionUrl(query);
        String response = SendApiRequest.sendGetRequest(searchUrl);
        List<VideoData> videos = JSONResponseParser.parseSearchResponsePage(response);      //List of incomplete videodatas

        switch (ConstValues.currentFilter)
        {
            case UNIQUE_CHANNELS ->  FilterLists.filterUniqueChannels(videos);       //Remove Videos with duplicate channels
        }

        for (VideoData v : videos)      //Complete VideoData Objects with full info
        {
            completeVideoData(v);
        }
        return videos;
    }

    /**
     * Completes a partially initialized VideoObject from Search response(YouTubeApi-v3).
     * @param v VideoObject reference of partial VideoData object.
     */
    private static void completeVideoData(VideoData v) throws IOException, URISyntaxException
    {
        String videoUrl = ApiUrlCreator.VideoApiFunctionUrl(v.videoId);
        String videoResponse = SendApiRequest.sendGetRequest(videoUrl);
        JSONResponseParser.parseToVideoData(v, videoResponse);
    }
}