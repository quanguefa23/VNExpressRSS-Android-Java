package com.example.recyclerview.presentation.ShowHeadline;

import com.example.recyclerview.entity.AppContract;
import com.example.recyclerview.entity.News;
import com.example.recyclerview.repository.NewsRepository;

import java.util.List;

/**
 * Presenter for ShowHeadline activity
 */
public class ShowHeadlinePresenter implements ShowHeadlineContract.Presenter {

    private ShowHeadlineContract.View view;
    private NewsRepository newsRepository;
    private int positionNextCard = 0;

    ShowHeadlinePresenter(ShowHeadlineContract.View mainMvpView) {
        view = mainMvpView;
    }

    @Override
    public void start() {
        newsRepository = view.getApplicationGraph().newsRepository();
    }

    @Override
    public void stop() {
        view = null;
    }

    public interface OnDataChangedListener {
        void onDataChanged(List<News> list);
    }

    public void getNewsData(String topicName) {
        OnDataChangedListener callback = new OnDataChangedListener() {
            @Override
            public void onDataChanged(List<News> list) {
                view.updateRecycleView(list);
            }
        };

        newsRepository.getNewsData(topicName, callback, view.isInternet());
    }

    // dummy implement
    @Override
    public boolean loadMore(List<News> listNews) {
        if (listNews != null && !listNews.isEmpty()) {
            listNews.add(listNews.get((positionNextCard++) % listNews.size()));
            return true;
        }
        return false;
    }

    @Override
    public String getTitle(String topicName) {
        return AppContract.TITLES.get(topicName);
    }
}
