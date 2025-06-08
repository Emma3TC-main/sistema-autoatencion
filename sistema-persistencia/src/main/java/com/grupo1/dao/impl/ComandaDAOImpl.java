/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1.dao.impl;

import com.grupo1.conexion.ConexionSQL;
import com.grupo1.dao.ComandaDAO;
import com.grupo1.dto.ComandaDTO;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author emma3tc
 */
public class ComandaDAOImpl implements ComandaDAO {

    @Override
    public void insertar(ComandaDTO comanda) throws SQLException {
        String sql = "{call proc_insertar_comandas(?, ?)}";
        try (CallableStatement cstmt = ConexionSQL.getInstancia().getConexion().prepareCall(sql)) {
            cstmt.setInt(1, comanda.getIdPedido());
            cstmt.setString(2, comanda.getEstado());
            cstmt.executeUpdate();
        }
    }

    @Override
    public ComandaDTO buscarPorId(int id) throws SQLException {
        String sql = "{call proc_obtener_comandas_por_id(?)}";
        try (CallableStatement cstmt = ConexionSQL.getInstancia().getConexion().prepareCall(sql)) {
            cstmt.setInt(1, id);
            try (ResultSet rs = cstmt.executeQuery()) {
                if (rs.next()) {
                    ComandaDTO comanda = new ComandaDTO();
                    comanda.setIdComanda(rs.getInt("id_comanda"));
                    comanda.setIdPedido(rs.getInt("id_pedido"));
                    comanda.setEstado(rs.getString("estado"));
                    return comanda;
                }
            }
        }
        return null;
    }

    @Override
    public List<ComandaDTO> listar() throws SQLException {
        List<ComandaDTO> lista = new ArrayList<>();
        String sql = "{call proc_obtener_todos_comandas}";
        try (CallableStatement cstmt = ConexionSQL.getInstancia().getConexion().prepareCall(sql); ResultSet rs = cstmt.executeQuery()) {
            while (rs.next()) {
                ComandaDTO comanda = new ComandaDTO();
                comanda.setIdComanda(rs.getInt("id_comanda"));
                comanda.setIdPedido(rs.getInt("id_pedido"));
                comanda.setEstado(rs.getString("estado"));
                lista.add(comanda);
            }
        }
        return lista;
    }

    @Override
    public void actualizar(ComandaDTO comanda) throws SQLException {
        String sql = "{call proc_actualizar_comandas(?, ?, ?)}";
        try (CallableStatement cstmt = ConexionSQL.getInstancia().getConexion().prepareCall(sql)) {
            cstmt.setInt(1, comanda.getIdComanda());
            cstmt.setInt(2, comanda.getIdPedido());
            cstmt.setString(3, comanda.getEstado());
            cstmt.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws SQLException {
        String sql = "{call proc_eliminar_comandas(?)}";
        try (CallableStatement cstmt = ConexionSQL.getInstancia().getConexion().prepareCall(sql)) {
            cstmt.setInt(1, id);
            cstmt.executeUpdate();
        }
    }
}
