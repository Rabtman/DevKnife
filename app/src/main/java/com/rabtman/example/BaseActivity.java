package com.rabtman.example;

import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;

import com.rabtman.devknife.view.FloatingDragger;

public class BaseActivity extends AppCompatActivity {
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(new FloatingDragger(this, layoutResID).getView());
    }
}