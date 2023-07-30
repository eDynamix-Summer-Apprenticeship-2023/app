package com.example.edynamixapprenticeship.ui.news;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edynamixapprenticeship.R;
import com.example.edynamixapprenticeship.model.news.RssItem;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private final List<RssItem> rssItems;

    public NewsAdapter(List<RssItem> rssItems) {
        this.rssItems = rssItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rss_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RssItem rssItem = rssItems.get(position);

        holder.title.setText(rssItem.getTitle());
        holder.description.setText(rssItem.getDescription());
    }

    @Override
    public int getItemCount() {
        return rssItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView description;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.rss_item_title);
            description = itemView.findViewById(R.id.rss_item_description);
        }
    }
}
