/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umaine.cs.cos226.SimpleCache.Server;


import java.io.File;
import java.net.InetSocketAddress;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.ini4j.Wini;

/**
 *
 * @author Edwards
 */
public class Main {

    public static final Logger logger = Logger.getLogger(Main.class);
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BasicConfigurator.configure();
        
        Options opts = new Options();
        PosixParser parser = new PosixParser();
        
        opts.addOption(OptionBuilder.withArgName("file")
                .hasArg()
                .create("config"));
        
        opts.addOption(OptionBuilder.withArgName("id")
                .hasArg()
                .create("serverid"));
        
        Logger.getRootLogger().setLevel(Level.ALL);
        
        try
        {
            CommandLine cli = parser.parse(opts, args);
            logger.debug("SimpleCache server initialized.");
            
            Wini ini = new Wini(cli.hasOption("config") ? 
                    new File(cli.getOptionValue("config")) : 
                    new File("simplecache.ini"));
            
            String serverSection = "server" + 
                    (cli.hasOption("serverid") ? 
                        ("." + cli.getOptionValue("serverid")) : 
                        "");
            
            StorageNode node = new StorageNode(
                    new InetSocketAddress(
                        ini.get(serverSection, "host"), 
                        ini.get(serverSection, "port", int.class)));
            node.run();
        }
        catch (Exception ex)
        {
            logger.fatal(ex, ex);
        }
        
    }
}
