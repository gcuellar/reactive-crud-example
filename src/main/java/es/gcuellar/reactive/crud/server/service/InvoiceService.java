package es.gcuellar.reactive.crud.server.service;

import es.gcuellar.reactive.crud.server.model.Invoice;
import es.gcuellar.reactive.crud.server.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    @Autowired
    public InvoiceService(final InvoiceRepository invoiceRepository){
        this.invoiceRepository = invoiceRepository;
    }

    public Mono<Invoice> createInvoice(Invoice invoice){
        return invoiceRepository.insert(invoice);
    }

    public Flux<Invoice> getInvoices(){
        return invoiceRepository.findAll().switchIfEmpty(Flux.empty());
    }

    public Mono<Invoice> getInvoice(Integer id){
        return invoiceRepository.findById(id).switchIfEmpty(Mono.empty());
    }
}
