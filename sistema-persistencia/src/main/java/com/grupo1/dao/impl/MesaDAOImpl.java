/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1.dao.impl;

import com.grupo1.conexion.ConexionSQL;
import com.grupo1.dao.MesaDAO;
import com.grupo1.dto.MesaDTO;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author emma3tc
 */
public class MesaDAOImpl implements MesaDAO {
     @Override
    public void insertar(MesaDTO mesa) throws SQLException {
        String sql = "{call proc_insertar_mesas(?, ?)}";
        try (CallableStatement cstmt = ConexionSQL.getInstancia().getConexion().prepareCall(sql)) {
            cstmt.setInt(1, mesa.getNumero());
            cstmt.setString(2, mesa.getEstado());
            cstmt.executeUpdate();
        }
    }

    @Override
    public MesaDTO buscarPorId(int id) throws SQLException {
        String sql = "{call proc_obtener_mesas_por_id(?)}";
        try (CallableStatement cstmt = ConexionSQL.getInstancia().getConexion().prepareCall(sql)) {
            cstmt.setInt(1, id);
            try (ResultSet rs = cstmt.executeQuery()) {
                if (rs.next()) {
                    MesaDTO mesa = new MesaDTO();
                    mesa.setIdMesa(rs.getInt("id_mesa"));
                    mesa.setNumero(rs.getInt("numero"));
                    mesa.setEstado(rs.getString("estado"));
                    return mesa;
                }
            }
        }
        return null;
    }

    @Override
    public List<MesaDTO> listar() throws SQLException {
        List<MesaDTO> mesas = new ArrayList<>();
        String sql = "{call proc_obtener_todos_mesas}";
        try (CallableStatement cstmt = ConexionSQL.getInstancia().getConexion().prepareCall(sql);
             ResultSet rs = cstmt.executeQuery()) {

            while (rs.next()) {
                MesaDTO mesa = new MesaDTO();
                mesa.setIdMesa(rs.getInt("id_mesa"));
                mesa.setNumero(rs.getInt("numero"));
                mesa.setEstado(rs.getString("estado"));
                mesas.add(mesa);
            }
        }
        return mesas;
    }

    @Override
    public void actualizar(MesaDTO mesa) throws SQLException {
        String sql = "{call proc_actualizar_mesas(?, ?, ?)}";
        try (CallableStatement cstmt = ConexionSQL.getInstancia().getConexion().prepareCall(sql)) {
            cstmt.setInt(1, mesa.getIdMesa());
            cstmt.setInt(2, mesa.getNumero());
            cstmt.setString(3, mesa.getEstado());
            cstmt.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws SQLException {
        String sql = "{call proc_eliminar_mesas(?)}";
        try (CallableStatement cstmt = ConexionSQL.getInstancia().getConexion().prepareCall(sql)) {
            cstmt.setInt(1, id);
            cstmt.executeUpdate();
        }
    }
}
