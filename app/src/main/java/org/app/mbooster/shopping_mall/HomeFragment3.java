package org.app.mbooster.shopping_mall;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import org.app.mbooster.Font.TypefaceUtil;
import org.app.mbooster.Helper.LogHelper;
import org.app.mbooster.R;
import org.app.mbooster.activity_folder.LiveStream.LiveStream;
import org.app.mbooster.listAdapter_folder.EventAdapter;
import org.app.mbooster.listAdapter_folder.GridRAdapter;
import org.app.mbooster.model_folder.Category;
import org.app.mbooster.model_folder.Event;
import org.app.mbooster.model_folder.SavePreferences;
import org.app.mbooster.model_folder.urlLink;
import org.app.mbooster.shopping_mall.Kwave.KwaveShoppingBag;
import org.app.mbooster.shopping_mall.Product.Product;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class HomeFragment3 extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private String mTitle;

    private RelativeLayout rootview;

    private LinearLayout linearLayout;

    private SearchView searchView;
    private View view3;

    private AssetManager assetManager;
    private SliderLayout mDemoSlider;
    private FloatingActionButton fab;

//    private SwipeRefreshLayout swipeRefresh;

    private ProgressBar progressBar;

    private ArrayList<Category_slider_item> sitems = new ArrayList<>();

    private RecyclerView eventRecyclerView, gridRecyclerView;

    private List<Category_item> category_items = new ArrayList<>();
    private GridRAdapter gridRAdapter;

    private List<Event> EventItems = new ArrayList<>();
    private EventAdapter event_adapter;

    private FloatingActionButton fab2;

    public static HomeFragment3 getInstance(String title) {

        HomeFragment3 sf = new HomeFragment3();
        sf.mTitle = title;
        return sf;

    }

    //ADD BY AMIR 7/5/2019

    NestedScrollView nestedScrollView;
    FloatingActionButton fab_redeem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assetManager = getActivity().getApplicationContext().getAssets();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home_fragment3, container, false);
        final Typeface tvFont = Typeface.createFromAsset(assetManager, "fonts/gotham_book_bold.ttf");

        progressBar = (ProgressBar) v.findViewById(R.id.progressbar);
        rootview = (RelativeLayout) v.findViewById(R.id.rootview);
        linearLayout = (LinearLayout) v.findViewById(R.id.linearLayout);
        mDemoSlider = (SliderLayout) v.findViewById(R.id.slider);

        fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        searchView = (SearchView) v.findViewById(R.id.searchView1);
        int searchPlateId = searchView.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        View searchPlate = searchView.findViewById(searchPlateId);
        ViewGroup.LayoutParams layoutParams = searchPlate.getLayoutParams();
        layoutParams.height -= 20;
        searchPlate.setBackgroundColor(Color.TRANSPARENT);
        searchPlate.setLayoutParams(layoutParams);

        searchView.setEnabled(false);
        searchView.clearFocus();

        searchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    searchView.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                }
            }
        });
        view3 = (View) v.findViewById(R.id.view3);
        view3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), ProductSearch.class);
                startActivity(i);
            }
        });

        enableSearchView(searchView, false);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        ViewGroup.LayoutParams p = mDemoSlider.getLayoutParams();
        p.height = (int) (width / 1.7778);
//        Log.i("double", String.valueOf(width /1.7778));
//        Log.i("int", String.valueOf((int)(width /1.7778)));

        mDemoSlider.setLayoutParams(p);


        eventRecyclerView = v.findViewById(R.id.eventRecyclerView);
        gridRecyclerView = v.findViewById(R.id.gridRecyclerView);

        eventRecyclerView.setNestedScrollingEnabled(false);
        gridRecyclerView.setNestedScrollingEnabled(false);

        eventRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        eventRecyclerView.setHasFixedSize(true);

        event_adapter = new EventAdapter(getActivity(), EventItems);
        eventRecyclerView.setAdapter(event_adapter);

        gridRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        gridRecyclerView.setHasFixedSize(true);

        gridRAdapter = new GridRAdapter(getActivity(), category_items);
        gridRecyclerView.setAdapter(gridRAdapter);
