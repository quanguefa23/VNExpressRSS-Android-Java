package com.example.recyclerview.presentation.ShowHeadline;

import com.example.recyclerview.application.ApplicationGraph;
import com.example.recyclerview.entity.News;
import com.example.recyclerview.presentation.BasePresenter;
import com.example.recyclerview.presentation.BaseView;

import java.util.List;

/**
 * Contract for ShowHeadline activity
 */
public interface ShowHeadlineContract {

    interface Presenter extends BasePresenter {
        String getTitle(String topicName);

        void getNewsData(String topicName);

        boolean loadMore(List<News> listNews);
    }

    interface View extends BaseView {
        ApplicationGraph getApplicationGraph();

        void updateRecycleView(List<News> list);

        boolean isInternet();
    }
}
