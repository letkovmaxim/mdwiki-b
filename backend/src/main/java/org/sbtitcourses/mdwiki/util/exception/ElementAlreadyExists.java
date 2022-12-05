package org.sbtitcourses.mdwiki.util.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class ElementAlreadyExists extends RuntimeException {

    private final String error;

    public ElementAlreadyExists(String error) {
        this.error = error;
    }
}
