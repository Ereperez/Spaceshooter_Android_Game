package com.ereperez.spaceshooter;
/**
 * Created by Edwin Perez
 */
public class Enemy extends BitMapEntity {
    private static final int ENEMY_HEIGHT = GameSettings.ENEMY_HEIGHT;

    Enemy(){
        super();
        int resID = R.drawable.ship1;
        switch (_game.rng.nextInt(5)){
            case 0:
                resID = R.drawable.ship1;
                break;
            case 1:
                resID = R.drawable.ship2;
                break;
            case 2:
                resID = R.drawable.ship3;
                break;
            case 3:
                resID = R.drawable.ship5;
                break;
            case 4:
                resID = R.drawable.ship6;
                break;
        }
        loadBitMap(resID, ENEMY_HEIGHT);
        bitmap = Utils.flipBitmap(bitmap, false);
        respawn();
    }

    @Override
    void respawn() {
        enemyXRespawn();
        _y = _game.rng.nextInt(STAGE_HEIGHT-ENEMY_HEIGHT);
    }

    @Override
    void update() {
        enemyMovementUpdate();
        if (right() <0){
            _x = STAGE_WIDTH + _game.rng.nextInt(ENEMY_SPAWN_OFFSET);
        }
    }
}
