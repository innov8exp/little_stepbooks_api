
INSERT INTO STEP_ADMIN_USER (id, username, email, password, nickname, role)
VALUES ('0', 'admin', 'admin@example.com', '{bcrypt}$2a$10$Mauvb3WBioPsOf9hZHX7l.np69XxobcoDn.kOEvcuu6YSafmqgQ6q', '系统管理员', 'ADMIN');

INSERT INTO STEP_USER (id, username, email, password, nickname, role, device_id)
VALUES ('0', 'admin', 'admin@example.com', '{bcrypt}$2a$10$Mauvb3WBioPsOf9hZHX7l.np69XxobcoDn.kOEvcuu6YSafmqgQ6q', '系统管理员', 'ADMIN', 'test_device_id_1');

INSERT INTO STEP_CLASSIFICATION (id, classification_name, min_age, max_age, description)
VALUES ('0', '0-3岁', 0, 3, '适合0-3岁阅读观看');
INSERT INTO STEP_CLASSIFICATION (id, classification_name, min_age, max_age, description)
VALUES ('1', '3-5岁', 3, 5, '适合3-5岁阅读观看');
INSERT INTO STEP_CLASSIFICATION (id, classification_name, min_age, max_age, description)
VALUES ('2', '5-10岁', 5, 10, '适合5-10岁阅读观看');
INSERT INTO STEP_CLASSIFICATION (id, classification_name, min_age, max_age, description)
VALUES ('3', '10岁以上', 10, 99, '适合10岁以上阅读观看');

INSERT INTO STEP_BOOK(id, book_name, author)
VALUES ('0', '我是霸王龙', '宫西达也');

INSERT INTO STEP_BOOK(id, book_name, author)
VALUES ('1', '你看起来好像很好吃', '宫西达也');

INSERT INTO STEP_BOOK(id, book_name, author)
VALUES ('2', '永远永远在一起', '宫西达也');

INSERT INTO STEP_BOOK(id, book_name, author)
VALUES ('3', '温柔的和体贴的', '宫西达也');

INSERT INTO STEP_BOOK(id, book_name, author)
VALUES ('4', '最爱的，是我', '宫西达也');

INSERT INTO STEP_BOOK(id, book_name, author)
VALUES ('5', '数学思维(强化训练1)', '北京出版社');

INSERT INTO STEP_BOOK(id, book_name, author)
VALUES ('6', '数学思维(强化训练2)', '北京出版社');

INSERT INTO STEP_BOOK(id, book_name, author)
VALUES ('7', '睡前小故事(认知篇)', '湖南少年儿童出版社');

INSERT INTO STEP_BOOK(id, book_name, author)
VALUES ('8', '睡前小故事(安全篇)', '湖南少年儿童出版社');

INSERT INTO STEP_BOOK(id, book_name, author)
VALUES ('9', '睡前小故事(生活篇)', '湖南少年儿童出版社');

INSERT INTO STEP_BOOK_CLASSIFICATION_REF(id, book_id, classification_id)
VALUES ('0', '0', '0');
INSERT INTO STEP_BOOK_CLASSIFICATION_REF(id, book_id, classification_id)
VALUES ('1', '0', '1');
INSERT INTO STEP_BOOK_CLASSIFICATION_REF(id, book_id, classification_id)
VALUES ('2', '0', '2');

INSERT INTO STEP_BOOK_CLASSIFICATION_REF(id, book_id, classification_id)
VALUES ('3', '1', '0');
INSERT INTO STEP_BOOK_CLASSIFICATION_REF(id, book_id, classification_id)
VALUES ('4', '1', '1');
INSERT INTO STEP_BOOK_CLASSIFICATION_REF(id, book_id, classification_id)
VALUES ('5', '1', '2');

INSERT INTO STEP_BOOK_CLASSIFICATION_REF(id, book_id, classification_id)
VALUES ('6', '2', '0');
INSERT INTO STEP_BOOK_CLASSIFICATION_REF(id, book_id, classification_id)
VALUES ('7', '2', '1');
INSERT INTO STEP_BOOK_CLASSIFICATION_REF(id, book_id, classification_id)
VALUES ('8', '2', '2');

INSERT INTO STEP_BOOK_CLASSIFICATION_REF(id, book_id, classification_id)
VALUES ('9', '3', '0');
INSERT INTO STEP_BOOK_CLASSIFICATION_REF(id, book_id, classification_id)
VALUES ('10', '3', '1');
INSERT INTO STEP_BOOK_CLASSIFICATION_REF(id, book_id, classification_id)
VALUES ('11', '3', '2');

INSERT INTO STEP_BOOK_CLASSIFICATION_REF(id, book_id, classification_id)
VALUES ('12', '4', '0');
INSERT INTO STEP_BOOK_CLASSIFICATION_REF(id, book_id, classification_id)
VALUES ('13', '4', '1');
INSERT INTO STEP_BOOK_CLASSIFICATION_REF(id, book_id, classification_id)
VALUES ('14', '4', '2');

INSERT INTO STEP_BOOK_CLASSIFICATION_REF(id, book_id, classification_id)
VALUES ('15', '5', '0');

