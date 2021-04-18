package com.vha.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Vector;


public class Nota {
    //Posição e dimensão
    Rectangle hitbox;
    Rectangle hitbox2;
    Integer[] notaSet;
    //static int fase = 1;
    float[] xy;
    float path;

    //Características da nota
    float movementSpeed; //world units per second
    float timeBetweenNotas;
    private float timeSinceLastNota;
    Code code;
    int indice;

    //Design
    TextureAtlas textureAtlas;
    TextureRegion textureRegion;

    private Fase fase = new Fase();
    //Criar uma classe para fases, armazenar animações lá
    private class Fase {
        // Construtor deve abrir o arquivo
        int fase, indice;
        private LinkedList<TextureRegion> fase1 = new LinkedList<>();
        private TextureAtlas fase1Atlas = new TextureAtlas("numeros.atlas");

        public Fase() {
            // Abrir arquivo
            fase = 1;
            fase1();
        }

        public void fase1 () {
            for (Integer i = 1; i < 10; i++) {
                fase1.add(fase1Atlas.findRegion(i.toString()));
            }
            fase1.add(fase1Atlas.findRegion("0"));
        }

        public void draw (Batch batch) {
            System.out.println(this.indice + " " + fase1.size());
            batch.draw(fase1.get(this.indice), 80, 900, 70, 70);
        }

        public void setIndice(int indice) { this.indice = indice; }

        public int getFase() {
            return fase;
        }
    }

    public Nota(float xCentre, float yPosition,
                float width, float height,
                float movementSpeed, TextureAtlas atlas,
                float timeBetweenNotas, int indice) {
        this.movementSpeed = movementSpeed;
        textureAtlas = atlas;
        this.textureRegion = textureAtlas.findRegion("nota");
        this.timeBetweenNotas = timeBetweenNotas;
        this.indice = indice;
        //seta a fase em code - em desenvolvimento
        code = new Code(fase.getFase(), indice);
        this.notaSet = code.getLista();
        //Definições da nota 1 - dividido em dois por problemas com o envio de notas duplas - modificar com o uso de lista para notas
        // posteriormente, englobando o timeBetweenNotas que deve ter uma condição específica para jogar notas duplas ou não
        this.hitbox = new Rectangle(xCentre - (width / 2) - (30 * notaSet[0]), yPosition, width, height);
        //Definições da nota 2
        this.hitbox2 = new Rectangle((float)((xCentre - (width)) + (120 * (notaSet[1]) - 5)), yPosition, width, height);
        //Pega os valores iniciais de x e y para dropar novas notas no mesmo local (requerido, até o momento, pois hitbox tem seus
        // valores alterados durante a execução
        this.xy = new float[]{(xCentre - (width / 2) - (30 * notaSet[0])), yPosition};
        path = 0;

    }

    public Nota(Nota nota) {
        this(nota.hitbox.x, nota.hitbox.y, nota.hitbox.width, nota.hitbox.height, nota.movementSpeed, nota.textureAtlas, nota.timeBetweenNotas, nota.indice);
    }

    public Nota(Nota nota, int indice) {
        this(nota.hitbox.x, nota.hitbox.y, nota.hitbox.width, nota.hitbox.height, nota.movementSpeed, nota.textureAtlas, nota.timeBetweenNotas, nota.indice);
        this.hitbox = new Rectangle((nota.hitbox.x - (30 * this.notaSet[0])), nota.hitbox.y, nota.hitbox.width, nota.hitbox.height);
        this.hitbox2 = new Rectangle((nota.hitbox.x + (20 * this.notaSet[1])), nota.hitbox.y, nota.hitbox.width, nota.hitbox.height);
    }

    public void draw(Batch batch) {
        throwNota(batch);
        fase.draw(batch);
        //Fase fase = new Fase();
//        fase.draw(batch);
        move();
    }

    //Movimentação da nota
    private void move() {
        hitbox.y -= movementSpeed/150;
        if((int)hitbox.y < 5) {
            hitbox.x = xy[0];
            hitbox2.x = xy[0] + 5 * notaSet[1];
            hitbox.y = xy[1];
            hitbox2.y = xy[1];
            hitbox.width = 50;
            hitbox.height = 25;
            path = 0;
        }
    }

