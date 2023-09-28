package myproject.icarus;

import java.util.Date;
import java.util.List;

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
    List<Comment> Comments;

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
