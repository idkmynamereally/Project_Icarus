package myproject.icarus;

/**
 * Class to imitate a YouTube channel with its data.
 */
public class Channel
{
    String channelName;
    String channelId;
    String channelDescription;
    String publishedAt;
    String country;
    int viewCount;
    int commentCount;
    int subCount;
    int videoCount;

    Channel()
    {
        this.channelName = "null";
        this.channelId = "null";
        this.channelDescription = "null";
        this.publishedAt = "null";
        this.country = "null";
        this.viewCount = -1;
        this.commentCount = -1;
        this.subCount = -1;
        this.videoCount = -1;
    }
}
