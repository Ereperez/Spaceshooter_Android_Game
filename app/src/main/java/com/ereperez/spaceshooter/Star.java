package com.ereperez.spaceshooter;

import android.graphics.Canvas;
import android.graphics.Paint;
/**
 * Created by Edwin Perez
 */
public class Star extends Entity {
    private static final int STAR_COLOR1 = GameSettings.STAR_COLOR1;
    private static final int STAR_COLOR2 = GameSettings.STAR_COLOR2;
    private static final int STAR_COLOR3 = GameSettings.STAR_COLOR3;
    private static final int STAR_COLOR4 = GameSettings.STAR_COLOR4;
    private static final float STAR_SPEED = GameSettings.STAR_SPEED;
    private static final int STAR_MAX_RADIUS = GameSettings.STAR_MAX_RADIUS;
    private static final int STAR_MIN_RADIUS = GameSettings.STAR_MIN_RADIUS;
    private int color = 0;
    private final float radius;

    Star() {
        _x = _game.rng.nextInt(STAGE_WIDTH);
        _y = _game.rng.nextInt(STAGE_HEIGHT);
        radius = _game.rng.nextInt(STAR_MAX_RADIUS) + STAR_MIN_RADIUS;
        _width = radius *2;
        _height = radius *2;
        switch (_game.rng.nextInt(4)){
            case 0:
                color = STAR_COLOR1;
                break;
            case 1:
                color = STAR_COLOR2;
                break;
            case 2:
                color = STAR_COLOR3;
                break;
            case 3:
                color = STAR_COLOR4;
                break;
        }
    }

    @Override
    void update() {
        _x -= STAR_SPEED;
        _x = Utils.wrap(_x, 0, STAGE_WIDTH + _width);
    }

    @Override
    void render(Canvas canvas, Paint paint) {
        paint.setColor(color);
        canvas.drawCircle(_x+ radius, _y+ radius, radius, paint);
    }
}
