/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 *
 * @author emma3tc
 */
public class ConexionSQL {

    private static ConexionSQL instancia;
    private HikariDataSource dataSource;

    private final String url = "jdbc:sqlserver://DESKTOP-LFBN0IP\\SQLEXPRESS:1433;databaseName=Restaurante;encrypt=true;trustServerCertificate=true;";
    private final String usuario = "java_user";
    private final String clave = "grupo1";

    private ConexionSQL() {
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(url);
            config.setUsername(usuario);
            config.setPassword(clave);

            // Configuraciones opcionales
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setIdleTimeout(60000);
            config.setConnectionTimeout(30000);
            config.setPoolName("MiPoolHikari");

            dataSource = new HikariDataSource(config);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ConexionSQL getInstancia() {
        if (instancia == null) {
            synchronized (ConexionSQL.class) {
                if (instancia == null) {
                    instancia = new ConexionSQL();
                }
            }
        }
        return instancia;
    }

    public Connection getConexion() throws SQLException {
        return dataSource.getConnection();
    }

    public void cerrarPool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }

    public HikariDataSource getDataSource() {
        return dataSource;
    }

}
