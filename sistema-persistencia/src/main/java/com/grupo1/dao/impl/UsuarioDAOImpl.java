/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1.dao.impl;

import com.grupo1.conexion.ConexionSQL;
import com.grupo1.dao.UsuarioDAO;
import com.grupo1.dto.UsuarioDTO;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author emma3tc
 */
public class UsuarioDAOImpl implements UsuarioDAO {

    @Override
    public void insertar(UsuarioDTO usuario) throws SQLException {
     
        String sql = "{call proc_insertar_usuarios(?,?,?,?)}";
        try(CallableStatement cstmt = ConexionSQL.getInstancia().getConexion().prepareCall(sql)){
            cstmt.setString(1, usuario.getNombre());
            cstmt.setString(2, usuario.getRol());
            cstmt.setString(3, usuario.getCorreo());
            cstmt.setString(4, usuario.getPasswordHash());
            cstmt.executeUpdate();
        }
        
    }

    @Override
    public UsuarioDTO buscarPorId(int id) throws SQLException {
                String sql = "{call proc_obtener_usuarios_por_id(?)}";
        try (CallableStatement cstmt = ConexionSQL.getInstancia().getConexion().prepareCall(sql)) {
            cstmt.setInt(1, id);
            try (ResultSet rs = cstmt.executeQuery()) {
                if (rs.next()) {
                    UsuarioDTO usuario = new UsuarioDTO();
                    usuario.setIdUsuario(rs.getInt("id_usuario"));
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setRol(rs.getString("rol"));
                    usuario.setCorreo(rs.getString("correo"));
                    usuario.setPasswordHash(rs.getString("password_hash"));
                    return usuario;
                }
            }
        }
        return null;
    }

    @Override
    public List<UsuarioDTO> listar() throws SQLException {
        List<UsuarioDTO> usuarios = new ArrayList<>();
        String sql = "{call proc_obtener_todos_usuarios}";
        try (CallableStatement cstmt = ConexionSQL.getInstancia().getConexion().prepareCall(sql);
             ResultSet rs = cstmt.executeQuery()) {
            while (rs.next()) {
                UsuarioDTO usuario = new UsuarioDTO();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setRol(rs.getString("rol"));
                usuario.setCorreo(rs.getString("correo"));
                usuario.setPasswordHash(rs.getString("password_hash"));
                usuarios.add(usuario);
            }
        }
        return usuarios;
    }

    @Override
    public void actualizar(UsuarioDTO usuario) throws SQLException {
            String sql = "{call proc_actualizar_usuarios(?, ?, ?, ?, ?)}";
            try (CallableStatement cstmt = ConexionSQL.getInstancia().getConexion().prepareCall(sql)){
                cstmt.setInt(1, usuario.getIdUsuario());
                cstmt.setString(2, usuario.getNombre());
                cstmt.setString(3, usuario.getRol());
                cstmt.setString(4, usuario.getCorreo());
                cstmt.setString(5, usuario.getPasswordHash());
                cstmt.executeUpdate();
            }        
    }

    @Override
    public void eliminar(int id) throws SQLException {
        String sql = "{call proc_eliminar_usuarios(?)}";
        try (CallableStatement cstmt = ConexionSQL.getInstancia().getConexion().prepareCall(sql)) {
            cstmt.setInt(1, id);
            cstmt.executeUpdate();
        }
    }
    
     @Override
    public UsuarioDTO login(String correo, String passwordHash) throws SQLException {
        UsuarioDTO usuario = null;

        String sql = "{ call SP_LOGIN_USER(?, ?) }";

        try (Connection conn = ConexionSQL.getInstancia().getConexion();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, correo);
            stmt.setString(2, passwordHash);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    usuario = new UsuarioDTO();
                    usuario.setIdUsuario(rs.getInt("id_usuario"));
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setRol(rs.getString("rol"));
                    usuario.setCorreo(rs.getString("correo"));
                    // no devolvemos el passwordHash por seguridad
                }
            }
        }

        return usuario;
    }
    
}
