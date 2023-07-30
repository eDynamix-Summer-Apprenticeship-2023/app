package com.example.edynamixapprenticeship.model.news;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "channel", strict = false)
public class Channel {

    @ElementList(name = "item", inline = true)
    private List<RssItem> rssItems;

    public List<RssItem> getRssItems() {
        return rssItems;
    }

    public void setRssItems(List<RssItem> rssItems) {
        this.rssItems = rssItems;
    }
}
