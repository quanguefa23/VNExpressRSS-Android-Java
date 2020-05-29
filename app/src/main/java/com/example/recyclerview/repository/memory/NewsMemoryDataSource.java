package com.example.recyclerview.repository.memory;

import com.example.recyclerview.entity.News;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Persistent data through configuration change or shutting down internet
 */
@Singleton
public class NewsMemoryDataSource {
    private String topicName;
    private List<News> listNews;

    @Inject
    NewsMemoryDataSource() {
        listNews = new ArrayList<>();
    }

    public void updateData(List<News> listNews, String topicName) {
        this.listNews = listNews;
        this.topicName = topicName;
    }

    public List<News> getNewsMemoryData() {
        return listNews;
    }

    public boolean isDataAvailable(String topicName) {
        if (this.topicName == null || topicName == null)
            return false;
        return this.topicName.equals(topicName);
    }
}
