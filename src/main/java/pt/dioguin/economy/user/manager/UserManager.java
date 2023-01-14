package pt.dioguin.economy.user.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pt.dioguin.economy.EconomyPlugin;
import pt.dioguin.economy.user.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UserManager {

    private final List<User> users;
    private HashMap<String, Double> top;

    public UserManager(){
        this.users = new ArrayList<>();
        this.top = new LinkedHashMap<>();

        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    saveAll();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }.runTaskTimer(EconomyPlugin.getInstance(), 20 * 60 * 30, 20 * 60 * 30);

        new BukkitRunnable() {
            @Override
            public void run() {
               reloadTop();
            }
        }.runTaskTimer(EconomyPlugin.getInstance(), 0, 20 * 60 * 5);

    }

    public void reloadTop(){

        HashMap<String, Double> map = new HashMap<>();
        for (User user : this.users)
            map.put(user.getName(), user.getAmount());

        top.clear();
        map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> top.put(x.getKey(), x.getValue()));
    }

    public User getUser(Player player){

        for (User user : this.users)
            if (user.getUniqueId().equals(player.getUniqueId()))
                return user;

        return null;
    }

    public User getUser(String name){

        for (User user : this.users)
            if (user.getName().equals(name))
                return user;

        return null;
    }

    public void load() throws SQLException {

        PreparedStatement statement = EconomyPlugin.getConnector().getConnection().prepareStatement("SELECT * FROM feltzeconomy");
        ResultSet result = statement.executeQuery();

        while (result.next())
            new User(UUID.fromString(result.getString("UNIQUEID")), result.getString("NAME"), result.getDouble("AMOUNT"));

        Bukkit.getConsoleSender().sendMessage("§aForam carregados §f" + this.users.size() + " §ausuários com sucesso.");
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
            statement = EconomyPlugin.getConnector().getConnection().prepareStatement("INSERT INTO feltzeconomy (UNIQUEID,AMOUNT,NAME) VALUES(?,?,?)");
            statement.setString(1, user.getUniqueId().toString());
            statement.setDouble(2, user.getAmount());
            statement.setString(3, user.getName());
        }

        statement.execute();
        statement.close();

        if (debug)
            Bukkit.getConsoleSender().sendMessage("§a[feltz-economy] O usuário §f" + user.getPlayer().getName() + " §acom sucesso.");
    }

    public List<User> getUsers() {
        return users;
    }

    public HashMap<String, Double> getTop() {
        return top;
    }
}
