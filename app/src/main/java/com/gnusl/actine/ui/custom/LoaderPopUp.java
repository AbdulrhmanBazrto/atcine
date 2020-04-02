package com.gnusl.actine.ui.custom;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.gnusl.actine.R;

public class LoaderPopUp extends DialogFragment {

    private static LoaderPopUp loaderPopUp;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View view = getActivity().getLayoutInflater().inflate(R.layout.loader_popup_layout, null);
        GifImageView gifImageView = view.findViewById(R.id.gif1);
        gifImageView.setGifImageResource(R.drawable.loader);
        dialog.setContentView(view);
        setCancelable(false);
        dialog.show();
        return dialog;
    }

    public static void show(FragmentActivity fragmentActivity) {

        if (loaderPopUp == null)
            loaderPopUp = new LoaderPopUp();
        if (fragmentActivity == null)
            return;

        if (loaderPopUp.isAdded())
            return;

        loaderPopUp.show(fragmentActivity.getSupportFragmentManager(), "");


    }

    public static void dismissLoader() {

        if (loaderPopUp == null)
            return;

        loaderPopUp.dismiss();
    }
}

