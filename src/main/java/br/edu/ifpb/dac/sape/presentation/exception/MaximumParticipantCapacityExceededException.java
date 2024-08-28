package br.edu.ifpb.dac.sape.presentation.exception;

public class MaximumParticipantCapacityExceededException extends RuntimeException {
    public MaximumParticipantCapacityExceededException() {
        super("Capacidade máxima de participantes excedida!");
    }
}
