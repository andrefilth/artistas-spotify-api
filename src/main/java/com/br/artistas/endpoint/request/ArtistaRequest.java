package com.br.artistas.endpoint.request;

import com.br.artistas.types.SearchType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Set;

public class ArtistaRequest {

    private  String nome;
    private SearchType tipo;
    private Set<String> albuns;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public SearchType getTipo() {
        return tipo;
    }

    public void setTipo(SearchType tipo) {
        this.tipo = tipo;
    }

    public Set<String> getAlbuns() {
        return albuns;
    }

    public void setAlbuns(Set<String> albuns) {
        this.albuns = albuns;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("nome", nome)
                .append("tipo", tipo)
                .append("albuns", albuns)
                .build();
    }
}
