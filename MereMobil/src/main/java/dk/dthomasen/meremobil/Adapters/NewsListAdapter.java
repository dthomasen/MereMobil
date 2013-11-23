package dk.dthomasen.meremobil.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.bican.wordpress.Page;

import java.util.List;

import dk.dthomasen.meremobil.R;

public class NewsListAdapter extends ArrayAdapter<Page> {

    public NewsListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        // TODO Auto-generated constructor stub
    }

    private List<Page> items;

    public NewsListAdapter(Context context, int resource, List<Page> items) {

        super(context, resource, items);

        this.items = items;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {

            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.news_list, null);

        }

        Page p = items.get(position);

        if (p != null) {

            TextView tt = (TextView) v.findViewById(R.id.id);
            TextView tt3 = (TextView) v.findViewById(R.id.description);

            if (tt != null) {
                tt.setText(p.getTitle());
            }
            if (tt3 != null) {

                tt3.setText(p.getDescription().replaceAll("\\<.*?\\>", ""));
            }
        }

        return v;

    }
}

