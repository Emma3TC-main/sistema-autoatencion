/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1.dao.impl;

import com.grupo1.conexion.ConexionSQL;
import com.grupo1.dao.PedidoDAO;
import com.grupo1.dto.PedidoDTO;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author emma3tc
 */
public class PedidoDAOImpl implements PedidoDAO {

    @Override
    public void insertar(PedidoDTO pedido) throws SQLException {
        String sql = "{call proc_insertar_pedidos(?, ?, ?, ?)}";
        try (CallableStatement cstmt = ConexionSQL.getInstancia().getConexion().prepareCall(sql)) {
            cstmt.setInt(1, pedido.getIdCliente());
            cstmt.setInt(2, pedido.getIdMesa());
            cstmt.setString(3, pedido.getTipoConsumo());
            cstmt.setString(4, pedido.getEstado());
            cstmt.executeUpdate();
        }
    }

    @Override
    public PedidoDTO buscarPorId(int id) throws SQLException {
        String sql = "{call proc_obtener_pedidos_por_id(?)}";
        try (CallableStatement cstmt = ConexionSQL.getInstancia().getConexion().prepareCall(sql)) {
            cstmt.setInt(1, id);
            try (ResultSet rs = cstmt.executeQuery()) {
                if (rs.next()) {
                    PedidoDTO pedido = new PedidoDTO();
                    pedido.setIdPedido(rs.getInt("id_pedido"));
                    pedido.setIdCliente(rs.getInt("id_cliente"));
                    pedido.setIdMesa(rs.getInt("id_mesa"));
                    pedido.setTipoConsumo(rs.getString("tipo_consumo"));
                    pedido.setEstado(rs.getString("estado"));
                    return pedido;
                }
            }
        }
        return null;
    }

    @Override
    public List<PedidoDTO> listar() throws SQLException {
        List<PedidoDTO> pedidos = new ArrayList<>();
        String sql = "{call proc_obtener_todos_pedidos}";
        try (CallableStatement cstmt = ConexionSQL.getInstancia().getConexion().prepareCall(sql); ResultSet rs = cstmt.executeQuery()) {
            while (rs.next()) {
                PedidoDTO pedido = new PedidoDTO();
                pedido.setIdPedido(rs.getInt("id_pedido"));
                pedido.setIdCliente(rs.getInt("id_cliente"));
                pedido.setIdMesa(rs.getInt("id_mesa"));
                pedido.setTipoConsumo(rs.getString("tipo_consumo"));
                pedido.setEstado(rs.getString("estado"));
                pedidos.add(pedido);
            }
        }
        return pedidos;
    }

    @Override
    public void actualizar(PedidoDTO pedido) throws SQLException {
        String sql = "{call proc_actualizar_pedidos(?, ?, ?, ?, ?)}";
        try (CallableStatement cstmt = ConexionSQL.getInstancia().getConexion().prepareCall(sql)) {
            cstmt.setInt(1, pedido.getIdPedido());
            cstmt.setInt(2, pedido.getIdCliente());
            cstmt.setInt(3, pedido.getIdMesa());
            cstmt.setString(4, pedido.getTipoConsumo());
            cstmt.setString(5, pedido.getEstado());
            cstmt.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws SQLException {
        String sql = "{call proc_eliminar_pedidos(?)}";
        try (CallableStatement cstmt = ConexionSQL.getInstancia().getConexion().prepareCall(sql)) {
            cstmt.setInt(1, id);
            cstmt.executeUpdate();
        }
    }
}
