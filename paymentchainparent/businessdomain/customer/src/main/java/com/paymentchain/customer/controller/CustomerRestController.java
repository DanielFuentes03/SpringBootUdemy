/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/RestController.java to edit this template
 */
package com.paymentchain.customer.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.paymentchain.customer.entities.Customer;
import com.paymentchain.customer.entities.CustomerProduct;
import com.paymentchain.customer.repository.CustomerRepository;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.time.Duration;
import java.util.Collections;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

/**
 *
 * @author DF504696
 */
@RestController
@RequestMapping("/customer")
public class CustomerRestController {
    
    @Autowired
    CustomerRepository cure;
    
    private final WebClient.Builder webClientBuilder;
    
    public CustomerRestController(WebClient.Builder webclientbuilder){
        this.webClientBuilder = webclientbuilder;
    }
    
    HttpClient client = HttpClient.create()
            //Conection TimeOut: is a period within which a conection between a client and a server must be established 
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,5000)
            .option(ChannelOption.SO_KEEPALIVE, true)
            .option(EpollChannelOption.TCP_KEEPIDLE, 300)
            .option(EpollChannelOption.TCP_KEEPINTVL, 60)
            //Response Time
            .responseTimeout(Duration.ofSeconds(1))
            .doOnConnected(connection -> {
                connection.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS)); 
                connection.addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS));
            });
    
    
    @GetMapping()
    public List<Customer> findAll() {
        return cure.findAll();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable(name = "id") long id) {
        Optional<Customer> customer = cure.findById(id);
                if(customer.isPresent()){
                    return new ResponseEntity<>(customer.get(),HttpStatus.OK);
                }else{
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> put(@PathVariable(name = "id") long id, @RequestBody Customer input) {
        Optional <Customer> optionalCustomer = cure.findById(id);
        if(optionalCustomer.isPresent()){
            Customer newCustomer = optionalCustomer.get();
            newCustomer.setNombre(input.getNombre());
            newCustomer.setTelefono(input.getTelefono());
            Customer save = cure.save(newCustomer );
            return new ResponseEntity<>(save,HttpStatus.NO_CONTENT);
        }else{
             return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }  
    }
    
    @PostMapping
    public ResponseEntity<?> post(@RequestBody Customer input) {
        input.getProducts().forEach(x -> x.setCustomer(input));
        Customer datoguardado = cure.save(input);
        return ResponseEntity.ok(datoguardado);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") long id) {
        cure.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @GetMapping("/full")
    public Customer getByCode(@RequestParam(name = "code") String code){
        Customer customer = cure.findByCode(code);
        List<CustomerProduct> products = customer.getProducts();
        products.forEach(x ->{
        String productName = getProductName(x.getId());
        x.setProductName(productName);
    });
        return customer;
    }
    
    private String getProductName(long id){
        WebClient build = webClientBuilder.clientConnector(new ReactorClientHttpConnector(client))
                .baseUrl("http://localhost:8083/product")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url","http://localhost:8083/product"))
                .build();
        JsonNode block = build.method(HttpMethod.GET).uri("/"+id).retrieve().bodyToMono(JsonNode.class).block();
        String name = block.get("nombre").asText();
        return name;
    }
    
    private List<?> getTransactions(String cuenta){
        WebClient build = webClientBuilder.clientConnector(new ReactorClientHttpConnector(client))
                .baseUrl("http://localhost:8084/transaction")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        Optional<List<?>> transactionsOptional = Optional.ofNullable(build.method(HttpMethod.GET)
                .uri(uriBuilder -> uriBuilder
                .path("\"/customer/transactions\"")
                .queryParam("cuenta_Bancaria", cuenta)
                .build())
        .retrieve()
        .bodyToFlux(Object.class)
        .collectList()
        .block());
        
        return transactionsOptional.orElse(Collections.emptyList());
    }
}
