package cs451;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.LinkedBlockingQueue;

public class Listener extends Thread{
    private final int port;
    private LinkedBlockingQueue<DatagramPacket> processingQueue;

    public Listener(int port, String name, LinkedBlockingQueue<DatagramPacket> processingQueue) {
        super(name);
        this.port = port;
        this.processingQueue = processingQueue;
    }

    @Override
    public void run() {
        super.run();
        System.out.println("lsitener hello");
        try(DatagramSocket clientSocket = new DatagramSocket(port)){
            while(true){
                byte[] buffer = new byte[65507]; //can reduce this once you are sure of the length of the message
                DatagramPacket datagramPacket = new DatagramPacket(buffer, 0, buffer.length);
                clientSocket.receive(datagramPacket);
                processingQueue.put(datagramPacket);
                //System.out.println("List:" + new String(datagramPacket.getData()));
            }

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) { //deal with last packet received...
            e.printStackTrace();
        }
    }
}
