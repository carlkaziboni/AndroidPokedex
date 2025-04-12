package com.example.pokedex;

public class Pokemon {
    private String name;
    private String url;

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    Pokemon(String name, String url){
        this.name = name;
        this.url = url;
    }
}
