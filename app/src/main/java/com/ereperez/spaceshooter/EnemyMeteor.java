package com.ereperez.spaceshooter;

/**
 * Created by Edwin Perez
 */
public class EnemyMeteor extends BitMapEntity{
    private static final int METEOR_HEIGHT = GameSettings.METEOR_HEIGHT;
    private static final float METEOR_SPEED = GameSettings.METEOR_SPEED;
    private static boolean swap = false;

    EnemyMeteor(){
        super();
        loadBitMap(R.drawable.meteor, METEOR_HEIGHT);
        respawn();
    }

    @Override
    void respawn() {
        enemyXRespawn();
        _y = STAGE_HEIGHT;
        swap = false;
    }

    @Override
    void update() {
        enemyMovementUpdate();
        if (right() < 0){
            _x = STAGE_WIDTH + _game.rng.nextInt(ENEMY_SPAWN_OFFSET);
        }
        if (!swap){
            _y -= METEOR_SPEED;
            if (_y == 0){
                swap = true;
            }
        }else {
            _y += METEOR_SPEED;
            if (_y == STAGE_HEIGHT){
                swap = false;
            }
        }
    }
}
