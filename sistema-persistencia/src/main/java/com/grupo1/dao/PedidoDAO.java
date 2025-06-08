/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.grupo1.dao;

import com.grupo1.dto.PedidoDTO;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author emma3tc
 */
public interface PedidoDAO {
    void insertar(PedidoDTO pedido) throws SQLException;
    PedidoDTO buscarPorId(int id) throws SQLException;
    List<PedidoDTO> listar() throws SQLException;
    void actualizar(PedidoDTO pedido) throws SQLException;
    void eliminar(int id) throws SQLException;
}
