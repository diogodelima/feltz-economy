package pt.dioguin.economy.user;

import pt.dioguin.economy.EconomyPlugin;

import java.util.UUID;

public class User {

    private final UUID uniqueId;
    private double amount;

    public User(UUID uniqueId){
        this.uniqueId = uniqueId;
        this.amount = 0;
        EconomyPlugin.getUserManager().getUsers().add(this);
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public double getAmount() {
        return amount;
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
}
