package com.ereperez.spaceshooter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
/**
 * Created by Edwin Perez
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static String gameDifficulty;
    static GameSettings gameSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameDifficulty = getString(R.string.difficulty_normal);//normal is pre-checked and default unless user changes it
        loadHighScore();
        gameSettings = new GameSettings(this);

        final Button startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(this);
    }

    private void loadHighScore(){
        final String EASY_SCORE = getString(R.string.easy_highscorelist);
        final String NORMAL_SCORE = getString(R.string.normal_highscorelist);
        final String HARD_SCORE = getString(R.string.hard_highscorelist);

        final TextView highScoreEasy = findViewById(R.id.highscore_easy_tv);
        SharedPreferences prefs = getSharedPreferences(Game.PREFS, Context.MODE_PRIVATE);
        int longestDistEasy = prefs.getInt(Game.LONGEST_DIST_EASY, 0);
        highScoreEasy.setText(String.format(EASY_SCORE, longestDistEasy));

        final TextView highScoreNormal = findViewById(R.id.highscore_normal_tv);
        int longestDistNorm = prefs.getInt(Game.LONGEST_DIST_NORMAL, 0);
        highScoreNormal.setText(String.format(NORMAL_SCORE, longestDistNorm));

        final TextView highScoreHard = findViewById(R.id.highscore_hard_tv);
        int longestDistHard = prefs.getInt(Game.LONGEST_DIST_HARD, 0);
        highScoreHard.setText(String.format(HARD_SCORE, longestDistHard));
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.rb_easy:
                if(checked)
                    gameDifficulty = getString(R.string.difficulty_easy);
                    Toast.makeText(getApplicationContext(), R.string.game_set_easy, Toast.LENGTH_SHORT).show();
                break;
            case R.id.rb_normal:
                if(checked)
                    gameDifficulty = getString(R.string.difficulty_normal);
                    Toast.makeText(getApplicationContext(), R.string.game_set_normal, Toast.LENGTH_SHORT).show();
                break;
            case R.id.rb_hard:
                if(checked)
                    gameDifficulty = getString(R.string.difficulty_hard);
                    Toast.makeText(getApplicationContext(), R.string.game_set_hard, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void onClick(View view) {
        final Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("GAME_DIFFICULTY", gameDifficulty);
        startActivity(intent);
        finish();
    }
}