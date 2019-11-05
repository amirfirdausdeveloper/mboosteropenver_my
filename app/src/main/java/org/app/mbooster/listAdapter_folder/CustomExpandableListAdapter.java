package org.app.mbooster.listAdapter_folder;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.app.mbooster.R;
import org.app.mbooster.activity_folder.MainActivity;
import org.app.mbooster.model_folder.SavePreferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by User on 25/8/2016.
 */
public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> expandableListTitle;
    private HashMap<String, List<String>> expandableListDetail;

    public CustomExpandableListAdapter(Context context, List<String> expandableListTitle, HashMap<String,
            List<String>> expandableListDetail, ArrayList<MainActivity.category_item> items) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.expandableListTitle.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(groupPosition);

        LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(context.LAYOUT_INFLATER_SERVICE);



        convertView = layoutInflater.inflate(R.layout.list_group, null);
        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.list_group);  //title name
        listTitleTextView.setText(listTitle.replace("\n", ""));
        listTitleTextView.setTextColor(Color.parseColor("#CC000000"));

        ImageView imageView = (ImageView) convertView.findViewById(R.id.image);

        if(listTitle.equals(context.getString(R.string.account))){
                imageView.setImageResource(R.mipmap.account_icon);
                imageView.setPadding(15,15,15,15);
        }
//        if(listTitle.equals(context.getString(R.string.redemption))){
//            imageView.setImageResource(R.drawable.icon_redeem_menu);
//            imageView.setPadding(15,15,15,15);
//        }
        if(listTitle.equals(context.getString(R.string.drawer_order_n_transaction))){
            imageView.setImageResource(R.mipmap.icon_order_history);
            imageView.setPadding(15,15,15,15);
        }
        if(listTitle.equals(context.getString(R.string.request_an_item))){
            imageView.setImageResource(R.mipmap.request_icon);
            imageView.setPadding(15,15,15,15);
        }
        if(listTitle.equals(context.getString(R.string.tabs_rewards))){
            imageView.setImageResource(R.mipmap.rewards_icon);
            imageView.setPadding(15,15,15,15);
        }
        if(listTitle.equals(context.getString(R.string.drawer_convert_to_ev))){
            imageView.setImageResource(R.mipmap.icon_convert);
            imageView.setPadding(15,15,15,15);
        }
        if(listTitle.equals(context.getString(R.string.help_and_contact_us))){
            imageView.setImageResource(R.mipmap.help_center_icon);
            imageView.setPadding(15,15,15,15);
        }
        if(listTitle.equals(context.getString(R.string.change_english))){
            imageView.setImageResource(R.drawable.change_language);
            imageView.setPadding(15,15,15,15);
        }
        if(listTitle.equals(context.getString(R.string.drawer_tnc))){
            imageView.setImageResource(R.mipmap.icon_tnc);
            imageView.setPadding(15,15,15,15);
        }

//        Drawable tintDrawable = DrawableCompat.wrap(originalDrawable).mutate();
//        DrawableCompat.setTint(tintDrawable, Color.parseColor("#000000"));
//        imageView.setImageDrawable(tintDrawable);


        return convertView;
    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final String expandedListText = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item, null);
        }
        TextView expandedListTextView = (TextView) convertView.findViewById(R.id.list_item);
        expandedListTextView.setText(expandedListText.replace("\n", "").replace(" ", ""));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
