package com.guftgue.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.guftgue.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentDashBoard extends Fragment {

    private LinearLayout mLLmyEncryptions,mLLTocontacts,mLLByContacts;

    private String[] permissions = new String[]
            {
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.READ_SMS,
                    Manifest.permission.RECEIVE_SMS,
                    Manifest.permission.SEND_SMS,
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.WRITE_CONTACTS
            };
    private int REQUEST_ID_MULTIPLE_PERMISSIONS = 14;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.activity_dash_board,container,false);



        checkPermissions();

        mLLmyEncryptions=view.findViewById(R.id.LLmyEncryptions);
        mLLTocontacts=view.findViewById(R.id.LLTocontacts);
        mLLByContacts=view.findViewById(R.id.LLByContacts);
/*
        mLLByContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MainActivity.class)
                        .putExtra("tag","bycontacts"));
            }
        });
        mLLTocontacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),MainActivity.class)
                        .putExtra("tag","tocontacts"));
            }
        });

        mLLmyEncryptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),MainActivity.class)
                        .putExtra("tag","myencryption"));
            }
        });*/
        return view;

    }



    public boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String permission : permissions) {
            result = ContextCompat.checkSelfPermission(getActivity(), permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(permission);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

/*    @Override
    public void onBackPressed() {
        Snackbar snackbar = Snackbar
                .make(view.findViewById(R.id.LLDashboard), "Press Exit to Close the App", Snackbar.LENGTH_LONG)
                //getWindow().getDecorView().getRootView()
                .setAction("EXIT", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getActivity().finish();
                    }
                });
        View snakView = snackbar.getView();
        snakView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        TextView textView1 = (TextView) snakView.findViewById(R.id.snackbar_action);
        textView1.setTextColor(Color.BLACK);
        textView1.setTypeface(textView1.getTypeface(), Typeface.BOLD);
        textView1.setTextSize(20);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        textView.setTextSize(16);
        snackbar.show();
    }*/


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //
    }
}
