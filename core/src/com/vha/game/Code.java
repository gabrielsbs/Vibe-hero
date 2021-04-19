package com.vha.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

//OBS: Essa classe é um teste, pode haver funções e variáveis não efetivas.
//Inicialmente, essa classe deveria segurar os toques na tela, porém, houve um problema na conexão com a
//classe Key, que obtém essa função por agora. - em discussão

public class Code {
    private static int indice;
    private int indiceList;

    private int fase;
    private Integer[] pressCode;
    private Integer[][] faseDeNotas1 = {{1, 0, 0}, {2, 0, 0}, {3, 0, 0}, {4, 0, 0}, {5, 0, 0},
                                        {0, 6, 0}, {0, 7, 0}, {0, 8, 0}, {0, 9, 0}, {0, 10, 0}};
    private Integer[][] faseDeNotas2 = {{1, 6, 0}, {1, 7, 0}, {1, 8, 0}, {1, 9, 0}, {1, 10, 0}};


    public Code(int fase) {
        this.fase = fase;

        indice += 1;

        pressCode = new Integer[3];
        pressCode[0] = 0;
        pressCode[1] = 0;
        pressCode[2] = 0;
    }

    public Code(int fase, int indice) {
        this.fase = fase;
        this.indiceList = indice;

        pressCode = new Integer[]{0, 0, 0};
    }

    public Vector<Integer> getNota() {
        Vector<Integer> excluir = new Vector<Integer>(3);
        excluir.add(0);
        excluir.add(5);
        excluir.add(0);
        return excluir;
    }

    public void touchPress(){}

    public int getFase() {
        return fase;
    }

    public void setIndiceList(int indice){
        this.indiceList = indice;
    }

    public Integer[] getLista(){
        return faseDeNotas1[indiceList];
    }

    public Integer[] getLista2() {return faseDeNotas2[indiceList];}

    public int getIndice() { return this.indiceList; }

}
