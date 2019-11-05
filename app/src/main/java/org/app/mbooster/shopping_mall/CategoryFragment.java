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
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SearchView;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import org.app.mbooster.Font.TypefaceUtil;
import org.app.mbooster.R;
import org.app.mbooster.activity_folder.MainActivity;
import org.app.mbooster.model_folder.SavePreferences;
import org.app.mbooster.model_folder.urlLink;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class CategoryFragment extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    TextView category_name, textView3;
    GridView gridview;
    String userId;
    categoryAdapter adapter;
    ArrayList<category_item> items = new ArrayList<>();
    ArrayList<slider_item> sitems = new ArrayList<>();
    private SliderLayout mDemoSlider;
    SearchView searchView;
    AssetManager assetManager;
    View view3;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        assetManager = getActivity().getApplicationContext().getAssets();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View myInflatedView = inflater.inflate(R.layout.fragment_category, container, false);
        final Typeface tvFont = Typeface.createFromAsset(assetManager, "fonts/gotham_book_bold.ttf");

        searchView = (SearchView) myInflatedView.findViewById(R.id.searchView1);
        int searchPlateId = searchView.getContext().getResources().getIdentifier("android:id/search_plate", null, null);

        View searchPlate = searchView.findViewById(searchPlateId);
        ViewGroup.LayoutParams layoutParams = searchPlate.getLayoutParams();
        layoutParams.height -= 20;
        searchPlate.setBackgroundColor(Color.TRANSPARENT);
        searchPlate.setLayoutParams(layoutParams);
        textView3 = (TextView) myInflatedView.findViewById(R.id.textView3);
        view3 = (View) myInflatedView.findViewById(R.id.view3);

        gridview = (GridView) myInflatedView.findViewById(R.id.gridview);
        adapter = new categoryAdapter(getActivity().getApplicationContext(), items);
        gridview.setAdapter(adapter);
        userId = SavePreferences.getUserID(getActivity().getApplicationContext());
        category_asyn(userId);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(getContext(), Subcategory.class);
                i.putExtra("category_id", items.get(position).getCategory_id());
                i.putExtra("category_name", items.get(position).getCategory_name());
                startActivity(i);
            }
        });
        searchView.setEnabled(false);
        searchView.clearFocus();
        view3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), ProductSearch.class);
                startActivity(i);
            }
        });

        mDemoSlider = (SliderLayout) myInflatedView.findViewById(R.id.slider);

