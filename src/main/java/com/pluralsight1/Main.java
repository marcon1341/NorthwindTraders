package com.pluralsight1;

import java.sql.*;

public class Main {

    private static final String JDBC_URL =
            "jdbc:mysql://localhost:3306/northwind?useSSL=false"
                    + "&allowPublicKeyRetrieval=true"
                    + "&serverTimezone=UTC";

    private static final String DB_USER = "root";
    private static final String DB_PASS = "PASSWORD";//Mysql pass

    public static void main(String[] args) {
        String sql = "SELECT ProductName FROM Products";

        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("All products in Northwind:");

            while (rs.next()) {
                System.out.println(rs.getString("ProductName"));
            }
        }
        catch (SQLException ex) {
            System.err.println("Error querying Northwind database:");
            ex.printStackTrace();
        }
    }
}
