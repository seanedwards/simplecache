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
public class ResponsePacket extends Packet {
    boolean isError;
    String message;
    int id = 0;
    
    public ResponsePacket(int id, boolean isErr, String msg)
    {
        super(Packet.Type.RESPONSE);
        this.id = id;
        this.isError = isErr;
        this.message = msg;
    }
    
    public int getID() { return this.id; }
    public String getMessage() { return this.message; }
    public boolean isError() { return this.isError; }
    
    @Override
    public void send(DataOutputStream s) throws IOException
    {
        super.send(s);
        s.writeInt(id);
        s.writeBoolean(this.isError);
        s.writeUTF(this.message);
        s.flush();
    }
    
}
