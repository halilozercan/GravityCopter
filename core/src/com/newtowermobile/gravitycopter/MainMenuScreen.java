/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.newtowermobile.gravitycopter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class MainMenuScreen extends ScreenAdapter {

	GravityCopter game;

	OrthographicCamera camera;
	OrthographicCamera uiCamera;

	Rectangle highScoresRect = new Rectangle();
	Rectangle achievementsRect = new Rectangle();
	Vector2 highScoresLoc;
	Vector2 achievementsLoc;

	float planeStateTime;
	int groundOffsetX = 0;

	public MainMenuScreen(GravityCopter game) {
		this.game = game;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		uiCamera = new OrthographicCamera();
		uiCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		uiCamera.update();

		highScoresLoc = new Vector2(Gdx.graphics.getWidth() / 4 - Assets.highScores.getRegionWidth() / 2, 3 * (Gdx.graphics.getHeight() / 4) - Assets.highScores.getRegionHeight() / 2);
		highScoresRect.set(highScoresLoc.x - 20, highScoresLoc.y - 20 , Assets.highScores.getRegionWidth() + 20, Assets.highScores.getRegionHeight() + 20);

		achievementsLoc = new Vector2( (3 * Gdx.graphics.getWidth()) / 4 - Assets.achievements.getRegionWidth() / 2, 3 * (Gdx.graphics.getHeight() / 4) - Assets.achievements.getRegionHeight() / 2);
		achievementsRect.set(achievementsLoc.x - 20, achievementsLoc.y - 20, Assets.achievements.getRegionWidth() + 20, Assets.achievements.getRegionHeight() + 20);

		if (!game.getResolver().getSignedInGPGS())
			game.getResolver().loginGPGS();
	}

	public void update (float delta) {
		planeStateTime += delta;

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

			if(OverlapTester.onePointInRectangle(highScoresRect, touchPoints)){
				game.getResolver().getLeaderboardGPGS();
			}
			else if(OverlapTester.onePointInRectangle(achievementsRect, touchPoints)){
				game.getResolver().getAchievementsGPGS();
			}
			else {
				game.setScreen(new GameScreen(game));
				game.getResolver().showAds(false);
			}
		}

		if(camera.position.x - groundOffsetX > Assets.ground.getRegionWidth() + 400) {
			groundOffsetX += Assets.ground.getRegionWidth();
		}
	}

	public void draw () {
		camera.update();

		game.batcher.setProjectionMatrix(camera.combined);
		game.batcher.begin();

		game.batcher.draw(Assets.background, 0, 0, 800, 480);
		game.batcher.draw(Assets.ground, groundOffsetX, 0);
		game.batcher.draw(Assets.ground, groundOffsetX + Assets.ground.getRegionWidth(), 0);
		game.batcher.draw(Assets.ceiling, groundOffsetX, 480 - Assets.ceiling.getRegionHeight());
		game.batcher.draw(Assets.ceiling, groundOffsetX + Assets.ceiling.getRegionWidth(), 480 - Assets.ceiling.getRegionHeight());
		game.batcher.draw(Assets.plane.getKeyFrame(planeStateTime), GameScreen.PLANE_START_X, GameScreen.PLANE_START_Y);

		game.batcher.end();

		game.batcher.setProjectionMatrix(uiCamera.combined);
		game.batcher.begin();

		game.batcher.draw(Assets.ready, Gdx.graphics.getWidth() / 2 - Assets.ready.getRegionWidth() / 2, Gdx.graphics.getHeight() / 2 - Assets.ready.getRegionHeight() / 2);
		game.batcher.draw(Assets.highScores, highScoresLoc.x, highScoresLoc.y / 3);
		game.batcher.draw(Assets.achievements, achievementsLoc.x, achievementsLoc.y / 3);

		game.batcher.end();
	}

	@Override
	public void render (float delta) {
		update(delta);
		draw();
	}
}
