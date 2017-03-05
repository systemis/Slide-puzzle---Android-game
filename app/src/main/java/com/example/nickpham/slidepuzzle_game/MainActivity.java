package com.example.nickpham.slidepuzzle_game;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.nickpham.slidepuzzle_game.DialogManager.Dialog_Pause_Game;
import com.example.nickpham.slidepuzzle_game.DialogManager.Dialog_Start_Game;
import com.example.nickpham.slidepuzzle_game.Handling.CropImage_EqualPart;
import com.example.nickpham.slidepuzzle_game.IUGraphics.DrawSlidePuzzle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{



    public static Bitmap  main_slide;
    public static int     Level_Play = 3;
    public static boolean StartGameEnable = false;



    public static LinearLayout   main_layout_slide;
    public static RelativeLayout MainAcitivtyLayout;
    public static TextView       Show_Time_Play, Show_Move_Amount;
    public static ImageView      IMG_StartGame_F;
    public static View           Get_Example_View;
    public static ImageButton    bt_stop_game;


    // Covert bitmap to byte[] and add to bt_image_slide
    public static List<Bitmap> bt_image_slide = new ArrayList<Bitmap>();

    public static int            Move_Amount = 0;
    public static int            Time_Play = 0;
    public static CountDownTimer Count_Time_Play;


    public static MediaPlayer Background_Music = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find views from layous resource
        Anhxa();

        // Example Slide Board
        main_slide = BitmapFactory.decodeResource(getResources(), R.mipmap.sanfragsico_bridge_photo);
        Draw_slidePuzzle = new DrawSlidePuzzle(main_layout_slide, MainActivity.this, new CropImage_EqualPart(MainActivity.this, main_slide, 6).bs, 6);

        // Custom background music
        Custom_Background_Music(MainActivity.this);

        Action_Pause_Game();
    }


    // Thuộc tính cho nhạc nền
    public static void Custom_Background_Music(Context context)
    {

        Background_Music = MediaPlayer.create(context, R.raw.background_music);

        Background_Music.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer)
            {

                mediaPlayer.reset();

            }
        });

    }

    public void Anhxa()
    {

        MainAcitivtyLayout = (RelativeLayout)  this.findViewById(R.id.activity_main);
        IMG_StartGame_F    = (ImageView)       this.findViewById(R.id.btn_start_game_example);

        main_layout_slide  = (LinearLayout)    this.findViewById(R.id.main_layout_slide);
        Show_Time_Play     = (TextView)        this.findViewById(R.id.Show_Time_Play);
        Show_Move_Amount   = (TextView)        this.findViewById(R.id.Show_Move_Amount);
        bt_stop_game       = (ImageButton)     this.findViewById(R.id.bt_pause_game);
        Get_Example_View   = ((LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.style_sg_slide, null, false);

        bt_stop_game.setEnabled(false);
        bt_stop_game.setImageResource(android.R.drawable.ic_media_play);
        bt_stop_game.setTag(0);

    }

    // Công cụ cắt ảnh ra từng phần theo từng cấp độ chơi
    public static CropImage_EqualPart equalPart;
    public static DrawSlidePuzzle Draw_slidePuzzle;

    public static void Handling_Slide_Board(Context context) {

        // Crop image to qual parts
        equalPart = new CropImage_EqualPart(context, main_slide, Level_Play);

        bt_image_slide = equalPart.bs;

        // Draw Slide Puzzle Board
        Draw_slidePuzzle = new DrawSlidePuzzle(main_layout_slide, context, bt_image_slide, Level_Play);



    }

    // Set thuộc tính cho bộ đếm thời gian
    public static void Custom_Time_Play()
    {

        Count_Time_Play = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long l) {

                Time_Play += 1;

                int Minute = Time_Play / 60;
                int Second = Time_Play - (60 * Minute);

                Show_Time_Play.setText(String.valueOf(Minute + ":" + Second));

            }

            @Override
            public void onFinish() {

                Count_Time_Play.start();

            }
        };

    }


    // Những hành động diễn ra khi người dùng nhấn vào Button Stop Game
    public void Action_Pause_Game() {

        bt_stop_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch ((int) bt_stop_game.getTag()) {

                    case 1:

                        Stop_Game(MainActivity.this, 2);

                        break;

                    case 0:

                        Restart_Game(MainActivity.this, main_slide, Level_Play);

                        break;

                    case 2:

                        Resume_Game();

                        break;
                }

            }
        });

    }


    // Khởi động Game khi người dùng lần đầu tiên nhấn vào nút button stop game ở giữa màn hình kể từ lúc mở game
    public static void Fist_Play(Context context, Bitmap Main_Slide, int GET_Level_Play)
    {

        // Start App by StartGameEnable boolean
        StartGameEnable = true;

        bt_stop_game.setEnabled(true);
        bt_stop_game.setTag(1);
        bt_stop_game.setImageResource(android.R.drawable.ic_media_pause);

        // Setup time play
        Custom_Time_Play();
        Count_Time_Play.start();

        // Get Level play and Main slides to play
        main_slide = Main_Slide;
        Level_Play = GET_Level_Play;

        // Add slide board
        Handling_Slide_Board(context);

        if (BackgroundMusicEnable)
        {

            Custom_Background_Music(context);
            Background_Music.start();

        }

    }

    // Những hành động diễn ra khi người dùng muốn dừng game
    public static void Stop_Game(Context context, int Mode_Stop)
    {

        // Exit Show Main Slide
        if (Main_Slide_Show != null)
        {

            if ((int) CV_View_ImageView_Actiong.getTag() == 1)
            {

                Comprise_Main_Layout_Slide.removeView(Main_Slide_Show);
                CV_View_ImageView_Actiong.setTag(0);
                CV_View_ImageView_Actiong.setImageResource(R.mipmap.eye);

            }

        }




        // RETURN BUTTON STOP GAME WITH Mode_Stop
        bt_stop_game.setImageResource(android.R.drawable.ic_media_play);
        bt_stop_game.setTag(Mode_Stop);

        // PAUSE TIME PLAY
        if (Count_Time_Play != null)
        {
            Count_Time_Play.cancel();
            Count_Time_Play = null;
        }

        // BLOCK SLIDE BOARD
        Draw_slidePuzzle.Lock_Play_Board();


        if (Mode_Stop == 2)
        {

            // SHOW DIALOG PAUSE GAME
            new Dialog_Pause_Game(context);

            if (Background_Music != null) Background_Music.pause();

            // Stop Random Action
            if (Draw_slidePuzzle.Anim_R != null)
            {
                Draw_slidePuzzle.Anim_R.cancel();
                //Draw_slidePuzzle.Unlock_Board_Play();
            }

        }else
        {

            // RETURN Move_Amout & Time_Play TO 0
            Move_Amount = 0;
            Time_Play = 0;

            // SET IMAGE FOR BUTTON STOP
            bt_stop_game.setImageResource(R.mipmap.restart_icon);

            if (Background_Music != null) Background_Music.stop();

        }

    }


    // Những hành động diễn ra khi người dùng muốn chơi lại Game với màn khác
    public static void Restart_Game(Context context, Bitmap get_main_slide, int GET_Level_Play)
    {

        // REMOVE OLD SLIDE BOARD
        main_layout_slide.removeAllViews();

        Show_Time_Play  .setText("00:00");
        Show_Move_Amount.setText("0");

        // ABOUT TIME PLAY
        Custom_Time_Play();
        Count_Time_Play.start();

        // ABOUT PHOTO TO PUZZLE
        main_slide = get_main_slide;
        Level_Play = GET_Level_Play;

        // CROP NET PHOTO
        equalPart = new CropImage_EqualPart(context, main_slide, Level_Play);
        bt_image_slide = equalPart.bs;

        // ADD NEW SLIDE BOARD
        Draw_slidePuzzle = new DrawSlidePuzzle(main_layout_slide, context, bt_image_slide, Level_Play);

        // RETURN BUTTON STOP TO FIGHT AVD
        bt_stop_game.setTag(1);
        bt_stop_game.setImageResource(android.R.drawable.ic_media_pause);

        // RESTART BACKGROUND MUSIC
        if (BackgroundMusicEnable)
        {
            Custom_Background_Music(context);
            Background_Music.start();
        }

    }


    public static void Resume_Game()
    {

        if (Count_Time_Play == null)
        {

            // RETURN BUTTON STOP TO FIGHT AVD
            bt_stop_game.setImageResource(android.R.drawable.ic_media_pause);
            bt_stop_game.setTag(1);

            // RESUME TIME PLAY
            Custom_Time_Play();
            Count_Time_Play.start();

            // UNLOCK SLIDE BOARD
            Draw_slidePuzzle.Unlock_Board_Play();


            // RESUME BACKGROUND MUSIC
            if(Background_Music != null)
            {
                Background_Music.start();
            }

        }

    }



    public final static int SELECT_PHOTO = 100;
    public static int Level_Play_For_From_Device = 0;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (Level_Play_For_From_Device != 0)
        {
            if (requestCode == SELECT_PHOTO)
            {

                if (resultCode == RESULT_OK)
                {

                    Uri select_image = data.getData();
                    String[] Path_Data = {MediaStore.Images.Media.DATA};

                    Cursor c = getContentResolver().query(select_image, Path_Data, null, null, null);
                    c.moveToFirst();

                    int pos = c.getColumnIndex(Path_Data[0]);
                    String Image_Path = c.getString(pos);

                    c.close();

                    Stop_Game(MainActivity.this, 0);

                    if (StartGameEnable == false)
                    {
                        Fist_Play(MainActivity.this   , BitmapFactory.decodeFile(Image_Path), Level_Play_For_From_Device);
                    }else
                    {
                        Restart_Game(MainActivity.this, BitmapFactory.decodeFile(Image_Path), Level_Play_For_From_Device);
                    }


                }else
                {

                    /**if (StartGameEnable)
                    {

                        Resume_Game();

                    }else
                    {

                        Return_Fist_Play(MainActivity.this);

                    } */

                }

            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    // Đưa game về trạng thái dừng ( tất cả đều dừng )
    public static int Return_Fist_Play(Context context)
    {


        main_layout_slide.removeAllViews();

        Draw_slidePuzzle = new DrawSlidePuzzle(main_layout_slide, context, new CropImage_EqualPart(context, main_slide, 6).bs, 6);

        MainAcitivtyLayout.addView(IMG_StartGame_F);
        StartGameEnable = false;

        bt_stop_game.setTag(0);
        bt_stop_game.setImageResource(android.R.drawable.ic_media_play);

        return 0;

    }

    // Những hành động diễn ra khi người dùng chiến thắng màn chơi
    public static void Action_When_Win(Context context)
    {

        // Do something when player wined
        Stop_Game(context, 0);

        // Dialog happy player
        Dialog Win_Dialog = Dialog_When_Win(context);

        Win_Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Win_Dialog.setCancelable(false);

        Win_Dialog.show();

    }


    // Set thuộc tính cho Dialog sẽ hiện ra khi người dùng chiến thắng màn chơi
    public static Dialog Dialog_When_Win(final Context context)
    {

        AlertDialog.Builder mDialog = new AlertDialog.Builder(context, R.style.AlertDialogCustomTheme);

        mDialog.setTitle("Happy to you :) ");
        mDialog.setIcon(R.mipmap.happy_icon);
        mDialog.setMessage("Chúc mừng bạn đã chiến thắng !, bạn có muốn chơi lại không ?");

        mDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                new Dialog_Start_Game(context, 2);

            }
        });

        mDialog.setNegativeButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

                Return_Fist_Play(context);

            }
        });

        return mDialog.create();

    }


    // Start Game khi client nhấn vào nút Play ở giữa màn hình
    public void Start_Game(View view)
    {

        if (StartGameEnable == false)
        {
            MainAcitivtyLayout.removeView(IMG_StartGame_F);

            new Dialog_Start_Game(MainActivity.this, 1);

        }

    }

    // Random các slides khi client click ImagView Random
    public void Action_Random_Slides(View view)
    {

        if((int) bt_stop_game.getTag() == 1)
        {

            if (StartGameEnable)
            {

                Draw_slidePuzzle.Random_Position_SG_Slide();

            }

        }

    }


    // Action when client click Sound Button
    public static boolean BackgroundMusicEnable = true;
    public void Action_On_Of_Background_Music(View view)
    {

        ImageView A_V_G_Sound = (ImageView) view;

        if (StartGameEnable)
        {

            if (Background_Music != null)
            {

                view.setTag(Background_Music.getCurrentPosition());
                Background_Music.stop();
                Background_Music = null;

                A_V_G_Sound.setImageResource(R.mipmap.volume_on_icon);

                BackgroundMusicEnable = false;

            }else
            {

                Custom_Background_Music(MainActivity.this);

                // GO TO OLD LOCATION
                Background_Music.seekTo((int) view.getTag());
                Background_Music.start();

                A_V_G_Sound.setImageResource(R.mipmap.volume_off_icon);

                BackgroundMusicEnable = true;

            }

        }

    }

    public static RelativeLayout Comprise_Main_Layout_Slide = null;
    public static ImageView Main_Slide_Show = null;
    public static ImageView CV_View_ImageView_Actiong;
    public void Show_Main_Slide(View v)
    {

        Comprise_Main_Layout_Slide = (RelativeLayout) MainActivity.this.findViewById(R.id.Location_main_layout_slide);
        CV_View_ImageView_Actiong  = (ImageView)      v;

        if (StartGameEnable)
        {

            if (Main_Slide_Show == null)
            {

                Main_Slide_Show = new ImageView(MainActivity.this);

                ImageView Example_Slide_SG = (ImageView) Get_Example_View.findViewById(R.id.button_eg_sg_slide);

                int Width  = Example_Slide_SG.getLayoutParams().width  * 3;
                int Height = Example_Slide_SG.getLayoutParams().height * 3;

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(Width, Height);
                Main_Slide_Show.setLayoutParams(params);
                Main_Slide_Show.setScaleType(ImageView.ScaleType.FIT_XY);

                Comprise_Main_Layout_Slide.addView(Main_Slide_Show);

                CV_View_ImageView_Actiong.setTag(1);
                CV_View_ImageView_Actiong.setImageResource(R.mipmap.hide);
                Draw_slidePuzzle.Lock_Play_Board();

                Main_Slide_Show.setImageBitmap(main_slide);
            }else
            {

                Main_Slide_Show.setImageBitmap(main_slide);

                if ((int) CV_View_ImageView_Actiong.getTag() == 1)
                {

                    Comprise_Main_Layout_Slide.removeView(Main_Slide_Show);

                    CV_View_ImageView_Actiong.setTag(0);
                    CV_View_ImageView_Actiong.setImageResource(R.mipmap.eye);
                    Draw_slidePuzzle.Unlock_Board_Play();

                }else
                {

                    Comprise_Main_Layout_Slide.addView(Main_Slide_Show);

                    CV_View_ImageView_Actiong.setTag(1);
                    CV_View_ImageView_Actiong.setImageResource(R.mipmap.hide);
                    Draw_slidePuzzle.Lock_Play_Board();

                }

            }

        }

    }





}
