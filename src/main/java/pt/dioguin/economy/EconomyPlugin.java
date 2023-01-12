package pt.dioguin.economy;

import org.bukkit.plugin.java.JavaPlugin;
import pt.dioguin.economy.database.Connector;
import pt.dioguin.economy.hook.VaultHook;
import pt.dioguin.economy.impl.EconomyImpl;
import pt.dioguin.economy.user.manager.UserManager;

import java.sql.SQLException;

public final class EconomyPlugin extends JavaPlugin {

    private static UserManager userManager;
    private static EconomyImpl economy;
    private static Connector connector;
    private VaultHook vaultHook;
    @Override
    public void onEnable() {
        connector = new Connector("localhost", "test", "root", "", 3306);
        connector.connect();
        connector.createTable("feltzeconomy");
        userManager = new UserManager();
        economy = new EconomyImpl();
        vaultHook = new VaultHook();
        vaultHook.hook();
        try {
            userManager.load();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
}
