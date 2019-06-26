package com.br.artistas.endpoint;

import com.br.artistas.constants.Constants;
import com.br.artistas.exceptions.ArtistaNotFoundException;
import com.br.artistas.services.ArtistaService;
import com.br.artistas.endpoint.response.ArtistaResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(Constants.CONTEXT_PATH)
public class ArtistaEndpoint {

    private static final Logger LOG = LoggerFactory.getLogger(ArtistaEndpoint.class);

    private final ArtistaService service;

    @Autowired
    public ArtistaEndpoint(ArtistaService service) {
        this.service = service;
    }


//    @ResponseStatus(value = HttpStatus.CREATED)
//    @PostMapping("/routes")
//    public Mono<RouteResponse> create(@RequestBody RouteRequest request) {
//
//        LOG.info("Solicitação de criação de novas rotas [{}]", request);
//
//        return service.create(toModel(request))
//                      .map(RouteResponse::new)
//                      .doOnSuccess(wr -> LOG.info("Resposta de criação das rotas [{}]", wr));
//    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/artista")
    public Mono<ArtistaResponse> find(@RequestParam(value = "nome") String nome) {

        LOG.info("Buscando o artista NOME: [ {} ]", nome);

        return service.find(nome)
                .switchIfEmpty(Mono.error(new ArtistaNotFoundException("artistas_validation",	"Informações não encontradas para o nome  " + nome)))
                .map(ArtistaResponse::new)
                .doOnSuccess(wr -> LOG.info("Coleção completa do artista [ {} ]", wr));
    }
}