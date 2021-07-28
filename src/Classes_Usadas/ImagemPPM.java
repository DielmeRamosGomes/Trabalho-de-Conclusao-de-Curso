/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes_Usadas;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author DIELME
 */
public class ImagemPPM extends Imagem {

    // matrizes que armazenam os valores dos pixels da imagem no modelo RGB
    private int[][] R;
    private int[][] G;
    private int[][] B;

    private int[][] A_linhaR;
    private int[][] A_linhaG;
    private int[][] A_linhaB;

    public ImagemPPM() {
        super();
    }

    public ImagemPPM(String numMagico, String comentario, String dimensao, String nivelMax, int[][] R, int[][] G, int[][] B) {
        super(numMagico, comentario, dimensao, nivelMax);
        this.R = R;
        this.G = G;
        this.B = B;

        String tamanho[] = dimensao.split(" ");

        setDimX(Integer.parseInt(tamanho[1]));
        setDimY(Integer.parseInt(tamanho[0]));

    } // Construtor

    public int[][] getA_linhaR() {
        return A_linhaR;
    }

    public void setA_linhaR(int[][] A_linhaR) {
        this.A_linhaR = A_linhaR;
    }

    public int[][] getA_linhaG() {
        return A_linhaG;
    }

    public void setA_linhaG(int[][] A_linhaG) {
        this.A_linhaG = A_linhaG;
    }

    public int[][] getA_linhaB() {
        return A_linhaB;
    }

    public void setA_linhaB(int[][] A_linhaB) {
        this.A_linhaB = A_linhaB;
    }

    public int[][] getR() {
        return R;
    }

    public void setR(int[][] R) {
        this.R = R;
    }

    public int[][] getG() {
        return G;
    }

    public void setG(int[][] G) {
        this.G = G;
    }

    public int[][] getB() {
        return B;
    }

    public void setB(int[][] B) {
        this.B = B;
    }

    public void lerImagemPPM(String arquivo) throws FileNotFoundException, IOException {

        //carrega o arquivo especificado pelo caminho
        File inputFile = new File(arquivo);
        Scanner in = new Scanner(inputFile);

        //lê cada linha do arquivo e atribui aos atriutos da classe
        setNumMagico(in.nextLine());   // número magico
        setComentario(in.nextLine());  // comentario
        dimensao = in.nextLine();      // obtendo dimensão da imagem

        String tamanho[] = dimensao.split(" ");

        //atribuiu a dimensão da imagem para os atributos dimX e dimY
        dimY = Integer.parseInt(tamanho[0]);
        dimX = Integer.parseInt(tamanho[1]);

        setNivelMax(in.nextLine());  // nivel maximo que os valores podem assumir            

        R = new int[dimX][dimY];
        G = new int[dimX][dimY];
        B = new int[dimX][dimY];

        while (in.hasNext()) {
            for (int linha = 0; linha < dimX; linha++) {
                for (int coluna = 0; coluna < dimY; coluna++) {
                    //lê cada numero inteiro que representa os pixels da imagem e atribui a matriz

                    R[linha][coluna] = in.nextInt();
                    G[linha][coluna] = in.nextInt();
                    B[linha][coluna] = in.nextInt();
                }
            }
        } // fim while 

        in.close();

    } // lerImagemPPM

    public void lerImagemA_linhaPPM(String arquivo) throws FileNotFoundException, IOException {

        //carrega o arquivo especificado pelo caminho
        File inputFile = new File(arquivo);
        Scanner in = new Scanner(inputFile);

        A_linhaR = new int[dimX][dimY];
        A_linhaG = new int[dimX][dimY];
        A_linhaB = new int[dimX][dimY];

        in.nextLine(); // ignorando as primeiras quatro linhas 
        in.nextLine();
        in.nextLine();
        in.nextLine();

        while (in.hasNext()) {
            for (int linha = 0; linha < dimX; linha++) {
                for (int coluna = 0; coluna < dimY; coluna++) {
                    //lê cada numero inteiro que representa os pixels da imagem e atribui a matriz

                    A_linhaR[linha][coluna] = in.nextInt();
                    A_linhaG[linha][coluna] = in.nextInt();
                    A_linhaB[linha][coluna] = in.nextInt();
                }
            }
        } // fim while 

        in.close();

    } // lerImagemDoublePPM

    public void SalvarImagemPPM(String arquivo) throws IOException {
        File outputFile = new File(arquivo); // //cria arquivo

        FileWriter out = new FileWriter(outputFile);  //cria variavel que ira escrever no arquivo

        //escreve o cabeçalho no arquivo
        out.write(numMagico + "\n" + "# CREATOR: Dielme Ramos Gomes \n" + dimY + " " + dimX + "\n" + nivelMax + "\n");

        //numMagico = "P3";
        for (int linha = 0; linha < dimX; linha++) {
            for (int coluna = 0; coluna < dimY; coluna++) {

                //escreve os pixels nas matrizes  R,G e B no arquivo
                out.write(String.valueOf(R[linha][coluna]) + " ");

                out.write(String.valueOf(G[linha][coluna]) + " ");

                out.write(String.valueOf(B[linha][coluna]) + " ");
            }
            out.write("\n");
        }

        out.close();

    } // SalvarImagemPPM

    public void SalvarImagemA_linhaPPM(String arquivo) throws IOException {
        File outputFile = new File(arquivo); // //cria arquivo

        FileWriter out = new FileWriter(outputFile);  //cria variavel que ira escrever no arquivo

        //escreve o cabeçalho no arquivo
        out.write(numMagico + "\n" + "# CREATOR: Dielme Ramos Gomes \n" + dimY + " " + dimX + "\n" + nivelMax + "\n");

        for (int linha = 0; linha < dimX; linha++) {
            for (int coluna = 0; coluna < dimY; coluna++) {

                //escreve os pixels nas matrizes  R,G e B no arquivo
                out.write(String.valueOf(A_linhaR[linha][coluna]) + " ");

                out.write(String.valueOf(A_linhaG[linha][coluna]) + " ");

                out.write(String.valueOf(A_linhaB[linha][coluna]) + " ");
            }
            out.write("\n");
        }

        out.close();

    } // SalvarImagemPPM

    public BufferedImage buffer() {
        BufferedImage buffer;

        //dimenões da imagem
        int largura = getDimX();
        int altura = getDimY();

        //especifica a dimensão do BufferedImage e o tipo de escala RGB
        buffer = new BufferedImage(altura, largura, BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < getDimX(); i++) {
            for (int j = 0; j < getDimY(); j++) {

                int rgb = ((R[i][j] << 16) + (G[i][j] << 8) + B[i][j]);

                //atribui o tom RGB ao buffer        
                buffer.setRGB(j, i, rgb);

            }
        }
        return buffer;

    } // buffer

    public ImagemPPM clone() {

        int[][] R = new int[dimX][dimY];
        int[][] G = new int[dimX][dimY];
        int[][] B = new int[dimX][dimY];

        for (int i = 0; i < dimX; i++) {
            for (int j = 0; j < dimY; j++) {
                R[i][j] = this.R[i][j];
                G[i][j] = this.G[i][j];
                B[i][j] = this.B[i][j];
            }
        }

        ImagemPPM novoPPM = new ImagemPPM(numMagico, comentario, dimensao, nivelMax, R, G, B);
        String tamanho[] = dimensao.split(" ");

        novoPPM.setDimX(Integer.parseInt(tamanho[1]));
        novoPPM.setDimY(Integer.parseInt(tamanho[0]));

        return novoPPM;

    } // Clone

} // Classe ImagemPPM
