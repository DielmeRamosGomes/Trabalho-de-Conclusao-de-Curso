/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Classes_Usadas.Imagem;
import Classes_Usadas.ImagemPGM;
import Classes_Usadas.ImagemPPM;
import com.sun.javafx.scene.control.skin.VirtualFlow.ArrayLinkedList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author DIELME
 */
public class Controlador {

    //paramentros utilizados para armazenar as imagens do tipo PPM e PGM
    public ImagemPGM pgm = new ImagemPGM();
    public ImagemPPM ppm = new ImagemPPM();

    public Controlador() {
    }

    private int[][] matriz_Quantizacao = new int[8][8];    // matriz de quantização 
    private int[][] valor_QuantizadoR;     // matriz que vai guardar os valores quantizados 
    private int[][] valor_QuantizadoG;     // matriz que vai guardar os valores quantizados 
    private int[][] valor_QuantizadoB;     // matriz que vai guardar os valores quantizados
    private double[][] retR;            // matriz resultante R 
    private double[][] retG;            // matriz resultante G 
    private double[][] retB;            // matriz resultante B
    private int[][] inversa_retR;            // matriz inversa inteira do resultado
    private int[][] inversa_retG;            // matriz inversa inteira do resultado
    private int[][] inversa_retB;            // matriz inversa inteira do resultado
    private int posj = 0;  // cada posj é um caracter em binario
    private int posj1 = 0;  // cada posj é um caracter em binario
    private int posk = 0; // cada posk é um numero do caracter que foi passado para binario 
    private int posk1 = 0; // cada posk é um numero do caracter que foi passado para binario 

    // teste
    private double[][] in_retR;            // matriz resultante  in R 
    private double[][] in_retG;            // matriz resultante in G 
    private double[][] in_retB;            // matriz resultante in B

    private int[][] A_linhaR;
    private int[][] A_linhaG;
    private int[][] A_linhaB;

    public ImagemPPM Tecnica_LSB_PPM(Imagem img, String mensagem) throws IOException {
        String[] vetBinario;
        ImagemPPM imagem = (ImagemPPM) img;

        int[][] R = imagem.getR(); // matrizes que representam a terna de cores vermelho verde azul
        int[][] G = imagem.getG();
        int[][] B = imagem.getB();

        ImagemPPM novaImagem = imagem.clone(); // copia da imagem 

        int[][] novaR = novaImagem.getR();
        int[][] novaG = novaImagem.getG();
        int[][] novaB = novaImagem.getB();

        vetBinario = Converte_Mensagem_ASCII_Binario(mensagem); // converte a mensagem para numeros ascii e depois de ascii para binarios

        int k = 0;
        int j = 0;

        for (int linha = 0; linha < novaImagem.getDimX(); linha++) {
            for (int coluna = 0; coluna < novaImagem.getDimY(); coluna++) {

                if (vetBinario[j] != null) { // Verifica se a mensagem acabou

                    String numBinR = Converte_Decimal_Binario(R[linha][coluna]);
                    String numBinG = Converte_Decimal_Binario(G[linha][coluna]);
                    String numBinB = Converte_Decimal_Binario(B[linha][coluna]);

                    if (k < 8) { // verifica se a string chegou no fim 

                        if (vetBinario[j].charAt(k) != numBinR.charAt(numBinR.length() - 1)) {
                            k++;

                            if (numBinR.charAt(numBinR.length() - 1) == '0') {
                                numBinR = numBinR.substring(0, numBinR.length() - 1) + "1";
                            } else {
                                numBinR = numBinR.substring(0, numBinR.length() - 1) + "0";
                            }

                            int numDecR = Converte_Binario_Decimal(numBinR);
                            novaR[linha][coluna] = numDecR;

                        } else {
                            int numDecR = Converte_Binario_Decimal(numBinR);
                            novaR[linha][coluna] = numDecR;
                            k++;
                        }
                    } else {
                        j++;
                        k = 0;
                    }

                    if (k < 8) { // verifica se a string chegou no fim

                        if (vetBinario[j].charAt(k) != numBinG.charAt(numBinG.length() - 1)) {
                            k++;

                            if (numBinG.charAt(numBinG.length() - 1) == '0') {
                                numBinG = numBinG.substring(0, numBinG.length() - 1) + "1";
                            } else {
                                numBinG = numBinG.substring(0, numBinG.length() - 1) + "0";
                            }

                            int numDecG = Converte_Binario_Decimal(numBinG);
                            novaG[linha][coluna] = numDecG;

                        } else {
                            int numDecG = Converte_Binario_Decimal(numBinG);
                            novaG[linha][coluna] = numDecG;
                            k++;
                        }
                    } else {
                        j++;
                        k = 0;
                    }

                    if (k < 8) { // verifica se a string chegou no fim

                        if (vetBinario[j].charAt(k) != numBinB.charAt(numBinB.length() - 1)) {
                            k++;

                            if (numBinB.charAt(numBinB.length() - 1) == '0') {
                                numBinB = numBinB.substring(0, numBinB.length() - 1) + "1";
                            } else {
                                numBinB = numBinB.substring(0, numBinB.length() - 1) + "0";
                            }

                            int numDecB = Converte_Binario_Decimal(numBinB);
                            novaB[linha][coluna] = numDecB;

                        } else {
                            int numDecB = Converte_Binario_Decimal(numBinB);
                            novaB[linha][coluna] = numDecB;
                            k++;
                        }
                    } else {
                        j++;
                        k = 0;
                    }

                } else {
                    break;
                }
            }
        }
        return novaImagem;
    } // Tecnica_LSB_PPM

    public ImagemPGM Tecnica_Filtragem_Mascaramento_PGM(Imagem img, String mensagem) throws IOException {
        String[] vetBinario;
        ImagemPGM imagem = (ImagemPGM) img;

        int[][] matriz = imagem.getMatriz(); // matrizes que representa a matriz de pixels da imagem

        ImagemPGM novaImagem = imagem.clone(); // copia da imagem 

        int[][] novaMatriz = novaImagem.getMatriz();

        vetBinario = Converte_Mensagem_ASCII_Binario(mensagem); // converte a mensagem para numeros ascii e depois de ascii para binarios

        int k = 0;
        int j = 0;

        for (int linha = 0; linha < novaImagem.getDimX(); linha++) {
            for (int coluna = 0; coluna < novaImagem.getDimY(); coluna++) {

                if (vetBinario[j] != null) { // Verifica se a mensagem acabou

                    String numBin = Converte_Decimal_Binario(matriz[linha][coluna]);

                    if (k < 8) { // verifica se a string chegou no fim 

                        if (vetBinario[j].charAt(k) != numBin.charAt(0)) { // Verificando se o primeiro bit mais significativo do pixel é diferente do bit da letra convertida em binario 
                            k++;

                            if (numBin.charAt(0) == '0') {
                                numBin = "1" + numBin.substring(1, numBin.length());
                            } else {
                                numBin = "0" + numBin.substring(1, numBin.length());
                            }

                            int numDec = Converte_Binario_Decimal(numBin);
                            novaMatriz[linha][coluna] = numDec;

                        } else {
                            int numDec = Converte_Binario_Decimal(numBin);
                            novaMatriz[linha][coluna] = numDec;
                            k++;
                        }
                    } else {
                        j++;
                        k = 0;
                    }

                } else {
                    break;
                }
            }
        }
        return novaImagem;
    } // Tecnica_Filtragem_Mascaramento_PGM

    public String Converte_Decimal_Binario(int num) {
        String binario = "";
        ArrayLinkedList lista = new ArrayLinkedList();
        for (int i = 0; i < 8; i++) {
            lista.addFirst(num % 2);
            num = num / 2;
        }
        for (int j = 0; j < lista.size(); j++) {
            binario += lista.get(j).toString();
        }
        return binario;
    } // Converte_Decimal_Binario

    public String[] Converte_Mensagem_ASCII_Binario(String mensagem) {
        String[] vetBinario = new String[1000]; // vetor onde cada posicao é uma letra da mensagem codificada de decimal para binario 
        //mensagem = mensagem.replaceAll("\\.", ""); // retirando espaços em branco 

        byte[] mensagemByte = mensagem.getBytes(); // convertendo mensagem para padrao ascii

        for (int i = 0; i < mensagemByte.length; i++) {
            vetBinario[i] = Converte_Decimal_Binario((mensagemByte[i]));
        }

        return vetBinario;
    } // Converte_Mensagem_ASCII_Binario

    public int Converte_Binario_Decimal(String binario) {
        int potencia = 0;
        int decimal = 0;
        for (int i = binario.length() - 1; i >= 0; i--) {
            decimal += Math.pow(2, potencia) * Character.getNumericValue(binario.charAt(i));
            potencia++;
        }
        return decimal;
    } // Converte_Binario_Decimal

    public String Converte_ASCII_String(ArrayList lista) {
        String mensagem = "";

        for (int i = 0; i < lista.size(); i++) {
            int num1 = Converte_Binario_Decimal((String) lista.get(i));

            if ((num1 >= 48 && num1 <= 57) || (num1 >= 65 && num1 <= 90) || (num1 >= 97 && num1 <= 122) || (num1 == 32) || (num1 == 10) || (num1 == 13)) {
                char ch = (char) num1;
                mensagem += ch;
            } else {
                break;
            }
        }

        return mensagem;
    } // Converte_ASCII_String

    public String Codificar_LSB(Imagem img) {
        ArrayList lista = new ArrayList();
        ArrayList vetBinario = new ArrayList<>();
        String mensagem;

        ImagemPPM imagem = (ImagemPPM) img;

        int[][] R = imagem.getR(); // matrizes que representam a terna de cores vermelho verde azul
        int[][] G = imagem.getG();
        int[][] B = imagem.getB();

        ImagemPPM novaImagem = imagem.clone(); // copia da imagem 

        for (int linha = 0; linha < novaImagem.getDimX(); linha++) {
            for (int coluna = 0; coluna < novaImagem.getDimY(); coluna++) {

                String numBinR = Converte_Decimal_Binario(R[linha][coluna]);
                String numBinG = Converte_Decimal_Binario(G[linha][coluna]);
                String numBinB = Converte_Decimal_Binario(B[linha][coluna]);

                lista.add(numBinR.charAt(numBinR.length() - 1));     // Pegando ultimo bit de cada caractere de cada pixel
                lista.add(numBinG.charAt(numBinG.length() - 1));
                lista.add(numBinB.charAt(numBinB.length() - 1));
            }
        }

        char[] aux = new char[8];
        int j = 0;

        for (int i = 0; i < lista.size(); i++) {             // andando por todos os caracteres e formando a letra com 8 bits         
            if (j < 8) {
                aux[j] = (char) lista.get(i);
                j++;
            } else {
                String bin = String.copyValueOf(aux);     // passando o vetor de caracter para string 
                vetBinario.add(bin);
                j = 0;
            }
        }

        mensagem = Converte_ASCII_String(vetBinario);

        return mensagem;
    }// Codificar_LSB

    public String Codificar_Filtragem_Mascaramento(Imagem img) {
        ArrayList lista = new ArrayList();
        ArrayList vetBinario = new ArrayList<>();
        String mensagem;

        ImagemPGM imagem = (ImagemPGM) img;

        int[][] matriz = imagem.getMatriz(); // matrizes que representa a matriz de pixels da imagem

        ImagemPGM novaImagem = imagem.clone(); // copia da imagem 

        for (int linha = 0; linha < novaImagem.getDimX(); linha++) {
            for (int coluna = 0; coluna < novaImagem.getDimY(); coluna++) {

                String numBin = Converte_Decimal_Binario(matriz[linha][coluna]);
                lista.add(numBin.charAt(0));                  // Pegando primeiro caracter de cada pixel
            }
        }

        char[] aux = new char[8];
        int j = 0;

        for (int i = 0; i < lista.size(); i++) {             // andando por todos os caracteres e formando a letra com 8 bits         
            if (j < 8) {
                aux[j] = (char) lista.get(i);
                j++;
            } else {
                String bin = String.copyValueOf(aux);     // passando o vetor de caracter para string 
                vetBinario.add(bin);
                j = 0;
            }
        }

        mensagem = Converte_ASCII_String(vetBinario);

        return mensagem;
    }// Codificar_Filtragem_Mascaramento

    public String Codificar_LSB_TD(ImagemPPM imagem) {
        ArrayList lista = new ArrayList();
        ArrayList vetBinario = new ArrayList<>();
        String mensagem;

        int[][] R = imagem.getR(); // matrizes que representam a terna de cores vermelho verde azul
        int[][] G = imagem.getG();
        int[][] B = imagem.getB();

        ImagemPPM novaImagem = imagem.clone(); // copia da imagem 

        for (int linha = 0; linha < novaImagem.getDimX(); linha++) {
            for (int coluna = 0; coluna < novaImagem.getDimY(); coluna++) {

                String numBinR = Converte_Decimal_Binario(R[linha][coluna]);
                String numBinG = Converte_Decimal_Binario(G[linha][coluna]);
                String numBinB = Converte_Decimal_Binario(B[linha][coluna]);

                lista.add(numBinR.charAt(numBinR.length() - 1));     // Pegando ultimo caracter de cada pixel
                lista.add(numBinG.charAt(numBinG.length() - 1));
                lista.add(numBinB.charAt(numBinB.length() - 1));
            }
        }

        char[] aux = new char[8];
        int j = 0;

        for (int i = 0; i < lista.size(); i++) {             // andando por todos os caracteres e formando a letra com 8 bits         
            if (j < 8) {
                aux[j] = (char) lista.get(i);
                j++;
            } else {
                String bin = String.copyValueOf(aux);     // passando o vetor de caracter para string 
                vetBinario.add(bin);
                j = 0;
            }
        }

        mensagem = Converte_ASCII_String(vetBinario);

        return mensagem;
    }// Codificar_LSB_TD

    public String Codificar_LSB_TD_BitLivre(ImagemPPM imagem) {
        ArrayList lista = new ArrayList();
        ArrayList vetBinario = new ArrayList<>();
        String mensagem;
        int pos = 4;
        int[][] R = imagem.getR(); // matrizes que representam a terna de cores vermelho verde azul
        int[][] G = imagem.getG();
        int[][] B = imagem.getB();

        ImagemPPM novaImagem = imagem.clone(); // copia da imagem 

        for (int linha = 0; linha < novaImagem.getDimX(); linha++) {
            for (int coluna = 0; coluna < novaImagem.getDimY(); coluna++) {

                String numBinR = Converte_Decimal_Binario(R[linha][coluna]);
                String numBinG = Converte_Decimal_Binario(G[linha][coluna]);
                String numBinB = Converte_Decimal_Binario(B[linha][coluna]);

                lista.add(numBinR.charAt(pos - 1));     // Pegando ultimo caracter de cada pixel
                lista.add(numBinG.charAt(pos - 1));
                lista.add(numBinB.charAt(pos - 1));
            }
        }

        char[] aux = new char[8];
        int j = 0;

        for (int i = 0; i < lista.size(); i++) {             // andando por todos os caracteres e formando a letra com 8 bits         
            if (j < 8) {
                aux[j] = (char) lista.get(i);
                j++;
            } else {
                String bin = String.copyValueOf(aux);     // passando o vetor de caracter para string 
                vetBinario.add(bin);
                j = 0;
            }
        }

        mensagem = Converte_ASCII_String(vetBinario);

        return mensagem;
    }// Codificar_LSB_TD

    public String Codificar_Transformacoes_Dominios(Imagem img) {
        String mensagem;
        ImagemPPM imagem = (ImagemPPM) img;
        ImagemPPM novaImagem;

        novaImagem = TDC_PPM_Sem_MSG(imagem); // aplicando a transformada e quantização para fazer a imagem voltar ao dominio da frequencia

        // como a quantização é usando LSB basta usarmos a codificação lsb para mostrar a mensagem ou texto codificado
        mensagem = Codificar_LSB_TD(novaImagem); // lsb
        //mensagem = Codificar_LSB_TD_BitLivre(novaImagem); // bit livre

        return mensagem;
    } // Codificar_Transformacoes_Dominios

