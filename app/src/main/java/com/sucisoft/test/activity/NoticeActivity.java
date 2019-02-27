package com.sucisoft.test.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.sucisoft.test.R;
import com.sucisoft.test.RedPackageView;


public class NoticeActivity extends AppCompatActivity {

    private TextView txt;
    private RedPackageView red_package;
    private ImageView cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        params.height = (int) (dm.heightPixels);
        params.width = (int) (dm.widthPixels);
        getWindow().setAttributes(params);
        setTitle("");
        initView();
    }

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            txt.setText("黄金搭档组合");
        }
    };


    private void initView() {
        txt = (TextView) findViewById(R.id.txt);
        red_package = (RedPackageView) findViewById(R.id.red_package);
        cancel = (ImageView) findViewById(R.id.cancel);
        red_package.setOnRedPackageOnclicking(new RedPackageView.onRedPackageOnclick() {
            @Override
            public void onOpen() {
                handler.postDelayed(runnable, 1400);
            }

        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
