package pt.dioguin.economy.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pt.dioguin.economy.EconomyPlugin;

public class EconomyExpansion extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "feltz";
    }

    @Override
    public @NotNull String getAuthor() {
        return "yDioguin_";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {

        if (params.equalsIgnoreCase("money"))
            return EconomyPlugin.getUserManager().getUser(player.getName()).getFormattedAmount();

        return null;
    }
}
