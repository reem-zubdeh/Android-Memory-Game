package com.reem.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    int[] order = new int[16];
    int[] currentlyTurned = {16,16};
    boolean turn1 = true;
    boolean[] clickable = new boolean[16];
    int moves = 0;
    int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView congratsPanel = (ImageView) findViewById(R.id.congratspanel);
        congratsPanel.setTranslationY(-2000);
        TextView congratsTop = (TextView) findViewById(R.id.congratstop);
        congratsTop.setTranslationY(-2000);
        TextView congratsBottom = (TextView) findViewById(R.id.congratsbottom);
        congratsBottom.setTranslationY(-2000);
        Button playAgain = (Button) findViewById(R.id.playagain);
        playAgain.setTranslationY(-2000);
        order = shuffle();
        for (int i = 0; i < 16; i++){
            clickable[i] = true;
        }
    }

    public void newGame(View view) {
        ImageView congratsPanel = (ImageView) findViewById(R.id.congratspanel);
        congratsPanel.animate().translationYBy(-2000).setDuration(500);
        TextView congratsTop = (TextView) findViewById(R.id.congratstop);
        congratsTop.animate().translationYBy(-2000).setDuration(500);
        TextView congratsBottom = (TextView) findViewById(R.id.congratsbottom);
        congratsBottom.animate().translationYBy(-2000).setDuration(500);
        Button playAgain = (Button) findViewById(R.id.playagain);
        playAgain.animate().translationYBy(-2000).setDuration(500);
        order = shuffle();
        for (int i = 0; i < 16; i++){
            turnBack(i);
        }
        moves = 0;
        score = 0;
        TextView scoreView = (TextView) findViewById(R.id.score);
        scoreView.setText("Score: " + score + "/8");
        TextView movesView = (TextView) findViewById(R.id.moves);
        movesView.setText("Moves: " + moves);
    }

    public int[] shuffle() {
        int[] results = new int[16];
        Random rand = new Random();
        for (int i = 0; i < 16; i++) {
            results[i] = 16;
        }
        for (int i = 0; i < 16; i++) {
            int num = 0;
            do {
                num = rand.nextInt(16);
            } while (contains(results, num));
            results[i] = num;
        }

        for (int i = 0; i < 16; i++) {
            if (results[i] >= 8) results[i] -= 8;
        }

        return results;
    }

    public boolean contains(int[] array, int val) {
        for (int i : array) {
            if (i == val) return true;
        }
        return false;
    }

    public void turnFront(View view) {

        ImageView current = (ImageView) view;
        int cardNo = Integer.parseInt(current.getTag().toString());

        if (clickable[cardNo]) {

            clickable[cardNo] = false;

            current.animate().rotationY(90).setDuration(300);
            new CountDownTimer(300,100){

                @Override
                public void onTick(long l) {}

                @Override
                public void onFinish() {
                    String filename = "img" + order[cardNo];
                    current.setImageResource(getResources().getIdentifier(filename, "drawable", getPackageName()));
                    current.animate().rotationY(180).setDuration(300);
                }
            }.start();


            if (turn1) {
                currentlyTurned[0] = cardNo;
                turn1 = false;
            }

            else {
                moves++;
                TextView movesView = (TextView) findViewById(R.id.moves);
                movesView.setText("Moves: " + moves);
                currentlyTurned[1] = cardNo;
                turn1 = true;
                if (order[currentlyTurned[0]] == order[currentlyTurned[1]]) {
                    Toast.makeText(this, "It's a match!", Toast.LENGTH_SHORT).show();
                    score++;
                    TextView scoreView = (TextView) findViewById(R.id.score);
                    scoreView.setText("Score: " + score + "/8");

                    if (score == 8) {
                        ImageView congratsPanel = (ImageView) findViewById(R.id.congratspanel);
                        congratsPanel.animate().translationYBy(2000).setStartDelay(700).setDuration(500);
                        TextView congratsTop = (TextView) findViewById(R.id.congratstop);
                        congratsTop.animate().translationYBy(2000).setStartDelay(700).setDuration(500);
                        TextView congratsBottom = (TextView) findViewById(R.id.congratsbottom);
                        congratsBottom.setText(moves + " moves");
                        congratsBottom.animate().translationYBy(2000).setStartDelay(700).setDuration(500);
                        Button playAgain = (Button) findViewById(R.id.playagain);
                        playAgain.animate().translationYBy(2000).setStartDelay(700).setDuration(500);
                    }

                }
                else {
                    turnBack(currentlyTurned[0]);
                    turnBack(currentlyTurned[1]);
                    currentlyTurned[0] = 16;
                    currentlyTurned[1] = 16;
                }
            }

        }

    }

    public void turnBack(int cardNo) {

        clickable[cardNo] = true;

        new CountDownTimer(800, 100) {

            @Override
            public void onTick(long l) {}

            @Override
            public void onFinish() {
                int cardId = getResources().getIdentifier("card"+cardNo, "id", getPackageName());
                ImageView current = (ImageView) findViewById(cardId);
                current.animate().rotationY(90).setDuration(300);
                new CountDownTimer(300,100){

                    @Override
                    public void onTick(long l) {}

                    @Override
                    public void onFinish() {
                        String filename = "img" + order[cardNo];
                        current.setImageResource(R.drawable.card);
                        current.animate().rotationY(0).setDuration(300);
                    }
                }.start();
            }
        }.start();
    }
}