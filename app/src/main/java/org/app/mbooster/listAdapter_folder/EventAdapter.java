package org.app.mbooster.listAdapter_folder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import org.app.mbooster.R;
import org.app.mbooster.model_folder.Event;
import org.app.mbooster.model_folder.SavePreferences;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by royfei on 07/11/2017.
 */

public class EventAdapter extends RecyclerView.Adapter {
    public static interface OnRecyclerViewListener {
        void onItemClick(int position);

        boolean onItemLongClick(int position);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    private static final String TAG = NotificationAdapter.class.getSimpleName();
    private List<Event> list;
    private Context context;
    private int[] timer_number = {R.drawable.timer_0, R.drawable.timer_1, R.drawable.timer_2, R.drawable.timer_3, R.drawable.timer_4, R.drawable.timer_5,
            R.drawable.timer_6, R.drawable.timer_7, R.drawable.timer_8, R.drawable.timer_9};

    public EventAdapter(Context context, List<Event> list) {
        this.list = list;
        this.context = context;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        //Logger.d(TAG, "onCreateViewHolder, i: " + i);
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_item_event, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {

        final ViewHolder holder = (ViewHolder) viewHolder;
        holder.position = i;
        final Event item = list.get(i);

        final ImageAware imageAware = new ImageViewAware(holder.imageView);
        ImageLoader.getInstance().displayImage(item.event_img, imageAware);

        holder.shopnow.setVisibility(View.GONE);
        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date));

        String dateStart = dateFormat.format(date);
        String dateStop = item.event_end_datetime;
        String event_start = item.event_start_datetime;

        holder.shopnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        Date d1 = null; // current datetime
        Date d2 = null; // end datetime , use to countdown event ends
        Date d3 = null; // start datetime , use to start countdown timer.
        long init_milli = 0;

        try {
            d1 = dateFormat.parse(dateStart);
            d2 = dateFormat.parse(dateStop);
            d3 = dateFormat.parse(event_start);


            init_milli = d3.getTime() - d1.getTime();
            if (init_milli < 0) {
                init_milli = 0;
            }

            //in milliseconds
//            init_milli2 = d2.getTime() - d1.getTime();
//            if (init_milli > 0) {
//                init_milli = 0;
//            }


//            Log.d("start milli", (d3.getTime() - d1.getTime()) + "");

        } catch (Exception e) {
            e.printStackTrace();
        }

        //init_milli = 5000;
        if (init_milli != 0) {
            //init timer
            long diffSeconds = init_milli / 1000 % 60;
            long diffMinutes = init_milli / (60 * 1000) % 60;
            long diffHours = init_milli / (60 * 60 * 1000) % 24;
            long diffDays = init_milli / (24 * 60 * 60 * 1000);

            holder.sec_digits.setImageDrawable(context.getResources().getDrawable(timer_number[(int) (diffSeconds % 10)]));
            holder.sec_tens.setImageDrawable(context.getResources().getDrawable(timer_number[(int) (diffSeconds / 10)]));
            holder.min_digits.setImageDrawable(context.getResources().getDrawable(timer_number[(int) (diffMinutes % 10)]));
            holder.min_tens.setImageDrawable(context.getResources().getDrawable(timer_number[(int) (diffMinutes / 10)]));
            holder.hrs_digits.setImageDrawable(context.getResources().getDrawable(timer_number[(int) (diffHours % 10)]));
            holder.hrs_tens.setImageDrawable(context.getResources().getDrawable(timer_number[(int) (diffHours / 10)]));
            holder.day_digits.setImageDrawable(context.getResources().getDrawable(timer_number[(int) (diffDays % 10)]));
            holder.day_tens.setImageDrawable(context.getResources().getDrawable(timer_number[(int) (diffDays / 10)]));
            //end init timer

            holder.tvstart_end.setText(context.getResources().getString(R.string.start_in));

            //counter
            final Date finalD = d2;
            final Date finalD1 = d1;
            final CountDownTimer countDownTimer = new CountDownTimer(init_milli, 1000) {
                @Override
                public void onTick(long mf) {
                    long diffSeconds = mf / 1000 % 60;
                    long diffMinutes = mf / (60 * 1000) % 60;
                    long diffHours = mf / (60 * 60 * 1000) % 24;
                    long diffDays = mf / (24 * 60 * 60 * 1000);

                    if (((int) mf / 1000) % 10 == 9) {
                        holder.sec_digits.setImageDrawable(context.getResources().getDrawable(timer_number[(int) (diffSeconds % 10)]));
                        holder.sec_tens.setImageDrawable(context.getResources().getDrawable(timer_number[(int) (diffSeconds / 10)]));
                        holder.min_digits.setImageDrawable(context.getResources().getDrawable(timer_number[(int) (diffMinutes % 10)]));
                        holder.min_tens.setImageDrawable(context.getResources().getDrawable(timer_number[(int) (diffMinutes / 10)]));
                        holder.hrs_digits.setImageDrawable(context.getResources().getDrawable(timer_number[(int) (diffHours % 10)]));
                        holder.hrs_tens.setImageDrawable(context.getResources().getDrawable(timer_number[(int) (diffHours / 10)]));
                        holder.day_digits.setImageDrawable(context.getResources().getDrawable(timer_number[(int) (diffDays % 10)]));
                        holder.day_tens.setImageDrawable(context.getResources().getDrawable(timer_number[(int) (diffDays / 10)]));

                    } else {
                        holder.sec_digits.setImageDrawable(context.getResources().getDrawable(timer_number[((int) mf / 1000) % 10]));
                    }
                }

                @Override
                public void onFinish() {
//                endGame(); //End the game or do whatever you want.
                    holder.timer_countdown.setVisibility(View.GONE);
                    holder.shopnow.setVisibility(View.GONE);
                    ImageLoader.getInstance().displayImage(item.event_start_button, imageAware);

//                   notifyDataSetChanged();
                    //countDownTimer2.start();
                    //allow to click
                    holder.rootView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent((Activity) context, org.app.mbooster.shopping_mall.Event.Event.class);
                            intent.putExtra("event_id", item.event_id);
                            context.startActivity(intent);
                        }
                    });

