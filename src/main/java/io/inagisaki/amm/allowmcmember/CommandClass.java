package io.inagisaki.amm.allowmcmember;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Guild;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Member;
import github.scarsz.discordsrv.util.DiscordUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;
public class CommandClass implements CommandExecutor, TabCompleter{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (Arrays.asList("allowmcmember", "amm").contains(command.getName())) {
            if (sender.isOp()) {
                if (args.length == 0) {
                    return true;
                } else {
                    if (args[0].equalsIgnoreCase("reload")) { //サブコマンドが「hello」かどうか
                        sender.sendMessage("再読み込み中....");
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
                        sender.sendMessage("再読み込みに成功しました。");
                    }
                    return true;
                }
            }
        }
        return false;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (Arrays.asList("allowmcmember", "amm").contains(command.getName())) {
            if (args.length == 1) {
                if (args[0].length() == 0) {
                    return Arrays.asList("reload");
                } else {
                    //入力されている文字列と先頭一致
                    if ("reload".startsWith(args[0])) {
                        return Collections.singletonList("reload");
                    }
                }
            }
        }
        return null;
    }
}
