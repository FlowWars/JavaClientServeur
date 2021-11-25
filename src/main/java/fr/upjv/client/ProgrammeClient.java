package fr.upjv.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ProgrammeClient {

    public static void main(String[] args){

        String ipServeur ="127.0.0.1";
        int numeroPort = 1234;

        Socket socket=null;
        DataInputStream dataInputStream=null;
        DataOutputStream dataOutputStream=null;

        try {
            socket=new Socket(ipServeur, numeroPort);
            System.out.print("Nom client : ");
            Scanner scanner = new Scanner(System.in);
            String nomClient=scanner.next();
            //On envoie la donnée au serveur
            dataOutputStream=new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF(nomClient);
            //on récupère la donnée envoyéé par le serveur
            dataInputStream=new DataInputStream(socket.getInputStream());
            String s1= dataInputStream.readUTF();
            System.out.println(s1);

            socket.close();
            scanner.close();
            dataInputStream.close();
            dataOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }




    }

}