    public String Ler_Arquivo_Texto(String arquivo) throws FileNotFoundException {

        //carrega o arquivo especificado pelo caminho
        File inputFile = new File(arquivo);

        Scanner in = new Scanner(inputFile);
        String mensagem = "";

        while (in.hasNext()) {
            mensagem += in.nextLine() + '\n';
        }

        return mensagem;

    } // Ler_Arquivo_Texto

    public ImagemPGM Transformada_Discreta_do_Cosseno_PGM(Imagem img, String mensagem) {
        ImagemPGM imagem = (ImagemPGM) img;
        ImagemPGM novaImagem = imagem.clone(); // copia da imagem 

        int qtdBL = (int) imagem.getDimX() / 8;
        int qtdBC = (int) imagem.getDimY() / 8;

        valor_QuantizadoR = new int[imagem.getDimX()][imagem.getDimY()];     // matriz que vai guardar os valores quantizados 

        retR = new double[imagem.getDimX()][imagem.getDimY()];            // matriz resultante R 

        for (int l = 0; l < qtdBL; l++) {
            for (int c = 0; c < qtdBC; c++) {
                TDC_Bloco_PGM(img, l, c);                   // o calculo da TDC é calculado em blocos de 8x8 pixels
            }
        }
        novaImagem.setMatriz(valor_QuantizadoR);

        try {
            novaImagem = Tecnica_LSB_PGM_DCT(novaImagem, mensagem);     // colocando a mensagem na ultima posição de cada bloco

        } catch (IOException ex) {
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
        }

        novaImagem = Transformada_Discreta_do_Cosseno_Inversa_PGM(novaImagem);

        return novaImagem;

    } // Transformada_Discreta_do_Cosseno_PGM

    public void TDC_Bloco_PGM(Imagem img, int l, int c) {
        ImagemPGM imagem = (ImagemPGM) img;
        int[][] matriz = imagem.getMatriz(); // matrizes da imagem

        int fator_Qualidade = 2;
        int tam = 8;
        double tmpR;

        for (int i = 0; i < tam; i++) {     // percorre a imagem 
            for (int j = 0; j < tam; j++) { // percorre a imagem 
                retR[i + l * 8][j + c * 8] = 0;
                for (int x = 0; x < tam; x++) {
                    for (int y = 0; y < tam; y++) {
                        tmpR = matriz[x + l * 8][y + c * 8];
                        tmpR *= Math.cos((2 * y + 1) * j * Math.PI / (2 * tam)); // calculo da TDC
                        tmpR *= Math.cos((2 * x + 1) * i * Math.PI / (2 * tam)); // calculo da TDC
                        retR[i + l * 8][j + c * 8] += tmpR; // cada ponto da matriz resultante é a soma dos produtos do cosseno de todos os valores de um bloco
                    }
                }
                retR[i + l * 8][j + c * 8] *= i == 0 ? 1.0 / Math.sqrt(tam) : Math.sqrt(2 / tam); //se o valor de i for 0, então o cálculo é 1 sobre a raiz quadrada do tamanho do bloco, se o valor de i for 1, então o cálculo é raiz quadrada de 2 sobre o tamanho do bloco
                retR[i + l * 8][j + c * 8] *= j == 0 ? 1.0 / Math.sqrt(tam) : Math.sqrt(2 / tam); //se o valor de j for 0, então o cálculo é 1 sobre a raiz quadrada do tamanho do bloco, se o valor de j for 1, então o cálculo é raiz quadrada de 2 sobre o tamanho do bloco

                //retR[i + l * 8][j + c * 8] *= 1.0 / Math.sqrt(2 * tam) ;   // resultado = resultado * 1/(raiz 2*tam)   
            }
        }

        // Quantização dos coeficientes 
        for (int i = 0; i < tam; i++) {
            for (int j = 0; j < tam; j++) {
                matriz_Quantizacao[i][j] = 1 + (1 + i + j) * fator_Qualidade;
                valor_QuantizadoR[i + l * 8][j + c * 8] = (int) Math.round(retR[i + l * 8][j + c * 8] / matriz_Quantizacao[i][j]);  // fazendo a quantização dos coeficientes R
                //System.out.print(valor_QuantizadoR[i + l * 8][j + c * 8] + " ");
            }
            //System.out.println("");
        }

    } // TDC_Bloco_PGM

    public ImagemPGM Transformada_Discreta_do_Cosseno_Inversa_PGM(ImagemPGM imagem) {
        ImagemPGM novaImagem = imagem.clone();

        int qtdBL = (int) imagem.getDimX() / 8;
        int qtdBC = (int) imagem.getDimY() / 8;

        inversa_retR = new int[imagem.getDimX()][imagem.getDimY()];             // matriz inversa inteira do resultado

        for (int l = 0; l < qtdBL; l++) {
            for (int c = 0; c < qtdBC; c++) {
                TDC_Bloco_Inversa_PGM(novaImagem, l, c);                   // o calculo da TDCI é calculado em blocos de 8x8 pixels
            }
        }
        novaImagem.setMatriz(inversa_retR);

        return novaImagem;

    } // Transformada_Discreta_do_Cosseno_Inversa_PGM

    public void TDC_Bloco_Inversa_PGM(ImagemPGM imagem, int l, int c) {
        int tam = 8;
        double tmpR;
        double[][] i_retR = new double[tam][tam];     // matriz resultante R inversa

        // Inversa da quantização 
        // Na inversa é feita primeiro a quantização inversa
        for (int i = 0; i < tam; i++) {
            for (int j = 0; j < tam; j++) {
                retR[i + l * 8][j + c * 8] = valor_QuantizadoR[i + l * 8][j + c * 8] * matriz_Quantizacao[i][j];  // matriz resultante recebe a operação inversa da quantização R
            }
        }
        for (int x = 0; x < tam; x++) {     // percorre a imagem 
            for (int y = 0; y < tam; y++) {   // percorre a imagem 
                i_retR[x][y] = 0;           // zera 
                for (int i = 0; i < tam; i++) {
                    for (int j = 0; j < tam; j++) {
                        tmpR = retR[i + l * 8][j + c * 8];
                        tmpR *= Math.cos((2 * y + 1) * j * Math.PI / (2 * tam)); // calculo da TDCI
                        tmpR *= Math.cos((2 * x + 1) * i * Math.PI / (2 * tam)); // calculo da TDCI
                        tmpR *= i == 0 ? 1.0 / Math.sqrt(tam) : Math.sqrt(2 / tam); //se o valor de i for 0, então o cálculo é 1 sobre a raiz quadrada do tamanho do bloco, se o valor de i for 1, então o cálculo é raiz quadrada de 2 sobre o tamanho do bloco
                        tmpR *= j == 0 ? 1.0 / Math.sqrt(tam) : Math.sqrt(2 / tam); //se o valor de j for 0, então o cálculo é 1 sobre a raiz quadrada do tamanho do bloco, se o valor de j for 1, então o cálculo é raiz quadrada de 2 sobre o tamanho do bloco
                        i_retR[x][y] += tmpR;  // cada ponto da matriz resultante é soma dos produtos do cosseno e dos coeficientes de todos os valores de um bloco
                    }
                }
                //i_retR[x][y] *= 1.0/4.0 ; 
            }
        }
        int[][] R = imagem.getMatriz();

        for (int i = 0; i < tam; i++) {
            for (int j = 0; j < tam; j++) {                           //atualiza os valores dos pixels na imagem
                /*System.out.print(Math.round(i_retR[i][j]) + " ");        //exibe o resultado
                System.out.print(Math.round(i_retG[i][j]) + " ");        //exibe o resultado
                System.out.print(Math.round(i_retB[i][j]) + " ");        //exibe o resultado
                 */
                if (i != 7 && j != 7) {
                    inversa_retR[i + l * 8][j + c * 8] = (int) Math.round(i_retR[i][j]);

                    if (i_retR[i][j] > 255) {
                        inversa_retR[i + l * 8][j + c * 8] = 255;
                    } else {
                        inversa_retR[i + l * 8][j + c * 8] = (int) Math.round(i_retR[i][j]);
                    }
                } else {
                    inversa_retR[i + l * 8][j + c * 8] = R[7 + l * 8][7 + c * 8]; // a ultima pos de cada bloco vai receber o pixel quantizado
                }
            }
            //System.out.println("");
        }

    } // TDC_Bloco_Inversa_PPM

    public ImagemPPM Transformada_Discreta_do_Cosseno_PPM(Imagem img, String mensagem) {
        ImagemPPM imagem = (ImagemPPM) img;
        ImagemPPM novaImagem = imagem.clone(); // copia da imagem 

        int qtdBL = (int) imagem.getDimX() / 8;
        int qtdBC = (int) imagem.getDimY() / 8;

        valor_QuantizadoR = new int[imagem.getDimX()][imagem.getDimY()];     // matriz que vai guardar os valores quantizados 
        valor_QuantizadoG = new int[imagem.getDimX()][imagem.getDimY()];     // matriz que vai guardar os valores quantizados 
        valor_QuantizadoB = new int[imagem.getDimX()][imagem.getDimY()];     // matriz que vai guardar os valores quantizados

        retR = new double[imagem.getDimX()][imagem.getDimY()];            // matriz resultante R 
        retG = new double[imagem.getDimX()][imagem.getDimY()];            // matriz resultante G 
        retB = new double[imagem.getDimX()][imagem.getDimY()];            // matriz resultante B

        for (int l = 0; l < qtdBL; l++) {
            for (int c = 0; c < qtdBC; c++) {
                //TDC_Bloco_PPM(img, vetBinario, l, c);         // o calculo da TDC é calculado em blocos de 8x8 pixels
                TDC_Bloco_PPM(img, l, c);                   // o calculo da TDC é calculado em blocos de 8x8 pixels
            }
        }
        novaImagem.setR(valor_QuantizadoR);
        novaImagem.setG(valor_QuantizadoG);
        novaImagem.setB(valor_QuantizadoB);

        try {
            //novaImagem = Tecnica_LSB_PPM_TDC_BitLivre(novaImagem, mensagem);     // bit em qualquer pos
            //novaImagem = Tecnica_LSB_PPM_TDC(novaImagem, mensagem);        // lsb
            novaImagem = Tecnica_LSB_PPM_TDC_Teste(novaImagem, mensagem);     // colocando a mensagem na ultima posição de cada bloco
        } catch (IOException ex) {
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
        }

        novaImagem = Transformada_Discreta_do_Cosseno_Inversa_PPM(novaImagem);

        return novaImagem;

    } // Transformada_Discreta_do_Cosseno_PPM

    public void TDC_Bloco_PPM(Imagem img, int l, int c) {
        ImagemPPM imagem = (ImagemPPM) img;

        int[][] R = imagem.getR(); // matrizes que representam a terna de cores vermelho verde azul
        int[][] G = imagem.getG();
        int[][] B = imagem.getB();

        int fator_Qualidade = 2;
        int tam = 8;
        double tmpR;
        double tmpG;
        double tmpB;

        for (int i = 0; i < tam; i++) {     // percorre a imagem 
            for (int j = 0; j < tam; j++) { // percorre a imagem 
                retR[i + l * 8][j + c * 8] = 0;
                retG[i + l * 8][j + c * 8] = 0;
                retB[i + l * 8][j + c * 8] = 0;
                for (int x = 0; x < tam; x++) {
                    for (int y = 0; y < tam; y++) {
                        tmpR = R[x + l * 8][y + c * 8];
                        tmpR *= Math.cos((2 * y + 1) * j * Math.PI / (2 * tam)); // calculo da TDC
                        tmpR *= Math.cos((2 * x + 1) * i * Math.PI / (2 * tam)); // calculo da TDC
                        retR[i + l * 8][j + c * 8] += tmpR; // cada ponto da matriz resultante é a soma dos produtos do cosseno de todos os valores de um bloco

                        tmpG = G[x + l * 8][y + c * 8];
                        tmpG *= Math.cos((2 * y + 1) * j * Math.PI / (2 * tam)); // calculo da TDC
                        tmpG *= Math.cos((2 * x + 1) * i * Math.PI / (2 * tam)); // calculo da TDC
                        retG[i + l * 8][j + c * 8] += tmpG; // cada ponto da matriz resultante é a soma dos produtos do cosseno de todos os valores de um bloco

                        tmpB = B[x + l * 8][y + c * 8];
                        tmpB *= Math.cos((2 * y + 1) * j * Math.PI / (2 * tam)); // calculo da TDC
                        tmpB *= Math.cos((2 * x + 1) * i * Math.PI / (2 * tam)); // calculo da TDC
                        retB[i + l * 8][j + c * 8] += tmpB; // cada ponto da matriz resultante é a soma dos produtos do cosseno de todos os valores de um bloco
                    }
                }
                retR[i + l * 8][j + c * 8] *= i == 0 ? 1.0 / Math.sqrt(tam) : Math.sqrt(2 / tam); //se o valor de i for 0, então o cálculo é 1 sobre a raiz quadrada do tamanho do bloco, se o valor de i for 1, então o cálculo é raiz quadrada de 2 sobre o tamanho do bloco
                retR[i + l * 8][j + c * 8] *= j == 0 ? 1.0 / Math.sqrt(tam) : Math.sqrt(2 / tam); //se o valor de j for 0, então o cálculo é 1 sobre a raiz quadrada do tamanho do bloco, se o valor de j for 1, então o cálculo é raiz quadrada de 2 sobre o tamanho do bloco

                retG[i + l * 8][j + c * 8] *= i == 0 ? 1.0 / Math.sqrt(tam) : Math.sqrt(2 / tam); //se o valor de i for 0, então o cálculo é 1 sobre a raiz quadrada do tamanho do bloco, se o valor de i for 1, então o cálculo é raiz quadrada de 2 sobre o tamanho do bloco
                retG[i + l * 8][j + c * 8] *= j == 0 ? 1.0 / Math.sqrt(tam) : Math.sqrt(2 / tam); //se o valor de j for 0, então o cálculo é 1 sobre a raiz quadrada do tamanho do bloco, se o valor de j for 1, então o cálculo é raiz quadrada de 2 sobre o tamanho do bloco

                retB[i + l * 8][j + c * 8] *= i == 0 ? 1.0 / Math.sqrt(tam) : Math.sqrt(2 / tam); //se o valor de i for 0, então o cálculo é 1 sobre a raiz quadrada do tamanho do bloco, se o valor de i for 1, então o cálculo é raiz quadrada de 2 sobre o tamanho do bloco
                retB[i + l * 8][j + c * 8] *= j == 0 ? 1.0 / Math.sqrt(tam) : Math.sqrt(2 / tam); //se o valor de j for 0, então o cálculo é 1 sobre a raiz quadrada do tamanho do bloco, se o valor de j for 1, então o cálculo é raiz quadrada de 2 sobre o tamanho do bloco

                /*retR[i + l * 8][j + c * 8] *= 1.0 / Math.sqrt(2 * tam) ;   // resultado = resultado * 1/(raiz 2*tam)
                retG[i + l * 8][j + c * 8] *= 1.0 / Math.sqrt(2 * tam) ;   // resultado = resultado * 1/(raiz 2*tam)
                retB[i + l * 8][j + c * 8] *= 1.0 / Math.sqrt(2 * tam) ;   // resultado = resultado * 1/(raiz 2*tam)
                 */
            }
        }

        // subtitui valor do pixel [7,7] por 0 > +0.0005
        // se for 1 > 0.005
        // nao quantizar mais 
        // Quantização dos coeficientes 
        for (int i = 0; i < tam; i++) {
            for (int j = 0; j < tam; j++) {
                matriz_Quantizacao[i][j] = 1 + (1 + i + j) * fator_Qualidade;
                valor_QuantizadoR[i + l * 8][j + c * 8] = (int) Math.round(retR[i + l * 8][j + c * 8] / matriz_Quantizacao[i][j]);  // fazendo a quantização dos coeficientes R
                valor_QuantizadoG[i + l * 8][j + c * 8] = (int) Math.round(retG[i + l * 8][j + c * 8] / matriz_Quantizacao[i][j]);  // fazendo a quantização dos coeficientes G
                valor_QuantizadoB[i + l * 8][j + c * 8] = (int) Math.round(retB[i + l * 8][j + c * 8] / matriz_Quantizacao[i][j]);  // fazendo a quantização dos coeficientes B
                //System.out.print(valor_QuantizadoR[i + l * 8][j + c * 8] + " ");
            }
            //System.out.println("");
        }

    } // TDC_Bloco_PPM

