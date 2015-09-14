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
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {
	public static Texture background;
	public static TextureRegion backgroundRegion;

	public static TextureRegion ready;
	public static TextureRegion gameOver;
	public static TextureRegion highScores;
	public static TextureRegion achievements;
	public static TextureRegion turbo;
	public static Animation plane;
	public static Animation planeDown;

	public static BitmapFont font;

	public static Music music;
	public static TextureRegion ground;
	public static TextureRegion ceiling;
	public static TextureRegion rock;
	public static TextureRegion rockDown;
	public static Sound explode;

	public static void load () {

		background = new Texture("background.png");

		ready = new TextureRegion(new Texture("ready.png"));
		gameOver = new TextureRegion(new Texture("gameover.png"));
		highScores = new TextureRegion(new Texture("highscores.png"));
		achievements = new TextureRegion(new Texture("achievements.png"));
		turbo = new TextureRegion(new Texture("turbo.png"));

		Texture planeFrame1 = new Texture("plane1.png");
		planeFrame1.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		Texture planeFrame2 = new Texture("plane2.png");
		planeFrame2.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		Texture planeFrame3 = new Texture("plane3.png");
		planeFrame3.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		TextureRegion planeFrame1Down = new TextureRegion(planeFrame1);
		planeFrame1Down.flip(false, true);
		TextureRegion planeFrame2Down = new TextureRegion(planeFrame2);
		planeFrame2Down.flip(false, true);
		TextureRegion planeFrame3Down = new TextureRegion(planeFrame3);
		planeFrame3Down.flip(false, true);

		plane = new Animation(0.05f, new TextureRegion(planeFrame1), new TextureRegion(planeFrame2), new TextureRegion(planeFrame3), new TextureRegion(planeFrame2));
		planeDown = new Animation(0.05f, planeFrame1Down, planeFrame2Down, planeFrame3Down, planeFrame2Down);
		plane.setPlayMode(Animation.PlayMode.LOOP);
		planeDown.setPlayMode(Animation.PlayMode.LOOP);

		ground = new TextureRegion(new Texture("ground.png"));
		ceiling = new TextureRegion(ground);
		rock = new TextureRegion(new Texture("rock.png"));
		rockDown = new TextureRegion(rock);

		ceiling.flip(true, true);
		rockDown.flip(false, true);

		font = new BitmapFont(Gdx.files.internal("obelix.fnt"));
		font.setColor(1f, 0.5f, 0.5f, 0.9f);

		music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
		music.setLooping(true);
		music.setVolume(0.5f);

		explode = Gdx.audio.newSound(Gdx.files.internal("explode.wav"));
	}
}
