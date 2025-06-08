/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.grupo1.dao;

import com.grupo1.dto.ProductoDTO;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author emma3tc
 */
public interface ProductoDAO {
    void insertar(ProductoDTO producto) throws SQLException;
    ProductoDTO buscarPorId(int id) throws SQLException;
    List<ProductoDTO> listar() throws SQLException;
    void actualizar(ProductoDTO producto) throws SQLException;
    void eliminar(int id) throws SQLException;
}
