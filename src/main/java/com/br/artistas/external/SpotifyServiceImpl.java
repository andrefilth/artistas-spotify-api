package com.br.artistas.external;

import com.br.artistas.config.SpotifyConfig;
import com.br.artistas.external.exception.SpotifyException;
import com.br.artistas.external.model.ArtistAlbumSpotifyResponse;
import com.br.artistas.security.SpotifyToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;

@Service
public class SpotifyServiceImpl implements SpotifyService {

    private static final Logger LOG = LoggerFactory.getLogger(SpotifyServiceImpl.class);

    private final WebClient client;
    private final SpotifyConfig config;
    private final String authorization;
    private final Mono<SpotifyToken> spotifyToken;

    @Autowired
    public SpotifyServiceImpl(WebClient client, SpotifyConfig config) {
        this.client = client;
        this.config = config;
        this.authorization = "Basic " + Base64.getEncoder()
                                              .encodeToString(String.format("%s:%s", config.getAccessKey(), config.getSecretKey())
                                              .getBytes(StandardCharsets.UTF_8));
        this.spotifyToken = this.getToken().cache(Duration.ofMinutes(30))
                .doOnSuccess(s -> LOG.info("Token do Spotify obtido com sucesso"));
    }

    @Override
    public Mono<ArtistAlbumSpotifyResponse> search(String nome) {
        String urlToken = String.format("%s/%s%s%s", config.getUrlApi(), "search","?q="+nome,"&type=album");
        LOG.info("Verificando se o artista existe da base do Spotify GET [ {} ]", urlToken);
        return getToken().flatMap(token->
                client.get()
                      .uri(urlToken)
                      .header("Authorization", "Bearer " + token.getAccessToken())
                      .exchange()
//                      .timeout(Duration.ofSeconds(30))
                .flatMap(res->{
                    if (res.statusCode().is2xxSuccessful()) {
                        return res.bodyToMono(ArtistAlbumSpotifyResponse.class);
                    }
                    return Mono.error(new SpotifyException(500, "spotify_communication_error", "Falha de comunicação com os serviços da Spotify"));
                })
        );
    }


    private Mono<SpotifyToken> getToken() {
        String urlToken = config.getUrlToken();
        LOG.info("Gerando token de autenticação ao serviço do Spotify [{}]", urlToken);
        return client.post()
                .uri(urlToken)
                .header("Authorization", authorization)
                .body(BodyInserters.fromFormData("grant_type", "client_credentials"))
                .exchange()
//                .timeout(Duration.ofSeconds(30))
                .flatMap(res -> {
                    if (res.statusCode().is2xxSuccessful()) {
                        return res.bodyToMono(SpotifyToken.class);
                    }
                    return Mono.error(new SpotifyException(500, "spotify_communication_error", "Falha de comunicação com os serviços da Spotify - Token"));
                });
    }

}
