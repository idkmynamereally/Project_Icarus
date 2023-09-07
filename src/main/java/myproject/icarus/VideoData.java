package myproject.icarus;

import java.util.Date;
public class VideoData
{
    String videoTitle;
    String channelTitle;
    String id;
    String uploadDate;
    int likes;
    int views;
    int commentCount;

    VideoData(String videoTitle, String channelTitle, String id, String uploadDate, int likes, int views, int commentCount){
        this.videoTitle = videoTitle;
        this.channelTitle= channelTitle;
        this.id = id;
        this.uploadDate = uploadDate;
        this.likes = likes;
        this.views = views;
        this.commentCount = commentCount;
    }

    @Override
    public String toString() {
        return "Video Title : " + videoTitle +"\nChannel Title : " + channelTitle + "\nLikes : " + likes + "\nViews : " + views + "\nComment Count : " + commentCount + "\nDate : " + uploadDate;
    }
    public String getVideoTitle() {
        return videoTitle;
    }
    public String getUploadDate() {
        return uploadDate;
    }
    public int getCommentCount() {
        return commentCount;
    }
    public int getLikes() {
        return likes;
    }
    public int getViews() {
        return views;
    }
    public String getChannelTitle() {
        return channelTitle;
    }
    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }
    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setLikes(int likes) {
        this.likes = likes;
    }
    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }
    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }
    public void setViews(int views) {
        this.views = views;
    }
}
