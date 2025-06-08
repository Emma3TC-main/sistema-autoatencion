/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1.dao.impl;

import com.grupo1.dao.ClienteDAO;
import com.grupo1.dto.ClienteDTO;
import com.grupo1.conexion.ConexionSQL;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author emma3tc
 */
public class ClienteDAOImpl implements ClienteDAO {

    @Override
    public void insertar(ClienteDTO cliente) throws SQLException {
        String sql = "{call proc_insertar_clientes(?,?,?,?)}";
        try(CallableStatement cstmt = ConexionSQL.getInstancia().getConexion().prepareCall(sql)){
            cstmt.setString(1, cliente.getNombre());
            cstmt.setString(2, cliente.getDNI());
            cstmt.setString(3, cliente.getRUC());
            cstmt.setString(4, cliente.getRazonSocial());
            cstmt.executeUpdate();
        }
    }

    @Override
    public ClienteDTO buscarPorId(int id) throws SQLException {
        String sql = "{call proc_obtener_clientes_por_id(?)}";
        try (CallableStatement cstmt = ConexionSQL.getInstancia().getConexion().prepareCall(sql)) {
            cstmt.setInt(1, id);
            try (ResultSet rs = cstmt.executeQuery()) {
                if (rs.next()) {
                    ClienteDTO cliente = new ClienteDTO();
                    cliente.setIdCliente(rs.getInt("id_cliente"));
                    cliente.setNombre(rs.getString("nombre"));
                    cliente.setDNI(rs.getString("dni"));
                    cliente.setRUC(rs.getString("ruc"));
                    cliente.setRazonSocial(rs.getString("razon_social"));
                    return cliente;
                }
            }
        }
        return null;
    }

    @Override
    public List<ClienteDTO> Listar() throws SQLException {
        List<ClienteDTO> clientes = new ArrayList<>();
        String sql = "{call proc_obtener_todos_clientes()}";
        try (CallableStatement cstmt = ConexionSQL.getInstancia().getConexion().prepareCall(sql);
             ResultSet rs = cstmt.executeQuery()) {
            while (rs.next()) {
                ClienteDTO cliente = new ClienteDTO();
                cliente.setIdCliente(rs.getInt("id_cliente"));
                cliente.setNombre(rs.getString("nombre"));
                cliente.setDNI(rs.getString("dni"));
                cliente.setRUC(rs.getString("ruc"));
                cliente.setRazonSocial(rs.getString("razon_social"));
                clientes.add(cliente);
            }
        }
        return clientes;
    }

    @Override
    public void actualizar(ClienteDTO cliente) throws SQLException {
        String sql = "{call proc_actualizar_clientes(?, ?, ?, ?, ?)}";
        try (CallableStatement cstmt = ConexionSQL.getInstancia().getConexion().prepareCall(sql)) {
            cstmt.setInt(1, cliente.getIdCliente());
            cstmt.setString(2, cliente.getNombre());
            cstmt.setString(3, cliente.getDNI());
            cstmt.setString(4, cliente.getRUC());
            cstmt.setString(5, cliente.getRazonSocial());
            cstmt.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws SQLException {
        String sql = "{call proc_eliminar_clientes(?)}";
        try (CallableStatement cstmt = ConexionSQL.getInstancia().getConexion().prepareCall(sql)) {
            cstmt.setInt(1, id);
            cstmt.executeUpdate();
        }
    }
    
}
