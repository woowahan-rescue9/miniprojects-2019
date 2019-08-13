INSERT into user(email, name) values('van@van.com', 'cony');

INSERT into article(content, is_present, user_id) values('수정될 내용입니다.', 1, 1);
INSERT into article(content, is_present, user_id) values('삭제될 내용입니다.', 1, 1);
INSERT into article(content, is_present, user_id) values('내용입니다.', 1, 1);

INSERT into comment(content, article_id, is_present, user_id) values('수정될 댓글입니다.', 1, 1, 1);
INSERT into comment(content, article_id, is_present, user_id) values('삭제될 댓글입니다.', 1, 1, 1);
INSERT into comment(content, article_id, is_present, user_id) values('댓글입니다.', 1, 1, 1);
