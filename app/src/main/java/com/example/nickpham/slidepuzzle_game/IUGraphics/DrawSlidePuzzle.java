package com.example.nickpham.slidepuzzle_game.IUGraphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.nickpham.slidepuzzle_game.MainActivity;
import com.example.nickpham.slidepuzzle_game.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

/**
 * Created by nickpham on 28/11/2016.
 */

public class DrawSlidePuzzle
{


    LinearLayout  get_main_layout;
    Context       context;
    ImageView     get_ImageView_slide_example;
    ImageView[][] sg_slide;
    List<Bitmap>  bt_image_slide;

    int Level_Play;

    public DrawSlidePuzzle(LinearLayout get_main_layout, Context context,List<Bitmap> bt_image_slide, int Level_Play)
    {

        this.get_main_layout = get_main_layout;
        this.context         = context;
        this.Level_Play      = Level_Play;
        this.bt_image_slide  = bt_image_slide;

        Setup_IU();

        // Khởi tạo hành động khi người chơi chạm vào các slide8
        Custom_Action_sg_slide_sg();

        // Khởi tạo thuộc tính cho các slide
        Custom_sg_slide();

        // Thêm các slide vào khung trò chơi cho người dùng sắp xếp
        Add_Main_Layout();

        // Lock Board
        Lock_Play_Board();

        if (MainActivity.StartGameEnable)
        {

            //Unlock_Board_Play();

            // Xáo trộn các slide
            this.Random_Position_SG_Slide();

            // Draw text in Slides
            this.Draw_Text_In_Slide();

        }

    }


    public void Setup_IU()
    {


        sg_slide = new ImageView[Level_Play][Level_Play];
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View get_gp_sg_slide = inflater.inflate(R.layout.style_sg_slide, null, false);
        this.get_ImageView_slide_example = (ImageView) get_gp_sg_slide.findViewById(R.id.button_eg_sg_slide);

    }



    public  void Draw_Text_In_Slide()
    {

        for (int i = 0; i < bt_image_slide.size(); i ++)
        {

            /**Canvas canvas = new Canvas(bt_image_slide.get(i));

            Paint paint = new Paint();
            paint.setColor(Color.YELLOW); // Text Color
            paint.setTextSize(15);
            paint.setStrokeWidth(20);
            // Text Size
            // some more settings...

            canvas.drawText(String.valueOf(i + 1), 20, 20, paint);
             */

            Bitmap BM_WV = bt_image_slide.get(i);

            Bitmap Bitmap_Have_Text = Bitmap.createBitmap(BM_WV.getWidth(), BM_WV.getHeight(), BM_WV.getConfig());

            Canvas Paper_Draw = new Canvas(Bitmap_Have_Text);

            Paper_Draw.drawBitmap(BM_WV, 0, 0, null);

            Paint CoVe = new Paint();

            CoVe.setTextSize(15);
            CoVe.setStrokeWidth(20);
            CoVe.setColor(Color.YELLOW);

            Paper_Draw.drawText(String.valueOf(i + 1), 20, 20, CoVe);

            bt_image_slide.set(i, Bitmap_Have_Text);

        }

    }


    public void Custom_sg_slide()
    {
        for (int dong = 0; dong < sg_slide.length; dong++)
        {

            for (int cot = 0; cot < sg_slide[dong].length; cot++)
            {

                int position_id_tag_text = (sg_slide.length * dong) + cot;

                ImageView btn_sg_slide = new ImageView(context);

                int nWidth  = get_ImageView_slide_example.getLayoutParams().width   * 3 / Level_Play;
                int nHeight = get_ImageView_slide_example.getLayoutParams().height  * 3 / Level_Play;
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(nWidth, nHeight);

                btn_sg_slide.setLayoutParams(params);
                btn_sg_slide.setScaleType(ImageView.ScaleType.FIT_XY);
                btn_sg_slide.setBackgroundResource(R.drawable.custom_border_slides);
                btn_sg_slide.setId(position_id_tag_text);

                if (position_id_tag_text != (Level_Play * Level_Play) - 1)
                {
                    btn_sg_slide.setImageBitmap(bt_image_slide.get(position_id_tag_text));
                }

                btn_sg_slide.setOnClickListener(action_sg_slide);
                btn_sg_slide.setTag(position_id_tag_text);
                sg_slide[dong][cot] = btn_sg_slide;
            }
        }


    }

