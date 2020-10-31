package de.marvinleiers.authplugin;

import de.marvinleiers.authplugin.commands.LoginCommand;
import de.marvinleiers.authplugin.commands.RegisterCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public final class AuthPlugin extends JavaPlugin implements Listener
{
    public static ArrayList<Player> register = new ArrayList<>();
    public static ArrayList<Player> login = new ArrayList<>();

    @Override
    public void onEnable()
    {
        this.getServer().getPluginManager().registerEvents(this, this);

        this.getCommand("register").setExecutor(new RegisterCommand());
        this.getCommand("login").setExecutor(new LoginCommand());
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event)
    {
        Player player = event.getPlayer();

        if (register.contains(player) || login.contains(player))
            event.setCancelled(true);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 5, false, false, false));

        if (!isRegistered(player))
        {
            register.add(player);

            new BukkitRunnable()
            {

                int i = 0;

                @Override
                public void run()
                {
                    if (!register.contains(player))
                    {
                        this.cancel();
                        return;
                    }

                    if (i >= 3)
                    {
                        player.kickPlayer("§cKicked for security reasons!");
                        this.cancel();
                        return;
                    }

                    i++;

                    player.sendMessage("§7Please register an account with §c§l/register <password> <password>!");
                }
            }.runTaskTimer(this, 0, 200);
        }
        else
        {
            login.add(player);

            new BukkitRunnable()
            {

                int i = 0;

                @Override
                public void run()
                {
                    if (!login.contains(player))
                    {
                        this.cancel();
                        return;
                    }

                    if (i >= 3)
                    {
                        player.kickPlayer("§cKicked for security reasons!");
                        this.cancel();
                        return;
                    }

                    i++;

                    player.sendMessage("§7Please login with /login <password>!");
                }
            }.runTaskTimer(this, 0, 200);
        }
    }

    @EventHandler
    public void onPrevent(PlayerCommandPreprocessEvent event)
    {
        if (!register.contains(event.getPlayer()) && !login.contains(event.getPlayer()))
            return;

        if (event.getMessage().toLowerCase().startsWith("/login") || event.getMessage().toLowerCase().startsWith("/register"))
            return;

        event.getPlayer().sendMessage("§cYou need to login first!");
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        if (!register.contains(event.getPlayer()) && !login.contains(event.getPlayer()))
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockPlaceEvent event)
    {
        if (!register.contains(event.getPlayer()) && !login.contains(event.getPlayer()))
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(PlayerInteractEvent event)
    {
        if (!register.contains(event.getPlayer()) && !login.contains(event.getPlayer()))
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event)
    {
        Player player = event.getPlayer();

        if (!register.contains(player) && !login.contains(player))
            return;

        if (event.getFrom().distance(event.getTo()) > 0)
            event.setCancelled(true);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event)
    {
        if (register.contains(event.getPlayer()))
            register.remove(event.getPlayer());

        if (login.contains(event.getPlayer()))
            login.remove(event.getPlayer());
    }

    public static AuthPlugin getPlugin()
    {
        return getPlugin(AuthPlugin.class);
    }

    private boolean isRegistered(Player player)
    {
        return getConfig().contains("pws." + player.getUniqueId().toString() + ".registeredOn");
    }
}
