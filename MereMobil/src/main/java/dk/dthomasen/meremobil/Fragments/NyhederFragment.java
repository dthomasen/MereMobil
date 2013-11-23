package dk.dthomasen.meremobil.Fragments;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import net.bican.wordpress.Page;

import java.util.List;

import dk.dthomasen.meremobil.Adapters.NewsListAdapter;
import dk.dthomasen.meremobil.R;
import dk.dthomasen.meremobil.interfaces.AsyncResponse;
import dk.dthomasen.meremobil.service.FetchRecentPosts;

public class NyhederFragment extends Fragment implements AsyncResponse, AdapterView.OnItemClickListener {
    FetchRecentPosts recentPostsTask;
    List<Page> recentPosts;
    Dialog progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nyheder,
                container, false);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i("MainActivity", "onCreate called");
        super.onCreate(savedInstanceState);
        progress = ProgressDialog.show(getActivity(), "Henter artikeloversigt", "Vent venligst...");
        recentPostsTask = new FetchRecentPosts(getActivity());
        recentPostsTask.delegate = this;
        recentPostsTask.execute(20);
    }

    @Override
    public void processFinish(List<Page> output) {
        recentPosts = output;
        ListView newsList = (ListView) getActivity().findViewById(R.id.NyhederList);
        NewsListAdapter customAdapter = new NewsListAdapter(getActivity(), R.layout.news_list, output);
        newsList.setAdapter(customAdapter);
        progress.dismiss();
        newsList.setOnItemClickListener(this);
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
        Log.i("Main", "Onresume called");
        super.onResume();
        if(recentPosts != null){
            ListView newsList = (ListView) getActivity().findViewById(R.id.NyhederList);
            NewsListAdapter customAdapter = new NewsListAdapter(getActivity(), R.layout.news_list, recentPosts);
            newsList.setAdapter(customAdapter);
            newsList.setOnItemClickListener(this);
        }
    }
}
