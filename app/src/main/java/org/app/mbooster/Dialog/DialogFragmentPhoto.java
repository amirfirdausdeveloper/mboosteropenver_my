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


public class DialogFragmentPhoto extends DialogFragment {

    Button btnTkPhoto, btnChoose, btnCancel;


    public onSubmitListener mListener;

    public static DialogFragmentPhoto newInstance(String arg, String arg2 ,String arg3) {
        DialogFragmentPhoto f = new DialogFragmentPhoto();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("arg", arg);
        args.putString("arg2", arg2);
        args.putString("arg3", arg3);

        f.setArguments(args);

        return f;
    }


    public interface onSubmitListener {
        void setOnSubmitListener(String arg);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity());
        TypefaceUtil.overrideFont(getActivity(), "SERIF", "fonts/gotham_book.otf");
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,

                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.fragment_sample_dialog2);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));

        dialog.show();

        final String arg = getArguments().getString("arg");
        final String arg2 = getArguments().getString("arg2");
        final String arg3 = getArguments().getString("arg3");



        btnTkPhoto = (Button) dialog.findViewById(R.id.btnTkPhoto);
        btnChoose = (Button) dialog.findViewById(R.id.btnChoosePhoto);
        btnCancel = (Button) dialog.findViewById(R.id.btnCancel);

        btnTkPhoto.setText(arg);
        btnChoose.setText(arg2);
        btnCancel.setText(arg3);

        Helper.buttonEffect(btnTkPhoto);
        Helper.buttonEffect(btnChoose);
        Helper.buttonEffect(btnCancel);

        btnTkPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.setOnSubmitListener(arg);
                dismiss();
            }
        });

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.setOnSubmitListener(arg2);
                dismiss();
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.setOnSubmitListener(arg3);
                dismiss();
            }
        });


        return dialog;
    }

}
