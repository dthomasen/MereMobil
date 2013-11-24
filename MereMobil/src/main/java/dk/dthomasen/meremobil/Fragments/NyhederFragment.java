package dk.dthomasen.meremobil.Fragments;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.bican.wordpress.Page;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import dk.dthomasen.meremobil.Adapters.NewsListAdapter;
import dk.dthomasen.meremobil.R;
import dk.dthomasen.meremobil.interfaces.AsyncResponse;
import dk.dthomasen.meremobil.service.FetchRecentPosts;

public class NyhederFragment extends Fragment implements AsyncResponse, AdapterView.OnItemClickListener, AbsListView.OnScrollListener{
    FetchRecentPosts recentPostsTask;
    List<Page> recentPosts = new ArrayList<Page>();
    Dialog progress;
    SharedPreferences prefs;
    Type pageListType = new TypeToken<List<Page>>(){}.getType();
    private ListView newsList;
    private NewsListAdapter customAdapter;
    private boolean mIsLoading = false;
    private boolean mMoreDataAvailable = true;
    private boolean mWasLoading = false;
    private final int AUTOLOAD_THRESHOLD = 4;
    private int previousTotal = 0;
    private View mFooterView;
    private boolean bottom = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nyheder,
                container, false);
        newsList = (ListView) view.findViewById(R.id.NyhederList);
        newsList.setOnItemClickListener(this);
        newsList.setOnScrollListener(this);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getActivity().getSharedPreferences("MereMobil", Context.MODE_PRIVATE);
        recentPosts = new Gson().fromJson(prefs.getString("recentNews", null), pageListType);
        if(recentPosts == null){
            recentPosts = new ArrayList<Page>();
        }
        mFooterView = LayoutInflater.from(getActivity()).inflate(R.layout.loading_view, null);
        //progress = ProgressDialog.show(getActivity(), "Henter nye artikler", "Vent venligst...");
        //recentPostsTask = new FetchRecentPosts(getActivity());
        //recentPostsTask.delegate = this;
        //recentPostsTask.execute(30);
    }

    @Override
    public void processFinish(List<Page> output) {
        if(previousTotal == output.size()){
            bottom = true;
        }
        if(customAdapter == null){
            customAdapter = new NewsListAdapter(getActivity(), R.layout.news_list, recentPosts);
            newsList.setAdapter(customAdapter);
            customAdapter.setNotifyOnChange(true);
            for (Page page : output){
                if(!containsPage(page)){
                    customAdapter.add(page);
                }
            }
        }else{
            for (Page page : output){
                if(!containsPage(page)){
                    customAdapter.add(page);
                }
            }
        }
        previousTotal = output.size();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("recentNews", new Gson().toJson(recentPosts));
        editor.apply();
        progress.dismiss();
        mIsLoading = false;
        newsList.removeFooterView(mFooterView);
    }

    public boolean containsPage(Page page){
        for(Page existing : recentPosts){
            if(page.getLink().equals(existing.getLink())){
                return true;
            }
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Fragment fragment = new ArticleFragment(recentPosts.get(position));
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack("NyhederFragment")
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(recentPosts != null){
            ListView newsList = (ListView) getActivity().findViewById(R.id.NyhederList);
            NewsListAdapter customAdapter = new NewsListAdapter(getActivity(), R.layout.news_list, recentPosts);
            newsList.setAdapter(customAdapter);
            customAdapter.setNotifyOnChange(true);
            newsList.setOnItemClickListener(this);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
       if(previousTotal == 0 && !mIsLoading){
           mIsLoading = true;
           progress = ProgressDialog.show(getActivity(), "Henter nye artikler", "Vent venligst...");
           recentPostsTask = new FetchRecentPosts(getActivity());
           recentPostsTask.delegate = this;
           recentPostsTask.execute(12);
       }else if (!mIsLoading && mMoreDataAvailable) {
            if (bottom) {
                newsList.removeFooterView(mFooterView);
            } else if (totalItemCount - AUTOLOAD_THRESHOLD <= firstVisibleItem + visibleItemCount) {
                mIsLoading = true;
                newsList.addFooterView(mFooterView, null, false);
                recentPostsTask = new FetchRecentPosts(getActivity());
                recentPostsTask.delegate = this;
                recentPostsTask.execute(totalItemCount+5);
            }
        }
    }
}
