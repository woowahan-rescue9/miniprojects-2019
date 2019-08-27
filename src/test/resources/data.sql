INSERT into user(email,encrypted_password, name, gender, birth, introduction, PROFILE_IMAGE_NAME, PROFILE_IMAGE_PATH)
 values('van@van.com', '$2a$10$OXxpTFlB2WhZK8WVBasRR.OX/jEqMK4JNe7SCqff1VTcIxyq.Wz7q','name' ,'gender', 'birth', 'introduction','default.png', 'src/test/resources/static/images/user/profile/default.png');

INSERT into article(content, deleted, user_id) values('수정될 내용입니다.', 0, 1);
INSERT into article(content, deleted, user_id) values('삭제될 내용입니다.', 0, 1);
INSERT into article(content, deleted, user_id) values('내용입니다.', 0, 1);
INSERT into article(content, deleted, user_id) values('좋아요 여부를 확인할 내용입니다.', 0, 1);

INSERT into comment(content, article_id, deleted, user_id) values('수정될 댓글입니다.', 1, 0, 1);
INSERT into comment(content, article_id, deleted, user_id) values('삭제될 댓글입니다.', 1, 0, 1);
INSERT into comment(content, article_id, deleted, user_id) values('댓글입니다.', 1, 0, 1);
INSERT into comment(content, article_id, deleted, user_id) values('좋아요 여부를 확인할 댓글입니다.', 1, 0, 1);
