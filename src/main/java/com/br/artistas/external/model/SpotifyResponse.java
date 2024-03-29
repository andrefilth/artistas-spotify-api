
package com.br.artistas.external.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "artists"
})
public class SpotifyResponse {

    @JsonProperty("artists")
    private Artist artists;

    @JsonProperty("artists")
    public Artist getArtists() {
        return artists;
    }

    @JsonProperty("artists")
    public void setArtists(Artist artists) {
        this.artists = artists;
    }

}