    // Thêm những hình ảnh đơn vào trong main layout để người dùng suy nghĩ và xếp các hình ảnh lại với nhau
    public void Add_Main_Layout()
    {

        get_main_layout.removeAllViews();

        for (int dong = 0; dong < sg_slide.length; dong ++)
        {

            LinearLayout SG_Line = new LinearLayout(context);
            RelativeLayout.LayoutParams params_SG_Line = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            SG_Line.setLayoutParams(params_SG_Line);
            SG_Line.setTag(dong + 1);
            SG_Line.setOrientation(LinearLayout.HORIZONTAL);

            for (int cot = 0; cot < sg_slide[dong].length; cot ++)
            {

                SG_Line.addView(sg_slide[dong][cot]);

            }

            get_main_layout.addView(SG_Line);

        }

    }

    // Hành động xảy ra khi người chơi nhấn vào các slide
    public View.OnClickListener action_sg_slide;
    public void Custom_Action_sg_slide_sg()
    {

        action_sg_slide = new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                int line  = (int) view.getId() / Level_Play;
                int colum = (int) view.getId() - ( Level_Play * line);

                Log.d("ID_Slide ", String.valueOf(view.getTag()));
                Log.d("Line "    , String.valueOf((int) view.getId() / Level_Play));
                Log.d("Colum "   , String.valueOf(colum));

                ImageView bt_clicking = (ImageView) view;


                if ((int) bt_clicking.getTag() != Level_Play * Level_Play - 1)
                {

                    Check_Move_sg_slide(line, colum);

                }

            }
        };

    }

    // Xáo trộn vị trí các slide trong mảng
    public CountDownTimer Anim_R = null;
    public void Random_Position_SG_Slide()
    {

        sg_slide[Level_Play - 1][Level_Play - 1].setImageBitmap(null);
        sg_slide[sg_slide.length - 1][sg_slide.length - 1].setTag(Level_Play * Level_Play - 1);


        Anim_R = new CountDownTimer(3000, 200) {
            @Override
            public void onTick(long l)
            {

                List<Integer> added = new ArrayList<>();
                for (int dong = 0; dong < sg_slide.length; dong ++) {

                    for (int cot = 0; cot < sg_slide[dong].length; cot++) {
                        if (dong * Level_Play + cot != Level_Play * Level_Play - 1) {
                            int rans;

                            if (added.size() == 0)
                            {

                                rans = new Random().nextInt(Level_Play * Level_Play - 1);

                            } else {

                                // Random position of Slides
                                do
                                {

                                    rans = new Random().nextInt(Level_Play * Level_Play - 1);

                                } while (added.indexOf(rans) != -1);

                            }


                            Bitmap b_a_image;
                            if (added.indexOf(rans) == -1)
                            {

                                b_a_image = bt_image_slide.get(rans);
                                added.add(rans);

                                sg_slide[dong][cot].setTag(rans);
                                sg_slide[dong][cot].setImageBitmap(b_a_image);

                            }


                            Log.d("Rans", String.valueOf(rans));
                            Log.d("Size", String.valueOf(added.size()));

                        }
                    }
                }

            }

            @Override
            public void onFinish()
            {


                Anim_R = null;
                Unlock_Board_Play();

            }
        };

        Anim_R.start();

    }

    // Kiểm tra coi thử đủ điều kiện để di chuyển View hay chưa
    public void Check_Move_sg_slide(int line, int colum)
    {
        boolean result_check = false;

        if (line + 1 <= Level_Play - 1)
        {
            if ((int) sg_slide[line + 1][colum].getTag() == Level_Play * Level_Play - 1)
            {
                int old_tag = (int) sg_slide[line][colum].getTag();

                sg_slide[line][colum].setTag(Level_Play * Level_Play - 1);
                sg_slide[line][colum].setImageBitmap(null);

                sg_slide[line + 1][colum].setTag(old_tag);

                SetImage_Slide(line + 1, colum, old_tag);

                result_check = true;
            }
        }


        if (line - 1 >= 0)
        {
            if ((int) sg_slide[line - 1][colum].getTag() == Level_Play * Level_Play - 1)
            {
                int old_tag = (int) sg_slide[line][colum].getTag();

                sg_slide[line][colum].setImageBitmap(null);

                sg_slide[line][colum].setTag(Level_Play * Level_Play - 1);

                sg_slide[line - 1][colum].setTag(old_tag);

                SetImage_Slide(line - 1, colum, old_tag);

                result_check = true;
            }
        }


        if (colum + 1 <= Level_Play - 1)
        {
            if ((int) sg_slide[line][colum + 1].getTag() == Level_Play * Level_Play - 1)
            {
                int old_tag = (int) sg_slide[line][colum].getTag();

                sg_slide[line][colum].setTag(Level_Play * Level_Play - 1);
                sg_slide[line][colum].setImageBitmap(null);

                sg_slide[line][colum + 1].setTag((old_tag));

                SetImage_Slide(line, colum + 1, old_tag);
                result_check = true;
            }
        }

        if (colum - 1 >= 0)
        {
            if ((int) sg_slide[line][colum - 1].getTag() == Level_Play * Level_Play - 1)
            {
                int old_tag = (int) sg_slide[line][colum].getTag();

                sg_slide[line][colum].setTag(Level_Play * Level_Play - 1);
                sg_slide[line][colum].setImageBitmap(null);

                sg_slide[line][colum - 1].setTag(old_tag);

                SetImage_Slide(line, colum - 1, old_tag);

                result_check = true;
            }
        }

        if (result_check)
        {

            MainActivity.Move_Amount += 1;
            MainActivity.Show_Move_Amount.setText(String.valueOf(MainActivity.Move_Amount));

        }


        check_win();

    }


    public void check_win()
    {

        boolean isWin = true;

        if((int) sg_slide[Level_Play - 1][Level_Play - 1].getTag() == Level_Play * Level_Play - 1)
        {

            for (int dong = 0; dong < sg_slide.length; dong ++)
            {
                for (int cot = 0; cot < sg_slide.length; cot ++)
                {
                    if ((int) sg_slide[dong][cot].getTag() != Level_Play * Level_Play - 1)
                    {

                        int pos = dong * Level_Play + cot;

                        if ((int) sg_slide[dong][cot].getTag() != pos) {

                            isWin = false;
                        }

                    }
                }
            }

        }else
        {
            isWin = false;
        }

        Log.d("check_win", String.valueOf(isWin));

        if (isWin)
        {

            Toast.makeText(context, "Xin chuc mung, ban da thang", Toast.LENGTH_LONG);

            MainActivity.Action_When_Win(context);


        }
    }


    public void SetImage_Slide(int row, int colum, int tag)
    {
        Bitmap cv_change = bt_image_slide.get(tag);

        sg_slide[row][colum].setImageBitmap(cv_change);
    }


    public void Lock_Play_Board()
    {

        // Disnable slides
        for (int dong = 0; dong < sg_slide.length; dong++)
        {

            for (int cot = 0; cot < sg_slide.length; cot++)
            {

                sg_slide[dong][cot].setEnabled(false);

            }

        }

        // Do something

    }

    public void Unlock_Board_Play()
    {

        // Enable slides
        for (int dong = 0; dong < sg_slide.length; dong++)
        {

            for (int cot = 0; cot < sg_slide.length; cot++)
            {

                sg_slide[dong][cot].setEnabled(true);

            }

        }

        // Do something

    }


}
