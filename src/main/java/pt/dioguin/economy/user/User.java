package pt.dioguin.economy.user;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pt.dioguin.economy.EconomyPlugin;
import pt.dioguin.economy.utils.Formatter;

import java.util.UUID;

public class User {

    private final UUID uniqueId;
    private final String name;
    private double amount;

    public User(UUID uniqueId, String name){
        this.uniqueId = uniqueId;
        this.name = name;
        this.amount = 0;
        EconomyPlugin.getUserManager().getUsers().add(this);
    }

    public User(UUID uniqueId, String name, double amount){
        this.uniqueId = uniqueId;
        this.name = name;
        this.amount = amount;
        EconomyPlugin.getUserManager().getUsers().add(this);
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public double getAmount() {
        return amount;
    }

    public String getFormattedAmount(){
        return new Formatter().formatNumber(this.amount);
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void removeAmount(double amount){
        this.amount -= amount;
    }

    public void addAmount(double amount){
        this.amount += amount;
    }

    public Player getPlayer(){
        return Bukkit.getPlayer(this.uniqueId);
    }

    public String getName() {
        return name;
    }
}
