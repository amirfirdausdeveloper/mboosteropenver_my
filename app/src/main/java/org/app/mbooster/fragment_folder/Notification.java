package org.app.mbooster.fragment_folder;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.app.mbooster.Helper.Helper;
import org.app.mbooster.R;
import org.app.mbooster.listAdapter_folder.NotificationAdapter;
import org.app.mbooster.model_folder.NotificationTitleItem;
import org.app.mbooster.model_folder.SavePreferences;
import org.app.mbooster.model_folder.urlLink;
import org.app.mbooster.shopping_mall.Notification.NotificationList;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Notification extends Fragment {

    Context context;
    private RecyclerView recyclerview;
    private SwipeRefreshLayout swiperefresh;
    private NotificationAdapter adapter;
    private List<NotificationTitleItem> items = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    private List<String> notifications = new ArrayList<>();
    int[] icons = {
            R.drawable.home,
            R.drawable.overview,

    };

    public Notification() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        titles.clear();
        titles.add("Promotion");
        titles.add("Events");
        Helper.CheckMaintenance(getActivity());

        swiperefresh = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
        adapter = new NotificationAdapter(getActivity(),items);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(adapter);

        adapter.setOnRecyclerViewListener(new NotificationAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
//                if(position != 2) {
//                    Intent intent = new Intent(getActivity(), NotificationList.class);
//
//                    intent.putExtra("title", items.get(position).title);
//                    intent.putExtra("notification_id", items.get(position).type);
//
//                    startActivity(intent);
//                }else{
//                    String url = "https://mbooster.my/seasonEvent/reservation.php";
//                    Intent i = new Intent(Intent.ACTION_VIEW);
//                    i.setData(Uri.parse(url));
//                    startActivity(i);
//                }

                if(items.get(position).link_url.equals("")){
                    Intent intent = new Intent(getActivity(), NotificationList.class);

                    intent.putExtra("title", items.get(position).title);
                    intent.putExtra("notification_id", items.get(position).type);
                    startActivity(intent);
                }else{
                    String url = items.get(position).link_url;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            }

            @Override
            public boolean onItemLongClick(int position) {
                return false;
            }
        });

        //NotificationTitleList();

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                NotificationTitleList();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        NotificationTitleList();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void NotificationTitleList() {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                items.clear();
                if(!swiperefresh.isRefreshing()){
                    swiperefresh.setRefreshing(true);
                }
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);
                swiperefresh.setRefreshing(false);
                try {
                    items.clear();
                    JSONArray array = jsonObject.getJSONArray("array");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject json = array.getJSONObject(i);
                        items.add(new NotificationTitleItem(json.getString("type"), json.getString("title"), json.getString("img_url"), json.getString("count"), json.getString("link_url")));
                    }

                    adapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... strings) {
                return new urlLink().happeningtitlelist(SavePreferences.getUserID(context), SavePreferences.getApplanguage(context));
            }
        }
        new getinfo().execute();
    }

}
