USE Dockin;
SET FOREIGN_KEY_CHECKS = 0;

INSERT IGNORE INTO users (user_id, name, password, role, ship_yard_area, language_code, tts_enabled)
VALUES ('1001', '김철수', '1234', 'ADMIN', '제8조선소', 'ko', 1),
       ('1002', '이영희', '1234', 'ADMIN', '제8조선소', 'en', 0);


INSERT IGNORE INTO equipment (equipment_id, name, qr_code)
VALUES (1, '굴착기 01', 'QR001'), (2, '지게차 05', 'QR002');


INSERT IGNORE INTO attendance (user_id, work_date, clock_in_time, role, in_location)
VALUES ('1001', '2026-01-08', NOW(), 'NORMAL', '제8조선소');

INSERT IGNORE INTO work_logs (user_id, title, equipment_id, log_text, audio_file_url)
VALUES
('1001', '오전 선체 용접 부위 점검', 1, '금일 오전 제8조선소 A구역에서 선체 하부 용접 상태를 점검함. 굴착기를 이용하여 진입로 확보 후 육안 검사 실시. 특이사항 없음.', 'https://s3.v-dockin.com/audio/log_01.mp3'),

('1001', '장비 연료 누유 확인 및 조치', 1, '작업 도중 굴착기 유압 호스 부근에서 미세한 누유 발견. 즉시 작업을 중단하고 정비팀에 보고 후 호스 교체 완료함.', 'https://s3.v-dockin.com/audio/log_02.mp3'),

('1002', '자재 운반 및 적재 작업', 2, '오후 2시경 강판 자재 5톤 분량을 제3도크로 운반 완료함. 지게차 포크 상태 양호하며 적재 구역 수평 확인 후 배치함.', 'https://s3.v-dockin.com/audio/log_03.mp3'),

('1002', '작업 종료 후 장비 정비', 2, '일과 종료 전 지게차 배터리 충전 상태 확인 및 외부 세척 실시. 타이어 마모 상태가 심해 다음 주 교체 필요함.', 'https://s3.v-dockin.com/audio/log_04.mp3'),

('1001', '야간 비상 통로 확보 작업', 1, '강풍 대비 현장 주변 적치물 정리 및 비상 통로 확보 작업 실시. 야간 시야 확보를 위해 장비 서치라이트 점검 병행함.', 'https://s3.v-dockin.com/audio/log_05.mp3');

SET FOREIGN_KEY_CHECKS = 1;

SET FOREIGN_KEY_CHECKS = 1;