package com.simplemethod.dhx;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

@Component
@CommandLine.Command(name = "university", mixinStandardHelpOptions = true, version = "1.0 dev")
public class MyCommand implements Runnable {

    @Autowired
    MainViewCassandra mainViewCassandra;

    @Autowired
    MainViewInfluxDB mainViewInfluxDB;

    @CommandLine.Option(names = {"-t", "--type"}, description = "Type of database I=InfluxDB C=Cassandra")
    private String[] type = new String[]{"C"};

    @SneakyThrows
    public void run() {

        if(type[0].equals("C") || type[0].equals("c"))
        {

           mainViewCassandra.menu();
           // mainViewInfluxDB.menu();
        }
        else if(type[0].equals("I") || type[0].equals("i"))
        {
            mainViewInfluxDB.menu();
        }
        else
        {
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Nie wybrano żadnego skłądu!|@"));
            System.exit(1);
        }
    }


}