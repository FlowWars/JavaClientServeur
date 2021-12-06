package fr.upjv.serveur;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProgrammeServeur {

    public static void main(String[] args) throws IOException{

        int numeroPort=1234;
        //driver jdbc du sgbd
        String DriverJdbc = "com.mysql.cj.jdbc.Driver";
        //url jdbc du sgbd (mysql)
        String UrlJDBC = "jdbc:mysql://localhost:3306/java_database";
        Connection cnx = null;
        Statement stmt=null;
        Socket socket;
        DataInputStream dataInputStream;
        DataOutputStream dataOutputStream;

        try {
            ServerSocket serverSocket = new ServerSocket(numeroPort);
            System.out.println("En attente de connexion d'un client");
            socket = serverSocket.accept();
            System.out.println("Connexion au client établie");

            //chargement en memoire du driver jdbc
            try {
                Class.forName(DriverJdbc);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                System.exit(-1);
            }
            //connexion à la BDD
            try {
                cnx = DriverManager.getConnection(UrlJDBC,"root","");//log de la BDD
            } catch (SQLException e) {
                e.printStackTrace();
                System.exit(-1);
            }
            System.out.println("Connexion à la BDD établie");
            dataInputStream=new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            dataOutputStream=new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            //On envoie au client un string qui lui confirme qu'il est connecté au serveur
            String s1="Bienvenue, vous êtes bien connecté";
            dataOutputStream.writeUTF(s1);
            dataOutputStream.flush();
            //le serveur questionne le client quelle action veut-il faire
            String s2= """
                    Que voulez-vous faire ?(Choississez entre 1, 2 ou 3) :
                    1) Ajouter un livre
                    2) Demander la liste de livres
                    3) Se déconnecter""";
            dataOutputStream.writeUTF(s2);
            dataOutputStream.flush();
            //le serveur récupère le choix
            boolean continuer = true;
            while(continuer) {
                int choix= dataInputStream.read();
                switch (choix) {
                    case 1:
                        String DemandeTitre = "Titre du livre: ";
                        dataOutputStream.writeUTF(DemandeTitre);
                        dataOutputStream.flush();
                        String titre = dataInputStream.readUTF();

                        String DemandeAuteur = "Auteur du livre: ";
                        dataOutputStream.writeUTF(DemandeAuteur);
                        dataOutputStream.flush();
                        String auteur = dataInputStream.readUTF();

                        String DemandeEditeur = "Editeur du livre: ";
                        dataOutputStream.writeUTF(DemandeEditeur);
                        dataOutputStream.flush();
                        String editeur = dataInputStream.readUTF();

                        String DemandeIsbn = "Isbn du livre: ";
                        dataOutputStream.writeUTF(DemandeIsbn);
                        dataOutputStream.flush();
                        String isbn = dataInputStream.readUTF();

                        String requestSansSelect = "INSERT INTO livre (titre, auteur, editeur, isbn) Values " +
                                "('" +
                                    titre + "','" +
                                    auteur + "','" +
                                    editeur + "','" +
                                    isbn +
                                "')";
                        try {
                            stmt = cnx.createStatement();
                        } catch (SQLException e) {
                            e.printStackTrace();
                            System.exit(-1);
                        }
                        try {
                            stmt.executeUpdate(requestSansSelect);
                            System.out.println("Requete insert réussie");
                            String insertionGood="Le livre a bien été inséré";
                            dataOutputStream.writeUTF(insertionGood);
                            dataOutputStream.flush();
                        } catch (SQLException e) {
                            e.printStackTrace();
                            System.exit(-1);
                        }
                        break;
                    case 2:
                        String choix2 = "Demande de la liste de livres";
                        dataOutputStream.writeUTF(choix2);
                        dataOutputStream.flush();
                        String reqSelect="SELECT titre,auteur,editeur,isbn FROM livre;";
                        ResultSet resultSet=null;
                        try{
                            resultSet=stmt.executeQuery(reqSelect);
                            System.out.println("Requete select réussie");
                        }catch(SQLException e){
                            e.printStackTrace();
                            System.exit(-1);
                        }
                        List<String> bookArray = new ArrayList<String>();
                        try {
                            while (resultSet.next()){
                                String book = "Le livre " + resultSet.getString("titre") +
                                        " de " + resultSet.getString("auteur") +
                                        " édité par " + resultSet.getString("editeur") +
                                        " (ISBN : " + resultSet.getString("isbn") + ")";
                                bookArray.add(book);
                            }
                        }catch (SQLException e){
                            e.printStackTrace();
                            System.exit(-1);
                        }
                        int nbrBook=bookArray.size();
                        dataOutputStream.writeInt(nbrBook);
                        dataOutputStream.flush();
                        for(String book: bookArray) {
                            dataOutputStream.writeUTF(book);
                            dataOutputStream.flush();
                        }
                        break;
                    case 3:
                        String choix3 = "Deconnexion";
                        dataOutputStream.writeUTF(choix3);
                        dataOutputStream.flush();
                        continuer=false;
                        break;
                    default:
                        String erreur = "Erreur";
                        dataOutputStream.writeUTF(erreur);
                        dataOutputStream.flush();
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
