package org.app.mbooster.Redemption_tabs;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.app.mbooster.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GoldFragment extends Fragment {


    public GoldFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gold, container, false);
    }

}
