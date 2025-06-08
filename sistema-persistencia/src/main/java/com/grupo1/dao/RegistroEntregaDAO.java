/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.grupo1.dao;

import com.grupo1.dto.RegistroEntregaDTO;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author emma3tc
 */
public interface RegistroEntregaDAO {

    void insertar(RegistroEntregaDTO registro) throws SQLException;

    RegistroEntregaDTO buscarPorId(int id) throws SQLException;

    List<RegistroEntregaDTO> listar() throws SQLException;

    void actualizar(RegistroEntregaDTO registro) throws SQLException;

    void eliminar(int id) throws SQLException;
}
