/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.grupo1.dao;

import com.grupo1.dto.BoletaFacturaDTO;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author emma3tc
 */
public interface BoletaFacturaDAO {

    void insertar(BoletaFacturaDTO boleta) throws SQLException;

    BoletaFacturaDTO buscarPorId(int id) throws SQLException;

    List<BoletaFacturaDTO> listar() throws SQLException;

    void actualizar(BoletaFacturaDTO boleta) throws SQLException;

    void eliminar(int id) throws SQLException;
}
