/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tiendaproyecto.tienda.service;

import com.tiendaproyecto.tienda.domain.Pedido;
import com.tiendaproyecto.tienda.domain.Usuario;
import com.tiendaproyecto.tienda.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Transactional
    public Pedido crearPedido(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    public List<Pedido> listarPedidosUsuario(Usuario usuario) {
        return pedidoRepository.findByUsuarioOrderByFechaPedidoDesc(usuario);
    }

    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    @Transactional
    public Pedido actualizarEstado(Long idPedido, Pedido.Estado estado) {
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(idPedido);
        if (pedidoOpt.isPresent()) {
            Pedido pedido = pedidoOpt.get();
            pedido.setEstado(estado);
            return pedidoRepository.save(pedido);
        }
        return null;
    }
}