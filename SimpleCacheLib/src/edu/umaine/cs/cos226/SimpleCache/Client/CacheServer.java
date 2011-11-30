/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umaine.cs.cos226.SimpleCache.Client;

import edu.umaine.cs.cos226.SimpleCache.DeletePacket;
import edu.umaine.cs.cos226.SimpleCache.Packet;
import edu.umaine.cs.cos226.SimpleCache.ResponsePacket;
import edu.umaine.cs.cos226.SimpleCache.RetrievePacket;
import edu.umaine.cs.cos226.SimpleCache.StorePacket;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Edwards
 */
public class CacheServer {
    Socket client;
    SocketAddress endpoint;
    DataInputStream inStream;
    DataOutputStream outStream;
    
    Map<Integer, RetrievePacket> pendingRetrievals = new HashMap<Integer, RetrievePacket>();
    
    public CacheServer(SocketAddress srv)
    {
        endpoint = srv;
        client = new Socket();
    }
    
    public void connect() throws IOException {
        client.connect(endpoint);
        this.inStream = new DataInputStream(client.getInputStream());
        this.outStream = new DataOutputStream(client.getOutputStream());
        client.setSoTimeout(10);
    }
    
    public void poll() throws Exception {
        if (inStream == null) return;
        try
        {
            Packet p = Packet.readPacket(inStream);
            if (p.getType() == Packet.Type.RESPONSE)
            {
                ResponsePacket rp = (ResponsePacket)p;
                if (rp.isError()) {
                    throw new Exception(rp.getMessage());
                }
                
                RetrievePacket retp = pendingRetrievals.get(rp.getID());
                if (retp != null) {
                    pendingRetrievals.remove(rp.getID());
                    retp.getListener().valueRetrieved(rp.getMessage());
                }
            }
        }
        catch (SocketTimeoutException ex)
        {
            // Ignore.
        }
    }
    
    void checkConnect() throws IOException {
        if (!client.isConnected()) connect();
    }
    
    public void put(String k, String v) throws IOException {
        checkConnect();
        new StorePacket(k, v).send(this.outStream);
    }
    
    public void get(String k, RetrievePacket.RetrieveListener l) throws IOException {
        checkConnect();
        RetrievePacket p = new RetrievePacket(k, l);
        this.pendingRetrievals.put(p.getID(), p);
        p.send(outStream);
    }
    
    public void delete(String k) throws IOException {
        checkConnect();
        new DeletePacket(k).send(this.outStream);
    }
}
