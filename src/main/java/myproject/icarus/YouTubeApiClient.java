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
import java.util.List;

public class YouTubeApiClient {
    public static List<VideoData> getSearchData(String query) throws IOException, URISyntaxException
    {      //List of videoIds
        int numOfVideos = 20;
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
        int numOfComments = 20;
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
            comment = topLevelCommentSnippet.getString("textDisplay");
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
}