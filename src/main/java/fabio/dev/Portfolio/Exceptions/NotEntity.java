package fabio.dev.Portfolio.Exceptions;

public class NotEntity extends RuntimeException {
    public NotEntity(String message) {
        super(message);
    }

    public NotEntity(String entity, Integer id) {
        super(String.format("%s with id %d not found", entity, id));
    }
}
