package org.app.mbooster.listAdapter_folder;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.app.mbooster.R;
import org.app.mbooster.model_folder.Paymentmethod;

import java.util.ArrayList;

public class PMExpandableLVAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<Paymentmethod> paymentmethods;
    private LayoutInflater layoutInflater;

    public PMExpandableLVAdapter(Context context, ArrayList<Paymentmethod> paymentmethods){
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.paymentmethods = paymentmethods;
    }

    @Override
    public int getGroupCount() {
        return paymentmethods.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return paymentmethods.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return null;
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        view = layoutInflater.inflate(R.layout.pmgroupview,null);

        ImageView logo_view = view.findViewById(R.id.logo_view);
        TextView pm_text = view.findViewById(R.id.pm_text);



        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
