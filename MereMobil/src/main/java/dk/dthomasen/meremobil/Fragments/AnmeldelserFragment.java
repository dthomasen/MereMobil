package dk.dthomasen.meremobil.Fragments;

/* test */
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dk.dthomasen.meremobil.R;

public class AnmeldelserFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_anmeldelser,
                container, false);
        return view;
    }
}
