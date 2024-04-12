/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paymentchain.transactions.controller;

import com.paymentchain.transactions.entities.Transaction;
import com.paymentchain.transactions.repository.TransactionRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author DF504696
 */
@RestController
@RequestMapping("/transaction")
public class TransactionRestController {
    
    @Autowired
    TransactionRepository trarep;
    
    @GetMapping()
    public List<Transaction> findAll(){
        return trarep.findAll();
    }
    
    @GetMapping()
    public List<Transaction> get(@RequestParam String cuenta_Bancaria){
        return trarep.findbyCuentaBancaria(cuenta_Bancaria);
    }
    
    @GetMapping("/{id}")
    public Transaction findById(@PathVariable (name = "id") long id){
        return trarep.findById(id).get();
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> put(@PathVariable(name = "id") long id, @RequestBody Transaction input){
        Transaction save = trarep.save(input);
        return ResponseEntity.ok(save);
    }
    
    @PostMapping
    public ResponseEntity<?> post(@RequestBody Transaction input){
      Transaction datoGuardado = trarep.save(input);
      return ResponseEntity.ok(datoGuardado);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete (@PathVariable(name = "id") long id){
        Optional<Transaction> findDelete = trarep.findById(id);
        if(findDelete.get() != null){
            trarep.delete(findDelete.get());
        }
        return ResponseEntity.ok().build();
    }
    
}
