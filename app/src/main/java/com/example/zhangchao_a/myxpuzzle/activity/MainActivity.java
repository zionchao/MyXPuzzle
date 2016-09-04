package com.example.zhangchao_a.myxpuzzle.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.zhangchao_a.myxpuzzle.R;
import com.example.zhangchao_a.myxpuzzle.adapter.GridItemsAdapter;
import com.example.zhangchao_a.myxpuzzle.util.ScreenUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener{

    private GridView mGvPicList;
    private List<Bitmap> mPicList;
    private int[] mResPicId;

    private PopupWindow mPopupWindow;
    private View mPopupView;

    private static final int RESUTL_IMAGE=100;
    // 返回码：相机
    private static final int RESULT_CAMERA = 200;
    private static final String IMAGE_TYPE="image/*";
    private static String TEMP_IMAGE_PATH;

    private TextView mTvPuzzleMainTypeSelected;
    private LayoutInflater mLayoutInflater;
    private TextView mTvType2;
    private TextView mTvType3;
    private TextView mTvType4;

    private int mType=2;
    private String[] mCustomItems=new String[]{"本地相册","相机拍照"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TEMP_IMAGE_PATH= Environment.getExternalStorageDirectory().getPath()+
                "/temp.png";
        mPicList=new ArrayList<Bitmap>();
        initViews();
        mGvPicList.setAdapter(new GridItemsAdapter(MainActivity.this,mPicList));
        mGvPicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==mResPicId.length-1)
                {
                    showDialogCustom();
                }else
                {
                    Intent intent=new Intent(MainActivity.this,PuzzleMain.class);
                    intent.putExtra("picSelectedID",mResPicId[position]);
                    intent.putExtra("mType",mType);
                    startActivity(intent);
                }
            }
        });


        mTvPuzzleMainTypeSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupShow(v);
            }
        });
    }

    private void showDialogCustom() {
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("选择：");
        builder.setItems(mCustomItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(0==which)
                {
                    Intent intent=new Intent(Intent.ACTION_PICK,null);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            IMAGE_TYPE);
                    startActivityForResult(intent,RESUTL_IMAGE);
                }else if(1==which)
                {
                    Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    Uri photoUri= Uri.fromFile(new File(TEMP_IMAGE_PATH));
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
                    startActivityForResult(intent,RESULT_CAMERA);
                }
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK)
        {
            if(requestCode==RESUTL_IMAGE&&data!=null)
            {
                Cursor cursor=this.getContentResolver().query(data.getData(),null,null,null,null);
                cursor.moveToFirst();
                String imagePath=cursor.getString(cursor.getColumnIndex("_data"));
                Intent intent=new Intent(MainActivity.this,PuzzleMain.class);
                intent.putExtra("picPath",imagePath);
                intent.putExtra("mType",mType);
                cursor.close();
                startActivity(intent);
            }
            else if (requestCode==RESULT_CAMERA)
            {
                Intent intent=new Intent(MainActivity.this,PuzzleMain.class);
                intent.putExtra("mPicPath",TEMP_IMAGE_PATH);
                intent.putExtra("mType",mType);
                startActivity(intent);
            }
        }
    }

    private void initViews() {
        mGvPicList=(GridView) findViewById(R.id.gv_xpuzzle_main_pic_list);
        mResPicId=new int[]{
                R.drawable.pic1, R.drawable.pic2, R.drawable.pic3,
                R.drawable.pic4, R.drawable.pic5, R.drawable.pic6,
                R.drawable.pic7, R.drawable.pic8, R.drawable.pic9,
                R.drawable.pic10, R.drawable.pic11, R.drawable.pic12,
                R.drawable.pic13, R.drawable.pic14,
                R.drawable.pic15, R.mipmap.ic_launcher
        };
        Bitmap[] bitmaps=new Bitmap[mResPicId.length];
        for (int i=0;i<bitmaps.length;i++)
        {
            bitmaps[i]= BitmapFactory.decodeResource(getResources(),mResPicId[i]);
            mPicList.add(bitmaps[i]);
        }

        mTvPuzzleMainTypeSelected= (TextView) findViewById(R.id.tv_pullzle_main_type_selected);
        mLayoutInflater= (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        mPopupView=mLayoutInflater.inflate(R.layout.xpuzzle_main_type_selected,null);
        mTvType2 = (TextView) mPopupView.findViewById(R.id.tv_main_type_2);
        mTvType3 = (TextView) mPopupView.findViewById(R.id.tv_main_type_3);
        mTvType4 = (TextView) mPopupView.findViewById(R.id.tv_main_type_4);
        mTvType2.setOnClickListener(this);
        mTvType3.setOnClickListener(this);
        mTvType4.setOnClickListener(this);

    }


    private void popupShow(View view)
    {
        int density= (int) ScreenUtil.getDeviceDensity(this);
        mPopupWindow=new PopupWindow(mPopupView,200*density,50*density);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        Drawable transpent=new ColorDrawable(Color.TRANSPARENT);
        mPopupWindow.setBackgroundDrawable(transpent);
        int[]location=new int[2];
        mPopupView.getLocationOnScreen(location);
        mPopupWindow.showAtLocation(view, Gravity.NO_GRAVITY,location[0]-40*density,location[1]+30*density);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tv_main_type_2:
                mType=2;
                mTvPuzzleMainTypeSelected.setText("2*2");
                break;
            case R.id.tv_main_type_3:
                mType = 3;
                mTvPuzzleMainTypeSelected.setText("3 X 3");
                break;
            case R.id.tv_main_type_4:
                mType = 4;
                mTvPuzzleMainTypeSelected.setText("4 X 4");
                break;
        }
        mPopupWindow.dismiss();
    }
}