    public ImagemPPM Transformada_Discreta_do_Cosseno_Inversa_PPM(ImagemPPM imagem) {
        ImagemPPM novaImagem = imagem.clone();

        int qtdBL = (int) imagem.getDimX() / 8;
        int qtdBC = (int) imagem.getDimY() / 8;

        inversa_retR = new int[imagem.getDimX()][imagem.getDimY()];             // matriz inversa inteira do resultado
        inversa_retG = new int[imagem.getDimX()][imagem.getDimY()];             // matriz inversa inteira do resultado
        inversa_retB = new int[imagem.getDimX()][imagem.getDimY()];             // matriz inversa inteira do resultado

        for (int l = 0; l < qtdBL; l++) {
            for (int c = 0; c < qtdBC; c++) {
                TDC_Bloco_Inversa_PPM(novaImagem, l, c);                   // o calculo da TDCI é calculado em blocos de 8x8 pixels

            }
        }
        novaImagem.setR(inversa_retR);
        novaImagem.setG(inversa_retG);
        novaImagem.setB(inversa_retB);

        return novaImagem;

    } // Transformada_Discreta_do_Cosseno_Inversa_PPM

    public void TDC_Bloco_Inversa_PPM(ImagemPPM imagem, int l, int c) {

        int tam = 8;
        double tmpR;
        double tmpG;
        double tmpB;

        double[][] i_retR = new double[tam][tam];     // matriz resultante R inversa
        double[][] i_retG = new double[tam][tam];     // matriz resultante G inversa
        double[][] i_retB = new double[tam][tam];     // matriz resultante B inversa

        // Inversa da quantização 
        // Na inversa é feita primeiro a quantização inversa
        for (int i = 0; i < tam; i++) {
            for (int j = 0; j < tam; j++) {
                retR[i + l * 8][j + c * 8] = valor_QuantizadoR[i + l * 8][j + c * 8] * matriz_Quantizacao[i][j];  // matriz resultante recebe a operação inversa da quantização R
                retG[i + l * 8][j + c * 8] = valor_QuantizadoG[i + l * 8][j + c * 8] * matriz_Quantizacao[i][j];  // matriz resultante recebe a operação inversa da quantização G
                retB[i + l * 8][j + c * 8] = valor_QuantizadoB[i + l * 8][j + c * 8] * matriz_Quantizacao[i][j];  // matriz resultante recebe a operação inversa da quantização B
            }
        }
        for (int x = 0; x < tam; x++) {     // percorre a imagem 
            for (int y = 0; y < tam; y++) {   // percorre a imagem 
                i_retR[x][y] = 0;           // zera 
                i_retG[x][y] = 0;           // zera 
                i_retB[x][y] = 0;           // zera
                for (int i = 0; i < tam; i++) {
                    for (int j = 0; j < tam; j++) {
                        tmpR = retR[i + l * 8][j + c * 8];
                        tmpR *= Math.cos((2 * y + 1) * j * Math.PI / (2 * tam)); // calculo da TDCI
                        tmpR *= Math.cos((2 * x + 1) * i * Math.PI / (2 * tam)); // calculo da TDCI
                        tmpR *= i == 0 ? 1.0 / Math.sqrt(tam) : Math.sqrt(2 / tam); //se o valor de i for 0, então o cálculo é 1 sobre a raiz quadrada do tamanho do bloco, se o valor de i for 1, então o cálculo é raiz quadrada de 2 sobre o tamanho do bloco
                        tmpR *= j == 0 ? 1.0 / Math.sqrt(tam) : Math.sqrt(2 / tam); //se o valor de j for 0, então o cálculo é 1 sobre a raiz quadrada do tamanho do bloco, se o valor de j for 1, então o cálculo é raiz quadrada de 2 sobre o tamanho do bloco
                        i_retR[x][y] += tmpR;  // cada ponto da matriz resultante é soma dos produtos do cosseno e dos coeficientes de todos os valores de um bloco

                        tmpG = retG[i + l * 8][j + c * 8];
                        tmpG *= Math.cos((2 * y + 1) * j * Math.PI / (2 * tam)); // calculo da TDCI
                        tmpG *= Math.cos((2 * x + 1) * i * Math.PI / (2 * tam)); // calculo da TDCI
                        tmpG *= i == 0 ? 1.0 / Math.sqrt(tam) : Math.sqrt(2 / tam); //se o valor de i for 0, então o cálculo é 1 sobre a raiz quadrada do tamanho do bloco, se o valor de i for 1, então o cálculo é raiz quadrada de 2 sobre o tamanho do bloco
                        tmpG *= j == 0 ? 1.0 / Math.sqrt(tam) : Math.sqrt(2 / tam); //se o valor de j for 0, então o cálculo é 1 sobre a raiz quadrada do tamanho do bloco, se o valor de j for 1, então o cálculo é raiz quadrada de 2 sobre o tamanho do bloco
                        i_retG[x][y] += tmpG;  // cada ponto da matriz resultante é soma dos produtos do cosseno e dos coeficientes de todos os valores de um bloco

                        tmpB = retB[i + l * 8][j + c * 8];
                        tmpB *= Math.cos((2 * y + 1) * j * Math.PI / (2 * tam)); // calculo da TDCI
                        tmpB *= Math.cos((2 * x + 1) * i * Math.PI / (2 * tam)); // calculo da TDCI
                        tmpB *= i == 0 ? 1.0 / Math.sqrt(tam) : Math.sqrt(2 / tam); //se o valor de i for 0, então o cálculo é 1 sobre a raiz quadrada do tamanho do bloco, se o valor de i for 1, então o cálculo é raiz quadrada de 2 sobre o tamanho do bloco
                        tmpB *= j == 0 ? 1.0 / Math.sqrt(tam) : Math.sqrt(2 / tam); //se o valor de j for 0, então o cálculo é 1 sobre a raiz quadrada do tamanho do bloco, se o valor de j for 1, então o cálculo é raiz quadrada de 2 sobre o tamanho do bloco
                        i_retB[x][y] += tmpB;  // cada ponto da matriz resultante é soma dos produtos do cosseno e dos coeficientes de todos os valores de um bloco
                    }
                }
                /*i_retR[x][y] *= 1.0/4.0 ; 
                i_retG[x][y] *= 1.0/4.0 ;
                i_retB[x][y] *= 1.0/4.0 ;
                 */
            }
        }
        int[][] R = imagem.getR();
        int[][] G = imagem.getG();
        int[][] B = imagem.getB();

        for (int i = 0; i < tam; i++) {
            for (int j = 0; j < tam; j++) {                           //atualiza os valores dos pixels na imagem
                /*System.out.print(Math.round(i_retR[i][j]) + " ");        //exibe o resultado
                System.out.print(Math.round(i_retG[i][j]) + " ");        //exibe o resultado
                System.out.print(Math.round(i_retB[i][j]) + " ");        //exibe o resultado
                 */
                if (i != 7 && j != 0) {
                    inversa_retR[i + l * 8][j + c * 8] = (int) Math.round(i_retR[i][j]);
                    inversa_retG[i + l * 8][j + c * 8] = (int) Math.round(i_retG[i][j]);
                    inversa_retB[i + l * 8][j + c * 8] = (int) Math.round(i_retB[i][j]);

                    if (i_retR[i][j] > 255) {
                        inversa_retR[i + l * 8][j + c * 8] = 255;
                    } else {
                        inversa_retR[i + l * 8][j + c * 8] = (int) Math.round(i_retR[i][j]);
                    }

                    if (i_retG[i][j] > 255) {
                        inversa_retG[i + l * 8][j + c * 8] = 255;
                    } else {
                        inversa_retG[i + l * 8][j + c * 8] = (int) Math.round(i_retG[i][j]);
                    }

                    if (i_retB[i][j] > 255) {
                        inversa_retB[i + l * 8][j + c * 8] = 255;
                    } else {
                        inversa_retB[i + l * 8][j + c * 8] = (int) Math.round(i_retB[i][j]);
                    }
                } else {
                    inversa_retR[i + l * 8][j + c * 8] = R[7 + l * 8][7 + c * 8]; // a ultima pos de cada bloco vai receber o pixel quantizado
                    inversa_retG[i + l * 8][j + c * 8] = G[7 + l * 8][7 + c * 8];
                    inversa_retB[i + l * 8][j + c * 8] = B[7 + l * 8][7 + c * 8];

                    /*inversa_retR[i + l * 8][j + c * 8] = R[7 + l * 8][0 + c * 8]; // a ultima pos de cada bloco vai receber o pixel quantizado
                    inversa_retG[i + l * 8][j + c * 8] = G[7 + l * 8][0 + c * 8];
                    inversa_retB[i + l * 8][j + c * 8] = B[7 + l * 8][0 + c * 8];
                     */
                }
            }
            //System.out.println("");
        }

    } // TDC_Bloco_Inversa_PPM

    public ImagemPPM Tecnica_LSB_PPM_TDC(ImagemPPM imagem, String mensagem) throws IOException {
        String[] vetBinario;

        int[][] R = imagem.getR(); // matrizes que representam a terna de cores vermelho verde azul
        int[][] G = imagem.getG();
        int[][] B = imagem.getB();

        ImagemPPM novaImagem = imagem.clone(); // copia da imagem 

        int[][] novaR = novaImagem.getR();
        int[][] novaG = novaImagem.getG();
        int[][] novaB = novaImagem.getB();

        vetBinario = Converte_Mensagem_ASCII_Binario(mensagem); // converte a mensagem para numeros ascii e depois de ascii para binarios

        int k = 0;
        int j = 0;

        for (int linha = 0; linha < novaImagem.getDimX(); linha++) {
            for (int coluna = 0; coluna < novaImagem.getDimY(); coluna++) {

                if (vetBinario[j] != null) { // Verifica se a mensagem acabou

                    String numBinR = Converte_Decimal_Binario(R[linha][coluna]);
                    String numBinG = Converte_Decimal_Binario(G[linha][coluna]);
                    String numBinB = Converte_Decimal_Binario(B[linha][coluna]);

                    if (k < 8) { // verifica se a string chegou no fim 

                        if (vetBinario[j].charAt(k) != numBinR.charAt(numBinR.length() - 1)) {
                            k++;

                            if (numBinR.charAt(numBinR.length() - 1) == '0') {
                                numBinR = numBinR.substring(0, numBinR.length() - 1) + "1";
                            } else {
                                numBinR = numBinR.substring(0, numBinR.length() - 1) + "0";
                            }

                            int numDecR = Converte_Binario_Decimal(numBinR);
                            novaR[linha][coluna] = numDecR;

                        } else {
                            int numDecR = Converte_Binario_Decimal(numBinR);
                            novaR[linha][coluna] = numDecR;
                            k++;
                        }
                    } else {
                        j++;
                        k = 0;
                    }

                    if (k < 8) { // verifica se a string chegou no fim

                        if (vetBinario[j].charAt(k) != numBinG.charAt(numBinG.length() - 1)) {
                            k++;

                            if (numBinG.charAt(numBinG.length() - 1) == '0') {
                                numBinG = numBinG.substring(0, numBinG.length() - 1) + "1";
                            } else {
                                numBinG = numBinG.substring(0, numBinG.length() - 1) + "0";
                            }

                            int numDecG = Converte_Binario_Decimal(numBinG);
                            novaG[linha][coluna] = numDecG;

                        } else {
                            int numDecG = Converte_Binario_Decimal(numBinG);
                            novaG[linha][coluna] = numDecG;
                            k++;
                        }
                    } else {
                        j++;
                        k = 0;
                    }

                    if (k < 8) { // verifica se a string chegou no fim

                        if (vetBinario[j].charAt(k) != numBinB.charAt(numBinB.length() - 1)) {
                            k++;

                            if (numBinB.charAt(numBinB.length() - 1) == '0') {
                                numBinB = numBinB.substring(0, numBinB.length() - 1) + "1";
                            } else {
                                numBinB = numBinB.substring(0, numBinB.length() - 1) + "0";
                            }

                            int numDecB = Converte_Binario_Decimal(numBinB);
                            novaB[linha][coluna] = numDecB;

                        } else {
                            int numDecB = Converte_Binario_Decimal(numBinB);
                            novaB[linha][coluna] = numDecB;
                            k++;
                        }
                    } else {
                        j++;
                        k = 0;
                    }

                } else {
                    break;
                }
            }
        }
        return novaImagem;
    } // Tecnica_LSB_PPM_TDC

    public ImagemPPM Tecnica_LSB_PPM_TDC_BitLivre(ImagemPPM imagem, String mensagem) throws IOException {
        String[] vetBinario;

        int[][] R = imagem.getR(); // matrizes que representam a terna de cores vermelho verde azul
        int[][] G = imagem.getG();
        int[][] B = imagem.getB();

        ImagemPPM novaImagem = imagem.clone(); // copia da imagem 

        int[][] novaR = novaImagem.getR();
        int[][] novaG = novaImagem.getG();
        int[][] novaB = novaImagem.getB();

        vetBinario = Converte_Mensagem_ASCII_Binario(mensagem); // converte a mensagem para numeros ascii e depois de ascii para binarios

        int k = 0;
        int j = 0;
        int pos = 4; // posição do bit 

        for (int linha = 0; linha < novaImagem.getDimX(); linha++) {
            for (int coluna = 0; coluna < novaImagem.getDimY(); coluna++) {

                if (vetBinario[j] != null) { // Verifica se a mensagem acabou

                    String numBinR = Converte_Decimal_Binario(R[linha][coluna]);
                    String numBinG = Converte_Decimal_Binario(G[linha][coluna]);
                    String numBinB = Converte_Decimal_Binario(B[linha][coluna]);

                    if (k < 8) { // verifica se a string chegou no fim 

                        if (vetBinario[j].charAt(k) != numBinR.charAt(pos - 1)) {
                            k++;

                            if (numBinR.charAt(pos - 1) == '0') { // colocando na posição pos-1 
                                numBinR = numBinR.substring(0, pos - 1) + "1" + numBinR.substring(pos, 8);
                            } else {
                                numBinR = numBinR.substring(0, pos - 1) + "0" + numBinR.substring(pos, 8);
                            }

                            int numDecR = Converte_Binario_Decimal(numBinR);
                            novaR[linha][coluna] = numDecR;

                        } else {
                            int numDecR = Converte_Binario_Decimal(numBinR);
                            novaR[linha][coluna] = numDecR;
                            k++;
                        }
                    } else {
                        j++;
                        k = 0;
                    }

                    if (k < 8) { // verifica se a string chegou no fim

                        if (vetBinario[j].charAt(k) != numBinG.charAt(pos - 1)) {
                            k++;

                            if (numBinG.charAt(pos - 1) == '0') {
                                numBinG = numBinG.substring(0, pos - 1) + "1" + numBinG.substring(pos, 8);
                            } else {
                                numBinG = numBinG.substring(0, pos - 1) + "0" + numBinG.substring(pos, 8);
                            }

                            int numDecG = Converte_Binario_Decimal(numBinG);
                            novaG[linha][coluna] = numDecG;

                        } else {
                            int numDecG = Converte_Binario_Decimal(numBinG);
                            novaG[linha][coluna] = numDecG;
                            k++;
                        }
                    } else {
                        j++;
                        k = 0;
                    }

                    if (k < 8) { // verifica se a string chegou no fim

                        if (vetBinario[j].charAt(k) != numBinB.charAt(pos - 1)) {
                            k++;

                            if (numBinB.charAt(pos - 1) == '0') {
                                numBinB = numBinB.substring(0, pos - 1) + "1" + numBinB.substring(pos, 8);
                            } else {
                                numBinB = numBinB.substring(0, pos - 1) + "0" + numBinB.substring(pos, 8);
                            }

                            int numDecB = Converte_Binario_Decimal(numBinB);
                            novaB[linha][coluna] = numDecB;

                        } else {
                            int numDecB = Converte_Binario_Decimal(numBinB);
                            novaB[linha][coluna] = numDecB;
                            k++;
                        }
                    } else {
                        j++;
                        k = 0;
                    }

                } else {
                    break;
                }
            }
        }
        return novaImagem;
    } // Tecnica_LSB_PPM_TDC

