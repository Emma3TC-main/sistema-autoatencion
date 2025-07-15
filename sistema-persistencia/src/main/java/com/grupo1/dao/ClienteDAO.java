/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.grupo1.dao;

/**
 *
 * @author emma3tc
 */

import com.grupo1.dto.ClienteDTO;
import java.sql.SQLException;
import java.util.List;

public interface ClienteDAO {
    void insertar(ClienteDTO cliente) throws SQLException;
    ClienteDTO buscarPorId(int id) throws SQLException;
    List<ClienteDTO> Listar() throws SQLException;
    void actualizar(ClienteDTO cliente) throws SQLException;
    void eliminar(int id) throws SQLException;
    int obtenerUltimoIdInsertado() throws SQLException;

}
