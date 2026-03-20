package com.example.Mascotas.clientes;

import com.example.Mascotas.direccion.Direccion;
import com.example.Mascotas.mascotas.Mascota;
import jakarta.persistence.*;
import lombok.*;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Entity
@Table(name="cliente")

public class cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCliente;
    @Column(nullable = false, length = 50)
    private String nombre;
    @Column(nullable = false, length = 50)
    private String apPaterno;
    @Column(nullable = false, length = 50)
    private String apMaterno;
    @Column(nullable = false, length = 50)
    private String email;

    @OneToOne(mappedBy = "cliente", cascade = CascadeType.ALL)
    private Direccion direccion;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Mascota> mascotas = new ArrayList<>();

}
