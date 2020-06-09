package com.simplemethod.dhx;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;

@Component
public class CassandraConfig {

    private CqlSession session;

    public CqlSession getSession() {
        return this.session;
    }

    public void close() {
        session.close();
    }


    void init() {
        session = CqlSession.builder()
                .withCloudSecureConnectBundle(Paths.get("E:\\Github\\dhx\\src\\main\\resources\\secure-connect-technologie.zip"))
                .withAuthCredentials("technologie", "technologiePSK")
                .withKeyspace("technologie")
                .build();
        // Select the release_version from the system.local table:
        ResultSet rs = session.execute("select release_version from system.local");

        Row row = rs.one();
        //Print the results of the CQL query to the console:
        if (row != null) {
            System.out.println(row.getString("release_version"));
        } else {
            System.out.println("An error occurred.");
        }

       // UuidCodec uuidCodec = new UuidCodec();
       // UUID xd = uuidCodec.parse("17b8e620-a8b2-11ea-a04d-7336f4620af4");
        //removeByUUIDAndPhoneNumber(xd,413742137);
        //setNameByUUIDAndPhoneNumber("Andrzej",xd,413424444);
        //ClientModel kd = findByUUID(xd);
        //System.out.println(kd.toString());
    }


}
