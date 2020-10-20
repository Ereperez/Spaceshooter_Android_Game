package com.ereperez.spaceshooter;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.os.Bundle;
/**
 * Created by Edwin Perez
 */
public class GameActivity extends AppCompatActivity {
    Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String gameDifficulty = getIntent().getStringExtra("GAME_DIFFICULTY");
        game = new Game(this);
        game.checkDifficulty(gameDifficulty);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        setContentView(game);
    }

    @Override
    protected void onResume(){
        super.onResume();
        game.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
        game.onPause();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        game.onDestroy();
    }
}