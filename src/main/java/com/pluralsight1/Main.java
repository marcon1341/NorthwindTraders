package com.pluralsight1;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Scanner;

public class Main {

//    private static final String JDBC_URL =
//            "jdbc:mysql://localhost:3306/northwind?useSSL=false"
//                    + "&allowPublicKeyRetrieval=true"
//                    + "&serverTimezone=UTC";
//
//    private static final String DB_USER = "root";
//    private static final String DB_PASS = "Msy.1341";//Mysql pass

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        boolean running = true;

        DataSource ds = DataSourceFactory.getDataSource();
        while (running) {
            System.out.println("""
                    What do you want to do?
                    1) Display all products
                    2) Display all customers
                    3) Display all categories
                    0) Exit
                    Select an option:
                    """);
            String choice = s.nextLine().trim();

            switch (choice) {
                case "1":
                    displayAllProducts(ds);
                    break;
                case "2":
                    displayAllCustomers(ds);
                    break;
                case "3":
                    displayAllCategories(s,ds);
                    break;
                case "0":
                    running = false;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
        s.close();
    }

    private static void displayAllProducts(DataSource ds) {
        String sql = "SELECT ProductId, ProductName, UnitPrice, UnitsInStock FROM Products";

        System.out.println("ALL PRODUCTS");
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()
        ) {
            System.out.printf("%-4s  %-25s  %24.2s  %14s%n", "ID", "Name", "Price", "Stock");
            while (rs.next()) {
                System.out.printf("%-4d  %-25s  %24.2f  %14d%n",
                        rs.getInt("ProductID"),
                        rs.getString("ProductName"),
                        rs.getDouble("UnitPrice"),
                        rs.getInt("UnitsInStock"));
            }

        } catch (SQLException ex) {
            System.err.println("Error querying Northwind database:");
            ex.printStackTrace();
        }

    }

    private static void displayAllCustomers(DataSource ds) {
        String sql = "SELECT ContactName, CompanyName, City, Country, Phone FROM Customers ORDER BY Country";

        System.out.println("\nAll CUSTOMERS");
        try (
                Connection conn = ds.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            System.out.printf("%-20s  %-30s  %-15s  %-15s  %s%n",
                    "Contact", "Company", "City", "Country", "Phone");
            while (rs.next()) {
                System.out.printf(
                        "%-20s %-30s %-15s %-15s %s%n",
                        rs.getString("ContactName"),
                        rs.getString("CompanyName"),
                        rs.getString("City"),
                        rs.getString("Country"),
                        rs.getString("Phone"));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving customers:");
            e.printStackTrace();
        }
    }

    private static void displayAllCategories(Scanner s, DataSource ds) {
        String sql = "SELECT categoryId, categoryName FROM categories order by CategoryID";

        System.out.println("\nALL CATEGORIES");
        try (
                Connection conn = ds.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            System.out.printf("%-4s %s%n", "ID", "Category Name");

            while (rs.next()) {
                System.out.printf("%-4d %s%n",
                        rs.getInt("CategoryId"),
                        rs.getString("CategoryName"));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving categories:");
            e.printStackTrace();
        }

        System.out.print("\nEnter the CategoryID to view its products: ");
        String line = s.nextLine().trim();
        int categoryId;
        try {
            categoryId = Integer.parseInt(line);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number – returning to menu.");
            return;
        }

        String sqlProds = """
                SELECT ProductID, ProductName, UnitPrice, UnitsInStock
                  FROM Products
                 WHERE CategoryID = ?
                 ORDER BY ProductName
                """;

        System.out.printf("%nProducts in Category %d %n", categoryId);
        try (
                Connection conn = ds.getConnection();
                PreparedStatement ps2 = conn.prepareStatement(sqlProds)
        ) {
            ps2.setInt(1, categoryId);
            try (ResultSet rs2 = ps2.executeQuery()) {

                System.out.printf("%-4s  %-30s  %8s  %6s%n",
                        "ID", "Name", "Price", "Stock");

                boolean found = false;
                while (rs2.next()) {
                    found = true;
                    System.out.printf("%-4d  %-30s  %8.2f  %6d%n",
                        rs2.getInt("ProductID"),
                        rs2.getString("ProductName"),
                        rs2.getDouble("UnitPrice"),
                        rs2.getInt("UnitsInStock"));
                }
                if (!found) {
                    System.out.println("No products found for CategoryID " + categoryId);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error querying products for category " + categoryId + ":");
            ex.printStackTrace();
        }
    }
}




