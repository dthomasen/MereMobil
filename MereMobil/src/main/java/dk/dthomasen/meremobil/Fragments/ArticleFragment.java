package dk.dthomasen.meremobil.Fragments;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.bican.wordpress.Page;

import dk.dthomasen.meremobil.R;

public class ArticleFragment extends Fragment{

    Page page;

    public ArticleFragment(Page page) {
        this.page = page;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.article_view,
                container, false);
        TextView titleText = (TextView) view.findViewById(R.id.articleTitle);
        titleText.setText(page.getTitle());
        TextView articleText = (TextView) view.findViewById(R.id.articleText);
        Log.i("ArticleFragment", page.getMt_text_more());
        articleText.setText(Html.fromHtml(page.getMt_text_more()));
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
