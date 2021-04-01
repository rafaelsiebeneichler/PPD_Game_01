/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

/**
 *
 * @author Rafa_
 */
public class Server {

    private static final int MAX_PAUSA = 500;
    private static final int MAX_COL = 50;
    private static final int MAX_LIN = 20;
    private static final char SIM_LIVRE = '.';
    private static final char SIM_BOLA = '@';
    private static final int NUN_PLAYERS = 4;
    private static final int NUM_TIMES = 2;

    private static char campo[][] = new char[MAX_LIN][MAX_COL];

    private static char players[] = new char[30];

    private static void inicializaJogo() {
        for (int i = 0; i < MAX_LIN; i++) {
            for (int j = 0; j < MAX_COL; j++) {
                campo[i][j] = SIM_LIVRE;
            }
        }

        char vetPos[] = {'A', 'a'};
        for (int p = 0; p < NUM_TIMES; p++) {
            for (int i = 0; i < NUN_PLAYERS; i++) {  // time 1
                players[i] = (char) (vetPos[p] + i);

                int lin = -1;
                int col = -1;
                do {
                    lin = (int) (Math.random() * MAX_LIN);
                    col = (int) (Math.random() * MAX_COL);

                } while (campo[lin][col] != SIM_LIVRE);
                campo[lin][col] = players[i];
            }
        }

        int lin = -1;
        int col = -1;
        do {
            lin = (int) (Math.random() * MAX_LIN);
            col = (int) (Math.random() * MAX_COL);

        } while (campo[lin][col] != SIM_LIVRE);
        campo[lin][col] = SIM_BOLA;

    }

    private static void limpaTela() {
        for (int i = 0; i < 25; i++) {
            System.out.println("");
        }
    }

    private static void mostraCampo() {
        limpaTela();

        System.out.println();
        for (int i = 0; i < MAX_COL; i++) {
            System.out.print("a");
        }
        for (int i = 0; i < MAX_LIN; i++) {
            System.out.println();
            for (int j = 0; j < MAX_COL; j++) {
                System.out.print(campo[i][j]);
            }
        }
        System.out.println("");
        for (int i = 0; i < MAX_COL; i++) {
            System.out.print("A");
        }

        try {
            Thread.sleep(MAX_PAUSA);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void movePlayer(char player, char movimento) {
        // player, simbolo valido dos jogadores no campo
        // movimentos:
        //         w ou W --> para cima
        //         x ou X --> para baixo
        //         a ou A --> para esquerda
        //         d ou D --> para direita
        //         sempre que for para cima da bola ela vai para o mesmo lado do
        //         movimento, no caso da latera, atravessa e começa no outro lado
        //         sempre que chegar a um extremo, time inverso ganha.

        // aqui será recebida um pacote UDP com a movimentação de determinado jogador
    }

    public static int getColPlayer(char player) {
        int pos = -1;
        for (int i = 0; i < MAX_LIN; i++) {
            for (int j = 0; j < MAX_COL; j++) {
                if (campo[i][j] == player) {
                    pos = j;
                    i = MAX_LIN;  // matar o loop externo rapidamente ou usar return
                    break;
                }
            }
        }
        return pos;
    }

    public static void getLinPlayer(char player) {
        // retorna a posicao do jogador no tabuleiro
    }

    public static void getColBola() {
        // retorna a posicao do jogador no tabuleiro
    }

    public static void getLinBola() {
        // retorna a posicao do jogador no tabuleiro
    }

    public static void main(String[] args) {
        inicializaJogo();

        while (true) {

            mostraCampo();

        }

    }

}

