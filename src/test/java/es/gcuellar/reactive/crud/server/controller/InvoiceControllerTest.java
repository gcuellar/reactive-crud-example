package es.gcuellar.reactive.crud.server.controller;

import es.gcuellar.reactive.crud.server.model.Invoice;
import es.gcuellar.reactive.crud.server.service.InvoiceService;
import es.gcuellar.reactive.crud.server.utils.ObjectRandomizer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
@WebFluxTest(InvoiceController.class)
public class InvoiceControllerTest {

    private final Integer TOTAL_INVOICES = 3;
    private final List<Invoice> invoices = ObjectRandomizer.randomListOf(Invoice.class, TOTAL_INVOICES);

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private InvoiceService invoiceService;

    @Test
    public void getAllInvoicesTestOK(){
        when(invoiceService.getInvoices()).thenReturn(Flux.fromIterable(invoices));

        webClient.get().uri("/invoices")
                .header(HttpHeaders.ACCEPT, "application/json")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Invoice.class)
                .hasSize(TOTAL_INVOICES);
    }

    @Test
    public void getInvoiceTestOK(){
        Integer id = invoices.get(0).getId();
        String name = invoices.get(0).getName();

        when(invoiceService.getInvoice(id)).thenReturn(Mono.just(invoices.get(0)));

        webClient.get().uri("/invoices/{id}",id)
               .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.number").isNotEmpty()
                .jsonPath("$.name").isEqualTo(name);
    }

    @Test
    public void postInvoiceTestOk(){
        ObjectRandomizer.RandomGenerationExclusion excludedField =
                new ObjectRandomizer.RandomGenerationExclusion("id", Integer.class, null);
        Invoice newInvoice = ObjectRandomizer.random(Invoice.class, excludedField);

        Invoice createdInvoice = new Invoice();
        BeanUtils.copyProperties(newInvoice,createdInvoice);
        createdInvoice.setId(-1);

        when(invoiceService.createInvoice(newInvoice)).thenReturn(Mono.just(createdInvoice));

        webClient.post().uri("/invoices")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(newInvoice), Invoice.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo(-1);
    }
}
