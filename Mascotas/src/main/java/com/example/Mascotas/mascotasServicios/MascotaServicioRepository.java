package com.example.Mascotas.mascotasServicios;

import com.example.Mascotas.mascotas.Mascota;
import com.example.Mascotas.servicios.Servicio;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface MascotaServicioRepository extends CrudRepository<MascotaServicio, Long> {

    // Buscar todas las asignaciones de una mascota
    List<MascotaServicio> findByMascota(Mascota mascota);

    // Buscar todas las asignaciones de un servicio
    List<MascotaServicio> findByServicio(Servicio servicio);

    // Buscar por mascota ID (alternativa más directa)
    List<MascotaServicio> findByMascotaIdMascota(Long mascotaId);

    // Buscar por servicio ID
    List<MascotaServicio> findByServicioIdServicio(Long servicioId);
}