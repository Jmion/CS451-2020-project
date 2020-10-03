package cs451;

import cs451.links.FairLossLink;
import cs451.links.StubbornLink;

import java.net.DatagramPacket;
import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ExecutorService;

public class Test {
    public static void main(String[] args) throws InterruptedException {

        ExecutorService executorService = Executors.newCachedThreadPool();


        LinkedBlockingQueue<DatagramPacket> incomingDatagramQueue1 = new LinkedBlockingQueue<>();
        LinkedBlockingQueue<Message> outgoing = new LinkedBlockingQueue<>();
        FairLossLink fl1 = new FairLossLink(11500, incomingDatagramQueue1, 0);
        StubbornLink sb1 = new StubbornLink(fl1, incomingDatagramQueue1, outgoing);

        sb1.send(new Message(0,1,0, 255,false), 11001);


        LinkedBlockingQueue<DatagramPacket> incomingDatagramQueue2 = new LinkedBlockingQueue<>();
        LinkedBlockingQueue<Message> outgoing2 = new LinkedBlockingQueue<>();
        FairLossLink fl2 = new FairLossLink(11001, incomingDatagramQueue2, 1);
        StubbornLink sb2 = new StubbornLink(fl2, incomingDatagramQueue2, outgoing2);
        fl2.send(new Message(1,0,1,1,false), 11500);

/*        Listener l1 = new Listener(11500, "list1", incomingDatagramQueue);
        Sender s2 = new Sender("send1", 11500);

        l1.start();
        Thread.sleep(1000);
        s2.start();*/

        while(true){
            byte[] d = outgoing.take().getByte();
            System.out.println(Arrays.toString(Message.fromData(d).getByte()));
            System.out.println(Message.fromData(d).toString());
        }
    }
}
