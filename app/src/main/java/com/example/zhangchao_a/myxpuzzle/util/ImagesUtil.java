package com.example.zhangchao_a.myxpuzzle.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import com.example.zhangchao_a.myxpuzzle.R;
import com.example.zhangchao_a.myxpuzzle.activity.PuzzleMain;
import com.example.zhangchao_a.myxpuzzle.bean.ItemBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZhangChao on 2016/9/4.
 */
public class ImagesUtil {
    private ItemBean itemBean;

    public void createInitBitmaps(int type,Bitmap picSelected,Context context)
    {
        Bitmap bitmap=null;
        List<Bitmap> bitmapItems=new ArrayList<Bitmap>();
        int itemWidth=picSelected.getWidth()/type;
        int itemHeight=picSelected.getHeight()/type;
        for(int i=1;i<=type;i++)
        {
            for(int j=1;j<=type;j++)
            {
                bitmap=Bitmap.createBitmap(picSelected,(j-1)*itemWidth,
                        (i-1)*itemHeight,itemWidth,itemHeight);
                bitmapItems.add(bitmap);
                itemBean=new ItemBean((i-1)*type+j,(i-1)*type+j,bitmap);
                GameUtil.mItemBeans.add(itemBean);
            }
        }

        PuzzleMain.mLastBitmap=bitmapItems.get(type*type-1);
        bitmapItems.remove(type*type-1);
        GameUtil.mItemBeans.remove(type*type-1);
        Bitmap blankBitmap= BitmapFactory.decodeResource(context.getResources(), R.drawable.blank);
        blankBitmap=Bitmap.createBitmap(blankBitmap,0,0,itemWidth,itemHeight);

        bitmapItems.add(blankBitmap);
        GameUtil.mItemBeans.add(new ItemBean(type*type,0,blankBitmap));
        GameUtil.mBlankItemBean=GameUtil.mItemBeans.get(type*type-1);
    }

    public  Bitmap resizeBitmap(float newWidth,float newHeight,Bitmap bitmap)
    {
        Matrix matrix=new Matrix();
        matrix.postScale(newWidth/bitmap.getWidth(),
                newHeight/bitmap.getHeight());
        Bitmap newBitmap=Bitmap.createBitmap(bitmap,0,0,
                bitmap.getWidth(),
                bitmap.getHeight(),
                matrix,true);
        return newBitmap;
    }


}
