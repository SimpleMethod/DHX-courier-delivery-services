package com.simplemethod.dhx;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.internal.core.type.codec.UuidCodec;
import com.simplemethod.dhx.CassandraDataModel.EmployeeModel;
import com.simplemethod.dhx.cassandraDAO.ClientDAO;
import com.simplemethod.dhx.cassandraDAO.EmployeeDAO;
import com.simplemethod.dhx.cassandraDAO.ParcelsDAO;
import com.simplemethod.dhx.CassandraDataModel.ClientModel;
import com.simplemethod.dhx.CassandraDataModel.ParcelsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import picocli.CommandLine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Controller
public class MainViewCassandra {

    private CqlSession session;

    @Autowired
    CassandraConfig cassandraConfig;

    @Autowired
    ClientDAO clientDAO;

    @Autowired
    EmployeeDAO employeeDAO;

    @Autowired
    ParcelsDAO parcelsDAO;

    public void menu() throws IOException {
        cassandraConfig.init();
        session = cassandraConfig.getSession();
        clientDAO.setSession(session);
        employeeDAO.setSession(session);
        parcelsDAO.setSession(session);

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
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(blue)  [7]|@ Wyszukwanie złozone paczki według imienia i nazwiska klienta"));
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(blue)  [8]|@ Zmiana miasta docelowego paczki"));
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(blue)  [9]|@ Wyszukiwanie wszystkich kierowców"));
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(blue)  [10]|@ Dodanie nowego kierowcy"));
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red)  [11]|@ Zamknięcie programu"));
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
                        findAllParcelsByNameAndSurname();
                        break;
                    case 8:
                        setCityParcels();
                        break;
                    case 11:
                        Runtime.getRuntime().exit(1);
                        break;
                    case 9:
                        findAllEmployee();
                        break;
                    case 10:
                        addNewEmployee();
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public void findAllEmployee()
    {
        ArrayList<EmployeeModel> clientModels = employeeDAO.findAll();
        clientModels.forEach(cl -> System.out.println(cl.toString() + "\r\n"));
    }

    public void findAllClients() {
        ArrayList<ClientModel> clientModels = clientDAO.findAll();
        clientModels.forEach(cl -> System.out.println(cl.toString() + "\r\n"));
    }

    public void findEmployeesByPhoneNumber()  throws IOException  {
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

        if (isNumeric(phoneNumber) ) {
            ArrayList<EmployeeModel> employeeModels = employeeDAO.findAllByPhoneNumber(Integer.parseInt(phoneNumber));
            employeeModels.forEach(cl -> System.out.println(cl.toString() + "\r\n"));
            if(employeeModels.isEmpty())
            {
                System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Brak kierowcy o podanym numerze!|@"));
            }
        } else {
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Numer telefonu musi być liczbą!|@"));
        }

    }

    public void findAllParcels() {
        ArrayList<ParcelsModel> parcelsModels = parcelsDAO.findAll();
        for (ParcelsModel parcelsModel : parcelsModels) {
            ClientModel clientModel = clientDAO.findByUUID(parcelsModel.getClient_id());
            EmployeeModel employeeModel = employeeDAO.findByUUID(parcelsModel.getEmployee_id());
            System.out.println("Identyfikator rekordu:" + parcelsModel.getParcel_id());
            System.out.println("Identyfikator rejonu paczki:" + parcelsModel.getIdentyfikator_rejonu_paczki());
            System.out.println("Imie klienta:" + clientModel.getImie());
            System.out.println("Nazwisko klienta:" + clientModel.getNazwisko());
            System.out.println("Telefon klienta:" + clientModel.getTelefon());
            System.out.println("Imie kuriera:" + employeeModel.getImie());
            System.out.println("Nazwisko kuriera:" + employeeModel.getNazwisko());
            System.out.println("Telefon kuriera:" + employeeModel.getTelefon());
            System.out.println("Miasto:" + parcelsModel.getMiasto());
            System.out.println("Ulica:" + parcelsModel.getUlica());
            System.out.println("Numer domu/mieszkania:" + parcelsModel.getNumer_domu());
            System.out.println("Pobranie:" + parcelsModel.getPobranie());
            System.out.println("Ilość paczek:" + parcelsModel.getIlosc_paczek());
        }
    }

