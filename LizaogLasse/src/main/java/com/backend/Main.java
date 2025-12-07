package com.backend;

import static spark.Spark.*;
import com.google.gson.*;
import java.sql.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        port(getAssignedPort());
        Gson gson = new Gson();

        createTable();

        get("/ping", (req, res) -> "Backend is running");

        post("/save", (req, res) -> {
            User u = gson.fromJson(req.body(), User.class);
            saveToDB(u);
            return "OK";
        });

        get("/data", (req, res) -> {
            List<User> list = loadAll();
            res.type("application/json");
            return gson.toJson(list);
        });
    }

    private static int getAssignedPort() {
        String port = System.getenv("PORT");
        return port != null ? Integer.parseInt(port) : 4567;
    }

    private static void createTable() {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:database.db")) {
            String sql = """
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT,
                    email TEXT
                );
            """;
            conn.createStatement().execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void saveToDB(User u) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:database.db")) {
            String sql = "INSERT INTO users(name, email) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, u.name);
            ps.setString(2, u.email);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<User> loadAll() {
        List<User> list = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:database.db")) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM users");
            while (rs.next()) {
                list.add(new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
