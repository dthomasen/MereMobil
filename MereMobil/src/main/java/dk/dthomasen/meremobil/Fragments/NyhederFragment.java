package dk.dthomasen.meremobil.Fragments;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.TextView;

import net.bican.wordpress.Page;

import java.util.List;
import java.util.concurrent.ExecutionException;

import dk.dthomasen.meremobil.R;
import dk.dthomasen.meremobil.interfaces.AsyncResponse;
import dk.dthomasen.meremobil.service.FetchRecentPosts.FetchRecentPosts;

public class NyhederFragment extends Fragment implements AsyncResponse{
    FetchRecentPosts recentPostsTask = new FetchRecentPosts(getActivity());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nyheder,
                container, false);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recentPostsTask.delegate = this;
        recentPostsTask.execute(10);
    }

    @Override
    public void processFinish(List<Page> output) {
        for (Page ou : output){
            Log.i("Main activity", ou.getTitle());
        }
    }
}
