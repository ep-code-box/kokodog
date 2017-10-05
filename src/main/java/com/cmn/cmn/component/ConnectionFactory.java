package com.cmn.cmn.component;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnection;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;

public class ConnectionFactory {
  private static interface Singleton {
    final ConnectionFactory INSTANCE = new ConnectionFactory();
  }

  private final DataSource dataSource;

  private ConnectionFactory() {
    try {
      Class.forName("com.mysql.jdbc.Driver");
    } catch (Exception e) {
      System.out.println("JDBC Driver load failed");
    }
    String propFile = this.getClass().getResource("/").getPath() + "/conf/log4j_db_info.properties";
    Properties props = new Properties();
    FileInputStream fis = null;
    try {
      fis = new FileInputStream(propFile);
      props.load(new java.io.BufferedInputStream(fis));
    } catch (Exception e) {
      System.out.println("Exception : getting log4j file info is failed.[" + (new File(propFile)).getAbsolutePath() + "]");
    }
    Properties properties = new Properties();
    properties.setProperty("user", props.getProperty("user"));
    properties.setProperty("password", props.getProperty("password"));
    GenericObjectPool<PoolableConnection> pool = new GenericObjectPool<PoolableConnection>();
    DriverManagerConnectionFactory connectionFactory = new DriverManagerConnectionFactory(props.getProperty("url"), properties);
    new PoolableConnectionFactory(connectionFactory, pool, null, "SELECT 1", 3, false, false, Connection.TRANSACTION_READ_COMMITTED);
    this.dataSource = new PoolingDataSource(pool);
  }

  public static Connection getDatabaseConnection() throws SQLException {
    return Singleton.INSTANCE.dataSource.getConnection();
  }
}