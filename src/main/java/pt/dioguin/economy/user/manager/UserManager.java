package pt.dioguin.economy.user.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pt.dioguin.economy.EconomyPlugin;
import pt.dioguin.economy.user.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserManager {

    private final List<User> users;

    public UserManager(){
        this.users = new ArrayList<>();
    }

    public User getUser(Player player){

        for (User user : this.users)
            if (user.getUniqueId().equals(player.getUniqueId()))
                return user;

        return null;
    }

    public void load() throws SQLException {

        PreparedStatement statement = EconomyPlugin.getConnector().getConnection().prepareStatement("SELECT * FROM feltzeconomy");
        ResultSet result = statement.executeQuery();

        while (result.next())
            new User(UUID.fromString(result.getString("UNIQUEID")), result.getDouble("AMOUNT"));

    }

    public void saveAll() throws SQLException {

        for (User user : this.users)
            save(user, false);

        Bukkit.getConsoleSender().sendMessage("§a[feltz-economy] Foram salvos §f" + this.users.size() + " §acom sucesso.");
    }

    public void save(User user, boolean debug) throws SQLException {

        PreparedStatement statement = EconomyPlugin.getConnector().getConnection().prepareStatement("SELECT * FROM feltzeconomy WHERE UNIQUEID = ?");
        statement.setString(1, user.getUniqueId().toString());
        ResultSet result = statement.executeQuery();

        if (result.next()){
            statement = EconomyPlugin.getConnector().getConnection().prepareStatement("UPDATE feltzeconomy SET AMOUNT = ? WHERE UNIQUEID = ?");
            statement.setDouble(1, user.getAmount());
            statement.setString(2, user.getUniqueId().toString());
        }else{
            statement = EconomyPlugin.getConnector().getConnection().prepareStatement("INSERT INTO feltzeconomy (UNIQUEID,AMOUNT) VALUES(?,?)");
            statement.setString(1, user.getUniqueId().toString());
            statement.setDouble(2, user.getAmount());
        }

        statement.execute();
        statement.close();

        if (debug)
            Bukkit.getConsoleSender().sendMessage("§a[feltz-economy] O usuário §f" + user.getPlayer().getName() + " §acom sucesso.");
    }

    public List<User> getUsers() {
        return users;
    }
}
