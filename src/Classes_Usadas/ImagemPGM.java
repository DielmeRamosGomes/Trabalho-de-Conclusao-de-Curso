/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes_Usadas;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author DIELME
 */
public class ImagemPGM extends Imagem {

    // matriz que vai armazenar os valores dos pixels na imagem em tons de cinza
    private int[][] matriz;
    private int[][] A_linha;

    public ImagemPGM() {
        super();
    }

    public ImagemPGM(String numMagico, String comentario, String dimensao, String nivelMax, int[][] matriz) {
        super(numMagico, comentario, dimensao, nivelMax);

        this.matriz = matriz;
        //vetor que guarda o tamanho da imagem dimX e dimY
        String tamanho[] = dimensao.split(" ");

        super.setDimX(Integer.parseInt(tamanho[1]));
        super.setDimY(Integer.parseInt(tamanho[0]));
    }

    public int[][] getA_linha() {
        return A_linha;
    }

    public void setA_linha(int[][] A_linha) {
        this.A_linha = A_linha;
    }

    public int[][] getMatriz() {
        return matriz;
    }

    public void setMatriz(int[][] matriz) {
        this.matriz = matriz;
    }

    BufferedImage buffer;

    public void lerImagemPGM(String arquivo) throws FileNotFoundException, IOException {

        //carrega o arquivo especificado pelo caminho
        File inputFile = new File(arquivo);

        Scanner in = new Scanner(inputFile);

        //lê cada linha do arquivo e atribui valores nos atriutos da classe
        numMagico = in.nextLine();
        comentario = in.nextLine();
        dimensao = in.nextLine();
        nivelMax = in.nextLine();

        //vetor que contem a dimensão da imagem separada
        String tamanho[] = dimensao.split(" ");

        //atribuiu a dimensão da imagem para os atributos dimX e dimY
        super.setDimX(Integer.parseInt(tamanho[1]));
        super.setDimY(Integer.parseInt(tamanho[0]));

        int linha, coluna;
        int dimY = super.getDimY();
        int dimX = super.getDimX();

        matriz = new int[dimX][dimY];

        while (in.hasNext()) {
            for (linha = 0; linha < dimX; linha++) {
                for (coluna = 0; coluna < dimY; coluna++) {

                    // le os numeros inteiros que representam os pixels e atribui na matriz
                    matriz[linha][coluna] = in.nextInt();
                }
            }
        }

        in.close();

    } // lerImagemPGM

    public void lerImagemA_linhaPGM(String arquivo) throws FileNotFoundException, IOException {

        //carrega o arquivo especificado pelo caminho
        File inputFile = new File(arquivo);

        Scanner in = new Scanner(inputFile);

        int linha, coluna;
        int dimY = super.getDimY();
        int dimX = super.getDimX();

        A_linha = new int[dimX][dimY];

        in.nextLine(); // ignorando as 4 primeiras linhas 
        in.nextLine();
        in.nextLine();
        in.nextLine();

        while (in.hasNext()) {
            for (linha = 0; linha < dimX; linha++) {
                for (coluna = 0; coluna < dimY; coluna++) {

                    // le os numeros inteiros que representam os pixels e atribui na matriz
                    A_linha[linha][coluna] = in.nextInt();
                }
            }
        }

        in.close();

    } // lerImagemA_linhaPGM

    public void salvarImagemPGM(String arquivo) throws IOException {
        File outputFile = new File(arquivo);
        FileWriter out = new FileWriter(outputFile);

        //escreve o cabeçalho da imagem no arquivo
        out.write(numMagico + "\n" + "# Criado por Dielme Ramos Gomes \n" + dimY + " " + dimX + "\n" + nivelMax + "\n");

        for (int linha = 0; linha < dimX; linha++) {
            for (int coluna = 0; coluna < dimY; coluna++) {
                //escreve a matriz no arquivo
                out.write(String.valueOf(matriz[linha][coluna]) + " ");
            }
            out.write("\n");
        }

        out.close();

    } // salvarImagemPGM

    public void salvarImagemA_linhaPGM(String arquivo) throws IOException {
        File outputFile = new File(arquivo);
        FileWriter out = new FileWriter(outputFile);

        //escreve o cabeçalho da imagem no arquivo
        out.write(numMagico + "\n" + "# Criado por Dielme Ramos Gomes \n" + dimY + " " + dimX + "\n" + nivelMax + "\n");

        for (int linha = 0; linha < dimX; linha++) {
            for (int coluna = 0; coluna < dimY; coluna++) {
                //escreve a matriz no arquivo
                out.write(String.valueOf(A_linha[linha][coluna]) + " ");
            }
            out.write("\n");
        }

        out.close();

    } // salvarImagemPGM

    public BufferedImage buffer() {
        BufferedImage buffer;
        //dimensões da imagem
        int largura = getDimX();
        int altura = getDimY();

        //especifica a dimensão do BufferedImage e o tipo de escala de cores para cinza
        buffer = new BufferedImage(altura, largura, BufferedImage.TYPE_BYTE_GRAY);

        //obtem o modelo de cores
        ColorModel modelo_cores = buffer.getColorModel();

        //fator que converte imagem com niveis diferentes de tons para os 255
        //tons de cinza especificos
        float fator = (255 / Integer.valueOf(this.nivelMax));

        int f;

        for (int i = 0; i < getDimX(); i++) {
            for (int j = 0; j < getDimY(); j++) {
                //converte o pixel para a escala de cinza padrão
                f = Math.round(getMatriz()[i][j] * fator);
                //atribui o tom de cinza ao modelo
                int rgb = modelo_cores.getRGB(f);
                //atribui o tom de cinza ao buffer
                buffer.setRGB(j, i, rgb);
            }
        }
        return buffer;

    } // buffer

    @Override
    public ImagemPGM clone() {
        int[][] novaMatriz = new int[dimX][dimY];

        for (int i = 0; i < dimX; i++) {
            for (int j = 0; j < dimY; j++) {

                //copia a matriz da imagem original para a copia
                novaMatriz[i][j] = matriz[i][j];
            }
        }

        //cria uma nova imagem PGM
        ImagemPGM novoPGM = new ImagemPGM(numMagico, comentario, dimensao, nivelMax, novaMatriz);
        String tamanho[] = dimensao.split(" ");

        novoPGM.setDimX(Integer.parseInt(tamanho[1]));
        novoPGM.setDimY(Integer.parseInt(tamanho[0]));

        return novoPGM;

    } // clone

} // ImagemPGM
