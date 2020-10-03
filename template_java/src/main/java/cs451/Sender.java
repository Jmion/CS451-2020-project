package cs451;

import java.io.IOException;
import java.net.*;

public class Sender extends Thread{
    private final int senderPort;
    public Sender(String name, int port) {
        super(name);
        this.senderPort = port;
    }

    @Override
    public void run() {
        super.run();
        System.out.println("sender hello");
        try(DatagramSocket serverSocket = new DatagramSocket(12000)) {
            for (int i = 0; i < 300; i++) {
                byte[] msg = new Message(0,1,0,i,true).getByte();
                DatagramPacket datagramPacket = new DatagramPacket(
                        msg,
                        Message.MESSAGE_LENGTH,
                        InetAddress.getLocalHost(),
                        senderPort
                );
                serverSocket.send(datagramPacket);
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
