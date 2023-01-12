package pt.dioguin.economy.hook;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import pt.dioguin.economy.EconomyPlugin;

public class VaultHook {

    private Economy provider;

    public void hook(){
        provider = EconomyPlugin.getEconomy();
        Bukkit.getServicesManager().register(Economy.class, this.provider, EconomyPlugin.getInstance(), ServicePriority.Normal);
        Bukkit.getConsoleSender().sendMessage("§a[feltz-economy] VaultAPI hooked successfully.");
    }

    public void unhook(){
        Bukkit.getServicesManager().unregister(EconomyPlugin.class, this.provider);
    }

}
