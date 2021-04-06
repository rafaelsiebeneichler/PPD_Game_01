package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EchoServer extends Thread {

    class Placar { //classe placar

        char jogador = ' ';
        int jogoAtual = 0;
        int golsmarcados = 0;
        int qtdJogos = 0;
        int x = 9999;
        int y = 9999;

        public Placar(char j, int a) {
            jogador = j;
            jogoAtual = a;
        }
    }

    //SOCKET
    private DatagramSocket socket;
    private boolean running;
    private int TAM_BUFFER = 1024; //define o tamanho do buffer
    private byte[] buf = new byte[TAM_BUFFER]; //cria um vetor de byte com o tamanho do buffer

    //GAME
    private int getAltura = 20;
    private int getLargura = 50;
    private char[][] getMatriz = new char[getAltura][getLargura];
    private char[] time1 = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'}; //vetor de letras para time 1
    private char[] time2 = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'}; //vetor de letras para time 2
    private int contLetraJogador = 0;
    private int contLetraTime1 = 0;
    private int contGolTime1 = 0;
    private int contLetraTime2 = 0;
    private int contGolTime2 = 0;
    private int numJogo = 0;
    private int xBola;
    private int yBola;
    private Map<Character, Placar> placares = new HashMap<Character, Placar>(); //map de placar

    public EchoServer() throws SocketException {
        socket = new DatagramSocket(4446); //porta de comunicação com o cliente
    }

    public void run() {
        running = true;
        String received;
        novoJogo(); //cria novo jogo
        String r = "";

        while (running) {
            DatagramPacket packet = new DatagramPacket(buf, buf.length); //
            try {
                received = "";
                socket.receive(packet); //recebe pacote
                InetAddress address = packet.getAddress(); //adiciona o ip do cliente na variável "address"
                int port = packet.getPort(); //adiciona a porta de comunicação na variável "port"
                packet = new DatagramPacket(buf, buf.length, address, port); //
                received = new String(packet.getData(), 0, packet.getLength()).trim(); //
                System.out.println("Server received: " + received); //imprime o que o servidor recebeu do cliente
                String array[] = new String[3]; //criar um array de três posições
                array = received.split(";"); //quebra a mensagem (Stringão) em três posições e guarda no array criado
                if (array[0].equals("sorteiaLetraJogador")) { //verifica se a primeira parte do "Stringão" é sorteiaLetraHeroi
                    char h = sorteiaLetraJogador(); //adiciona a letra sorteada ao herói
                    r = "" + h;
                    sorteiaPosicaoJogador(h); //sorteia posição do herói
                } else if (array[0].equals("sorteiaPosicaoJogador")) { //verifica se a primeira parte do "Stringão" é sorteiaPosicaoHeroi
                    sorteiaPosicaoJogador(array[1].charAt(0)); //sorteia posição do herói
                } else if (array[0].equals("imprimeMatrizClient")) { //verifica se a primeira parte do "Stringão" é imprimirMatrizClient
                    r = imprimirMatrizClient(); //imprime matriz escondida do cliente
                } else if (array[0].equals("movimento")) { //verifica se a primeira parte do "Stringão" é movimento
                    r = moverJogador(array[1].charAt(0), array[2]); //realiza o movimento do herói (dados do jogador e movimento vem do "Stringão")
                } else if (array[0].equals("esperarIniciar")) { //verifica se a primeira parte do "Stringão" é esperarIniciar
                    char h = array[1].charAt(0); //adiciona a letra recebida ao herói
                    if (placares.get(h).jogoAtual != numJogo) { //verifica se o jogo que o herói perdeu é diferente do atual 
                        r = "started"; //se for, ele inicia
                    } else {
                        r = "wait"; //se não, aguarda novo jogo começar
                    }
                } else if (array[0].equals("getPlacar")) { //exibe placar do herói
                    char h = array[1].charAt(0); //adiciona a letra ao herói
                    r = String.valueOf(contGolTime1) + ';' + String.valueOf(contGolTime2) + ';'
                            + String.valueOf(placares.get(h).golsmarcados) + ';' + String.valueOf(placares.get(h).qtdJogos);
                } else if (array[0].equals("getJogo")) { //exibe total de mapas
                    char h = array[1].charAt(0); //adiciona a letra ao herói
                    r = String.valueOf(numJogo);
                }
                packet = new DatagramPacket(r.getBytes(), r.getBytes().length, address, port); // empacota mensagem
                socket.send(packet); // retorna pacote da mensagem
                System.out.println(imprimirMatriz()); // imprime mapa do jogo
            } catch (IOException ex) {
                Logger.getLogger(EchoServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        socket.close(); //encerra conexão
    }

    public void novoJogo() {
        //preenche a matriz com espaços em branco - (.)
        preencheMatriz();

        //sorteia posição da saída - (#)
        sorteiaSaida();

        //adiciona +1 no total de jogos/mapas
        numJogo++;
    }

    //preenche a matriz com espaços em branco - (.)
    public void preencheMatriz() {
        for (int i = 0; i < getAltura; i++) {
            for (int j = 0; j < getLargura; j++) {
                if ((i == 0) || (i == getAltura - 1)) {
                    getMatriz[i][j] = '/';
                } else {
                    getMatriz[i][j] = '.';
                }
            }
        }
    }

    //sorteia posição da saída - (#)
    public void sorteiaSaida() {
        xBola = 1 + (int) (Math.random() * (getAltura - 2));
        yBola = 1 + (int) (Math.random() * (getLargura - 2));
        getMatriz[xBola][yBola] = '@';
    }

    //realiza o sorteio da letra do herói/cliente
    public char sorteiaLetraJogador() {
        char jogador = '&';
        if ((contLetraJogador % 2) == 0) {
            for (int i = contLetraTime1; i < time1.length; i++) {
                contLetraJogador++;
                contLetraTime1++;
                jogador = time1[i];
                placares.put(jogador, new Placar(jogador, numJogo)); //adiciona um jogo/mapa ao placar do herói
                return jogador;
            }
        } else {
            for (int i = contLetraTime2; i < time2.length; i++) {
                contLetraJogador++;
                contLetraTime2++;
                jogador = time2[i];
                placares.put(jogador, new Placar(jogador, numJogo)); //adiciona um jogo/mapa ao placar do herói
                return jogador;
            }
        }
        return jogador;
    }

    //realiza o movimento do herói, quando digitado W - A - S - D
    public String moverJogador(char jogador, String comando) {
        String ret = "OK";
        comando = comando.toLowerCase();
        int x = placares.get(jogador).x;
        int y = placares.get(jogador).y;
        if (comando.equals("w")) {
            if (x > 0) {
                ret = verificarJogador(x - 1, y, jogador, x, y, x - 2, y);
            }
        } else if (comando.equals("s")) {
            if (x < getAltura - 1) {
                ret = verificarJogador(x + 1, y, jogador, x, y, x + 2, y);
            }
        } else if (comando.equals("a")) {
            if (y > 0) {
                ret = verificarJogador(x, y - 1, jogador, x, y, x, y - 2);
            }
        } else if (comando.equals("d")) {
            if (y < getLargura - 1) {
                ret = verificarJogador(x, y + 1, jogador, x, y, x, y + 2);
            }
        }
        return ret;
    }

    //movimento;simbolo;movimento) ---- >>> movimento;H;W ---> eu quero ir para cima --> retorna Ok, BOOM, WIN
    public String verificarJogador(int x, int y, char jogador, int xa, int ya, int xb, int yb) {
        String ret = "OK";
        Boolean lTemLetra = false;
        for (int i = 0; i < time1.length; i++) {
            if (!lTemLetra) {
                lTemLetra = getMatriz[x][y] == time1[i];
            }
        }
        for (int i = 0; i < time2.length; i++) {
            if (!lTemLetra) {
                lTemLetra = getMatriz[x][y] == time2[i];
            }
        }
        if (lTemLetra) {
            //Faz nada
        } else if (getMatriz[x][y] == '/') {
            ret = "GOL";
            System.out.println("---GOL, JOGADOR: '" + jogador + "'!---");
            System.out.println("\n---COMENÇANDO OUTRO JOGO---");
            placares.get(jogador).golsmarcados = placares.get(jogador).golsmarcados + 1;
            novoJogo();
        } else if (getMatriz[x][y] == '@') {
            if ((yb >= 0) && (yb <= (getLargura - 1))) {
                System.out.println("---DOMINA A BOLA JOGADOR: '" + jogador + "'!---");
                getMatriz[xa][ya] = '.';
                getMatriz[x][y] = jogador;
                placares.get(jogador).x = x;
                placares.get(jogador).y = y;
                if (getMatriz[xb][yb] == '/') {
                    ret = "GOL";
                    System.out.println("---GOL, JOGADOR: '" + jogador + "'!---");
                    System.out.println("\n---COMENÇANDO OUTRO JOGO---");
                    placares.get(jogador).golsmarcados = placares.get(jogador).golsmarcados + 1;
                    if (xb == 0) {
                        contGolTime1 = contGolTime1 + 1;
                    } else {
                        contGolTime2 = contGolTime2 + 1;
                    }
                    novoJogo();
                } else {
                    getMatriz[xb][yb] = '@';
                }
            }
        } else {
            getMatriz[xa][ya] = '.';
            getMatriz[x][y] = jogador;
            placares.get(jogador).x = x;
            placares.get(jogador).y = y;
        }
        return ret;
    }

    //imprime matriz no servidor
    public String imprimirMatriz() {
        String matriz = "";
        for (int i = 0; i < getAltura; i++) {
            for (int j = 0; j < getLargura; j++) {
                matriz = matriz + getMatriz[i][j];// + "\t";
            }
            matriz = matriz + '\n';
        }
        return matriz;
    }

    //imprime matriz no cliente
    public String imprimirMatrizClient() {
        String matriz = "";
        for (int i = 0; i < getAltura; i++) {
            for (int j = 0; j < getLargura; j++) {
                if ((getMatriz[i][j] != '*') && (getMatriz[i][j] != '#')) {
                    matriz = matriz + getMatriz[i][j];
                } else {
                    matriz = matriz + ".";
                }
            }
            matriz = matriz + '\n';
        }
        return matriz;
    }

    //sorteia posição inicial do jogador
    public void sorteiaPosicaoJogador(char jogador) {
        placares.get(jogador).qtdJogos = placares.get(jogador).qtdJogos + 1; //soma +1 para a quantidade de jogos/partidas daquele herói
        int linha = 1 + (int) (Math.random() * (getAltura - 2)); //sorteia número da linha (posição) de 0 a 9
        placares.get(jogador).x = linha; //X recebe valor da linha (posição) do jogador
        int coluna = 1 + (int) (Math.random() * (getLargura - 2)); //sorteia número da coluna (posição) de 0 a 9
        placares.get(jogador).y = coluna; //Y recebe valor da coluna (posição) do jogador
        while (((placares.get(jogador).x == xBola) && (placares.get(jogador).y == yBola))) { //se a posição sorteada do jogador for igual a da bola, sorteia novamente
            linha = 1 + (int) (Math.random() * (getAltura - 2)); //sorteia número da linha (posição) de 0 a 9
            placares.get(jogador).x = linha; //X recebe valor da linha (posição) do jogador
            coluna = 1 + (int) (Math.random() * (getLargura - 2)); //sorteia número da coluna (posição) de 0 a 9
            placares.get(jogador).y = coluna; //Y recebe valor da coluna (posição) do jogador
        }
        getMatriz[linha][coluna] = jogador; //coloca jogador na posição sorteada (linha e coluna)
    }
}
