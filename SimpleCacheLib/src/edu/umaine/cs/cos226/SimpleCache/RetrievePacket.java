/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umaine.cs.cos226.SimpleCache;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 *
 * @author Edwards
 */
public class RetrievePacket extends Packet {
    String key;
    int id;
    RetrieveListener listener;
    
    public interface RetrieveListener {
        void valueRetrieved(String v);
    }
    
    public RetrievePacket(String key, RetrieveListener l)
    {
        super(Packet.Type.RETRIEVE);
        Random rand = new Random();
        this.id = rand.nextInt();
        this.listener = l;
        this.key = key;
    }
    
    public RetrievePacket(int id, String key, RetrieveListener l)
    {
        super(Packet.Type.RETRIEVE);
        this.id = id;
        this.listener = l;
        this.key = key;
    }
    
    public String getKey() { return this.key; }
    public int getID() { return this.id; }
    public RetrieveListener getListener() { return this.listener; }
    
    @Override
    public void send(DataOutputStream s) throws IOException
    {
        super.send(s);
        s.writeInt(this.id);
        s.writeUTF(this.key);
    }
    
}
