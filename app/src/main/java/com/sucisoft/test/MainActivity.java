package com.sucisoft.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.sucisoft.test.activity.NoticeActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }

    public boolean checkNumber(Object value) {
        String str = String.valueOf(value);
        String regex = "^[0-9]*$";
        return str.matches(regex);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn:
                Intent intent = new Intent(this, NoticeActivity.class);
                startActivity(intent);
                break;
            case R.id.btn1:
                RedDialog.ob(this).show();
                break;
        }
    }

    private void initView() {
        btn = (Button) findViewById(R.id.btn);

        btn.setOnClickListener(this);
    }
}
