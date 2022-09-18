package com.hzzz.points;

import com.hzzz.points.commands.Death;
import com.hzzz.points.commands.Here;
import com.hzzz.points.commands.PointsCommand;
import com.hzzz.points.commands.Where;
import com.hzzz.points.data_manager.DeathSQLite;
import com.hzzz.points.listeners.DeathListeners;
import com.hzzz.points.text.text;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import static org.bukkit.ChatColor.BLUE;

public final class Points extends JavaPlugin {
    public static FileConfiguration config;
    public static Logger logger = Logger.getLogger("Points");
    private static Points INSTANCE;
    private final List<String> commands = new ArrayList<>();

    private final List<Listener> eventHandlers = new ArrayList<>();

    @Override
    public void onLoad() {
        logger.info(BLUE + "<Points>插件加载");
        saveDefaultConfig();  // 如果配置文件不存在, 保存默认的配置
    }

    @Override
    public void onEnable() {
        INSTANCE = this;

        // 读取配置
        config = getConfig();

        // here指令
        if (config.getBoolean("here.enable", false)) {
            setExecutor("here", Here.getInstance());
        }

        // where指令
        if (config.getBoolean("here.enable", false)) {
            setExecutor("where", Where.getInstance());
        }

        // death模块
        if (config.getBoolean("death.enable", false)) {
            // 数据库检查 启动数据库
            if (DeathSQLite.getInstance().state()){
                logger.info(String.format(text.sqlite_ready,"death.sqlite"));
                // 数据库成功启动才启动death模块

                // death指令
                setExecutor("death", Death.getInstance());

                // 注册监听
                if (config.getBoolean("death.message.enable", false)) {
                    registerEvents(DeathListeners.getInstance());
                }
            }else{
                logger.info(String.format(text.sqlite_not_ready,"death.sqlite"));
            }
        }

        // points指令
        Objects.requireNonNull(Bukkit.getPluginCommand("points")).setExecutor(PointsCommand.getInstance());

        // 启动消息
        logger.info(BLUE + "<Points>插件启动");
    }

    public void registerEvents(Listener listener) {
        eventHandlers.add(listener);
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

    public void setExecutor(String command, CommandExecutor executor) {
        commands.add(command);
        Objects.requireNonNull(Bukkit.getPluginCommand(command)).setExecutor(executor);
    }

    public void disableExecutor() {
        for (String command : commands) {
            Objects.requireNonNull(Bukkit.getPluginCommand(command)).setExecutor(null);
        }
        commands.clear();
    }

    public void disableEventHandler() {
        for (Listener listener : eventHandlers) {
            HandlerList.unregisterAll(listener);
        }
        eventHandlers.clear();
    }

    public void onReload() {
        // reload一遍配置文件，用于重载
        reloadConfig();

        onDisable();
        onEnable();
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);  // 关闭插件时, 确保取消我调度的所有任务
        disableExecutor();  // 卸载指令
        disableEventHandler();  // 卸载监听器
        // 消息
        logger.info(BLUE + "<Points>插件关闭");
    }

    public static Points getInstance() {  // 获取实例的方法
        return INSTANCE;
    }
}