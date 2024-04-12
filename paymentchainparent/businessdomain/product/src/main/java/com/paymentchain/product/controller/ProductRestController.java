/*

 */
package com.paymentchain.product.controller;

import com.paymentchain.product.entities.Product;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import com.paymentchain.product.repository.ProductRepository;
import java.util.Optional;

/**
 *
 * @author DF504696
 */
@RestController
@RequestMapping("/product")
public class ProductRestController {
    
    @Autowired
    ProductRepository prre;
    
    @GetMapping()
    public List<Product> findAll() {
        return prre.findAll();
    }
    
    @GetMapping("/{id}")
    public Product get(@PathVariable(name = "id") long id) {
        return prre.findById(id).get();
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> put(@PathVariable(name = "id") long id, @RequestBody Product input) {
        Product save = prre.save(input);
        return ResponseEntity.ok(save);
    }
    
    @PostMapping
    public ResponseEntity<?> post(@RequestBody Product input) {
        Product datoguardado = prre.save(input);
        return ResponseEntity.ok(datoguardado);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") long id) {
        Optional<Product> findDelete = prre.findById(id);
        if(findDelete.get() != null){
            prre.delete(findDelete.get());
        }
        return ResponseEntity.ok().build();
    }
}
