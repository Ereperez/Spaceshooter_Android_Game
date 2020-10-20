package com.ereperez.spaceshooter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Locale;
/**
 * Created by Edwin Perez
 */
public class GameHUD {
    private static final String DIFFICULTY = GameSettings.DIFFICULTY_HUD;
    private static final String HEALTH = GameSettings.HEALTH_HUD;
    private static final String DISTANCE = GameSettings.DISTANCE_HUD;
    private static final String GAME_OVER = GameSettings.GAME_OVER_HUD;
    private static final String GAME_OVER_RESTART = GameSettings.GAME_OVER_RESTART_HUD;
    private static final String START_SCREEN = GameSettings.START_SCREEN_HUD;
    private static final int STAGE_HEIGHT = GameSettings.STAGE_HEIGHT;
    private static final float HUD_TEXT_SIZE = GameSettings.HUD_TEXT_SIZE;
    private static final float FULLSCREEN_TEXT_SIZE = GameSettings.FULLSCREEN_TEXT_SIZE;
    private static final int HUD_TEXT_OFFSET = GameSettings.HUD_TEXT_OFFSET;

    final float centerY = (float) STAGE_HEIGHT/2;
    final float centerX = (centerY)+300;
    private boolean gameOver = false;
    private int health = 0;
    private int distanceTraveled = 0;
    private String gameDifficulty = null;
    private Canvas canvas = null;
    private Paint paint = null;

    public void renderHUD(final boolean mGameOver, final int mHealth, final int mDistance, final String mGameDif, final Canvas mCanvas, final Paint mPaint){
        gameOver = mGameOver;
        health = mHealth;
        distanceTraveled = mDistance;
        gameDifficulty = mGameDif;
        canvas = mCanvas;
        paint = mPaint;
        if (gameOver && distanceTraveled == 0){
            startScreen();
        }else{
            gameHUD();
        }
    }

    private void gameHUD(){
        paint.setColor(Color.WHITE);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(HUD_TEXT_SIZE);
        if (!gameOver){
            canvas.drawText(String.format(Locale.getDefault(),"%s%s", DIFFICULTY, gameDifficulty), HUD_TEXT_OFFSET, HUD_TEXT_SIZE, paint);
            canvas.drawText(String.format(Locale.getDefault(), "%s%d", HEALTH, health), HUD_TEXT_OFFSET, HUD_TEXT_SIZE*2, paint);
            canvas.drawText(String.format(Locale.getDefault(),"%s%d", DISTANCE, distanceTraveled), HUD_TEXT_OFFSET, HUD_TEXT_SIZE*3, paint);
        }else {
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize(FULLSCREEN_TEXT_SIZE);
            canvas.drawText(GAME_OVER, centerX, centerY, paint);
            canvas.drawText(GAME_OVER_RESTART, centerX, centerY + FULLSCREEN_TEXT_SIZE, paint);
        }
    }

    private void startScreen(){
        paint.setColor(Color.WHITE);
        paint.setTextSize(FULLSCREEN_TEXT_SIZE);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawColor(Color.BLACK);
        canvas.drawText(START_SCREEN, centerX, centerY, paint);
    }
}
