package org.app.mbooster.Dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.app.mbooster.Font.TypefaceUtil;
import org.app.mbooster.Helper.Helper;
import org.app.mbooster.R;


public class DialogFragmentSingleBtn extends DialogFragment {

    Button btnConfirm;
    TextView content, title;
    LinearLayout rl;
    WebView content_web;

    public onSubmitListener mListener;

    public static DialogFragmentSingleBtn newInstance(String arg, String arg2) {
        DialogFragmentSingleBtn f = new DialogFragmentSingleBtn();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("arg", arg);
        args.putString("arg2", arg2);
        f.setArguments(args);

        return f;
    }


    public interface onSubmitListener {
        void setOnSubmitListener(String arg);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Dialog dialog = new Dialog(getActivity(), R.style.AlertDialogTheme);
        TypefaceUtil.overrideFont(getActivity(), "SERIF", "fonts/gotham_book.otf");
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.fragment_dialog_single_btn);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        String arg = getArguments().getString("arg");
        String arg2 = getArguments().getString("arg2");

        rl = (LinearLayout) dialog.findViewById(R.id.rl);
        int width = getResources().getDisplayMetrics().widthPixels / 5 * 4;
        final ViewGroup.LayoutParams layoutParams = rl.getLayoutParams();
        layoutParams.width = width;
        rl.setLayoutParams(layoutParams);

        content = (TextView) dialog.findViewById(R.id.content);
        content_web = (WebView) dialog.findViewById(R.id.content_web);
        content.setVisibility(View.GONE);

//        content_web.loadData(arg, "text/html", "UTF-8");
        content_web.getSettings().setDefaultTextEncodingName("utf-8");

//        String contentstr =
//                "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" +
//                        "<html><head>" +
//                        "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />" +
//                        "</head><body>";
//
//        contentstr += arg + "</body></html>";
        WebSettings settings = content_web.getSettings();
        settings.setDefaultTextEncodingName("utf-8");
        content_web.loadData(arg, "text/html; charset=utf-8",null);

        content_web.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(final WebView view, String url) {
                super.onPageFinished(view, url);

                ViewTreeObserver viewTreeObserver = content_web.getViewTreeObserver();

                viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        int height = content_web.getMeasuredHeight();
                        if (height != 0) {
//                           Toast.makeText(getActivity(), "height:"+height,Toast.LENGTH_SHORT).show();
                            content_web.getViewTreeObserver().removeOnPreDrawListener(this);
                        }

                        ViewGroup.LayoutParams layoutParams1 = content_web.getLayoutParams();
                        layoutParams1.height = height;
                        content_web.setLayoutParams(layoutParams1);

                        return false;
                    }
                });
            }
        });
        title = (TextView) dialog.findViewById(R.id.title);
        title.setVisibility(View.GONE);
        content.setText(Html.fromHtml(arg));
        btnConfirm = (Button) dialog.findViewById(R.id.btnConfirm);
        btnConfirm.setText(arg2);

        Helper.buttonEffect(btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.setOnSubmitListener("ok");
                //dismiss();
            }
        });


        return dialog;
    }


}
