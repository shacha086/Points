package com.hzzz.points.commands;

import com.hzzz.points.text.text;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.hzzz.points.data_manager.operations_set.DeathLog.outputDeathLog;
import static com.hzzz.points.utils.Utils.checkPermission;
import static com.hzzz.points.data_manager.operations_set.DeathMessageConfig.updateDeathMessageConfig;
import static com.hzzz.points.Points.config;

public final class Death implements CommandExecutor {
    private static final Death INSTANCE = new Death();

    public static Death getInstance() {
        return INSTANCE;
    }

    private Death() {
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (args.length == 0) {
            // /death
            sender.sendMessage(text.help_death);
            return true;
        }

        switch (args[0]) {
            case "message" -> {
                if (config.getBoolean("death.message.enable", false)) {  // 检查子模块是否开启
                    // 检查执行者
                    if (!(sender instanceof Player player)) {
                        sender.sendMessage(text.player_only);
                        return true;
                    }
                    // 权限检查
                    if (config.getBoolean("death.message.command-permission.enable", false)
                            && !checkPermission(sender, config.getString("death.message.command-permission.node", "points.command.death.message"))) {
                        sender.sendMessage(text.no_permission);
                        return true;
                    }
                    if (args.length > 1) {  // 参数过多语法错误
                        return false;
                    }

                    if (updateDeathMessageConfig(player)) {  // 更改数据库config
                        sender.sendMessage(text.enable_death_message);
                    } else {
                        sender.sendMessage(text.disable_death_message);
                    }

                }else{
                    sender.sendMessage(text.disable_module);
                }
            }
            case "log" -> {
                if (config.getBoolean("death.log.enable", false)) {  // 检查子模块是否开启
                    if (args.length == 1) {  // /death log
                        // 检查执行者
                        if (!(sender instanceof Player player)) {
                            sender.sendMessage(text.player_only);
                            return true;
                        }
                        // 权限检查
                        if (config.getBoolean("death.log.permission.enable", false)
                                && !checkPermission(sender, config.getString("death.log.permission.node.self", "points.command.death.log.self"))) {
                            sender.sendMessage(text.no_permission);
                            return true;
                        }
                        outputDeathLog(player, player);  // 查看自己的log

                    }else {  // /death log Howie_HzGo
                        // 权限检查
                        if (config.getBoolean("death.log.permission.enable", false)
                                && !checkPermission(sender, config.getString("death.log.permission.node.other", "points.command.death.log.other"))
                                && !checkPermission(sender, String.format(config.getString("death.log.permission.node.player", "points.command.death.log.%s"), args[0]))) {
                            sender.sendMessage(text.no_permission);
                            return true;
                        }

                        Player target_player = Bukkit.getPlayer(args[0]);
                        if (target_player == null) {  // 检查是否获取到玩家
                            sender.sendMessage(text.no_player);
                            return true;
                        }
                        outputDeathLog(target_player, sender);  // 查看玩家的log
                    }

                }else{
                    sender.sendMessage(text.disable_module);
                }
            }
            default -> sender.sendMessage(text.help_death);
        }
        return true;
    }
}
