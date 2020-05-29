package com.example.recyclerview.presentation.ShowHeadline;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.recyclerview.R;
import com.example.recyclerview.application.ApplicationGraph;
import com.example.recyclerview.application.MyApplication;
import com.example.recyclerview.entity.News;
import com.example.recyclerview.presentation.ReadNews.ReadNewsActivity;
import com.example.recyclerview.presentation.ShowHeadline.adapter.EndlessRecyclerViewScrollListener;
import com.example.recyclerview.presentation.ShowHeadline.adapter.NewsAdapter;
import com.example.recyclerview.support.NetworkingChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * Main activity of the app
 * Show list of news with title and summary
 * Handle recycleView Animation
 */
public class ShowHeadlineActivity extends AppCompatActivity implements ShowHeadlineContract.View {
    ShowHeadlineContract.Presenter presenter;
    RecyclerView recyclerView;
    NewsAdapter adapter;
    View emptyView;
    String topicName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_headline);

        injectPresenter();
        mapWidget();
        setTopicName();
        changeTitle();
        prepareRecyclerView();

        // fetch data first time
        presenter.getNewsData(topicName);
    }

    @Override
    public ApplicationGraph getApplicationGraph() {
        return ((MyApplication) getApplicationContext()).applicationGraph;
    }

    private void injectPresenter() {
        presenter = new ShowHeadlinePresenter(this);
        presenter.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.stop();
    }

    private void mapWidget() {
        recyclerView = findViewById(R.id.recyclerView);
        emptyView = findViewById(R.id.empty_view);
    }

    private void setTopicName() {
        topicName = getIntent().getStringExtra("topic");
    }

    private void changeTitle() {
        TextView titleTV = findViewById(R.id.activity_title);
        titleTV.setText(presenter.getTitle(topicName));
    }

    @Override
    public void updateRecycleView(List<News> list) {
        if (list == null || list.isEmpty())
            return;

        emptyView.setVisibility(android.view.View.INVISIBLE);
        adapter.updateDataAndNotify(list);

        // animation
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(
                ShowHeadlineActivity.this, R.anim.layout_animation_from_bottom);
        recyclerView.setLayoutAnimation(animation);
    }

    @Override
    public boolean isInternet() {
        return NetworkingChecker.isInternetAvailable(this);
    }

    private void prepareRecyclerView() {
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new NewsAdapter(this, R.layout.layout_news, new ArrayList<News>());
        recyclerView.setAdapter(adapter);

        loadMoreWhenScrollToEnd(gridLayoutManager);
        setOnClickItem();
    }

    private void loadMoreWhenScrollToEnd(StaggeredGridLayoutManager gridLayoutManager) {
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int oldSize, RecyclerView view) {
                List<News> listNews = adapter.getListNews();
                if (presenter.loadMore(listNews))
                    adapter.notifyItemInserted(listNews.size() - 2);
            }
        });
    }

    private void setOnClickItem() {
        adapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(android.view.View itemView, int position) {
                Intent intent = new Intent(ShowHeadlineActivity.this, ReadNewsActivity.class);
                intent.putExtra("link", adapter.getListNews().get(position).getLink());
                startActivity(intent);
            }
        });
    }
}
