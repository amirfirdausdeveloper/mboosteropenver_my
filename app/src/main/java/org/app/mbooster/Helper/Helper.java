package org.app.mbooster.Helper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.app.mbooster.BuildConfig;
import org.app.mbooster.Font.TypefaceUtil;
import org.app.mbooster.Holder.ConstantHolder;
import org.app.mbooster.R;
import org.app.mbooster.activity_folder.SplashActivity;
import org.app.mbooster.activity_folder.signInActivity;
import org.app.mbooster.model_folder.SavePreferences;
import org.app.mbooster.model_folder.urlLink;
import org.app.mbooster.shopping_mall.Shopping_bag;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.Locale;

/**
 * Created by royfei on 18/04/2017.
 */

public class Helper {

    public static void setAppLocale(Context context, String languageCode, String countryCode){
        Resources resources = context.getResources();
        Locale locale = new Locale(languageCode, countryCode);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    public static void showErrorMessage(Context c, String title, String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setMessage(msg);
        builder.setTitle(title);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }


    public static String TWMoney(String str){
        return String.format("%,d",(int)Double.parseDouble(str));
    }

    public static void CloseKeyboard(Activity activity){
        // Check if no view has focus:
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void setupImageCache(Context context) {

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(false)
                .imageScaleType(ImageScaleType.EXACTLY)
                .cacheOnDisc(true)
//                .delayBeforeLoading(1000)
//                .showImageForEmptyUri(fallbackImage)
                //.showImageOnFail(R.drawable.placeholder)
               // .showImageOnLoading(R.d)
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(options)
                .build();

        ImageLoader.getInstance().init(config);
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static void CheckMaintenance(final Context context) {
        class getinfo extends AsyncTask<String, String, JSONObject> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);

                try {
                    // mdata = new MaintenanceData(jsonObject.getString("maintenance"), jsonObject.getString("maintenance_img"));
                    if (JSonHelper.getObjString(jsonObject, JSonHelper.MAINTAINANCE_ANDROID).equals(ConstantHolder.IS_MAINTAINANCE)) {
                        Helper.setupImageCache(context);
                        LayoutInflater factory = LayoutInflater.from(context);
                        final View indexDialogView = factory.inflate(R.layout.dialog_image_btn, null);
                        final AlertDialog index_dialog = new AlertDialog.Builder(context).create();
                        index_dialog.setView(indexDialogView);
                        index_dialog.setCancelable(false);
                        index_dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        index_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                        indexDialogView.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //your business logic
                                Intent intent = new Intent(context, SplashActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                //((Activity) (context)).finish();
                                context.startActivity(intent);
                            }
                        });
//                            ImageLoader.getInstance().displayImage(jsonObject.getString(""), new ImageViewAware());
                        ImageLoader.getInstance().displayImage(jsonObject.getString("maintenance_img"), new ImageViewAware((ImageView) indexDialogView.findViewById(R.id.dialog_image), false), new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String imageUri, View view) {
                                ((ProgressBar) indexDialogView.findViewById(R.id.progressbar)).setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                ((ProgressBar) indexDialogView.findViewById(R.id.progressbar)).setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                ((ProgressBar) indexDialogView.findViewById(R.id.progressbar)).setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onLoadingCancelled(String imageUri, View view) {
                                ((ProgressBar) indexDialogView.findViewById(R.id.progressbar)).setVisibility(View.INVISIBLE);
                            }
                        });

                        try {
//                            if (!BuildConfig.DEBUG) {
                                index_dialog.show();
//                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Intent intent = new Intent(context, SplashActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(intent);
                            ((Activity) (context)).finish();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            protected JSONObject doInBackground(String... strings) {
                JSONObject json = new urlLink().appMaintenance(SavePreferences.getApplanguage(context));
                return json;
            }
        }
        new getinfo().execute();

    }



    /**
     * Open another app.
     *
     * @param context     current Context, like Activity, App, or Service
     * @param packageName the full package name of the app to open
     * @return true if likely successful, false if unsuccessful
     */
    public static boolean openApp(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        try {
            Intent i = manager.getLaunchIntentForPackage(packageName);
            if (i == null) {
                return false;
            }
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            context.startActivity(i);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String StringToTwoDecimal(String text) {
        DecimalFormat formatter = new DecimalFormat("#,###,###.00");
        if (text.equals("")) {
            return formatter.format(Double.parseDouble("0.0"));
        }
        Double d ;
        try{
           d = Double.parseDouble(text);
        }catch (Exception e){
            e.printStackTrace();
            //formatter = new DecimalFormat("#,###,###");
            return text;
        }
        return formatter.format(d);
    }

    public static String getDeviceID(Context context) {

        String device_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        if (device_id == null || device_id.length() == 0) {
            try {
                //use mac address
                WifiManager m_wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                device_id = m_wm.getConnectionInfo().getMacAddress();
            } catch (Exception e) {

            }
        }
        return device_id;
    }

    public static boolean isNetworkAvailable(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting() && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }

    }

    public static void buttonEffect(View button){
        button.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0x55f4f4f4, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
    }

    public static  void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setBackgroundColor(Color.TRANSPARENT);
    }

    public static void LoginDialog(final Context context){

        String arg = context.getString(R.string.please_login_to_continue);
        String arg2 = context.getString(R.string.login_required);
        String arg3 = context.getString(R.string.login);
        String arg4 = context.getString(R.string.cancel);

        final Dialog dialog = new Dialog(context, R.style.AlertDialogTheme);
        TypefaceUtil.overrideFont(context, "SERIF", "fonts/gotham_book.otf");
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
                //dialog.dismiss();

                context.startActivity(new Intent(context, signInActivity.class));
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ((Activity)context).finish();
            }
        });

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                ((Activity)context).finish();
            }
        });
        dialog.show();

    }


    public static String StringToDoubleDecimal(String value, String pattern){
        String result = "0.00";
        try {
            DecimalFormat format = new DecimalFormat(pattern);
            result = format.format(value);
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    public static String doubleDecimalToString(double value){
        return doubleDecimalToString(value, "0.00");
    }

    public static String doubleDecimalToString(double value, String pattern){
        String result = "0.00";
        try {
            DecimalFormat format = new DecimalFormat(pattern);
            result = format.format(value);
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    public static String encode64(String s){
        String base64 = new String();
        try {
            // Sending side
            byte[] data = s.getBytes("UTF-8");
            base64 = Base64.encodeToString(data, Base64.DEFAULT);
        }catch (UnsupportedEncodingException e){
            LogHelper.error(e.getLocalizedMessage());
        }

        return base64;
    }


}
