package cs451.links;

import cs451.Message;

import java.net.InetAddress;

public interface Link {
    void send(Message msg, int destinationPort, InetAddress inetAddress);

    //to start thread to execute the processing
    void receive();
}
