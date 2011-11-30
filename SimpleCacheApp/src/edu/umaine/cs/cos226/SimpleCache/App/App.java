/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umaine.cs.cos226.SimpleCache.App;

import edu.umaine.cs.cos226.SimpleCache.Client.CacheServer;
import edu.umaine.cs.cos226.SimpleCache.Client.ClientConnection;
import edu.umaine.cs.cos226.SimpleCache.RetrievePacket;
import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.util.LinkedList;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.ini4j.Wini;

/**
 *
 * @author Edwards
 */
public class App {
    
    static boolean retrieved = false;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        //Options opts = new Options();
        OptionGroup helpopts = new OptionGroup();
        
        helpopts.addOption(OptionBuilder
                .withDescription("Prints this message.")
                .create("help"));
        
        OptionGroup cmd = new OptionGroup();
        cmd.addOption(OptionBuilder
                .withArgName("key")
                .withDescription("Stores a value to the server for the specified key")
                .hasArg()
                .create("store"));
        cmd.addOption(OptionBuilder
                .withArgName("key")
                .withDescription("Deletes a value to the server for the specified key")
                .hasArg()
                .create("delete"));
        cmd.addOption(OptionBuilder
                .withArgName("key")
                .withDescription("Retrieves a value from the server for the specified key")
                .hasArg()
                .create("retrieve"));
        
        Options opts = new Options();
        opts.addOptionGroup(helpopts);
        opts.addOptionGroup(cmd);
        
        opts.addOption(OptionBuilder.withArgName("file")
                .hasArg()
                .create("config"));
        
        PosixParser parser = new PosixParser();

        try
        {
            CommandLine cli = parser.parse(opts, args);
            
            if (cli.hasOption("help"))
            {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("SimpleCacheApp [-config FILE] -[store|retrieve|delete] KEY [NEWVAL]", opts);
                return;
            }

            Wini ini = new Wini(cli.hasOption("config") ? 
                    new File(cli.getOptionValue("config")) : 
                    new File("simplecache.ini"));
            
            String[] serverIPs = ini.get("client").getAll("server[]", String[].class);
            LinkedList<CacheServer> servers = new LinkedList<CacheServer>();
            for (String serverAddr : serverIPs)
            {
                int split = serverAddr.lastIndexOf(':');
                if (split == -1) throw new Exception("Invalid format for server address: " + serverAddr);
                int port = Integer.parseInt(serverAddr.substring(split + 1));
                String host = serverAddr.substring(0, split);
                
                servers.add(new CacheServer(new InetSocketAddress(host, port)));
            }
            
            ClientConnection conn = new ClientConnection(servers.toArray(new CacheServer[]{}));
            
            if (cli.hasOption("store")) {
                if (cli.getArgs().length < 1)
                {
                    throw new Exception("store command requires a value to be specified.");
                }
                conn.put(cli.getOptionValue("store"), cli.getArgs()[1]);
            }
            else if (cli.hasOption("delete")) {
                conn.delete(cli.getOptionValue("delete"));
            }
            else if (cli.hasOption("retrieve")) {
                conn.get(cli.getOptionValue("retrieve"), new RetrievePacket.RetrieveListener() {
                    @Override
                    public void valueRetrieved(String v) {
                        retrieved = true;
                        System.out.println(v);
                    }
                });
                conn.poll();
            }
        }
        catch (ConnectException ex) {
            System.err.println("Could not connect to server:");
            System.err.println(ex.toString());
        }
        catch (IOException ex)
        {
            System.err.println("Could not open config file:");
            System.err.println(ex.toString());
        }
        catch (Exception ex)
        {
            System.err.println(ex.toString());
        }
    }
}
