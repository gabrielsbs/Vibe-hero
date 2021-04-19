package com.vha.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;

import sun.rmi.runtime.Log;

public class  Key extends Game implements InputProcessor {
    //Características de Key
    private float movementSpeed = 0;
    private Code code;
    private Integer[] key = new Integer[3]; //tem a ver com a fase an
    private boolean keyboard_flag = false; //SETA SE É COM TECLADO OU TOUCH
    private int indice, indice2;
    private boolean score_flag = false;
    private int score;

    //Posição e dimensão
    Rectangle hitbox;

    //Design
    private TextureAtlas keyTextAtlas;
    private TextureRegion keyTexture;
    private Sprite keySprite;
    private Camera camera;
    private Array<Sprite> keySprites;
    private SpriteBatch batch;
    private Vector3 touchPoint;
    private Vector3 touchPoint2;
    //novo
    private Vector3 touchLeft;
    private Vector3 touchRight;

    private String message = "";

    // MultiTouch
    private class TouchInfo {
        public float touchX = 0;
        public float touchY = 0;
        public boolean touched = false;
    }

    private Map<Integer, TouchInfo> touches = new HashMap<Integer, TouchInfo>();

    private LinkedList<HitAnimation> hitAnim = new LinkedList<>();

    public Key (float xCentre, float yCentre,
                double width, double height,
                TextureAtlas atlas){
        this.movementSpeed = 0;
        this.keyTexture = atlas.findRegion("key");
        this.hitbox = new Rectangle(xCentre, yCentre, (float) width, (float) height);

        camera = new PerspectiveCamera();
        batch = new SpriteBatch();

        keyTextAtlas = atlas;

        // posteriormente usar arquivos, ou algo do tipo, para segurar a fase, indice e pontuação
        indice = 4;
        score = 0;

        batch = new SpriteBatch();

        //novo
        touchLeft = new Vector3(-1, -1, 0);
        touchRight = new Vector3(-1, -1, 0);
    }

    private class HitAnimation {
        //Hit design
        private String[] fireAnimInfo;
        private TextureRegion[] fireTextures;
        private Animation<TextureRegion> fireAnimation;
        private float fireAnimTimer;

        HitAnimation () {
          /*  fireAnimInfo = new String[] {
                    "fog1", "fog2", "fog3", "fog4", "fog5", "fog6", "fog7", "fog8", "fog9", "fog10"
            };
            fireTextures = new TextureRegion[fireAnimInfo.length];
            for (int i = 0; i < fireAnimInfo.length; i++) {
               // fireTextures[i] = keyTextAtlas.findRegion(fireAnimInfo[i]);
            }

            float totalAnimationTime = 1f;
            fireAnimation = new Animation<TextureRegion>(totalAnimationTime / 10, fireTextures);
            fireAnimTimer = 0;*/
        }

        public void update (float deltaTime) {
            fireAnimTimer += deltaTime;

        }

        public void draw (SpriteBatch batch) {
            batch.draw(fireAnimation.getKeyFrame(fireAnimTimer),
                    hitbox.x * indice - 10, hitbox.y,
                    hitbox.width + 40, hitbox.height + 40);
        }

        public boolean isFinished () {
            return fireAnimation.isAnimationFinished(fireAnimTimer);
        }
    }

    public void update (SpriteBatch batch, float deltaTime) {
        ListIterator<HitAnimation> hitAnimationListIterator = hitAnim.listIterator();

       /* while (hitAnimationListIterator.hasNext()) {
            HitAnimation hitAnimation = hitAnimationListIterator.next();
            hitAnimation.update(deltaTime);
            if (hitAnimation.isFinished()) {
                hitAnimationListIterator.remove();
            } else {
                hitAnimation.draw(batch);
            }
        }*/
    }

    @Override
    public void create () {
        //Seta características da sprite
        keySprite = new Sprite(keyTexture);
        keySprite.setSize(hitbox.width, hitbox.height);

        // Capta entrada de toque
        Gdx.input.setInputProcessor(this);

        // Loop dos toques
        for (int i = 0; i < 10; i++) {//i < 10
            touches.put(i, new TouchInfo());
        }

        touchPoint = new Vector3(0, 0, 0);


    }

    @Override
    public void render() {
        indice = code.getIndice();
        indice2 = code.getIndice() + 5;
        super.render();
        camera.update();

    }


    public void draw(SpriteBatch batch) {
        inicializaKeys();

        for(int i = 0; i < 10; i++) {
            keySprites.get(i).draw(batch);
        }
    }

    private void inicializaKeys() {
        //Múltiplos sprites
        keySprites = new Array<Sprite>(10);
        for(int i = 0; i < 10; i++) {
            keySprites.add(new Sprite(keyTexture));
            keySprites.get(i).setPosition(hitbox.x + (hitbox.width + 20) * i, hitbox.y);
            keySprites.get(i).setSize(hitbox.width, hitbox.height);
        }
    }

