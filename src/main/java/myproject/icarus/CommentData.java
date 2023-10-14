package myproject.icarus;

public class CommentData
{
    String commentId;
    String channelId;
    String videoId;
    String textOriginal;
    int likeCount;
    String authorName;
    String publishedAt;

    CommentData()
    {
        this.commentId = "null";
        this.channelId = "null";
        this.videoId = "null";
        this.textOriginal = "null";
        this.likeCount = -1;
        this.authorName = "null";
        this.publishedAt = "null";
    }

    CommentData(String commentId, String channelId, String videoId, String textDisplay, int likeCount, String authorName, String publishedAt)
    {
        this.commentId = commentId;
        this.channelId = channelId;
        this.videoId = videoId;
        this.textOriginal = textDisplay;
        this.likeCount = likeCount;
        this.authorName = authorName;
        this.publishedAt = publishedAt;
    }

    @Override
    public String toString()
    {
        String op = "Comment : " + textOriginal + "\nAuthor Name : " + authorName + "\nLikes : " + likeCount + "\nPublished At : " + publishedAt;
        return op;
    }
}
