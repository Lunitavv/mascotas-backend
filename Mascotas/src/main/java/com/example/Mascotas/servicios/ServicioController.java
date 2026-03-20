package com.example.Mascotas.servicios;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/servicio")
public class ServicioController {

    @Autowired
    private ServicioRepository servicioRepository;

    @GetMapping
    public ResponseEntity<Iterable<Servicio>> findAll() {
        return ResponseEntity.ok(servicioRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Servicio> findById(@PathVariable Long id) {
        Optional<Servicio> servicio = servicioRepository.findById(id);
        if (servicio.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(servicio.get());
    }

    @PostMapping
    public ResponseEntity<Servicio> create(@RequestBody Servicio servicio, UriComponentsBuilder uriBuilder) {
        Servicio servicioGuardado = servicioRepository.save(servicio);
        URI url = uriBuilder.path("/servicio/{id}")
                .buildAndExpand(servicioGuardado.getIdServicio()).toUri();
        return ResponseEntity.created(url).body(servicioGuardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Servicio> update(@PathVariable Long id, @RequestBody Servicio servicio) {
        Optional<Servicio> servicioOpcional = servicioRepository.findById(id);
        if (servicioOpcional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        servicio.setIdServicio(id);
        return ResponseEntity.ok(servicioRepository.save(servicio));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Servicio> servicioOpcional = servicioRepository.findById(id);
        if (servicioOpcional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        servicioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}