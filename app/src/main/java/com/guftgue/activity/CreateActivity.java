package com.guftgue.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBar;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.guftgue.others.AppConstants;
import com.guftgue.others.MyStateview;
import com.guftgue.others.ProgressClickListener;
import com.guftgue.others.SelectItemInterface;
import com.guftgue.adapter.ChooseCharacterRVAdapter;
import com.guftgue.adapter.ChooseSpclCharacterRVAdapter;
import com.guftgue.model.Model;
import com.guftgue.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.guftgue.others.ApiServices.add_my_encryption_setting;

public class CreateActivity extends AppCompatActivity implements ChooseCharacterRVAdapter.SelectItemInterface, ChooseSpclCharacterRVAdapter.SelectSpclItemInterface, ProgressClickListener {

    private RecyclerView mRVChooseCharacters;
    private ArrayList<Model> arrayList;
    private TextView mTxtToolbarTitle;
    private ChooseCharacterRVAdapter adapter;
    private RecyclerView mRvReplacewith;
    private ArrayList<Model> arrayList1;
    private Toolbar mToolBar;
    private MyStateview mStateview;
    private Button mbtnCreate;
    private ChooseSpclCharacterRVAdapter adapter1;
    int selectedPosition = -1;
    private SelectItemInterface selectItemInterface;
    public String alphabets;
    public String spclCharacter;
    private EditText mTvChooseletters;
    private String alpha;
    private RelativeLayout mLLCharacter, mLLSpclCharater;
    private EditText EdtEncryptionName;
    String setting_id;
    ArrayList<String> encryptData = new ArrayList<>();
    private String user_id;
    private String encryption_type;
    private LinearLayout llRoot;
    private ArrayList<String> Encryption1 = new ArrayList<>();
    private ArrayList<String> Encryption2 = new ArrayList<>();
    private ArrayList<String> Encryption3 = new ArrayList<>();
    private ArrayList<String> AdditionalChar= new ArrayList<>();

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        prepareToolbar();
        CapitalEncryption();
        mTvChooseletters = findViewById(R.id.TvChooseletters);
        EdtEncryptionName = findViewById(R.id.EdtEncryptionName);
        llRoot = findViewById(R.id.llRoot);
        llRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
            }
        });
        mTvChooseletters.setEnabled(false);
        mStateview = new MyStateview(this, null);
        mbtnCreate = findViewById(R.id.btnCreate);

        String ButtonType = this.getIntent().getStringExtra("type");
        setting_id = this.getIntent().getStringExtra("setting_id");
        if (setting_id == null) {
            setting_id = "";
        }
        if (ButtonType.equalsIgnoreCase("edit")) {
            mbtnCreate.setText("Recreate");
        } else {
            mbtnCreate.setText("Create");
        }

        encryption_type = this.getIntent().getStringExtra(AppConstants.EN_TYPE);
        Log.d("encryption_type", "-----------------------" + encryption_type);

        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", "");
        Log.d("user_id", "----------------" + user_id);
        mLLCharacter = findViewById(R.id.LLCharacter);
        mLLSpclCharater = findViewById(R.id.LLSpclCharater);
        mLLSpclCharater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CreateActivity.this, "please select character First", Toast.LENGTH_SHORT).show();
            }
        });

        /*toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/
        AdditionalChar.add("."+"=="+".");
        AdditionalChar.add("?"+"=="+"?");
        // AdditionalChar.add(","+"=="+",");
        mbtnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (encryptionValidate()) {
                    encryptData.addAll(AdditionalChar);
                    if(encryption_type.equalsIgnoreCase(AppConstants.ENCRYPTION_ONE)) {
                        encryptData.addAll(Encryption1);
                    }else if(encryption_type.equalsIgnoreCase(AppConstants.ENCRYPTION_TWO)){
                        encryptData.addAll(Encryption2);
                    }else if(encryption_type.equalsIgnoreCase(AppConstants.ENCRYPTION_THREE)){
                        encryptData.addAll(Encryption3);
                    }
                    Log.d("All data","-----------"+encryptData.toString().replace("[", "").replace("]", ""));
                    validate();
                }
            }
        });

        mRVChooseCharacters = (RecyclerView) findViewById(R.id.RVChooseCharacters);
        arrayList = new ArrayList<>();
        arrayList.add(new Model("0"));
        arrayList.add(new Model("1"));
        arrayList.add(new Model("2"));
        arrayList.add(new Model("3"));
        arrayList.add(new Model("4"));
        arrayList.add(new Model("5"));
        arrayList.add(new Model("6"));
        arrayList.add(new Model("7"));
        arrayList.add(new Model("8"));
        arrayList.add(new Model("9"));
        arrayList.add(new Model("a"));
        arrayList.add(new Model("b"));
        arrayList.add(new Model("c"));
        arrayList.add(new Model("d"));
        arrayList.add(new Model("e"));
        arrayList.add(new Model("f"));
        arrayList.add(new Model("g"));
        arrayList.add(new Model("h"));
        arrayList.add(new Model("i"));
        arrayList.add(new Model("j"));
        arrayList.add(new Model("k"));
        arrayList.add(new Model("l"));
        arrayList.add(new Model("m"));
        arrayList.add(new Model("n"));
        arrayList.add(new Model("o"));
        arrayList.add(new Model("p"));
        arrayList.add(new Model("q"));
        arrayList.add(new Model("r"));
        arrayList.add(new Model("s"));
        arrayList.add(new Model("t"));
        arrayList.add(new Model("u"));
        arrayList.add(new Model("v"));
        arrayList.add(new Model("w"));
        arrayList.add(new Model("x"));
        arrayList.add(new Model("y"));
        arrayList.add(new Model("z"));

        mRVChooseCharacters.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new ChooseCharacterRVAdapter(this, arrayList, mRVChooseCharacters, CreateActivity.this);
        mRVChooseCharacters.setAdapter(adapter);


        mRvReplacewith = (RecyclerView) findViewById(R.id.RvReplacewith);
        arrayList1 = new ArrayList<>();
        arrayList1.add(new Model("@"));
        arrayList1.add(new Model("#"));
        arrayList1.add(new Model("$"));
        arrayList1.add(new Model("%"));
        arrayList1.add(new Model("±"));
        arrayList1.add(new Model("¯"));
        arrayList1.add(new Model("³"));
        arrayList1.add(new Model("¾"));
        arrayList1.add(new Model("À"));
        arrayList1.add(new Model("Á"));
        arrayList1.add(new Model("Â"));
        arrayList1.add(new Model("Ã"));
        arrayList1.add(new Model("^"));
        arrayList1.add(new Model("&"));
        arrayList1.add(new Model("*"));
        arrayList1.add(new Model("+"));
        arrayList1.add(new Model("_"));
        arrayList1.add(new Model("("));
        arrayList1.add(new Model(")"));
        arrayList1.add(new Model("="));
        arrayList1.add(new Model("-"));
        arrayList1.add(new Model("{"));
        arrayList1.add(new Model("}"));
        arrayList1.add(new Model("¥"));
        arrayList1.add(new Model("‡"));
        arrayList1.add(new Model(":"));
        arrayList1.add(new Model("²"));
        arrayList1.add(new Model("/"));
        arrayList1.add(new Model(">"));
        arrayList1.add(new Model("<"));
        arrayList1.add(new Model("|"));
        arrayList1.add(new Model("~"));
        arrayList1.add(new Model("!"));
        arrayList1.add(new Model(";"));
        arrayList1.add(new Model("`"));
        arrayList1.add(new Model("†"));

        mRvReplacewith.setLayoutManager(new GridLayoutManager(this, 2));
        adapter1 = new ChooseSpclCharacterRVAdapter(this, arrayList1, mRvReplacewith, CreateActivity.this);
        adapter1.isClickable = false;
        //  mRvReplacewith.setVisibility(View.GONE);

        mLLCharacter.setVisibility(View.VISIBLE);
        mRvReplacewith.setAdapter(adapter1);


    }

    private void prepareToolbar() {
        mToolBar = findViewById(R.id.main_activity_toolbar);
        mTxtToolbarTitle = findViewById(R.id.toolbarTitle);
        setSupportActionBar(mToolBar); // Setting/replace toolbar as the ActionBar
        mTxtToolbarTitle.setText("CREATE ENCRYPTIONS");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public  void CapitalEncryption(){
        Encryption1.add('A'+"=="+'×');
        Encryption1.add('B'+"=="+'ß');
        Encryption1.add('C'+"=="+'‰');
        Encryption1.add('D'+"=="+'Þ');
        Encryption1.add('E'+"=="+'Æ');
        Encryption1.add('F'+"=="+'ø');
        Encryption1.add('G'+"=="+'÷');
        Encryption1.add('H'+"=="+'ƒ');
        Encryption1.add('I'+"=="+'ý');
        Encryption1.add('J'+"=="+'ð');
        Encryption1.add('K'+"=="+'š');
        Encryption1.add('L'+"=="+'Ú');
        Encryption1.add('M'+"=="+'Ó');
        Encryption1.add('N'+"=="+'Ð');
        Encryption1.add('O'+"=="+'Ì');
        Encryption1.add('P'+"=="+'Ê');
        Encryption1.add('Q'+"=="+'È');
        Encryption1.add('R'+"=="+'Å');
        Encryption1.add('S'+"=="+'Ä');
        Encryption1.add('T'+"=="+'—');
        Encryption1.add('U'+"=="+'®');
        Encryption1.add('V'+"=="+'©');
        Encryption1.add('W'+"=="+'€');
        Encryption1.add('X'+"=="+'µ');
        Encryption1.add('Y'+"=="+'ñ');
        Encryption1.add('Z'+"=="+'ç');

        Encryption2.add('A'+"=="+'‰');
        Encryption2.add('B'+"=="+'ß');
        Encryption2.add('C'+"=="+'×');
        Encryption2.add('D'+"=="+'÷');
        Encryption2.add('E'+"=="+'ƒ');
        Encryption2.add('F'+"=="+'ø');
        Encryption2.add('G'+"=="+'Þ');
        Encryption2.add('H'+"=="+'Æ');
        Encryption2.add('I'+"=="+'š');
        Encryption2.add('J'+"=="+'ð');
        Encryption2.add('K'+"=="+'ý');
        Encryption2.add('L'+"=="+'Ð');
        Encryption2.add('M'+"=="+'Ê');
        Encryption2.add('N'+"=="+'Ú');
        Encryption2.add('O'+"=="+'Ì');
        Encryption2.add('P'+"=="+'Ó');
        Encryption2.add('Q'+"=="+'È');
        Encryption2.add('R'+"=="+'Å');
        Encryption2.add('S'+"=="+'Ä');
        Encryption2.add('T'+"=="+'—');
        Encryption2.add('U'+"=="+'®');
        Encryption2.add('V'+"=="+'©');
        Encryption2.add('W'+"=="+'€');
        Encryption2.add('X'+"=="+'µ');
        Encryption2.add('Y'+"=="+'ñ');
        Encryption2.add('Z'+"=="+'ç');


        Encryption3.add('A'+"=="+'‰');
        Encryption3.add('B'+"=="+'ß');
        Encryption3.add('C'+"=="+'×');
        Encryption3.add('D'+"=="+'÷');
        Encryption3.add('E'+"=="+'ƒ');
        Encryption3.add('F'+"=="+'ø');
        Encryption3.add('G'+"=="+'Þ');
        Encryption3.add('H'+"=="+'Æ');
        Encryption3.add('I'+"=="+'š');
        Encryption3.add('J'+"=="+'ð');
        Encryption3.add('K'+"=="+'ý');
        Encryption3.add('L'+"=="+'Ð');
        Encryption3.add('M'+"=="+'Ê');
        Encryption3.add('N'+"=="+'Ú');
        Encryption3.add('O'+"=="+'Ì');
        Encryption3.add('P'+"=="+'Ó');
        Encryption3.add('Q'+"=="+'È');
        Encryption3.add('R'+"=="+'Å');
        Encryption3.add('S'+"=="+'Ä');
        Encryption3.add('T'+"=="+'—');
        Encryption3.add('U'+"=="+'®');
        Encryption3.add('V'+"=="+'©');
        Encryption3.add('W'+"=="+'€');
        Encryption3.add('X'+"=="+'µ');
        Encryption3.add('Y'+"=="+'ñ');
        Encryption3.add('Z'+"=="+'ç');
    }

    @Override
    public void onClick(View view, int position) {
        hideKeyboard(view);

        if (adapter.isCharacterClikable) {
            alphabets = arrayList.get(position).getmTvAlphabet();
            Log.d("alphabet", "==================" + alphabets);
            arrayList.remove(position);
            adapter.notifyDataSetChanged();
            //encryptData.add(alphabets + "==");
            mTvChooseletters.setText(alphabets + "==");
            adapter1.isClickable = true;
        } else {
            Snackbar snackbar = Snackbar
                    .make(getWindow().getDecorView().getRootView(), "Please select Replace Character", Snackbar.LENGTH_LONG);
            View snakView = snackbar.getView();
            snakView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
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
        }
        adapter.isCharacterClikable = false;
    }


    public boolean encryptionValidate() {
        if (!(arrayList.isEmpty())) {
            Log.d("arraylist_size", "----------------" + arrayList.size());
            Snackbar snackbar = Snackbar
                    .make(getWindow().getDecorView().getRootView(), "Please Select All Characters", Snackbar.LENGTH_LONG);
            View snakView = snackbar.getView();
            snakView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
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
            return false;
        } else if (!(arrayList1.isEmpty())) {
            Log.d("arraylist_size1", "----------------" + arrayList1.size());
            Snackbar snackbar = Snackbar
                    .make(getWindow().getDecorView().getRootView(), "Please Select All Characters", Snackbar.LENGTH_LONG);
            View snakView = snackbar.getView();
            snakView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
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
            return false;
        }
        return true;
    }

    @Override
    public void onItemClick(View view, int position) {

        if (adapter1.isClickable) {
            Log.d("arraylist", "------------------" + arrayList.size());
            Log.d("arraylist1", "------------------" + arrayList1.size());

            spclCharacter = arrayList1.get(position).getmTvAlphabet();
            Log.d("alphabet", "==================" + spclCharacter);
            arrayList1.remove(position);
            adapter1.notifyDataSetChanged();
            mTvChooseletters.setText("");
            encryptData.add(alphabets + "==" + spclCharacter);
            mTvChooseletters.setText(alphabets + "==" + spclCharacter);
            mTvChooseletters.setText(encryptData.toString().replace("[", "").replace("]", ""));
        } else {
            Snackbar snackbar = Snackbar
                    .make(getWindow().getDecorView().getRootView(), "Please select Character First", Snackbar.LENGTH_LONG);
            //getWindow().getDecorView().getRootView()
            View snakView = snackbar.getView();
            snakView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
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

        }
        adapter1.isClickable = false;
        adapter.isCharacterClikable = true;

    }

    public void validate() {
        mStateview.showLoading();
        String EncryptionName;
        if (EdtEncryptionName.getText().toString().trim().isEmpty()) {
            EncryptionName = encryption_type;
        } else {
            EncryptionName = EdtEncryptionName.getText().toString().trim();
        }
        Log.d("user_id", "-------------------" + user_id);
        Log.d("encryp_logic", "-------------------" +encryptData.toString().replace("[", "").replace("]", ""));
        Log.d("encription_type", "-------------------" + encryption_type);
        Log.d("setting_id", "-------------------" + setting_id);
        Log.d("encription_name", "-------------------" + EdtEncryptionName.getText().toString().trim());
        AndroidNetworking.post(add_my_encryption_setting)
                .addBodyParameter("user_id", user_id)
                .addBodyParameter("encription_name", EncryptionName)
                .addBodyParameter("encryp_logic",encryptData.toString().replace("[", "").replace("]", ""))
                .addBodyParameter("encription_type", encryption_type)
                .addBodyParameter("setting_id", setting_id)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", "-------------" + response);
                        try {
                            mStateview.showContent();
                            if (response.getString("code").equalsIgnoreCase("200")) {
                                String message = response.getString("message");

                                Snackbar snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), message, Snackbar.LENGTH_SHORT);
                                View snakView = snackbar.getView();
                                snakView.setBackgroundColor(ContextCompat.getColor(CreateActivity.this, R.color.colorPrimary));
                                TextView textView1 = (TextView) snakView.findViewById(R.id.snackbar_action);
                                textView1.setTextColor(Color.BLACK);
                                textView1.setTypeface(textView1.getTypeface(), Typeface.BOLD);
                                textView1.setTextSize(16);
                                View sbView = snackbar.getView();
                                TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
                                textView.setTextColor(Color.WHITE);
                                textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
                                textView.setTextSize(16);
                                snackbar.show();

                                startActivity(new Intent(CreateActivity.this, EncryptDecryptDashboardActivity.class)
                                                .putExtra("tag", "myencryption")
                              /*  .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)*/);
                                finish();
                            } else {
                                mStateview.showContent();
                                Toast.makeText(CreateActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            mStateview.showRetry();
                        }
                    }


                    @Override
                    public void onError(ANError anError) {
                        mStateview.showRetry();

                    }
                });

    }

    @Override
    public void onRetryClick() {

    }
}





