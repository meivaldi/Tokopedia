package com.tokopedia.testproject.problems.news.network;

import com.tokopedia.testproject.problems.news.model.NewsResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsService {
    @GET("everything")
    Observable<NewsResult> getEverything(@Query("q") String query, @Query("apiKey") String apiKey, @Query("pageSize") int size,
                                         @Query("sortBy") String sort);

    @GET("top-headlines")
    Observable<NewsResult> getHeadlines(@Query("country") String country, @Query("apiKey") String apiKey,
                                        @Query("pageSize") int pageSize);
}
