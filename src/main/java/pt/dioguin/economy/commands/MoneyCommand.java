package pt.dioguin.economy.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pt.dioguin.economy.EconomyPlugin;
import pt.dioguin.economy.event.MoneySendEvent;
import pt.dioguin.economy.user.User;
import pt.dioguin.economy.utils.Formatter;

import java.util.Map;

public class MoneyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)){
            sender.sendMessage("§cOnly players can execute this command.");
            return true;
        }

        Player player = (Player) sender;
        User user = EconomyPlugin.getUserManager().getUser(player);

        if (user == null)
            user = new User(player.getUniqueId(), player.getName());

        if (args.length == 0){
            player.sendMessage("§aYou have §f" + user.getFormattedAmount() + " §acoin(s).");
            return true;
        }

        if (args[0].equalsIgnoreCase("pay") || args[0].equalsIgnoreCase("enviar")){
            //money pay <nickname> <quantity>
            if (args.length != 3){
                player.sendMessage("§cERROR! Digite /money enviar <player> <amount>");
                return true;
            }

            if (args[2].equalsIgnoreCase("nan")){
                player.sendMessage("§cERROR! Enter a valid amount.");
                return true;
            }

            if (args[1].equalsIgnoreCase(player.getName())){
                player.sendMessage("§cERROR! You cannot send money to yourself.");
                return true;
            }

            User targetUser = EconomyPlugin.getUserManager().getUser(args[1]);
            if (targetUser == null || targetUser.getPlayer() == null){
                player.sendMessage("§cThis user is not yet online.");
                return true;
            }

            double amount;

            try{
                amount = Double.parseDouble(args[2]);
            }catch (Exception e){
                player.sendMessage("§cERROR! Enter a valid amount.");
                return true;
            }

            if (!EconomyPlugin.getEconomy().has(player.getName(), amount)){
                player.sendMessage("§cERROR! You don't have enough money to make this transaction.");
                return true;
            }

            String formatted = new Formatter().formatNumber(amount);
            EconomyPlugin.getEconomy().depositPlayer(targetUser.getName(), amount);
            EconomyPlugin.getEconomy().withdrawPlayer(player.getName(), amount);
            player.sendMessage("§aYou have sent §f" + formatted + " §acoin(s) to the player §f" + targetUser.getName() + " §asuccessfully!");
            targetUser.getPlayer().sendMessage("§aYou have successfully received §f" + formatted + " §acoin(s) from the player §f" + player.getName() + "§a.");
            Bukkit.getPluginManager().callEvent(new MoneySendEvent(player, targetUser.getPlayer(), amount));
            return true;
        }

        if (args[0].equalsIgnoreCase("top") && args.length == 1){

            int pos = 1;
            net.luckperms.api.model.user.User userLP = EconomyPlugin.getInstance().getLuckPermsAPI().getPlayerAdapter(Player.class).getUser(player);
            String prefix = userLP.getCachedData().getMetaData().getPrefix().replace("&", "§");

            player.sendMessage("");
            player.sendMessage("   §eRichest players on the server");
            player.sendMessage("         §7Updates every 5 minutes.");
            player.sendMessage("");

            for (Map.Entry<String, Double> item : EconomyPlugin.getUserManager().getTop().entrySet()) {
                player.sendMessage(" §f" + pos + "º " + prefix + " " + item.getKey() + ": §a" + new Formatter().formatNumber(item.getValue()) + " coin(s).");
                pos++;
            }

            player.sendMessage("");
            return true;
        }

        if (player.hasPermission("economy.admin")){

            if (args[0].equalsIgnoreCase("add")){

                if (args.length != 3) {
                    player.sendMessage("§cTry typing '/money add <player> <amount>'");
                    return true;
                }

                Player target = Bukkit.getPlayerExact(args[1]);
                double amount;

                if (target == null){
                    player.sendMessage("§cThis user does not have an account on the server.");
                    return true;
                }

                try{
                    amount = Double.parseDouble(args[2]);
                }catch (Exception e){
                    player.sendMessage("§cERROR! Enter a valid amount.");
                    return true;
                }

                EconomyPlugin.getEconomy().depositPlayer(args[1], amount);
                player.sendMessage("§aYou have successfully added §f" + new Formatter().formatNumber(amount) + " §acoin(s) for the player §f" + args[1] + "§a.");
                return true;
            }

            if (args[0].equalsIgnoreCase("remove")){

                if (args.length != 3) {
                    player.sendMessage("§cTry typing '/money remove <player> <amount>'");
                    return true;
                }

                Player target = Bukkit.getPlayerExact(args[1]);
                double amount;

                if (target == null){
                    player.sendMessage("§cThis user does not have an account on the server.");
                    return true;
                }

                try{
                    amount = Double.parseDouble(args[2]);
                }catch (Exception e){
                    player.sendMessage("§cERROR! Enter a valid amount.");
                    return true;
                }

                EconomyPlugin.getEconomy().withdrawPlayer(args[1], amount);
                player.sendMessage("§aYou have successfully removed §f" + new Formatter().formatNumber(amount) + " §acoin(s) from the player §f" + args[1] + "§a.");
                return true;
            }

            if (args[0].equalsIgnoreCase("set")){

                if (args.length != 3) {
                    player.sendMessage("§cTry typing '/money set <player> <amonut>'");
                    return true;
                }

                Player target = Bukkit.getPlayerExact(args[1]);
                double amount;

                if (target == null){
                    player.sendMessage("§cThis user does not have an account on the server.");
                    return true;
                }

                try{
                    amount = Double.parseDouble(args[2]);
                }catch (Exception e){
                    player.sendMessage("§cERROR! Enter a valid amount.");
                    return true;
                }

                EconomyPlugin.getEconomy().withdrawPlayer(args[1], EconomyPlugin.getEconomy().getBalance(args[1]));
                EconomyPlugin.getEconomy().depositPlayer(args[1], amount);
                player.sendMessage("§aYou have successfully set §f" + new Formatter().formatNumber(amount) + " §acoin(s) for the player §f" + args[1] + "§a.");
                return true;
            }

        }

        if (args.length == 1){

            User targetUser = EconomyPlugin.getUserManager().getUser(args[0]);
            if (targetUser == null){
                player.sendMessage("§cThis user does not have an account on the server.");
                return true;
            }

            player.sendMessage("§aPlayer §f" + targetUser.getName() + " §ahas §f" + targetUser.getFormattedAmount() + " §acoin(s).");
            return true;
        }

        player.sendMessage("§e§lHELP");
        player.sendMessage("");
        player.sendMessage("§e/money §7- Check your account balance");
        player.sendMessage("§e/money <player> §7- Check the balance of another player's account");
        player.sendMessage("§e/money pay <player> <amount> §7- Send money to a player's account");
        player.sendMessage("");
        return false;
    }
}
