INSERT into user(email,encrypted_password, name, gender, cover_url, birth, introduction)
 values('van@van.com', '$2a$10$OXxpTFlB2WhZK8WVBasRR.OX/jEqMK4JNe7SCqff1VTcIxyq.Wz7q','name' ,'gender', 'coverUrl', 'birth', 'introduction');

INSERT into article(content, is_present, user_id) values('수정될 내용입니다.', 1, 1);
INSERT into article(content, is_present, user_id) values('삭제될 내용입니다.', 1, 1);
INSERT into article(content, is_present, user_id) values('내용입니다.', 1, 1);
INSERT into article(content, is_present, user_id) values('좋아요 여부를 확인할 내용입니다.', 1, 1);

INSERT into comment(content, article_id, is_present, user_id) values('수정될 댓글입니다.', 1, 1, 1);
INSERT into comment(content, article_id, is_present, user_id) values('삭제될 댓글입니다.', 1, 1, 1);
INSERT into comment(content, article_id, is_present, user_id) values('댓글입니다.', 1, 1, 1);
INSERT into comment(content, article_id, is_present, user_id) values('좋아요 여부를 확인할 댓글입니다.', 1, 1, 1);