    public ImagemPPM TDC_PPM_Sem_MSG(Imagem img) {
        ImagemPPM imagem = (ImagemPPM) img;
        ImagemPPM novaImagem = imagem.clone(); // copia da imagem 

        int qtdBL = (int) imagem.getDimX() / 8;
        int qtdBC = (int) imagem.getDimY() / 8;

        valor_QuantizadoR = new int[imagem.getDimX()][imagem.getDimY()];     // matriz que vai guardar os valores quantizados 
        valor_QuantizadoG = new int[imagem.getDimX()][imagem.getDimY()];     // matriz que vai guardar os valores quantizados 
        valor_QuantizadoB = new int[imagem.getDimX()][imagem.getDimY()];     // matriz que vai guardar os valores quantizados

        retR = new double[imagem.getDimX()][imagem.getDimY()];            // matriz resultante R 
        retG = new double[imagem.getDimX()][imagem.getDimY()];            // matriz resultante G 
        retB = new double[imagem.getDimX()][imagem.getDimY()];            // matriz resultante B

        for (int l = 0; l < qtdBL; l++) {
            for (int c = 0; c < qtdBC; c++) {
                //TDC_Bloco_PPM(img, vetBinario, l, c);         // o calculo da TDC é calculado em blocos de 8x8 pixels
                TDC_Bloco_PPM_Sem_Ultima_Pos(img, l, c);                   // o calculo da TDC é calculado em blocos de 8x8 pixels
            }
        }
        novaImagem.setR(valor_QuantizadoR);
        novaImagem.setG(valor_QuantizadoG);
        novaImagem.setB(valor_QuantizadoB);

        return novaImagem;

    } // TDC_PPM_Sem_MSG

    public void TDC_Bloco_PPM_Sem_Ultima_Pos(Imagem img, int l, int c) {
        ImagemPPM imagem = (ImagemPPM) img;

        int[][] R = imagem.getR(); // matrizes que representam a terna de cores vermelho verde azul
        int[][] G = imagem.getG();
        int[][] B = imagem.getB();

        int fator_Qualidade = 2;
        int tam = 8;
        double tmpR;
        double tmpG;
        double tmpB;

        for (int i = 0; i < tam; i++) {     // percorre a imagem 
            for (int j = 0; j < tam; j++) { // percorre a imagem 
                retR[i + l * 8][j + c * 8] = 0;
                retG[i + l * 8][j + c * 8] = 0;
                retB[i + l * 8][j + c * 8] = 0;
                for (int x = 0; x < tam; x++) {
                    for (int y = 0; y < tam; y++) {
                        tmpR = R[x + l * 8][y + c * 8];
                        tmpR *= Math.cos((2 * y + 1) * j * Math.PI / (2 * tam)); // calculo da TDC
                        tmpR *= Math.cos((2 * x + 1) * i * Math.PI / (2 * tam)); // calculo da TDC
                        retR[i + l * 8][j + c * 8] += tmpR; // cada ponto da matriz resultante é a soma dos produtos do cosseno de todos os valores de um bloco

                        tmpG = G[x + l * 8][y + c * 8];
                        tmpG *= Math.cos((2 * y + 1) * j * Math.PI / (2 * tam)); // calculo da TDC
                        tmpG *= Math.cos((2 * x + 1) * i * Math.PI / (2 * tam)); // calculo da TDC
                        retG[i + l * 8][j + c * 8] += tmpG; // cada ponto da matriz resultante é a soma dos produtos do cosseno de todos os valores de um bloco

                        tmpB = B[x + l * 8][y + c * 8];
                        tmpB *= Math.cos((2 * y + 1) * j * Math.PI / (2 * tam)); // calculo da TDC
                        tmpB *= Math.cos((2 * x + 1) * i * Math.PI / (2 * tam)); // calculo da TDC
                        retB[i + l * 8][j + c * 8] += tmpB; // cada ponto da matriz resultante é a soma dos produtos do cosseno de todos os valores de um bloco
                    }
                }
                retR[i + l * 8][j + c * 8] *= i == 0 ? 1.0 / Math.sqrt(tam) : Math.sqrt(2 / tam); //se o valor de i for 0, então o cálculo é 1 sobre a raiz quadrada do tamanho do bloco, se o valor de i for 1, então o cálculo é raiz quadrada de 2 sobre o tamanho do bloco
                retR[i + l * 8][j + c * 8] *= j == 0 ? 1.0 / Math.sqrt(tam) : Math.sqrt(2 / tam); //se o valor de j for 0, então o cálculo é 1 sobre a raiz quadrada do tamanho do bloco, se o valor de j for 1, então o cálculo é raiz quadrada de 2 sobre o tamanho do bloco

                retG[i + l * 8][j + c * 8] *= i == 0 ? 1.0 / Math.sqrt(tam) : Math.sqrt(2 / tam); //se o valor de i for 0, então o cálculo é 1 sobre a raiz quadrada do tamanho do bloco, se o valor de i for 1, então o cálculo é raiz quadrada de 2 sobre o tamanho do bloco
                retG[i + l * 8][j + c * 8] *= j == 0 ? 1.0 / Math.sqrt(tam) : Math.sqrt(2 / tam); //se o valor de j for 0, então o cálculo é 1 sobre a raiz quadrada do tamanho do bloco, se o valor de j for 1, então o cálculo é raiz quadrada de 2 sobre o tamanho do bloco

                retB[i + l * 8][j + c * 8] *= i == 0 ? 1.0 / Math.sqrt(tam) : Math.sqrt(2 / tam); //se o valor de i for 0, então o cálculo é 1 sobre a raiz quadrada do tamanho do bloco, se o valor de i for 1, então o cálculo é raiz quadrada de 2 sobre o tamanho do bloco
                retB[i + l * 8][j + c * 8] *= j == 0 ? 1.0 / Math.sqrt(tam) : Math.sqrt(2 / tam); //se o valor de j for 0, então o cálculo é 1 sobre a raiz quadrada do tamanho do bloco, se o valor de j for 1, então o cálculo é raiz quadrada de 2 sobre o tamanho do bloco

                /*retR[i + l * 8][j + c * 8] *= 1.0 / Math.sqrt(2 * tam) ;   // resultado = resultado * 1/(raiz 2*tam)
                retG[i + l * 8][j + c * 8] *= 1.0 / Math.sqrt(2 * tam) ;   // resultado = resultado * 1/(raiz 2*tam)
                retB[i + l * 8][j + c * 8] *= 1.0 / Math.sqrt(2 * tam) ;   // resultado = resultado * 1/(raiz 2*tam)
                 */
            }
        }

        // Quantização dos coeficientes 
        for (int i = 0; i < tam; i++) {
            for (int j = 0; j < tam; j++) {
                if (i != 7 && j != 7) {
                    matriz_Quantizacao[i][j] = 1 + (1 + i + j) * fator_Qualidade;
                    valor_QuantizadoR[i + l * 8][j + c * 8] = (int) Math.round(retR[i + l * 8][j + c * 8] / matriz_Quantizacao[i][j]);  // fazendo a quantização dos coeficientes R
                    valor_QuantizadoG[i + l * 8][j + c * 8] = (int) Math.round(retG[i + l * 8][j + c * 8] / matriz_Quantizacao[i][j]);  // fazendo a quantização dos coeficientes G
                    valor_QuantizadoB[i + l * 8][j + c * 8] = (int) Math.round(retB[i + l * 8][j + c * 8] / matriz_Quantizacao[i][j]);  // fazendo a quantização dos coeficientes B
                    //System.out.print(valor_QuantizadoR[i + l * 8][j + c * 8] + " ");
                } else {
                    valor_QuantizadoR[i + l * 8][j + c * 8] = R[7 + l * 8][7 + c * 8];
                    valor_QuantizadoG[i + l * 8][j + c * 8] = G[7 + l * 8][7 + c * 8];
                    valor_QuantizadoB[i + l * 8][j + c * 8] = B[7 + l * 8][7 + c * 8];
                }
            }
            //System.out.println("");
        }

    } // TDC_Bloco_PPM_Sem_Ultima_Pos

    public ImagemPGM TDC_PGM_Sem_MSG(Imagem img) {
        ImagemPGM imagem = (ImagemPGM) img;
        ImagemPGM novaImagem = imagem.clone(); // copia da imagem 

        int qtdBL = (int) imagem.getDimX() / 8;
        int qtdBC = (int) imagem.getDimY() / 8;

        valor_QuantizadoR = new int[imagem.getDimX()][imagem.getDimY()];     // matriz que vai guardar os valores quantizados 

        retR = new double[imagem.getDimX()][imagem.getDimY()];            // matriz resultante R 

        for (int l = 0; l < qtdBL; l++) {
            for (int c = 0; c < qtdBC; c++) {
                //TDC_Bloco_PPM(img, vetBinario, l, c);         // o calculo da TDC é calculado em blocos de 8x8 pixels
                TDC_Bloco_PGM_Sem_Ultima_Pos(img, l, c);                   // o calculo da TDC é calculado em blocos de 8x8 pixels
            }
        }
        novaImagem.setMatriz(valor_QuantizadoR);

        return novaImagem;

    } // TDC_PGM_Sem_MSG

    public void TDC_Bloco_PGM_Sem_Ultima_Pos(Imagem img, int l, int c) {
        ImagemPGM imagem = (ImagemPGM) img;
        int[][] R = imagem.getMatriz(); // matriz da imagem

        int fator_Qualidade = 2;
        int tam = 8;
        double tmpR;

        for (int i = 0; i < tam; i++) {     // percorre a imagem 
            for (int j = 0; j < tam; j++) { // percorre a imagem 
                retR[i + l * 8][j + c * 8] = 0;
                for (int x = 0; x < tam; x++) {
                    for (int y = 0; y < tam; y++) {
                        tmpR = R[x + l * 8][y + c * 8];
                        tmpR *= Math.cos((2 * y + 1) * j * Math.PI / (2 * tam)); // calculo da TDC
                        tmpR *= Math.cos((2 * x + 1) * i * Math.PI / (2 * tam)); // calculo da TDC
                        retR[i + l * 8][j + c * 8] += tmpR; // cada ponto da matriz resultante é a soma dos produtos do cosseno de todos os valores de um bloco
                    }
                }
                retR[i + l * 8][j + c * 8] *= i == 0 ? 1.0 / Math.sqrt(tam) : Math.sqrt(2 / tam); //se o valor de i for 0, então o cálculo é 1 sobre a raiz quadrada do tamanho do bloco, se o valor de i for 1, então o cálculo é raiz quadrada de 2 sobre o tamanho do bloco
                retR[i + l * 8][j + c * 8] *= j == 0 ? 1.0 / Math.sqrt(tam) : Math.sqrt(2 / tam); //se o valor de j for 0, então o cálculo é 1 sobre a raiz quadrada do tamanho do bloco, se o valor de j for 1, então o cálculo é raiz quadrada de 2 sobre o tamanho do bloco

                //retR[i + l * 8][j + c * 8] *= 1.0 / Math.sqrt(2 * tam) ;   // resultado = resultado * 1/(raiz 2*tam)                
            }
        }

        // Quantização dos coeficientes 
        for (int i = 0; i < tam; i++) {
            for (int j = 0; j < tam; j++) {
                if (i != 7 && j != 7) {
                    matriz_Quantizacao[i][j] = 1 + (1 + i + j) * fator_Qualidade;
                    valor_QuantizadoR[i + l * 8][j + c * 8] = (int) Math.round(retR[i + l * 8][j + c * 8] / matriz_Quantizacao[i][j]);  // fazendo a quantização dos coeficientes R
                    //System.out.print(valor_QuantizadoR[i + l * 8][j + c * 8] + " ");
                } else {
                    valor_QuantizadoR[i + l * 8][j + c * 8] = R[7 + l * 8][7 + c * 8];
                }
            }
            //System.out.println("");
        }
    } // TDC_Bloco_PGM_Sem_Ultima_Pos

    public ImagemPPM Ocultar_Mensagem_BitLivre_PPM(Imagem img, String mensagem, int pos) throws IOException {
        String[] vetBinario;

        ImagemPPM imagem = (ImagemPPM) img;

        int[][] R = imagem.getR(); // matrizes que representam a terna de cores vermelho verde azul
        int[][] G = imagem.getG();
        int[][] B = imagem.getB();

        ImagemPPM novaImagem = imagem.clone(); // copia da imagem 

        int[][] novaR = novaImagem.getR();
        int[][] novaG = novaImagem.getG();
        int[][] novaB = novaImagem.getB();

        vetBinario = Converte_Mensagem_ASCII_Binario(mensagem); // converte a mensagem para numeros ascii e depois de ascii para binarios

        int k = 0;
        int j = 0;
        //int pos = 0 ;
        //pos = 4 ;
        for (int linha = 0; linha < novaImagem.getDimX(); linha++) {
            for (int coluna = 0; coluna < novaImagem.getDimY(); coluna++) {

                if (vetBinario[j] != null) { // Verifica se a mensagem acabou

                    String numBinR = Converte_Decimal_Binario(R[linha][coluna]);
                    String numBinG = Converte_Decimal_Binario(G[linha][coluna]);
                    String numBinB = Converte_Decimal_Binario(B[linha][coluna]);

                    if (k < 8) { // verifica se a string chegou no fim 

                        if (vetBinario[j].charAt(k) != numBinR.charAt(pos - 1)) {
                            k++;

                            if (numBinR.charAt(pos - 1) == '0') { // colocando na posição pos-1 
                                numBinR = numBinR.substring(0, pos - 1) + "1" + numBinR.substring(pos, 8);
                            } else {
                                numBinR = numBinR.substring(0, pos - 1) + "0" + numBinR.substring(pos, 8);
                            }

                            int numDecR = Converte_Binario_Decimal(numBinR);
                            novaR[linha][coluna] = numDecR;

                        } else {
                            int numDecR = Converte_Binario_Decimal(numBinR);
                            novaR[linha][coluna] = numDecR;
                            k++;
                        }
                    } else {
                        j++;
                        k = 0;
                    }

                    if (k < 8) { // verifica se a string chegou no fim

                        if (vetBinario[j].charAt(k) != numBinG.charAt(pos - 1)) {
                            k++;

                            if (numBinG.charAt(pos - 1) == '0') {
                                numBinG = numBinG.substring(0, pos - 1) + "1" + numBinG.substring(pos, 8);
                            } else {
                                numBinG = numBinG.substring(0, pos - 1) + "0" + numBinG.substring(pos, 8);
                            }

                            int numDecG = Converte_Binario_Decimal(numBinG);
                            novaG[linha][coluna] = numDecG;

                        } else {
                            int numDecG = Converte_Binario_Decimal(numBinG);
                            novaG[linha][coluna] = numDecG;
                            k++;
                        }
                    } else {
                        j++;
                        k = 0;
                    }

                    if (k < 8) { // verifica se a string chegou no fim

                        if (vetBinario[j].charAt(k) != numBinB.charAt(pos - 1)) {
                            k++;

                            if (numBinB.charAt(pos - 1) == '0') {
                                numBinB = numBinB.substring(0, pos - 1) + "1" + numBinB.substring(pos, 8);
                            } else {
                                numBinB = numBinB.substring(0, pos - 1) + "0" + numBinB.substring(pos, 8);
                            }

                            int numDecB = Converte_Binario_Decimal(numBinB);
                            novaB[linha][coluna] = numDecB;

                        } else {
                            int numDecB = Converte_Binario_Decimal(numBinB);
                            novaB[linha][coluna] = numDecB;
                            k++;
                        }
                    } else {
                        j++;
                        k = 0;
                    }

                } else {
                    break;
                }
            }
        }
        return novaImagem;
    } // Ocultar_Mensagem_BitLivre_PPM

