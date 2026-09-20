package com.talentsync.authservice.exception;

public final class AuthExceptions {
    private AuthExceptions() {}
    public static class Conflict extends RuntimeException { public Conflict(String message) { super(message); } }
    public static class InvalidCredentials extends RuntimeException { public InvalidCredentials() { super("Invalid email or password"); } }
}
