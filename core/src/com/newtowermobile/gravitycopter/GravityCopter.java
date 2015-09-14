package com.newtowermobile.gravitycopter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by absolute on 13.09.2015.
 */
public class GravityCopter extends Game {
    final ActionResolver resolver;

    // used by all screens
    public SpriteBatch batcher;

    public GravityCopter(ActionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void create () {
        batcher = new SpriteBatch();
        Assets.load();
        setScreen(new MainMenuScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    public ActionResolver getResolver(){
        return resolver;
    }
}
