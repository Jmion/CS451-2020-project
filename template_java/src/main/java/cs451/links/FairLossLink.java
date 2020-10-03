package cs451.links;

import cs451.Message;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.*;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class FairLossLink implements Link{

    private final int port;
    private DatagramSocket datagramSocket;
    private final Queue<DatagramPacket> receptionProcessingQueue;
    private final int MY_ID;

    /**
     *
     * @param portNumber This instances PORT number as defined in the HOST file
     * @param receptionProcessingQueue destination of incomming packets
     * @param myID This instances ID.
     */
    public FairLossLink(int portNumber, Queue<DatagramPacket> receptionProcessingQueue, int myID) {
        this.port = portNumber;
        this.receptionProcessingQueue = receptionProcessingQueue;
        this.MY_ID = myID;
        DatagramSocket datagramSocket = null;
        try {
            datagramSocket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        this.datagramSocket = datagramSocket;
        new Thread(this::receive).start();
    }

    public synchronized void send(Message msg, int destinationPort, InetAddress inetAddress){
        try {
            this.send(prepareDatagramPacket(msg, destinationPort, inetAddress));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(DatagramPacket datagramPacket) throws IOException {
        datagramSocket.send(datagramPacket);
    }

    public DatagramPacket prepareDatagramPacket(Message msg, int destinationPort, InetAddress inetAddress){
        msg.setLastHop(MY_ID);
        return new DatagramPacket(
                msg.getByte(),
                Message.MESSAGE_LENGTH,
                inetAddress,
                //InetAddress.getByName("127.0.0.1"),
                destinationPort
        );
    }

    public void receive(){
        //noinspection InfiniteLoopStatement
        while(true){
            byte[] buffer = new byte[65507];
            DatagramPacket datagramPacket = new DatagramPacket(buffer, 0, buffer.length);
            try {
                datagramSocket.receive(datagramPacket);
                receptionProcessingQueue.add(datagramPacket);
                System.out.println("data received");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
