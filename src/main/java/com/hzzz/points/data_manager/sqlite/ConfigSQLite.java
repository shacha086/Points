package com.hzzz.points.data_manager.sqlite;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 管理config.sqlite
 */
public class ConfigSQLite extends BaseSQLite {
    private static final ConfigSQLite instance;

    static {
        try {
            instance = new ConfigSQLite();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public final PreparedStatement psInsertDeathConfig;
    public final PreparedStatement psSelectDeathConfig;
    public final PreparedStatement psUpdateDeathConfig;

    /**
     * 获取数据库实例
     *
     * @return statement of database
     */
    public static ConfigSQLite getInstance() {
        return instance;
    }

    /**
     * 单例 无参数 初始化数据库
     */
    private ConfigSQLite() throws SQLException {
        super("jdbc:sqlite:plugins/Points/database/config.sqlite");
        // 创建表
        st.executeUpdate("CREATE TABLE if NOT EXISTS DeathMessageConfig(" +
                "uuid CHAR(36) NOT NULL UNIQUE PRIMARY KEY, " +
                "username VARCHAR(255) NOT NULL, " +
                "enable INTEGER NOT NULL" +
                ")");
        // 初始化
        psInsertDeathConfig = con.prepareStatement("INSERT OR IGNORE INTO DeathMessageConfig(uuid, username, enable) VALUES (?, ?, 1)");
        psSelectDeathConfig = con.prepareStatement("SELECT * FROM DeathMessageConfig WHERE uuid=?");
        psUpdateDeathConfig = con.prepareStatement("UPDATE DeathMessageConfig SET enable=? WHERE uuid=?");
    }
}
