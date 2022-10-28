package com.example.quiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button[][] buttons;
    Button[] right;

    LottieAnimationView law, lawEnd;

    int i, j;

    int numAnswered, numCorrect;

    Button reset;

    IncomingCall_Receiver incomingCall_receiver;
    IntentFilter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttons = new Button[3][3];
        right = new Button[3];

        buttons[0][0]=(Button)findViewById(R.id.b1_1);
        buttons[0][1]=(Button)findViewById(R.id.b1_2);
        buttons[0][2]=(Button)findViewById(R.id.b1_3);
        buttons[1][0]=(Button)findViewById(R.id.b2_1);
        buttons[1][1]=(Button)findViewById(R.id.b2_2);
        buttons[1][2]=(Button)findViewById(R.id.b2_3);
        buttons[2][0]=(Button)findViewById(R.id.b3_1);
        buttons[2][1]=(Button)findViewById(R.id.b3_2);
        buttons[2][2]=(Button)findViewById(R.id.b3_3);

        right[0]=buttons[0][1]; right[1]=buttons[1][0]; right[2]=buttons[2][2];

        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                buttons[i][j].setOnClickListener(this);
            }
        }

        law = (LottieAnimationView) findViewById(R.id.animation);
        lawEnd = (LottieAnimationView) findViewById(R.id.animationEnd);

        reset = (Button) findViewById(R.id.reset);
        reset.setOnClickListener(this);

        numCorrect = 0;

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);

        IncomingCall_Receiver incomingCall_receiver = new IncomingCall_Receiver();
        IntentFilter filter = new IntentFilter("android.intent.action.PHONE_STATE");
    }

    @Override
    public void onClick(View v){
        numAnswered ++;
        if (v==right[0] || v==right[1] || v==right[2]) {
            numCorrect++;
            i = getRow(buttons, v);
            for (j = 0; j < buttons[i].length; j++) {
                if (buttons[i][j] != v)
                    buttons[i][j].setAlpha(0.5f);
                else
                    buttons[i][j].setBackgroundColor(getResources().getColor(R.color.teal_700));
                buttons[i][j].setClickable(false);
            }
            law.setAnimation(R.raw.correct_gif);
            law.playAnimation();
        }
        else if (v != reset){
            i = getRow(buttons, v);
            for (j = 0; j < buttons[i].length; j++) {
                if (buttons[i][j] != v)
                    buttons[i][j].setAlpha(0.5f);
                else
                    buttons[i][j].setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
                buttons[i][j].setClickable(false);
            }
            law.setAnimation(R.raw.incorrect_gif);
            law.playAnimation();
        }

        if (numAnswered == 3) {
            if (numCorrect == 3)
                lawEnd.setAnimation(R.raw.victory_gif);
            else
                lawEnd.setAnimation(R.raw.defeat_gif);
            lawEnd.playAnimation();
            reset.setVisibility(View.VISIBLE);
        }

        if (v == reset) {
            for (int i = 0; i < buttons.length; i++) {
                for (int j = 0; j < buttons[i].length; j++) {
                    buttons[i][j].setAlpha(1f);
                    buttons[i][j].setBackgroundColor(getResources().getColor(R.color.purple_200));
                    buttons[i][j].setClickable(true);
                }
            }
            numAnswered = 0;
            numCorrect = 0;
            law.pauseAnimation();
            law.setImageDrawable(null);
            lawEnd.pauseAnimation();
            lawEnd.setImageDrawable(null);
            reset.setVisibility(View.GONE);
        }
    }

    public <T> int getRow(T[][] array ,T item) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j <array[i].length; j++) {
                if (array[i][j].equals(item))
                    return i;
            }
        }
        return -1;
    }

    public void makeNotClickable(Button btn) {
        btn.setAlpha(0.5f);
        btn.setClickable(false);
    }

    @Override
    public void onStart() {
        super.onStart();
        registerReceiver(incomingCall_receiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(incomingCall_receiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(incomingCall_receiver, filter);
    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterReceiver(incomingCall_receiver);
    }
}