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

public class MoneyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)){
            sender.sendMessage("§cApenas jogadores podem executar este comando.");
            return true;
        }

        Player player = (Player) sender;
        User user = EconomyPlugin.getUserManager().getUser(player);

        if (user == null)
            user = new User(player.getUniqueId());

        if (args.length == 0){
            player.sendMessage("§aVocê possui §f" + user.getFormattedAmount() + " §acoin(s).");
            return true;
        }

        if (args[0].equalsIgnoreCase("pay") || args[0].equalsIgnoreCase("enviar")){
            //money pay <nickname> <quantity>
            if (args.length != 3){
                player.sendMessage("§cERRO! Digite /money enviar <jogador> <quantia>");
                return true;
            }

            Player target = Bukkit.getPlayerExact(args[1]);
            User targetUser = EconomyPlugin.getUserManager().getUser(target);
            if (targetUser == null){
                player.sendMessage("§cEste usuário não possui uma conta em nosso servidor.");
                return true;
            }

            double amount;

            try{
                amount = Double.parseDouble(args[2]);
            }catch (Exception e){
                player.sendMessage("§cERRO! Digite uma quantia válida.");
                return true;
            }

            if (!EconomyPlugin.getEconomy().has(player.getName(), amount)){
                player.sendMessage("§cERRO! Você não possui dinheiro o suficiente para efetuar esta transação.");
                return true;
            }

            String formatted = new Formatter().formatNumber(amount);
            EconomyPlugin.getEconomy().depositPlayer(target.getName(), amount);
            EconomyPlugin.getEconomy().withdrawPlayer(player.getName(), amount);
            player.sendMessage("§aVocê enviou §f" + formatted + " §acoin(s) para o jogador §f" + target.getName() + " §acom sucesso!");
            target.sendMessage("§aVocê recebeu §f" + formatted + " §acoin(s) do jogador §f" + player.getName() + " §acom sucesso!");
            Bukkit.getPluginManager().callEvent(new MoneySendEvent(player, target, amount));
            return true;
        }

        if (args[0].equalsIgnoreCase("top") && args.length == 1){

            return true;
        }

        if (args.length == 1){

            Player target = Bukkit.getPlayerExact(args[0]);
            User targetUser = EconomyPlugin.getUserManager().getUser(target);
            if (targetUser == null){
                player.sendMessage("§cEste usuário não possui uma conta em nosso servidor.");
                return true;
            }

            player.sendMessage("§aO usuário §f" + targetUser.getPlayer().getName() + " §apossui §f" + targetUser.getFormattedAmount() + " §acoin(s).");
            return true;
        }

        return false;
    }
}
