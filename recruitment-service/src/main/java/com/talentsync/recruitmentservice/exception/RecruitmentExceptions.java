package com.talentsync.recruitmentservice.exception;

public final class RecruitmentExceptions {
    private RecruitmentExceptions() {}
    public static class NotFound extends RuntimeException { public NotFound(Long id) { super("Recruitment record " + id + " not found"); } }
    public static class Duplicate extends RuntimeException { public Duplicate(Long id) { super("Recruitment record already exists for application " + id); } }
}
