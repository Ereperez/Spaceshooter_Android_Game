package com.ereperez.spaceshooter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
/**
 * Created by Edwin Perez
 */
public class Game extends SurfaceView implements Runnable {
    public static final String TAG = "Game";
    public static final String PREFS = "com.ereperez.spaceshooter.Game";
    public static final String LONGEST_DIST_EASY = "longest_distance_easy";
    public static final String LONGEST_DIST_NORMAL = "longest_distance_normal";
    public static final String LONGEST_DIST_HARD = "longest_distance_hard";
    private static final int STAGE_WIDTH = GameSettings.STAGE_WIDTH;
    private static final int STAGE_HEIGHT = GameSettings.STAGE_HEIGHT;
    private static final int STAR_COUNT = GameSettings.STAR_COUNT;
    private static final int ENEMY_COUNT_EASY = GameSettings.ENEMY_COUNT_EASY;
    private static final int ENEMY_COUNT_NORMAL = GameSettings.ENEMY_COUNT_NORMAL;
    private static final int ENEMY_COUNT_HARD = GameSettings.ENEMY_COUNT_HARD;
    private static final int METEOR_COUNT = GameSettings.METEOR_COUNT;
    private static final String EASY = GameSettings.EASY;
    private static final String HARD = GameSettings.HARD;
    private static String gameDifficulty;
    private final GameHUD gameHUD;
    private Thread gameThread = null;
    private volatile boolean isRunning = false;
    private SurfaceHolder holder = null;
    private final Paint paint = new Paint();
    private Canvas canvas = null;

    private final ArrayList<Entity> entities = new ArrayList<>();
    private Player player = null;
    final Random rng  = new Random ();
    private Jukebox jukebox = null;
    private SharedPreferences prefs = null;
    private SharedPreferences.Editor editor = null;

    volatile boolean isBoosting = false;
    float playerSpeed = 0f;
    int distanceTraveled = 0;
    int maxDistanceTraveled = 0;
    boolean gameOver = true;

    public Game(Context context) {
        super(context);
        Entity._game = this;
        holder = getHolder();
        holder.setFixedSize(STAGE_WIDTH, STAGE_HEIGHT);

        jukebox = new Jukebox(context);
        gameHUD = new GameHUD();

        prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        editor = prefs.edit();
        editor.apply();
        player = new Player();
    }

    void checkDifficulty(String gameDif){
        gameDifficulty = gameDif;
        for (int i = 0; i < STAR_COUNT; i++){
            entities.add(new Star());
        }
        if (Objects.equals(gameDifficulty, EASY)){
            for (int i = 0; i < ENEMY_COUNT_EASY; i++){
                entities.add(new Enemy());
            }
        }else if (Objects.equals(gameDifficulty, HARD)){
            for (int i = 0; i < ENEMY_COUNT_HARD; i++){
                entities.add(new Enemy());
            }
            for (int j = 0; j < METEOR_COUNT; j++){
                entities.add(new EnemyMeteor());
            }
        }else{
            for (int i = 0; i < ENEMY_COUNT_NORMAL; i++){
                entities.add(new Enemy());
            }
        }
    }

    private void restart(){
        for(Entity e : entities){
            e.respawn();
        }
        player.respawn();
        distanceTraveled = 0;
        if (Objects.equals(gameDifficulty, EASY)){
            maxDistanceTraveled = prefs.getInt(LONGEST_DIST_EASY, 0);
        }else if (Objects.equals(gameDifficulty, HARD)){
            maxDistanceTraveled = prefs.getInt(LONGEST_DIST_HARD, 0);
        }else{
            maxDistanceTraveled = prefs.getInt(LONGEST_DIST_NORMAL, 0);
        }
        gameOver = false;
        jukebox.play(Jukebox.GAME_START);
    }

    @Override
    public void run() {
        while (isRunning){
            update();
            render();
        }
    }

    private void update(){
        if (gameOver) { return; }
        player.update();
        for(Entity e : entities){
            e.update();
        }
        distanceTraveled += playerSpeed;
        checkCollisions();
        checkGameOver();
    }

    private void checkGameOver() {
        if (player.health < 1){
            gameOver = true;
            if (distanceTraveled > maxDistanceTraveled){
                maxDistanceTraveled = distanceTraveled;
                if (Objects.equals(gameDifficulty, EASY)){
                    editor.putInt(LONGEST_DIST_EASY, maxDistanceTraveled);
                }else if (Objects.equals(gameDifficulty, HARD)){
                    editor.putInt(LONGEST_DIST_HARD, maxDistanceTraveled);
                }else{
                    editor.putInt(LONGEST_DIST_NORMAL, maxDistanceTraveled);
                }
                editor.apply();
            }
            jukebox.play(Jukebox.GAME_OVER);
        }
    }

    private void checkCollisions() {
        Entity temp = null;
        for (int i = STAR_COUNT; i < entities.size(); i++){
            temp = entities.get(i);
            if (player.isColliding(temp)){
                player.onCollision(temp);
                temp.onCollision(player);
                if (distanceTraveled > 0){
                    jukebox.play(Jukebox.CRASH);
                }
            }
        }
    }

    private void render(){
        if (!lockCanvas()) {
            return;
        }
        canvas.drawColor(Color.BLACK); //clear the screen
        for(Entity e : entities){
            e.render(canvas, paint);
        }
        player.render(canvas, paint);
        gameHUD.renderHUD(gameOver, player.health, distanceTraveled, gameDifficulty, canvas, paint);
        holder.unlockCanvasAndPost(canvas);
    }

    private boolean lockCanvas() {
        if(!holder.getSurface().isValid()){
            return false;
        }
        canvas = holder.lockCanvas();
        return (canvas != null);
    }

    protected void onResume(){
        Log.d(TAG, "onResume");
        isRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    protected void onPause(){
        Log.d(TAG, "onPause");
        isRunning = false;
        try {
            gameThread.join();
        }catch (InterruptedException e) {
            Log.d(TAG, Log.getStackTraceString(e.getCause()));
        }
    }

    protected void onDestroy(){
        Log.d(TAG, "onDestroy");
        gameThread = null;
        for(Entity e : entities){
            e.destroy();
        }
        player.destroy();
        player = null;
        jukebox.destroy();
        jukebox = null;
        Entity._game = null;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        switch(event.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_UP: //finger lifted
                isBoosting = false;
                if (gameOver){
                    restart();
                }
                break;
            case MotionEvent.ACTION_DOWN: //finger pressed
                isBoosting = true;
                if (!gameOver){
                    jukebox.play(Jukebox.BOOST);
                }
                break;
        }
        return true;
    }
}
