package pt.dioguin.economy.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pt.dioguin.economy.EconomyPlugin;
import pt.dioguin.economy.user.User;

public class PlayerListener implements Listener {

    @EventHandler
    void onPlayerJoin(PlayerJoinEvent event){

        Player player = event.getPlayer();
        User user = EconomyPlugin.getUserManager().getUser(player);

        if (user == null)
            new User(player.getUniqueId(), player.getName());

    }

}
