package fr.upjv.client;

import java.io.BufferedOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ProgrammeClient {

    public static void main(String[] args) throws IOException{

        String ipServeur ="127.0.0.1";
        int numeroPort = 1234;

        Socket socket=new Socket(ipServeur, numeroPort);

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }


    }

}
