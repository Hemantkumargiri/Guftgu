package com.guftgue.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.guftgue.R;

public class FragmentEncryptions extends Fragment {

    TextView TvEncryptiontoContacts,TvEncryptionByContacts;
    public static ImageView Imgfilter;

    @Override
    public void onResume() {
        super.onResume();
        /*TvHome.setTextColor(getResources().getColor(R.color.black));
        TvHome.setBackgroundColor(getResources().getColor(R.color.white));
        tvUserlist.setTextColor(getResources().getColor(R.color.white));
        tvUserlist.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        Tvmyencryption.setTextColor(getResources().getColor(R.color.black));
        Tvmyencryption.setBackgroundColor(getResources().getColor(R.color.white));
        Tvmyprofile.setTextColor(getResources().getColor(R.color.black));
        Tvmyprofile.setBackgroundColor(getResources().getColor(R.color.white));*/
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_encryptions,container,false);
        TvEncryptiontoContacts= view.findViewById(R.id.textViewEncryptToContact);
        TvEncryptionByContacts= view.findViewById(R.id.textViewEncryptByContact);
        Imgfilter= view.findViewById(R.id.Imgfilter);
        Fragment fragment = new FragmentUserList();
        TvEncryptiontoContacts.setTextColor(getResources().getColor(R.color.white));
        TvEncryptiontoContacts.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        TvEncryptiontoContacts.setTypeface(Typeface.DEFAULT_BOLD);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameContainerContacts,fragment).commit();
        TvEncryptiontoContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new FragmentUserList();
                TvEncryptionByContacts.setTextColor(getResources().getColor(R.color.black));
                TvEncryptiontoContacts.setTextColor(getResources().getColor(R.color.white));
                TvEncryptionByContacts.setBackgroundColor(getResources().getColor(R.color.white));
                TvEncryptiontoContacts.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameContainerContacts,fragment).commit();
            }
        });

        TvEncryptionByContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new FragmentByContact();
                TvEncryptionByContacts.setTextColor(getResources().getColor(R.color.white));
                TvEncryptiontoContacts.setTextColor(getResources().getColor(R.color.black));
                TvEncryptionByContacts.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                TvEncryptiontoContacts.setBackgroundColor(getResources().getColor(R.color.white));
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameContainerContacts,fragment).commit();
            }
        });


        return view;
    }
}
