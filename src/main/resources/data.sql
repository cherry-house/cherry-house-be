INSERT INTO user_tb (email, password, name, profile_image, introduction)
VALUES ('cherry@cherry.com', '$2a$12$gP85FkXghfpyI2eiba.FZ.8i8N2541KqTRCT4ujSjrYDTFBAgFEiG', '이피글', null, '1년차 초보 미용사 입니다 :) '),
       ('cherry1@cherry.com', '$2a$12$gP85FkXghfpyI2eiba.FZ.8i8N2541KqTRCT4ujSjrYDTFBAgFEiG', '2피글', null, '2년차 초보 미용사 입니다 :) ');


INSERT INTO post_tb (title, content, category, created_date, user_id)
VALUES ('부산 헤어 모델 구해요!', '서면 피글 미용실에서 무료로 헤어 모델 하실 분 구합니다. 1년차 미용사입니다.', 'HAIR', '2024-01-05', 1),
       ('대구 네일 모델 구해요!', '동성로 피글샵 네일 모델 구함', 'NAIL', '2024-01-06', 1),
       ('포항 메이크업 모델 구해요!', '헤/메 모델 하실 분 구합니다~', 'MAKEUP', '2024-01-07', 1),
       ('부산 네일 모델 구해요2!', '포폴용 네일 받으실 분 구합니당~', 'NAIL', '2024-01-08', 1),
       ('대구 왁싱 모델 구해요3!', '서면 피글 미용실에서 무료로 헤어 모델 하실 분 구합니다. 1년차 미용사입니다.', 'ETC', '2024-01-09', 1),
       ('서울 메컵 모델 구해요4!', '준비생입니다. 홍대쪽 위치해있습니다.', 'MAKEUP', '2024-01-10', 1),
       ('부산 헤어 모델 구해요5!', '얼굴 공개 하실 헤어 모델 구합니다. 포폴용입니다.', 'HAIR', '2024-01-11', 1),
       ('네일 받으실 분~', '서면 피글 미용실에서 무료로 헤어 모델 하실 분 구합니다. 1년차 미용사입니다.', 'NAIL', '2024-01-12', 1),
       ('6-7만원 네일 모델 구함', '서면 피글 미용실에서 무료로 헤어 모델 하실 분 구합니다. 1년차 미용사입니다.', 'NAIL', '2024-01-13', 1),
       ('서면 네일 모델 구해요', '서면 피글 미용실에서 무료로 헤어 모델 하실 분 구합니다. 1년차 미용사입니다.', 'NAIL', '2024-01-14', 1),
       ('공짜로 네일 받으실 분 구합니다.', '서면 피글 미용실에서 무료로 헤어 모델 하실 분 구합니다. 1년차 미용사입니다.', 'NAIL', '2024-01-15', 1),
       ('예쁘게 머리 하고 가세요~', '서면 피글 미용실에서 무료로 헤어 모델 하실 분 구합니다. 1년차 미용사입니다.', 'HAIR', '2024-01-16', 1);

INSERT INTO heart_tb (user_id, post_id)
VALUES (1, 2),
       (1, 3),
       (1, 4),
       (1, 7),
       (1, 8),
       (1, 9),
       (1, 10);

INSERT INTO chat_room_tb (user_id, post_id)
VALUES (2, 1),
       (2, 1);

INSERT INTO chat_tb (content, chat_room_id, sender_id, is_read, type)
VALUES ('안녕하세요', 1, 2, 0, 'text');