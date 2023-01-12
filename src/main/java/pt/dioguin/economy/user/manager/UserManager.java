package pt.dioguin.economy.user.manager;

import org.bukkit.entity.Player;
import pt.dioguin.economy.user.User;

import java.util.ArrayList;
import java.util.List;

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

    public List<User> getUsers() {
        return users;
    }
}
