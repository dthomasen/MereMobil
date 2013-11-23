package dk.dthomasen.meremobil.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import net.bican.wordpress.Page;

import dk.dthomasen.meremobil.R;

public class ArticleFragment extends Fragment{

    Page page;
    WebView articleText;

    public ArticleFragment(Page page) {
        this.page = page;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.article_view,
                container, false);
        articleText = (WebView) view.findViewById(R.id.articleText);
        articleText.getSettings().setJavaScriptEnabled(true);
        articleText.setWebViewClient(new WebViewClient() {
            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                articleText.loadUrl("javascript:(function() { " +
                        "document.getElementById('header').style.display = 'none'; " +
                        "})()");
                articleText.loadUrl("javascript:(function() { " +
                        "document.getElementById('cookie-law-info-again').style.display = 'none'; " +
                        "})()");
                articleText.loadUrl("javascript:(function() { " +
                        "document.getElementById('switch').style.display = 'none'; " +
                        "})()");
            }
        });
        articleText.loadUrl(page.getLink());
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
