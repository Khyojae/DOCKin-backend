-- 사용자
-- 사용자 권한
-- 장비 정보
-- 작업 일지
-- 체크리스트
-- 근태 관리
-- 채팅방
-- 긴급 연락처
-- 안전 관리
-- refresh_token
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS safety_enrollments;
DROP TABLE IF EXISTS safety_courses;
DROP TABLE IF EXISTS emergency_contacts;
DROP TABLE IF EXISTS chat_messages;
DROP TABLE IF EXISTS chat_members;
DROP TABLE IF EXISTS chat_rooms;
DROP TABLE IF EXISTS absence_requests;
DROP TABLE IF EXISTS attendance;
DROP TABLE IF EXISTS notifications;
DROP TABLE IF EXISTS checklist_results;
DROP TABLE IF EXISTS checklist_items;
DROP TABLE IF EXISTS checklists;
DROP TABLE IF EXISTS work_logs;
DROP TABLE IF EXISTS equipment;
DROP TABLE IF EXISTS Authority;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS refresh_token;
ALTER TABLE attendance ADD COLUMN total_work_time VARCHAR(20) DEFAULT NULL;

SET FOREIGN_KEY_CHECKS = 1;
-- 1. 사용자
CREATE TABLE users (
                       user_id VARCHAR(50) PRIMARY KEY,
                       name VARCHAR(10) NOT NULL,
                       password VARCHAR(256) NOT NULL,
                       role ENUM('ADMIN','USER'),
                       work_shift ENUM('MORNING', 'AFTERNOON', 'NIGHT') DEFAULT 'MORNING',
                       language_code VARCHAR(10) DEFAULT 'ko',
                       tts_enabled BOOLEAN DEFAULT TRUE,
                       created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                       updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       ship_yard_area VARCHAR(100) NOT NULL
);

-- 2. 사용자 권한
CREATE TABLE Authority(
                          id INTEGER AUTO_INCREMENT PRIMARY KEY,
                          authority VARCHAR(256) NOT NULL,
                          user_id VARCHAR(50) NOT NULL,
                          FOREIGN KEY(user_id) REFERENCES users(user_id)
);

