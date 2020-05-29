package com.example.recyclerview.repository;

import com.example.recyclerview.entity.AppContract;
import com.example.recyclerview.entity.News;
import com.example.recyclerview.presentation.ShowHeadline.ShowHeadlinePresenter;
import com.example.recyclerview.repository.memory.NewsMemoryDataSource;
import com.example.recyclerview.repository.remote.NewsRemoteDataSource;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * News Repository, persistent data of the app
 */
@Singleton
public class NewsRepository {
    private final NewsMemoryDataSource newsMemoryDataSource;
    private final NewsRemoteDataSource newsRemoteDataSource;

    @Inject
    NewsRepository(NewsMemoryDataSource newsMemoryDataSource, NewsRemoteDataSource newsRemoteDataSource) {
        this.newsMemoryDataSource = newsMemoryDataSource;
        this.newsRemoteDataSource = newsRemoteDataSource;
    }

    public void getNewsData(final String topicName,
                            final ShowHeadlinePresenter.OnDataChangedListener callback,
                            boolean isInternet) {
        // get data in memory if available
        if (newsMemoryDataSource.isDataAvailable(topicName)) {
            callback.onDataChanged(newsMemoryDataSource.getNewsMemoryData());
            return;
        }

        // get data remote if internet available
        if (isInternet) {
            OnGetRemoteDataListener newCallback = new OnGetRemoteDataListener() {
                @Override
                public void onGetDataSuccess(List<News> list) {
                    newsMemoryDataSource.updateData(list, topicName); // update in-memory data
                    callback.onDataChanged(list);
                }
            };

            String url = getURLFromTopicName(topicName);
            newsRemoteDataSource.getNewsRemoteData(url, newCallback);
        }
    }

    private String getURLFromTopicName(String topicName) {
        return AppContract.URLS.get(topicName);
    }

    public interface OnGetRemoteDataListener {
        void onGetDataSuccess(List<News> list);
    }
}