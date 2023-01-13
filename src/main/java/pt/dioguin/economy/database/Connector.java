package pt.dioguin.economy.database;

import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Connector {

    private Connection connection;
    private final String hostname;
    private final String database;
    private final String user;
    private final String password;
    private final int port;

    public Connector(String hostname, String database, String user, String password, int port){
        this.hostname = hostname;
        this.database = database;
        this.user = user;
        this.password = password;
        this.port = port;
    }

    public void connect(){

        String url = "jdbc:mysql://" + this.hostname + ":" + this.port + "/" + this.database;

        try {
            this.connection = DriverManager.getConnection(url, this.user, this.password);
            Bukkit.getConsoleSender().sendMessage("§aConexão realizada com sucesso.");
        }catch (SQLException e){
            Bukkit.getConsoleSender().sendMessage("§cOcorreu um erro ao abrir a conexão.");
            e.printStackTrace();
        }

    }

    public void close(){

        if (this.connection != null){
            try {
                this.connection.close();
                this.connection  = null;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void createTable(String name){

        try {
            PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + name + " (UNIQUEID VARCHAR(255), NAME VARCHAR(255), AMOUNT DOUBLE)");
            statement.executeUpdate();
            statement.close();
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public Connection getConnection() {
        return connection;
    }
}
