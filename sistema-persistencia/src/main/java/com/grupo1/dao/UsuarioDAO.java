/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.grupo1.dao;

import com.grupo1.dto.UsuarioDTO;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author emma3tc
 */
public interface UsuarioDAO {
    
    void insertar(UsuarioDTO usuario) throws SQLException;
    UsuarioDTO buscarPorId(int id) throws SQLException;
    List<UsuarioDTO> listar() throws SQLException;
    void actualizar(UsuarioDTO usuario) throws SQLException;
    void eliminar(int id) throws SQLException;
    UsuarioDTO login(String correo, String passwordHash) throws SQLException;
    
}
