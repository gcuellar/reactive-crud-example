package es.gcuellar.reactive.crud.server.model;

import lombok.Data;

@Data
public class Invoice {

    private Integer id;
    private String name;
    private String number;
    private Double amount;
}
