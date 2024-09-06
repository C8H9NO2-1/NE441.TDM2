package Exercice3.fr.esisar;

import java.awt.Color;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

import javax.swing.JFrame;

public class Droite {
    public static void main(String[] args) throws Exception {
        Droite g = new Droite();
        g.execute();
    }

    private void execute() throws Exception {
        JFrame frame = new JFrame("Chenillard");
        frame.setSize(300, 300);

        frame.getContentPane().setBackground(Color.green);
        frame.setVisible(true);

        DatagramSocket socketR = new DatagramSocket(null);
        socketR.bind(new InetSocketAddress(4002));

        DatagramSocket socketE = new DatagramSocket();
        InetSocketAddress adrDest = new InetSocketAddress("127.0.0.1", 4001);

        boolean b = true;
        while (b) {
            
            // Attente de l'ordre du programme 1
            byte[] bufR = new byte[2048];
            DatagramPacket dpR = new DatagramPacket(bufR, bufR.length);
            socketR.receive(dpR);
            String reponse = new String(bufR, dpR.getOffset(), dpR.getLength());
            
            if (reponse.equals("red")) {
                frame.getContentPane().setBackground(Color.red);
                frame.setVisible(true);
                Thread.sleep(1000);
                frame.getContentPane().setBackground(Color.green);
                frame.setVisible(true);
                
                // Envoie d'un message au programme 1
                byte[] bufE = new String("red").getBytes();
                DatagramPacket dpE = new DatagramPacket(bufE, bufE.length, adrDest);
                socketE.send(dpE);
                System.out.println("Message envoyé");
            } else {
                frame.dispose();
                b = false;
            }
        }

        // Fermeture de la socket
        socketR.close();
        socketE.close();
    }
}