    //Função responsável por pegar toques simultâneos
    private void multiTouch() {
        Boolean flagPoint = Boolean.FALSE;
        if (touches.get(0).touched) {
            testeSpritesTocadas(touchPoint);
        }
    }

    public Rectangle getSpriteHitbox() {
        return keySprites.get(indice).getBoundingRectangle();
    }

    public Rectangle getSpriteHitbox2() {
        return keySprites.get(indice2).getBoundingRectangle();
    }

    private void testeSpritesTocadas(Vector3 touchPoint) {
        inicializaKeys();
        Vector3 touchP = new Vector3(touchPoint.x, touchPoint.y, 0);
        camera.unproject(touchP);

        for (int i = 0; i < 10; i++) {
            //Primeiro toque
            if (keySprites.get(i).getBoundingRectangle().contains(touchPoint.x - 50, (float) (touchPoint.y / 9))) {
                if (i < 5) {
                    key[0] = i + 1;
                    //if(!segundoToque)
                    key[1] = 0;
                    key[2] = 0;
                } else if (i >= 5) {
                    key[1] = i + 1;
                    //segundotoque
                    key[0] = 0;
                    key[2] = 0;
                    //Colocar exceção para z e ç
                }
            }
        }
    }

    public void hit (Nota nota) {
        score_flag = true;
        score += 1;
        setIndice(indice + 1, indice2 + 1);
        // animação
        hitAnim.add(new HitAnimation());
    }

    //Verifica se as posições da hitbox desta classe e da hitbox de entrada se interceptam
    //ADIÇÃO: fator de correção por um bug dado pela diferença de set da câmera pelo código e pelo InputProcessor
    public boolean intersects(Rectangle hitbox) {
        int fatorCorrecao = 0, x = (int) hitbox.x;
        hitbox.x = x + fatorCorrecao;
        return keySprites.get(indice).getBoundingRectangle().overlaps(hitbox);
    }

    public boolean intersects2(Rectangle hitbox) {
        int fatorCorrecao = 0, x = (int) hitbox.x;
        hitbox.x = x + fatorCorrecao;
        return keySprites.get(indice2).getBoundingRectangle().overlaps(hitbox);
    }

    //Detecta teclas do teclado apertadas
    public Integer[] keyPress() {
        Integer[] press = new Integer[3];
        boolean flag90 = false;

        if (Gdx.input.isKeyPressed(Input.Keys.NUM_1)) {
            key[0] = 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)) {
            key[0] = 2;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_3)) {
            key[0] = 3;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_4)) {
            key[0] = 4;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_5)) {
            key[0] = 5;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_6)) {
            key[1] = 6;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_7)) {
            key[1] = 7;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_8)) {
            key[1] = 8;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_9)) {
            key[1] = 9;
            flag90 = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_0)) {
            if (flag90) {
                key[0] = 9;
            }
            key[1] = 10;
        }
        else if (keyboard_flag) {
            //temporário
            key[0] = 0;
            key[1] = 0;
            key[2] = 0;
        }

        // TEMPORÁRIO
        key[2] = 0;
        press[0] = key[0];
        press[1] = key[1];
        press[2] = key[2];

        return press;
    }

    public void touchPress(int indice, int indice2) {
        Gdx.input.setInputProcessor(this);
    }

    public Integer[] getKey () {
        Integer[] aux_k = new Integer[]{key[0], key[1], key[2]};

        return key;
    }

    public void setIndice(int indice, int indice2) {
        this.indice = indice;
        this.indice2 = indice2;
    }

    public int getIndice(int indice) { return indice; }

    public void flushList() {
        key = new Integer[]{0, 0, 0};
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(pointer < 10){
            //Teste da primeira fase - 1 input
            touches.put(pointer, new TouchInfo());
            touches.get(pointer).touchX = screenX;
            touches.get(pointer).touchY = screenY;
            touches.get(pointer).touched = true;

            touchPoint = new Vector3(screenX, screenY, 0);
            touchRight = new Vector3(screenX, screenY, 0);
            touchLeft = new Vector3(screenX, screenY, 0);
            inicializaKeys();
            multiTouch();
        }

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(pointer < 10){
            touches.get(pointer).touchX = 0;
            touches.get(pointer).touchY = 0;
            touches.get(pointer).touched = false;

            key = new Integer[]{0, 0, 0};
        }
        return true;
    }

    public static class InputTransform
    {
        private static int appWidth = 480;
        private static int appHeight = 320;

        public static float getCursorToModelX(int screenX, int cursorX)
        {
            return (((float)cursorX) * appWidth) / ((float)screenX);
        }

        public static float getCursorToModelY(int screenY, int cursorY)
        {
            return ((float)(screenY - cursorY)) * appHeight / ((float)screenY) ;
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
