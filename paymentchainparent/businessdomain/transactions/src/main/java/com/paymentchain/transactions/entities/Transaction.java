/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paymentchain.transactions.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Date;
import lombok.Data;

/**
 *
 * @author DF504696
 */
@Data
@Entity
public class Transaction {
    @GeneratedValue(strategy = GenerationType.AUTO)   
    @Id
    private long id;
    private String referencia;
    private String cuenta_Bancaria;
    private Date fecha;
    private Double monto;
    private Double fee;
    private String descripcion;
    private String estatus;
    private String canal;
    
}
