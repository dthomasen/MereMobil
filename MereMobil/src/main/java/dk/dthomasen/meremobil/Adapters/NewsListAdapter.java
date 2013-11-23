package dk.dthomasen.meremobil.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.bican.wordpress.Page;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import dk.dthomasen.meremobil.R;

public class NewsListAdapter extends ArrayAdapter<Page> {

    public NewsListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    private List<Page> items;

    public NewsListAdapter(Context context, int resource, List<Page> items) {

        super(context, resource, items);

        this.items = items;

        Collections.sort(this.items, new Comparator<Page>() {
            public int compare(Page obj1, Page obj2) {
                return obj2.getDateCreated().compareTo(obj1.getDateCreated());
            }
        });

    }

    @Override
    public void add(Page object) {
        super.add(object);
        Collections.sort(this.items, new Comparator<Page>() {
                    public int compare(Page obj1, Page obj2) {
                        return obj2.getDateCreated().compareTo(obj1.getDateCreated());
                    }
                });
        super.notifyDataSetChanged();
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

            TextView title = (TextView) v.findViewById(R.id.title);
            TextView description = (TextView) v.findViewById(R.id.description);

            if (title != null) {
                title.setText(p.getTitle());
            }
            if (description != null) {

                description.setText(p.getDescription().replaceAll("\\<.*?\\>", ""));
            }
        }

        return v;

    }
}

