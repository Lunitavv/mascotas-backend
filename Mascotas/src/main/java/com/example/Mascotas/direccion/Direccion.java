package com.example.Mascotas.direccion;

import com.example.Mascotas.clientes.cliente;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Entity
@Table(name="direccion")

public class Direccion  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idDireccion;
    @Column(nullable = false, length = 100)
    private String calle;
    @Column(nullable = false, length = 20)
    private String numero;

    @OneToOne
    @JoinColumn(name = "idCliente")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private cliente cliente;
}
