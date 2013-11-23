package dk.dthomasen.meremobil.service;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import net.bican.wordpress.Page;
import net.bican.wordpress.Wordpress;

import java.util.ArrayList;
import java.util.List;

import dk.dthomasen.meremobil.R;
import dk.dthomasen.meremobil.interfaces.AsyncResponse;

/**
 * Created by Dennis on 16-11-13.
 */
public class FetchRecentPosts extends AsyncTask<Integer, Void, List<Page>> {

    Activity caller;
    public AsyncResponse delegate=null;

    public FetchRecentPosts(Activity caller){
       this.caller = caller;
    }

    @Override
    protected List<Page> doInBackground(Integer... params) {
        try {
            System.setProperty("org.xml.sax.driver", "org.xmlpull.v1.sax2.Driver");
            String username = caller.getResources().getString(R.string.username);
            String password = caller.getResources().getString(R.string.password);
            String xmlRpcUrl = caller.getResources().getString(R.string.xmlrpcurl);
            Wordpress wp = new Wordpress(username, password, xmlRpcUrl);
            List<Page> recentPosts = wp.getRecentPosts(params[0]);
            Log.i("Fetch","Recentposts: "+recentPosts.size());
            List<Page> returnList = new ArrayList<Page>();

            for (int i = params[1]; i < recentPosts.size(); i++){ //@TODO REFACTOR
               returnList.add(recentPosts.get(i));
               Log.i("Fetch", "Adding element");
            }
            Log.i("Fetch", "Returning: "+returnList.size());
            return returnList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Page> pages) {
        super.onPostExecute(pages);
        delegate.processFinish(pages);
    }
}
