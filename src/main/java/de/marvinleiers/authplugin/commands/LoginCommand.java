package de.marvinleiers.authplugin.commands;

import de.marvinleiers.authplugin.AuthPlugin;
import de.marvinleiers.authplugin.enryption.Security;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class LoginCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage("§cThis comand is only for players!");
            return true;
        }

        Player player = (Player) sender;

        if (!AuthPlugin.login.contains(player))
        {
            player.sendMessage("§cSomething went wrong here. If have not registered yet, please create an account with /register.");
            return true;
        }

        if (args.length < 1)
        {
            player.sendMessage("§c/login <password>");
            return true;
        }

        String pw = Security.encrypt(args[0]);

        if (!AuthPlugin.getPlugin().getConfig().isSet("pws." + player.getUniqueId().toString() + ".pw"))
        {
            player.sendMessage("§cSeems like your data has been editied manually... Please contact an admin!!!");
            return true;
        }

        String pwCompare = AuthPlugin.getPlugin().getConfig().getString("pws." + player.getUniqueId().toString() + ".pw");

        if (!pw.equals(pwCompare))
        {
            player.sendMessage("§cYour password was incorrect!");
            return true;
        }

        player.sendMessage("§aYou are now logged in!");
        AuthPlugin.login.remove(player);

        player.removePotionEffect(PotionEffectType.BLINDNESS);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);

        return true;
    }
}
