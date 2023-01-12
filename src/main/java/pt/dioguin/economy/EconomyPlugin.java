package pt.dioguin.economy;

import org.bukkit.plugin.java.JavaPlugin;
import pt.dioguin.economy.hook.VaultHook;
import pt.dioguin.economy.impl.EconomyImpl;
import pt.dioguin.economy.user.manager.UserManager;

public final class EconomyPlugin extends JavaPlugin {

    private static UserManager userManager;
    private static EconomyImpl economy;
    private VaultHook vaultHook;
    @Override
    public void onEnable() {
        userManager = new UserManager();
        economy = new EconomyImpl();
        vaultHook = new VaultHook();
        vaultHook.hook();
    }

    @Override
    public void onDisable() {
        vaultHook.unhook();
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
}
