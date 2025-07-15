/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1.dao.impl;

import com.grupo1.conexion.ConexionSQL;
import com.grupo1.dao.DetallePedidoDAO;
import com.grupo1.dto.DetallePedidoDTO;
import java.math.BigDecimal;
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
public class DetallePedidoDAOImpl implements DetallePedidoDAO {

    @Override
    public void insertar(DetallePedidoDTO detalle) throws SQLException {
        if (detalle == null) {
            throw new IllegalArgumentException("❌ El detalle de pedido no puede ser null.");
        }
        if (detalle.getIdPedido() <= 0) {
            throw new IllegalArgumentException("❌ ID de pedido inválido.");
        }
        if (detalle.getIdProducto() <= 0) {
            throw new IllegalArgumentException("❌ ID de producto inválido.");
        }
        if (detalle.getCantidad() <= 0) {
            throw new IllegalArgumentException("❌ La cantidad debe ser mayor a cero.");
        }
        if (detalle.getPrecioUnitario() == null) {
            throw new IllegalArgumentException("❌ El precio unitario es obligatorio.");
        }

        String sql = "{call proc_insertar_detalle_pedido(?,?,?,?,?)}";  // El último es OUTPUT

        try (Connection con = ConexionSQL.getInstancia().getConexion(); CallableStatement cstmt = con.prepareCall(sql)) {

            cstmt.setInt(1, detalle.getIdPedido());
            cstmt.setInt(2, detalle.getIdProducto());
            cstmt.setInt(3, detalle.getCantidad());
            cstmt.setBigDecimal(4, detalle.getPrecioUnitario());

            cstmt.registerOutParameter(5, Types.INTEGER);

            int filas = cstmt.executeUpdate();
            if (filas == 0) {
                throw new SQLException("❌ No se insertó el detalle de pedido.");
            }

            int idGenerado = cstmt.getInt(5);
            detalle.setIdDetalle(idGenerado);  // Guarda el ID si lo necesitas después
        }
    }

    @Override
    public DetallePedidoDTO buscarPorId(int id) throws SQLException {
        String sql = "{call proc_obtener_detalle_pedido_por_id(?)}";
        try (Connection con = ConexionSQL.getInstancia().getConexion(); CallableStatement cstmt = con.prepareCall(sql)) {
            cstmt.setInt(1, id);
            try (ResultSet rs = cstmt.executeQuery()) {
                if (rs.next()) {
                    DetallePedidoDTO detalle = new DetallePedidoDTO();
                    detalle.setIdDetalle(rs.getInt("id_detalle"));
                    detalle.setIdPedido(rs.getInt("id_pedido"));
                    detalle.setIdProducto(rs.getInt("id_producto"));
                    detalle.setCantidad(rs.getInt("cantidad"));
                    detalle.setPrecioUnitario(rs.getBigDecimal("precio_unitario"));
                    return detalle;
                }
            }
        }
        return null;
    }

    @Override
    public List<DetallePedidoDTO> listar() throws SQLException {
        List<DetallePedidoDTO> lista = new ArrayList<>();
        String sql = "{call proc_obtener_todos_detalle_pedido}";
        try (Connection con = ConexionSQL.getInstancia().getConexion(); CallableStatement cstmt = con.prepareCall(sql); ResultSet rs = cstmt.executeQuery()) {
            while (rs.next()) {
                DetallePedidoDTO detalle = new DetallePedidoDTO();
                detalle.setIdDetalle(rs.getInt("id_detalle"));
                detalle.setIdPedido(rs.getInt("id_pedido"));
                detalle.setIdProducto(rs.getInt("id_producto"));
                detalle.setCantidad(rs.getInt("cantidad"));
                detalle.setPrecioUnitario(rs.getBigDecimal("precio_unitario"));
                lista.add(detalle);
            }
        }
        return lista;
    }

    @Override
    public void actualizar(DetallePedidoDTO detalle) throws SQLException {
        String sql = "{call proc_actualizar_detalle_pedido(?, ?, ?, ?, ?)}";
        try (Connection con = ConexionSQL.getInstancia().getConexion(); CallableStatement cstmt = con.prepareCall(sql)) {
            cstmt.setInt(1, detalle.getIdDetalle());
            cstmt.setInt(2, detalle.getIdPedido());
            cstmt.setInt(3, detalle.getIdProducto());
            cstmt.setInt(4, detalle.getCantidad());
            cstmt.setBigDecimal(5, detalle.getPrecioUnitario());
            cstmt.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws SQLException {
        String sql = "{call proc_eliminar_detalle_pedido(?)}";
        try (Connection con = ConexionSQL.getInstancia().getConexion(); CallableStatement cstmt = con.prepareCall(sql)) {
            cstmt.setInt(1, id);
            cstmt.executeUpdate();
        }
    }

    @Override
    public int obtenerUltimoIdInsertado() throws SQLException {
        String sql = "SELECT MAX(id_detalle) AS ultimo_id FROM Detalle_Pedido";
        try (Connection con = ConexionSQL.getInstancia().getConexion(); PreparedStatement stmt = con.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("ultimo_id");
            }
        }
        return -1;
    }

    @Override
    public List<DetallePedidoDTO> listarPorPedido(int idPedido) throws SQLException {
        List<DetallePedidoDTO> lista = new ArrayList<>();
        String sql = "{call proc_obtener_detalles_por_pedido(?)}";  // Asegúrate de tener este procedimiento en tu BD
        try (Connection con = ConexionSQL.getInstancia().getConexion(); CallableStatement cstmt = con.prepareCall(sql)) {
            cstmt.setInt(1, idPedido);
            try (ResultSet rs = cstmt.executeQuery()) {
                while (rs.next()) {
                    DetallePedidoDTO detalle = new DetallePedidoDTO();
                    detalle.setIdDetalle(rs.getInt("id_detalle"));
                    detalle.setIdPedido(rs.getInt("id_pedido"));
                    detalle.setIdProducto(rs.getInt("id_producto"));
                    detalle.setCantidad(rs.getInt("cantidad"));
                    detalle.setPrecioUnitario(rs.getBigDecimal("precio_unitario"));
                    lista.add(detalle);
                }
            }
        }
        return lista;
    }

    @Override
    public BigDecimal calcularTotalPorPedido(int idPedido) throws SQLException {
        String sql = "{call proc_calcular_total_pedido(?)}";
        try (Connection con = ConexionSQL.getInstancia().getConexion(); CallableStatement stmt = con.prepareCall(sql)) {
            stmt.setInt(1, idPedido);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("total");
                }
            }
        }
        return BigDecimal.ZERO;
    }

}
