package es.gcuellar.reactive.crud.server.controller;

import es.gcuellar.reactive.crud.server.model.Invoice;
import es.gcuellar.reactive.crud.server.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @Autowired
    public InvoiceController(final InvoiceService invoiceService){
        this.invoiceService = invoiceService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Invoice> saveOneInvoice(@RequestBody Invoice invoice){
        return invoiceService.createInvoice(invoice);
    }

    @GetMapping
    public Flux<Invoice> getAllInvoices(){
        return invoiceService.getInvoices();
    }

    @GetMapping("/{id}")
    public Mono<Invoice> getOneInvoice(@PathVariable Integer id){
        Mono<Invoice> invoice= invoiceService.getInvoice(id);
        return invoice;
    }
}