INSERT INTO STEP_BOOK_CLASSIFICATION_REF(id, book_id, classification_id)
VALUES ('16', '6', '0');

INSERT INTO STEP_BOOK_CLASSIFICATION_REF(id, book_id, classification_id)
VALUES ('17', '7', '0');

INSERT INTO STEP_BOOK_CLASSIFICATION_REF(id, book_id, classification_id)
VALUES ('18', '8', '0');

INSERT INTO STEP_BOOK_CLASSIFICATION_REF(id, book_id, classification_id)
VALUES ('19', '9', '0');

INSERT INTO STEP_BOOK_SET(id, code, name)
VALUES ('1', 'bs001', '宫西达也恐龙系列(全5册)');
INSERT INTO STEP_BOOK_SET(id, code, name)
VALUES ('2', 'bs002', '睡前小故事(3册)');
INSERT INTO STEP_BOOK_SET(id, code, name)
VALUES ('3', 'bs003', '数学思维(2册)');

INSERT INTO STEP_BOOK_SET_BOOK_REF(id, book_set_id, book_id)
VALUES ('1', '1', '0');
INSERT INTO STEP_BOOK_SET_BOOK_REF(id, book_set_id, book_id)
VALUES ('2', '1', '1');
INSERT INTO STEP_BOOK_SET_BOOK_REF(id, book_set_id, book_id)
VALUES ('3', '1', '2');
INSERT INTO STEP_BOOK_SET_BOOK_REF(id, book_set_id, book_id)
VALUES ('4', '1', '3');
INSERT INTO STEP_BOOK_SET_BOOK_REF(id, book_set_id, book_id)
VALUES ('5', '1', '4');

INSERT INTO STEP_BOOK_SET_BOOK_REF(id, book_set_id, book_id)
VALUES ('6', '2', '7');
INSERT INTO STEP_BOOK_SET_BOOK_REF(id, book_set_id, book_id)
VALUES ('7', '2', '8');
INSERT INTO STEP_BOOK_SET_BOOK_REF(id, book_set_id, book_id)
VALUES ('8', '2', '9');

INSERT INTO STEP_BOOK_SET_BOOK_REF(id, book_set_id, book_id)
VALUES ('9', '3', '5');
INSERT INTO STEP_BOOK_SET_BOOK_REF(id, book_set_id, book_id)
VALUES ('10', '3', '6');

INSERT INTO STEP_PRODUCT(id, sku_code, sku_name, price, product_nature, book_set_id)
VALUES ('0', '001', '宫西达也恐龙系列(全5册)', 39.00, 'PHYSICAL', '1');
INSERT INTO STEP_PRODUCT(id, sku_code, sku_name, price, product_nature, book_set_id)
VALUES ('1', '002', '宫西达也恐龙系列(全5册)+课程', 29.00, 'PHYSICAL', '1');
INSERT INTO STEP_PRODUCT(id, sku_code, sku_name, price, product_nature, book_set_id)
VALUES ('2', '003', '宫西达也恐龙系列课程', 19.00, 'VIRTUAL', '1');
INSERT INTO STEP_PRODUCT(id, sku_code, sku_name, price, product_nature, book_set_id)
VALUES ('3', '004', '睡前小故事(3册)', 29.00, 'PHYSICAL', '2');
INSERT INTO STEP_PRODUCT(id, sku_code, sku_name, price, product_nature, book_set_id)
VALUES ('4', '005', '睡前小故事(3册)+课程', 19.00, 'PHYSICAL', '2');
INSERT INTO STEP_PRODUCT(id, sku_code, sku_name, price, product_nature, book_set_id)
VALUES ('5', '006', '睡前小故事课程', 9.00, 'VIRTUAL', '2');
INSERT INTO STEP_PRODUCT(id, sku_code, sku_name, price, product_nature, book_set_id)
VALUES ('6', '007', '数学思维(2册)', 29.00, 'PHYSICAL', '3');
INSERT INTO STEP_PRODUCT(id, sku_code, sku_name, price, product_nature, book_set_id)
VALUES ('7', '008', '数学思维(2册)+课程', 19.00, 'PHYSICAL', '3');
INSERT INTO STEP_PRODUCT(id, sku_code, sku_name, price, product_nature, book_set_id)
VALUES ('8', '009', '数学思维课程', 9.00, 'VIRTUAL', '3');

INSERT INTO STEP_INVENTORY(id, product_id, inventory_quantity)
VALUES ('0', '0', 100);
INSERT INTO STEP_INVENTORY(id, product_id, inventory_quantity)
VALUES ('1', '1', 100);

INSERT INTO STEP_INVENTORY(id, product_id, inventory_quantity)
VALUES ('3', '3', 100);
INSERT INTO STEP_INVENTORY(id, product_id, inventory_quantity)
VALUES ('4', '4', 100);

INSERT INTO STEP_INVENTORY(id, product_id, inventory_quantity)
VALUES ('6', '6', 100);
INSERT INTO STEP_INVENTORY(id, product_id, inventory_quantity)
VALUES ('7', '7', 100);

