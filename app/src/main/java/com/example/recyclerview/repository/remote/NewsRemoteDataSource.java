package com.example.recyclerview.repository.remote;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.recyclerview.entity.News;
import com.example.recyclerview.repository.NewsRepository;
import com.example.recyclerview.support.SingleTaskExecutor;

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

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.example.recyclerview.application.MyApplication.APP_TAG;

/**
 * Fetch RSS data via http and parse XML into list news
 */
@Singleton
public class NewsRemoteDataSource {
    @Inject
    NewsRemoteDataSource() {
    }

    public void getNewsRemoteData(final String urlString, final NewsRepository.OnGetRemoteDataListener callback) {
        // create a liveData to wrap listNews and observe data change
        final MutableLiveData<List<News>> listNewsLive = new MutableLiveData<>();
        listNewsLive.observeForever(new Observer<List<News>>() {
            @Override
            public void onChanged(List<News> list) {
                callback.onGetDataSuccess(list);
            }
        });

        // fetch data, utilize single executor service
        SingleTaskExecutor.queueRunnable(new Runnable() {
            @Override
            public void run() {
                String xmlString = getXMLStringFromUrl(urlString);

                if(xmlString.equals("")) {
                    Log.d(APP_TAG, "Can not get RSS data");
                    return;
                }

                List<News> list = getListNewsFromXMLString(xmlString);
                listNewsLive.postValue(list);
            }

            private List<News> getListNewsFromXMLString(String xmlString) {
                XMLDOMParser parser = new XMLDOMParser();
                Document document = parser.getDocument(xmlString);
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

            private String getXMLStringFromUrl(String urlString) {
                StringBuilder stringBuilder = new StringBuilder();

                try {
                    URL url = new URL(urlString);
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
                    return "";
                }

                return stringBuilder.toString();
            }
        });
    }
}
