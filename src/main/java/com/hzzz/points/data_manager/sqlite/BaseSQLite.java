package com.hzzz.points.data_manager.sqlite;

import com.hzzz.points.Points;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static com.hzzz.points.utils.Text.getMessage;
import static com.hzzz.points.utils.Utils.logError;
import static com.hzzz.points.utils.msgKey.database_driver_error;
import static com.hzzz.points.utils.msgKey.database_error;

/**
 * <p>sqlite数据库对象 基类</p>
 *
 * @author <a href="https://github.com/HowieHz/">HowieHz</a>
 * @version v0.2.3
 * @since 2022-10-05 20:34
 */
public abstract class BaseSQLite {
    protected final Connection con;  // 连接
    protected final Statement st;  // 数据库操作接口

    private static final String DRIVER = "org.sqlite.JDBC";

    // 加载驱动
    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            logError(getMessage(database_driver_error));
            e.printStackTrace();
        }
    }

    /**
     * 获取操作接口Statement
     *
     * @return statement of sqlite
     */
    public Statement getStatement() {
        return st;
    }

    /**
     * 获取操作接口Connection
     *
     * @return Connection of sqlite
     */
    public Connection getConnection() {
        return con;
    }

    /**
     * 初始化
     *
     * @param url 地址
     * @throws SQLException 出现数据库错误
     */
    protected BaseSQLite(String url) throws SQLException {
        // 连接数据库 初始化st
        con = DriverManager.getConnection(url);
        st = con.createStatement();
    }

    /**
     * 异步执行sql语句
     *
     * @param sql sql语句
     */
    public void asyncExecuteUpdate(String sql) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    st.executeUpdate(sql);
                } catch (SQLException e) {
                    logError(getMessage(database_error));
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(Points.getInstance());
    }

    /**
     * 检查数据库状态是否准备好<br>（检查Statement和Connection是否调用过close方法）<br>
     * 准备好则返回true
     *
     * @return 是否可用
     */
    public boolean isReady() {  // 状态查询
        return (st != null && con != null);
    }
}
