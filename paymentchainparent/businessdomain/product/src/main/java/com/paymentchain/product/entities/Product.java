
package com.paymentchain.product.entities;
import jakarta.persistence.*;
import lombok.Data;
/**
 *
 * @author DF504696
 */
@Data
@Entity
public class Product {
    @GeneratedValue(strategy = GenerationType.AUTO)   
    @Id
    private long id;
    private String nombre;
    private String code;
}
