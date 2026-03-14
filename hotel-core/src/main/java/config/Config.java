package config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Properties;

@Component
public class Config {
    public String getDataDir() {
        return dataDir;
    }

    public String getStateFilePath() {
        return stateFilePath;
    }

    public int getResidenceHistoryMax() {
        return residenceHistoryMax;
    }

    public boolean isRoomStatusChangeEnabled() {
        return isRoomStatusChangeEnabled;
    }

    public String getJdbc_driver() {
        return jdbc_driver;
    }

    public void setJdbc_driver(String jdbc_driver) {
        this.jdbc_driver = jdbc_driver;
    }

    public String getJdbc_password() {
        return jdbc_password;
    }

    public void setJdbc_password(String jdbc_password) {
        this.jdbc_password = jdbc_password;
    }

    public String getJdbc_user() {
        return jdbc_user;
    }

    public void setJdbc_user(String jdbc_user) {
        this.jdbc_user = jdbc_user;
    }

    public String getJdbc_url() {
        return jdbc_url;
    }

    public void setJdbc_url(String jdbc_url) {
        this.jdbc_url = jdbc_url;
    }

    @Value("${app.data.dir:./data}")
    private String dataDir;

    @Value("${state.file.path:./data/state.dat}")
    private String stateFilePath;

    @Value("${room.residence.history.max:3}")
    private int residenceHistoryMax;

    @Value("${room.status.change.enabled}")
    private boolean isRoomStatusChangeEnabled;

    @Value("${jdbc.url:jdbc:postgresql://localhost:5432/hotel}")
    private String jdbc_url;

    @Value("${jdbc.user:postgres}")
    private String jdbc_user;

    @Value("${jdbc.password:postgres}")
    private String jdbc_password;

    @Value("${jdbc.driver:org.postgresql.Driver}")
    private String jdbc_driver;


}
