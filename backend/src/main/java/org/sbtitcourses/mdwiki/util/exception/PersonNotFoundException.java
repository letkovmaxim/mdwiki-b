package org.sbtitcourses.mdwiki.util.exception;

public class PersonNotFoundException extends RuntimeException implements NotFoundException {

    @Override
    public String getError() {
        return "Человек не найден";
    }
}
