package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client {

    public static void main(String[] args) throws SocketException, UnknownHostException, IOException, InterruptedException {
        Boolean heroiMorto = false;
        String echo = "", opcao = "";
        EchoClient client = new EchoClient(); //cria um novo cliente
        char jogador = client.sendEcho("sorteiaLetraJogador;").charAt(0); //sorteia uma letra de A-Z para o herói e adiciona a letra a variável "heroi"
        System.out.println("Seu jogador é: " + jogador); //imprime a letra do herói
        String jogo = client.sendEcho("getJogo;" + jogador + ";"); //imprime o número do mapa/jogo
        System.out.println("Mapa: "); //falta imprimir lado cliente
        System.out.println(client.sendEcho("imprimeMatrizClient;")); //imprime a matriz escondida para o cliente
        BufferedReader cmdUser = new BufferedReader(new InputStreamReader(System.in)); //criar uma variável buffer, para armazenar o que foi digitado pelo cliente
        while (true) { //loop
            System.out.println("Utilize as letras a seguir para mover o seu jogador na quadra:\nW: para cima\nA: para esquerda\nS: para baixo\nD: para direita\n"); //imprime tutorial
            opcao = cmdUser.readLine(); //realiza a leitura do que foi escrito pelo cliente e salva em uma variável chamada "opcao"
            echo = client.sendEcho("movimento;" + jogador + ";" + opcao + ";"); //manda "Stringão" para o server, para realizar o movimento do herói do cliente na matriz
            if (echo.equals("GOL")) { //verifica se o jogador marcou gol
                System.out.println("Você marcou um golaço!"); //imprime mensagem do gol
                echo = client.sendEcho("getPlacar;" + jogador + ";"); //solicita placar atualizado do jogador (mostrando apenas no servidor ainda)
                String array[] = new String[3]; //criar um array de três posições
                array = echo.split(";"); //quebra a mensagem (Stringão) em três posições e guarda no array criado
                System.out.println("Jaguaritica - " + array[0] + " VS " + array[1] + " - Sucuri");
                System.out.println("Gols Marcados: " + array[2]);
                System.out.println("Jogos: " + array[3]);
                //servidor já inicia outro jogo automaticamente após uma vitória
                client.sendEcho("sorteiaPosicaoJogador;" + jogador + ";"); //sorteia nova posição do jogador no novo jogo
                jogo = client.sendEcho("getJogo;" + jogador + ";"); //imprime o número do mapa/jogo (mostrando apenas no servidor ainda)
                System.out.println("Jogo: " + jogo); //falta imprimir lado cliente
            }
            System.out.println(client.sendEcho("imprimeMatrizClient;")); //imprime a matriz escondida para o cliente
        }
    }

}
