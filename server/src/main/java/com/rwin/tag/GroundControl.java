package com.rwin.tag;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.Semaphore;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.message.GZipEncoder;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rwin.tag.api.CorsSupportFilter;
import com.rwin.tag.api.ImageProvider;
import com.rwin.tag.util.test.TestLoader;

/**
 * Start a server, attach a client, and send a message.
 */
public class GroundControl {
    public static Logger log = LoggerFactory.getLogger(GroundControl.class);
    public static long start = System.currentTimeMillis();

    public static void main(String[] args) throws IOException,
            InterruptedException {

        // create the parser
        CommandLineParser parser = new GnuParser();
        Options options = new Options();
        try {
            // parse the command line arguments
            CommandLine line = parser.parse(options, args);

            if (line.hasOption("log4j") || line.hasOption('l')) {
                PropertyConfigurator
                        .configureAndWatch(line.getOptionValue('l'));
            } else {
                BasicConfigurator.configure();
            }
        } catch (ParseException exp) {
            // oops, something went wrong
            System.err.println("Parsing failed.  Reason: " + exp.getMessage());
        }
        final String baseUri = "http://0.0.0.0:8080/";

        // Setup JDBC
        try {
            // The newInstance() call is a work around for some
            // broken Java implementations
            // Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception e) {
            log.error("Error registering JDBC driver: message: "
                    + e.getMessage());
            log.error("Catastrophic failure, exiting");
            return;
        }

        System.out.println("Starting grizzly...");
        log.info("Starting grizzly");

        ResourceConfig rc = new ResourceConfig();
        rc.register(CorsSupportFilter.class);
        rc.register(ImageProvider.class);
        rc.register(MultiPartFeature.class);
        rc.packages("com.rwin.tag.api");
        rc.register(GZipEncoder.class);
        final HttpServer server = GrizzlyHttpServerFactory.createHttpServer(
                URI.create(baseUri), rc);
        server.start();

        TestLoader.CreateData();

        // block and wait so we can serve rest stuff.
        Semaphore s = new Semaphore(-1);
        s.acquire();
    }
}
