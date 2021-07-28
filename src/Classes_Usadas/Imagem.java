/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes_Usadas;

/**
 *
 * @author DIELME
 */
public class Imagem {
    
    protected String numMagico;       //identifica o tipo da imagem
    protected String comentario;      //comentario da imagem
    protected String dimensao;        //armazena a dimensao da imagem
    protected String nivelMax;        //valor maximo que os pixels pode assumir 
    protected int dimY;               //dimensaoY altura da imagem 
    protected int dimX;               //dimensaoX largura da imagem

    public Imagem() {
    }

    public Imagem(String numMagico, String comentario, String dimensao, String nivelMax) {
        this.numMagico = numMagico;
        this.comentario = comentario;
        this.dimensao = dimensao;
        this.nivelMax = nivelMax;
    }

    public String getNumMagico() {
        return numMagico;
    }

    public void setNumMagico(String numMagico) {
        this.numMagico = numMagico;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getDimensao() {
        return dimensao;
    }

    public void setDimensao(String dimensao) {
        this.dimensao = dimensao;
    }

    public String getNivelMax() {
        return nivelMax;
    }

    public void setNivelMax(String nivelMax) {
        this.nivelMax = nivelMax;
    }

    public int getDimY() {
        return dimY;
    }

    public void setDimY(int dimY) {
        this.dimY = dimY;
    }

    public int getDimX() {
        return dimX;
    }

    public void setDimX(int dimX) {
        this.dimX = dimX;
    }
                
} // Imagem 
