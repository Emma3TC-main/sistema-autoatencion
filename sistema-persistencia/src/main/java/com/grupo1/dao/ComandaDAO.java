/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.grupo1.dao;

import com.grupo1.dto.ComandaDTO;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author emma3tc
 */
public interface ComandaDAO {

    void insertar(ComandaDTO comanda) throws SQLException;

    ComandaDTO buscarPorId(int id) throws SQLException;

    List<ComandaDTO> listar() throws SQLException;

    void actualizar(ComandaDTO comanda) throws SQLException;

    void eliminar(int id) throws SQLException;
}
