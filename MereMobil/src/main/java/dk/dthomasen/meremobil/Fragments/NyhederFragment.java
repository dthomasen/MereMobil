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

public class NyhederFragment extends Fragment implements AsyncResponse, AdapterView.OnItemClickListener, AbsListView.OnScrollListener {
    FetchRecentPosts recentPostsTask;
    List<Page> recentPosts = new ArrayList<Page>();
    Dialog progress;
    SharedPreferences prefs;
    Type pageListType = new TypeToken<List<Page>>(){}.getType();
    private NewsListAdapter customAdapter;
    private boolean loading;
    private int previousTotal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nyheder,
                container, false);
        ListView newsList = (ListView) view.findViewById(R.id.NyhederList);
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
    }

    @Override
    public void processFinish(List<Page> output) {
        ListView newsList = (ListView) getActivity().findViewById(R.id.NyhederList);
        if(customAdapter == null){
            customAdapter = new NewsListAdapter(getActivity(), R.layout.news_list, recentPosts);
            newsList.setAdapter(customAdapter);
        }else{
            customAdapter.addAll(output);
            customAdapter.notifyDataSetChanged();
        }
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("recentNews", new Gson().toJson(recentPosts));
        editor.apply();
        loading = false;
        progress.dismiss();
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
            newsList.setOnItemClickListener(this);
        }
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (loading) {
            if (totalItemCount > previousTotal) {
                previousTotal = totalItemCount;
            }
        }
        if (!loading && (totalItemCount - visibleItemCount) <= firstVisibleItem) {
            Log.i("MaiN", "Totalitemcount= "+totalItemCount);
            if(totalItemCount == 0){
                if(recentPosts.size() == 0){
                    loading = true;
                    progress = ProgressDialog.show(getActivity(), "Henter artikeloversigt", "Vent venligst...");
                    recentPostsTask = new FetchRecentPosts(getActivity());
                    recentPostsTask.delegate = this;
                    recentPostsTask.execute(15,0);
                }
            }else{
                loading = true;
                progress = ProgressDialog.show(getActivity(), "Henter artikeloversigt", "Vent venligst..."+totalItemCount);
                recentPostsTask = new FetchRecentPosts(getActivity());
                recentPostsTask.delegate = this;
                recentPostsTask.execute(totalItemCount+5,recentPosts.size());
            }
         }
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

}
