CREATE DATABASE IF NOT EXISTS recruitment_db;
USE recruitment_db;
CREATE TABLE IF NOT EXISTS recruitment_records (
 id BIGINT AUTO_INCREMENT PRIMARY KEY, application_id BIGINT NOT NULL UNIQUE, candidate_id BIGINT NOT NULL, job_id BIGINT NOT NULL,
 status VARCHAR(30) NOT NULL, interview_at TIMESTAMP NULL, interview_date DATE NULL, interview_time TIME NULL, interview_mode VARCHAR(30), meeting_link VARCHAR(500), interview_status VARCHAR(30) DEFAULT 'NOT_SCHEDULED', interview_notes TEXT, decision_notes TEXT,
 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
 INDEX idx_recruitment_candidate (candidate_id), INDEX idx_recruitment_job (job_id), INDEX idx_recruitment_status (status)
);
