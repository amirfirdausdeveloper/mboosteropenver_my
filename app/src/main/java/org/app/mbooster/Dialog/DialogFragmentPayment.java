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
import org.app.mbooster.Helper.Helper;
import org.app.mbooster.R;


public class DialogFragmentPayment extends DialogFragment {

    Button btnTkPhoto, btnChoose, btnCancel;


    public onSubmitListener mListener;

    public interface onSubmitListener {
        void setOnSubmitListener(String arg);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity(),R.style.AlertDialogTheme);
        TypefaceUtil.overrideFont(getActivity(), "SERIF", "fonts/gotham_book.otf");
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        dialog.setContentView(R.layout.fragment_sample_payment);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));

        dialog.show();

        btnTkPhoto = (Button) dialog.findViewById(R.id.btnTkPhoto);
        btnChoose = (Button) dialog.findViewById(R.id.btnChoosePhoto);
        btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        Helper.buttonEffect(btnTkPhoto);
        Helper.buttonEffect(btnChoose);
        Helper.buttonEffect(btnCancel);

        btnTkPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.setOnSubmitListener("TakePhoto");
                dismiss();
            }
        });

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.setOnSubmitListener("ChoosePhoto");
                dismiss();
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.setOnSubmitListener("Cancel");
                dismiss();
            }
        });


        return dialog;
    }

}
