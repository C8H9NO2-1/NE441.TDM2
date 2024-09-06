package fr.esisar;

import java.awt.Color;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

import javax.swing.JFrame;

public class Telecomande {
    public static void main(String[] args) throws Exception {
        Telecomande t = new Telecomande();
        t.execute();
    }

    private void execute() throws Exception {
        JFrame frame = new JFrame("Chenillard");
        frame.setSize(300, 300);

        frame.getContentPane().setBackground(Color.green);
        frame.setVisible(true);

        // Le serveur se declare aupres de la couche transport
        // sur le port 5099
        DatagramSocket socket = new DatagramSocket(null);
        socket.bind(new InetSocketAddress(7050));

        boolean b = true;
        while (b) {

            // Attente du message
            byte[] bufR = new byte[2048];
            DatagramPacket dpR = new DatagramPacket(bufR, bufR.length);
            socket.receive(dpR);
            String message = new String(bufR, dpR.getOffset(), dpR.getLength());
            System.out.println("Changement de couleur " + message);
 
            if (message.equals("red\n")) {
                frame.getContentPane().setBackground(Color.red);
                frame.setVisible(true);
            } else if (message.equals("green\n")) {
                frame.getContentPane().setBackground(Color.green);
                frame.setVisible(true);
            } else {
                frame.dispose();
                b = false;
            }
        }

        // Fermeture de la socket
        socket.close();
    }
}