    public String Extrair_Mensagem_BitLivre_PPM(Imagem img, int pos) {
        ArrayList lista = new ArrayList();
        ArrayList vetBinario = new ArrayList<>();
        String mensagem;

        ImagemPPM imagem = (ImagemPPM) img;

        int[][] R = imagem.getR(); // matrizes que representam a terna de cores vermelho verde azul
        int[][] G = imagem.getG();
        int[][] B = imagem.getB();

        ImagemPPM novaImagem = imagem.clone(); // copia da imagem 

        for (int linha = 0; linha < novaImagem.getDimX(); linha++) {
            for (int coluna = 0; coluna < novaImagem.getDimY(); coluna++) {

                String numBinR = Converte_Decimal_Binario(R[linha][coluna]);
                String numBinG = Converte_Decimal_Binario(G[linha][coluna]);
                String numBinB = Converte_Decimal_Binario(B[linha][coluna]);

                lista.add(numBinR.charAt(pos - 1)); // Pegando o caracter de cada pixel a partir de pos [2,7]
                lista.add(numBinG.charAt(pos - 1));
                lista.add(numBinB.charAt(pos - 1));
            }
        }

        char[] aux = new char[8];
        int j = 0;

        for (int i = 0; i < lista.size(); i++) {             // andando por todos os caracteres e formando a letra com 8 bits         
            if (j < 8) {
                aux[j] = (char) lista.get(i);
                j++;
            } else {
                String bin = String.copyValueOf(aux);     // passando o vetor de caracter para string 
                vetBinario.add(bin);
                j = 0;
            }
        }
        mensagem = Converte_ASCII_String(vetBinario);

        return mensagem;
    }// Extrair_Mensagem_BitLivre_PPM

    public ImagemPGM Ocultar_Mensagem_BitLivre_PGM(Imagem img, String mensagem, int pos) throws IOException {
        String[] vetBinario;
        ImagemPGM imagem = (ImagemPGM) img;

        int[][] matriz = imagem.getMatriz(); // matrizes que representa a matriz de pixels da imagem

        ImagemPGM novaImagem = imagem.clone(); // copia da imagem 

        int[][] novaMatriz = novaImagem.getMatriz();

        vetBinario = Converte_Mensagem_ASCII_Binario(mensagem); // converte a mensagem para numeros ascii e depois de ascii para binarios

        int k = 0;
        int j = 0;

        for (int linha = 0; linha < novaImagem.getDimX(); linha++) {
            for (int coluna = 0; coluna < novaImagem.getDimY(); coluna++) {

                if (vetBinario[j] != null) { // Verifica se a mensagem acabou

                    String numBin = Converte_Decimal_Binario(matriz[linha][coluna]);

                    if (k < 8) { // verifica se a string chegou no fim 

                        if (vetBinario[j].charAt(k) != numBin.charAt(pos - 1)) { // Verificando se bit que está entre a posição [2,7] do pixel é diferente do bit da letra convertida em binario 
                            k++;

                            if (numBin.charAt(pos - 1) == '0') {
                                numBin = numBin.substring(0, pos - 1) + "1" + numBin.substring(pos, 8);
                            } else {
                                numBin = numBin.substring(0, pos - 1) + "0" + numBin.substring(pos, 8);
                            }

                            int numDec = Converte_Binario_Decimal(numBin);
                            novaMatriz[linha][coluna] = numDec;

                        } else {
                            int numDec = Converte_Binario_Decimal(numBin);
                            novaMatriz[linha][coluna] = numDec;
                            k++;
                        }
                    } else {
                        j++;
                        k = 0;
                    }

                } else {
                    break;
                }
            }
        }
        return novaImagem;
    } // Tecnica_Filtragem_Mascaramento_PGM

    public String Extrair_Mensagem_BitLivre_PGM(Imagem img, int pos) {
        ArrayList lista = new ArrayList();
        ArrayList vetBinario = new ArrayList<>();
        String mensagem;

        ImagemPGM imagem = (ImagemPGM) img;

        int[][] matriz = imagem.getMatriz(); // matrizes que representa a matriz de pixels da imagem

        ImagemPGM novaImagem = imagem.clone(); // copia da imagem 

        for (int linha = 0; linha < novaImagem.getDimX(); linha++) {
            for (int coluna = 0; coluna < novaImagem.getDimY(); coluna++) {

                String numBin = Converte_Decimal_Binario(matriz[linha][coluna]);
                lista.add(numBin.charAt(pos - 1));       // Pegando o caracter de cada pixel na posição entre [2,7]
            }
        }

        char[] aux = new char[8];
        int j = 0;

        for (int i = 0; i < lista.size(); i++) {             // andando por todos os caracteres e formando a letra com 8 bits         
            if (j < 8) {
                aux[j] = (char) lista.get(i);
                j++;
            } else {
                String bin = String.copyValueOf(aux);     // passando o vetor de caracter para string 
                vetBinario.add(bin);
                j = 0;
            }
        }
        //System.out.println("lista: " + lista.toString());
        //System.out.println("vetBinario: " + vetBinario.toString());
        mensagem = Converte_ASCII_String(vetBinario);

        return mensagem;
    }// Extrair_Mensagem_BitLivre_PGM

    public ImagemPPM Tecnica_LSB_PPM_TDC_Bloco_Teste(ImagemPPM imagem, String mensagem, String[] vetBinario, int l, int c) throws IOException {
        //String[] vetBinario;

        int[][] R = imagem.getR(); // matrizes que representam a terna de cores vermelho verde azul
        int[][] G = imagem.getG();
        int[][] B = imagem.getB();

        ImagemPPM novaImagem = imagem.clone(); // copia da imagem 

        int[][] novaR = novaImagem.getR();
        int[][] novaG = novaImagem.getG();
        int[][] novaB = novaImagem.getB();

        //vetBinario = Converte_Mensagem_ASCII_Binario(mensagem); // converte a mensagem para numeros ascii e depois de ascii para binarios
        //int tam = 8 ;
        //int k = 0;
        //int j = 0;
        //int pos = 0 ;
        //pos = 8 ;
        //for (int linha = 0; linha < tam ; linha++) {
        //for (int coluna = 0; coluna < tam ; coluna++) {
        if (vetBinario[posj] != null) { // Verifica se a mensagem acabou

            String numBinR = Converte_Decimal_Binario(R[7 + l * 8][7 + c * 8]);
            String numBinG = Converte_Decimal_Binario(G[7 + l * 8][7 + c * 8]);
            String numBinB = Converte_Decimal_Binario(B[7 + l * 8][7 + c * 8]);

            if (posk < 8) { // verifica se a string chegou no fim 

                if (vetBinario[posj].charAt(posk) != numBinR.charAt(numBinR.length() - 1)) {
                    posk++;

                    if (numBinR.charAt(numBinR.length() - 1) == '0') {
                        numBinR = numBinR.substring(0, numBinR.length() - 1) + "1";
                    } else {
                        numBinR = numBinR.substring(0, numBinR.length() - 1) + "0";
                    }

                    int numDecR = Converte_Binario_Decimal(numBinR);
                    novaR[7 + l * 8][7 + c * 8] = numDecR;

                } else {
                    int numDecR = Converte_Binario_Decimal(numBinR);
                    novaR[7 + l * 8][7 + c * 8] = numDecR;
                    posk++;

                }
            } else {
                posj++;
                posk = 0;
            }

            if (posk < 8) { // verifica se a string chegou no fim

                if (vetBinario[posj].charAt(posk) != numBinG.charAt(numBinG.length() - 1)) {
                    posk++;

                    if (numBinG.charAt(numBinG.length() - 1) == '0') {
                        numBinG = numBinG.substring(0, numBinG.length() - 1) + "1";
                    } else {
                        numBinG = numBinG.substring(0, numBinG.length() - 1) + "0";
                    }

                    int numDecG = Converte_Binario_Decimal(numBinG);
                    novaG[7 + l * 8][7 + c * 8] = numDecG;

                } else {
                    int numDecG = Converte_Binario_Decimal(numBinG);
                    novaG[7 + l * 8][7 + c * 8] = numDecG;
                    posk++;

                }
            } else {
                posj++;
                posk = 0;
            }

            if (posk < 8) { // verifica se a string chegou no fim

                if (vetBinario[posj].charAt(posk) != numBinB.charAt(numBinB.length() - 1)) {
                    posk++;

                    if (numBinB.charAt(numBinB.length() - 1) == '0') {
                        numBinB = numBinB.substring(0, numBinB.length() - 1) + "1";
                    } else {
                        numBinB = numBinB.substring(0, numBinB.length() - 1) + "0";
                    }

                    int numDecB = Converte_Binario_Decimal(numBinB);
                    novaB[7 + l * 8][7 + c * 8] = numDecB;

                } else {
                    int numDecB = Converte_Binario_Decimal(numBinB);
                    novaB[7 + l * 8][7 + c * 8] = numDecB;
                    posk++;

                }
            } else {
                posj++;
                posk = 0;
            }

        }
        //}
        //}
        return novaImagem;
    } // Tecnica_LSB_PPM_TDC_Teste

    public ImagemPPM Tecnica_LSB_PPM_TDC_Bloco_Teste_BitLivre(ImagemPPM imagem, String mensagem, String[] vetBinario, int l, int c) throws IOException {
        //String[] vetBinario;

        int[][] R = imagem.getR(); // matrizes que representam a terna de cores vermelho verde azul
        int[][] G = imagem.getG();
        int[][] B = imagem.getB();

        ImagemPPM novaImagem = imagem.clone(); // copia da imagem 

        int[][] novaR = novaImagem.getR();
        int[][] novaG = novaImagem.getG();
        int[][] novaB = novaImagem.getB();

        //vetBinario = Converte_Mensagem_ASCII_Binario(mensagem); // converte a mensagem para numeros ascii e depois de ascii para binarios
        //int tam = 8 ;
        //int k = 0;
        //int j = 0;
        int pos = 4;

        //for (int linha = 0; linha < tam ; linha++) {
        //for (int coluna = 0; coluna < tam ; coluna++) {
        if (vetBinario[posj] != null) { // Verifica se a mensagem acabou

            String numBinR = Converte_Decimal_Binario(R[7 + l * 8][7 + c * 8]);
            String numBinG = Converte_Decimal_Binario(G[7 + l * 8][7 + c * 8]);
            String numBinB = Converte_Decimal_Binario(B[7 + l * 8][7 + c * 8]);

            if (posk < 8) { // verifica se a string chegou no fim 

                if (vetBinario[posj].charAt(posk) != numBinR.charAt(pos - 1)) {
                    posk++;

                    if (numBinR.charAt(pos - 1) == '0') {
                        numBinR = numBinR.substring(0, pos - 1) + "1" + numBinR.substring(pos, 8);
                    } else {
                        numBinR = numBinR.substring(0, pos - 1) + "0" + numBinR.substring(pos, 8);
                    }

                    int numDecR = Converte_Binario_Decimal(numBinR);
                    novaR[7 + l * 8][7 + c * 8] = numDecR;

                } else {
                    int numDecR = Converte_Binario_Decimal(numBinR);
                    novaR[7 + l * 8][7 + c * 8] = numDecR;
                    posk++;
                }
            } else {
                posj++;
                posk = 0;
            }

            if (posk < 8) { // verifica se a string chegou no fim

                if (vetBinario[posj].charAt(posk) != numBinG.charAt(pos - 1)) {
                    posk++;

                    if (numBinG.charAt(pos - 1) == '0') {
                        numBinG = numBinG.substring(0, pos - 1) + "1" + numBinG.substring(pos, 8);
                    } else {
                        numBinG = numBinG.substring(0, pos - 1) + "0" + numBinG.substring(pos, 8);
                    }

                    int numDecG = Converte_Binario_Decimal(numBinG);
                    novaG[7 + l * 8][7 + c * 8] = numDecG;

                } else {
                    int numDecG = Converte_Binario_Decimal(numBinG);
                    novaG[7 + l * 8][7 + c * 8] = numDecG;
                    posk++;
                }
            } else {
                posj++;
                posk = 0;
            }

            if (posk < 8) { // verifica se a string chegou no fim

                if (vetBinario[posj].charAt(posk) != numBinB.charAt(pos - 1)) {
                    posk++;

                    if (numBinB.charAt(pos - 1) == '0') {
                        numBinB = numBinB.substring(0, pos - 1) + "1" + numBinB.substring(pos, 8);
                    } else {
                        numBinB = numBinB.substring(0, pos - 1) + "0" + numBinB.substring(pos, 8);
                    }

                    int numDecB = Converte_Binario_Decimal(numBinB);
                    novaB[7 + l * 8][7 + c * 8] = numDecB;

                } else {
                    int numDecB = Converte_Binario_Decimal(numBinB);
                    novaB[7 + l * 8][7 + c * 8] = numDecB;
                    posk++;
                }
            } else {
                posj++;
                posk = 0;
            }

        }
        //}
        //}
        return novaImagem;
    } // Tecnica_LSB_PPM_TDC_Teste

