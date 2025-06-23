package exceptions;

import java.io.Serial;

public class OutOfStockException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;

    public OutOfStockException(String message) {
        super(message);
    }

    public OutOfStockException(String message, Throwable cause) {
        super(message, cause);
    }

    public OutOfStockException(Throwable cause) {
        super(cause);
    }
}
