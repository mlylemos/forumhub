package com.alura.forumhub.exception;

public class DuplicateTopicException extends RuntimeException {
    public DuplicateTopicException(String message) { super(message); }
}
