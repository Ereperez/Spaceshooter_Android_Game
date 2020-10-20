package com.ereperez.spaceshooter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
/**
 * Created by Edwin Perez
 */
public class BitMapEntity extends Entity {
    protected Bitmap bitmap = null;

    public BitMapEntity() {}

    protected void loadBitMap(int resID, int height){
        destroy();
        Bitmap tempBit = BitmapFactory.decodeResource(
                _game.getContext().getResources(),
                resID
        );
        //scaling the player ship - https://developer.android.com/topic/performance/graphics/load-bitmap
        bitmap = Utils.scaleToTargetHeight(tempBit, height);
        tempBit.recycle(); //reduces memory use
        _width = bitmap.getWidth();
        _height = bitmap.getHeight();
    }

    @Override
    void render(Canvas canvas, Paint paint) {
        canvas.drawBitmap(bitmap, _x, _y, paint);
    }

    @Override
    void destroy() {
        if (bitmap != null){
            bitmap.recycle();
            bitmap = null;
        }
    }

    @Override
    void onCollision(Entity that) {
        _x = STAGE_WIDTH + _game.rng.nextInt(ENEMY_SPAWN_OFFSET);
    }

    void enemyXRespawn() {
        _x = STAGE_WIDTH + _game.rng.nextInt(ENEMY_SPAWN_OFFSET);
    }

    void enemyMovementUpdate(){
        _velX = -_game.playerSpeed;
        _x += _velX;
    }
}
