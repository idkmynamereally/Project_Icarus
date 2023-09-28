package myproject.icarus;

/*
public class YouTubeApiClient {
    public static List<VideoData> getSearchData(String query) throws IOException, URISyntaxException
    {      //List of videoIds
        int numOfVideos = 25;
        String order = "relevance";     //Other : date, rating, relevance, title, videoCount, viewCount
        List<VideoData> l = new ArrayList<>(numOfVideos); //List of VideoData Objects
        JSONArray videoArr = (new JSONObject(getJsonResponseSearchData(query, order, numOfVideos))).getJSONArray("items");     //JSON Array of Video Data that are found by query.
        JSONObject vidObj;
        String id;
        for (int i = 0; i < numOfVideos; i++)
        {
            vidObj = videoArr.getJSONObject(i);
            id = vidObj.getJSONObject("id").getString("videoId");
            l.add(i, getVideoData(id));
        }
        return l;
    }
    public static VideoData getVideoData(String videoId) throws IOException, URISyntaxException
    {
        String videoTitle;
        String channelTitle;
        String uploadDate;
        int likes;
        int views;
        int commentCount;

        String json = YouTubeApiClient.getJsonResponseVideoData(videoId);
        JSONObject jsonResponse = new JSONObject(json);     //Entire Response
        JSONObject arr0 = new JSONObject(new JSONArray(jsonResponse.get("items").toString()).get(0).toString());   //Zero Index of JSONArray at items
        JSONObject jsSnippet = arr0.getJSONObject("snippet");   //Snippet part

        videoTitle = jsSnippet.getString("title");
        channelTitle = jsSnippet.getString("channelTitle");
        uploadDate = jsSnippet.getString("publishedAt");    //Get Published
        uploadDate = uploadDate.replace("T", " ");

        JSONObject jsStats = new JSONObject(arr0.get("statistics").toString());

        likes = jsStats.getInt("likeCount");
        views = jsStats.getInt("viewCount");
        commentCount = jsStats.getInt("commentCount");
        return new VideoData(videoTitle, channelTitle, videoId, uploadDate, likes, views, commentCount);
    }
    public static List<Comment> getCommentsData(String videoId) throws IOException, URISyntaxException
    {
        int numOfComments = 50;
        ArrayList<Comment> comments = new ArrayList<>(numOfComments);
        JSONArray apiResponseArray = (new JSONObject(getJsonResponseCommentsData(videoId, numOfComments))).getJSONArray("items");
        JSONObject topLevelCommentSnippet;
        Comment com;
        String comment;
        String authorName;
        int likes;
        for (int i = 0; i < numOfComments; i++)
        {
            topLevelCommentSnippet = apiResponseArray.getJSONObject(i).getJSONObject("snippet").getJSONObject("topLevelComment").getJSONObject("snippet");
            comment = topLevelCommentSnippet.getString("textOriginal");
            authorName = topLevelCommentSnippet.getString("authorDisplayName");
            likes = topLevelCommentSnippet.getInt("likeCount");
            com = new Comment(comment, authorName, likes);
            comments.add(i, com);
        }
        return comments;
    }

    private static final String API_KEY = "AIzaSyCG9m-cT3ugYXbx_TsPkJNfEnFH1GaaOvM";
    private static String getJsonResponseSearchData(String query, String order, int numOfVideos) throws IOException, URISyntaxException {
        String jsonResponseSearchData = sendGetRequest(buildApiUrlSearchData(query, order, numOfVideos));
        return jsonResponseSearchData;
    }
    private static String getJsonResponseVideoData(String videoId) throws IOException, URISyntaxException {
        String jsonResponseVideoData = sendGetRequest(buildApiUrlVideoData(videoId));
        return jsonResponseVideoData;
    }
    private static String getJsonResponseCommentsData(String videoId, int numOfComments) throws IOException, URISyntaxException {
        String jsonResponseCommentsData = sendGetRequest(buildApiUrlCommentsData(videoId, numOfComments));
        return jsonResponseCommentsData;
    }

    private static String buildApiUrlSearchData(String query, String order, int numOfVideos) {
        String baseUrl = "https://www.googleapis.com/youtube/v3/search";
        String parameters = String.format("key=%s&q=%s&order=%s&maxResults=%d&part=snippet&fields=items&type=video", API_KEY, query, order, numOfVideos);
        return baseUrl + "?" + parameters;
    }
    private static String buildApiUrlVideoData(String videoId) {
        String baseUrl = "https://www.googleapis.com/youtube/v3/videos";
        String parameters = String.format("id=%s&stat&key=%s&part=snippet,statistics&fields=items", videoId, API_KEY);
        return baseUrl + "?" + parameters;
    }
    private static String buildApiUrlCommentsData(String videoId, int numOfComments) {
        String baseUrl = "https://www.googleapis.com/youtube/v3/commentThreads";
        String parameters = String.format("videoId=%s&key=%s&part=snippet&maxResults=%d&order=relevance&fields=items", videoId, API_KEY, numOfComments);
        return baseUrl + "?" + parameters;
    }

    private static String sendGetRequest(String url) throws IOException, URISyntaxException {
        URI u = new URI(url);
        HttpURLConnection connection = (HttpURLConnection) (u.toURL()).openConnection();
        connection.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        }
    }
}*/

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

