package com.hzzz.points.data_manager;

import com.hzzz.points.Points;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;

public class ConfigSQLite {
    private static final ConfigSQLite INSTANCE = new ConfigSQLite();
//    private final FileConfiguration CONFIG = Points.config;  // 读取配置
    private Connection con;  // 连接
    private Statement st = null;  // 数据库操作接口
    private boolean ready_flag = false;  // 是否准备好的标志

    public static ConfigSQLite getInstance() {
        return INSTANCE;
    }
    public Statement getStatement() {
        return st;
    }

    private ConfigSQLite() {
        setup();
    }

    private void setup() {  // 初始化数据库连接
        try {
            // 连接数据库
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:plugins/Points/config.sqlite");
            st = con.createStatement();
            // 创建表
            st.executeUpdate("CREATE TABLE if NOT EXISTS DeathMessageConfig(" +
                    "UUID CHAR(36) NOT NULL UNIQUE PRIMARY KEY, " +
                    "Name VARCHAR(255) NOT NULL, " +
                    "Enable INTEGER NOT NULL" +
                    ")");
            ready_flag = true;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void executeUpdate(String sql) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    st.executeUpdate(sql);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(Points.getInstance());
    }

    public boolean state() {  // 状态查询
        return (st != null && con != null && ready_flag);
    }
}