//        if(SavePreferences.getApplanguage(getContext()).equals("CN")){
//            searchView.setQueryHint("搜索Mbooster");
//            textView3.setText("所有类别");
//        }
        textView3.setTypeface(tvFont);
        //get screen display

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


        return myInflatedView;
    }

    @Override
    public void onResume() {
        super.onResume();
        //searchView.clearFocus();
    }

    public void setGridViewHeightBasedOnChildren(GridView gridView, int columns) {
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int items = listAdapter.getCount();
        int rows = 0;

        View listItem = listAdapter.getView(0, null, gridView);
        listItem.measure(0, 0);
        totalHeight = listItem.getMeasuredHeight() + 20;

        float x = 1;
        if (items > columns) {
            x = items / columns;
            rows = (int) (x);
            totalHeight *= rows;
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        gridView.setLayoutParams(params);

    }


    public void category_asyn(final String userId) {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                items.clear();
                sitems.clear();
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);

                try {

                    String response = jsonObject.getString("success");
                    if (response.equals("1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("array");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject json = (JSONObject) jsonArray.get(i);
                            category_item item = new category_item();
                            item.setCategory_id(json.getString("category_id"));
                            item.setCategory_name(json.getString("category_name"));
                            item.setCategory_image(json.getString("category_img"));
                            item.setCategory_rank(json.getString("category_rank"));
                            item.setCategory_parent(json.getString("category_parent"));

                            if (items.size() == 8) {
                                break;
                            } else {
                                items.add(item);
                            }
                        }

                        //slider image
                        //ArrayList<String> sliderpath = new ArrayList<>();

                        JSONArray jsonaa = jsonObject.getJSONArray("image");
                        sitems.clear();

                        if (sitems.size() < jsonaa.length()) {
                            HashMap<String, String> url_maps = new HashMap<String, String>();
                            for (int i = 0; i < jsonaa.length(); i++) {
                                JSONObject jj = jsonaa.getJSONObject(i);
//                                String path = jj.getString("slider_img").replace(" ", "%20");
//                                path = path.replace(":", "%3A");
//                                path = path.replace("-", "%2D");

                                sitems.add(new slider_item(jj.getString("slider_img"), jj.getString("category_id"), jj.getString("product_id"), jj.getString("subcategory_id")));

                                // url_maps.put(String.valueOf(i), new urlLink().nonePath + "/products/" + path);
                                url_maps.put(String.valueOf(i), jj.getString("slider_img"));
                                Log.i("path", jj.getString("slider_img"));
                            }

                            for (String name : url_maps.keySet()) {

                                CustomSliderLayout textSliderView = new CustomSliderLayout(getActivity().getApplicationContext());
                                // initialize a SliderLayout
                                textSliderView
                                        //.description(name)
                                        .image(url_maps.get(name))
                                        .setScaleType(BaseSliderView.ScaleType.Fit)
                                        .setOnSliderClickListener(CategoryFragment.this);

                                //add your extra information
                                textSliderView.bundle(new Bundle());
                                textSliderView.getBundle()
                                        .putString("extra", name);

                                mDemoSlider.addSlider(textSliderView);
                            }
                            mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
                            mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                            mDemoSlider.setCustomAnimation(new DescriptionAnimation2());
                            mDemoSlider.setDuration(4000);
                            mDemoSlider.addOnPageChangeListener(CategoryFragment.this);
                            mDemoSlider.setCurrentPosition(0);
                        }


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                adapter.notifyDataSetChanged();
                setGridViewHeightBasedOnChildren(gridview, 2);
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                urlLink userFunctions = new urlLink();
                JSONObject json = userFunctions.getcategory(userId,SavePreferences.getApplanguage(getContext().getApplicationContext()));
//                Log.i("JSON GET category",json.toString());
                return json;
            }
        }
        getinfo ge = new getinfo();
        ge.execute();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        //Toast.makeText(getActivity().getApplicationContext(),slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
        if (sitems.get(Integer.parseInt(slider.getBundle().get("extra") + "")).category_id.equals("0")) {
            if (sitems.get(Integer.parseInt(slider.getBundle().get("extra") + "")).product_id.equals("0")) {
                //do nothing
            } else {
                //start product details activity
            }
        } else {
            Intent i = new Intent(getContext(), Subcategory.class);
            i.putExtra("category_id", sitems.get(Integer.parseInt(slider.getBundle().get("extra") + "")).getCategory_id());
            for (int j = 0; j < items.size(); j++) {
                if (sitems.get(Integer.parseInt(slider.getBundle().get("extra") + "")).getCategory_id().equals(items.get(j).getCategory_id())) {
                    i.putExtra("category_name", items.get(j).getCategory_name());
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
//        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    class categoryAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        Context context;
        ArrayList<category_item> items;

        public categoryAdapter(Context context, ArrayList<category_item> items) {
            mInflater = LayoutInflater.from(context);
            this.context = context;
            this.items = items;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = mInflater.inflate(R.layout.categoryitem, null);
            ViewHolder holder = new ViewHolder();
            category_item item = items.get(position);

            holder.imageview = (ImageView) convertView.findViewById(R.id.img);
            holder.textview = (TextView) convertView.findViewById(R.id.category_name);

            //setsize of image view

            int width = getResources().getDisplayMetrics().widthPixels / 2;
            android.view.ViewGroup.LayoutParams layoutParams = holder.imageview.getLayoutParams();
            layoutParams.width = width;
            layoutParams.height = (int) (((getResources().getDisplayMetrics().widthPixels - 20) / 2) / 1.33333);

            holder.imageview.setLayoutParams(layoutParams);

//            String path = item.getCategory_image().replace(" ", "%20");
//            path = path.replace(":", "%3A");
//            path = path.replace("-", "%2D");
            //Picasso.with(context).load(new urlLink().nonePath+"/products/" + path).centerCrop().resize(width,layoutParams.height).into(holder.imageview);
//            ImageLoader.getInstance().displayImage(new urlLink().nonePath+"vendors/"+path,new ImageViewAware(holder.imageview,false));
            ImageLoader.getInstance().displayImage(item.getCategory_image(), new ImageViewAware(holder.imageview, false));

//            Log.i("path",new urlLink().nonePath+"/products/" +path);
            holder.textview.setText(item.getCategory_name());
            holder.textview.setTextColor(Color.WHITE);


            final Typeface tvFont = Typeface.createFromAsset(assetManager, "fonts/gotham_book_bold.ttf");
            holder.textview.setTypeface(tvFont);
            return convertView;
        }

        class ViewHolder {
            ImageView imageview;
            TextView textview;
        }
    }

    class category_item {

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

    class slider_item {
        String slider_img;
        String category_id;
        String product_id;
        String subcategory_id;

        public slider_item(String slider_img, String category_id, String product_id, String subcategory_id) {
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
