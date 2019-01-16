package com.sucisoft.test.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.TextView;

import com.sucisoft.test.R;
import com.sucisoft.test.RedPackageView;


public class NoticeActivity extends AppCompatActivity {

    private TextView txt;
    private RedPackageView red_package;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        params.height = (int) (dm.heightPixels * 0.6);
        params.width = (int) (dm.widthPixels * 0.72);
        getWindow().setAttributes(params);
        setTitle("");
        initView();
    }

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            red_package.onRefreshStatus();
        }
    };


    private void initView() {
        txt = (TextView) findViewById(R.id.txt);
        red_package = (RedPackageView) findViewById(R.id.red_package);
        red_package.setOnRedPackageOnclicking(new RedPackageView.onRedPackageOnclick() {
            @Override
            public void onOpen() {
                txt.setText("红黑树");
                handler.postDelayed(runnable, 2400);
            }
        });
    }


}
