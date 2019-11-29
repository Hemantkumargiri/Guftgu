package com.guftgue.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.guftgue.R;
import com.guftgue.model.encrSendlist;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class EncryptDecryptFragment extends Fragment {
    private TextView mTxtToolbarTitle;
    private Toolbar mToolBar;
    private ImageView mProfileImage;
    private RelativeLayout mRelativeLayoutText, mRelativeLayoutImage;
    private String encryption;
    Context context;

    public EncryptDecryptFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_encrypt_decrypt, container, false);
        init(view);
        prepareToolbar();
        setListener();
        return view;
    }

    private void prepareToolbar() {
        /*setSupportActionBar(mToolBar); // Setting/replace toolbar as the ActionBar
        mTxtToolbarTitle.setText("Text Encrypt / Decrypt");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowHomeEnabled(true);
        }*/
    }

    private void setListener() {
        mRelativeLayoutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("encryption",encryption);
                TextEncryptDecryptFragment fragment = new TextEncryptDecryptFragment();
                fragment.setArguments(bundle);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        mRelativeLayoutImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImageEncryptDecryptFragment fragment = new ImageEncryptDecryptFragment();
                //fragment.setArguments(bundle);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    private void init(View view) {
        mToolBar = view.findViewById(R.id.main_activity_toolbar);
        mTxtToolbarTitle = view.findViewById(R.id.toolbarTitle);
        mProfileImage = view.findViewById(R.id.toolbar_profile_image);
        mRelativeLayoutText = view.findViewById(R.id.relativeLayoutTextEncrypt);
        mRelativeLayoutImage = view.findViewById(R.id.relativeLayoutImageEncrypt);
        encryption = getActivity().getIntent().getStringExtra("encryption");
    }
}
