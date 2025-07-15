/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1.dao.impl;

/**
 *
 * @author PC
 */
import com.grupo1.conexion.ConexionSQL;
import com.grupo1.dao.ReporteDAO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReporteDAOImpl implements ReporteDAO {

    private Connection obtenerConexion() throws SQLException {
        return ConexionSQL.getInstancia().getConexion();
    }

    private List<Object[]> ejecutarProcedure(String procedureName) throws SQLException {
        List<Object[]> lista = new ArrayList<>();

        try (Connection con = obtenerConexion(); CallableStatement stmt = con.prepareCall("{call " + procedureName + "}")) {

            try (ResultSet rs = stmt.executeQuery()) {
                ResultSetMetaData meta = rs.getMetaData();
                int columnas = meta.getColumnCount();
                while (rs.next()) {
                    Object[] fila = new Object[columnas];
                    for (int i = 0; i < columnas; i++) {
                        fila[i] = rs.getObject(i + 1);
                    }
                    lista.add(fila);
                }
            }
        }
        return lista;
    }

    @Override
    public List<Object[]> listarPedidos() throws SQLException {
        return ejecutarProcedure("proc_reporte_pedidos");
    }

    @Override
    public List<Object[]> listarComandas() throws SQLException {
        return ejecutarProcedure("proc_reporte_comandas");
    }

    @Override
    public List<Object[]> listarBoletas() throws SQLException {
        return ejecutarProcedure("proc_reporte_boletas");
    }

    @Override
    public List<Object[]> listarGuiaRemision() throws SQLException {
        return ejecutarProcedure("proc_reporte_guia_remision");
    }

    @Override
    public List<Object[]> listarRegistrosEntrega() throws SQLException {
        return ejecutarProcedure("proc_reporte_registros_entrega");
    }

    @Override
    public List<Object[]> listarClientes() throws SQLException {
        return ejecutarProcedure("proc_reporte_clientes");
    }
}
