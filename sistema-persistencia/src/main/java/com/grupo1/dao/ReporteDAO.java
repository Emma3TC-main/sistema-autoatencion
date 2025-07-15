/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1.dao;

/**
 *
 * @author PC
 */
import java.sql.SQLException;
import java.util.List;

public interface ReporteDAO {

    List<Object[]> listarPedidos() throws SQLException;

    List<Object[]> listarComandas() throws SQLException;

    List<Object[]> listarBoletas() throws SQLException;

    List<Object[]> listarGuiaRemision() throws SQLException;

    List<Object[]> listarRegistrosEntrega() throws SQLException;

    List<Object[]> listarClientes() throws SQLException;
}
