package fr.upjv.bdd;

import java.sql.*;

public class CreationDatabase {

    public static void main(String[] args) {

        try {
            String DriverJDBC = "com.mysql.cj.jdbc.Driver";
            String UrlJDBC = "jdbc:mysql://localhost:3306";

            Class.forName(DriverJDBC);
            Connection cnx = DriverManager.getConnection(UrlJDBC, "root", "");
            Statement stmt = cnx.createStatement();
            String req = "CREATE DATABASE IF NOT EXISTS java_database CHARACTER SET utf8 COLLATE utf8_general_ci";
            System.out.println("Creation de la base de donnée...");
            stmt.executeUpdate(req);
            System.out.println("Base de donnée crée avec succès");

            UrlJDBC = UrlJDBC + "/java_database";
            cnx = DriverManager.getConnection(UrlJDBC, "root", "");
            stmt = cnx.createStatement();
            req = "Create Table IF NOT EXISTS livre " +
                    "(" +
                    "id Integer AUTO_INCREMENT not NULL" + ", " +
                    "titre VARCHAR(255), " +
                    "auteur VARCHAR(255), " +
                    "editeur VARCHAR(255), " +
                    "isbn BIGINT(20), " +
                    "PRIMARY KEY (id)" +
                    ");";
            System.out.println("Creation de la table Livre...");
            stmt.executeUpdate(req);
            System.out.println("Table livre crée avec succès");
            stmt.close();
            cnx.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
