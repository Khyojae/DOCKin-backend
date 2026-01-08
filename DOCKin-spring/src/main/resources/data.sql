SET FOREIGN_KEY_CHECKS = 0;
INSERT INTO users (user_id, name, password, role, ship_yard_area, language_code, tts_enabled, created_at, updated_at)
VALUES ('1001', '김철수', '1234', 'ADMIN', '제8조선소', 'ko', TRUE, NOW(), NOW());
INSERT INTO users (user_id, name, password, role, ship_yard_area, language_code, tts_enabled, created_at, updated_at)
VALUES ('1002', '이영희', '1234', 'ADMIN', '제8조선소', 'en', FALSE, NOW(), NOW());
INSERT INTO users (user_id, name, password, role, ship_yard_area, language_code, tts_enabled, created_at, updated_at)
VALUES ('1003', '박민준', '1234', 'USER', '제5조선소', 'ko', TRUE, NOW(), NOW());
SET FOREIGN_KEY_CHECKS = 1;

