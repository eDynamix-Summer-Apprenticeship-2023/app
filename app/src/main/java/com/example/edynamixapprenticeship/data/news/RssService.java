package com.example.edynamixapprenticeship.data.news;

import com.example.edynamixapprenticeship.model.news.RssFeed;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RssService {
    @GET
    Observable<RssFeed> getRssFeed(@Url String url);
}
