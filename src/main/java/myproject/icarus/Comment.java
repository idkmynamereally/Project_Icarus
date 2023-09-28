package myproject.icarus;

public class Comment
{
    String commentId;
    String channelId;
    String videoId;
    String textDisplay;
    int likeCount;
    String authorName;
    String publishedAt;

    Comment(String commentId, String channelId, String videoId, String textDisplay, int likeCount, String authorName, String publishedAt)
    {
        this.commentId = commentId;
        this.channelId = channelId;
        this.videoId = videoId;
        this.textDisplay = textDisplay;
        this.likeCount = likeCount;
        this.authorName = authorName;
        this.publishedAt = publishedAt;
    }

    @Override
    public String toString()
    {
        String op = "Comment : " + textDisplay + "\nAuthor Name : " + authorName + "\nLikes : " + likeCount;
        return op;
    }
}
