package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class EchoClient {
    
    private DatagramSocket socket;
    private InetAddress address;
    int TAM_BUFFER = 1024; //define o tamanho do buffer
    private byte[] bufRet = new byte[TAM_BUFFER]; //cria um vetor de byte com o tamanho do buffer

    public EchoClient() throws SocketException, UnknownHostException {
        socket = new DatagramSocket(); //criar um novo socket
        address = InetAddress.getByName("localhost"); //pega o ip da máquina
    }

    //método para enviar mensagem para o servidor
    public String sendEcho(String msg) throws IOException {
        byte[] buf = msg.getBytes(); //pega a mensagem e adiciona a um vetor de bytes
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 4446); //cria um pacote contendo a mensagem, tamanho da mensagem, ip do cliente e a porta de comunicação com o servidor
        socket.send(packet); //envia o pacote para o servidor
        // System.out.println("Client send: " + msg); //imprime mensagem enviada
        packet = new DatagramPacket(bufRet, bufRet.length); //Cria novo pacote pra receber
        socket.receive(packet); // Recebe pacote
        String received = new String(packet.getData(), 0, packet.getLength()).trim(); // Converte string e trinca
        return received; // retorna
    }

    public void close() {
        socket.close(); //encerra conexão
    }
    
}
