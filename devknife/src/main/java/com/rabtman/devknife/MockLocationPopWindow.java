package com.rabtman.devknife;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

/**
 * @author Rabtman
 */
public class MockLocationPopWindow extends PopupWindow {

    private EditText txtLat, txtLog;
    private double mLat = 0;
    private double mLog = 0;

    public MockLocationPopWindow(Context context) {
        initView(context);
    }

    private void initView(Context context) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.popwindow_mock_location, null);

        txtLat = (EditText) rootView.findViewById(R.id.edit_lat);
        txtLog = (EditText) rootView.findViewById(R.id.edit_log);
        Button moveUp = (Button) rootView.findViewById(R.id.move_up);
        Button moveDown = (Button) rootView.findViewById(R.id.move_down);
        Button moveLeft = (Button) rootView.findViewById(R.id.move_left);
        Button moveRight = (Button) rootView.findViewById(R.id.move_right);

        moveUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLat++;
                txtLat.setText(mLat + "");
            }
        });
        moveDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLat--;
                txtLat.setText(mLat + "");
            }
        });
        moveLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLog++;
                txtLog.setText(mLog + "");
            }
        });
        moveRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLog--;
                txtLog.setText(mLog + "");
            }
        });

        this.setContentView(rootView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setBackgroundDrawable(new ColorDrawable(0xffffff));
        this.setOutsideTouchable(true);
        this.setFocusable(true);
    }

}
