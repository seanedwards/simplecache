package edu.umaine.cs.cos226.SimpleCache.Server;


import edu.umaine.cs.cos226.SimpleCache.Server.StorageNode;
import java.io.IOException;
import java.net.SocketAddress;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Edwards
 */
public class ThreadNode extends Thread {
    public StorageNode node;
    
    public ThreadNode(SocketAddress addr) throws IOException
    {
        this.node = new StorageNode(addr);
    }
    
    @Override
    public void run()
    {
        node.run();
    }
    
    public void halt()
    {
        node.stop();
    }
}
