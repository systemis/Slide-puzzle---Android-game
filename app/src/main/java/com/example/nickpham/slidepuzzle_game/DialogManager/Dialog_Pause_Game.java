package com.example.nickpham.slidepuzzle_game.DialogManager;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.IntRange;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.nickpham.slidepuzzle_game.GAS.SlidePuzzleIntroduction;
import com.example.nickpham.slidepuzzle_game.MainActivity;
import com.example.nickpham.slidepuzzle_game.R;

/**
 * Created by nickpham on 30/11/2016.
 */

public class Dialog_Pause_Game
{

    Context context;

    Dialog dl_pause_game, dl_about_game;

    AppCompatActivity content_activity;

    public Dialog_Pause_Game(Context context)
    {


        this.context = context;
        this.content_activity = (AppCompatActivity) this.context;

        // Create dialog Remind
        CreateDialog();

        dl_about_game = Dialog_Introduction_Game();
        dl_about_game.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dl_about_game.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dl_about_game.setCancelable(false);

        // Show Dialog
        dl_pause_game.show();

    }

    public void CreateDialog()
    {

        dl_pause_game = new Dialog(context);
        dl_pause_game.setContentView(R.layout.layout_dialog_pause_game);
        dl_pause_game.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dl_pause_game.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dl_pause_game.setCancelable(false);

        CreateUI();

    }


    public void CreateUI()
    {

        Button bt_newgame   = (Button) dl_pause_game.findViewById(R.id.button_g_newgame);
        Button bt_done      = (Button) dl_pause_game.findViewById(R.id.button_g_done);
        Button bt_aboutgame = (Button) dl_pause_game.findViewById(R.id.button_g_aboutgame);


        View.OnClickListener View_Action_DialogThis = new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                switch (view.getId())
                {

                    case R.id.button_g_newgame:

                        // Go to custom to restart Game
                        Dialog_Start_Game dialog_start_game = new Dialog_Start_Game(context, 2);

                        break;

                    case R.id.button_g_aboutgame:

                        dl_about_game.show();

                        break;

                    case R.id.button_g_done:

                        dl_pause_game.dismiss();

                        MainActivity.Resume_Game();

                        break;
                }

                dl_pause_game.dismiss();
            }
        };

        bt_newgame     .setOnClickListener(View_Action_DialogThis);
        bt_done        .setOnClickListener(View_Action_DialogThis);
        bt_aboutgame   .setOnClickListener(View_Action_DialogThis);

    }



    public Dialog Dialog_Introduction_Game()
    {

        AlertDialog.Builder mDialog = new AlertDialog.Builder(context, R.style.AlertDialogCustomTheme);

        mDialog.setTitle("Introduction about App ");

        mDialog.setMessage(new SlidePuzzleIntroduction().Get_INTRODUCTION());

        mDialog.setNegativeButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {

                dialogInterface.dismiss();

            }
        });


        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface)
            {

                MainActivity.Resume_Game();

            }
        });


        return mDialog.create();
    }


}
