/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1.conexion;

/**
 *
 * @author PC
 */
import java.sql.Connection;
import java.sql.SQLException;
import com.zaxxer.hikari.HikariPoolMXBean;
import com.zaxxer.hikari.HikariDataSource;

public class TestConexionSQL {

    public static void main(String[] args) {
        ConexionSQL conexionSQL = ConexionSQL.getInstancia();

        // Mostrar estado del pool antes
        imprimirEstadoPool(conexionSQL);

        for (int i = 1; i <= 15; i++) {
            try (Connection con = conexionSQL.getConexion()) {
                System.out.println("Conexión #" + i + " abierta correctamente.");
                Thread.sleep(500); // Simula tiempo de uso
            } catch (SQLException e) {
                System.err.println("Error al obtener conexión: " + e.getMessage());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Mostrar estado del pool después
        imprimirEstadoPool(conexionSQL);
    }

    private static void imprimirEstadoPool(ConexionSQL conexionSQL) {
        HikariDataSource ds = conexionSQL.getDataSource();
        HikariPoolMXBean poolStatus = ds.getHikariPoolMXBean();

        System.out.println("---- ESTADO DEL POOL ----");
        System.out.println("Activas: " + poolStatus.getActiveConnections());
        System.out.println("En espera: " + poolStatus.getThreadsAwaitingConnection());
        System.out.println("Totales: " + poolStatus.getTotalConnections());
        System.out.println("Inactivas: " + poolStatus.getIdleConnections());
        System.out.println("--------------------------");
    }
}