class ConstValues
{
    static final String API_KEY = "AIzaSyCG9m-cT3ugYXbx_TsPkJNfEnFH1GaaOvM";
    static int pageCount = 2;
}

class defaultSearchValues
{
    static int maxResults = 100;
    static String order = "relevance";
    static String relevanceLanguage = "en";
    static String type = "video";
    static String pubAfter = "1970-01-01T00:00:00Z";
}

class ApiUrlCreator
{
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
    static String SearchApiFunctionUrl(String q)
    {
        return SearchApiFunctionUrl(defaultSearchValues.maxResults, defaultSearchValues.order, defaultSearchValues.pubAfter, q, defaultSearchValues.relevanceLanguage, defaultSearchValues.type);
    }

    static String VideoApiFunctionUrl(String id)
    {
        String baseUrl = "https://youtube.googleapis.com/youtube/v3/videos?part=" + "snippet" + "%2C" + "statistics";
        baseUrl += "&id=" + id;
        baseUrl += "&key=" + ConstValues.API_KEY;
        return baseUrl;
    }

    static String getPageResponseUrl(String pageToken)
    {
        String baseUrl = "https://youtube.googleapis.com/youtube/v3/search?part=snippet%2Cid";
        baseUrl += "&pageToken=" + pageToken;
        baseUrl += "&key=" + ConstValues.API_KEY;
        return baseUrl;
    }

    static String getThumbnail(String videoId)
    {
        String baseUrl = "https://i.ytimg.com/vi/" + videoId + "/maxresdefault.jpg";
        return baseUrl;
    }
}

class SendApiRequest
{
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

class FilterLists
{
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

class JSONResponseParser
{
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

    static VideoData parseVideoResponse(String jsonString)
    {
        VideoData video = new VideoData();

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
        video.audioLanguage = snippet.getString("defaultAudioLanguage");
        video.likeCount = stats.getInt("likeCount");
        video.viewCount = stats.getInt("viewCount");
        video.commentCount = stats.getInt("commentCount");

        return video;
    }

    static void parseToVideoData(VideoData video, String jsonString)
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

    //WARNING : DOES NOT INITIALIZE VIDEODATA ENTIRELY, Used as an Early Step for FILTERING LIST.DO NOT USE ANYWHERE ELSE
    private static VideoData parseVideoObjectOfSearch(JSONObject vidObject)         //Creates a Incomplete VideoData Object, Extracts Data from a Video Object to VideoData OBject
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

public class YoutubeApiClient
{
    //1. Get Response 2. Parse to Incomplete video objects 3. Filter Duplicate channels 4. Complete the objects
    public static List<VideoData> search(String query) throws IOException, URISyntaxException
    {
        String searchUrl = ApiUrlCreator.SearchApiFunctionUrl(query);
        String response = SendApiRequest.sendGetRequest(searchUrl);
        List<VideoData> videos = JSONResponseParser.parseSearchResponsePage(response);      //List of incomplete videodatas
        FilterLists.filterUniqueChannels(videos);       //Remove Videos with dupplicate channels
        for (VideoData v : videos)      //Complete VideoData Objects with full info
        {
            completeVideoData(v);
        }
        return videos;
    }

    private static void completeVideoData(VideoData v) throws IOException, URISyntaxException
    {
        String videoUrl = ApiUrlCreator.VideoApiFunctionUrl(v.videoId);
        String videoResponse = SendApiRequest.sendGetRequest(videoUrl);
        JSONResponseParser.parseToVideoData(v, videoResponse);
    }
}