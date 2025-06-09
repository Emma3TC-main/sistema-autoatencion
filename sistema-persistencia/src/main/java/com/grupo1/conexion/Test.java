/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1.conexion;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Test{
    public static void main(String[] args) {
        try {
            // Obtener instancia de ConexionSQL
            ConexionSQL conexionSQL = ConexionSQL.getInstancia();

            // Obtener una conexión desde HikariCP
            try (Connection conn = conexionSQL.getConexion()) {
                System.out.println("¡Conexión obtenida con éxito desde HikariCP!");

                // Ejecutar una consulta simple
                String sql = "SELECT @@VERSION";
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(sql)) {

                    if (rs.next()) {
                        System.out.println("SQL Server version: " + rs.getString(1));
                    }
                }
            }

            // Cerrar el pool (opcional, para pruebas)
            conexionSQL.cerrarPool();
            System.out.println("Pool de conexiones cerrado correctamente.");

        } catch (Exception e) {
            System.err.println("Error durante la prueba de conexión:");
            e.printStackTrace();
        }
    }
}
