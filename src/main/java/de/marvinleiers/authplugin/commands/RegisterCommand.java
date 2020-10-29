package de.marvinleiers.authplugin.commands;

import de.marvinleiers.authplugin.AuthPlugin;
import de.marvinleiers.authplugin.enryption.Security;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RegisterCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage("§cOnly players can execute this command!");
            return true;
        }

        Player player = (Player) sender;

        if (!AuthPlugin.register.contains(player))
        {
            player.sendMessage("§cYou can't execute this command. If you think this is an error please contact staff!");
            return true;
        }

        if (args.length != 2)
        {
            player.sendMessage("§c/register <password> <password>");
            return true;
        }

        String pw = args[0];
        String pwMatch = args[1];

        if (pw.length() > 16)
        {
            player.sendMessage("§cYour password is too long. Please only use 16 characters or less!");
            return true;
        }

        if(!pw.equals(pwMatch))
        {
            player.sendMessage("§cYou passwords don't match!");
            return true;
        }

        String pwEnc = Security.encrypt(pw);

        AuthPlugin.getPlugin().getConfig().set("pws." + player.getUniqueId().toString() + ".pw", pwEnc);
        AuthPlugin.getPlugin().getConfig().set("pws." + player.getUniqueId().toString() + ".registeredOn", System.currentTimeMillis());
        AuthPlugin.getPlugin().saveConfig();

        AuthPlugin.register.remove(player);
        AuthPlugin.login.add(player);

        player.sendMessage("§aYou password has been saved! You may login now with /login <password>");
        return true;
    }
}
