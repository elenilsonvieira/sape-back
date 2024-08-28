package br.edu.ifpb.dac.sape.presentation.exception;

public class FavoriteSportException extends RuntimeException {
    public FavoriteSportException() {
        super("Esporte já favoritado!");
    }
}