//        swipeRefresh = v.findViewById(R.id.swiperefresh);
//        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                category();
//            }
//        });

        category();

        fab2 = v.findViewById(R.id.fab2);
        fab2.setOnClickListener(view -> startActivity(new Intent(getActivity(), LiveStream.class)));
        fab2.setVisibility(View.GONE);

        //ADD BY AMIR 7/5/2019

        nestedScrollView = v.findViewById(R.id.nestedScrollView);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        //total disable search view after anything system prompt or focus changed
        if (searchView != null) {
            searchView.clearFocus();
            rootview.requestFocus();
        }
    }

    private void doneRefresh(){
//        if(swipeRefresh!=null){
//            swipeRefresh.setRefreshing(false);
//        }
    }

    private void enableSearchView(final View view, boolean enabled) {
        view.setEnabled(enabled);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                final View child = viewGroup.getChildAt(i);
                enableSearchView(child, enabled);
            }
        }
    }


    private void category() {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            @Override
            protected void onPreExecute() {
                LogHelper.debug("[Category] 8");
                super.onPreExecute();
                category_items.clear();
                sitems.clear();
                EventItems.clear();
                mDemoSlider.removeAllSliders();
                linearLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                doneRefresh();
                LogHelper.debug("[Category] 9");
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                LogHelper.debug("[Category] 3");
                super.onPostExecute(jsonObject);
                Log.i("categoryimg", jsonObject.toString());
                linearLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                try {

                    boolean contain_kw_event = false;
                    Date d1 = null; // current datetime
                    final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date();
                    String dateStart = dateFormat.format(date);
                    d1 = dateFormat.parse(dateStart);
                    JSONArray jsonArray = jsonObject.getJSONArray("event");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json = jsonArray.getJSONObject(i);
                        String dateStop = json.getString("event_end_datetime");
                        Date d2 = dateFormat.parse(dateStop);
                        if (d2.getTime() - d1.getTime() > 0) {
                            EventItems.add(new Event(json.getString("event_id"), json.getString("event_name"), json.getString("event_start_datetime"), json.getString("event_end_datetime"), json.getString("event_desc"), json.getString("event_img"), json.getString("event_start_button")));
                            if (json.getString("event_id").equals("5")) {
                                contain_kw_event = true;
                            }
                        }
                    }
                    event_adapter.notifyDataSetChanged();
                    JSONArray array = jsonObject.getJSONArray("array");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject json = array.getJSONObject(i);
                        Category_item item = new Category_item();
                        item.setCategory_id(json.getString("category_id"));
                        item.setCategory_name(json.getString("category_name"));
                        item.setCategory_image(json.getString("category_img"));
                        category_items.add(item);
                    }

                    LogHelper.debug("[Category] 4");
                    gridRAdapter.notifyDataSetChanged();
                    //slider image

                    JSONArray jsonaa = jsonObject.getJSONArray("image");
                    sitems.clear();

                    // if(sitems.size() < jsonaa.length()) {
                    HashMap<String, String> url_maps = new HashMap<String, String>();
                    for (int i = 0; i < jsonaa.length(); i++) {
                        JSONObject jj = jsonaa.getJSONObject(i);
                        sitems.add(new Category_slider_item(jj.getString("slider_img"), jj.getString("category_id"), jj.getString("product_id"), jj.getString("subcategory_id")));
                        url_maps.put(String.valueOf(i), jj.getString("slider_img"));
                    }
                    LogHelper.debug("[Category] 5");

                    for (int i = 0; i < sitems.size(); i++) {

                        CustomSliderLayout textSliderView = new CustomSliderLayout(getActivity().getApplicationContext());
                        // initialize a SliderLayout
                        Log.i("path slider", sitems.get(i).slider_img);
                        textSliderView
                                //.description(name)
                                .image(sitems.get(i).slider_img)
                                .setScaleType(BaseSliderView.ScaleType.Fit)
                                .setOnSliderClickListener(HomeFragment3.this);

                        //add your extra information
                        textSliderView.bundle(new Bundle());
                        textSliderView.getBundle()
                                .putString("extra", String.valueOf(i));
                        mDemoSlider.addSlider(textSliderView);
                    }

                    mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
                    mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                    mDemoSlider.setCustomAnimation(new DescriptionAnimation2());
                    mDemoSlider.setDuration(4000);
                    mDemoSlider.addOnPageChangeListener(HomeFragment3.this);
                    mDemoSlider.setCurrentPosition(0);
                    //  }

                    LogHelper.debug("[Category] 6");
                    Log.i("usertype", SavePreferences.getUserType(getActivity()));
                    if (jsonObject.getString("mmspot_email_valid").equals("0") && SavePreferences.getUserType(getActivity()).equals("0")) {
                        String arg = getString(R.string.please_update_your_email_via_mmspot);
                        String arg2 = getString(R.string.update_email);
                        String arg3 = getString(R.string.yes);
                        String arg4 = getString(R.string.cancel);

                        final Dialog dialog = new Dialog(getActivity(), R.style.AlertDialogTheme);
                        TypefaceUtil.overrideFont(getActivity().getApplicationContext(), "SERIF", "fonts/gotham_book.otf");
                        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        dialog.setContentView(R.layout.fragment_dialog_universal);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();

                        TextView content = (TextView) dialog.findViewById(R.id.content);
                        TextView title = (TextView) dialog.findViewById(R.id.title);
                        title.setText(arg2);
                        Button btnConfirm = (Button) dialog.findViewById(R.id.btnConfirm);
                        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);

                        btnConfirm.setText(arg3);
                        btnCancel.setText(arg4);

                        content.setText(arg);
                        btnConfirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                final String appPackageName = "asia.mcalls.mspot"; // getPackageName() from Context or Activity object
                                Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage(appPackageName);

                                if (intent == null) {
                                    try {
                                        //Check whether Google Play store is installed or not:
                                        getActivity().getPackageManager().getPackageInfo(appPackageName, 0);
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                    } catch (final Exception e) {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                    }
                                } else {
                                    startActivity(intent);
                                }

                            }
                        });

                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                        LogHelper.debug("[Category] 7");
                    }

