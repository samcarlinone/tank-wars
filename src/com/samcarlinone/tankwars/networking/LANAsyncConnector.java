package com.samcarlinone.tankwars.networking;

import sun.rmi.transport.tcp.TCPConnection;

import java.io.IOException;

/**
 * Created by CARLINSE1 on 1/31/2017.
 */
public class LANAsyncConnector {

    private MulticastDiscoveryThread lan_discovery;
    private TCPThread conn;
    private String host = "";
    private boolean searching = true;
    private boolean returned = false;

    public LANAsyncConnector() {
        try {
            lan_discovery = new MulticastDiscoveryThread();
            lan_discovery.start();
        } catch(IOException e) {
            System.out.println("Network error");
        }
    }

    /**
     * Returns TCPThread or null if no peer found yet
     * @return
     */
    public TCPThread connect() {
        try {
            if(searching) {
                host = lan_discovery.queue.poll();

                if(host == null)
                    return null;

                conn = new TCPThread(host);
                conn.start();
                searching = false;
            }

            if(returned)
                return null;

            if(host == "") {
                if(conn.rx_queue.peek() != null) {
                    lan_discovery.queue.put("terminate");

                    System.out.println("Connectd to");

                    conn.rx_queue.poll();
                    returned = true;
                    return conn;
                }
            } else {
                System.out.println("Connecting from");
                //Send message to signal connection
                conn.tx_queue.put("Hello world.");
                returned = true;
                return conn;
            }
        } catch (InterruptedException e) {
            System.out.println("Threading Error");
        }

        return null;
    }
}