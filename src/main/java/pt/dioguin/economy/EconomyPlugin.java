package pt.dioguin.economy;

import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import pt.dioguin.economy.commands.MoneyCommand;
import pt.dioguin.economy.database.Connector;
import pt.dioguin.economy.hook.VaultHook;
import pt.dioguin.economy.impl.EconomyImpl;
import pt.dioguin.economy.listener.PlayerListener;
import pt.dioguin.economy.placeholder.EconomyExpansion;
import pt.dioguin.economy.user.manager.UserManager;

import java.sql.SQLException;

public final class EconomyPlugin extends JavaPlugin {

    private static UserManager userManager;
    private static EconomyImpl economy;
    private static Connector connector;
    private VaultHook vaultHook;
    private LuckPerms luckPermsAPI;

    @Override
    public void onEnable() {
        connector = new Connector("localhost", "feltz", "root", "benficamerda123", 3306);
        connector.connect();
        connector.createTable("feltzeconomy");
        userManager = new UserManager();
        economy = new EconomyImpl();
        vaultHook = new VaultHook();
        vaultHook.hook();

        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null)
            luckPermsAPI = provider.getProvider();

        try {
            userManager.load();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
            new EconomyExpansion().register();

        getCommand("money").setExecutor(new MoneyCommand());
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
    }

    @Override
    public void onDisable() {
        vaultHook.unhook();
        try {
            userManager.saveAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static UserManager getUserManager() {
        return userManager;
    }

    public static EconomyPlugin getInstance(){
        return getPlugin(EconomyPlugin.class);
    }

    public static EconomyImpl getEconomy() {
        return economy;
    }

    public static Connector getConnector() {
        return connector;
    }

    public LuckPerms getLuckPermsAPI() {
        return luckPermsAPI;
    }
}
