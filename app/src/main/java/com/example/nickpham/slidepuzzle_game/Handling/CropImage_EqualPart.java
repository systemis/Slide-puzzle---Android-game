package com.example.nickpham.slidepuzzle_game.Handling;

import android.app.WallpaperInfo;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.nickpham.slidepuzzle_game.R;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 * Created by nickpham on 29/11/2016.
 */

public class CropImage_EqualPart
{


    // Nhiệm vụ của class này là chia hình ảnh người dùng chọn ra làm 8 phần bằng nhau để làm các slide
    public ArrayList<Bitmap> bs= new ArrayList<Bitmap>();

    Bitmap b;
    int Level_Play;

    public CropImage_EqualPart(Context context, Bitmap getBitmap, int Level_Play)
    {
        this.b = getBitmap;
        this.Level_Play = Level_Play;

        My_Code(b);

        bs.remove(bs.size() - 1);

    }


    public void My_Code(Bitmap b_cv)
    {

        int width  = b_cv.getWidth();
        int height = b_cv.getHeight();

        int colum_px = width / Level_Play;
        int row_px = height / Level_Play ;

        for (int i = 0; i < Level_Play; i++)
        {

            for (int j = 0; j < Level_Play; j++)
            {

                int start_y = row_px * i;
                int start_x = colum_px * j;

                Bitmap sg_bm = Bitmap.createBitmap(b_cv, start_x, start_y, colum_px, row_px);

                bs.add(sg_bm);

                sg_bm = null;

            }

        }

    }


    public Bitmap Crop_Image_Main_Slide(Bitmap b_cv)
    {
        Bitmap BM_Result_Crop = null;

        int Width  = b_cv.getWidth();
        int Height = b_cv.getHeight();

        int colum_px  = Width  - ( Width  / Level_Play);
        int row_px    = Height - ( Height / Level_Play);

        BM_Result_Crop = Bitmap.createBitmap(b_cv, 0, 0, colum_px, row_px);


        return BM_Result_Crop;

    }



}
