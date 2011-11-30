/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umaine.cs.cos226.SimpleCache.Client;

import edu.umaine.cs.cos226.SimpleCache.RetrievePacket;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Edwards
 */
public class ClientConnection {
    CacheServer[] servers;
    
    public ClientConnection(CacheServer server)
    {
        this.servers = new CacheServer[] { server };
    }
    
    public ClientConnection(CacheServer[] servers)
    {
        this.servers = servers;
    }
    
    public void put(String k, String v) throws IOException {
        servers[k.hashCode() % servers.length].put(k, v);
    }
    
    public void get(String k, RetrievePacket.RetrieveListener l) throws IOException {
        servers[k.hashCode() % servers.length].get(k, l);
    }
    
    public void delete(String k) throws IOException {
        servers[k.hashCode() % servers.length].delete(k);
    }
    
    public void poll() throws Exception
    {
        for (CacheServer s : servers) {
            s.poll();
        }
    }
}
