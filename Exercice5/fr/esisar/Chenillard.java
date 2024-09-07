package Exercice5.fr.esisar;

import java.awt.Color;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

import javax.swing.JFrame;

public class Chenillard {
    public static void main(String[] args) throws Exception {
        Chenillard c = new Chenillard();
        if (args.length == 0) {
            System.out.println("Erreur, veuillez entrer le numéro de la fenêtre");
        } else {
            c.execute(args[0]);
        }
    }

    private void execute(String numero) throws Exception {
        JFrame frame = new JFrame("Chenillard");
        frame.setSize(300, 300);

        frame.getContentPane().setBackground(Color.green);
        frame.setVisible(true);

        DatagramSocket socketR = new DatagramSocket(null);
        int position = Integer.valueOf(numero).intValue();

        DatagramSocket socketE = new DatagramSocket();
        InetSocketAddress adrDest; 

        // On fait tous les cas de position
        // La première est un peu différente car elle est rouge au début
        // et c'est elle qui lance la chaine de commande UDP
        switch (position) {
            case 1:
                socketR.bind(new InetSocketAddress(4001)); 
                adrDest = new InetSocketAddress("127.0.0.1", 4002);
                
                // On met en place la première fenêtre rouge
                frame.getContentPane().setBackground(Color.red);
                frame.setVisible(true);
                Thread.sleep(1000);

                // On envoie la première commande
                byte[] bufE = new String("red").getBytes();
                DatagramPacket dpE = new DatagramPacket(bufE, bufE.length, adrDest);
                socketE.send(dpE);

                frame.getContentPane().setBackground(Color.green);
                frame.setVisible(true);

                break;
            case 2:
                socketR.bind(new InetSocketAddress(4002)); 
                adrDest = new InetSocketAddress("127.0.0.1", 4003);
                break;
            case 3:
                socketR.bind(new InetSocketAddress(4003)); 
                adrDest = new InetSocketAddress("127.0.0.1", 4004);
                break;
            case 4:
                socketR.bind(new InetSocketAddress(4004)); 
                adrDest = new InetSocketAddress("127.0.0.1", 4001);
                break;
        
            default:
                socketR.close();
                socketE.close();
                System.out.println("Veuillez entrer un numéro valide");
                return;
        }

        // A ce moment, nous avons déterminé les ports utilisés par notre
        // programme, le reste peut être fait de manière générique

        boolean b = true;
        while (b) {
            
            // Attente de l'ordre du programme précédent
            byte[] bufR = new byte[2048];
            DatagramPacket dpR = new DatagramPacket(bufR, bufR.length);
            socketR.receive(dpR);
            String reponse = new String(bufR, dpR.getOffset(), dpR.getLength());
            
            if (reponse.equals("red")) {
                frame.getContentPane().setBackground(Color.red);
                frame.setVisible(true);
                Thread.sleep(1000);
                
                // Envoie d'un message au programme suivant
                byte[] bufE = new String("red").getBytes();
                DatagramPacket dpE = new DatagramPacket(bufE, bufE.length, adrDest);
                socketE.send(dpE);

                frame.getContentPane().setBackground(Color.green);
                frame.setVisible(true);
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
