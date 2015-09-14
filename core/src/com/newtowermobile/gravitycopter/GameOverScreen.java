package com.newtowermobile.gravitycopter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Created by absolute on 14.09.2015.
 */
public class GameOverScreen extends ScreenAdapter {
    private static float GRAVITY_FINAL = 15;
    public static final float PLANE_START_Y = 240;
    public static final float PLANE_START_X = 50;
    private static final float PLANE_VELOCITY_X = 200;

    GravityCopter game;

    OrthographicCamera camera;
    OrthographicCamera uiCamera;

    float groundOffsetX = 0;

    float planeStateTime = 0;
    int score = 0;

    public GameOverScreen(GravityCopter game, int score) {
        this.game = game;
        this.score = score;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        uiCamera = new OrthographicCamera();
        uiCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        uiCamera.update();
    }

    private void update(float delta) {
        planeStateTime += delta;

        if(Gdx.input.justTouched()) {
            game.setScreen(new MainMenuScreen(game));
        }

    }

    private void draw() {
        camera.update();

        game.batcher.setProjectionMatrix(camera.combined);
        game.batcher.begin();

        game.batcher.draw(Assets.background, 0, 0, 800, 480);
        game.batcher.draw(Assets.ground, groundOffsetX, 0);
        game.batcher.draw(Assets.ground, groundOffsetX + Assets.ground.getRegionWidth(), 0);
        game.batcher.draw(Assets.ceiling, groundOffsetX, 480 - Assets.ceiling.getRegionHeight());
        game.batcher.draw(Assets.ceiling, groundOffsetX + Assets.ceiling.getRegionWidth(), 480 - Assets.ceiling.getRegionHeight());

        game.batcher.end();

        game.batcher.setProjectionMatrix(uiCamera.combined);
        game.batcher.begin();

        game.batcher.draw(Assets.gameOver, Gdx.graphics.getWidth() / 2 - Assets.gameOver.getRegionWidth() / 2, Gdx.graphics.getHeight() / 2 - Assets.gameOver.getRegionHeight() / 2);
        Assets.font.draw(game.batcher, "" + score, Gdx.graphics.getWidth() - 200 , Gdx.graphics.getHeight() / 2 + 36 );

        game.batcher.end();
    }

    @Override
    public void render (float delta) {
        update(delta);
        draw();
    }
}
