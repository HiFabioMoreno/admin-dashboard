package fabio.dev.Portfolio.Exceptions;

public class NoEntityException extends RuntimeException {
    public NoEntityException(String message) {
        super(message);
    }
    public NoEntityException(String entity, Integer id) {
        super(String.format("%s with id %d not found", entity, id));
    }
}
