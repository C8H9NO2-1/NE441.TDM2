package Exercice6.fr.esisar;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class Serveur {
    public static void main(String[] args) throws Exception {
        Serveur s = new Serveur();
        s.execute();
    }

    private void execute() throws Exception {
        // On met en place le serveur sur le port 4000
        DatagramSocket socket = new DatagramSocket(null);
        socket.bind(new InetSocketAddress(4000));

        // Le serveur maintient une liste de tous les clients qui sont
        // connectés en stockant les ports et les adresses
        List<Integer> ports = new ArrayList<>();
        List<InetAddress> adresses = new ArrayList<>();

        String message;

        do {
            byte[] bufR = new byte[2048];
            DatagramPacket dpR = new DatagramPacket(bufR, bufR.length);
            socket.receive(dpR);
            message = new String(bufR, dpR.getOffset(), dpR.getLength());

            // On stocke le port du client
            ports.add(dpR.getPort());
            adresses.add(dpR.getAddress());

            System.out.println("Client enregistré");
        } while (message.equals("hello"));

        // Une fois qu'on a bien enregistré tous les clients, il faut leur
        // donner tous les ordres nécessaires
        while (true) {
            // On ordonne à chaque fenêtre de changer de couleur les unes à la
            // suite des autres
            for (int i = 0; i < ports.size(); i++) {
                byte[] bufE = new String("red").getBytes();
                DatagramPacket dpE = new DatagramPacket(bufE, bufE.length,
                        adresses.get(i), ports.get(i));
                socket.send(dpE);
                Thread.sleep(1000);

                bufE = new String("green").getBytes();
                dpE = new DatagramPacket(bufE, bufE.length,
                        adresses.get(i), ports.get(i));
                socket.send(dpE);
            }
        }

        //socket.close();
    }
}
