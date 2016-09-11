package com.example.zhangchao_a.myxpuzzle.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhangchao_a.myxpuzzle.R;
import com.example.zhangchao_a.myxpuzzle.adapter.GridItemsAdapter;
import com.example.zhangchao_a.myxpuzzle.bean.ItemBean;
import com.example.zhangchao_a.myxpuzzle.util.GameUtil;
import com.example.zhangchao_a.myxpuzzle.util.ImagesUtil;
import com.example.zhangchao_a.myxpuzzle.util.ScreenUtil;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ZhangChao on 2016/9/4.
 */
public class PuzzleMain extends Activity implements View.OnClickListener {

    public static Bitmap mLastBitmap;

    public static int TYPE = 2;

    public static int COUNT_INDEX = 0;
    public static int TIMER_INDEX = 0;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    TIMER_INDEX++;
                    mTvTimer.setText("" + TIMER_INDEX);
                    break;
                default:
                    break;
            }
        }
    };

    private TextView mTvTimer;
    private Bitmap mPicSelected;
    private GridView mGvPuzzleMainDetail;
    private int mResId;
    private String mPicPath;
    private ImageView mImageView;
    private Button mBtnBack;
    private Button mBtnImage;
    private Button mBtnRestart;
    private TextView mTvPuzzleMainCouont;
    private List<Bitmap> mBitmapItemList = new ArrayList<Bitmap>();
    private GridItemsAdapter mAdapter;
    private boolean mIsShowImg;
    private Timer mTimer;
    private TimerTask mTimerTask;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xpuzzle_puzzle_detail_main);
        Bitmap picSelectedTemp;
        mResId = getIntent().getExtras().getInt("picSelectedID");
        mPicPath = getIntent().getExtras().getString("mPicPath");
        if (mResId != 0) {
            picSelectedTemp = BitmapFactory.decodeResource(getResources(), mResId);
        } else
            picSelectedTemp = BitmapFactory.decodeFile(mPicPath);

        TYPE = getIntent().getExtras().getInt("mType", 2);
        handlerImage(picSelectedTemp);
        initViews();
        generateGame();
        mGvPuzzleMainDetail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (GameUtil.isMoveable(position)) {
                    GameUtil.swapItems(GameUtil.mItemBeans.get(position),
                            GameUtil.mBlankItemBean);
                    recreateData();
                    mAdapter.notifyDataSetChanged();
                    COUNT_INDEX++;
                    mTvPuzzleMainCouont.setText("" + COUNT_INDEX);
                    if (GameUtil.isSuccess()) {
                        recreateData();
                        mBitmapItemList.remove(TYPE * TYPE - 1);
                        mBitmapItemList.add(mLastBitmap);
                        mAdapter.notifyDataSetChanged();
                        ;
                        Toast.makeText(PuzzleMain.this, "拼图成功", Toast.LENGTH_LONG).show();
                        mGvPuzzleMainDetail.setEnabled(false);
                        mTimer.cancel();
                        mTimerTask.cancel();
                    }
                }
            }
        });

        mBtnBack.setOnClickListener(this);
        mBtnImage.setOnClickListener(this);
        mBtnRestart.setOnClickListener(this);
    }

    private void generateGame() {
        new ImagesUtil().createInitBitmaps(TYPE, mPicSelected, PuzzleMain.this);
        GameUtil.getPuzzleGenerator();
        for (ItemBean temp : GameUtil.mItemBeans) {
            mBitmapItemList.add(temp.getmBitmap());
        }
        mAdapter = new GridItemsAdapter(this, mBitmapItemList);
        mGvPuzzleMainDetail.setAdapter(mAdapter);
        mTimer = new Timer(true);
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                mHandler.sendMessage(msg);
            }
        };
        mTimer.schedule(mTimerTask, 0, 1000);
    }

    private void initViews() {
        mBtnBack = (Button) findViewById(R.id.btn_puzzle_main_back);
        mBtnImage = (Button) findViewById(R.id.btn_puzzle_main_img);
        mBtnRestart = (Button) findViewById(R.id.btn_puzzle_main_restart);
        mIsShowImg = false;
        mGvPuzzleMainDetail = (GridView) findViewById(R.id.gv_puzzle_main_detail);
        mGvPuzzleMainDetail.setNumColumns(TYPE);
        RelativeLayout.LayoutParams gridParams = new RelativeLayout.LayoutParams(mPicSelected.getWidth(),
                mPicSelected.getHeight());
        gridParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        gridParams.addRule(RelativeLayout.BELOW, R.id.ll_puzzle_main_spinner);
        mGvPuzzleMainDetail.setLayoutParams(gridParams);
        mGvPuzzleMainDetail.setHorizontalSpacing(0);
        mGvPuzzleMainDetail.setVerticalSpacing(0);
        mTvPuzzleMainCouont = (TextView) findViewById(R.id.tv_puzzle_main_counts);
        mTvTimer = (TextView) findViewById(R.id.tv_puzzle_main_time);
        mTvTimer.setText("0秒");
        addImageView();

    }

    private void addImageView() {
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.rl_puzzle_main_main_layout);
        mImageView=new ImageView(PuzzleMain.this);
        mImageView.setImageBitmap(mPicSelected);
        int x= (int) (mPicSelected.getWidth()*0.9F);
        int y=(int) (mPicSelected.getHeight()*0.9F);
        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(x,y);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        mImageView.setLayoutParams(params);
        relativeLayout.addView(mImageView);
        mImageView.setVisibility(View.GONE);

    }

    private void handlerImage(Bitmap bitmap) {
        int screenWidth = ScreenUtil.getScreenSize(this).widthPixels;
        int screenHeight = ScreenUtil.getScreenSize(this).heightPixels;
        mPicSelected = new ImagesUtil().resizeBitmap(
                screenWidth * 0.8f, screenHeight * 0.6f,
                bitmap);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_puzzle_main_back:
                PuzzleMain.this.finish();
                break;
            case R.id.btn_puzzle_main_img:
                Animation animShow = AnimationUtils.loadAnimation(PuzzleMain.this,
                        R.anim.image_show_anim);
                Animation animHide = AnimationUtils.loadAnimation(PuzzleMain.this,
                        R.anim.image_hide_anim);
                if (mIsShowImg) {
                    mImageView.startAnimation(animHide);
                    mImageView.setVisibility(View.GONE);
                    mIsShowImg = false;
                } else {
                    mImageView.startAnimation(animShow);
                    mImageView.setVisibility(View.VISIBLE);
                    mIsShowImg = true;
                }
                break;
            case R.id.btn_puzzle_main_restart:
                cleanConfig();
                generateGame();
                recreateData();
                mTvPuzzleMainCouont.setText("" + COUNT_INDEX);
                mAdapter.notifyDataSetChanged();
                mGvPuzzleMainDetail.setEnabled(true);
                break;
        }
    }

    private void cleanConfig() {
        GameUtil.mItemBeans.clear();
        mTimer.cancel();
        mTimerTask.cancel();
        COUNT_INDEX = 0;
        TIMER_INDEX = 0;
        if (mPicPath != null) {
            File file = new File(MainActivity.TEMP_IMAGE_PATH);
            if (file.exists())
                file.delete();
        }
    }

    private void recreateData() {
        mBitmapItemList.clear();
        for (ItemBean temp : GameUtil.mItemBeans) {
            mBitmapItemList.add(temp.getmBitmap());
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
        cleanConfig();
        this.finish();

    }
}
