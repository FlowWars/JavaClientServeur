package fr.upjv.client;

import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.sql.*;
import java.util.Scanner;

public class ProgrammeClient {

    public static void main(String[] args){

        String ipServeur ="127.0.0.1";
        int numeroPort = 1234;
        boolean brain=false;

        Socket socket=null;
        DataInputStream dataInputStream=null;
        DataOutputStream dataOutputStream=null;

        try {
            socket=new Socket(ipServeur, numeroPort);
            System.out.print("Nom client : ");
            Scanner scanner = new Scanner(System.in);
            String nomClient=scanner.next();


            ////////////////////CONNECTION//////////////////////////////
            //1er etape : trouver le nom du driver jdbc du sgbd
            String nomDriverJdbcDuSGBD = "com.mysql.cj.jdbc.Driver";

            //2nd etape : format du url jdbc du sgbd (mysql)
            String UrlJDBC = "jdbc:mysql://localhost:3306/java_database";

            //3eme etape : chargement en memoire du driver jdbc
            try {
                Class.forName(nomDriverJdbcDuSGBD);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                System.exit(-3);
            }

            //4eme etape : élaboration d'une connexion à la BD
            Connection cnx = null;
            try {
                cnx = DriverManager.getConnection(UrlJDBC,"root","jgatEJmj");//log de la BDD
            } catch (SQLException e) {
                e.printStackTrace();
                System.exit(-4);
            }

            Statement stmt=null;

            try {
                stmt = cnx.createStatement();
            } catch (SQLException e) {
                e.printStackTrace();
                System.exit(-5);
            }
            //////////////////////FIN CONNECTION//////////////////////////

            do {
                System.out.print("1 ajout livre, 2 demande des livres");
                Scanner scanner1 = new Scanner(System.in);
                String choice = scanner1.next();
                brain = false;
                switch (choice) {
                    case "1":
                        // ajout livre

                        System.out.print("Saisir le titre");////////////////TITRE
                        Scanner scanner2 = new Scanner(System.in);
                        String titre = scanner2.nextLine();
                        titre=titre.replace("'","''");

                        System.out.print("Saisir l'auteur");////////////////AUTEUR
                        Scanner scanner3 = new Scanner(System.in);
                        String auteur = scanner3.nextLine();
                        auteur=auteur.replace("'","''");

                        System.out.print("Saisir l'editeur");////////////////EDITEUR
                        Scanner scanner4 = new Scanner(System.in);
                        String editeur = scanner4.nextLine();
                        editeur=editeur.replace("'","''");

                        System.out.print("Saisir l'isbn");////////////////ISBN
                        Scanner scanner5 = new Scanner(System.in);

                        //////////////CONTROL DE SAISIE D'UN NOMBRE/////////////////
                        String s=null;
                        String isbn;
                        boolean validInput=false;
                        do{
                            isbn = new String(scanner5.next()) ;
                            s = isbn.toString();
                            if(s.matches("\\d+")){// checks if input only contains digits
                                validInput=true;
                            }
                            else{
                                // invalid input
                                System.out.println("Valeur invalide, saisir un nombre valide");
                            }
                        }while(!validInput);
                        ////////////FIN CONTROL DE SAISIE D'UN NOMBRE////////////////

                            String requestSansSelect = "INSERT INTO livre (titre, auteur, editeur, isbn) Values ('"+titre+"','"+auteur+"','"+editeur+"','"+isbn+"')";

                        try {
                            stmt.executeUpdate(requestSansSelect);
                            System.out.println("INSERT OK !!!");
                        } catch (SQLException e) {
                            e.printStackTrace();
                            System.exit(-7);
                        }

                        scanner2.close();
                        scanner3.close();
                        scanner4.close();
                        scanner5.close();


                        break;

                    case "2": //////////Afficher les livres///////////

//////////////////////TEST BDD LIRE/////////////////////////////////
                        //le resultat d'une requete (avec select) est un resultat en java
                        String requeteAvecSelect = "SELECT titre,auteur,editeur,isbn FROM livre;";//////////////////////////UN SELECT

                        ResultSet resultSet=null;

                        try {
                            resultSet = stmt.executeQuery(requeteAvecSelect);
                            System.out.println("SELECT succes");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }


                        try {
                            while(resultSet.next()){

                                String titre1=resultSet.getString("titre");
                                String auteur1=resultSet.getString("auteur");
                                String editeur1=resultSet.getString("editeur");
                                String isbn1=resultSet.getString("isbn");
                                System.out.println("Le Livre "+titre1+" de "+auteur1+" édité par "+editeur1+", reference : "+isbn1);

                                // String unNomindex = resultSet.getString(2);
                                // System.out.println("la colonne 2 :  "+ unNomindex);
                            }
                        }catch(SQLException e){
                            e.printStackTrace();
                            System.exit(-9);
                        }/////////////////////FIN SELECT/////////////////////

                        break;

                    default:
                        brain = true;
                        break;
                }
                scanner1.close();
            } while (brain);
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
