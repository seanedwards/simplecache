/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umaine.cs.cos226.SimpleCache;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 *
 * @author Edwards
 */
public abstract class Packet {
    public enum Type
    {
        INVALID,
        STORE,
        RETRIEVE,
        DELETE,
        
        RESPONSE
    }
    
    private Type packetType = Type.INVALID;
    
    public Packet(Type t)
    {
        this.packetType = t;
    }
    
    public static Packet readPacket(DataInputStream r) throws IOException
    {
        int ptype = (int)r.readByte();
        if (ptype >= Type.values().length)
            throw new IOException("Invalid packet type received.");
        
        Type t = Type.values()[ptype];
        Packet ret = null;
        switch (t)
        {
            case STORE:
                String k = r.readUTF();
                String v = r.readUTF();
                ret = new StorePacket(k, v);
                break;
            case RETRIEVE:
                int id = r.readInt();
                String key = r.readUTF();
                ret = new RetrievePacket(id, key, null);
                break;
            case DELETE:
                ret = new DeletePacket(r.readUTF());
                break;
            case RESPONSE:
                int rid = r.readInt();
                boolean isErr = r.readBoolean();
                String msg = r.readUTF();
                ret = new ResponsePacket(rid, isErr, msg);
                break;
            default:
                throw new IOException("Invalid packet type received.");
        }
        
        return ret;
    }
    
    public Type getType() { return this.packetType; }
    
    public void send(DataOutputStream s) throws IOException
    {
        s.writeByte((byte)this.packetType.ordinal());
    }
}
