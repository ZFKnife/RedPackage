package com.sucisoft.test;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 2033152950
 * Created by zf on 2019/1/28.
 */

public class RedDialog extends Dialog {
    private Context context;
    private RedPackage2View redPackage2View;
    private TextView txt;
    private static RedDialog redDialog;
    private ImageView cancel;


    private RedDialog(@NonNull Context context) {
        super(context, R.style.DialogStyle);
        this.context = context;
    }

    public static RedDialog ob(Context context) {
        if (RedDialog.redDialog == null) {
            RedDialog.redDialog = new RedDialog(context);
        }
        return redDialog;
    }

    private String text;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            text = "黄金搭档组合\n(1桶蒙力+1袋奶蛋白)";
            redPackage2View.onRefreshStatus();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.red_dialog);
        setCanceledOnTouchOutside(false);
        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        WindowManager windowManager = win.getWindowManager();
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        lp.width = (int) (dm.widthPixels * 0.94);
        lp.height = (int) (dm.heightPixels * 0.64);
        lp.gravity = Gravity.CENTER;
        win.setAttributes(lp);
        redPackage2View = findViewById(R.id.red_package);
        txt = findViewById(R.id.txt);
        cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context = null;
                redDialog = null;
                dismiss();
            }
        });
        redPackage2View.setOnRedPackageOnclicking(new RedPackage2View.onRedPackageOnclick() {
            @Override
            public void onOpen() {
                handler.postDelayed(runnable, 2000);
            }

            @Override
            public void onEnd() {
                txt.setText(text);
            }
        });
    }
}
