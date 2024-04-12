/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paymentchain.customer.entities;
import jakarta.persistence.*;
import java.util.List;
import lombok.Data;
/**
 *
 * @author DF504696
 */
@Data
@Entity
public class Customer {
    @GeneratedValue(strategy = GenerationType.AUTO)   
    @Id
    private long id;
    private String code;
    private String nombre;
    private String apellidos;
    private String direccion;
    private String telefono;
    private String numero_cuenta;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomerProduct> products;
    
    @Transient
    private List<?> transactions;
    
}
