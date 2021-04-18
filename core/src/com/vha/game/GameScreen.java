
package com.vha.game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.ListIterator;

//Anotação: falta otimizar coisas como: construtores das classes, variáveis repetidas em classes, modos, etc

public class GameScreen implements Screen {
    VibeHero game;
    //Screen
    private Camera camera;
    private Viewport viewport;

    //Design
    private SpriteBatch batch;
    private Texture background;
    private TextureAtlas textureAtlas;
    private Texture goBack;

    //Objetos
    private Key key;
    private LinkedList<Nota> notaList;

    //key indice
    int indice;
    private int score = 0;
    private String points = "Pontos = 0";
    BitmapFont teste = new BitmapFont();

    public GameScreen(VibeHero game) {
        this.game = game;
        camera = new OrthographicCamera();
        viewport = new StretchViewport(VibeHero.WIDTH, VibeHero.HEIGHT, camera);

        //Instancia as texturas do atlas
        textureAtlas = new TextureAtlas("image.atlas");

        //Background estático usado
        background = new Texture("background_1.png");
        goBack = new Texture("goBack.png");

        //Abre o arquivo de etapa e seta o indice
        indice = 0; //readFile();

        double fator = 1.35;
        key = new Key(125 + 70, 65 + 15, 118 * fator, 59 * fator, textureAtlas);

        //CRIAR FLAG EM TIMEBETWEENNOTAS PRA OU VIR 1, 2 OU 3 (IMPLEMENTAÇÃO DPS) OU VIR UMA SÓ
        notaList = new LinkedList<Nota>();
        notaList.add(new Nota((float) ((VibeHero.WIDTH / 2) - (5 * 50 * fator)), (float) (VibeHero.HEIGHT / 2 + 120 / (2.75)),
                (float) (50 * fator), (float) (25 * fator),
                140, textureAtlas,
                800, indice));

        batch = new SpriteBatch();
    }

    void readFile() {
        File etapa = new File("etapa.txt");
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(etapa));
            String text = null;

            while ((text = br.readLine()) != null) {
                indice = Integer.parseInt(text);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ignored) {
            }
        }
    }

    void writeFile(int indice) {
        try {
            FileWriter etapa = new FileWriter("etapa.txt");
            etapa.write(String.valueOf(indice));
            etapa.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render(float deltaTime) {
        batch.begin();

        detectInput();

        //Função responsável por desenhar o background de acordo com as informações setadas
        batch.draw(background, 0, 0, VibeHero.WIDTH, VibeHero.HEIGHT);

        //Desenha keys
        key.draw(batch);
        key.update(batch, deltaTime);

        teste.setColor(1.0f, 1.0f, 0f, 1.0f);
        teste.draw(batch, points, 1500, 900);
        teste.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        teste.getData().setScale(6f);

        ListIterator<Nota> notaListIterator = notaList.listIterator();
        while (notaListIterator.hasNext()) {
            Nota nota = notaListIterator.next();
            nota.update(deltaTime);
            nota.draw(batch);
        }
        collision();

        batch.end();
    }

    //Detecta colisão - quando dois objetos se tocam em um evento (de clique, por exemplo)
    private boolean detectInput() {
        //Detecta toque na tela - em desenvolvimento
        touchPress();

        //Verifica se os vetores de Key e Nota são iguais. Isto é, detecta se o impulso colocado em
        //Nota é igual ao obtido pelo pressionamento das teclas.
        //Ex: É setado o valor de [5, 0, 0] em Key
        return Arrays.toString(key.getKey()).equals(Arrays.toString(notaList.getLast().getNota()));
    }

    private void touchPress() {
        key.setIndice(indice);
        key.touchPress(indice);
    }

    private String convertNotaSetToString(Integer[] notaSet) {
        if (notaSet[1] == 0) {
            return String.valueOf(notaSet[0]);
        }
        if (notaSet[0] == 0) {
            return String.valueOf(notaSet[1]);
        }
       /* String[] letras;
        switch (notaSet[0]){
            case 1:
                letras = new String[]{"E", "A", "B", "C", "D"};
                return letras[notaSet[1]];
            case 2:
                letras = new String[]{"J", "F", "G", "H", "I"};
                return letras[notaSet[1]];
            case 3:
                letras = new String[]{"O", "K", "L", "M", "N"};
                return letras[notaSet[1]];
        }*/
        return "";
    }

    //Para cada tecla, checa se intercepta a nota
    private void collision() {
        //Percorre a LinkedList de Nota notaList e verifica se há próximo
        ListIterator<Nota> iterator = notaList.listIterator();
        while (iterator.hasNext()) {
            Nota nota = iterator.next();
            System.out.println(Arrays.toString(nota.getNota()) + " " + Arrays.toString(key.getKey()) + " " +
                    key.intersects(nota.getHitbox()));
            if (nota.getNota()[1] > 7) {
                System.out.println(nota.hitbox2 + " " + key.getSpriteHitbox());
            }
            if (key.intersects(nota.getHitbox()) && detectInput()) {
                //TEMPORÁRIO
                if (indice < 9) {
                    key.hit(nota);
                    System.out.println("Acerto!");
                    indice += 1;

                } else if (indice == 9) {
                    key.hit(nota);
                    System.out.println("Acerto!");
                    indice = 0;
                }
                score++;
                points = "Pontos: " + score;
                System.out.println(score);
                game.openResponseView(convertNotaSetToString(notaList.getLast().getNota()));
                spawnNotas(indice, true);
                notaList.remove();
                nota.setIndice(indice);
                //writeFile(indice);
            }
        }
    }

    private void spawnNotas(int indice, Boolean canThrow) { //, float deltaTime
        //Para fase 3 - de palavras: transformar em float
        float timeBetweenSpawns = 3f;
        float notaSpawnTimer = 0;
        //Boolean canThrow =
        int dist = 120;
        double fator = 1.35;

        //notaSpawnTimer += deltaTime;

        if (canThrow) { // notaSpawnTimer > timeBetweenSpawns - P/ LETRAS
            notaList.add(new Nota((float) ((VibeHero.WIDTH / 2) - (5 * 50 * fator)), (float) (VibeHero.HEIGHT / 2 + 120 / (2.75)),
                    (float) (50 * fator), (float) (25 * fator),
                    140, textureAtlas,
                    800, indice));
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        batch.setProjectionMatrix(camera.combined);

    }

    @Override
    public void show() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }

}