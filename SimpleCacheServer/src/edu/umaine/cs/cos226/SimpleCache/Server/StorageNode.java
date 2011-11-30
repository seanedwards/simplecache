/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umaine.cs.cos226.SimpleCache.Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.log4j.Logger;

/**
 *
 * @author Edwards
 */
public class StorageNode {
    Map<String, String> vals = new HashMap<String, String>();
    ServerSocket sock;
    boolean running = true;
    
    public StorageNode(SocketAddress addr) throws IOException
    {
        sock = new ServerSocket();
        sock.bind(addr);
        sock.setSoTimeout(10);
    }
    
    public void run()
    {
        while (running) {
            try {
                Socket csock = sock.accept();
                if (csock == null) continue;
                ClientHandler client = new ClientHandler(this, csock);

                client.start();
            }
            catch (SocketTimeoutException ex) {
                // expected
            }
            catch (IOException ex) {
                Logger.getLogger(StorageNode.class).error(ex, ex);
            }
        }
        
        try {
            sock.close();
        }
        catch (IOException ex) {
            Logger.getLogger(StorageNode.class).warn(ex, ex);
        }
    }
    
    public void stop()
    {
        this.running = false;
    }
    
    synchronized public void put(String k, String v) {
        Logger.getLogger(StorageNode.class).debug("Stored " + k + ":" + v);
        vals.put(k, v);
    }
    
    synchronized public String get(String k) {
        Logger.getLogger(StorageNode.class).debug("Retrieved " + k);
        return vals.get(k);
    }
    
    synchronized public void delete(String k) {
        Logger.getLogger(StorageNode.class).debug("Deleted " + k);
        vals.remove(k);
    }
}
