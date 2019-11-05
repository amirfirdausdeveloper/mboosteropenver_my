package org.app.mbooster.Dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import org.app.mbooster.Font.TypefaceUtil;
import org.app.mbooster.Helper.Helper;
import org.app.mbooster.R;


public class DialogFragmentAppVersion extends DialogFragment {

    Button  btnConfirm;
    TextView content,title;


    public onSubmitListener mListener;

    public interface onSubmitListener {
        void setOnSubmitListener(String arg);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Dialog dialog = new Dialog(getActivity(),R.style.AlertDialogTheme);
        TypefaceUtil.overrideFont(getActivity(), "SERIF", "fonts/gotham_book.otf");
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.fragment_dialog_app_ver);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        content = (TextView) dialog.findViewById(R.id.content);
        title = (TextView) dialog.findViewById(R.id.title);
        btnConfirm = (Button) dialog.findViewById(R.id.btnConfirm);

        Helper.buttonEffect(btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.setOnSubmitListener("goplaystore");
                //dismiss();
            }
        });


        return dialog;
    }



}
