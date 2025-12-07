package com.backend;

import static spark.Spark.*;
import java.io.InputStream;

public class Main {

    public static void main(String[] args) {
        port(getAssignedPort());

        enableCORS();

        // Health check
        get("/ping", (req, res) -> "Backend is running");

        // Serve users.json file
        get("/data", (req, res) -> {
            res.type("application/json");

            try {
                // Load the file from src/main/resources/users.json
                InputStream is = Main.class.getResourceAsStream("/users.json");

                if (is == null) {
                    System.err.println("users.json not found!");
                    return "[]";
                }

                return new String(is.readAllBytes());
            } catch (Exception e) {
                e.printStackTrace();
                return "[]";
            }
        });
    }

    // Port for Render
    private static int getAssignedPort() {
        String port = System.getenv("PORT");
        return port != null ? Integer.parseInt(port) : 4567;
    }

    // Allow GitHub Pages to fetch from Render
    private static void enableCORS() {
        options("/*",
                (request, response) -> {
                    String reqHeaders = request.headers("Access-Control-Request-Headers");
                    if (reqHeaders != null) response.header("Access-Control-Allow-Headers", reqHeaders);

                    String reqMethod = request.headers("Access-Control-Request-Method");
                    if (reqMethod != null) response.header("Access-Control-Allow-Methods", reqMethod);

                    return "OK";
                });

        before((req, res) -> {
            res.header("Access-Control-Allow-Origin", "*");
            res.header("Access-Control-Allow-Methods", "*");
            res.header("Access-Control-Allow-Headers", "*");
        });
    }
}
