package cs451;

import java.net.InetAddress;
import java.nio.ByteBuffer;

public class Message {
    public static final int MESSAGE_LENGTH =17;
    int sourceID, destinationID, lastHop, sequenceNumber, destinationPort;
    InetAddress destinationIP;
    boolean ack;

    public Message(int sourceID, int destinationID, int lastHop, int destinationPort, InetAddress destinationIP, int sequenceNumber, boolean ack) {
        this.sourceID = sourceID;
        this.destinationID = destinationID;
        this.lastHop = lastHop;
        this.sequenceNumber = sequenceNumber;
        this.ack = ack;
    }

    public Message(int source, int destination, int sequenceNumber, boolean ack) {
        this(source, destination, -1, sequenceNumber, ack);
    }

    public Message(Message message) {
        this(message.source, message.destination, message.lastHop, message.sequenceNumber, message.ack);
    }
    public Message getAck(){
        Message msg = new Message(this);
        msg.ack = true;
        return msg;
    }


    public void setLastHop(int lastHop) {
        this.lastHop = lastHop;
    }

    public int getSource() {
        return source;
    }

    public int getDestination() {
        return destination;
    }

    public int getLastHop() {
        return lastHop;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }


    public byte[] getByte() {
        ByteBuffer bb = ByteBuffer.allocate(MESSAGE_LENGTH);
        bb.putInt(0, source);
        bb.putInt(4, destination);
        bb.putInt(8, lastHop);
        bb.putInt(12, sequenceNumber);
        bb.put(16, (byte)(ack ? 0x1 : 0x0));
        return bb.array();
    }

    public static Message fromData(byte[] data) {
        ByteBuffer bb = ByteBuffer.wrap(data);
        int source = bb.getInt(0);
        int destination = bb.getInt(4);
        int lastHop = bb.getInt(8);
        int sequenceNumber = bb.getInt(12);
        boolean ack = bb.get(16) != 0x00;
        return new Message(source, destination, lastHop, sequenceNumber, ack);
    }

    public boolean isAck(){
        return ack;
    }

    @Override
    public String toString() {
        return "Message{" +
                "source=" + source +
                ", destination=" + destination +
                ", lastHop=" + lastHop +
                ", sequenceNumber=" + sequenceNumber +
                ", ack=" + ack +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if (source != message.source) return false;
        if (destination != message.destination) return false;
        if (lastHop != message.lastHop) return false;
        return sequenceNumber == message.sequenceNumber;
    }

    @Override
    public int hashCode() {
        int result = source * 2;
        result =  result + 3 * destination;
        result = result + 5 * lastHop;
        result =  result + 7 *sequenceNumber;
        return result;
    }
}
