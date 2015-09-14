package com.newtowermobile.gravitycopter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by absolute on 14.09.2015.
 */
public class GameScreen extends ScreenAdapter {
    private static float GRAVITY_FINAL = 15;
    public static final float PLANE_START_Y = 240;
    public static final float PLANE_START_X = 50;
    private static final float PLANE_VELOCITY_X = 200;

    GravityCopter game;

    OrthographicCamera camera;
    OrthographicCamera uiCamera;

    Rectangle turboRect = new Rectangle();
    Vector2 turboLoc;

    float groundOffsetX = 0;

    Vector2 planePosition = new Vector2();
    Vector2 planeVelocity = new Vector2();

    float planeStateTime = 0;
    Vector2 gravity = new Vector2();
    Array<Rock> rocks = new Array<Rock>();
    int score = 0;
    boolean isGameOver = false;

    static class Rock {
        Vector2 position = new Vector2();
        TextureRegion image;
        boolean counted;

        public Rock(float x, float y, TextureRegion image) {
            this.position.x = x;
            this.position.y = y;
            this.image = image;
        }
    }

    public GameScreen(GravityCopter game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        uiCamera = new OrthographicCamera();
        uiCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        uiCamera.update();

        turboLoc = new Vector2( 30, 30);
        turboRect.set(turboLoc.x - 20, turboLoc.y - 20, turboLoc.x + Assets.turbo.getRegionWidth() + 20, turboLoc.x + Assets.turbo.getRegionHeight() + 20);

        resetWorld();
    }

    private void resetWorld() {
        score = 0;
        groundOffsetX = 0;
        planePosition.set(PLANE_START_X, PLANE_START_Y);
        planeVelocity.set(0, 0);
        gravity.set(0, GRAVITY_FINAL);
        camera.position.x = 400;

        rocks.clear();
        for(int i = 0; i < 5; i++) {
            boolean isDown = MathUtils.randomBoolean();
            rocks.add(new Rock(700 + i * 200, isDown?480-Assets.rock.getRegionHeight(): 0, isDown? Assets.rockDown: Assets.rock));
        }
    }

    private void update(float delta) {
        planeStateTime += delta;

        if(isGameOver)
            return;

        if(Gdx.input.justTouched()) {
            Vector2[] touchPoints = new Vector2[2];
            if(Gdx.input.isTouched(0)){
                touchPoints[0] = new Vector2(Gdx.input.getX(0), Gdx.input.getY(0));
                Gdx.app.log("touchevent", "0 touched at " + touchPoints[0].x+ " " + touchPoints[0].y);
            }
            if(Gdx.input.isTouched(1)){
                touchPoints[1] = new Vector2(Gdx.input.getX(1), Gdx.input.getY(1));
                Gdx.app.log("touchevent", "1 touched at " + touchPoints[1].x+ " " + touchPoints[1].y);
            }

            if(OverlapTester.onePointNotInRectangle(turboRect, touchPoints)){
                gravity.x = 0;
                gravity.y = gravity.y > 0 ?  -GRAVITY_FINAL : GRAVITY_FINAL;
            }

            if(OverlapTester.onePointInRectangle(turboRect, touchPoints)){
                gravity.x = 10;
            }
        }

        if(gravity.y > 0){
            gravity.y -= (delta * 5);
        }
        else{
            gravity.y += (delta * 5);
        }

        planeVelocity.add(gravity);
        if(gravity.x == 0){
            planeVelocity.x = PLANE_VELOCITY_X;
        }

        planePosition.mulAdd(planeVelocity, delta);

        camera.position.x = planePosition.x + 350;
        if(camera.position.x - groundOffsetX > Assets.ground.getRegionWidth() + 400) {
            groundOffsetX += Assets.ground.getRegionWidth();
        }

        Rectangle rect1 = new Rectangle(planePosition.x + 20, planePosition.y, Assets.plane.getKeyFrames()[0].getRegionWidth() - 20, Assets.plane.getKeyFrames()[0].getRegionHeight());
        for(Rock r: rocks) {
            if(camera.position.x - r.position.x > 400 + r.image.getRegionWidth()) {
                boolean isDown = MathUtils.randomBoolean();
                r.position.x += 5 * 200;
                r.position.y = isDown?480-Assets.rock.getRegionHeight(): 0;
                r.image = isDown? Assets.rockDown: Assets.rock;
                r.counted = false;
            }
            Rectangle rect2 = new Rectangle(r.position.x + (r.image.getRegionWidth() - 30) / 2 + 20, r.position.y, 20, r.image.getRegionHeight() - 10);
            if(rect1.overlaps(rect2)) {
                gameOver();
            }
            if(r.position.x < planePosition.x && !r.counted) {
                score++;
                r.counted = true;
            }
        }

        if(planePosition.y < Assets.ground.getRegionHeight() - 20 ||
                planePosition.y + Assets.plane.getKeyFrames()[0].getRegionHeight() > 480 - Assets.ground.getRegionHeight() + 20) {

            gameOver();
        }
    }

