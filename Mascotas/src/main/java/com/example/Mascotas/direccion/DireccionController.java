package com.example.Mascotas.direccion;



import com.example.Mascotas.clientes.cliente;
import com.example.Mascotas.clientes.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/direccion")
public class DireccionController {

    @Autowired
    private DireccionRepository direccionRepository;
    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping
    public ResponseEntity<Iterable<Direccion>> findAll() {
        return ResponseEntity.ok(direccionRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Direccion> findById(@PathVariable Long id) {
        Optional<Direccion> direccion = direccionRepository.findById(id);
        if (direccion.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(direccion.get());
    }

    @PostMapping
    public ResponseEntity<Direccion> create(@RequestBody Direccion direccion, UriComponentsBuilder uriBuilder) {
        Optional<cliente> clienteOpcional = clienteRepository.findById(direccion.getCliente().getIdCliente());
        if (clienteOpcional.isEmpty()) {
            return ResponseEntity.unprocessableEntity().build();
        }
        direccion.setCliente(clienteOpcional.get());
        Direccion direccionGuardada = direccionRepository.save(direccion);
        URI url = uriBuilder.path("/direccion/{id}")
                .buildAndExpand(direccionGuardada.getIdDireccion()).toUri();
        return ResponseEntity.created(url).body(direccionGuardada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Direccion> direccion = direccionRepository.findById(id);
        if (direccion.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        direccionRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}