package fr.upjv.serveur;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ProgrammeServeur {

    public static void main(String[] args) throws IOException{

        int numeroPort=1234;
        ServerSocket serverSocket = new ServerSocket(numeroPort);
        System.out.println("En attente de connexion d'un client");
        Socket socket=null;

        try {
            socket = serverSocket.accept();
            System.out.println("Connexion réussie");
            //On récupère la donnée envoyée par le client
            DataInputStream dataInputStream=new DataInputStream(socket.getInputStream());
            String nomClient = dataInputStream.readUTF();
            //On effectue un traitement
            String s1="Bienvenue "+nomClient+", vous etes bien connecté";
            //On envoie la donnée au client
            DataOutputStream dataOutputStream=new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF(s1);

            socket.close();
            dataInputStream.close();
            dataOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
