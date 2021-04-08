/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 *
 * @author Rafa_
 */
public class Server {

    public static void main(String[] args) throws SocketException {

        new EchoServer().start(); //inicia o servidor

      /* inicializaJogo();

        while (true) {

            mostraCampo();

        }*/
    }

}
