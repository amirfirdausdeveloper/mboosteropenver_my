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


public class DialogFragmentUniversal extends DialogFragment {

    Button  btnConfirm, btnCancel;
    TextView content,title;

    public static DialogFragmentUniversal newInstance(String arg, String arg2, String arg3, String arg4) {
        DialogFragmentUniversal f = new DialogFragmentUniversal();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("arg", arg);
        args.putString("arg2", arg2);
        args.putString("arg3", arg3);
        args.putString("arg4", arg4);
        f.setArguments(args);

        return f;
    }


    public onSubmitListener mListener;

    public interface onSubmitListener {
        void setOnSubmitListener(String arg);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String arg = getArguments().getString("arg");
        String arg2 = getArguments().getString("arg2");
        final String arg3 = getArguments().getString("arg3");
        final String arg4 = getArguments().getString("arg4");
        final Dialog dialog = new Dialog(getActivity(),R.style.AlertDialogTheme);
        TypefaceUtil.overrideFont(getActivity(), "SERIF", "fonts/gotham_book.otf");
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.fragment_dialog_universal);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        content = (TextView) dialog.findViewById(R.id.content);
        title = (TextView) dialog.findViewById(R.id.title);
        title.setText(arg2);
        btnConfirm = (Button) dialog.findViewById(R.id.btnConfirm);
        btnCancel = (Button) dialog.findViewById(R.id.btnCancel);

        btnConfirm.setText(arg3);
        btnCancel.setText(arg4);

        Helper.buttonEffect(btnConfirm);
        Helper.buttonEffect(btnCancel);


        content.setText(arg);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.setOnSubmitListener(arg3);
                dismiss();
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.setOnSubmitListener(arg4);
                dismiss();
            }
        });


        return dialog;
    }



}
