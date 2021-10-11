package es.gcuellar.reactive.crud.server.repository;

import es.gcuellar.reactive.crud.server.model.Invoice;
import es.gcuellar.reactive.crud.server.utils.ObjectRandomizer;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Repository
public class InvoiceRepository {

    private Map<Integer,Invoice> data = new HashMap<>();

    public Mono<Invoice> findById(Integer id){
        return Mono.just(data.get(id));
    }

    public Flux<Invoice> findAll(){
        return Flux.fromIterable(data.values());
    }

    public Mono<Invoice> insert(Invoice invoice){
        Integer id = ObjectRandomizer.randomInteger();
        invoice.setId(id);
        data.put(id,invoice);
        return Mono.just(data.get(id));
    }

}
