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

public class Menu implements Screen {

    //screen
    private Camera camera;
    private Viewport viewport;

    //graphics
    private SpriteBatch batch;
    private TextureAtlas textureAtlas;
    private Texture background;
    private Texture playButton;
    private Texture replayButton;
    private Texture trophyButton;
    private Texture phase1;
    private Texture phase2;
    private Texture phase3;

    //fator de design
    int fator = 3;

    final VibeHero game;


    public Menu(final VibeHero game){
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new StretchViewport(VibeHero.WIDTH,VibeHero.HEIGHT,camera);

        background = new Texture("backgroundtITitle.png");
        playButton =  new Texture("play.jpg");
        replayButton =  new Texture("goBack.png");
        trophyButton =  new Texture("trophy.jpg");
        phase1 =  new Texture("num.png");
        phase2 =  new Texture("letra.png");
        phase3 =  new Texture("simb.png");

        batch = new SpriteBatch();
    }


    @Override
    public void render(float delta) {
        game.batch.begin();

        game.batch.draw(background,0,0,VibeHero.WIDTH,VibeHero.HEIGHT);

        //botao play
        if (Gdx.input.getX() < fator * 405
                && Gdx.input.getX() > fator * 340
                && VibeHero.HEIGHT - Gdx.input.getY() < fator * 225
                && VibeHero.HEIGHT - Gdx.input.getY() > fator * 170) {
            game.batch.draw(playButton, fator * 350,fator * 170,fator * 60,fator * 60);
            if (Gdx.input.isTouched()) {
                game.setScreen(new GameScreen(game));

            }
        }
        else {
            game.batch.draw(playButton, fator * 350,fator * 170,fator * 50,fator * 50);
        }

        //botao replay
        if (Gdx.input.getX() < fator * 405
                && Gdx.input.getX() > fator * 340
                && VibeHero.HEIGHT - Gdx.input.getY() < fator * 155
                && VibeHero.HEIGHT - Gdx.input.getY() > fator * 100){
            game.batch.draw(replayButton, fator * 348,fator * 100,fator * 60,fator * 60);
        }
        else{
            game.batch.draw(replayButton, fator * 350,fator * 100,fator * 50,fator * 50);
        }

        //botao pontuacao
        if(Gdx.input.getX() < fator * 405
                && Gdx.input.getX() > fator * 340
                && VibeHero.HEIGHT - Gdx.input.getY() < fator * 85
                && VibeHero.HEIGHT - Gdx.input.getY() > fator * 30){
            game.batch.draw(trophyButton, fator * 348,fator * 30,fator * 60,fator * 60);
            if(Gdx.input.isTouched()){
                game.setScreen(new Pontuacao(game));
            }
        } else {
            game.batch.draw(trophyButton, fator * 350,fator * 30,fator * 50,fator * 50);
        }

        //botao fase numeros
        if(Gdx.input.getX() < fator * 670
                && Gdx.input.getX() > fator * 600
                && VibeHero.HEIGHT - Gdx.input.getY() < fator * 285
                && VibeHero.HEIGHT - Gdx.input.getY() > fator * 225){
            game.batch.draw(phase1, fator * 610,fator * 225,fator * 70,fator * 70);
        } else {
            game.batch.draw(phase1, fator * 610,fator * 225,fator * 60,fator * 60);
        }

        //botao fase alfabeto
        if(Gdx.input.getX() < fator * 670 &&
                Gdx.input.getX() > fator * 600 &&
                VibeHero.HEIGHT - Gdx.input.getY() < fator * 215 &&
                VibeHero.HEIGHT - Gdx.input.getY() > fator * 155){
            game.batch.draw(phase2, fator * 610,fator * 155,fator * 70,fator * 70);
        }else{
            game.batch.draw(phase2, fator * 610,fator * 155,fator * 60,fator * 60);
        }

        //botao fase simbolos
        if(Gdx.input.getX() < fator * 670
                && Gdx.input.getX() > fator * 600
                && VibeHero.HEIGHT - Gdx.input.getY() < fator * 145
                && VibeHero.HEIGHT - Gdx.input.getY() > fator * 85){
            game.batch.draw(phase3, fator * 610,fator * 85,fator * 70,fator * 70);
        }
        else{
            game.batch.draw(phase3, fator * 610,fator * 85,fator * 60,fator * 60);
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
