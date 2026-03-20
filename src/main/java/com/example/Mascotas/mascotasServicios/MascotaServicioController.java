package com.example.Mascotas.mascotasServicios;

import com.example.Mascotas.mascotas.Mascota;
import com.example.Mascotas.mascotas.MascotaRepository;
import com.example.Mascotas.servicios.Servicio;
import com.example.Mascotas.servicios.ServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/mascota-servicio")
public class MascotaServicioController {

    @Autowired
    private MascotaServicioRepository mascotaServicioRepository;

    @Autowired
    private MascotaRepository mascotaRepository;

    @Autowired
    private ServicioRepository servicioRepository;

    // GET todas las asignaciones
    @GetMapping
    public ResponseEntity<Iterable<MascotaServicio>> findAll() {
        return ResponseEntity.ok(mascotaServicioRepository.findAll());
    }

    // GET asignación por ID
    @GetMapping("/{id}")
    public ResponseEntity<MascotaServicio> findById(@PathVariable Long id) {
        Optional<MascotaServicio> mascotaServicio = mascotaServicioRepository.findById(id);
        if (mascotaServicio.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mascotaServicio.get());
    }

    // POST crear nueva asignación
    @PostMapping
    public ResponseEntity<?> create(@RequestBody MascotaServicio mascotaServicio, UriComponentsBuilder uriBuilder) {

        // Validar que la mascota existe
        if (mascotaServicio.getMascota() == null || mascotaServicio.getMascota().getIdMascota() == null) {
            return ResponseEntity.badRequest().body("Debe especificar una mascota");
        }

        Optional<Mascota> mascotaOpcional = mascotaRepository.findById(mascotaServicio.getMascota().getIdMascota());
        if (mascotaOpcional.isEmpty()) {
            return ResponseEntity
                    .unprocessableEntity()
                    .body("La mascota con ID " + mascotaServicio.getMascota().getIdMascota() + " no existe");
        }

        // Validar que el servicio existe
        if (mascotaServicio.getServicio() == null || mascotaServicio.getServicio().getIdServicio() == null) {
            return ResponseEntity.badRequest().body("Debe especificar un servicio");
        }

        Optional<Servicio> servicioOpcional = servicioRepository.findById(mascotaServicio.getServicio().getIdServicio());
        if (servicioOpcional.isEmpty()) {
            return ResponseEntity
                    .unprocessableEntity()
                    .body("El servicio con ID " + mascotaServicio.getServicio().getIdServicio() + " no existe");
        }

        // Validar campos obligatorios
        if (mascotaServicio.getFecha() == null) {
            return ResponseEntity.badRequest().body("La fecha es obligatoria");
        }

        // Asignar las entidades completas
        mascotaServicio.setMascota(mascotaOpcional.get());
        mascotaServicio.setServicio(servicioOpcional.get());

        // Guardar
        MascotaServicio guardado = mascotaServicioRepository.save(mascotaServicio);

        // Construir URI de la nueva asignación
        URI url = uriBuilder.path("/mascota-servicio/{id}")
                .buildAndExpand(guardado.getIdMascotaServicio())
                .toUri();

        return ResponseEntity.created(url).body(guardado);
    }

    // PUT actualizar asignación existente (AGREGADO)
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody MascotaServicio mascotaServicioActualizado) {

        // 1. Verificar si la asignación existe
        Optional<MascotaServicio> asignacionExistente = mascotaServicioRepository.findById(id);

        if (asignacionExistente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        MascotaServicio asignacion = asignacionExistente.get();

        // 2. Actualizar FECHA (si viene en la petición)
        if (mascotaServicioActualizado.getFecha() != null) {
            asignacion.setFecha(mascotaServicioActualizado.getFecha());
        }

        // 3. Actualizar NOTA (si viene en la petición)
        if (mascotaServicioActualizado.getNota() != null) {
            asignacion.setNota(mascotaServicioActualizado.getNota());
        }

        // 4. Validar y actualizar la MASCOTA (si viene en la petición)
        if (mascotaServicioActualizado.getMascota() != null &&
                mascotaServicioActualizado.getMascota().getIdMascota() != null) {

            Optional<Mascota> mascotaOpcional = mascotaRepository.findById(
                    mascotaServicioActualizado.getMascota().getIdMascota()
            );

            if (mascotaOpcional.isEmpty()) {
                return ResponseEntity
                        .unprocessableEntity()
                        .body("La mascota con ID " + mascotaServicioActualizado.getMascota().getIdMascota() + " no existe");
            }

            asignacion.setMascota(mascotaOpcional.get());
        }

        // 5. Validar y actualizar el SERVICIO (si viene en la petición)
        if (mascotaServicioActualizado.getServicio() != null &&
                mascotaServicioActualizado.getServicio().getIdServicio() != null) {

            Optional<Servicio> servicioOpcional = servicioRepository.findById(
                    mascotaServicioActualizado.getServicio().getIdServicio()
            );

            if (servicioOpcional.isEmpty()) {
                return ResponseEntity
                        .unprocessableEntity()
                        .body("El servicio con ID " + mascotaServicioActualizado.getServicio().getIdServicio() + " no existe");
            }

            asignacion.setServicio(servicioOpcional.get());
        }

        // 6. Guardar los cambios
        try {
            MascotaServicio asignacionActualizada = mascotaServicioRepository.save(asignacion);
            return ResponseEntity.ok(asignacionActualizada);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .internalServerError()
                    .body("Error al actualizar: " + e.getMessage());
        }
    }

    // DELETE eliminar asignación
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<MascotaServicio> mascotaServicio = mascotaServicioRepository.findById(id);
        if (mascotaServicio.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        mascotaServicioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // GET asignaciones por mascota (OPCIONAL - útil)
    @GetMapping("/mascota/{mascotaId}")
    public ResponseEntity<Iterable<MascotaServicio>> findByMascota(@PathVariable Long mascotaId) {
        Optional<Mascota> mascota = mascotaRepository.findById(mascotaId);
        if (mascota.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // Asumiendo que tienes este método en el repository
        // Si no, crea el método en MascotaServicioRepository
        return ResponseEntity.ok(mascotaServicioRepository.findByMascota(mascota.get()));
    }

    // GET asignaciones por servicio (OPCIONAL - útil)
    @GetMapping("/servicio/{servicioId}")
    public ResponseEntity<Iterable<MascotaServicio>> findByServicio(@PathVariable Long servicioId) {
        Optional<Servicio> servicio = servicioRepository.findById(servicioId);
        if (servicio.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // Asumiendo que tienes este método en el repository
        return ResponseEntity.ok(mascotaServicioRepository.findByServicio(servicio.get()));
    }
}