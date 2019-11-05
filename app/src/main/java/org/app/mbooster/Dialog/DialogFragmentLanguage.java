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

import org.app.mbooster.Font.TypefaceUtil;
import org.app.mbooster.R;


public class DialogFragmentLanguage extends DialogFragment {

    Button btnEN, btnCNTW, btnCN;


    public onSubmitListener mListener;

    public interface onSubmitListener {
        void setOnSubmitListener(String arg, String arg2);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity(),R.style.AlertDialogTheme);
        TypefaceUtil.overrideFont(getActivity(), "SERIF", "fonts/gotham_book.otf");
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.fragment_dialog_language);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();


        btnEN = (Button) dialog.findViewById(R.id.btnEN);
        btnCNTW = (Button) dialog.findViewById(R.id.btnCNTW);
        btnCN = (Button) dialog.findViewById(R.id.btnCN);


        btnEN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.setOnSubmitListener("EN", "");
                dismiss();
            }
        });

        btnCNTW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.setOnSubmitListener("zh", "TW");
                dismiss();
            }
        });


        btnCN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.setOnSubmitListener("zh", "CN");
                dismiss();
            }
        });


        return dialog;
    }

}
