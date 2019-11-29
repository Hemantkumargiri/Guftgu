package com.guftgue.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.guftgue.R;

public class CreateImageEncryptionActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText EdtEncryptionkey;
    private EditText EdtConfEncryptionkey;
    private Button btnCreate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_image_encryption);

        toolbar = findViewById(R.id.toolbar);
        EdtConfEncryptionkey = findViewById(R.id.EdtConfEncryptionkey);
        btnCreate = findViewById(R.id.btnCreate);
        EdtEncryptionkey = findViewById(R.id.EdtEncryptionkey);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
