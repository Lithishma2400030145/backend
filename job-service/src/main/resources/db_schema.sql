-- MySQL Database Script for Job Service
-- Creates the job_db database and tables

-- Create Database
CREATE DATABASE IF NOT EXISTS job_db;
USE job_db;

-- Create Jobs Table
CREATE TABLE IF NOT EXISTS jobs (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    company VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL,
    description LONGTEXT NOT NULL,
    required_skills LONGTEXT,
    salary DECIMAL(10, 2) NOT NULL,
    salary_type VARCHAR(50),
    status VARCHAR(50) NOT NULL DEFAULT 'DRAFT',
    job_type VARCHAR(500),
    category VARCHAR(500),
    benefits LONGTEXT,
    experience_level VARCHAR(100),
    application_count INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    
    INDEX idx_status (status),
    INDEX idx_company (company),
    INDEX idx_location (location),
    INDEX idx_experience_level (experience_level),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert Sample Data (Optional)
INSERT INTO jobs (title, company, location, description, required_skills, salary, salary_type, status, job_type, category, benefits, experience_level, application_count, created_by, updated_by)
VALUES 
    ('Senior Java Developer', 'Tech Corp', 'San Francisco', 'We are looking for a senior Java developer with 5+ years of experience', 'Java, Spring Boot, Microservices, Docker', 150000.00, 'YEARLY', 'OPEN', 'FULL_TIME', 'IT', 'Health Insurance, 401k, Stock Options', 'SENIOR', 0, 'admin', 'admin'),
    ('Frontend Developer', 'Digital Solutions', 'New York', 'Seeking a frontend developer skilled in React and Angular', 'React, Angular, JavaScript, CSS', 110000.00, 'YEARLY', 'OPEN', 'FULL_TIME', 'IT', 'Remote Work, Flexible Hours', 'JUNIOR', 0, 'admin', 'admin'),
    ('Database Administrator', 'Cloud Systems', 'Austin', 'DBA role managing MySQL and PostgreSQL databases', 'MySQL, PostgreSQL, MongoDB, Cloud DB', 130000.00, 'YEARLY', 'OPEN', 'FULL_TIME', 'IT', 'Training Budget, Career Development', 'SENIOR', 0, 'admin', 'admin'),
    ('QA Engineer', 'Software Testing Ltd', 'Seattle', 'Quality assurance engineer for automated and manual testing', 'Selenium, JUnit, TestNG, API Testing', 95000.00, 'YEARLY', 'OPEN', 'FULL_TIME', 'IT', 'Unlimited PTO, Wellness Program', 'JUNIOR', 0, 'admin', 'admin'),
    ('DevOps Engineer', 'Infrastructure Pro', 'Boston', 'Managing cloud infrastructure and CI/CD pipelines', 'Docker, Kubernetes, AWS, Jenkins', 140000.00, 'YEARLY', 'OPEN', 'FULL_TIME', 'IT', 'Stock Options, Relocation Assistance', 'SENIOR', 0, 'admin', 'admin');

-- Grant privileges (if needed)
-- GRANT ALL PRIVILEGES ON job_db.* TO 'root'@'localhost';
-- FLUSH PRIVILEGES;
