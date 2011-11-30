/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import org.apache.log4j.BasicConfigurator;
import edu.umaine.cs.cos226.SimpleCache.Client.ClientConnection;
import edu.umaine.cs.cos226.SimpleCache.Client.CacheServer;
import edu.umaine.cs.cos226.SimpleCache.RetrievePacket;
import java.io.IOException;
import java.net.InetSocketAddress;
import edu.umaine.cs.cos226.SimpleCache.Server.StorageNode;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Edwards
 */
public class ServerTest {
    
    public ServerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        BasicConfigurator.configure();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
        nodes = new ThreadNode[] {null, null, null, null, null};
        servers = new CacheServer[] {null, null, null, null, null};
        try {
            for (int x = 0; x < 5; ++x)
            {
                nodes[x] = new ThreadNode(new InetSocketAddress("127.0.0.1", 1231 + x));
                nodes[x].start();
                servers[x] = new CacheServer(new InetSocketAddress("127.0.0.1", 1231 + x));
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        conn = new ClientConnection(servers);
    }
    
    ThreadNode[] nodes;
    CacheServer[] servers;
    ClientConnection conn;
    
    @After
    public void tearDown() throws InterruptedException {
        for (ThreadNode n : nodes)
        {
            n.halt();
            n.join();
        }
    }
    
    @Test
    public void SingleTest() throws Exception
    {
        String[] alphabet = new String[]{
            "a",
            "b",
            "c",
            "d",
            "e",
            "f",
            "g",
            "h",
            "i",
            "j",
            "k",
            "l",
            "m",
            "n",
            "o",
            "p",
            "q",
            "r",
            "s",
            "t",
            "u",
            "v",
            "w",
            "x",
            "y",
            "z"
        };
        
        for (String s : alphabet)
            conn.put(s, s.toUpperCase());
        
        for (final String s : alphabet)
        {
            conn.get(s, new RetrievePacket.RetrieveListener() {

                @Override
                public void valueRetrieved(String v) {
                    Assert.assertEquals(s.toUpperCase(), v);
                }
            });
            conn.poll();
            conn.delete(s);
        }
    }
}
