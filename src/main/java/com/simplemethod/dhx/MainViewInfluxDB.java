package com.simplemethod.dhx;

import com.simplemethod.dhx.InfluxDBDataModel.ParcelsModel;
import com.simplemethod.dhx.InfluxDBDataModel.EmployeeModel;
import com.simplemethod.dhx.InfluxDBDAO.InfluxClientDAO;
import com.simplemethod.dhx.InfluxDBDAO.InfluxEmployeeDAO;
import com.simplemethod.dhx.InfluxDBDAO.InfluxParcelsDAO;
import com.simplemethod.dhx.InfluxDBDataModel.ClientModel;
import org.influxdb.InfluxDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import picocli.CommandLine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.List;
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
        for (; ; ) {
            System.out.print("\n");
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(green) Wybierz pozycję z menu:|@"));
            System.out.print("\n");
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(blue)  [1]|@ Wyszukiwanie wszystkich klientow"));
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(blue)  [2]|@ Wyszukiwanie wszystkich kierowcow po numerze telefonu"));
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(blue)  [3]|@ Wyszukiwanie wszystkich paczek"));
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(blue)  [4]|@ Dodanie nowego klienta"));
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(blue)  [5]|@ Zmiana imienia klienta"));
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(blue)  [6]|@ Usunięcie klienta"));
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(blue)  [7]|@ Wyszukwanie paczki według miasta"));
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(blue)  [8]|@ Zmiana nazwiska pracownika "));
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(blue)  [9]|@ Wyszukiwanie wszystkich kierowców"));
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(blue)  [10]|@ Dodanie nowego kierowcy"));
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(blue)  [11]|@ Dodanie nowej paczki"));
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red)  [12]|@ Zamknięcie programu"));

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String input = br.readLine();
            if (isNumeric(input)) {
                int parseInt = Integer.parseInt(input);
                switch (parseInt) {
                    case 1:
                        findAllClients();
                        break;
                    case 2:
                        findEmployeesByPhoneNumber();
                        break;
                    case 3:
                        findAllParcels();
                        break;
                    case 4:
                        addNewClient();
                        break;
                    case 5:
                        ChangeClientName();
                        break;
                    case 6:
                        removeClient();
                        break;
                    case 7:
                        findAllParcelsByCity();
                        break;
                    case 8:
                        setSurname();
                        break;
                    case 10:
                        addNewEmployee();
                        break;
                    case 11:
                        addNewParcels();
                        break;
                    case 12:
                        Runtime.getRuntime().exit(1);
                        break;
                    case 9:
                        findAllEmployee();
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public void findAllEmployee() {
        List<EmployeeModel> clientModels = influxEmployeeDAO.findAll();
        clientModels.forEach(cl -> System.out.println(cl.toString() + "\r\n"));
    }

    public void findAllClients() {
        List<ClientModel> clientModels = influxClientDAO.findAll();
        clientModels.forEach(cl -> System.out.println(cl.toString() + "\r\n"));
    }

    public void findEmployeesByPhoneNumber() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(white) Menu wyszukiwania kierowców|@"));
        System.out.println("Telefon kierowcy:");
        String phoneNumber = br.readLine();
        try {
            if (phoneNumber.isEmpty()) {
                System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Dane nie mogą być puste!|@"));
                return;
            }
        } catch (NullPointerException e) {
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Dane nie mogą być puste!|@"));
            return;
        }

        if (isNumeric(phoneNumber)) {
            List<EmployeeModel> employeeModels = influxEmployeeDAO.findAllByPhoneNumber(Integer.parseInt(phoneNumber));
            employeeModels.forEach(cl -> System.out.println(cl.toString() + "\r\n"));
            if (employeeModels.isEmpty()) {
                System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Brak kierowcy o podanym numerze!|@"));
            }
        } else {
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Numer telefonu musi być liczbą!|@"));
        }

    }

    public void findAllParcels() {
        List<ParcelsModel> parcelsModels = influxParcelsDAO.findAll();
        for (ParcelsModel parcelsModel : parcelsModels) {
            System.out.println("Identyfikator rekordu:" + String.format("%019d", BigInteger.valueOf(parcelsModel.getParcel_id().toEpochMilli())));
            System.out.println("Identyfikator rejonu paczki:" + parcelsModel.getIdentyfikator_rejonu_paczki());
            System.out.println("Miasto:" + parcelsModel.getMiasto());
            System.out.println("Ulica:" + parcelsModel.getUlica());
            System.out.println("Numer domu/mieszkania:" + parcelsModel.getNumer_domu());
            System.out.println("Pobranie:" + parcelsModel.getPobranie());
            System.out.println("Ilość paczek:" + parcelsModel.getIlosc_paczek());
        }
    }
    public  void addNewParcels() throws  IOException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(white) Menu dodawania nowej paczki|@"));
        System.out.print("\n");
        System.out.println("Identyfikator rejonu paczki:");
        String trackingID = br.readLine();
        System.out.println("Identyfikator klienta:");
        String clientID= br.readLine();
        System.out.println("Identyfikator kierowcy:");
        String employeeID = br.readLine();
        System.out.println("Miasto:");
        String city = br.readLine();
        System.out.println("Ulica:");
        String street = br.readLine();
        System.out.println("Numer domu:");
        String houseNumber = br.readLine();
        System.out.println("Ilosc paczek:");
        String numberOfParcels = br.readLine();
        System.out.println("Kwota pobrania:");
        String cost = br.readLine();
        try {
            if (trackingID == null || clientID == null || employeeID==null || city==null || street==null || houseNumber==null || numberOfParcels==null || cost==null) {
                System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Dane nie mogą być puste!|@"));
                return;
            }
        } catch (NullPointerException e) {
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Dane nie mogą być puste!|@"));
            return;
        }
        if (isNumeric(trackingID)) {

            influxParcelsDAO.saveParcels(Long.parseLong(clientID),Long.parseLong(employeeID),city,street,Integer.parseInt(houseNumber),Integer.parseInt(numberOfParcels),Integer.parseInt(cost),Integer.parseInt(trackingID));
        } else {
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Numer telefonu być liczbą!|@"));
        }
    }
    public void addNewEmployee() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(white) Menu dodawania nowego klienta|@"));
        System.out.print("\n");
        System.out.println("Imie:");
        String firstName = br.readLine();
        System.out.println("Nazwisko:");
        String secondName = br.readLine();
        System.out.println("Telefon:");
        String phoneNumber = br.readLine();
        try {
            if (firstName == null || secondName == null || phoneNumber.isEmpty()) {
                System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Dane nie mogą być puste!|@"));
                return;
            }
        } catch (NullPointerException e) {
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Dane nie mogą być puste!|@"));
            return;
        }
        if (isNumeric(phoneNumber)) {
            influxEmployeeDAO.saveEmployee(firstName, secondName, Integer.parseInt(phoneNumber));
        } else {
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Numer telefonu być liczbą!|@"));
        }
    }

    public void addNewClient() throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(white) Menu dodawania nowego klienta|@"));
        System.out.print("\n");
        System.out.println("Imie:");
        String firstName = br.readLine();
        System.out.println("Nazwisko:");
        String secondName = br.readLine();
        System.out.println("Miasto:");
        String city = br.readLine();
        System.out.println("Ulica:");
        String street = br.readLine();
        System.out.println("Numer domu:");
        String houseNumber = br.readLine();
        System.out.println("Telefon:");
        String phoneNumber = br.readLine();

        try {
            if (city == null || firstName == null || secondName == null || street == null || houseNumber.isEmpty() || phoneNumber.isEmpty()) {
                System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Dane nie mogą być puste!|@"));
                return;
            }
        } catch (NullPointerException e) {
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Dane nie mogą być puste!|@"));
            return;
        }

        if (isNumeric(phoneNumber) && isNumeric(houseNumber)) {
            influxClientDAO.saveCustomer(firstName, secondName, city, street, Integer.parseInt(houseNumber), Integer.parseInt(phoneNumber));
        } else {
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Numer telefonu oraz numer domu musi być liczbą!|@"));
        }
    }

    public void ChangeClientName() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(white) Podaj nowe imie|@"));
        System.out.print("\n");
        System.out.println("Imie:");
        String firstName = br.readLine();
        System.out.println("Identyfikator klienta:");
        String uuid = br.readLine();
        try {
            if (firstName == null || uuid == null) {
                System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Dane nie mogą być puste!|@"));
                return;
            }
        } catch (NullPointerException e) {
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Dane nie mogą być puste!|@"));
            return;
        }
        BigInteger bigInteger = new BigInteger(uuid);
        influxClientDAO.setNameByUUID(firstName, bigInteger.longValue());
    }

    void removeClient() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Usuwanie klienta|@"));
        System.out.println("Identyfikator klienta:");
        String uuid = br.readLine();
        try {
            if (uuid == null) {
                System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Dane nie mogą być puste!|@"));
                return;
            }
        } catch (NullPointerException e) {
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Dane nie mogą być puste!|@"));
            return;
        }
        BigInteger bigInteger = new BigInteger(uuid);
        influxClientDAO.removeByUUID(bigInteger.longValue());
    }

    public void findAllParcelsByCity() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(white) Menu wyszukania wszystkich paczek kuriera|@"));
        System.out.print("\n");
        System.out.println("Miasto:");
        String city = br.readLine();

        try {
            if (city == null) {
                System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Dane nie mogą być puste!|@"));
                return;
            }
        } catch (NullPointerException e) {
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Dane nie mogą być puste!|@"));
            return;
        }

        List<ParcelsModel> parcelsModels = influxParcelsDAO.findByCity(city);
        for (ParcelsModel parcelsModel : parcelsModels) {
            List<EmployeeModel> employeeModel = influxEmployeeDAO.findByUUID(parcelsModel.getEmployee_id().toEpochMilli());
            System.out.println("Identyfikator rekordu:" + String.format("%019d", BigInteger.valueOf(parcelsModel.getParcel_id().toEpochMilli())));
            System.out.println("Identyfikator rejonu paczki:" + parcelsModel.getIdentyfikator_rejonu_paczki());
            System.out.println("Imie kuriera:" + employeeModel.get(0).getImie());
            System.out.println("Nazwisko kuriera:" + employeeModel.get(0).getNazwisko());
            System.out.println("Telefon kuriera:" + employeeModel.get(0).getTelefon());
            System.out.println("Miasto:" + parcelsModel.getMiasto());
            System.out.println("Ulica:" + parcelsModel.getUlica());
            System.out.println("Numer domu/mieszkania:" + parcelsModel.getNumer_domu());
            System.out.println("Pobranie:" + parcelsModel.getPobranie());
            System.out.println("Ilość paczek:" + parcelsModel.getIlosc_paczek());
        }
        if (parcelsModels.isEmpty()) {
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Brak paczek dla podanego klienta!|@"));
        }
    }

    void setSurname() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(white) Podaj nowe nazwisko|@"));
        System.out.print("\n");
        System.out.println("Nazwisko:");
        String firstName = br.readLine();
        System.out.println("Identyfikator pracownika:");
        String uuid = br.readLine();
        try {
            if (firstName == null || uuid == null) {
                System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Dane nie mogą być puste!|@"));
                return;
            }
        } catch (NullPointerException e) {
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Dane nie mogą być puste!|@"));
            return;
        }
        BigInteger bigInteger = new BigInteger(uuid);
        influxEmployeeDAO.setSurnameByUUID(firstName, bigInteger.longValue());
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
