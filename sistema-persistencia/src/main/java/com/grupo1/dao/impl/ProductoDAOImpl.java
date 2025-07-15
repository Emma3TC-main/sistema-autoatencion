/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1.dao.impl;

import com.grupo1.conexion.ConexionSQL;
import com.grupo1.dao.ProductoDAO;
import com.grupo1.dto.ProductoDTO;
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
public class ProductoDAOImpl implements ProductoDAO {

    @Override
    public void insertar(ProductoDTO producto) throws SQLException {
        if (producto == null) {
            throw new IllegalArgumentException("❌ Producto no puede ser null.");
        }
        if (producto.getNombre() == null || producto.getNombre().isBlank()) {
            throw new IllegalArgumentException("❌ El nombre del producto es obligatorio.");
        }
        if (producto.getPrecioUnitario() == null) {
            throw new IllegalArgumentException("❌ El precio unitario es obligatorio.");
        }
        if (producto.getStock() < 0) {
            throw new IllegalArgumentException("❌ El stock no puede ser negativo.");
        }

        String sql = "{call proc_insertar_productos(?,?,?,?,?)}";  // 5 parámetros: último es OUTPUT

        try (Connection con = ConexionSQL.getInstancia().getConexion(); CallableStatement cstmt = con.prepareCall(sql)) {

            cstmt.setString(1, producto.getNombre());
            cstmt.setBigDecimal(2, producto.getPrecioUnitario());
            cstmt.setInt(3, producto.getStock());
            cstmt.setBoolean(4, producto.isDisponible());

            // Param OUTPUT
            cstmt.registerOutParameter(5, Types.INTEGER);

            int filas = cstmt.executeUpdate();
            if (filas == 0) {
                throw new SQLException("❌ No se insertó el producto.");
            }

            // Obtener ID generado
            int idGenerado = cstmt.getInt(5);
            producto.setIdProducto(idGenerado);
        }
    }

    @Override
    public ProductoDTO buscarPorId(int id) throws SQLException {
        String sql = "{call proc_obtener_productos_por_id(?)}";
        try (Connection con = ConexionSQL.getInstancia().getConexion(); CallableStatement cstmt = con.prepareCall(sql)) {

            cstmt.setInt(1, id);
            try (ResultSet rs = cstmt.executeQuery()) {
                if (rs.next()) {
                    ProductoDTO producto = new ProductoDTO();
                    producto.setIdProducto(rs.getInt("id_producto"));
                    producto.setNombre(rs.getString("nombre"));
                    producto.setPrecioUnitario(rs.getBigDecimal("precio_unitario"));
                    producto.setStock(rs.getInt("stock"));
                    producto.setDisponible(rs.getBoolean("disponible"));
                    return producto;
                }
            }
        }
        return null;
    }

    @Override
    public List<ProductoDTO> listar() throws SQLException {
        List<ProductoDTO> productos = new ArrayList<>();
        String sql = "{call proc_obtener_todos_productos}";
        try (Connection con = ConexionSQL.getInstancia().getConexion(); CallableStatement cstmt = con.prepareCall(sql); ResultSet rs = cstmt.executeQuery()) {

            while (rs.next()) {
                ProductoDTO producto = new ProductoDTO();
                producto.setIdProducto(rs.getInt("id_producto"));
                producto.setNombre(rs.getString("nombre"));
                producto.setPrecioUnitario(rs.getBigDecimal("precio_unitario"));
                producto.setStock(rs.getInt("stock"));
                producto.setDisponible(rs.getBoolean("disponible"));
                productos.add(producto);
            }
        }
        return productos;
    }

    @Override
    public void actualizar(ProductoDTO producto) throws SQLException {
        String sql = "{call proc_actualizar_productos(?, ?, ?, ?, ?)}";
        try (Connection con = ConexionSQL.getInstancia().getConexion(); CallableStatement cstmt = con.prepareCall(sql)) {

            cstmt.setInt(1, producto.getIdProducto());
            cstmt.setString(2, producto.getNombre());
            cstmt.setBigDecimal(3, producto.getPrecioUnitario());
            cstmt.setInt(4, producto.getStock());
            cstmt.setBoolean(5, producto.isDisponible());
            cstmt.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws SQLException {
        String sql = "{call proc_eliminar_productos(?)}";
        try (Connection con = ConexionSQL.getInstancia().getConexion(); CallableStatement cstmt = con.prepareCall(sql)) {

            cstmt.setInt(1, id);
            cstmt.executeUpdate();
        }
    }
}
