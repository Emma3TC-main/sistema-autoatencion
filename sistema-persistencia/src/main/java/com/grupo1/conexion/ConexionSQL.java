/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author emma3tc
 */
public class ConexionSQL {
    private static ConexionSQL instancia;
    private Connection conexion;

    private final String url = "jdbc:sqlserver://localhost:1433;databaseName=Restaurante"; //revisar puerto
    private final String usuario = "tu_usuario";
    private final String clave = "tu_clave";

    private ConexionSQL() {
        try {
            conexion = DriverManager.getConnection(url, usuario, clave);
        } catch (SQLException e) {
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

    public Connection getConexion() {
        return conexion;
    }
}
