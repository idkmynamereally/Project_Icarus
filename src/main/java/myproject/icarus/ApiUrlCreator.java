package myproject.icarus;

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
    static String searchApiFunctionUrl(int maxResults, String order, String pubAfter, String q, String relevanceLanguage, String type, String pageToken)
    {
        String baseUrl = "https://youtube.googleapis.com/youtube/v3/search?part=" + "snippet" + "%2C" + "id";      //Only uses part Snippet & id

        String[] qParts = q.split(" ");
        StringBuilder search = new StringBuilder("");
        search.append(qParts[0]);
        for (int i = 1; i < qParts.length; i++)
        {
            search.append("%20");
            search.append(qParts[i]);
        }

        baseUrl += "&maxResults=" + maxResults;
        baseUrl += "&order=" + order;
        baseUrl += "&publishedAfter=" + pubAfter;
        baseUrl += "&q=" + search;
        baseUrl += "&relevance=" + relevanceLanguage;
        baseUrl += "&type=" + type;
        baseUrl += "&key=" + constValues.API_KEY;
        if (!pageToken.equals("null"))
            baseUrl += "&pageToken=" + pageToken;
        return baseUrl;
    }

    /**
     * Minimizes the multi argument SearchApiFunctionUrl by only asking for query and using default values from DefaultSearchValues class.
     * @param q Specifies the query to search YouTube for.
     * @return URL to Search-list function(YoutubeApi-v3) in string format.
     */
    static String searchApiFunctionUrl(String q, String pageToken)
    {
        return searchApiFunctionUrl(defaultSearchValues.maxResults, defaultSearchValues.order, defaultSearchValues.pubAfter, q, defaultSearchValues.relevanceLanguage, defaultSearchValues.type, pageToken);
    }

    /**
     * Creates a URL to Videos-list function(YouTubeApi-v3).
     * @param id YouTube Video ID to call Videos function for.
     * @return URL to Videos-list function(YoutubeApi-v3) in string format.
     */
    static String VideoApiFunctionUrl(String id)
    {
        String baseUrl = "https://youtube.googleapis.com/youtube/v3/videos?part=" + "snippet" + "%2C" + "statistics";
        baseUrl += "&id=" + id;
        baseUrl += "&key=" + constValues.API_KEY;
        return baseUrl;
    }

    /**
     * Creates a URL to CommentThreads-list function(YouTubeApi-v3) to a particular VideoId
     * @param maxResults self Explanatory cmon(Number of Comments to return. doesn't seem to go higher than 100)
     * @param moderationStatus published/heldForReview/likelySpam/rejected
     * @param order time/relevance
     * @param textFormat plaintext/html
     * @param videoId Videos for which to get the Comments.
     * @return URL to get a CommentThreadListResponse.
     */
    static String commentThreadApiFunctionUrl(int maxResults, String moderationStatus, String order, String textFormat, String videoId, String pageToken)
    {
        String baseUrl = "https://youtube.googleapis.com/youtube/v3/commentThreads?part=" + "snippet" + "%2C" + "id";
        baseUrl += "&maxResults=" + maxResults;
        baseUrl += "&moderationStatus=" + moderationStatus;
        baseUrl += "&order=" + order;
        baseUrl += "&textFormat=" + textFormat;
        baseUrl += "&videoId=" + videoId;
        baseUrl += "&key=" + constValues.API_KEY;
        if (!pageToken.equals("null"))
            baseUrl += "&pageToken=" + pageToken;
        return baseUrl;
    }

    static String commentThreadApiPageTokenUrl(String videoId, String pageToken)
    {
        return commentThreadApiFunctionUrl(commentDefaultValues.maxResults, commentDefaultValues.moderationStatus, commentDefaultValues.order, commentDefaultValues.textFormat, videoId, pageToken);
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