//                    if (jsonObject.getString("APP_INDEX_DIALOG_STATUS").equals("1")) {
//                        Log.i("APP_INDEX_DIALOG_STATUS", "show dialog");
//                        LayoutInflater factory = LayoutInflater.from(getActivity());
//                        final View indexDialogView = factory.inflate(R.layout.dialog_image_btn, null);
//                        final AlertDialog index_dialog = new AlertDialog.Builder(getActivity()).create();
//                        index_dialog.setView(indexDialogView);
//
//                        index_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//                        index_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//
//                        indexDialogView.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                //your business logic
//                                index_dialog.dismiss();
//                            }
//                        });
//
//                        ImageView dialog_image = (ImageView) indexDialogView.findViewById(R.id.dialog_image);
//
//                        ImageLoader.getInstance().displayImage(jsonObject.getString("APP_INDEX_DIALOG_IMG"), new ImageViewAware(dialog_image, false));
//
//                        index_dialog.show();
//                    }


                    if (contain_kw_event) {
                        fab.setVisibility(View.VISIBLE);
                        fab.setOnClickListener(view -> startActivity(new Intent(getActivity(), KwaveShoppingBag.class)));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            @Override
            protected JSONObject doInBackground(String... params) {
                LogHelper.debug("[Category] 1");
                return new urlLink().getcategory(SavePreferences.getUserID(getContext()), SavePreferences.getApplanguage(getContext()));
            }
        }
        new getinfo().execute();
    }


    @Override
    public void onSliderClick(BaseSliderView slider) {


//        Toast.makeText(getActivity().getApplicationContext(), slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
        if (sitems.get(Integer.parseInt(slider.getBundle().get("extra") + "")).category_id.equals("0")) {
            if (sitems.get(Integer.parseInt(slider.getBundle().get("extra") + "")).product_id.equals("0")) {
                //do nothing
            } else {
                //start product details activity
                Intent i = new Intent(getContext(), Product.class);
                i.putExtra("productid", sitems.get(Integer.parseInt(slider.getBundle().get("extra") + "")).product_id);
                startActivity(i);
            }
        } else {
            Intent i = new Intent(getContext(), Subcategory.class);
            i.putExtra("category_id", sitems.get(Integer.parseInt(slider.getBundle().get("extra") + "")).getCategory_id());
            i.putExtra("subcategory_id", sitems.get(Integer.parseInt(slider.getBundle().get("extra") + "")).getSubcategory_id());
//            Log.i("category_id", sitems.get(Integer.parseInt(slider.getBundle().get("extra") + "")).getCategory_id());
//            Log.i("subcategory_id", sitems.get(Integer.parseInt(slider.getBundle().get("extra") + "")).getSubcategory_id());
            for (int j = 0; j < category_items.size(); j++) {
                if (sitems.get(Integer.parseInt(slider.getBundle().get("extra") + "")).getCategory_id().equals(category_items.get(j).getCategory_id())) {
                    i.putExtra("category_name", category_items.get(j).getCategory_name());
//                    Log.i("category_name",  category_items.get(j).getCategory_name());
                    break;
                }
            }
            startActivity(i);
        }
        //i.putExtra("category_name",items.get(position).getCategory_name());

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        //Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    public class Category_item {

        String category_id;
        String category_name;
        String category_image;
        String category_rank;
        String category_parent;

        public String getCategory_id() {
            return category_id;
        }

        public void setCategory_id(String category_id) {
            this.category_id = category_id;
        }

        public String getCategory_image() {
            return category_image;
        }

        public void setCategory_image(String category_image) {
            this.category_image = category_image;
        }

        public String getCategory_name() {
            return category_name;
        }

        public void setCategory_name(String category_name) {
            this.category_name = category_name;
        }

        public String getCategory_parent() {
            return category_parent;
        }

        public void setCategory_parent(String category_parent) {
            this.category_parent = category_parent;
        }

        public String getCategory_rank() {
            return category_rank;
        }

        public void setCategory_rank(String category_rank) {
            this.category_rank = category_rank;
        }

    }

    class Category_slider_item {

        String slider_img;
        String category_id;
        String product_id;
        String subcategory_id;

        public Category_slider_item(String slider_img, String category_id, String product_id, String subcategory_id) {
            this.slider_img = slider_img;
            this.category_id = category_id;
            this.product_id = product_id;
            this.subcategory_id = subcategory_id;
        }

        public String getCategory_id() {
            return category_id;
        }

        public String getProduct_id() {
            return product_id;
        }

        public String getSlider_img() {
            return slider_img;
        }

        public String getSubcategory_id() {
            return subcategory_id;
        }
    }
}
