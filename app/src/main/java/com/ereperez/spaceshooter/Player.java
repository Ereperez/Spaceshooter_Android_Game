package com.ereperez.spaceshooter;

import android.graphics.Bitmap;
/**
 * Created by Edwin Perez
 */
public class Player extends BitMapEntity {
    static final String TAG = "Player";
    private static final int PLAYER_HEIGHT = GameSettings.PLAYER_HEIGHT;
    private static final int STARTING_POSITION = GameSettings.STARTING_POSITION;
    private static final int STARTING_HEALTH = GameSettings.STARTING_HEALTH;
    private static final float ACC = GameSettings.ACC;
    private static final float MIN_VEL = GameSettings.MIN_VEL;
    private static final float MAX_VEL = GameSettings.MAX_VEL;
    private static final float GRAVITY = GameSettings.GRAVITY;
    private static final float LIFT = GameSettings.LIFT;
    private static final float DRAG = GameSettings.DRAG;
    private static final int COLOR = GameSettings.PLAYER_BLINK_COLOR;
    private static final int IMMUNITY_TIME = GameSettings.PLAYER_IMMUNITY_TIME;

    int health = 0;
    int updateCount = 0;
    boolean immunity = false;
    final Bitmap tempBit;

    Player() {
        super();
        loadBitMap(R.drawable.player_ship, PLAYER_HEIGHT);
        tempBit = bitmap;
        respawn();
    }

    @Override
    void respawn() {
        _x = STARTING_POSITION;
        health = STARTING_HEALTH;
        _velX = 0f;
        _velY = 0f;
        bitmap = tempBit;
        immunity = false;
    }

    @Override
    void update() {
        _velX *= DRAG;
        _velY += GRAVITY;
        if (_game.isBoosting){
            _velX *= ACC;
            _velY += LIFT;
        }
        _velX = Utils.clamp(_velX, MIN_VEL, MAX_VEL);
        _velY = Utils.clamp(_velY, -MAX_VEL, MAX_VEL);
        _y += _velY;
        _y = Utils.clamp(_y, 0, STAGE_HEIGHT-_height);
        _game.playerSpeed = _velX;
        updateCount++;
        checkImmunity();
    }

    @Override
    void onCollision(Entity that) {
        if (updateCount > IMMUNITY_TIME){
            updateCount = 0;
            immunity = true;
            health--;
        }
    }

    private void checkImmunity(){
        if (immunity && updateCount > 0){
            if (updateCount %2 == 0 && updateCount < IMMUNITY_TIME) {
                bitmap = Utils.colorBitmap(bitmap, COLOR);
            }
            else {
                bitmap = tempBit;
            }
            if (updateCount >= IMMUNITY_TIME){
                immunity = false;
            }
        }
    }
}
