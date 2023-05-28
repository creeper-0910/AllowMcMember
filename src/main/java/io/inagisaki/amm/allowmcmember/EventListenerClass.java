package io.inagisaki.amm.allowmcmember;


import github.scarsz.discordsrv.dependencies.jda.api.entities.Guild;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.util.DiscordUtil;
import org.bukkit.Bukkit;
import java.util.List;
import java.time.LocalDate;
import java.time.DayOfWeek;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Member;

public class EventListenerClass implements Listener {
    @EventHandler
    public void onJoinPlayer(PlayerJoinEvent e) {
        SettingManager.reloadConfig();
        LocalDate currentDate = LocalDate.now();
        DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
        Bukkit.getLogger().info(dayOfWeek.toString());
        List<String> restrictionDays = SettingManager.getConfig().getStringList("RestrictionDay");
        if (restrictionDays.contains(dayOfWeek.toString())) {
            String userId = DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(e.getPlayer().getUniqueId());
            List<Guild> guilds = DiscordUtil.getJda().getGuilds();
            String guildId = guilds.get(0).getId().toString();
            try {
                Member member = DiscordUtil.getJda().getGuildById(guildId).getMemberById(userId);
                if (member != null) {
                    Bukkit.getLogger().info(DiscordUtil.getTopRole(member).getId());
                    List<String> allowRoles = SettingManager.getConfig().getStringList("AllowUser");
                    String userRole = DiscordUtil.getTopRole(member).getId();
                    if (!allowRoles.contains(userRole)) {
                        Bukkit.getLogger().info("許可されていないユーザーです。");
                        e.getPlayer().kickPlayer(SettingManager.getConfig().getString("KickMessage"));
                    }
                }
            } catch (IllegalArgumentException err) {
                Bukkit.getLogger().info("未認証のユーザーです。");
                String linkId = DiscordSRV.getPlugin().getAccountLinkManager().generateCode(e.getPlayer().getUniqueId());
                String botName = DiscordSRV.getPlugin().getMainGuild().getSelfMember().getUser().getName();
                String linkText = String.format(SettingManager.getConfig().getString("linkMessage"),botName,linkId);
                e.getPlayer().kickPlayer(linkText);
            }
        }
    }
}
