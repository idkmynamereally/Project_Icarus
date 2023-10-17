package myproject.icarus;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Static Class to convert raw JSON reponses to java objects.
 * @author slash
 */
public class JSONResponseParser
{
    /**
     * Takes a JSON Response String and returns the nextPageToken property value. (Currently Tested and meant for CommentThread and Search functions).
     * @param jsonString String JSONResponse
     * @return NULL if no such property, the nextPageToken if available.
     */
    public static String getNextPageToken(String jsonString)
    {
        JSONObject response = new JSONObject(jsonString);
        if (response.has("nextPageToken"))
            return response.getString("nextPageToken");
        return "null";
    }
    /**
     * Initializes a List of VideoData objects with [incomplete]data returned from Search function (YouTubeApi-v3).
     * @param jsonString raw jsonResponse of Search function(YoutubeApi-v3) in String.
     * @return List of VideoData objects with incomplete data(id, title, channelId, channelTitle, publishedAt, publishedAt).
     */
    public static List<VideoData> parseSearchResponsePage(String jsonString)       //Extracts data from the response of the Search Function
    {
        List<VideoData> videos = new ArrayList<>();

        String nextPageToken = "null";
        int totalResults = 0;
        int resultsPerPage = 0;
        JSONObject pageInfo = new JSONObject();
        JSONArray items = new JSONArray();
        JSONObject response = new JSONObject(jsonString);

        if (response.has("pageInfo"))
            pageInfo = response.getJSONObject("pageInfo");
        if (response.has("items"))
            items = response.getJSONArray("items");
        if (response.has("nextPageToken"))
            nextPageToken = response.getString("nextPageToken");
        if (pageInfo.has("totalResults"))
            totalResults = pageInfo.getInt("totalResults");
        if (pageInfo.has("resultsPerPage"))
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
     * Takes response to a CommentThreadList Function and returns a list of CommentData objects.
     * @param jsonString JSON response created by the CommentThreadResponse-List
     * @return List of CommentData objects
     */
    public static List<CommentData> parseCommentThreadResponse(String jsonString)
    {
        List<CommentData> comments = new ArrayList<>();
        JSONObject responseObject = new JSONObject(jsonString);
        JSONObject pageInfo = responseObject.getJSONObject("pageInfo");
        JSONArray items = responseObject.getJSONArray("items");
        int totalResults = 0;
        int resultsPerPage = 0;

        String nextPageToken = "null";
        if (responseObject.has("nextPageToken"))
            nextPageToken = responseObject.getString("nextPageToken");
        totalResults = pageInfo.getInt("totalResults");
        resultsPerPage = pageInfo.getInt("resultsPerPage");

        JSONObject item;
        for (int i = 0; i < totalResults; i++)      //The search function uses resultsPerPage BUT it is totalResults here I have NO FUCKING CLUE WHY. I HAVE BEEN DEBUGGING FOR 6 HOURS FUCK YOUTUBE
        {
            item = items.getJSONObject(i);
            comments.add(parseCommentItem(item));
        }
        return comments;
    }

    /**
     * Used to parse individual JSONObject in the JSON Array items in the response.
     * @param item item in an items array in the CommentThreadResponse-List
     * @return CommentData object fully initialized.
     */
    public static CommentData parseCommentItem(JSONObject item)
    {
        CommentData comment = new CommentData();
        JSONObject snippet = item.getJSONObject("snippet");
        JSONObject topLevelComment = snippet.getJSONObject("topLevelComment");
        JSONObject topSnippet = topLevelComment.getJSONObject("snippet");

        if(item.has("id"))
            comment.commentId = item.getString("id");
        if(topSnippet.has("authorDisplayName"))
            comment.authorName = topSnippet.getString("authorDisplayName");
        if(topSnippet.has("channelId"))
            comment.channelId = topSnippet.getString("channelId");
        if(topSnippet.has("textOriginal"))
            comment.textOriginal = topSnippet.getString("textOriginal");
        if(topSnippet.has("publishedAt"))
            comment.publishedAt = topSnippet.getString("publishedAt");
        if(topSnippet.has("likeCount"))
            comment.likeCount = topSnippet.getInt("likeCount");

        return comment;
    }

    /**
     * Takes the response of YouTube video function uses it to return a new VideoData object entirely.
     * @param jsonString raw JSON response of Video(YoutubeApi-v3) function.
     * @return New VideoData object with data parsed from Video function JSON response.
     */
    public static VideoData parseVideoResponse(String jsonString)  //Overloaded
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
    public static void parseToVideoData(VideoData video, String jsonString)       //VIDEO Method response Parsed to a VideoData Object
    {
        JSONObject response = new JSONObject(jsonString);
        JSONArray items = response.getJSONArray("items");
        JSONObject videoObj = items.getJSONObject(0);
        JSONObject snippet = videoObj.getJSONObject("snippet");
        JSONObject stats = videoObj.getJSONObject("statistics");

        if (videoObj.has("id"))
            video.videoId = videoObj.getString("id");
        if (snippet.has("title"))
            video.videoTitle = snippet.getString("title");
        if (snippet.has("channelId"))
            video.channel.channelId = snippet.getString("channelId");
        if (snippet.has("channelTitle"))
            video.channel.channelName = snippet.getString("channelTitle");
        if (snippet.has("publishedAt"))
            video.publishedAt = snippet.getString("publishedAt");
        if (snippet.has("description"))
            video.description = snippet.getString("description");
        if (snippet.has("defaultAudioLanguage"))
            video.audioLanguage = snippet.getString("defaultAudioLanguage");
        if (stats.has("likeCount"))
            video.likeCount = stats.getInt("likeCount");
        if (stats.has("viewCount"))
            video.viewCount = stats.getInt("viewCount");
        if (stats.has("commentCount"))
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

        String videoId = "null";
        String publishedAt = "null";
        String channelId = "null";
        String channelTitle = "null";
        String videoTitle = "null";
        String description = "null";
        String thumbnailLink = "null";

        JSONObject idObj = vidObject.getJSONObject("id");
        JSONObject snippet = vidObject.getJSONObject("snippet");
        /*JSONObject thumbnailObj = vidObject.getJSONObject("thumbnails").getJSONObject("high");*/

        if (idObj.has("videoId"))
            videoId = idObj.getString("videoId");
        if(snippet.has("title"))
            videoTitle = snippet.getString("title");
        if(snippet.has("description"))
            description = snippet.getString("description");
        if(snippet.has("channelId"))
            channelId = snippet.getString("channelId");
        if(snippet.has("channelTitle"))
            channelTitle = snippet.getString("channelTitle");
        if(snippet.has("publishedAt"))
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

