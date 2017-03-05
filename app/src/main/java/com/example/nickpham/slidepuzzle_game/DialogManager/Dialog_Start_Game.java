package com.example.nickpham.slidepuzzle_game.DialogManager;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.nickpham.slidepuzzle_game.GAS.MainSlideImageApp;
import com.example.nickpham.slidepuzzle_game.MainActivity;
import com.example.nickpham.slidepuzzle_game.R;


/**
 * Created by nickpham on 02/12/2016.
 */

public class Dialog_Start_Game
{


    /**
     *
     * if Mode start == 1 Start new Game
     * if Mode start == 2 Restart Game
     *
     */

    Context context;
    int  Mode_Start;

    AppCompatActivity Content_Activity;

    public Dialog THIS_Dialog_Start_Game;
    public Dialog_Start_Game(Context context, int Mode_Start)
    {

        this.context = context;
        this.Mode_Start = Mode_Start;

        this.Content_Activity = (AppCompatActivity) this.context;

        Create_Dialog_THIS_Start_Game();

        Choose_Level_Play().show();

    }


    public static int Level_Play;
    public Dialog Choose_Level_Play()
    {

        String[] item_name = { "Dễ (3x3)", "Trung bình (4x4)", "Khó (5x5)", "Cực khó (6x6) " };

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustomTheme);
        builder.setTitle("Lựa chọn độ chơi ");

        builder.setItems(item_name, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {

                Level_Play = i + 3;

                THIS_Dialog_Start_Game.show();

                Toast.makeText(context, "Level to play: " + String.valueOf(Level_Play) , Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

                if (Mode_Start == 1)
                {

                    MainActivity.Return_Fist_Play(context);

                }else
                {


                    MainActivity.Resume_Game();

                }


            }
        });

        Dialog level_dialog = builder.create();
        level_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        level_dialog.setCancelable(false);

        return level_dialog;

    }

    public void Create_Dialog_THIS_Start_Game()
    {

        this.THIS_Dialog_Start_Game = new Dialog(context, R.style.AlertDialogCustomTheme);

        THIS_Dialog_Start_Game.setTitle(R.string.dialog_start_title);
        THIS_Dialog_Start_Game.setContentView(R.layout.layout_dialog_start_game);

        THIS_Dialog_Start_Game.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        THIS_Dialog_Start_Game.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        CreateUI_Dialog_THIS_Start_Game();

        THIS_Dialog_Start_Game.setCancelable(false);
    }


    LinearLayout Show_Main_Slides;
    public void CreateUI_Dialog_THIS_Start_Game()
    {

        final Button btn_choose_main_slide_app = (Button) THIS_Dialog_Start_Game.findViewById(R.id.btn_choose_slide_app);
        Button btn_choose_main_slide_device = (Button) THIS_Dialog_Start_Game.findViewById(R.id.btn_choose_slide_device);
        Button btn_choose_main_slide_done = (Button) THIS_Dialog_Start_Game.findViewById(R.id.btn_choose_slide_done);

        Show_Main_Slides = (LinearLayout) THIS_Dialog_Start_Game.findViewById(R.id.show_mainslides_app);

        btn_choose_main_slide_app.setTag("Closing");

        View.OnClickListener listener_THIS_Dialog_Start_Game_2 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (view.getId())
                {

                    case R.id.btn_choose_slide_app:

                        // Show main slides from app
                        if (btn_choose_main_slide_app.getTag().toString().equals("Closing")) {

                            SHOW_SLIDES();

                            btn_choose_main_slide_app.setTag("Opening");

                        } else {

                            Show_Main_Slides.removeAllViews();

                            btn_choose_main_slide_app.setTag("Closing");

                        }
                        break;

                    case R.id.btn_choose_slide_device:

                        // Show main slides from device
                        Check_Permission_Pick_Image(context);

                        break;

                    case R.id.btn_choose_slide_done:

                        // Exit dialog
                        THIS_Dialog_Start_Game.dismiss();

                        if (Mode_Start == 1)
                        {

                            MainActivity.Return_Fist_Play(context);

                        }else
                        {

                            MainActivity.Resume_Game();

                        }

                        break;

                }

                if (view.getId() != R.id.btn_choose_slide_app && btn_choose_main_slide_app.getTag().toString().equals("Closing") == false)
                {
                    Show_Main_Slides.removeAllViews();
                    btn_choose_main_slide_app.setTag("Closing");
                }

            }
        };


        btn_choose_main_slide_app.setOnClickListener(listener_THIS_Dialog_Start_Game_2);
        btn_choose_main_slide_device.setOnClickListener(listener_THIS_Dialog_Start_Game_2);
        btn_choose_main_slide_done.setOnClickListener(listener_THIS_Dialog_Start_Game_2);

    }


    View.OnClickListener Action_Choose_Main_Slide_App;
    public void Custom_Action_Choose_Main_Slide_App()
    {


        Action_Choose_Main_Slide_App = new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                Bitmap Selected_Main_Slide = BitmapFactory.decodeResource(context.getResources() ,Main_Slide_Path[(int) view.getTag()]);

                if (Mode_Start == 2)
                {
                    MainActivity.Stop_Game(context, 0);
                    MainActivity.Restart_Game(context, Selected_Main_Slide, Level_Play);

                }else if (Mode_Start == 1)
                {
                    MainActivity.Fist_Play(context, Selected_Main_Slide, Level_Play);
                }

                THIS_Dialog_Start_Game.dismiss();
            }
        };




    }

    int[] Main_Slide_Path = new MainSlideImageApp().getMain_Slide_Path();
    public void SHOW_SLIDES()
    {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View layout_example = inflater.inflate(R.layout.style_sg_slide, null, false);
        ViewGroup.LayoutParams params = ((ImageView) layout_example.findViewById(R.id.button_eg_sg_slide)).getLayoutParams();

        Custom_Action_Choose_Main_Slide_App();

        for (int i = 0; i < Main_Slide_Path.length; i ++)
        {

            ImageView SG_IMV_Main_Slide = new ImageView(context);

            SG_IMV_Main_Slide.setLayoutParams(params);
            SG_IMV_Main_Slide.setImageResource(Main_Slide_Path[i]);
            SG_IMV_Main_Slide.setTag(i);
            SG_IMV_Main_Slide.setOnClickListener(Action_Choose_Main_Slide_App);

            Show_Main_Slides.addView(SG_IMV_Main_Slide);

            SG_IMV_Main_Slide = null;

        }


    }


    public  int Check_Permission_Pick_Image(final Context context)
    {

        AppCompatActivity appCompatActivity = (AppCompatActivity) context;

        int result_check = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result_check == PackageManager.PERMISSION_GRANTED)
        {

            MainActivity.Level_Play_For_From_Device = Level_Play;

            Intent Pick_Image_Intent = new Intent(Intent.ACTION_PICK);
            Pick_Image_Intent.setType("image/*");

            appCompatActivity.startActivityForResult(Pick_Image_Intent, MainActivity.SELECT_PHOTO);

            THIS_Dialog_Start_Game.dismiss();

        }else
        {

            AlertDialog.Builder Remind_On_Permission = new AlertDialog.Builder(context);
            Remind_On_Permission.setMessage("Ứng dụng cần bạn cấp quyền để sử dụng tính năng này, vào Settings -> Apps -> Slide Puzzle -> Permissions để cấp quyền cho ứng dụng ");
            Remind_On_Permission.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {

                    dialogInterface.dismiss();

                }
            });

            Remind_On_Permission.create().show();

        }

        return 0;

    }



}
