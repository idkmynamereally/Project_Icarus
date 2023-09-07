package myproject.icarus;

public class Comment
{
    Comment(String comment, String authorName, int likes)
    {
        this.comment = comment;
        this.authorName = authorName;
        this.likes = likes;
    }
    String comment;
    String authorName;
    int likes;

    @Override
    public String toString()
    {
        String op = "Comment : " + comment + "\nAuthor Name : " + authorName + "\nLikes : " + likes;
        return op;
    }
}
