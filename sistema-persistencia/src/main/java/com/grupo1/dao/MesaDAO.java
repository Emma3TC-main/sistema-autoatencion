/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.grupo1.dao;

import com.grupo1.dto.MesaDTO;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author emma3tc
 */
public interface MesaDAO {

    void insertar(MesaDTO mesa) throws SQLException;

    MesaDTO buscarPorId(int id) throws SQLException;

    List<MesaDTO> listar() throws SQLException;

    void actualizar(MesaDTO mesa) throws SQLException;

    void eliminar(int id) throws SQLException;

    MesaDTO buscarPorNumero(int numero) throws SQLException;

}
