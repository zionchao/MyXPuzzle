package com.example.zhangchao_a.myxpuzzle.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.example.zhangchao_a.myxpuzzle.R;

import org.w3c.dom.Text;

/**
 * Created by ZhangChao on 2016/9/4.
 */
public class PuzzleMain extends Activity implements View.OnClickListener{

    public static Bitmap mLastBitmap;

    public static int TYPE=2;

    public static int COUNT_INDEX=0;
    public static int TIMER_INDEX=0;

    private Handler mHandler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what)
            {
                case 1:
                    TIMER_INDEX++;
                    mTvTimer.setText(""+TIMER_INDEX);
                    break;
                default:
                    break;
            }
        }
    };

    private TextView mTvTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xpuzzle_puzzle_detail_main);
    }

    @Override
    public void onClick(View v) {

    }
}
