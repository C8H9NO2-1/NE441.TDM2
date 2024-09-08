package Exercice6.fr.esisar;

import java.awt.Color;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

import javax.swing.JFrame;

public class Client {
    public static void main(String[] args) throws Exception {
        Client c = new Client();
        c.execute(args.length != 0);
    }

    private void execute(Boolean last) throws Exception {
        JFrame frame = new JFrame("Chenillard");
        frame.setSize(300, 300);

        frame.getContentPane().setBackground(Color.green);
        frame.setVisible(true);

        DatagramSocket socket = new DatagramSocket();
        InetSocketAddress adrDest = new InetSocketAddress("127.0.0.1", 4000);

        // Le client s'enregistre auprès du serveur
        byte[] bufE;
        if (last) {
            bufE = new String("last").getBytes();
        } else {
            bufE = new String("hello") .getBytes();
        }
        DatagramPacket dpE = new DatagramPacket(bufE, bufE.length, adrDest);
        socket.send(dpE);
        
        // Puis on attend une réponse du serveur pour savoir quoi faire
        boolean b = true;
        while (b) {
            byte[] bufR = new byte[2048];
            DatagramPacket dpR = new DatagramPacket(bufR, bufR.length);
            socket.receive(dpR);
            String reponse = new String(bufR, dpR.getOffset(), dpR.getLength());

            if (reponse.equals("red")) {
                frame.getContentPane().setBackground(Color.red); 
                frame.setVisible(true);
            } else if (reponse.equals("green")) {
                frame.getContentPane().setBackground(Color.green);
                frame.setVisible(true);
            } else {
                frame.dispose();
                b = false;
            }
        }

        socket.close();
    }
}