    public ImagemPPM Tecnica_LSB_PPM_TDC_Teste(ImagemPPM imagem, String mensagem) throws IOException {
        //ImagemPPM novaImagem = imagem.clone(); // copia da imagem
        String[] vetBinario;
        vetBinario = Converte_Mensagem_ASCII_Binario(mensagem); // converte a mensagem para numeros ascii e depois de ascii para binarios

        int qtdBL = (int) imagem.getDimX() / 8;
        int qtdBC = (int) imagem.getDimY() / 8;

        for (int l = 0; l < qtdBL; l++) {
            for (int c = 0; c < qtdBC; c++) {
                try {
                    imagem = Tecnica_LSB_PPM_TDC_Bloco_Teste(imagem, mensagem, vetBinario, l, c);  // Ocultando mensagem no ultimo pixel de cada bloco
                    //imagem = Tecnica_LSB_PPM_TDC_Bloco_Teste_BitLivre(imagem, mensagem, vetBinario, l, c);  // Ocultando mensagem no ultimo pixel de cada bloco
                } catch (IOException ex) {
                    Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        posj = 0;
        posk = 0;
        return imagem;
    }

    public ImagemPGM Tecnica_LSB_PGM_DCT(ImagemPGM imagem, String mensagem) throws IOException {
        String[] vetBinario;
        vetBinario = Converte_Mensagem_ASCII_Binario(mensagem); // converte a mensagem para numeros ascii e depois de ascii para binarios

        int qtdBL = (int) imagem.getDimX() / 8;
        int qtdBC = (int) imagem.getDimY() / 8;

        for (int l = 0; l < qtdBL; l++) {
            for (int c = 0; c < qtdBC; c++) {
                try {
                    imagem = Tecnica_LSB_PGM_DCT_Bloco(imagem, mensagem, vetBinario, l, c);  // Ocultando mensagem no ultimo pixel de cada bloco
                    //imagem = Tecnica_LSB_PPM_TDC_Bloco_Teste_BitLivre(imagem, mensagem, vetBinario, l, c);  // Ocultando mensagem no ultimo pixel de cada bloco
                } catch (IOException ex) {
                    Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        posj1 = 0;
        posk1 = 0;
        return imagem;
    } // Tecnica_MSB_PGM_DCT

    public ImagemPGM Tecnica_LSB_PGM_DCT_Bloco(ImagemPGM imagem, String mensagem, String[] vetBinario, int l, int c) throws IOException {
        int[][] matriz = imagem.getMatriz(); // matriz da imagem

        ImagemPGM novaImagem = imagem.clone(); // copia da imagem 
        int[][] novaR = novaImagem.getMatriz();

        if (vetBinario[posj1] != null) { // Verifica se a mensagem acabou

            String numBinR = Converte_Decimal_Binario(matriz[7 + l * 8][7 + c * 8]);

            if (posk1 < 8) { // verifica se a string chegou no fim 

                /*if (vetBinario[posj1].charAt(posk1) != numBinR.charAt(0)) {
                    posk1++;

                    if (numBinR.charAt(0) == '0') {
                        numBinR = "1" + numBinR.substring(1, numBinR.length());
                    } else {
                        numBinR = "0" + numBinR.substring(1, numBinR.length());
                    }

                    int numDecR = Converte_Binario_Decimal(numBinR);
                    novaR[7 + l * 8][7 + c * 8] = numDecR;

                } else {
                    int numDecR = Converte_Binario_Decimal(numBinR);
                    novaR[7 + l * 8][7 + c * 8] = numDecR;
                    posk1++;

                }*/
                if (vetBinario[posj1].charAt(posk1) != numBinR.charAt(numBinR.length() - 1)) {
                    posk1++;

                    if (numBinR.charAt(numBinR.length() - 1) == '0') {
                        numBinR = numBinR.substring(0, numBinR.length() - 1) + "1";
                    } else {
                        numBinR = numBinR.substring(0, numBinR.length() - 1) + "0";
                    }

                    int numDecR = Converte_Binario_Decimal(numBinR);
                    novaR[7 + l * 8][7 + c * 8] = numDecR;

                } else {
                    int numDecR = Converte_Binario_Decimal(numBinR);
                    novaR[7 + l * 8][7 + c * 8] = numDecR;
                    posk1++;

                }

            } else {
                posj1++;
                posk1 = 0;
            }
        }
        return novaImagem;
    } // Tecnica_MSB_PGM_DCT_Bloco

    public String Codificar_DCT(Imagem img) {
        String mensagem;
        ImagemPPM imagem = (ImagemPPM) img;
        ImagemPPM novaImagem;
        ArrayList lista;
        ArrayList lista_completa = new ArrayList();
        ArrayList vetBinario;

        novaImagem = TDC_PPM_Sem_MSG(imagem); // imagem com a ultima pos de cada bloco ja quantizada

        int qtdBL = (int) imagem.getDimX() / 8;
        int qtdBC = (int) imagem.getDimY() / 8;

        for (int l = 0; l < qtdBL; l++) {
            for (int c = 0; c < qtdBC; c++) {
                // acessando o ultimo pixel de cada bloco e pegando o ultimo bit do pixel em binario
                lista = Codificar_DCT_Bloco_LSB(novaImagem, l, c);

                lista_completa.add(lista.get(0));
                lista_completa.add(lista.get(1));
                lista_completa.add(lista.get(2));

            }
        }

        vetBinario = Formar_Caracter_8bit(lista_completa); // cada posição do arraylist vai ser um numero binario com 8 bits 
        mensagem = Converte_ASCII_String(vetBinario); // convertendo cada numero binario para decimal e depois para string, assim formando a mensagem oculta
        //System.out.println("lista_completa: " + lista_completa.toString());
        //System.out.println("vetBinario: " + vetBinario.toString());
        return mensagem;
    } // Codificar_DCT

    public ArrayList Codificar_DCT_Bloco_LSB(ImagemPPM imagem, int l, int c) {
        ArrayList lista = new ArrayList();

        //int pos = 4;
        int[][] R = imagem.getR(); // matrizes que representam a terna de cores vermelho verde azul
        int[][] G = imagem.getG();
        int[][] B = imagem.getB();

        //ImagemPPM novaImagem = imagem.clone(); // copia da imagem 
        String numBinR = Converte_Decimal_Binario(R[7 + l * 8][7 + c * 8]); // acessando a ultima posição de cada bloco
        String numBinG = Converte_Decimal_Binario(G[7 + l * 8][7 + c * 8]);
        String numBinB = Converte_Decimal_Binario(B[7 + l * 8][7 + c * 8]);

        /*String numBinR = Converte_Decimal_Binario(R[7 + l * 8][0 + c * 8]); // acessando a ultima posição de cada bloco
        String numBinG = Converte_Decimal_Binario(G[7 + l * 8][0 + c * 8]);
        String numBinB = Converte_Decimal_Binario(B[7 + l * 8][0 + c * 8]);
         */
        lista.add(numBinR.charAt(numBinR.length() - 1));     // Pegando ultimo bit de cada pixel
        lista.add(numBinG.charAt(numBinG.length() - 1));
        lista.add(numBinB.charAt(numBinB.length() - 1));

        /*
        lista.add(numBinR.charAt(pos - 1));     // Pegando ultimo bit de cada pixel
        lista.add(numBinG.charAt(pos - 1));
        lista.add(numBinB.charAt(pos - 1));
         */
        return lista;
    }// Codificar_DCT_Bloco_LSB

    public String Codificar_DCT_PGM(Imagem img) {
        String mensagem;
        ImagemPGM imagem = (ImagemPGM) img;
        ImagemPGM novaImagem;
        ArrayList lista;
        ArrayList lista_completa = new ArrayList();
        ArrayList vetBinario;

        novaImagem = TDC_PGM_Sem_MSG(imagem); // imagem com a ultima pos de cada bloco ja quantizada

        int qtdBL = (int) imagem.getDimX() / 8;
        int qtdBC = (int) imagem.getDimY() / 8;

        for (int l = 0; l < qtdBL; l++) {
            for (int c = 0; c < qtdBC; c++) {
                // acessando o ultimo pixel de cada bloco e pegando o ultimo bit do pixel em binario
                lista = Codificar_DCT_Bloco_MSB_PGM(novaImagem, l, c);

                lista_completa.add(lista.get(0));
            }
        }
        vetBinario = Formar_Caracter_8bit(lista_completa); // cada posição do arraylist vai ser um numero binario com 8 bits 
        mensagem = Converte_ASCII_String(vetBinario); // convertendo cada numero binario para decimal e depois para string, assim formando a mensagem oculta
        //System.out.println("lista_completa: " + lista_completa.toString());
        //System.out.println("vetBinario: " + vetBinario.toString());
        return mensagem;
    } //Codificar_DCT_PGM

    public ArrayList Codificar_DCT_Bloco_MSB_PGM(ImagemPGM imagem, int l, int c) {
        ArrayList lista = new ArrayList();
        //int pos = 4;
        int[][] matriz = imagem.getMatriz(); // matrizes que representam a terna de cores vermelho verde azul

        String numBinR = Converte_Decimal_Binario(matriz[7 + l * 8][7 + c * 8]); // acessando a ultima posição de cada bloco

        //lista.add(numBinR.charAt(0));     // Pegando primeiro bit de cada pixel
        //lista.add(numBinR.charAt(pos - 1));     // Pegando o bit na posição escolhida de cada pixel
        lista.add(numBinR.charAt(numBinR.length() - 1)); // pegando ultimo bit do ultimo pixel de cada bloco

        return lista;
    }// Codificar_DCT_Bloco_MSB_PGM

    // Ocultando a Mensagem PPM
    public ImagemPPM DCT_PPM(Imagem img, String mensagem) {
        ImagemPPM imagem = (ImagemPPM) img;
        ImagemPPM novaImagem = imagem.clone(); // copia da imagem 
        ImagemPPM A_linha = imagem.clone(); // copia da imagem 

        int qtdBL = (int) imagem.getDimX() / 8;
        int qtdBC = (int) imagem.getDimY() / 8;

        retR = new double[imagem.getDimX()][imagem.getDimY()];            // matriz resultante R 
        retG = new double[imagem.getDimX()][imagem.getDimY()];            // matriz resultante G 
        retB = new double[imagem.getDimX()][imagem.getDimY()];            // matriz resultante B

        valor_QuantizadoR = new int[imagem.getDimX()][imagem.getDimY()];     // matriz que vai guardar os valores quantizados 
        valor_QuantizadoG = new int[imagem.getDimX()][imagem.getDimY()];     // matriz que vai guardar os valores quantizados 
        valor_QuantizadoB = new int[imagem.getDimX()][imagem.getDimY()];     // matriz que vai guardar os valores quantizados

        for (int l = 0; l < qtdBL; l++) {
            for (int c = 0; c < qtdBC; c++) {
                DCT_Bloco_PPM(img, l, c);                   // o calculo da TDC é calculado em blocos de 8x8 pixels
            }
        }

        novaImagem = DCT_Inversa_PPM(novaImagem); // matriz inversa com inteiros 

        try {
            A_linha = Tecnica_Coeficiente_DCT(novaImagem, imagem, mensagem);     // A_linha tem a mensagem oculta 
        } catch (IOException ex) {
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
        }

        novaImagem.setA_linhaR(A_linha.getR()); // passando A_linha para a imagem 
        novaImagem.setA_linhaG(A_linha.getG());
        novaImagem.setA_linhaB(A_linha.getB());

        return novaImagem;  // inversa B e A_linha

    } // DCT_PPM

    public void DCT_Bloco_PPM(Imagem img, int l, int c) {
        ImagemPPM imagem = (ImagemPPM) img;

        int[][] R = imagem.getR(); // matrizes que representam a terna de cores vermelho verde azul
        int[][] G = imagem.getG();
        int[][] B = imagem.getB();

        int tam = 8;
        int fator_Qualidade = 2;
        double tmpR;
        double tmpG;
        double tmpB;

        for (int i = 0; i < tam; i++) {     // percorre a imagem 
            for (int j = 0; j < tam; j++) { // percorre a imagem 
                retR[i + l * 8][j + c * 8] = 0;
                retG[i + l * 8][j + c * 8] = 0;
                retB[i + l * 8][j + c * 8] = 0;
                for (int x = 0; x < tam; x++) {
                    for (int y = 0; y < tam; y++) {
                        tmpR = R[x + l * 8][y + c * 8];
                        tmpR *= Math.cos((2 * y + 1) * j * Math.PI / (2 * tam)); // calculo da TDC
                        tmpR *= Math.cos((2 * x + 1) * i * Math.PI / (2 * tam)); // calculo da TDC
                        retR[i + l * 8][j + c * 8] += tmpR; // cada ponto da matriz resultante é a soma dos produtos do cosseno de todos os valores de um bloco

                        tmpG = G[x + l * 8][y + c * 8];
                        tmpG *= Math.cos((2 * y + 1) * j * Math.PI / (2 * tam)); // calculo da TDC
                        tmpG *= Math.cos((2 * x + 1) * i * Math.PI / (2 * tam)); // calculo da TDC
                        retG[i + l * 8][j + c * 8] += tmpG; // cada ponto da matriz resultante é a soma dos produtos do cosseno de todos os valores de um bloco

                        tmpB = B[x + l * 8][y + c * 8];
                        tmpB *= Math.cos((2 * y + 1) * j * Math.PI / (2 * tam)); // calculo da TDC
                        tmpB *= Math.cos((2 * x + 1) * i * Math.PI / (2 * tam)); // calculo da TDC
                        retB[i + l * 8][j + c * 8] += tmpB; // cada ponto da matriz resultante é a soma dos produtos do cosseno de todos os valores de um bloco
                    }
                }
                retR[i + l * 8][j + c * 8] *= i == 0 ? 1.0 / Math.sqrt(tam) : Math.sqrt(2 / tam); //se o valor de i for 0, então o cálculo é 1 sobre a raiz quadrada do tamanho do bloco, se o valor de i for 1, então o cálculo é raiz quadrada de 2 sobre o tamanho do bloco
                retR[i + l * 8][j + c * 8] *= j == 0 ? 1.0 / Math.sqrt(tam) : Math.sqrt(2 / tam); //se o valor de j for 0, então o cálculo é 1 sobre a raiz quadrada do tamanho do bloco, se o valor de j for 1, então o cálculo é raiz quadrada de 2 sobre o tamanho do bloco

                retG[i + l * 8][j + c * 8] *= i == 0 ? 1.0 / Math.sqrt(tam) : Math.sqrt(2 / tam); //se o valor de i for 0, então o cálculo é 1 sobre a raiz quadrada do tamanho do bloco, se o valor de i for 1, então o cálculo é raiz quadrada de 2 sobre o tamanho do bloco
                retG[i + l * 8][j + c * 8] *= j == 0 ? 1.0 / Math.sqrt(tam) : Math.sqrt(2 / tam); //se o valor de j for 0, então o cálculo é 1 sobre a raiz quadrada do tamanho do bloco, se o valor de j for 1, então o cálculo é raiz quadrada de 2 sobre o tamanho do bloco

                retB[i + l * 8][j + c * 8] *= i == 0 ? 1.0 / Math.sqrt(tam) : Math.sqrt(2 / tam); //se o valor de i for 0, então o cálculo é 1 sobre a raiz quadrada do tamanho do bloco, se o valor de i for 1, então o cálculo é raiz quadrada de 2 sobre o tamanho do bloco
                retB[i + l * 8][j + c * 8] *= j == 0 ? 1.0 / Math.sqrt(tam) : Math.sqrt(2 / tam); //se o valor de j for 0, então o cálculo é 1 sobre a raiz quadrada do tamanho do bloco, se o valor de j for 1, então o cálculo é raiz quadrada de 2 sobre o tamanho do bloco

                /*retR[i + l * 8][j + c * 8] *= 1.0 / Math.sqrt(2 * tam) ;   // resultado = resultado * 1/(raiz 2*tam)
                retG[i + l * 8][j + c * 8] *= 1.0 / Math.sqrt(2 * tam) ;   // resultado = resultado * 1/(raiz 2*tam)
                retB[i + l * 8][j + c * 8] *= 1.0 / Math.sqrt(2 * tam) ;   // resultado = resultado * 1/(raiz 2*tam)
                 */
            }
        }

        for (int i = 0; i < tam; i++) {
            for (int j = 0; j < tam; j++) {
                matriz_Quantizacao[i][j] = 1 + (1 + i + j) * fator_Qualidade;
                valor_QuantizadoR[i + l * 8][j + c * 8] = (int) Math.round(retR[i + l * 8][j + c * 8] / matriz_Quantizacao[i][j]);  // fazendo a quantização dos coeficientes R
                valor_QuantizadoG[i + l * 8][j + c * 8] = (int) Math.round(retG[i + l * 8][j + c * 8] / matriz_Quantizacao[i][j]);  // fazendo a quantização dos coeficientes G
                valor_QuantizadoB[i + l * 8][j + c * 8] = (int) Math.round(retB[i + l * 8][j + c * 8] / matriz_Quantizacao[i][j]);  // fazendo a quantização dos coeficientes B
                //System.out.print(valor_QuantizadoR[i + l * 8][j + c * 8] + " ");
            }
            //System.out.println("");
        }

    } // DCT_Bloco_PPM

    public ImagemPPM Tecnica_Coeficiente_DCT(ImagemPPM imagem, ImagemPPM imagemOriginal, String mensagem) throws IOException {
        String[] vetBinario;
        vetBinario = Converte_Mensagem_ASCII_Binario(mensagem); // converte a mensagem para numeros ascii e depois de ascii para binarios
        ImagemPPM novaImagem = imagem.clone();

        int qtdBL = (int) imagem.getDimX() / 8;
        int qtdBC = (int) imagem.getDimY() / 8;

        A_linhaR = new int[imagem.getDimX()][imagem.getDimY()];
        A_linhaG = new int[imagem.getDimX()][imagem.getDimY()];
        A_linhaB = new int[imagem.getDimX()][imagem.getDimY()];

        A_linhaR = imagemOriginal.getR();
        A_linhaG = imagemOriginal.getG();
        A_linhaB = imagemOriginal.getB();

        for (int l = 0; l < qtdBL; l++) {
            for (int c = 0; c < qtdBC; c++) {
                try {
                    Tecnica_Coeficiente_DCT_Bloco(imagem, vetBinario, l, c);  // Ocultando mensagem no ultimo pixel de cada bloco
                } catch (IOException ex) {
                    Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        posj = 0;
        posk = 0;

        novaImagem.setR(A_linhaR);
        novaImagem.setG(A_linhaG);
        novaImagem.setB(A_linhaB);

        return novaImagem;
    } // Tecnica_Coeficiente_DCT

    public void Tecnica_Coeficiente_DCT_Bloco(ImagemPPM imagem, String[] vetBinario, int l, int c) throws IOException {
        int[][] R = imagem.getR();
        int[][] G = imagem.getG();
        int[][] B = imagem.getB();

        if (vetBinario[posj] != null) { // Verifica se a mensagem acabou

            if (posk < 8) { // verifica se a string chegou no fim 

                if (vetBinario[posj].charAt(posk) == '0') { // verifica se o bit da mensagem oculta e igual 0

                    A_linhaR[7 + l * 8][7 + c * 8] = R[7 + l * 8][7 + c * 8];   // copia a inversa na pos [7,7] para a matriz A'
                    posk++;
                } else {

                    A_linhaR[7 + l * 8][7 + c * 8] = R[7 + l * 8][7 + c * 8] - 1; // copia a inversa na pos [7,7] para a matriz A'

                    if (A_linhaR[7 + l * 8][7 + c * 8] < 0) {
                        //A_linhaR[7 + l * 8][7 + c * 8] = 0;
                        A_linhaR[7 + l * 8][7 + c * 8] += 2;
                    }

                    posk++;
                }
            } else {
                posj++;
                posk = 0;
            }

            if (posk < 8) { // verifica se a string chegou no fim

                if (vetBinario[posj].charAt(posk) == '0') {  // verifica se o bit da mensagem oculta e igual 0

                    A_linhaG[7 + l * 8][7 + c * 8] = G[7 + l * 8][7 + c * 8];   // copia a inversa na pos [7,7] para a matriz A'
                    posk++;
                } else {

                    A_linhaG[7 + l * 8][7 + c * 8] = G[7 + l * 8][7 + c * 8] - 1; // copia a inversa na pos [7,7] para a matriz A'

                    if (A_linhaG[7 + l * 8][7 + c * 8] < 0) {
                        //A_linhaG[7 + l * 8][7 + c * 8] = 0;
                        A_linhaG[7 + l * 8][7 + c * 8] += 2;
                    }

                    posk++;
                }
            } else {
                posj++;
                posk = 0;
            }

            if (posk < 8) { // verifica se a string chegou no fim

                if (vetBinario[posj].charAt(posk) == '0') {  // verifica se o bit da mensagem oculta e igual 0

                    A_linhaB[7 + l * 8][7 + c * 8] = B[7 + l * 8][7 + c * 8];   // copia a inversa na pos [7,7] para a matriz A'
                    posk++;
                } else {

                    A_linhaB[7 + l * 8][7 + c * 8] = B[7 + l * 8][7 + c * 8] - 1; // copia a inversa na pos [7,7] para a matriz A'

                    if (A_linhaB[7 + l * 8][7 + c * 8] < 0) {
                        //A_linhaB[7 + l * 8][7 + c * 8] = 0;
                        A_linhaB[7 + l * 8][7 + c * 8] += 2;
                    }

                    posk++;
                }
            } else {
                posj++;
                posk = 0;
            }
        }
    } // Tecnica_Coeficiente_DCT_Bloco

    public ImagemPPM DCT_Inversa_PPM(ImagemPPM imagem) {
        ImagemPPM novaImagem = imagem.clone();

        int qtdBL = (int) imagem.getDimX() / 8;
        int qtdBC = (int) imagem.getDimY() / 8;

        inversa_retR = new int[imagem.getDimX()][imagem.getDimY()];             // matriz inversa inteira do resultado
        inversa_retG = new int[imagem.getDimX()][imagem.getDimY()];             // matriz inversa inteira do resultado
        inversa_retB = new int[imagem.getDimX()][imagem.getDimY()];             // matriz inversa inteira do resultado

        // tste
        in_retR = new double[imagem.getDimX()][imagem.getDimY()];             // matriz inversa inteira do resultado
        in_retG = new double[imagem.getDimX()][imagem.getDimY()];             // matriz inversa inteira do resultado
        in_retB = new double[imagem.getDimX()][imagem.getDimY()];             // matriz inversa inteira do resultado

        for (int l = 0; l < qtdBL; l++) {
            for (int c = 0; c < qtdBC; c++) {
                DCT_Bloco_Inversa_PPM(l, c);                   // o calculo da TDCI é calculado em blocos de 8x8 pixels

            }
        }

        /*novaImagem.setMatrizR(retR); // matriz de coeficientes 
        novaImagem.setMatrizG(retG);
        novaImagem.setMatrizB(retB);
         */
        // teste
        /*novaImagem.setMatrizR(in_retR); // matriz inversa de double 
        novaImagem.setMatrizG(in_retG);
        novaImagem.setMatrizB(in_retB);*/
        novaImagem.setR(inversa_retR); // inversa com numeros interios 
        novaImagem.setG(inversa_retG);
        novaImagem.setB(inversa_retB);

        return novaImagem;

    } // DCT_Inversa_PPM

    public void DCT_Bloco_Inversa_PPM(int l, int c) {
        int tam = 8;
        double tmpR;
        double tmpG;
        double tmpB;

        double[][] i_retR = new double[tam][tam];     // matriz resultante R inversa
        double[][] i_retG = new double[tam][tam];     // matriz resultante G inversa
        double[][] i_retB = new double[tam][tam];     // matriz resultante B inversa

        // Inversa da quantização 
        // Na inversa é feita primeiro a quantização inversa
        for (int i = 0; i < tam; i++) {
            for (int j = 0; j < tam; j++) {
                retR[i + l * 8][j + c * 8] = valor_QuantizadoR[i + l * 8][j + c * 8] * matriz_Quantizacao[i][j];  // matriz resultante recebe a operação inversa da quantização R
                retG[i + l * 8][j + c * 8] = valor_QuantizadoG[i + l * 8][j + c * 8] * matriz_Quantizacao[i][j];  // matriz resultante recebe a operação inversa da quantização G
                retB[i + l * 8][j + c * 8] = valor_QuantizadoB[i + l * 8][j + c * 8] * matriz_Quantizacao[i][j];  // matriz resultante recebe a operação inversa da quantização B
            }
        }

        for (int x = 0; x < tam; x++) {     // percorre a imagem 
            for (int y = 0; y < tam; y++) {   // percorre a imagem 
                i_retR[x][y] = 0;           // zera 
                i_retG[x][y] = 0;           // zera 
                i_retB[x][y] = 0;           // zera
                for (int i = 0; i < tam; i++) {
                    for (int j = 0; j < tam; j++) {
                        tmpR = retR[i + l * 8][j + c * 8];
                        tmpR *= Math.cos((2 * y + 1) * j * Math.PI / (2 * tam)); // calculo da TDCI
                        tmpR *= Math.cos((2 * x + 1) * i * Math.PI / (2 * tam)); // calculo da TDCI
                        tmpR *= i == 0 ? 1.0 / Math.sqrt(tam) : Math.sqrt(2 / tam); //se o valor de i for 0, então o cálculo é 1 sobre a raiz quadrada do tamanho do bloco, se o valor de i for 1, então o cálculo é raiz quadrada de 2 sobre o tamanho do bloco
                        tmpR *= j == 0 ? 1.0 / Math.sqrt(tam) : Math.sqrt(2 / tam); //se o valor de j for 0, então o cálculo é 1 sobre a raiz quadrada do tamanho do bloco, se o valor de j for 1, então o cálculo é raiz quadrada de 2 sobre o tamanho do bloco
                        i_retR[x][y] += tmpR;  // cada ponto da matriz resultante é soma dos produtos do cosseno e dos coeficientes de todos os valores de um bloco

                        tmpG = retG[i + l * 8][j + c * 8];
                        tmpG *= Math.cos((2 * y + 1) * j * Math.PI / (2 * tam)); // calculo da TDCI
                        tmpG *= Math.cos((2 * x + 1) * i * Math.PI / (2 * tam)); // calculo da TDCI
                        tmpG *= i == 0 ? 1.0 / Math.sqrt(tam) : Math.sqrt(2 / tam); //se o valor de i for 0, então o cálculo é 1 sobre a raiz quadrada do tamanho do bloco, se o valor de i for 1, então o cálculo é raiz quadrada de 2 sobre o tamanho do bloco
                        tmpG *= j == 0 ? 1.0 / Math.sqrt(tam) : Math.sqrt(2 / tam); //se o valor de j for 0, então o cálculo é 1 sobre a raiz quadrada do tamanho do bloco, se o valor de j for 1, então o cálculo é raiz quadrada de 2 sobre o tamanho do bloco
                        i_retG[x][y] += tmpG;  // cada ponto da matriz resultante é soma dos produtos do cosseno e dos coeficientes de todos os valores de um bloco

                        tmpB = retB[i + l * 8][j + c * 8];
                        tmpB *= Math.cos((2 * y + 1) * j * Math.PI / (2 * tam)); // calculo da TDCI
                        tmpB *= Math.cos((2 * x + 1) * i * Math.PI / (2 * tam)); // calculo da TDCI
                        tmpB *= i == 0 ? 1.0 / Math.sqrt(tam) : Math.sqrt(2 / tam); //se o valor de i for 0, então o cálculo é 1 sobre a raiz quadrada do tamanho do bloco, se o valor de i for 1, então o cálculo é raiz quadrada de 2 sobre o tamanho do bloco
                        tmpB *= j == 0 ? 1.0 / Math.sqrt(tam) : Math.sqrt(2 / tam); //se o valor de j for 0, então o cálculo é 1 sobre a raiz quadrada do tamanho do bloco, se o valor de j for 1, então o cálculo é raiz quadrada de 2 sobre o tamanho do bloco
                        i_retB[x][y] += tmpB;  // cada ponto da matriz resultante é soma dos produtos do cosseno e dos coeficientes de todos os valores de um bloco
                    }
                }
                /*i_retR[x][y] *= 1.0/4.0 ; 
                i_retG[x][y] *= 1.0/4.0 ;
                i_retB[x][y] *= 1.0/4.0 ;
                 */
            }
        }

        for (int i = 0; i < tam; i++) {
            for (int j = 0; j < tam; j++) {                           //atualiza os valores dos pixels na imagem

                inversa_retR[i + l * 8][j + c * 8] = (int) Math.round(i_retR[i][j]);
                inversa_retG[i + l * 8][j + c * 8] = (int) Math.round(i_retG[i][j]);
                inversa_retB[i + l * 8][j + c * 8] = (int) Math.round(i_retB[i][j]);

                if (i_retR[i][j] > 255) {
                    inversa_retR[i + l * 8][j + c * 8] = 255;
                } else {
                    inversa_retR[i + l * 8][j + c * 8] = (int) Math.round(i_retR[i][j]);
                }

                if (i_retG[i][j] > 255) {
                    inversa_retG[i + l * 8][j + c * 8] = 255;
                } else {
                    inversa_retG[i + l * 8][j + c * 8] = (int) Math.round(i_retG[i][j]);
                }

                if (i_retB[i][j] > 255) {
                    inversa_retB[i + l * 8][j + c * 8] = 255;
                } else {
                    inversa_retB[i + l * 8][j + c * 8] = (int) Math.round(i_retB[i][j]);
                }
            }
        }

        // teste 
        for (int i = 0; i < tam; i++) {
            for (int j = 0; j < tam; j++) {                           //atualiza os valores dos pixels na imagem

                in_retR[i + l * 8][j + c * 8] = i_retR[i][j];
                in_retG[i + l * 8][j + c * 8] = i_retG[i][j];
                in_retB[i + l * 8][j + c * 8] = i_retB[i][j];

//                if (i_retR[i][j] > 255) {
//                    in_retR[i + l * 8][j + c * 8] = 255;
//                } else {
//                    in_retR[i + l * 8][j + c * 8] = i_retR[i][j];
//                }
//
//                if (i_retG[i][j] > 255) {
//                    in_retG[i + l * 8][j + c * 8] = 255;
//                } else {
//                    in_retG[i + l * 8][j + c * 8] = i_retG[i][j];
//                }
//
//                if (i_retB[i][j] > 255) {
//                    in_retB[i + l * 8][j + c * 8] = 255;
//                } else {
//                    in_retB[i + l * 8][j + c * 8] = i_retB[i][j];
//                }
            }
        }

    } // TDC_Bloco_Inversa_PPM

    // Extração da Mensagem Oculta PPM 
    public String Extrair_DCT(Imagem img) {
        String mensagem;
        ImagemPPM imagem = (ImagemPPM) img; // imagem com a IDCT com inteiros  
        ArrayList lista;
        ArrayList lista_completa = new ArrayList();
        ArrayList vetBinario;

        //DCT_PPM_Sem_MSG(imagem); // As matrizes retR retG retB contem a dct da imagem inteira 
        int qtdBL = (int) imagem.getDimX() / 8;
        int qtdBC = (int) imagem.getDimY() / 8;

        int aux = 0;
        for (int l = 0; l < qtdBL; l++) {
            for (int c = 0; c < qtdBC; c++) {
                // acessando o ultimo pixel de cada bloco e pegando o ultimo bit do pixel em binario
                lista = Extrair_DCT_Bloco(imagem, l, c);
                aux += 3;
                if (aux < 7) {
                    lista_completa.add(lista.get(0));
                    lista_completa.add(lista.get(1));
                    lista_completa.add(lista.get(2));
                    //System.out.println("lista:" + lista.toString());                    

                } else {
                    lista_completa.add(lista.get(0));
                    lista_completa.add(lista.get(1));
                    //System.out.println("lista:" + lista.toString());
                    aux = 0;
                }
            }
        }
        vetBinario = Formar_Caracter_8bit(lista_completa); // cada posição do arraylist vai ser um numero binario com 8 bits 
        mensagem = Converte_ASCII_String(vetBinario); // convertendo cada numero binario para decimal e depois para string, assim formando a mensagem oculta        
        //System.out.println("lista_completa: " + lista_completa.toString());
        //System.out.println("vetBinario: " + vetBinario.toString());

        return mensagem;
    } // Extrair_DCT

    public void DCT_PPM_Sem_MSG(Imagem img) {
        ImagemPPM imagem = (ImagemPPM) img;

        int qtdBL = (int) imagem.getDimX() / 8;
        int qtdBC = (int) imagem.getDimY() / 8;

        retR = new double[imagem.getDimX()][imagem.getDimY()];            // matriz resultante R 
        retG = new double[imagem.getDimX()][imagem.getDimY()];            // matriz resultante G 
        retB = new double[imagem.getDimX()][imagem.getDimY()];            // matriz resultante B

        for (int l = 0; l < qtdBL; l++) {
            for (int c = 0; c < qtdBC; c++) {
                DCT_Bloco_PPM_Sem_MSG(img, l, c);                   // o calculo da TDC é calculado em blocos de 8x8 pixels
            }
        }
    } // DCT_PPM_Sem_MSG

    public void DCT_Bloco_PPM_Sem_MSG(Imagem img, int l, int c) {
        ImagemPPM imagem = (ImagemPPM) img;

        int[][] R = imagem.getR();
        int[][] G = imagem.getG();
        int[][] B = imagem.getB();

        int tam = 8;
        double tmpR;
        double tmpG;
        double tmpB;

        for (int i = 0; i < tam; i++) {     // percorre a imagem 
            for (int j = 0; j < tam; j++) { // percorre a imagem 
                retR[i + l * 8][j + c * 8] = 0;
                retG[i + l * 8][j + c * 8] = 0;
                retB[i + l * 8][j + c * 8] = 0;
                for (int x = 0; x < tam; x++) {
                    for (int y = 0; y < tam; y++) {
                        tmpR = R[x + l * 8][y + c * 8];
                        tmpR *= Math.cos((2 * y + 1) * j * Math.PI / (2 * tam)); // calculo da TDC
                        tmpR *= Math.cos((2 * x + 1) * i * Math.PI / (2 * tam)); // calculo da TDC
                        retR[i + l * 8][j + c * 8] += tmpR; // cada ponto da matriz resultante é a soma dos produtos do cosseno de todos os valores de um bloco

                        tmpG = G[x + l * 8][y + c * 8];
                        tmpG *= Math.cos((2 * y + 1) * j * Math.PI / (2 * tam)); // calculo da TDC
                        tmpG *= Math.cos((2 * x + 1) * i * Math.PI / (2 * tam)); // calculo da TDC
                        retG[i + l * 8][j + c * 8] += tmpG; // cada ponto da matriz resultante é a soma dos produtos do cosseno de todos os valores de um bloco

                        tmpB = B[x + l * 8][y + c * 8];
                        tmpB *= Math.cos((2 * y + 1) * j * Math.PI / (2 * tam)); // calculo da TDC
                        tmpB *= Math.cos((2 * x + 1) * i * Math.PI / (2 * tam)); // calculo da TDC
                        retB[i + l * 8][j + c * 8] += tmpB; // cada ponto da matriz resultante é a soma dos produtos do cosseno de todos os valores de um bloco
                    }
                }
                retR[i + l * 8][j + c * 8] *= i == 0 ? 1.0 / Math.sqrt(tam) : Math.sqrt(2 / tam); //se o valor de i for 0, então o cálculo é 1 sobre a raiz quadrada do tamanho do bloco, se o valor de i for 1, então o cálculo é raiz quadrada de 2 sobre o tamanho do bloco
                retR[i + l * 8][j + c * 8] *= j == 0 ? 1.0 / Math.sqrt(tam) : Math.sqrt(2 / tam); //se o valor de j for 0, então o cálculo é 1 sobre a raiz quadrada do tamanho do bloco, se o valor de j for 1, então o cálculo é raiz quadrada de 2 sobre o tamanho do bloco

                retG[i + l * 8][j + c * 8] *= i == 0 ? 1.0 / Math.sqrt(tam) : Math.sqrt(2 / tam); //se o valor de i for 0, então o cálculo é 1 sobre a raiz quadrada do tamanho do bloco, se o valor de i for 1, então o cálculo é raiz quadrada de 2 sobre o tamanho do bloco
                retG[i + l * 8][j + c * 8] *= j == 0 ? 1.0 / Math.sqrt(tam) : Math.sqrt(2 / tam); //se o valor de j for 0, então o cálculo é 1 sobre a raiz quadrada do tamanho do bloco, se o valor de j for 1, então o cálculo é raiz quadrada de 2 sobre o tamanho do bloco

                retB[i + l * 8][j + c * 8] *= i == 0 ? 1.0 / Math.sqrt(tam) : Math.sqrt(2 / tam); //se o valor de i for 0, então o cálculo é 1 sobre a raiz quadrada do tamanho do bloco, se o valor de i for 1, então o cálculo é raiz quadrada de 2 sobre o tamanho do bloco
                retB[i + l * 8][j + c * 8] *= j == 0 ? 1.0 / Math.sqrt(tam) : Math.sqrt(2 / tam); //se o valor de j for 0, então o cálculo é 1 sobre a raiz quadrada do tamanho do bloco, se o valor de j for 1, então o cálculo é raiz quadrada de 2 sobre o tamanho do bloco

                /*retR[i + l * 8][j + c * 8] *= 1.0 / Math.sqrt(2 * tam) ;   // resultado = resultado * 1/(raiz 2*tam)
                retG[i + l * 8][j + c * 8] *= 1.0 / Math.sqrt(2 * tam) ;   // resultado = resultado * 1/(raiz 2*tam)
                retB[i + l * 8][j + c * 8] *= 1.0 / Math.sqrt(2 * tam) ;   // resultado = resultado * 1/(raiz 2*tam)
                 */
            }
        }
    } // DCT_Bloco_PPM_Sem_MSG

    public ArrayList Extrair_DCT_Bloco(ImagemPPM imagem, int l, int c) {
        ArrayList lista = new ArrayList();

        int[][] AlinhaR = imagem.getA_linhaR(); // A_linha
        int[][] AlinhaG = imagem.getA_linhaG();
        int[][] AlinhaB = imagem.getA_linhaB();

        int[][] R = imagem.getR(); // Imagem B
        int[][] G = imagem.getG();
        int[][] B = imagem.getB();

        if (AlinhaR[7 + l * 8][7 + c * 8] == R[7 + l * 8][7 + c * 8]) {  // adicionar o bit 0 na lista 
            lista.add('0');
        } else {
            lista.add('1');
        }
        
        if (AlinhaG[7 + l * 8][7 + c * 8] == G[7 + l * 8][7 + c * 8]) {  // adicionar o bit 0 na lista 
            lista.add('0');
        } else {
            lista.add('1');
        }
        
        if (AlinhaB[7 + l * 8][7 + c * 8] == B[7 + l * 8][7 + c * 8]) {  // adicionar o bit 0 na lista 
            lista.add('0');
        } else {
            lista.add('1');
        }
        
        /*System.out.println("AlinhaR: " + AlinhaR[7+l*8][7+c*8] + " R: " + R[7+l*8][7+c*8]);
        System.out.println("AlinhaG: " + AlinhaG[7+l*8][7+c*8] + " G: " + G[7+l*8][7+c*8]);
        System.out.println("AlinhaB: " + AlinhaB[7+l*8][7+c*8] + " B: " + B[7+l*8][7+c*8]);
        */
        return lista;
    }// Codificar_DCT_Bloco_LSB

    public ArrayList Formar_Caracter_8bit(ArrayList lista) {
        ArrayList vetBinario = new ArrayList<>();

        char[] aux = new char[8];
        int j = 0;

        for (int i = 0; i < lista.size(); i++) {             // andando por todos os caracteres e formando a letra com 8 bits         
            if (j < 8) {
                aux[j] = (char) lista.get(i);
                j++;
            } else {
                i = i - 1;
                String bin = String.copyValueOf(aux);     // passando o vetor de caracter para string 
                vetBinario.add(bin);
                j = 0;
            }
        }
        return vetBinario;
    }// Formar_Caracter_8bit

    
        // Ocultando a Mensagem PGM
    public ImagemPGM DCT_PGM(Imagem img, String mensagem) {
        ImagemPGM imagem = (ImagemPGM) img;
        ImagemPGM novaImagem = imagem.clone(); // copia da imagem 
        ImagemPGM A_linha = imagem.clone(); // copia da imagem 

        int qtdBL = (int) imagem.getDimX() / 8;
        int qtdBC = (int) imagem.getDimY() / 8;

        retR = new double[imagem.getDimX()][imagem.getDimY()];            // matriz resultante R 
        retG = new double[imagem.getDimX()][imagem.getDimY()];            // matriz resultante G 
        retB = new double[imagem.getDimX()][imagem.getDimY()];            // matriz resultante B

        valor_QuantizadoR = new int[imagem.getDimX()][imagem.getDimY()];     // matriz que vai guardar os valores quantizados 

        for (int l = 0; l < qtdBL; l++) {
            for (int c = 0; c < qtdBC; c++) {
                DCT_Bloco_PGM(img, l, c);                   // o calculo da TDC é calculado em blocos de 8x8 pixels
            }
        }

        novaImagem = DCT_Inversa_PGM(novaImagem); // matriz inversa com inteiros 

        try {
            A_linha = Tecnica_Coeficiente_DCT_PGM(novaImagem, imagem, mensagem);     // A_linha tem a mensagem oculta 
        } catch (IOException ex) {
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
        }

        novaImagem.setA_linha(A_linha.getMatriz()); // passando A_linha para a imagem 

        return novaImagem;  // inversa B e A_linha

    } // DCT_PPM

    public void DCT_Bloco_PGM(Imagem img, int l, int c) {
        ImagemPGM imagem = (ImagemPGM) img;

        int[][] R = imagem.getMatriz(); // matrizes que representam a terna de cores vermelho verde azul


        int tam = 8;
        int fator_Qualidade = 2;
        double tmpR;

        for (int i = 0; i < tam; i++) {     // percorre a imagem 
            for (int j = 0; j < tam; j++) { // percorre a imagem 
                retR[i + l * 8][j + c * 8] = 0;
                for (int x = 0; x < tam; x++) {
                    for (int y = 0; y < tam; y++) {
                        tmpR = R[x + l * 8][y + c * 8];
                        tmpR *= Math.cos((2 * y + 1) * j * Math.PI / (2 * tam)); // calculo da TDC
                        tmpR *= Math.cos((2 * x + 1) * i * Math.PI / (2 * tam)); // calculo da TDC
                        retR[i + l * 8][j + c * 8] += tmpR; // cada ponto da matriz resultante é a soma dos produtos do cosseno de todos os valores de um bloco

                    }
                }
                retR[i + l * 8][j + c * 8] *= i == 0 ? 1.0 / Math.sqrt(tam) : Math.sqrt(2 / tam); //se o valor de i for 0, então o cálculo é 1 sobre a raiz quadrada do tamanho do bloco, se o valor de i for 1, então o cálculo é raiz quadrada de 2 sobre o tamanho do bloco
                retR[i + l * 8][j + c * 8] *= j == 0 ? 1.0 / Math.sqrt(tam) : Math.sqrt(2 / tam); //se o valor de j for 0, então o cálculo é 1 sobre a raiz quadrada do tamanho do bloco, se o valor de j for 1, então o cálculo é raiz quadrada de 2 sobre o tamanho do bloco

                /*retR[i + l * 8][j + c * 8] *= 1.0 / Math.sqrt(2 * tam) ;   // resultado = resultado * 1/(raiz 2*tam)
                 */
            }
        }

        for (int i = 0; i < tam; i++) {
            for (int j = 0; j < tam; j++) {
                matriz_Quantizacao[i][j] = 1 + (1 + i + j) * fator_Qualidade;
                valor_QuantizadoR[i + l * 8][j + c * 8] = (int) Math.round(retR[i + l * 8][j + c * 8] / matriz_Quantizacao[i][j]);  // fazendo a quantização dos coeficientes R
                //System.out.print(valor_QuantizadoR[i + l * 8][j + c * 8] + " ");
            }
            //System.out.println("");
        }

    } // DCT_Bloco_PPM

    public ImagemPGM Tecnica_Coeficiente_DCT_PGM(ImagemPGM imagem, ImagemPGM imagemOriginal, String mensagem) throws IOException {
        String[] vetBinario;
        vetBinario = Converte_Mensagem_ASCII_Binario(mensagem); // converte a mensagem para numeros ascii e depois de ascii para binarios
        ImagemPGM novaImagem = imagem.clone();

        int qtdBL = (int) imagem.getDimX() / 8;
        int qtdBC = (int) imagem.getDimY() / 8;

        A_linhaR = new int[imagem.getDimX()][imagem.getDimY()];
        A_linhaG = new int[imagem.getDimX()][imagem.getDimY()];
        A_linhaB = new int[imagem.getDimX()][imagem.getDimY()];

        A_linhaR = imagemOriginal.getMatriz();


        for (int l = 0; l < qtdBL; l++) {
            for (int c = 0; c < qtdBC; c++) {
                try {
                    Tecnica_Coeficiente_DCT_Bloco_PGM(imagem, vetBinario, l, c);  // Ocultando mensagem no ultimo pixel de cada bloco
                } catch (IOException ex) {
                    Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        posj = 0;
        posk = 0;

        novaImagem.setMatriz(A_linhaR);

        return novaImagem;
    } // Tecnica_Coeficiente_DCT

    public void Tecnica_Coeficiente_DCT_Bloco_PGM(ImagemPGM imagem, String[] vetBinario, int l, int c) throws IOException {
        int[][] R = imagem.getMatriz();

        if (vetBinario[posj] != null) { // Verifica se a mensagem acabou

            if (posk < 8) { // verifica se a string chegou no fim 

                if (vetBinario[posj].charAt(posk) == '0') { // verifica se o bit da mensagem oculta e igual 0

                    A_linhaR[7 + l * 8][7 + c * 8] = R[7 + l * 8][7 + c * 8];   // copia a inversa na pos [7,7] para a matriz A'
                    posk++;
                } else {

                    A_linhaR[7 + l * 8][7 + c * 8] = R[7 + l * 8][7 + c * 8] - 1; // copia a inversa na pos [7,7] para a matriz A'

                    if (A_linhaR[7 + l * 8][7 + c * 8] < 0) {
                        //A_linhaR[7 + l * 8][7 + c * 8] = 0;
                        A_linhaR[7 + l * 8][7 + c * 8] += 2;
                    }

                    posk++;
                }
            } else {
                posj++;
                posk = 0;
            }
        }
    } // Tecnica_Coeficiente_DCT_Bloco

    public ImagemPGM DCT_Inversa_PGM(ImagemPGM imagem) {
        ImagemPGM novaImagem = imagem.clone();

        int qtdBL = (int) imagem.getDimX() / 8;
        int qtdBC = (int) imagem.getDimY() / 8;

        inversa_retR = new int[imagem.getDimX()][imagem.getDimY()];             // matriz inversa inteira do resultado

        for (int l = 0; l < qtdBL; l++) {
            for (int c = 0; c < qtdBC; c++) {
                DCT_Bloco_Inversa_PGM(l, c);                   // o calculo da TDCI é calculado em blocos de 8x8 pixels

            }
        }
        
        novaImagem.setMatriz(inversa_retR); // inversa com numeros interios 

        return novaImagem;

    } // DCT_Inversa_PPM

    public void DCT_Bloco_Inversa_PGM(int l, int c) {
        int tam = 8;
        double tmpR;

        double[][] i_retR = new double[tam][tam];     // matriz resultante R inversa

        // Inversa da quantização 
        // Na inversa é feita primeiro a quantização inversa
        for (int i = 0; i < tam; i++) {
            for (int j = 0; j < tam; j++) {
                retR[i + l * 8][j + c * 8] = valor_QuantizadoR[i + l * 8][j + c * 8] * matriz_Quantizacao[i][j];  // matriz resultante recebe a operação inversa da quantização R
            }
        }

        for (int x = 0; x < tam; x++) {     // percorre a imagem 
            for (int y = 0; y < tam; y++) {   // percorre a imagem 
                i_retR[x][y] = 0;           // zera 
                for (int i = 0; i < tam; i++) {
                    for (int j = 0; j < tam; j++) {
                        tmpR = retR[i + l * 8][j + c * 8];
                        tmpR *= Math.cos((2 * y + 1) * j * Math.PI / (2 * tam)); // calculo da TDCI
                        tmpR *= Math.cos((2 * x + 1) * i * Math.PI / (2 * tam)); // calculo da TDCI
                        tmpR *= i == 0 ? 1.0 / Math.sqrt(tam) : Math.sqrt(2 / tam); //se o valor de i for 0, então o cálculo é 1 sobre a raiz quadrada do tamanho do bloco, se o valor de i for 1, então o cálculo é raiz quadrada de 2 sobre o tamanho do bloco
                        tmpR *= j == 0 ? 1.0 / Math.sqrt(tam) : Math.sqrt(2 / tam); //se o valor de j for 0, então o cálculo é 1 sobre a raiz quadrada do tamanho do bloco, se o valor de j for 1, então o cálculo é raiz quadrada de 2 sobre o tamanho do bloco
                        i_retR[x][y] += tmpR;  // cada ponto da matriz resultante é soma dos produtos do cosseno e dos coeficientes de todos os valores de um bloco

                    }
                }
                /*i_retR[x][y] *= 1.0/4.0 ; 
                 */
            }
        }

        for (int i = 0; i < tam; i++) {
            for (int j = 0; j < tam; j++) {                           //atualiza os valores dos pixels na imagem

                inversa_retR[i + l * 8][j + c * 8] = (int) Math.round(i_retR[i][j]);

                if (i_retR[i][j] > 255) {
                    inversa_retR[i + l * 8][j + c * 8] = 255;
                } else {
                    inversa_retR[i + l * 8][j + c * 8] = (int) Math.round(i_retR[i][j]);
                }
            }
        }

    } // TDC_Bloco_Inversa_PPM

    // Extração da Mensagem Oculta PGM 
    public String Extrair_DCT_PGM(Imagem img) {
        String mensagem;
        ImagemPGM imagem = (ImagemPGM) img; // imagem com a IDCT com inteiros  
        ArrayList lista;
        ArrayList lista_completa = new ArrayList();
        ArrayList vetBinario;

        //DCT_PPM_Sem_MSG(imagem); // As matrizes retR retG retB contem a dct da imagem inteira 
        int qtdBL = (int) imagem.getDimX() / 8;
        int qtdBC = (int) imagem.getDimY() / 8;

        int aux = 0;
        for (int l = 0; l < qtdBL; l++) {
            for (int c = 0; c < qtdBC; c++) {
                // acessando o ultimo pixel de cada bloco e pegando o ultimo bit do pixel em binario
                lista = Extrair_DCT_Bloco_PGM(imagem, l, c);
                
                if (aux < 8) {
                    lista_completa.add(lista.get(0));                    
                    aux++;
                } else {
                    aux = 0;
                }
            }
        }
        vetBinario = Formar_Caracter_8bit(lista_completa); // cada posição do arraylist vai ser um numero binario com 8 bits 
        mensagem = Converte_ASCII_String(vetBinario); // convertendo cada numero binario para decimal e depois para string, assim formando a mensagem oculta        
        //System.out.println("lista_completa: " + lista_completa.toString());
        //System.out.println("vetBinario: " + vetBinario.toString());

        return mensagem;
    } // Extrair_DCT

    public ArrayList Extrair_DCT_Bloco_PGM(ImagemPGM imagem, int l, int c) {
        ArrayList lista = new ArrayList();

        int[][] Alinha = imagem.getA_linha(); // A_linha

        int[][] R = imagem.getMatriz(); // Imagem B

        if (Alinha[7 + l * 8][7 + c * 8] == R[7 + l * 8][7 + c * 8]) {  // adicionar o bit 0 na lista 
            lista.add('0');
        } else {
            lista.add('1');
        }
        
        /*System.out.println("AlinhaR: " + AlinhaR[7+l*8][7+c*8] + " R: " + R[7+l*8][7+c*8]);
        */
        return lista;
    }// Codificar_DCT_Bloco_LSB
    

} // Controlador
