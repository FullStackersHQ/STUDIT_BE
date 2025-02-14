-- User 테이블에 더미 데이터 삽입
INSERT INTO user (id, kakaoId, nickname, profileImage, point, email, role, createdAt, updatedAt)
VALUES
    (1, 1001, '홍길동', 'https://example.com/profile1.jpg', 100, 'hong@example.com', 'ROLE_USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 1002, '김철수', 'https://example.com/profile2.jpg', 200, 'kim@example.com', 'ROLE_USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (3, 1003, '박영희', 'https://example.com/profile3.jpg', 150, 'park@example.com', 'ROLE_ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- StudyRecruit 테이블에 더미 데이터 삽입
INSERT INTO study_recruits (
    recruit_id, leader_id, title, description, tags, category,
    goalTime, deposit, maxMembers,
    studyStartAt, studyEndAt,
    recruitStartAt, recruitEndAt,
    status, updatedAt
) VALUES
    (1, 1, '알고리즘 스터디', '매일 한 문제씩 푸는 알고리즘 스터디입니다.', '#코딩 #알고리즘', 'CODING',
     100, 50000, 10,
     '2025-02-15 10:00:00', '2025-06-15 18:00:00',
     '2025-02-10 09:00:00', '2025-02-14 23:59:59',
     'ACTIVE', NOW()),
    (2, 2, '토익 스터디', '900점 목표로 함께 공부해요.', '#토익 #영어', 'LANGUAGE',
     80, 30000, 5,
     '2025-03-01 08:00:00', '2025-06-30 20:00:00',
     '2025-02-20 10:00:00', '2025-02-28 23:59:59',
     'ACTIVE', NOW());

-- StudyRegister 테이블에 더미 데이터 삽입
INSERT INTO study_registers (register_id, recruit_id, user_id, status, registerAt)
VALUES
    (1, 1, 2, 'REGISTER', CURRENT_TIMESTAMP),
    (2, 1, 3, 'REGISTER', CURRENT_TIMESTAMP),
    (3, 2, 1, 'REGISTER', CURRENT_TIMESTAMP);

-- StudyRoom 테이블 생성
--CREATE TABLE StudyRoom (
--    roomId BIGINT PRIMARY KEY AUTO_INCREMENT,
--    leader_id BIGINT NOT NULL,
--    title VARCHAR(255) NOT NULL,
--    description VARCHAR(255),
--    tags VARCHAR(255),
--    category ENUM('CODING','COLLEGE_ENTRANCE','EMPLOYMENT','EXAMS','LANGUAGE','OTHERS','PUBLIC_SERVICE','TEACHER_APPOINTMENT') NOT NULL,
--    goalTime INT NOT NULL,
--    deposit INT NOT NULL,
--    maxMembers INT NOT NULL,
--    studyStartAt DATETIME(6) NOT NULL,
--    studyEndAt DATETIME(6) NOT NULL,
--    status ENUM('ended','progress') NOT NULL,
--    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--    updatedAt DATETIME(6) DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
--);


-- StudyRoom 테이블 데이터 삽입
INSERT INTO StudyRoom (roomId, leader_id, title, description, tags, category, goalTime, deposit, maxMembers, studyStartAt, studyEndAt, status, updatedAt)
VALUES
(1, 1, '알고리즘 스터디', '매일 한 문제씩 푸는 알고리즘 스터디입니다.', '#코딩 #알고리즘', 'CODING', 100, 50000, 10, '2025-02-15 10:00:00', '2025-06-15 18:00:00', 'progress', NOW()),
(2, 2, '토익 스터디', '900점 목표로 함께 공부해요.', '#토익 #영어', 'LANGUAGE', 80, 30000, 5, '2025-03-01 08:00:00', '2025-06-30 20:00:00', 'progress', NOW());


-- study_partcpnt_mgnt 테이블 생성
--CREATE TABLE study_partcpnt_mgnt (
--    id BIGINT PRIMARY KEY AUTO_INCREMENT,
--    room_id BIGINT NOT NULL,
--    user_id BIGINT NOT NULL,
--    joinPoint INT NOT NULL,
--    joinAt DATETIME(6) NOT NULL,
--    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--    studyCompleteStatus ENUM('COMPLETE','FAIL','REFUND') NOT NULL,
--    user_code ENUM('BLACK','LEADER','MEMBER') NOT NULL,
--    FOREIGN KEY (room_id) REFERENCES StudyRoom(roomId),
--    FOREIGN KEY (user_id) REFERENCES user(id)
--);


-- study_partcpnt_mgnt 테이블 데이터 삽입
INSERT INTO study_partcpnt_mgnt (room_id, user_id, joinPoint, joinAt, createdAt, studyCompleteStatus, user_code)
VALUES
(1, 1, 50, '2025-02-16 10:00:00', NOW(), 'COMPLETE', 'MEMBER'),
(2, 2, 30, '2025-03-02 08:30:00', NOW(), 'FAIL', 'LEADER');