package com.example.zhangchao_a.myxpuzzle.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.zhangchao_a.myxpuzzle.util.ScreenUtil;

import java.util.List;

/**
 * Created by ZhangChao on 2016/9/4.
 */
public class GridItemsAdapter extends BaseAdapter{

    private Context mContext;
    private List<Bitmap> mBitmapItemLists;

    public GridItemsAdapter(Context context,List<Bitmap> picList) {
        mContext=context;
        this.mBitmapItemLists=picList;
    }

    @Override
    public int getCount() {
        return mBitmapItemLists.size();
    }

    @Override
    public Object getItem(int position) {
        return mBitmapItemLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView iv_pic_item=null;
        int density= (int) ScreenUtil.getDeviceDensity(mContext);
        if (convertView==null)
        {
            iv_pic_item=new ImageView(mContext);
            iv_pic_item.setLayoutParams(new GridView.LayoutParams(80*density,100*density));
            iv_pic_item.setScaleType(ImageView.ScaleType.FIT_XY);
        }else
        {
            iv_pic_item=(ImageView)convertView;
        }
//        iv_pic_item.setBackgroundColor(android.R.color.black);
        iv_pic_item.setImageBitmap(mBitmapItemLists.get(position));
        return iv_pic_item;
    }
}
