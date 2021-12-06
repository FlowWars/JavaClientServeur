package fr.upjv.client;

import javax.sound.midi.Soundbank;
import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ProgrammeClient {

    public static void main(String[] args){

        String ipServeur ="127.0.0.1";
        int numeroPort = 1234;
        Socket socket;
        DataInputStream dataInputStream;
        DataOutputStream dataOutputStream;

        try {
            socket=new Socket(ipServeur, numeroPort);
            Scanner scanner = new Scanner(System.in);

            dataInputStream=new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            dataOutputStream=new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            System.out.println("Serveur: " + dataInputStream.readUTF());
            //on récupère le deuxième string
            System.out.println("Serveur: " + dataInputStream.readUTF());
            //le client répond par un choix numerique
            boolean continuer = true;
            while(continuer) {
                boolean faute=false;
                int choix = 0;
                while(!faute)
                {
                    try
                    {
                        System.out.print("Client: ");
                        choix = scanner.nextInt();
                        scanner.nextLine();
                        dataOutputStream.write(choix);
                        dataOutputStream.flush();
                        faute = true;
                    }
                    catch(InputMismatchException e)
                    {
                        System.out.println("Veuillez saisir un chiffre!");
                        scanner.nextLine();
                    }
                }
                switch (choix) {
                    case 1:
                        String demandeTitre = dataInputStream.readUTF();
                        System.out.println("Serveur: " + demandeTitre);
                        String titre = scanner.nextLine();
                        dataOutputStream.writeUTF(titre);
                        dataOutputStream.flush();

                        String demandeAuteur = dataInputStream.readUTF();
                        System.out.println("Serveur: " + demandeAuteur);
                        String auteur = scanner.nextLine();
                        dataOutputStream.writeUTF(auteur);
                        dataOutputStream.flush();

                        String demandeEditeur = dataInputStream.readUTF();
                        System.out.println("Serveur: " + demandeEditeur);
                        String editeur = scanner.nextLine();
                        dataOutputStream.writeUTF(editeur);
                        dataOutputStream.flush();

                        String demandeIsbn = dataInputStream.readUTF();
                        System.out.println("Serveur: " + demandeIsbn);
                        String isbn;
                        boolean validInt=false;
                        do {
                            isbn = scanner.next().toString();
                            if(isbn.matches("\\d+")){
                                validInt=true;
                                scanner.nextLine();
                                dataOutputStream.writeUTF(isbn);
                                dataOutputStream.flush();
                            }else{
                                System.out.println("Valeur invalide, saisir un nombre valide");
                            }
                        }while(!validInt);
                        String insertionGood = dataInputStream.readUTF();
                        System.out.println("Serveur: " + insertionGood);
                        break;
                    case 2:
                        String choix2= dataInputStream.readUTF();
                        System.out.println("Serveur: " + choix2);
                        System.out.print("Serveur: ");
                        int nbrBook = dataInputStream.readInt();
                        for (int i=0;i<nbrBook;i++){
                            String book = dataInputStream.readUTF();
                            System.out.println(book);
                        }
                        break;
                    case 3:
                        String deconnexion = dataInputStream.readUTF();
                        System.out.println("Serveur: " + deconnexion);
                        continuer=false;
                        break;
                    default:
                        String erreur = dataInputStream.readUTF();
                        System.out.println("Serveur: " + erreur);
                        break;
                }
            }
            socket.close();
            dataInputStream.close();
            dataOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
