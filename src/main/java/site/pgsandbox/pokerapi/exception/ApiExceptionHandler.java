package site.pgsandbox.pokerapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import site.pgsandbox.pokerapi.exception.card.*;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(DeckNotFoundException.class)
    public ProblemDetail handleDeckNotFound(DeckNotFoundException exception) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setTitle("Deck not found");
        problem.setDetail(exception.getMessage());
        return problem;
    }

    @ExceptionHandler(EmptyDeckException.class)
    public ProblemDetail handleEmptyDeck(EmptyDeckException exception) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problem.setTitle("Empty deck");
        problem.setDetail(exception.getMessage());
        return problem;
    }
}
