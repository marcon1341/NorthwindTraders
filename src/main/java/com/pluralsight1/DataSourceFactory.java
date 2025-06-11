package com.pluralsight1;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;

public class DataSourceFactory {
    private static final String JDBC_URL =
            "jdbc:mysql://localhost:3306/northwind?useSSL=false"
                    + "&allowPublicKeyRetrieval=true"
                    + "&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "Msy.1341";//Mysql pass

    //creating datasource once
    private  static  final BasicDataSource ds = new BasicDataSource();
    static {
        ds.setUrl(JDBC_URL);
        ds.setUsername(DB_USER);
        ds.setPassword(DB_PASS);
    }
    public static DataSource getDataSource(){
        return ds;
    }
    
}
