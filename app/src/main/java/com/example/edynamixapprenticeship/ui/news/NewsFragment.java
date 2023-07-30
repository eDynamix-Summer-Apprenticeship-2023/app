package com.example.edynamixapprenticeship.ui.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edynamixapprenticeship.R;
import com.example.edynamixapprenticeship.data.news.RssService;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class NewsFragment extends Fragment {

    private RecyclerView recyclerView;
    private RssService rssService;
    private CompositeDisposable compositeDisposable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rss_fragment, container, false);

        recyclerView = view.findViewById(R.id.news_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://feeds.simplecast.com/54nAGcIl/")
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        rssService = retrofit.create(RssService.class);
        compositeDisposable = new CompositeDisposable();

        fetchRssFeed();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
    }

    private void fetchRssFeed() {
        compositeDisposable.add(rssService.getRssFeed("")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rssFeed ->
                        recyclerView.setAdapter(new NewsAdapter(rssFeed.getChannel().getRssItems())), Throwable::printStackTrace));
    }
}
