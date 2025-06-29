-- Insert sample users data
INSERT INTO
    users (
        email,
        password,
        full_name,
        role,
        is_active
    )
VALUES (
        'admin@school.com',
        '$2a$10$rAM0QZqZqZqZqZqZqZqZqO',
        'Admin User',
        'ADMIN',
        true
    ),
    (
        'nurse@school.com',
        '$2a$10$rAM0QZqZqZqZqZqZqZqZqO',
        'School Nurse',
        'SCHOOL_NURSE',
        true
    ),
    (
        'manager@school.com',
        '$2a$10$rAM0QZqZqZqZqZqZqZqZqO',
        'School Manager',
        'MANAGER',
        true
    ),
    (
        'parent1@example.com',
        '$2a$10$rAM0QZqZqZqZqZqZqZqZqO',
        'Parent One',
        'PARENT',
        true
    ),
    (
        'parent2@example.com',
        '$2a$10$rAM0QZqZqZqZqZqZqZqZqO',
        'Parent Two',
        'PARENT',
        true
    ),
    (
        'student1@school.com',
        '$2a$10$rAM0QZqZqZqZqZqZqZqZqO',
        'Student One',
        'STUDENT',
        true
    ),
    (
        'student2@school.com',
        '$2a$10$rAM0QZqZqZqZqZqZqZqZqO',
        'Student Two',
        'STUDENT',
        true
    );