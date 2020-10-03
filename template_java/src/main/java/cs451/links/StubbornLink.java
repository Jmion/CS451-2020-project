package cs451.links;

import cs451.Message;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.logging.Logger;

public class StubbornLink implements Link {

    Set<DatagramPacket> unconfirmedMsgSend;
    private final FairLossLink fairLossLink;
    private final LinkedBlockingQueue<DatagramPacket> incomingPackets;
    private final LinkedBlockingQueue<Message> outgoingMessage;
    //private final Map<Integer, Integer> idToLastConsecutiveAck;

    public StubbornLink(FairLossLink fairLossLink, LinkedBlockingQueue<DatagramPacket> incoming, LinkedBlockingQueue<Message> outgoingMessage) {
        this.fairLossLink = fairLossLink;
        this.incomingPackets = incoming;
        this.outgoingMessage = outgoingMessage;
        this.unconfirmedMsgSend = Collections.synchronizedSet(new HashSet<>());
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(this::resendUnACKMsg, 5000, 2000, TimeUnit.MILLISECONDS);
        new Thread(this::receive).start();
    }

    /**
     * Resend unACK msg
     */
    private void resendUnACKMsg() {
        unconfirmedMsgSend.forEach( (dp) ->fairLossLink.send(Message.fromData(dp.getData()), dp.getPort(), dp.getAddress()));
    }

    /**
     * Send ack for msg.
     * @param dp message port object
     */
    private void sendAck(DatagramPacket dp) {
        fairLossLink.send(Message.fromData(dp.getData()).getAck(), dp.getPort(), dp.getAddress());
    }

    @Override
    public void send(Message msg, int destinationPort, InetAddress destinationIP) {
        System.out.println("stubornLink sending:" + msg);
        fairLossLink.send(msg, destinationPort, destinationIP);

        unconfirmedMsgSend.add(new DatagramPacket(
                new Message(msg).getAck(msg.getDestination()),
                Message.MESSAGE_LENGTH,
                inetAddress,
                //InetAddress.getByName("127.0.0.1"),
                destinationPort
        );.);
        System.out.println("Send msg waiting confirmation " + unconfirmedMsgSend);

    }

    @Override
    public void receive() {
        while (true) {
            DatagramPacket dp = null;
            try {
                dp = incomingPackets.take();
                System.out.println("STL: incomming data");
                Message msg = Message.fromData(dp.getData());
                System.out.println(msg);
                if (msg.isAck()) {
                    System.out.println("ACK : " + msg);
                    System.out.println(unconfirmedMsgSend);
                    MsgPort receivedMsg = new MsgPort(msg, dp.getPort());
                    System.out.println("REceived MspPort_ "+ receivedMsg);
                    unconfirmedMsgSend.remove(receivedMsg);
                    System.out.println(unconfirmedMsgSend.size());
                }else {
                    outgoingMessage.add(msg);
                    fairLossLink.send(msg.getAck(), dp.getPort(), dp.getAddress());
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }


}




