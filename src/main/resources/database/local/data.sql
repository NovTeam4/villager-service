INSERT INTO MEMBER (email, is_deleted, encoded_password)
VALUES('test@gmail.com', false, '$2a$10$Q4mVM5CMPDWI.ehCzL2wB.KvXVA6sG0UlKyEQPPQFYf1d7GawoyZa');

INSERT INTO MEMBER_DETAIL(member_id, nickname)
VALUES(1, 'hello');

INSERT INTO CATEGORY(name, is_visible)
VALUES('일반', true);

INSERT INTO CATEGORY(name, is_visible)
VALUES('모임 자랑글', true);

INSERT INTO comment_template(is_visible, template_content)
VALUES(TRUE,'너무 즐거웠습니다. 다음에 또 놀아요.');

INSERT INTO comment_template(is_visible, template_content)
VALUES(TRUE,'저는..별로였어요. 즐겁지 않았습니다.');

INSERT INTO comment_template(is_visible, template_content)
VALUES(FALSE,'재미게 놀다갑니다.');

