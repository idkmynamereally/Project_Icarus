package myproject.icarus;

import java.util.List;

/**
 * Class to imitate a YouTube video with its data.
 */
public class VideoData
{
    Channel channel;
    String videoTitle;
    String videoId;
    String publishedAt;
    String description;
    String thumbnailUrl;
    String audioLanguage;
    int likeCount;
    int viewCount;
    int commentCount;
    List<CommentData> comments;

    /**
     * Initializes the object with invalid values.
     */
    VideoData()
    {
        videoTitle = "null";
        videoId = "null";
        publishedAt = "null";
        description = "null";
        thumbnailUrl = "null";
        audioLanguage = "null";
        likeCount = -1;
        viewCount = -1;
        commentCount = -1;
        channel = new Channel();
    }

    /**
     * Overridden toString to print values to console.
     * @return String with VideoData values.
     */
    @Override
    public String toString()
    {
        return  "Video Title : " + videoTitle +
                "\nChannel Name : " + channel.channelName +
                "\nLikes : " + likeCount +
                "\nViews : " + viewCount +
                "\nComment Count : " + commentCount +
                "\nUpload Date : " + publishedAt+
                "\nThumbnail : " + thumbnailUrl+
                "\nLanguage : " + audioLanguage+
                "\nLikeCount : " + likeCount+
                "\nViewCount : " + viewCount+
                "\nCommentCount : " + commentCount;
    }
}
