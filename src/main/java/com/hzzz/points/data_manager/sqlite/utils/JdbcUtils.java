package com.hzzz.points.data_manager.sqlite.utils;

import com.hzzz.points.text.Text;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.hzzz.points.utils.Utils.logError;

/**
 * 一些工具，用来快捷获取sqlite数据库的connection
 */
public class JdbcUtils {
    private static final String driver = "org.sqlite.JDBC";

    // 加载驱动
    static {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            logError(Text.database_driver_error);
            e.printStackTrace();
        }
    }

    /**
     * 获取sqlite数据库的connection
     *
     * @param url 地址
     * @return connection of database
     */
    public static Connection getConnection(String url) throws SQLException {
        return DriverManager.getConnection(url);
    }
}
