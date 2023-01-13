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
            sender.sendMessage("§cApenas jogadores podem executar este comando.");
            return true;
        }

        Player player = (Player) sender;
        User user = EconomyPlugin.getUserManager().getUser(player);

        if (user == null)
            user = new User(player.getUniqueId(), player.getName());

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

            if (args[2].equalsIgnoreCase("nan")){
                player.sendMessage("§cERRO! Digite uma quantia válida.");
                return true;
            }

            if (args[1].equalsIgnoreCase(player.getName())){
                player.sendMessage("§cERRO! Você não pode enviar dinheiro para si mesmo.");
                return true;
            }

            User targetUser = EconomyPlugin.getUserManager().getUser(args[1]);
            if (targetUser == null || targetUser.getPlayer() == null){
                player.sendMessage("§cEste usuário não está online.");
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
            EconomyPlugin.getEconomy().depositPlayer(targetUser.getName(), amount);
            EconomyPlugin.getEconomy().withdrawPlayer(player.getName(), amount);
            player.sendMessage("§aVocê enviou §f" + formatted + " §acoin(s) para o jogador §f" + targetUser.getName() + " §acom sucesso!");
            targetUser.getPlayer().sendMessage("§aVocê recebeu §f" + formatted + " §acoin(s) do jogador §f" + player.getName() + " §acom sucesso!");
            Bukkit.getPluginManager().callEvent(new MoneySendEvent(player, targetUser.getPlayer(), amount));
            return true;
        }

        if (args[0].equalsIgnoreCase("top") && args.length == 1){

            int pos = 1;
            net.luckperms.api.model.user.User userLP = EconomyPlugin.getInstance().getLuckPermsAPI().getPlayerAdapter(Player.class).getUser(player);
            String prefix = userLP.getCachedData().getMetaData().getPrefix().replace("&", "§");

            player.sendMessage("");
            player.sendMessage("   §eJogadores mais ricos do servidor");
            player.sendMessage("         §7Atualiza a cada 5 minutos.");
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
                    player.sendMessage("§cDigite '/money add <jogador> <quantia>'");
                    return true;
                }

                Player target = Bukkit.getPlayerExact(args[1]);
                double amount;

                if (target == null){
                    player.sendMessage("§cEste usuário não possui uma conta no servidor.");
                    return true;
                }

                try{
                    amount = Double.parseDouble(args[2]);
                }catch (Exception e){
                    player.sendMessage("§cERRO! Digite uma quantia válida.");
                    return true;
                }

                EconomyPlugin.getEconomy().depositPlayer(args[1], amount);
                player.sendMessage("§aVocê adicionou §f" + new Formatter().formatNumber(amount) + " §acoin(s) ao jogador §f" + args[1] + " §acom sucesso.");
                return true;
            }

            if (args[0].equalsIgnoreCase("remove")){

                if (args.length != 3) {
                    player.sendMessage("§cDigite '/money remove <jogador> <quantia>'");
                    return true;
                }

                Player target = Bukkit.getPlayerExact(args[1]);
                double amount;

                if (target == null){
                    player.sendMessage("§cEste usuário não possui uma conta no servidor.");
                    return true;
                }

                try{
                    amount = Double.parseDouble(args[2]);
                }catch (Exception e){
                    player.sendMessage("§cERRO! Digite uma quantia válida.");
                    return true;
                }

                EconomyPlugin.getEconomy().withdrawPlayer(args[1], amount);
                player.sendMessage("§aVocê adicionou §f" + new Formatter().formatNumber(amount) + " §acoin(s) ao jogador §f" + args[1] + " §acom sucesso.");
                return true;
            }

            if (args[0].equalsIgnoreCase("set")){

                if (args.length != 3) {
                    player.sendMessage("§cDigite '/money set <jogador> <quantia>'");
                    return true;
                }

                Player target = Bukkit.getPlayerExact(args[1]);
                double amount;

                if (target == null){
                    player.sendMessage("§cEste usuário não possui uma conta no servidor.");
                    return true;
                }

                try{
                    amount = Double.parseDouble(args[2]);
                }catch (Exception e){
                    player.sendMessage("§cERRO! Digite uma quantia válida.");
                    return true;
                }

                EconomyPlugin.getEconomy().withdrawPlayer(args[1], EconomyPlugin.getEconomy().getBalance(args[1]));
                EconomyPlugin.getEconomy().depositPlayer(args[1], amount);
                player.sendMessage("§aVocê definiu §f" + new Formatter().formatNumber(amount) + " §acoin(s) ao jogador §f" + args[1] + " §acom sucesso.");
                return true;
            }

        }

        if (args.length == 1){

            User targetUser = EconomyPlugin.getUserManager().getUser(args[0]);
            if (targetUser == null){
                player.sendMessage("§cEste usuário não possui uma conta em nosso servidor.");
                return true;
            }

            player.sendMessage("§aO usuário §f" + targetUser.getName() + " §apossui §f" + targetUser.getFormattedAmount() + " §acoin(s).");
            return true;
        }

        player.sendMessage("§e§lAJUDA");
        player.sendMessage("");
        player.sendMessage("§e/money §7- veja o saldo da sua conta");
        player.sendMessage("§e/money <jogador> §7- veja o saldo da conta de outro jogador");
        player.sendMessage("§e/money enviar <jogador> <quantia> §7- envie dinheiro para a conta de um jogador");
        player.sendMessage("");
        return false;
    }
}
