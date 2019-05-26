package com.tokopedia.testproject.problems.news.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.testproject.R;
import com.tokopedia.testproject.problems.news.model.Article;
import com.tokopedia.testproject.problems.news.presenter.NewsPresenter;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements com.tokopedia.testproject.problems.news.presenter.NewsPresenter.View {

    private static final int ARTICLE_LENGTH = 20;

    private NewsPresenter newsPresenter;
    private NewsAdapter newsAdapter;

    private Dialog retryDialog;
    private ProgressDialog progressDialog;
    private EditText searchET;
    private RelativeLayout mainView, emptyState;

    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;

    protected Handler handler;
    private LinearLayoutManager mLayoutManager;
    private List<Article> articleList, articleListNews;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        handler = new Handler();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait...");

        retryDialog = new Dialog(this);
        retryDialog.setContentView(R.layout.refresh_dialog);
        retryDialog.setCancelable(false);
        RelativeLayout retry = retryDialog.findViewById(R.id.retry);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retryDialog.dismiss();
                newsPresenter.getEverything("android");
            }
        });

        mainView = findViewById(R.id.main_view);
        emptyState = findViewById(R.id.empty);

        newsPresenter = new NewsPresenter(this);
        articleList = new ArrayList<>();
        articleListNews = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        newsAdapter = new NewsAdapter(articleListNews, recyclerView);
        /*newsAdapter.setOnLoadMoreListener(new NewsAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                articleListNews.add(null);
                newsAdapter.notifyItemInserted(articleListNews.size() - 1);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        articleListNews.remove(articleListNews.size() - 1);
                        newsAdapter.notifyItemRemoved(articleListNews.size());

                        int start = articleListNews.size();
                        int end = start + ARTICLE_LENGTH;

                        for (int i=0; i<= end; i++) {
                            articleListNews.add(articleList.get(i));

                            newsAdapter.notifyItemInserted(articleListNews.size());
                        }

                        newsAdapter.setLoaded();
                    }
                }, 2000);
            }
        });*/

        recyclerView.setAdapter(newsAdapter);

        viewPager = findViewById(R.id.view_pager);
        dotsLayout = findViewById(R.id.layoutDots);

        layouts = new int[]{
                R.layout.headline1,
                R.layout.headline2,
                R.layout.headline3,
                R.layout.headline4,
                R.layout.headline5 };

        addBottomDots(0);
        viewPagerAdapter = new ViewPagerAdapter(newsAdapter);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        progressDialog.show();
        newsPresenter.getEverything("android");

        searchET = findViewById(R.id.search);
        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                newsAdapter.getFilter().filter(s);
            }
        });
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            if (position == layouts.length - 1) {

            } else {

            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void addBottomDots(int current) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[i]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[current].setTextColor(colorsActive[current]);
    }

    @Override
    public void onSuccessGetNews(List<Article> articles) {
        progressDialog.dismiss();

        for (Article article: articles) {
            articleList.add(article);
        }

        for (int i=0; i<ARTICLE_LENGTH; i++) {
            articleListNews.add(articleList.get(i));
        }

        newsAdapter.notifyDataSetChanged();

        if (articles.size() == 0) {
            emptyState.setVisibility(View.VISIBLE);
            mainView.setVisibility(View.INVISIBLE);
        } else {
            emptyState.setVisibility(View.INVISIBLE);
            mainView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onErrorGetNews(Throwable throwable) {
        progressDialog.dismiss();
        emptyState.setVisibility(View.VISIBLE);
        mainView.setVisibility(View.INVISIBLE);
        retryDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        newsPresenter.unsubscribe();
    }

    public class ViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;
        private NewsAdapter adapter;

        public ViewPagerAdapter(NewsAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            ImageView image = view.findViewById(R.id.image);
            TextView title = view.findViewById(R.id.title);

            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
