package org.app.mbooster.activity_folder.Welcome;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.app.mbooster.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link OnBoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OnBoardFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;

    private ImageView imageView;
    public OnBoardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     *
     * @return A new instance of fragment OnBoardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OnBoardFragment newInstance(String param1) {
        OnBoardFragment fragment = new OnBoardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_on_board, container, false);

        imageView = (ImageView) view.findViewById(R.id.imageview);
        if(mParam1.equals("1")){
            imageView.setImageResource(R.drawable.onboarding_01);
        }else if(mParam1.equals("2")){
            imageView.setImageResource(R.drawable.onboarding_02);
        }else if(mParam1.equals("3")){
            imageView.setImageResource(R.drawable.onboarding_03);
        }else if(mParam1.equals("4")){
            imageView.setImageResource(R.drawable.onboarding_04);
        }
        return view;
    }


}
