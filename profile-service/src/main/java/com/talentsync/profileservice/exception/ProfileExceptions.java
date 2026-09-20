package com.talentsync.profileservice.exception;

public final class ProfileExceptions {
    private ProfileExceptions() {}
    public static class NotFound extends RuntimeException { public NotFound(Long id) { super("Profile for user " + id + " not found"); } }
    public static class Conflict extends RuntimeException { public Conflict(Long id) { super("Profile already exists for user " + id); } }
}
