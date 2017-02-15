package com.rabtman.example;

import android.os.Bundle;
import android.view.View;

import com.rabtman.devknife.MockLocationPopWindow;

/**
 * @author Rabtman
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.showMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MockLocationPopWindow(getBaseContext()).showAsDropDown(v);
            }
        });


        BaiduMapManager.getInstance(this).startTrace();
    }
}
