package com.commons.cli.test;

import org.apache.commons.cli.*;

/**
 * Created by lcc on 2018/9/15.
 */
public class CommonsCliTest {

    private static void main(String[] args) {

        final Options options = new Options();
        final Option option = new Option("f", true, "Configuration file path");
        options.addOption(option);

        final CommandLineParser parser = new PosixParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch ( ParseException e) {
//            throw new Exception("parser command line error",e);
        }

        String configFilePath = null;
        if (cmd.hasOption("f")) {
            configFilePath = cmd.getOptionValue("f");
        }else{
            System.err.println("please input the configuration file path by -f option");
            System.exit(1);
        }


//        return configFilePath;
    }

}
