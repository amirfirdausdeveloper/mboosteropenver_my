package org.app.mbooster.listAdapter_folder;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.app.mbooster.R;
import org.app.mbooster.model_folder.NotificationItem;
import org.app.mbooster.model_folder.SavePreferences;
import org.app.mbooster.model_folder.urlLink;
import org.json.JSONObject;

import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyTypefaceSpan;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

/**
 * Created by royfei on 24/10/2017.
 */

public class NotificationListAdapter extends RecyclerView.Adapter {
    public static interface OnRecyclerViewListener {
        void onItemClick(int position);

        boolean onItemLongClick(int position);
    }

    private NotificationListAdapter.OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    private static final String TAG = NotificationListAdapter.class.getSimpleName();
    private List<NotificationItem> list;
    private Context context;

    public NotificationListAdapter(Context context, List<NotificationItem> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        //Logger.d(TAG, "onCreateViewHolder, i: " + i);
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_item_notificationlist, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
//        Logger.d(TAG, "onBindViewHolder, i: " + i + ", viewHolder: " + viewHolder);
        ViewHolder holder = (ViewHolder) viewHolder;
        holder.position = i;
        NotificationItem item = list.get(i);


        // Create the Typeface you want to apply to certain text
        CalligraphyTypefaceSpan typefaceSpan;
        if (item.read.equals("1")) {
            typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(context.getAssets(), "fonts/gotham_book.ttf"));
        } else {
            typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(context.getAssets(), "fonts/gotham_book_bold.ttf"));
        }
        SpannableStringBuilder title_sBuilder = new SpannableStringBuilder();
        title_sBuilder.append(item.title); // Bold this
        title_sBuilder.setSpan(typefaceSpan, 0, item.title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.title.setText(title_sBuilder, TextView.BufferType.SPANNABLE);

        SpannableStringBuilder subtitle_sBuilder = new SpannableStringBuilder();
        subtitle_sBuilder.append(item.subtitle); // Bold this
        subtitle_sBuilder.setSpan(typefaceSpan, 0, item.subtitle.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.subtitle.setText(subtitle_sBuilder, TextView.BufferType.SPANNABLE);

        SpannableStringBuilder date_sBuilder = new SpannableStringBuilder();
        date_sBuilder.append(item.date); // Bold this
        date_sBuilder.setSpan(typefaceSpan, 0, item.date.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.date.setText(date_sBuilder, TextView.BufferType.SPANNABLE);


//        holder.title.setText(item.title);
//        holder.subtitle.setText(item.subtitle);
//        holder.date.setText(item.date);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public View rootView;
        public TextView title, subtitle, date;

        public int position;

        public ViewHolder(View itemView) {
            super(itemView);


            date = (TextView) itemView.findViewById(R.id.date);
            title = (TextView) itemView.findViewById(R.id.titleTextView);
            subtitle = (TextView) itemView.findViewById(R.id.subtitleTextView);

            rootView = itemView.findViewById(R.id.rootview);
            rootView.setOnClickListener(this);
            rootView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (null != onRecyclerViewListener) {
                onRecyclerViewListener.onItemClick(position);
            }
            NotificationItem item = list.get(position);
            item.setRead("1");
//            HappeningRead(item.id);
            list.set(position, item);
            notifyDataSetChanged();
        }

        @Override
        public boolean onLongClick(View v) {
            if (null != onRecyclerViewListener) {
                return onRecyclerViewListener.onItemLongClick(position);
            }
            return false;
        }
    }

//    private void HappeningRead(final String happening_id){
//        class getinfo extends AsyncTask<String,String,JSONObject>{
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//            }
//
//            @Override
//            protected void onPostExecute(JSONObject jsonObject) {
//                super.onPostExecute(jsonObject);
//            }
//
//            @Override
//            protected JSONObject doInBackground(String... strings) {
//                return new urlLink().happening_read(SavePreferences.getUserID(context),happening_id);
//            }
//        }
//        new getinfo().execute();
//    }
}