/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1.dao.impl;

import com.grupo1.conexion.ConexionSQL;
import com.grupo1.dao.DetallePedidoDAO;
import com.grupo1.dto.DetallePedidoDTO;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author emma3tc
 */
public class DetallePedidoDAOImpl implements DetallePedidoDAO {

    @Override
    public void insertar(DetallePedidoDTO detalle) throws SQLException {
        String sql = "{call proc_insertar_detalle_pedido(?, ?, ?, ?)}";
        try (CallableStatement cstmt = ConexionSQL.getInstancia().getConexion().prepareCall(sql)) {
            cstmt.setInt(1, detalle.getIdPedido());
            cstmt.setInt(2, detalle.getIdProducto());
            cstmt.setInt(3, detalle.getCantidad());
            cstmt.setBigDecimal(4, detalle.getPrecioUnitario());
            cstmt.executeUpdate();
        }
    }

    @Override
    public DetallePedidoDTO buscarPorId(int id) throws SQLException {
        String sql = "{call proc_obtener_detalle_pedido_por_id(?)}";
        try (CallableStatement cstmt = ConexionSQL.getInstancia().getConexion().prepareCall(sql)) {
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
        try (CallableStatement cstmt = ConexionSQL.getInstancia().getConexion().prepareCall(sql); ResultSet rs = cstmt.executeQuery()) {
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
        try (CallableStatement cstmt = ConexionSQL.getInstancia().getConexion().prepareCall(sql)) {
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
        try (CallableStatement cstmt = ConexionSQL.getInstancia().getConexion().prepareCall(sql)) {
            cstmt.setInt(1, id);
            cstmt.executeUpdate();
        }
    }
}
