package com.example.Mascotas.clientes;

import com.example.Mascotas.mascotas.MascotaRepository;
import com.fasterxml.jackson.core.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/cliente")
public class ClienteController {
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private MascotaRepository mascotaRepository;

    /// metodo para obtener todos los clientes
    @GetMapping
    public ResponseEntity<Iterable<cliente>> findAll(){
        return ResponseEntity.ok(this.clienteRepository.findAll());
    }

    /// metodo para crear cliente
    @PostMapping
    public ResponseEntity<cliente> create(@RequestBody cliente cliente, UriComponentsBuilder uriComponentsBuilder){

        if(cliente.getDireccion() !=null){
            cliente.getDireccion().setCliente(cliente);
        }

        if(cliente.getMascotas() != null && !cliente.getMascotas().isEmpty()){
            cliente.getMascotas().forEach(mascota -> mascota.setCliente(cliente));
        }

        cliente create = clienteRepository.save(cliente);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{idCliente}")
                .buildAndExpand(create.getIdCliente())
                .toUri();

        return ResponseEntity.created(uri).body(create);
    }

    /// buscar cliente por id
    @GetMapping("/{idcliente}")
    public ResponseEntity<cliente> findId(@PathVariable Long idcliente){
        Optional<cliente> clienteOptional = clienteRepository.findById(idcliente);

        if(clienteOptional.isPresent()){
            return ResponseEntity.ok(clienteOptional.get());
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    /// actualizar cliente
    @PutMapping("/{idcliente}")
    public ResponseEntity<cliente> update(
            @PathVariable Long idcliente,
            @RequestBody cliente clienteActualizado
    ){
        Optional<cliente> clienteOptional = clienteRepository.findById(idcliente);

        if(!clienteOptional.isPresent()){
            return ResponseEntity.notFound().build();
        }
        cliente clienteExistente = clienteOptional.get();

        /// actualizar los datos del cliente
        clienteExistente.setNombre(clienteActualizado.getNombre());
        clienteExistente.setApMaterno(clienteActualizado.getApMaterno());
        clienteExistente.setApMaterno(clienteActualizado.getApMaterno());
        clienteExistente.setEmail(clienteActualizado.getEmail());

        if(clienteActualizado.getDireccion() != null){
            clienteActualizado.getDireccion().setCliente(clienteExistente);
            clienteExistente.setDireccion(clienteActualizado.getDireccion());
        }

        cliente clienteUpdate = clienteRepository.save(clienteExistente);
        return ResponseEntity.ok(clienteUpdate);
    }

    /// metodo para eliminarClientes
    @DeleteMapping("/{idcliente}")
    public ResponseEntity<Void> delete(@PathVariable Long idcliente){
        if(clienteRepository.existsById(idcliente)){
            clienteRepository.deleteById(idcliente);
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.notFound().build();
        }
    }
}