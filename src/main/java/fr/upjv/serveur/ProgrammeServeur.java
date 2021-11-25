package fr.upjv.serveur;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ProgrammeServeur {

    public static void main(String[] args) throws IOException{

        int numeroPort=1234;

        ServerSocket serverSocket = new ServerSocket(numeroPort);

        Socket maSocket=null;

        try {
            maSocket = serverSocket.accept();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        System.out.println("client connecté");

        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
