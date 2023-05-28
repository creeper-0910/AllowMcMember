package io.inagisaki.amm.allowmcmember;
import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Guild;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Member;
import github.scarsz.discordsrv.util.DiscordUtil;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Guild;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Member;
import github.scarsz.discordsrv.util.DiscordUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public final class AllowMcMember extends JavaPlugin {
    private int taskId;
    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("AMMの起動に成功しました!");
        saveDefaultConfig();
        SettingManager.setup(this, "config.yml");
        SettingManager.getConfig().options().copyDefaults(true);
        SettingManager.saveConfig();
        getServer().getPluginManager().registerEvents(new EventListenerClass(),this);
        getCommand("amm").setExecutor(new CommandClass());
        getCommand("allowmcmember").setExecutor(new CommandClass());
        LocalTime targetTime = LocalTime.of(SettingManager.getConfig().getInt("Hours"), SettingManager.getConfig().getInt("Minutes"));

        LocalTime currentTime = LocalTime.now();

        long initialDelaySeconds = currentTime.until(targetTime, java.time.temporal.ChronoUnit.SECONDS);

        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                SettingManager.reloadConfig();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    Bukkit.getLogger().info(player.getName());
                    LocalDate currentDate = LocalDate.now();
                    DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
                    Bukkit.getLogger().info(dayOfWeek.toString());
                    List<String> restrictionDays = SettingManager.getConfig().getStringList("RestrictionDay");
                    if (restrictionDays.contains(dayOfWeek.toString())) {
                        String userId = DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(player.getUniqueId());
                        List<Guild> guilds = DiscordUtil.getJda().getGuilds();
                        String guildId = guilds.get(0).getId().toString();
                        Member member = DiscordUtil.getJda().getGuildById(guildId).getMemberById(userId);
                        if (member != null) {
                            Bukkit.getLogger().info(DiscordUtil.getTopRole(member).getId());
                            List<String> AllowRole = SettingManager.getConfig().getStringList("AllowUser");
                            String UserRole = DiscordUtil.getTopRole(member).getId();
                            if (!AllowRole.contains(UserRole)) {
                                Bukkit.getLogger().info("許可されていないユーザーです。");
                                player.kickPlayer(SettingManager.getConfig().getString("KickMessage"));
                            }
                        }
                    }
                }
            }
        };

        taskId = task.runTaskLater(this, initialDelaySeconds * 20L).getTaskId();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getServer().getScheduler().cancelTask(taskId);
    }
}
