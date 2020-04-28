package com.example.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class ShowHeadlineActivity extends AppCompatActivity {

    List<News> listNews;
    RecyclerView recyclerView;
    NewsAdapter adapter;
    View emptyView;
    int count;
    int fixedSize;
    String topicName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_headline);

        getTopicName();
        changeTitle();
        mapWidget();
        prepareRecyclerView();

        if (isInternetAvailable())
            getNewsData();
    }

    private void getTopicName() {
        topicName = getIntent().getStringExtra("topic");
    }

    private void changeTitle() {
        TextView titleTV = findViewById(R.id.activity_title);
        titleTV.setText(RSSContract.TITLES.get(topicName));
    }

    public boolean isInternetAvailable() {
        if (!isNetworkConnected())
            return false;

        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm != null && cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isConnected();
    }

    private void mapWidget() {
        recyclerView = findViewById(R.id.recyclerView);
        emptyView = findViewById(R.id.empty_view);
    }

    private void prepareRecyclerView() {
        listNews = new ArrayList<>();

        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new NewsAdapter(this, R.layout.layout_news, listNews);
        recyclerView.setAdapter(adapter);

        loadMoreWhenScrollToEnd(gridLayoutManager);
        setOnClickItem();

    }

    private void setOnClickItem() {
        adapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Intent intent = new Intent(ShowHeadlineActivity.this, ReadNewsActivity.class);
                intent.putExtra("link", listNews.get(position).getLink());
                startActivity(intent);
            }
        });
    }

    private void loadMoreWhenScrollToEnd(StaggeredGridLayoutManager gridLayoutManager) {
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (fixedSize != 0) {
                    listNews.add(listNews.get((count++) % fixedSize));
                    adapter.notifyItemInserted(listNews.size() - 2);
                }
            }
        });
    }

    private void getNewsData() {
        String url = RSSContract.URLS.get(topicName);
        new ReadRSS().execute(url);
    }

    private class ReadRSS extends AsyncTask<String, Void, List<News>> {

        @Override
        protected List<News> doInBackground(String... strings) {
            StringBuilder stringBuilder = new StringBuilder();
            try {
                URL url = new URL(strings[0]);
                URLConnection urlConnection = url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                bufferedReader.close();

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            String s = stringBuilder.toString();
            XMLDOMParser parser = new XMLDOMParser();
            Document document = parser.getDocument(s);
            NodeList nodeList = document.getElementsByTagName("item");

            List<News> list = new ArrayList<>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                String title = parser.getValue(element, "title");
                String link = parser.getValue(element, "link");

                String sumNode = parser.getValueSummary(element, "description");
                String token[] = sumNode.split("</br>");
                String summary = token.length > 1 ? token[1] : "";

                list.add(new News(title, summary, link));
            }

            return list;
        }

        @Override
        protected void onPostExecute(List<News> list) {
            super.onPostExecute(list);

            if (list == null)
                return;

            listNews.addAll(list);
            fixedSize = listNews.size();
            if (!listNews.isEmpty() && emptyView.getVisibility() == View.VISIBLE)
                emptyView.setVisibility(View.INVISIBLE);

            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(
                    ShowHeadlineActivity.this, R.anim.layout_animation_from_bottom);
            recyclerView.setLayoutAnimation(animation);

            adapter.notifyDataSetChanged();
        }
    }
}
