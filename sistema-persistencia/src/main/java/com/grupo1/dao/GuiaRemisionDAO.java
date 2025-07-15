/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.grupo1.dao;

import com.grupo1.dto.GuiaRemisionDTO;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author emma3tc
 */
public interface GuiaRemisionDAO {

    void insertar(GuiaRemisionDTO guia) throws SQLException;

    GuiaRemisionDTO buscarPorId(int id) throws SQLException;

    List<GuiaRemisionDTO> listar() throws SQLException;

    void actualizar(GuiaRemisionDTO guia) throws SQLException;

    void eliminar(int id) throws SQLException;

    int obtenerUltimoIdInsertado() throws SQLException;
}
