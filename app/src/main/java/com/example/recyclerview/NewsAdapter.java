package com.example.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private int layout;
    private List<News> listNews;
    private Context context;

    public NewsAdapter(Context context, int layout, List<News> listNews) {
        this.context = context;
        this.layout = layout;
        this.listNews = listNews;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(layout, parent, false);
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        News news = listNews.get(position);
        holder.titleTV.setText(news.getTitle());
        holder.summaryTV.setText(news.getSummary());

        Animation ani = AnimationUtils.loadAnimation(context, R.anim.item_animation_scale);
        holder.containerCV.startAnimation(ani);
    }

    @Override
    public int getItemCount() {
        return listNews.size();
    }

    // Define listener member variable
    private OnItemClickListener listener;
    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleTV;
        TextView summaryTV;
        CardView containerCV;

        ViewHolder(final View itemView) {
            super(itemView);
            titleTV = itemView.findViewById(R.id.title);
            summaryTV = itemView.findViewById(R.id.summary);
            containerCV = itemView.findViewById(R.id.container);

            // Setup the click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(itemView, position);
                        }
                    }
                }
            });
        }
    }
}