package com.guftgue.others;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.guftgue.R;


public class MyToast {

    public static void display(Context context, String message) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View  view = inflater.inflate(R.layout.custom_toast_layout, null);

        Toast   mtoast = new Toast(context);
        TextView   tv = (TextView) view.findViewById(R.id.toastId);
        tv.setText(message);
        mtoast.setView(view);
        mtoast.setDuration(Toast.LENGTH_LONG);
        mtoast.show();
    }
}