    //Atualiza o as dimensões e posição da nota - em desenvolvimento
    public void update(float deltaTime) {
        //Números experimentais

        double[] factor;
        path += 1.46;
        float noteSpace = (float) (0.23 / 9);
        int WORLD_WIDTH = VibeHero.WIDTH;
        double fator = 1.35;

        factor = new double[]{
                WORLD_WIDTH * 0.4 - path * 0.74, WORLD_WIDTH * (0.4 + noteSpace) - path * 0.64,
                WORLD_WIDTH * (0.4 + noteSpace *  2) - path * 0.46, WORLD_WIDTH * (0.4 + noteSpace * 3) - path * 0.34,
                WORLD_WIDTH * (0.4 + noteSpace * 4) - path * 0.18, WORLD_WIDTH * (0.4 + noteSpace * 5) - path * 0.1,
                WORLD_WIDTH * (0.4 + noteSpace * 6) + path * 0.08, WORLD_WIDTH * (0.4 + noteSpace* 7) + path * 0.2,
                WORLD_WIDTH * (0.4 + noteSpace * 8) + path * 0.3, WORLD_WIDTH * (0.4 + noteSpace * 9) + path * 0.31};

        if (hitbox.width < (130 * fator)) {
            hitbox.width += 0.17;
            hitbox.height += 0.17;
            if (getNota()[0] != null && getNota()[0] != 0) {
                hitbox.x = (float) factor[notaSet[0] - 1];
            }
            if (getNota()[1] != null && getNota()[1] != 0) {
                hitbox2.x = (float) factor[notaSet[1] - 1] + 50;
            }
        }

        //Atualiza as dimensões e posições
        hitbox.set(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
        hitbox2.set(hitbox2.x, hitbox.y, hitbox.width, hitbox.height);
        timeSinceLastNota += deltaTime;
        code.setIndiceList(indice);
        notaSet = code.getLista();
        fase.setIndice(indice);
    }

    //Efetivamente desenha as notas, com mais detalhamento. É atualizado por update e atualiza o design da nota
    public void throwNota(Batch batch) {
        if (notaSet[0] == 1) {
            batch.draw(textureRegion, (hitbox.x), hitbox.y, hitbox.width, hitbox.height);
        }
        if (notaSet[0] == 2) {
            batch.draw(textureRegion, (hitbox.x), hitbox.y, hitbox.width, hitbox.height);
        }
        if (notaSet[0] == 3) {
            batch.draw(textureRegion, (hitbox.x), hitbox.y, hitbox.width, hitbox.height);
        }
        if (notaSet[0] == 4) {
            batch.draw(textureRegion, (hitbox.x), hitbox.y, hitbox.width, hitbox.height);
        }
        if (notaSet[0] == 5) {
            batch.draw(textureRegion, (hitbox.x), hitbox.y, hitbox.width, hitbox.height);
        }
        if (notaSet[1] == 6) {
            batch.draw(textureRegion, (hitbox2.x), hitbox.y, hitbox.width, hitbox.height);
        }
        if (notaSet[1] == 7) {
            batch.draw(textureRegion, (hitbox2.x), hitbox.y, hitbox.width, hitbox.height);
        }
        if (notaSet[1] == 8) {
            batch.draw(textureRegion, (hitbox2.x), hitbox.y, hitbox.width, hitbox.height);
        }
        if (notaSet[1] == 9) {
            batch.draw(textureRegion, (hitbox2.x), hitbox.y, hitbox.width, hitbox.height);
        }
        if (notaSet[1] == 10) {
            batch.draw(textureRegion, (hitbox2.x), hitbox.y, hitbox.width, hitbox.height);
        }
    }

    //TODO
    public boolean canThrowNota() {
        return (timeSinceLastNota - timeBetweenNotas >= 0);
    }

    public float getX() {
        return hitbox.x;
    }

    public float getY() {
        return hitbox.y;
    }

    public void setMovementSpeed(float movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

    //Retorna as especificações de nota através de Rectangle (objeto da libgdx)
    public Rectangle getHitbox() {
        if (indice < 5)
            return new Rectangle(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
        else
            return new Rectangle(hitbox2.x, hitbox2.y, hitbox.width, hitbox.height);
        }

    //Seta um array de inteiros com a nota (Ex: [5, 0, 0])
    public void setNota(Integer[] notaSet) {
        //Teste com a posição inicial
        if((this.notaSet[0] != null && this.notaSet[0] == 0) && (this.notaSet[1] != null && this.notaSet[1] == 0)) {
            hitbox.x = hitbox.x - 10 * notaSet[0];
            hitbox2.x = hitbox.x + 10 * notaSet[1];
        }
        this.notaSet = notaSet;
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }

    public Integer[] getNota() {
        if (notaSet[0] == null)
            notaSet[0] = 0;
        else if (notaSet[1] == null)
            notaSet[1] = 0;
        else if (notaSet[2] == null)
            notaSet[2] = 0;

        return notaSet;
    }

}
