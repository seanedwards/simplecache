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
public class StorePacket extends Packet {
    String key;
    String value;
    
    public StorePacket(String key, String val)
    {
        super(Packet.Type.STORE);
        this.key = key;
        this.value = val;
    }
    
    public String getKey() { return this.key; }
    public String getValue() { return this.value; }
    
    @Override
    public void send(DataOutputStream s) throws IOException
    {
        super.send(s);
        s.writeUTF(this.key);
        s.writeUTF(this.value);
    }
    
}
