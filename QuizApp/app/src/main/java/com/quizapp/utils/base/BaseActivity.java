package com.quizapp.utils.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.quizapp.utils.PrefManager;

//It is Base class of activity. Common methods and variable declare here
public class BaseActivity extends AppCompatActivity {
    public Activity mAct = BaseActivity.this;   // Current activity context
    public PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        initBaseActivity();
    }

    private void initBaseActivity() {
        prefManager = new PrefManager();
    }
}
