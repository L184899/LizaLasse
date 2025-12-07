package com.backend;

import static spark.Spark.*;
import com.google.gson.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        port(getAssignedPort());
        Gson gson = new Gson();

        get("/ping", (req, res) -> "Backend is running");

        get("/data", (req, res) -> {
            res.type("application/json");
            return gson.toJson(loadUsers());
        });
    }

    private static int getAssignedPort() {
        String port = System.getenv("PORT");
        return port != null ? Integer.parseInt(port) : 4567;
    }

    private static List<User> loadUsers() {
        try {
            String path = "src/main/resources/users.json";

            System.out.println("Loading JSON from: " + path);

            String json = Files.readString(Paths.get(path));
            Gson gson = new Gson();

            User[] arr = gson.fromJson(json, User[].class);
            return Arrays.asList(arr);

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
