/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1.dao.impl;

import com.grupo1.conexion.ConexionSQL;
import com.grupo1.dao.MesaDAO;
import com.grupo1.dto.MesaDTO;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author emma3tc
 */
public class MesaDAOImpl implements MesaDAO {

    @Override
    public void insertar(MesaDTO mesa) throws SQLException {
        if (mesa == null) {
            throw new IllegalArgumentException("❌ La mesa no puede ser null.");
        }
        if (mesa.getNumero() <= 0) {
            throw new IllegalArgumentException("❌ El número de mesa debe ser positivo.");
        }
        if (mesa.getEstado() == null || mesa.getEstado().isBlank()) {
            throw new IllegalArgumentException("❌ El estado de la mesa es obligatorio.");
        }

        String sql = "{call proc_insertar_mesas(?,?,?)}"; // 3 parámetros, el último OUTPUT

        try (Connection con = ConexionSQL.getInstancia().getConexion(); CallableStatement cstmt = con.prepareCall(sql)) {

            cstmt.setInt(1, mesa.getNumero());
            cstmt.setString(2, mesa.getEstado());

            // Param OUTPUT
            cstmt.registerOutParameter(3, Types.INTEGER);

            int filas = cstmt.executeUpdate();
            if (filas == 0) {
                throw new SQLException("❌ No se insertó la mesa.");
            }

            // Obtener ID generado
            int idGenerado = cstmt.getInt(3);
            mesa.setIdMesa(idGenerado);
        }
    }

    @Override
    public MesaDTO buscarPorId(int id) throws SQLException {
        String sql = "{call proc_obtener_mesas_por_id(?)}";
        try (Connection con = ConexionSQL.getInstancia().getConexion(); CallableStatement cstmt = con.prepareCall(sql)) {
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
    public MesaDTO buscarPorNumero(int numero) throws SQLException {
        String sql = "{call proc_obtener_mesas_por_numero(?)}"; // Asegúrate de tener este SP en tu BD

        try (Connection con = ConexionSQL.getInstancia().getConexion(); CallableStatement cstmt = con.prepareCall(sql)) {

            cstmt.setInt(1, numero);

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
        try (Connection con = ConexionSQL.getInstancia().getConexion(); CallableStatement cstmt = con.prepareCall(sql); ResultSet rs = cstmt.executeQuery()) {

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
        try (Connection con = ConexionSQL.getInstancia().getConexion(); CallableStatement cstmt = con.prepareCall(sql)) {
            cstmt.setInt(1, mesa.getIdMesa());
            cstmt.setInt(2, mesa.getNumero());
            cstmt.setString(3, mesa.getEstado());
            cstmt.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws SQLException {
        String sql = "{call proc_eliminar_mesas(?)}";
        try (Connection con = ConexionSQL.getInstancia().getConexion(); CallableStatement cstmt = con.prepareCall(sql)) {
            cstmt.setInt(1, id);
            cstmt.executeUpdate();
        }
    }
}
