/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umaine.cs.cos226.SimpleCache.Server;

import edu.umaine.cs.cos226.SimpleCache.DeletePacket;
import edu.umaine.cs.cos226.SimpleCache.Packet;
import edu.umaine.cs.cos226.SimpleCache.ResponsePacket;
import edu.umaine.cs.cos226.SimpleCache.RetrievePacket;
import edu.umaine.cs.cos226.SimpleCache.StorePacket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import org.apache.log4j.Logger;

/**
 *
 * @author Edwards
 */
public class ClientHandler extends Thread {
    DataInputStream inStream;
    DataOutputStream outStream;
    StorageNode node;
    public ClientHandler(StorageNode spawner, Socket s) throws IOException
    {
        this.node = spawner;
        this.inStream = new DataInputStream(s.getInputStream());
        this.outStream = new DataOutputStream(s.getOutputStream());
    }
    
    @Override
    public void run()
    {
        try
        {
            while (true) {
                Packet p = Packet.readPacket(this.inStream);

                try
                {
                    switch(p.getType())
                    {
                        case STORE:
                            StorePacket sp = (StorePacket)p;
                            this.node.put(sp.getKey(), sp.getValue());
                            break;
                        case RETRIEVE:
                            RetrievePacket rp = (RetrievePacket)p;
                            String dat = this.node.get(rp.getKey());
                            ResponsePacket rsp = null;
                            if (dat != null)
                                rsp = new ResponsePacket(rp.getID(), false, dat);
                            else
                                rsp = new ResponsePacket(rp.getID(), true, "No such value present.");
                            rsp.send(this.outStream);
                            break;
                        case DELETE:
                            DeletePacket dp = (DeletePacket)p;
                            this.node.delete(dp.getKey());
                            break;
                    }
                }
                catch (Exception ex)
                {
                    Logger.getLogger(ClientHandler.class).error(ex, ex);
                    ResponsePacket rsp = new ResponsePacket(0, true, ex.getMessage());
                    rsp.send(this.outStream);
                }
            }
        }
        catch (SocketException ex) {
            Logger.getLogger(ClientHandler.class).warn(ex, ex);
        }
        catch (Exception ex)
        {
            Logger.getLogger(ClientHandler.class).error(ex, ex);
        }
        finally
        {
            try
            {
                this.inStream.close();
                this.outStream.close();
            }
            catch (IOException ex)
            {
                Logger.getLogger(ClientHandler.class).warn(ex, ex);
            }
        }
    }
}