-- 3. 장비 정보
CREATE TABLE equipment (
                           equipment_id INT PRIMARY KEY AUTO_INCREMENT,
                           name VARCHAR(100),
                           nfc_tag VARCHAR(100) UNIQUE,
                           qr_code VARCHAR(100) UNIQUE,
                           created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                           updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
-- 4. 작업 일지 (음성 → 텍스트 저장)
CREATE TABLE work_logs (
                           log_id INT PRIMARY KEY AUTO_INCREMENT,
                           user_id VARCHAR(50),
                           title VARCHAR(256) NOT NULL,
                           equipment_id INT,
                           log_text TEXT NOT NULL,
                           image_url VARCHAR(255),
                           audio_file_url VARCHAR(255),
                           created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                           updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           FOREIGN KEY (user_id) REFERENCES users(user_id),
                           FOREIGN KEY (equipment_id) REFERENCES equipment(equipment_id)
);
-- 5. 체크리스트
CREATE TABLE checklists (
                            checklist_id INT PRIMARY KEY AUTO_INCREMENT,
                            equipment_id INT NOT NULL,
                            title VARCHAR(100) NOT NULL,
                            role ENUM('pre', 'post'), -- 작업 전/후
                            created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                            updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            FOREIGN KEY (equipment_id) REFERENCES equipment(equipment_id)
);

-- 6. 체크리스트 항목
CREATE TABLE checklist_items (
                                 item_id INT PRIMARY KEY AUTO_INCREMENT,
                                 checklist_id INT NOT NULL,
                                 content VARCHAR(255) NOT NULL,
                                 sequence INT NOT NULL,   -- 출력 순서 관리
                                 FOREIGN KEY (checklist_id) REFERENCES checklists(checklist_id)
);

-- 7. 체크리스트 결과
CREATE TABLE checklist_results (
                                   result_id INT PRIMARY KEY AUTO_INCREMENT,
                                   checklist_id INT NOT NULL,
                                   user_id VARCHAR(50) NOT NULL,
                                   equipment_id INT NOT NULL,
                                   checked_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                   is_checked BOOLEAN NOT NULL,
                                   FOREIGN KEY (checklist_id) REFERENCES checklists(checklist_id),
                                   FOREIGN KEY (user_id) REFERENCES users(user_id),
                                   FOREIGN KEY (equipment_id) REFERENCES equipment(equipment_id)
);


-- 9. 근태 관리 테이블
CREATE TABLE attendance (
                            id BIGINT PRIMARY KEY AUTO_INCREMENT,
                            user_id VARCHAR(50) NOT NULL,
                            clock_in_time DATETIME NOT NULL,
                            clock_out_time DATETIME,
                            work_date DATE NOT NULL,
                            role ENUM('NORMAL','LATE','ABSENT','VACATION','SICK'),

                            in_location VARCHAR(255),
                            out_location VARCHAR(255),
                            CONSTRAINT fk_attendance_member FOREIGN KEY (user_id) REFERENCES users (user_id),
                            INDEX idx_work_date (work_date)
);

-- 10. 근태 요청 (병결/휴가 서류 등록)
CREATE TABLE absence_requests (
                                  request_id INT PRIMARY KEY AUTO_INCREMENT,
                                  user_id VARCHAR(50) NOT NULL,
                                  request_type VARCHAR(20) NOT NULL, -- 'SICK'(병결), 'VACATION'(휴가)
                                  start_date DATE NOT NULL,
                                  end_date DATE NOT NULL,
                                  reason TEXT NOT NULL,
                                  document_url VARCHAR(255), -- 서류 파일 (이미지/PDF) 저장 경로 (S3 등)
                                  status VARCHAR(20) DEFAULT 'PENDING', -- 'PENDING'(대기), 'APPROVED'(승인), 'REJECTED'(거절)
                                  requested_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                  processed_by VARCHAR(50), -- 승인/거절 처리한 관리자
                                  processed_at DATETIME,
                                  last_message_content TEXT,
                                  last_message_at DATETIME,
                                  FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
                                  FOREIGN KEY (processed_by) REFERENCES users(user_id)
);

-- 11. 채팅방 정보
CREATE TABLE chat_rooms (
                            room_id INT PRIMARY KEY AUTO_INCREMENT,
                            room_name VARCHAR(100), -- 단체방 이름 (1:1 방은 NULL 가능)
                            is_group BOOLEAN DEFAULT FALSE, -- 단체방 여부 (TRUE: 단체, FALSE: 1:1),
                            creator_id VARCHAR(50),
                            last_message_content TEXT,
                            last_message_at DATETIME,
                            created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 12. 채팅방 참여자
CREATE TABLE chat_members (
                              id INT PRIMARY KEY AUTO_INCREMENT,
                              room_id INT NOT NULL,
                              user_id VARCHAR(50) NOT NULL,
                              joined_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                              last_read_time DATETIME DEFAULT CURRENT_TIMESTAMP, -- 마지막 읽은 시간 (안읽은 메시지 수 계산용)
                              FOREIGN KEY (room_id) REFERENCES chat_rooms(room_id) ON DELETE CASCADE,
                              FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 13. 채팅 메시지
CREATE TABLE chat_messages (
                               message_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                               room_id INT NOT NULL,
                               sender_id VARCHAR(50) NOT NULL,
                               content TEXT NOT NULL,
                               message_type ENUM('TEXT','IMAGE','FILE') DEFAULT 'TEXT',
                               file_url VARCHAR(255),
                               sent_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                               FOREIGN KEY (room_id) REFERENCES chat_rooms(room_id) ON DELETE CASCADE,
                               INDEX idx_room_sent (room_id,sent_at)
);



-- 15. 안전 교육 과정 정보
CREATE TABLE safety_courses (
                                course_id INT PRIMARY KEY AUTO_INCREMENT,
                                title VARCHAR(255) NOT NULL,
                                description TEXT NOT NULL,
                                video_url VARCHAR(255) NOT NULL,
                                duration_minutes INT DEFAULT 0,
                                is_mandatory BOOLEAN DEFAULT TRUE, -- 필수 이수 여부
                                created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 16. 사용자별 안전 교육 이수 상태
CREATE TABLE safety_enrollments (
                                    enrollment_id INT PRIMARY KEY AUTO_INCREMENT,
                                    user_id VARCHAR(50) NOT NULL,
                                    course_id INT NOT NULL,
                                    status VARCHAR(20) NOT NULL DEFAULT 'UNWATCHED',
                                    completion_date DATETIME, -- 이수 완료 시각 (NULL이면 미이수)
                                    enrolled_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
                                    FOREIGN KEY (course_id) REFERENCES safety_courses(course_id) ON DELETE CASCADE,
                                    UNIQUE KEY uk_user_course (user_id, course_id)
);

-- 17. refresh_token
CREATE TABLE refresh_token (
                               user_id VARCHAR(255) NOT NULL,
                               token VARCHAR(512) NOT NULL, -- JWT는 길기 때문에 길이를 충분히 줍니다.
                               PRIMARY KEY (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