                    long init_milli2 = finalD.getTime() - finalD1.getTime();
                    endcounter(init_milli2,i,holder);
//                    final CountDownTimer countDownTimer2 = new CountDownTimer(init_milli2, 1000) {
//                        @Override
//                        public void onTick(long millisUntilFinished) {
//
//                        }
//
//                        @Override
//                        public void onFinish() {
//                            holder.shopnow.setVisibility(View.INVISIBLE);
//                            list.remove(i);
//                            EventAdapter.this.notifyDataSetChanged();
//                        }
//                    };
//                    countDownTimer2.start();
                }
            };

            //counter start
            countDownTimer.start();


        } else if (init_milli == 0) {
            holder.timer_countdown.setVisibility(View.GONE);
            holder.shopnow.setVisibility(View.GONE);
            ImageLoader.getInstance().displayImage(item.event_start_button, imageAware);

            //allow to click
            holder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent((Activity) context, org.app.mbooster.shopping_mall.Event.Event.class);
                    intent.putExtra("event_id", item.event_id);
                    context.startActivity(intent);
                }
            });


            long init_milli2 = d2.getTime() - d1.getTime();
            endcounter(init_milli2,i,holder);
        }

    }

    public void endcounter(long init_milli2, final int i,final ViewHolder holder){
        final CountDownTimer countDownTimer2 = new CountDownTimer(init_milli2, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                holder.shopnow.setVisibility(View.GONE);


                list.remove(i);
                EventAdapter.this.notifyDataSetChanged();
            }
        };

        countDownTimer2.start();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public View rootView;
        public ImageView shopnow, imageView, day_tens, day_digits, hrs_tens, hrs_digits, min_tens, min_digits, sec_tens, sec_digits;
        public TextView tvstart_end;
        public int position;
        public LinearLayout timer_countdown;

        public ViewHolder(View itemView) {
            super(itemView);

            timer_countdown = (LinearLayout) itemView.findViewById(R.id.timer_countdown);
            imageView = (ImageView) itemView.findViewById(R.id.image_view);
            shopnow = (ImageView) itemView.findViewById(R.id.shopnow);
            day_tens = (ImageView) itemView.findViewById(R.id.day_tens);
            day_digits = (ImageView) itemView.findViewById(R.id.day_digits);
            hrs_tens = (ImageView) itemView.findViewById(R.id.hrs_tens);
            hrs_digits = (ImageView) itemView.findViewById(R.id.hrs_digits);
            min_tens = (ImageView) itemView.findViewById(R.id.min_tens);
            min_digits = (ImageView) itemView.findViewById(R.id.min_digits);
            sec_tens = (ImageView) itemView.findViewById(R.id.sec_tens);
            sec_digits = (ImageView) itemView.findViewById(R.id.sec_digits);
            tvstart_end = (TextView) itemView.findViewById(R.id.tvstart_end);
            //counter part

            rootView = itemView.findViewById(R.id.rootview);
            rootView.setOnClickListener(this);
            rootView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (null != onRecyclerViewListener) {
                onRecyclerViewListener.onItemClick(position);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (null != onRecyclerViewListener) {
                return onRecyclerViewListener.onItemLongClick(position);
            }
            return false;
        }
    }
}