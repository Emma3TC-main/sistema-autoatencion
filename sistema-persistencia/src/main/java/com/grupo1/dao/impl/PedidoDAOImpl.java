/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1.dao.impl;

import com.grupo1.conexion.ConexionSQL;
import com.grupo1.dao.PedidoDAO;
import com.grupo1.dto.PedidoDTO;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author emma3tc
 */
public class PedidoDAOImpl implements PedidoDAO {

    @Override
    public void insertar(PedidoDTO pedido) throws SQLException {
        if (pedido == null) {
            throw new IllegalArgumentException("❌ El pedido no puede ser null.");
        }
        if (pedido.getIdCliente() <= 0) {
            throw new IllegalArgumentException("❌ ID del cliente inválido.");
        }
        if (pedido.getTipoConsumo() == null || pedido.getTipoConsumo().isBlank()) {
            throw new IllegalArgumentException("❌ Tipo de consumo es obligatorio.");
        }
        if (pedido.getEstado() == null || pedido.getEstado().isBlank()) {
            throw new IllegalArgumentException("❌ Estado del pedido es obligatorio.");
        }

        String sql = "{call proc_insertar_pedidos(?,?,?,?,?)}"; // Último parámetro es OUTPUT

        try (Connection con = ConexionSQL.getInstancia().getConexion(); CallableStatement cstmt = con.prepareCall(sql)) {

            cstmt.setInt(1, pedido.getIdCliente());

            // Aquí está el cambio importante 👇
            if (pedido.getIdMesa() != null) {
                cstmt.setInt(2, pedido.getIdMesa());
            } else {
                cstmt.setNull(2, Types.INTEGER);
            }

            cstmt.setString(3, pedido.getTipoConsumo());
            cstmt.setString(4, pedido.getEstado());

            // Param OUTPUT
            cstmt.registerOutParameter(5, Types.INTEGER);

            int filas = cstmt.executeUpdate();
            if (filas == 0) {
                throw new SQLException("❌ No se insertó el pedido.");
            }

            // Obtener ID generado
            int idGenerado = cstmt.getInt(5);
            pedido.setIdPedido(idGenerado);
        }
    }

    @Override
    public PedidoDTO buscarPorId(int id) throws SQLException {
        String sql = "{call proc_obtener_pedidos_por_id(?)}";
        try (Connection con = ConexionSQL.getInstancia().getConexion(); CallableStatement cstmt = con.prepareCall(sql)) {
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
        try (Connection con = ConexionSQL.getInstancia().getConexion(); CallableStatement cstmt = con.prepareCall(sql); ResultSet rs = cstmt.executeQuery()) {
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
        try (Connection con = ConexionSQL.getInstancia().getConexion(); CallableStatement cstmt = con.prepareCall(sql)) {
            cstmt.setInt(1, pedido.getIdPedido());
            cstmt.setInt(2, pedido.getIdCliente());
            cstmt.setInt(3, pedido.getIdMesa());
            cstmt.setString(4, pedido.getTipoConsumo());
            cstmt.setString(5, pedido.getEstado());
            cstmt.executeUpdate();
        }
    }

    @Override
    public void actualizarEstado(int idPedido, String nuevoEstado) throws SQLException {
        String sql = "{call proc_actualizar_estado_pedido(?, ?)}";
        try (Connection con = ConexionSQL.getInstancia().getConexion(); CallableStatement cstmt = con.prepareCall(sql)) {
            cstmt.setInt(1, idPedido);
            cstmt.setString(2, nuevoEstado);
            cstmt.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws SQLException {
        String sql = "{call proc_eliminar_pedidos(?)}";
        try (Connection con = ConexionSQL.getInstancia().getConexion(); CallableStatement cstmt = con.prepareCall(sql)) {
            cstmt.setInt(1, id);
            cstmt.executeUpdate();
        }
    }

    @Override
    public int obtenerUltimoIdInsertado() throws SQLException {
        String sql = "SELECT MAX(id_pedido) AS ultimo_id FROM Pedidos";
        try (Connection con = ConexionSQL.getInstancia().getConexion(); PreparedStatement stmt = con.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("ultimo_id");
            }
        }
        return -1;
    }

}
