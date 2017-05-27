package org.restbucks.wechat.bff.http.security;

public class CannotIssueJwtException extends RuntimeException {
    public CannotIssueJwtException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
