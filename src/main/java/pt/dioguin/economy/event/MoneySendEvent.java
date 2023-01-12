package pt.dioguin.economy.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MoneySendEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player sender;
    private final Player receiver;
    private final double amount;

    public MoneySendEvent(Player sender, Player receiver, double amount){
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public Player getSender() {
        return sender;
    }

    public Player getReceiver() {
        return receiver;
    }

    public double getAmount() {
        return amount;
    }
}
