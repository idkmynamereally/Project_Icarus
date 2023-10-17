package myproject.icarus;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class with functions to filter Lists of VideoData on pre-defined basis.
 */
class FilterLists
{
    /**
     * Filters a VideoData list to only contain one video per Channel.
     * @param videos List of VideoData objects to filter.
     * @return Returns the same list with only unique channels.
     */
    public static List<VideoData> filterUniqueChannels(List<VideoData> videos)
    {
        Set<String> channelIds = new HashSet<>();
        for (int i = 0; i < videos.size(); i++)
        {
            VideoData v = videos.get(i);
            if (channelIds.contains(v.channel.channelId))
            {
                videos.remove(v);
                i--;
            }
            else
            {
                channelIds.add(v.channel.channelId);
            }
        }
        return videos;
    }
}