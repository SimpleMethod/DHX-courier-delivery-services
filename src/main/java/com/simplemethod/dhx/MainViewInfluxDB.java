package com.simplemethod.dhx;

import com.simplemethod.dhx.InfluxDBDAO.InfluxClientDAO;
import com.simplemethod.dhx.InfluxDBDAO.InfluxEmployeeDAO;
import com.simplemethod.dhx.InfluxDBDAO.InfluxParcelsDAO;
import com.simplemethod.dhx.InfluxDBDataModel.ClientModel;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import picocli.CommandLine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Controller
public class MainViewInfluxDB {

    private InfluxDB influxDB;

    @Autowired
    InfluxDBConfig influxDBConfig;

    @Autowired
    InfluxClientDAO influxClientDAO;

    @Autowired
    InfluxEmployeeDAO influxEmployeeDAO;

    @Autowired
    InfluxParcelsDAO influxParcelsDAO;

    public void menu() throws IOException {
        influxDBConfig.init();
        influxDB = influxDBConfig.getInfluxDB();
        influxClientDAO.setInfluxDB(influxDB);
        influxEmployeeDAO.setInfluxDB(influxDB);
        influxParcelsDAO.setInfluxDB(influxDB);
        for (;;) {
            System.out.print("\n");
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(green) Wybierz pozycję z menu:|@"));
            System.out.print("\n");
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(blue)  [1]|@ Wyszukiwanie wszystkich klientow"));
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(blue)  [2]|@ Wyszukiwanie wszystkich kierowcow po numerze telefonu"));
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(blue)  [3]|@ Wyszukiwanie wszystkich paczek"));
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(blue)  [4]|@ Dodanie nowego klienta"));
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(blue)  [5]|@ Zmiana imienia klienta"));
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(blue)  [6]|@ Usunięcie klienta"));
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(blue)  [7]|@ Wyszukiwanie złożone-Wyszukwanie paczki według klienta"));
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(blue)  [8]|@ Zmiana numeru nadania paczki "));
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red)  [9]|@ Zamknięcie programu"));
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String input = br.readLine();
            if (isNumeric(input)) {
                int parseInt = Integer.parseInt(input);
                switch (parseInt) {
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    case 7:
                        break;
                    case 8:
                        break;
                    case 9:
                        Runtime.getRuntime().exit(1);
                        break;
                    default:
                        break;
                }
            }

        }
    }
    private final Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    /**
     * Checking if string is a number.
     *
     * @param strNum String to check.
     * @return True of false.
     */
    public boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }

}