    private void draw() {
        camera.update();
        game.batcher.setProjectionMatrix(camera.combined);
        game.batcher.begin();

        game.batcher.draw(Assets.background, camera.position.x - Assets.background.getWidth() / 2, 0);
        for(Rock rock: rocks) {
            game.batcher.draw(rock.image, rock.position.x, rock.position.y);
        }
        game.batcher.draw(Assets.ground, groundOffsetX, 0);
        game.batcher.draw(Assets.ground, groundOffsetX + Assets.ground.getRegionWidth(), 0);
        game.batcher.draw(Assets.ceiling, groundOffsetX, 480 - Assets.ceiling.getRegionHeight());
        game.batcher.draw(Assets.ceiling, groundOffsetX + Assets.ceiling.getRegionWidth(), 480 - Assets.ceiling.getRegionHeight());
        if(gravity.y > 0) {
            game.batcher.draw(Assets.plane.getKeyFrame(planeStateTime), planePosition.x, planePosition.y);
        }
        else{
            game.batcher.draw(Assets.planeDown.getKeyFrame(planeStateTime), planePosition.x, planePosition.y);
        }
        game.batcher.end();

        game.batcher.setProjectionMatrix(uiCamera.combined);
        game.batcher.begin();
        Assets.font.draw(game.batcher, "" + score, Gdx.graphics.getWidth() - 200, Gdx.graphics.getHeight() / 2 + 36);
        game.batcher.draw(Assets.turbo, turboLoc.x, Gdx.graphics.getHeight() - turboLoc.y - Assets.turbo.getRegionHeight());
        game.batcher.end();
    }

    @Override
    public void render (float delta) {
        update(delta);
        draw();
    }

    private void gameOver() {
        isGameOver = true;
        Assets.explode.play();
        planeVelocity.x = 0;
        planeVelocity.y = 0;

        game.getResolver().submitScoreGPGS(score);

        if(score >= 100000){
            game.getResolver().unlockAchievementGPGS(com.newtowermobile.gravitycopter.Constants.AchievementIds.ach100000);
        }
        else if(score >= 10000){
            game.getResolver().unlockAchievementGPGS(com.newtowermobile.gravitycopter.Constants.AchievementIds.ach10000);
        }
        else if(score >= 1000){
            game.getResolver().unlockAchievementGPGS(com.newtowermobile.gravitycopter.Constants.AchievementIds.ach1000);
        }
        else if(score >= 100){
            game.getResolver().unlockAchievementGPGS(com.newtowermobile.gravitycopter.Constants.AchievementIds.ach100);
        }
        else if(score >= 10){
            game.getResolver().unlockAchievementGPGS(com.newtowermobile.gravitycopter.Constants.AchievementIds.ach10);
        }

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                game.setScreen(new GameOverScreen(game, score));
            }
        }, 1000);

    }
}
