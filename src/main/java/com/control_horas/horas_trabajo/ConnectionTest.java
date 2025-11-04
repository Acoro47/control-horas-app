package com.control_horas.horas_trabajo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import io.github.cdimascio.dotenv.Dotenv;

public class ConnectionTest {

	public static void main(String[] args) {

		// 1. Carga variables de .env
        Dotenv dotenv = Dotenv.load();

        String host     = dotenv.get("PGHOST");
        String port     = dotenv.get("PGPORT");
        String database = dotenv.get("PGDATABASE");
        String user     = dotenv.get("PGUSER");
        String pass     = dotenv.get("PGPASSWORD");
        String sslMode     = dotenv.get("PGSSLMODE");

        // 2. Construye la URL JDBC
        String jdbcUrl = String.format(
            "jdbc:postgresql://%s:%s/%s?%s",
            host, port, database,sslMode
        );

        // 3. Intenta la conexión
        try (Connection conn = DriverManager.getConnection(jdbcUrl, user, pass)) {
            System.out.println("Conexión exitosa a la base de datos!");
        } catch (SQLException e) {
            e.printStackTrace();
        }

	}

}
