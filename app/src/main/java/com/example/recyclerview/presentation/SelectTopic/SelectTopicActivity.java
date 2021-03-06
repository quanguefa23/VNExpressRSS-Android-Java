package com.example.recyclerview.presentation.SelectTopic;

import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.recyclerview.R;
import com.example.recyclerview.presentation.ShowHeadline.ShowHeadlineActivity;

/**
 * Shows all topics for users' choice
 * This simple activity does not need a presenter
 */
public class SelectTopicActivity extends AppCompatActivity{
    @UiThread
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_topic);
    }

    public void onClickCardView(View view) {
        Intent intent = new Intent(SelectTopicActivity.this, ShowHeadlineActivity.class);
        intent.putExtra("topic", getStringId(view));
        startActivity(intent);
    }

    //get id of View in String format (ex: "button_1")
    private String getStringId(View view) {
        if (view.getId() == View.NO_ID)
            return "no-id";
        String[] id = view.getResources().getResourceName(view.getId()).split("/");
        return id[1];
    }
}