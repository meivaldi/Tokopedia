package com.tokopedia.testproject.problems.news.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tokopedia.testproject.R;
import com.tokopedia.testproject.problems.news.model.Article;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter implements Filterable {
    private List<Article> articleList;
    private List<Article> articleListFiltered;

    private int visibleThreshold = 20;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    NewsAdapter(List<Article> articleList, RecyclerView recyclerView) {
        setArticleList(articleList);

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {

                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }

                        loading = true;
                    }
                }
            });
        }
    }

    void setArticleList(List<Article> articleList) {
        if (articleList == null && articleListFiltered == null) {
            this.articleList = new ArrayList<>();
            this.articleListFiltered = new ArrayList<>();
        } else {
            this.articleList = articleList;
            this.articleListFiltered = articleList;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder viewHolder;

        if (i == VIEW_ITEM) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_news,
                    viewGroup, false);
            viewHolder = new NewsViewHolder(v);
        } else {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.progressbar, viewGroup, false);

            viewHolder = new ProgressBarViewHolder(v);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        if (holder instanceof NewsViewHolder) {
            Article article = articleListFiltered.get(i);

            ((NewsViewHolder) holder).bind(article);
        } else {
            ((ProgressBarViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return articleListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String string = constraint.toString();
                if (string.isEmpty())
                    articleListFiltered = articleList;
                else {
                    List<Article> filteredList = new ArrayList<>();
                    for (Article article: articleList) {
                        if (article.getTitle().toLowerCase().contains(string.toLowerCase()))
                            filteredList.add(article);
                    }

                    articleListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = articleListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                articleListFiltered = (ArrayList<Article>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemViewType(int position) {
        return articleListFiltered.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    public void setLoaded() {
        loading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.onLoadMoreListener = listener;
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvTitle;
        TextView tvDescription;
        TextView tvDate;

        NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvDate = itemView.findViewById(R.id.tvDate);
        }

        void bind(Article article) {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy");
            Date date = null;
            try {
                date = inputFormat.parse(article.getPublishedAt());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String formattedDate = outputFormat.format(date);
            System.out.println(formattedDate);

            Glide.with(itemView).load(article.getUrlToImage()).into(imageView);
            tvTitle.setText(article.getTitle());
            tvDescription.setText(article.getDescription());
            tvDate.setText(formattedDate);
        }
    }

    class ProgressBarViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressBarViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar1);
        }
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

}
