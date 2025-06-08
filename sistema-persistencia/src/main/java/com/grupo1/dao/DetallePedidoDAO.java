/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.grupo1.dao;

import com.grupo1.dto.DetallePedidoDTO;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author emma3tc
 */
public interface DetallePedidoDAO {
    void insertar(DetallePedidoDTO detalle) throws SQLException;
    DetallePedidoDTO buscarPorId(int id) throws SQLException;
    List<DetallePedidoDTO> listar() throws SQLException;
    void actualizar(DetallePedidoDTO detalle) throws SQLException;
    void eliminar(int id) throws SQLException;
}