/*
package com.guftgue.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.guftgue.others.AppConstants;
import com.guftgue.others.MyStateview;
import com.guftgue.others.ProgressClickListener;
import com.guftgue.others.SelectItemInterface;
import com.guftgue.adapter.ChooseCharacterRVAdapter;
import com.guftgue.adapter.ChooseSpclCharacterRVAdapter;
import com.guftgue.model.Model;
import com.guftgue.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.guftgue.others.ApiServices.add_my_encryption_setting;

public class CreateActivity extends AppCompatActivity implements ChooseCharacterRVAdapter.SelectItemInterface, ChooseSpclCharacterRVAdapter.SelectSpclItemInterface, ProgressClickListener {

    private RecyclerView mRVChooseCharacters;
    private ArrayList<Model> arrayList;
    private ChooseCharacterRVAdapter adapter;
    private RecyclerView mRvReplacewith;
    private ArrayList<Model> arrayList1;
    private Toolbar toolbar;
    private MyStateview mStateview;
    private Button mbtnCreate;
    private ChooseSpclCharacterRVAdapter adapter1;
    int selectedPosition = -1;
    private SelectItemInterface selectItemInterface;
    public String alphabets;
    public String spclCharacter;
    private EditText mTvChooseletters;
    private String alpha;
    private RelativeLayout mLLCharacter, mLLSpclCharater;
    private EditText EdtEncryptionName;
    String setting_id;
    ArrayList<String> encryptData = new ArrayList<>();
    private String user_id;
    private String encryption_type;
    private LinearLayout llRoot;
    private ArrayList<String> capitalEncryption= new ArrayList<>();
    private ArrayList<String> AdditionalChar= new ArrayList<>();

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        CapitalEncryption();
        mTvChooseletters = findViewById(R.id.TvChooseletters);
        EdtEncryptionName = findViewById(R.id.EdtEncryptionName);
        llRoot = findViewById(R.id.llRoot);
        llRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
            }
        });
        mTvChooseletters.setEnabled(false);
        mStateview = new MyStateview(this, null);
        mbtnCreate = findViewById(R.id.btnCreate);
        String ButtonType = this.getIntent().getStringExtra("type");
        setting_id = this.getIntent().getStringExtra("setting_id");
        if (setting_id == null) {
            setting_id = "";
        }
        if (ButtonType.equalsIgnoreCase("edit")) {
            mbtnCreate.setText("Recreate");
        } else {
            mbtnCreate.setText("Create");
        }

        encryption_type = this.getIntent().getStringExtra(AppConstants.EN_TYPE);
        Log.d("encryption_type", "-----------------------" + encryption_type);

        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", "");
        Log.d("user_id", "----------------" + user_id);
        mLLCharacter = findViewById(R.id.LLCharacter);
        mLLSpclCharater = findViewById(R.id.LLSpclCharater);
        mLLSpclCharater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CreateActivity.this, "please select character First", Toast.LENGTH_SHORT).show();
            }
        });

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        AdditionalChar.add("."+"=="+".");
        AdditionalChar.add("?"+"=="+"?");
      //  AdditionalChar.add(","+"=="+"ß");
        mbtnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (encryptionValidate()) {
                    encryptData.addAll(AdditionalChar);
                 //   encryptData.addAll(capitalEncryption);
                    Log.d("All data","-----------"+encryptData.toString().replace("[", "").replace("]", ""));
                    validate();
                }
            }
        });
        mRVChooseCharacters = (RecyclerView) findViewById(R.id.RVChooseCharacters);
        arrayList = new ArrayList<>();
        arrayList.add(new Model("0"));
        arrayList.add(new Model("1"));
        arrayList.add(new Model("2"));
        arrayList.add(new Model("3"));
        arrayList.add(new Model("4"));
        arrayList.add(new Model("5"));
        arrayList.add(new Model("6"));
        arrayList.add(new Model("7"));
        arrayList.add(new Model("8"));
        arrayList.add(new Model("9"));
        arrayList.add(new Model("a"));
        arrayList.add(new Model("A"));
        arrayList.add(new Model("b"));
        arrayList.add(new Model("B"));
        arrayList.add(new Model("c"));
        arrayList.add(new Model("C"));
        arrayList.add(new Model("d"));
        arrayList.add(new Model("D"));
        arrayList.add(new Model("e"));
        arrayList.add(new Model("E"));
        arrayList.add(new Model("f"));
        arrayList.add(new Model("F"));
        arrayList.add(new Model("g"));
        arrayList.add(new Model("G"));
        arrayList.add(new Model("h"));
        arrayList.add(new Model("H"));
        arrayList.add(new Model("i"));
        arrayList.add(new Model("I"));
        arrayList.add(new Model("j"));
        arrayList.add(new Model("J"));
        arrayList.add(new Model("k"));
        arrayList.add(new Model("K"));
        arrayList.add(new Model("l"));
        arrayList.add(new Model("L"));
        arrayList.add(new Model("m"));
        arrayList.add(new Model("M"));
        arrayList.add(new Model("n"));
        arrayList.add(new Model("N"));
        arrayList.add(new Model("o"));
        arrayList.add(new Model("O"));
        arrayList.add(new Model("p"));
        arrayList.add(new Model("P"));
        arrayList.add(new Model("q"));
        arrayList.add(new Model("Q"));
        arrayList.add(new Model("r"));
        arrayList.add(new Model("R"));
        arrayList.add(new Model("s"));
        arrayList.add(new Model("S"));
        arrayList.add(new Model("t"));
        arrayList.add(new Model("T"));
        arrayList.add(new Model("u"));
        arrayList.add(new Model("U"));
        arrayList.add(new Model("v"));
        arrayList.add(new Model("V"));
        arrayList.add(new Model("w"));
        arrayList.add(new Model("W"));
        arrayList.add(new Model("x"));
        arrayList.add(new Model("X"));
        arrayList.add(new Model("y"));
        arrayList.add(new Model("Y"));
        arrayList.add(new Model("z"));
        arrayList.add(new Model("Z"));

        mRVChooseCharacters.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new ChooseCharacterRVAdapter(this, arrayList, mRVChooseCharacters, CreateActivity.this);
        mRVChooseCharacters.setAdapter(adapter);


        mRvReplacewith = (RecyclerView) findViewById(R.id.RvReplacewith);
        arrayList1 = new ArrayList<>();
        arrayList1.add(new Model("@"));
        arrayList1.add(new Model("#"));
        arrayList1.add(new Model("$"));
        arrayList1.add(new Model("%"));
        arrayList1.add(new Model("±"));
        arrayList1.add(new Model("¯"));
        arrayList1.add(new Model("³"));
        arrayList1.add(new Model("¾"));
        arrayList1.add(new Model("À"));
        arrayList1.add(new Model("Á"));
        arrayList1.add(new Model("Â"));
        arrayList1.add(new Model("Ã"));
        arrayList1.add(new Model("^"));
        arrayList1.add(new Model("&"));
        arrayList1.add(new Model("*"));
        arrayList1.add(new Model("+"));
        arrayList1.add(new Model("_"));
        arrayList1.add(new Model("("));
        arrayList1.add(new Model(")"));
        arrayList1.add(new Model("="));
        arrayList1.add(new Model("-"));
        arrayList1.add(new Model("{"));
        arrayList1.add(new Model("}"));
        arrayList1.add(new Model("¥"));
        arrayList1.add(new Model("þ"));
        arrayList1.add(new Model(":"));
        arrayList1.add(new Model("²"));
        arrayList1.add(new Model("/"));
        arrayList1.add(new Model(">"));
        arrayList1.add(new Model("<"));
        arrayList1.add(new Model("|"));
        arrayList1.add(new Model("~"));
        arrayList1.add(new Model("!"));
        arrayList1.add(new Model(";"));
        arrayList1.add(new Model("`"));
        arrayList1.add(new Model("å"));
        arrayList1.add(new Model("…"));
        arrayList1.add(new Model("'"));
        arrayList1.add(new Model("µ"));
        arrayList1.add(new Model("€"));
        arrayList1.add(new Model("©"));
        arrayList1.add(new Model("®"));
        arrayList1.add(new Model("á"));
        arrayList1.add(new Model("Ä"));
        arrayList1.add(new Model("Å"));
        arrayList1.add(new Model("È"));
        arrayList1.add(new Model("Ê"));
        arrayList1.add(new Model("Ì"));
        arrayList1.add(new Model("Ð"));
        arrayList1.add(new Model("Ó"));
        arrayList1.add(new Model("Ú"));
        arrayList1.add(new Model("š"));
        arrayList1.add(new Model("ð"));
        arrayList1.add(new Model("ý"));
        arrayList1.add(new Model("ƒ"));
        arrayList1.add(new Model("÷"));
        arrayList1.add(new Model("ø"));
        arrayList1.add(new Model("Æ"));
        arrayList1.add(new Model("Þ"));
        arrayList1.add(new Model("‰"));
        arrayList1.add(new Model("ß"));
        arrayList1.add(new Model("×"));

        mRvReplacewith.setLayoutManager(new GridLayoutManager(this, 2));
        adapter1 = new ChooseSpclCharacterRVAdapter(this, arrayList1, mRvReplacewith, CreateActivity.this);
        adapter1.isClickable = false;
        //  mRvReplacewith.setVisibility(View.GONE);

        mLLCharacter.setVisibility(View.VISIBLE);
        mRvReplacewith.setAdapter(adapter1);


    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public  void CapitalEncryption(){
        capitalEncryption.add('A'+"=="+'×');
        capitalEncryption.add('B'+"=="+'ß');
        capitalEncryption.add('C'+"=="+'‰');
        capitalEncryption.add('D'+"=="+'Þ');
        capitalEncryption.add('E'+"=="+'Æ');
        capitalEncryption.add('F'+"=="+'ø');
        capitalEncryption.add('G'+"=="+'÷');
        capitalEncryption.add('H'+"=="+'ƒ');
        capitalEncryption.add('I'+"=="+'ý');
        capitalEncryption.add('J'+"=="+'ð');
        capitalEncryption.add('K'+"=="+'š');
        capitalEncryption.add('L'+"=="+'Ú');
        capitalEncryption.add('M'+"=="+'Ó');
        capitalEncryption.add('N'+"=="+'Ð');
        capitalEncryption.add('O'+"=="+'Ì');
        capitalEncryption.add('P'+"=="+'Ê');
        capitalEncryption.add('Q'+"=="+'È');
        capitalEncryption.add('R'+"=="+'Å');
        capitalEncryption.add('S'+"=="+'Ä');
        capitalEncryption.add('T'+"=="+'á');
        capitalEncryption.add('U'+"=="+'®');
        capitalEncryption.add('V'+"=="+'©');
        capitalEncryption.add('W'+"=="+'€');
        capitalEncryption.add('X'+"=="+'µ');
        capitalEncryption.add('Y'+"=="+'å');
        capitalEncryption.add('Z'+"=="+'`');
    }

    @Override
    public void onClick(View view, int position) {
        hideKeyboard(view);

        if (adapter.isCharacterClikable) {
            alphabets = arrayList.get(position).getmTvAlphabet();
            Log.d("alphabet", "==================" + alphabets);
            arrayList.remove(position);
            adapter.notifyDataSetChanged();
            //encryptData.add(alphabets + "==");
            mTvChooseletters.setText(alphabets + "==");
            adapter1.isClickable = true;
        } else {
            Snackbar snackbar = Snackbar
                    .make(getWindow().getDecorView().getRootView(), "Please select Replace Character", Snackbar.LENGTH_LONG);
            View snakView = snackbar.getView();
            snakView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
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
        }
        adapter.isCharacterClikable = false;
    }


    public boolean encryptionValidate() {
        if (!(arrayList.isEmpty())) {
            Log.d("arraylist_size", "----------------" + arrayList.size());
            Snackbar snackbar = Snackbar
                    .make(getWindow().getDecorView().getRootView(), "Please Select All Characters", Snackbar.LENGTH_LONG);
            View snakView = snackbar.getView();
            snakView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
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
            return false;
        } else if (!(arrayList1.isEmpty())) {
            Log.d("arraylist_size1", "----------------" + arrayList1.size());
            Snackbar snackbar = Snackbar
                    .make(getWindow().getDecorView().getRootView(), "Please Select All Characters", Snackbar.LENGTH_LONG);
            View snakView = snackbar.getView();
            snakView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
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
            return false;
        }
        return true;
    }

    @Override
    public void onItemClick(View view, int position) {

        if (adapter1.isClickable) {
            Log.d("arraylist", "------------------" + arrayList.size());
            Log.d("arraylist1", "------------------" + arrayList1.size());

            spclCharacter = arrayList1.get(position).getmTvAlphabet();
            Log.d("alphabet", "==================" + spclCharacter);
            arrayList1.remove(position);
            adapter1.notifyDataSetChanged();
            mTvChooseletters.setText("");
            encryptData.add(alphabets + "==" + spclCharacter);
            mTvChooseletters.setText(alphabets + "==" + spclCharacter);
            mTvChooseletters.setText(encryptData.toString().replace("[", "").replace("]", ""));
        } else {
            Snackbar snackbar = Snackbar
                    .make(getWindow().getDecorView().getRootView(), "Please select Character First", Snackbar.LENGTH_LONG);
            //getWindow().getDecorView().getRootView()
            View snakView = snackbar.getView();
            snakView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
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

        }
        adapter1.isClickable = false;
        adapter.isCharacterClikable = true;

    }

    public void validate() {
        mStateview.showLoading();
        String EncryptionName;
        if (EdtEncryptionName.getText().toString().trim().isEmpty()) {
            EncryptionName = encryption_type;
        } else {
            EncryptionName = EdtEncryptionName.getText().toString().trim();
        }
        Log.d("user_id", "-------------------" + user_id);
        Log.d("encryp_logic", "-------------------" +encryptData.toString().replace("[", "").replace("]", ""));
        Log.d("encription_type", "-------------------" + encryption_type);
        Log.d("setting_id", "-------------------" + setting_id);
        Log.d("encription_name", "-------------------" + EdtEncryptionName.getText().toString().trim());
        AndroidNetworking.post(add_my_encryption_setting)
                .addBodyParameter("user_id", user_id)
                .addBodyParameter("encription_name", EncryptionName)
                .addBodyParameter("encryp_logic",encryptData.toString().replace("[", "").replace("]", ""))
                .addBodyParameter("encription_type", encryption_type)
                .addBodyParameter("setting_id", setting_id)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", "-------------" + response);
                        try {
                            mStateview.showContent();
                            if (response.getString("code").equalsIgnoreCase("200")) {
                                String message = response.getString("message");

                                Snackbar snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), message, Snackbar.LENGTH_SHORT);
                                View snakView = snackbar.getView();
                                snakView.setBackgroundColor(ContextCompat.getColor(CreateActivity.this, R.color.colorPrimary));
                                TextView textView1 = (TextView) snakView.findViewById(R.id.snackbar_action);
                                textView1.setTextColor(Color.BLACK);
                                textView1.setTypeface(textView1.getTypeface(), Typeface.BOLD);
                                textView1.setTextSize(16);
                                View sbView = snackbar.getView();
                                TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
                                textView.setTextColor(Color.WHITE);
                                textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
                                textView.setTextSize(16);
                                snackbar.show();

                                startActivity(new Intent(CreateActivity.this, NewDashboard.class)
                                                .putExtra("tag", "myencryption")
                              */
/*  .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)*//*
);
                                finish();
                            } else {
                                mStateview.showContent();
                                Toast.makeText(CreateActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            mStateview.showRetry();
                        }
                    }


                    @Override
                    public void onError(ANError anError) {
                        mStateview.showRetry();

                    }
                });

    }

    @Override
    public void onRetryClick() {

    }
}
*/
