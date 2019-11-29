package com.guftgue.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.guftgue.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyEncryptionFragment extends Fragment {


    public MyEncryptionFragment() {
        // Required empty public constructor
    }

    public static MyEncryptionFragment newInstance(String param1, String param2) {
        MyEncryptionFragment fragment = new MyEncryptionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_encryption, container, false);
    }

}
