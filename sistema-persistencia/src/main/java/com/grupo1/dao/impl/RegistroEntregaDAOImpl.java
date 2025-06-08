/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1.dao.impl;

import com.grupo1.conexion.ConexionSQL;
import com.grupo1.dao.RegistroEntregaDAO;
import com.grupo1.dto.RegistroEntregaDTO;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author emma3tc
 */
public class RegistroEntregaDAOImpl implements RegistroEntregaDAO {

    @Override
    public void insertar(RegistroEntregaDTO registro) throws SQLException {
        String sql = "{call proc_insertar_registro_entregas(?, ?, ?, ?)}";
        try (CallableStatement cstmt = ConexionSQL.getInstancia().getConexion().prepareCall(sql)) {
            cstmt.setInt(1, registro.getIdPedido());
            cstmt.setInt(2, registro.getIdUsuario());
            cstmt.setString(3, registro.getMetodoEntrega());
            cstmt.setString(4, registro.getFirmaCliente());
            cstmt.executeUpdate();
        }
    }

    @Override
    public RegistroEntregaDTO buscarPorId(int id) throws SQLException {
        String sql = "{call proc_obtener_registro_entregas_por_id(?)}";
        try (CallableStatement cstmt = ConexionSQL.getInstancia().getConexion().prepareCall(sql)) {
            cstmt.setInt(1, id);
            try (ResultSet rs = cstmt.executeQuery()) {
                if (rs.next()) {
                    RegistroEntregaDTO r = new RegistroEntregaDTO();
                    r.setIdRegistro(rs.getInt("id_registro"));
                    r.setIdPedido(rs.getInt("id_pedido"));
                    r.setIdUsuario(rs.getInt("id_usuario"));
                    r.setMetodoEntrega(rs.getString("metodo_entrega"));
                    r.setFirmaCliente(rs.getString("firma_cliente"));
                    return r;
                }
            }
        }
        return null;
    }

    @Override
    public List<RegistroEntregaDTO> listar() throws SQLException {
        List<RegistroEntregaDTO> lista = new ArrayList<>();
        String sql = "{call proc_obtener_todos_registro_entregas}";
        try (CallableStatement cstmt = ConexionSQL.getInstancia().getConexion().prepareCall(sql); ResultSet rs = cstmt.executeQuery()) {
            while (rs.next()) {
                RegistroEntregaDTO r = new RegistroEntregaDTO();
                r.setIdRegistro(rs.getInt("id_registro"));
                r.setIdPedido(rs.getInt("id_pedido"));
                r.setIdUsuario(rs.getInt("id_usuario"));
                r.setMetodoEntrega(rs.getString("metodo_entrega"));
                r.setFirmaCliente(rs.getString("firma_cliente"));
                lista.add(r);
            }
        }
        return lista;
    }

    @Override
    public void actualizar(RegistroEntregaDTO registro) throws SQLException {
        String sql = "{call proc_actualizar_registro_entregas(?, ?, ?, ?, ?)}";
        try (CallableStatement cstmt = ConexionSQL.getInstancia().getConexion().prepareCall(sql)) {
            cstmt.setInt(1, registro.getIdRegistro());
            cstmt.setInt(2, registro.getIdPedido());
            cstmt.setInt(3, registro.getIdUsuario());
            cstmt.setString(4, registro.getMetodoEntrega());
            cstmt.setString(5, registro.getFirmaCliente());
            cstmt.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws SQLException {
        String sql = "{call proc_eliminar_registro_entregas(?)}";
        try (CallableStatement cstmt = ConexionSQL.getInstancia().getConexion().prepareCall(sql)) {
            cstmt.setInt(1, id);
            cstmt.executeUpdate();
        }
    }
}
