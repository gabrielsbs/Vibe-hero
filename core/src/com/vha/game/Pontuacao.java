package com.vha.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Pontuacao implements Screen {

    private Camera camera;
    private Viewport viewport;

    private SpriteBatch batch;
    private Texture background;
    private TextureAtlas textureAtlas;
    private TextureRegion phase1;
    private TextureRegion phase2;
    private TextureRegion phase3;
    private Texture goBack;

    VibeHero game;

    private final int WORDL_WIDTH = VibeHero.WIDTH;
    private final int WORDL_HEIGHT = VibeHero.HEIGHT;

    public Pontuacao(VibeHero game) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORDL_WIDTH, WORDL_HEIGHT, camera);

        textureAtlas = new TextureAtlas("botoes.atlas");
        background = new Texture("background_1.png");
        phase1 = textureAtlas.findRegion("botaoIniciar");
        phase2 = textureAtlas.findRegion("botaoIniciar");
        phase3 = textureAtlas.findRegion("botaoIniciar");
        goBack = new Texture("goBack.png");

        batch = new SpriteBatch();

    }

    @Override
    public void render(float delta) {
        game.batch.begin();

        game.batch.draw(background,0,0,WORDL_WIDTH,WORDL_HEIGHT);

        game.batch.draw(phase1, 218,267,60,60);
        game.batch.draw(phase2, 333,267,60,60);
        game.batch.draw(phase3, 438,267,60,60);

        if(Gdx.input.getX() < 665 && Gdx.input.getX() > 600 && WORDL_HEIGHT - Gdx.input.getY() < 332 && WORDL_HEIGHT - Gdx.input.getY() > 267){
            game.batch.draw(goBack, 600,267,70,70);
            if(Gdx.input.isTouched()){
                game.setScreen(new Menu(game));
            }
        }
        else {
            game.batch.draw(goBack, 600,267,60,60);
        }

        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height,true);
        game.batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }

    @Override
    public void dispose() { }

    @Override
    public void show() { }

}