    public void  addNewEmployee()throws IOException
    {
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
            if (firstName == null || secondName == null ||  phoneNumber.isEmpty()) {
                System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Dane nie mogą być puste!|@"));
                return;
            }
        } catch (NullPointerException e) {
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Dane nie mogą być puste!|@"));
            return;
        }
        if (isNumeric(phoneNumber) ) {
            employeeDAO.saveEmployee(firstName, secondName, Integer.parseInt(phoneNumber));
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
            clientDAO.saveCustomer(firstName, secondName, city, street, Integer.parseInt(houseNumber), Integer.parseInt(phoneNumber));
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
        System.out.println("Telefon:");
        String phoneNumber = br.readLine();
        System.out.println("Identyfikator klienta:");
        String uuid = br.readLine();
        try {
            if (firstName == null || uuid == null || phoneNumber.isEmpty()) {
                System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Dane nie mogą być puste!|@"));
                return;
            }
        } catch (NullPointerException e) {
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Dane nie mogą być puste!|@"));
            return;
        }
        if (isNumeric(phoneNumber)) {
            UuidCodec uuidCodec = new UuidCodec();
            UUID client = uuidCodec.parse(uuid);
            clientDAO.setNameByUUIDAndPhoneNumber(firstName, client, Integer.parseInt(phoneNumber));
        } else {
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Numer telefonu musi być liczbą!|@"));
        }
    }

    void removeClient() throws IOException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Usuwanie klienta|@"));
        System.out.println("Telefon:");
        String phoneNumber = br.readLine();
        System.out.println("Identyfikator klienta:");
        String uuid = br.readLine();
        try {
            if (uuid == null || phoneNumber.isEmpty()) {
                System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Dane nie mogą być puste!|@"));
                return;
            }
        } catch (NullPointerException e) {
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Dane nie mogą być puste!|@"));
            return;
        }

        if (isNumeric(phoneNumber)) {
            UuidCodec uuidCodec = new UuidCodec();
            UUID client = uuidCodec.parse(uuid);
            clientDAO.removeByUUIDAndPhoneNumber(client, Integer.parseInt(phoneNumber));
        } else {
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Numer telefonu musi być liczbą!|@"));
        }
    }

    public void findAllParcelsByNameAndSurname() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(white) Menu wyszukania wszystkich paczek klienta|@"));
        System.out.print("\n");
        System.out.println("Imie:");
        String firstName = br.readLine();
        System.out.println("Nazwisko:");
        String secondName = br.readLine();

        ClientModel clientModel = clientDAO.findByNameAndSurname(firstName, secondName);
        if (clientModel != null) {
            UUID client_ID = clientModel.getClient_id();
            ArrayList<ParcelsModel> parcelsModels = parcelsDAO.findByClient(client_ID);
            for (ParcelsModel parcelsModel : parcelsModels) {
                System.out.println(parcelsModel.toString() + "\r\n");
            }
            if(parcelsModels.isEmpty())
            {
                System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Brak paczek dla podanego klienta!|@"));
            }
        }
    }

    void setCityParcels() throws  IOException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(white) Menu zmiany miasta docelowego paczki|@"));
        System.out.print("\n");
        System.out.println("Miasto:");
        String city = br.readLine();
        System.out.println("Identyfikator rejonu paczki:");
        String trackingID = br.readLine();
        System.out.println("Identyfikator paczki:");
        String uuid = br.readLine();

        try {
            if (city == null || trackingID==null || uuid==null) {
                System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Dane nie mogą być puste!|@"));
                return;
            }
        } catch (NullPointerException e) {
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Dane nie mogą być puste!|@"));
            return;
        }
        if (isNumeric(trackingID)) {
            UuidCodec uuidCodec = new UuidCodec();
            UUID parcels = uuidCodec.parse(uuid);
            parcelsDAO.setCityByUUIDAndTrackingID(city,parcels,Integer.parseInt(trackingID));
        } else {
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|bold,fg(red) Numer telefonu musi być liczbą!|@"));
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
