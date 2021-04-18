package com.vha.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class VibeHero extends Game implements ApplicationListener {
    GameScreen gameScreen;

    private IActivityRequestHandler myRequestHandler;
    public static int HEIGHT;
    public static int WIDTH;

    public VibeHero(IActivityRequestHandler handler, int height, int width) {
        myRequestHandler = handler;
        this.HEIGHT = height;
        this.WIDTH = width;
    }

    public SpriteBatch batch;


    @Override
    public void create() {
        batch = new SpriteBatch();
        this.setScreen(new Menu(this));
    }

    @Override
    public void dispose() {
        super.dispose();
        ;
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    public void openResponseView(String answer) {
        myRequestHandler.showResponseSelector(true, answer);
    }

}
