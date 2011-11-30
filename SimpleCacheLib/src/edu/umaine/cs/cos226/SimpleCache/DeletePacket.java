/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umaine.cs.cos226.SimpleCache;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author Edwards
 */
public class DeletePacket extends Packet {
    String key;
    
    public DeletePacket(String key)
    {
        super(Packet.Type.DELETE);
        this.key = key;
    }
    
    public String getKey() { return this.key; }
    
    @Override
    public void send(DataOutputStream s) throws IOException
    {
        super.send(s);
        s.writeUTF(this.key);
    }
}
