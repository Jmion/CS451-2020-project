package cs451;

import java.net.InetAddress;

public class MessageFactory {
    public final int MY_ID;

    public MessageFactory(int myID) {
        this.MY_ID = myID;
    }

    public Message getMessage(int sourceID, int destinationID, int seqNumber, boolean ack, InetAddress destinationIP, int destinationPort) {

    }
}
