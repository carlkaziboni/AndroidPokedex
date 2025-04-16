package com.example.pokedex;

public class Pokemon {
    private String name;
    private String url;
    private boolean caught;

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public boolean isCaught() {
        return caught;
    }

    public void setCaught(boolean caught){
        this.caught = caught;
    }

    Pokemon(String name, String url){
        this.name = name;
        this.url = url;
        this.caught = false;
    }
}
