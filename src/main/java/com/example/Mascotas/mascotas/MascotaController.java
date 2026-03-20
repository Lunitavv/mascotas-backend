package com.example.Mascotas.mascotas;

import com.example.Mascotas.clientes.ClienteRepository;
import com.example.Mascotas.clientes.cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/mascota")
public class MascotaController {

    @Autowired
    private MascotaRepository mascotaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    // GET todas las mascotas: /mascota
    @GetMapping
    public ResponseEntity<Iterable<Mascota>> findAll() {
        return ResponseEntity.ok(mascotaRepository.findAll());
    }

    // GET mascota por ID: /mascota/5
    @GetMapping("/{id}")
    public ResponseEntity<Mascota> findById(@PathVariable Long id) {
        Optional<Mascota> mascota = mascotaRepository.findById(id);

        return mascota.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST crear mascota: /mascota
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Mascota mascota, UriComponentsBuilder uriComponentsBuilder) {

        // Validar que viene el cliente
        if (mascota.getCliente() == null || mascota.getCliente().getIdCliente() == null) {
            return ResponseEntity.badRequest().body("Debe especificar un cliente");
        }

        // Buscar el cliente
        Optional<cliente> clienteOpcional = clienteRepository.findById(mascota.getCliente().getIdCliente());

        if (!clienteOpcional.isPresent()) {
            return ResponseEntity
                    .unprocessableEntity()
                    .body("El cliente con ID " + mascota.getCliente().getIdCliente() + " no existe");
        }

        // Asignar el cliente existente
        mascota.setCliente(clienteOpcional.get());

        // Guardar la mascota
        Mascota mascotaGuardada = mascotaRepository.save(mascota);

        // Construir URI de la nueva mascota
        URI location = uriComponentsBuilder
                .path("/mascota/{id}")
                .buildAndExpand(mascotaGuardada.getIdMascota())
                .toUri();

        return ResponseEntity.created(location).body(mascotaGuardada);
    }

    // DELETE eliminar mascota: /mascota/5
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (!mascotaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        mascotaRepository.deleteById(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    // PUT actualizar mascota completa: /mascota/5
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Mascota mascota) {
        if (!mascotaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        // Asegurar que el ID de la mascota sea el correcto
        mascota.setIdMascota(id);

        // Validar cliente si viene
        if (mascota.getCliente() != null && mascota.getCliente().getIdCliente() != null) {
            Optional<cliente> clienteOpcional = clienteRepository.findById(mascota.getCliente().getIdCliente());
            if (!clienteOpcional.isPresent()) {
                return ResponseEntity.unprocessableEntity().body("El cliente no existe");
            }
            mascota.setCliente(clienteOpcional.get());
        }

        Mascota mascotaActualizada = mascotaRepository.save(mascota);
        return ResponseEntity.ok(mascotaActualizada);
    }
}