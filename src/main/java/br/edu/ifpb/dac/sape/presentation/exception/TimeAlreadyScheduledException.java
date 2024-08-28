package br.edu.ifpb.dac.sape.presentation.exception;

public class TimeAlreadyScheduledException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public TimeAlreadyScheduledException() {
        super("Já existe uma prática agendada para esse horário!");
    }

}
