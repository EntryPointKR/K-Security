package cloud.swiftnode.kspam;

import cloud.swiftnode.kspam.listener.PlayerListener;
import cloud.swiftnode.kspam.runnable.UpdateRunnable;
import cloud.swiftnode.kspam.runnable.bukkit.TimerBukkitRunnable;
import cloud.swiftnode.kspam.storage.StaticStorage;
import cloud.swiftnode.kspam.util.Lang;
import cloud.swiftnode.kspam.util.Static;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

/**
 * Created by EntryPoint on 2016-12-17.
 */
public class KSpam extends JavaPlugin {
    private static KSpam INST;

    public static KSpam getInst() {
        return INST;
    }

    @Override
    public void onEnable() {
        INST = this;
        saveDefaultConfig();
        Bukkit.getConsoleSender().sendMessage(Lang.INTRO.toString());
        instantiate();
    }

    private void instantiate() {
        // Listener register
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        // Update check
        Static.runTaskAsync(new Runnable() {
            @Override
            public void run() {
                new UpdateRunnable().run();
            }
        });
        // Check all player every hour
        if (getConfig().getBoolean("check-timer", true)) {
            new TimerBukkitRunnable().runTaskTimerAsynchronously(this, 3600 * 20, 3600 * 20);
        }
        // Metrics
        try {
            Metrics metrics = new Metrics(this);
            Metrics.Graph blocked = metrics.createGraph("Blocked count");
            Metrics.Graph player = metrics.createGraph("Player count");
            blocked.addPlotter(new Metrics.Plotter("Blocked count") {
                @Override
                public int getValue() {
                    System.out.println(StaticStorage.getCachedIpSet().size());
                    return StaticStorage.getCachedIpSet().size();
                }
            });
            player.addPlotter(new Metrics.Plotter("Player count") {
                @Override
                public int getValue() {
                    System.out.println(StaticStorage.getPlayerSet().size());
                    return Static.getOnlinePlayers().length;
                }
            });
            metrics.addGraph(blocked);
            metrics.addGraph(player);
            metrics.start();
        } catch (Exception ex) {
            Static.consoleMsg(
                    Lang.PREFIX + Lang.EXCEPTION.toString("Metrics 접속 실패 " + ex.getMessage()));
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.isOp()) {
            StaticStorage.setErrorMessage(!StaticStorage.isErrorMessage());
            sender.sendMessage(Lang.PREFIX + Lang.SWITCH.toString(StaticStorage.isErrorMessage()));
            sender.sendMessage(Lang.PREFIX + StaticStorage.getPlayerSet().toString());
        } else {
            sender.sendMessage(Lang.PREFIX + Lang.NO_PERM.toString());
        }
        return true;
    }
}
