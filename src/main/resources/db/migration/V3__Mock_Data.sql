
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

INSERT INTO STEP_PRODUCT(id, sku_code, sku_name, price, has_inventory)
VALUES ('0', '001', '宫西达也恐龙系列(全5册)', 39.00, 'true');
INSERT INTO STEP_PRODUCT(id, sku_code, sku_name, price, has_inventory)
VALUES ('1', '002', '宫西达也恐龙系列(全5册)+课程', 29.00, 'true');
INSERT INTO STEP_PRODUCT(id, sku_code, sku_name, price, has_inventory)
VALUES ('2', '003', '宫西达也恐龙系列课程', 19.00, 'false');

INSERT INTO STEP_INVENTORY(id, product_id, inventory_quantity)
VALUES ('0', '0', 100);
INSERT INTO STEP_INVENTORY(id, product_id, inventory_quantity)
VALUES ('1', '1', 100);

--
--INSERT INTO STEP_BOOK (id, book_name, author, cover_img, introduction, keywords, content_location, status)
--values ('0', 'Husband Be A Gentleman', 'Smith Lose', 'https://novlshared.s3.ap-northeast-1.amazonaws.com/book_cover_img/1.jpeg',
--'Yan Jing was a poor scholar, he struggled to study for ten years. During that trialling period, Yan Shi Ning’s mother Dung Thi supported him. Yan Jing and Dung Thi had a loving relationship. Unexpectedly after he entered the palace to take the imperial examination, he caught Princess Kang Hua’s attention.Yan Jing faced a difficult dilemma. On one side he loved Dung Thi and on the other side was his high ambition. His final decision was he wanted both and married Princess Kang Hua.',
--'{husband, gentleman}', 'https://novlshared.s3.ap-northeast-1.amazonaws.com/books/Husband+Be+A+Gentleman/chapter1.txt',
--'ONLINE'
--);
--
--INSERT INTO STEP_BOOK (id, book_name, author, cover_img, introduction, keywords, content_location, status)
--values ('1', 'Sheng You Bo Lan', 'Yi Zhasi', 'https://novlshared.s3.ap-northeast-1.amazonaws.com/book_cover_img/2.jpeg',
--'Yan Jing was a poor scholar, he struggled to study for ten years. During that trialling period, Yan Shi Ning’s mother Dung Thi supported him. Yan Jing and Dung Thi had a loving relationship. Unexpectedly after he entered the palace to take the imperial examination, he caught Princess Kang Hua’s attention.Yan Jing faced a difficult dilemma. On one side he loved Dung Thi and on the other side was his high ambition. His final decision was he wanted both and married Princess Kang Hua.',
--'{husband, gentleman}', 'https://novlshared.s3.ap-northeast-1.amazonaws.com/books/Husband+Be+A+Gentleman/chapter2.txt',
--'ONLINE'
--);
--
--INSERT INTO STEP_BOOK (id, book_name, author, cover_img, introduction, keywords, content_location, status)
--values ('2', 'Shi Li Tao Hua', 'Tang', 'https://novlshared.s3.ap-northeast-1.amazonaws.com/book_cover_img/3.jpeg',
--'After Yan Shi Ning’s birth, half of Yan Jing’s heart was in the imperial city and the other half was with Dung Thi. Occasionally he visited his parents and Dung Thi in the country. He missed Dung Thi and wanted to visit more often but Princess Kang Hua was a powerful force and didn’t allow him to visit them often.',
--'{husband, tang}', 'https://novlshared.s3.ap-northeast-1.amazonaws.com/books/Husband+Be+A+Gentleman/chapter3.txt',
--'ONLINE'
--);
--
--INSERT INTO STEP_BOOK (id, book_name, author, cover_img, introduction, keywords, content_location, status)
--values ('3', 'Tian Luo Luan Shi', 'Yi Du Er Guo Tou', 'https://novlshared.s3.ap-northeast-1.amazonaws.com/book_cover_img/4.jpeg',
--'More than ten years past by quickly. Dung Thi worked hard to earn a living and died of pneumonia. When Yan Jing found out Dung Thi died, he cried continuously. Then he brought Yan Shi Ning to the imperial city. That year Yan Shi Ning was only sixteen years old.',
--'{husband, tang}', 'https://novlshared.s3.ap-northeast-1.amazonaws.com/books/Husband+Be+A+Gentleman/chapter3.txt',
--'ONLINE'
--);
--
--
--INSERT INTO STEP_BOOK (id, book_name, author, cover_img, introduction, keywords, content_location, status)
--values ('4', 'Chang An Che', 'Mo Yibai', 'https://novlshared.s3.ap-northeast-1.amazonaws.com/book_cover_img/5.jpeg',
--'Usually young ladies at the age of sixteen were the target of many matchmakers. But Yan Shi Ning’s family background wasn’t ideal. Even though she was the prime minister’s daughter, she didn’t have high hopes for an ideal husband. Prominent men wanted to marry someone who was most compatible with them to elevate their status. In the prime minister’s manor behind Yan Shi Ning was his golden daughter. The golden daughter’s mother was a princess and the princess was also the empress’ blood sister.',
--'{husband, tang}', 'https://novlshared.s3.ap-northeast-1.amazonaws.com/books/Husband+Be+A+Gentleman/chapter3.txt',
--'ONLINE'
--);
--
--INSERT INTO STEP_BOOK (id, book_name, author, cover_img, introduction, keywords, content_location, status)
--values ('5', 'Sheng Shi Chang An', 'Yu Ye Fei Ran', 'https://novlshared.s3.ap-northeast-1.amazonaws.com/book_cover_img/6.jpeg',
--'Compared to Princess Kang Hua’s daughter, Yan Shi Ning didn’t have any powerful family connections and Yan Shi Ning was alone in the world. It was the main reason why many court officials didn’t want to marry Yan Shi Ning. Their eyes focused solely on the prime minister’s golden daughter.',
--'{husband, tang}', 'https://novlshared.s3.ap-northeast-1.amazonaws.com/books/Husband+Be+A+Gentleman/chapter3.txt',
--'ONLINE'
--);
--
--INSERT INTO STEP_BOOK (id, book_name, author, cover_img, introduction, keywords, content_location, status)
--values ('6', 'Ting Hao De', 'Tang', 'https://novlshared.s3.ap-northeast-1.amazonaws.com/book_cover_img/7.jpeg',
--'For two years Yan Shi Ning stayed in an endless restless state in the imperial city. No suitors wanted to marry her. When she was free she thought about the two consequences of not having suitors. One, soon she would past the desirable marriage age to start a family then it was logical that the ground below was forced to look at what was above. Second, her father stood behind Princess Kang Hua to support the princess, and Yan Shi Ning’s marriage state wasn’t an exception.',
--'{husband, tang}', 'https://novlshared.s3.ap-northeast-1.amazonaws.com/books/Husband+Be+A+Gentleman/chapter3.txt',
--'ONLINE'
--);
--
--
--INSERT INTO STEP_BOOK (id, book_name, author, cover_img, introduction, keywords, content_location, status)
--values ('7', 'Mei You Ming Zi', 'Tang', 'https://novlshared.s3.ap-northeast-1.amazonaws.com/book_cover_img/8.jpeg',
--'Although Yan Shi Nang aged as each day past, she didn’t care if she became an old maid. When other people pitied her, she smiled and silently maintained her gentle and docile image.',
--'{husband, tang}', 'https://novlshared.s3.ap-northeast-1.amazonaws.com/books/Husband+Be+A+Gentleman/chapter3.txt',
--'ONLINE'
--);
--
--
--INSERT INTO STEP_BOOKSHELF (id, book_id, sort_index, user_id)
--VALUES ('1', '0', 1, '0');
--
--INSERT INTO STEP_BOOKSHELF (id, book_id, sort_index, user_id)
--VALUES ('2', '1', 2, '0');
--
--INSERT INTO STEP_BOOKSHELF (id, book_id, sort_index, user_id)
--VALUES ('3', '2', 3, '0');
--
--INSERT INTO STEP_BOOKSHELF (id, book_id, sort_index, user_id)
--VALUES ('4', '3', 4, '0');
--
--INSERT INTO STEP_BOOKSHELF (id, book_id, sort_index, user_id)
--VALUES ('5', '4', 5, '0');
--
--INSERT INTO STEP_BOOKSHELF (id, book_id, sort_index, user_id)
--VALUES ('6', '5', 6, '0');
--
--INSERT INTO STEP_FAVORITE (id, book_id, sort_index, user_id)
--VALUES ('1', '5', 1, '0');
--
--INSERT INTO STEP_FAVORITE (id, book_id, sort_index, user_id)
--VALUES ('2', '7', 2, '0');
--
--
--
--
--
----book0
--INSERT INTO STEP_CHAPTER (id, chapter_number, chapter_name, introduction, book_id, content_link)
--VALUES ('0', 1, 'Chapter One', 'Chapter One', '0',
--'https://novlshared.s3.ap-northeast-1.amazonaws.com/books/Husband+Be+A+Gentleman/chapter1.txt');
--
----book1
--INSERT INTO STEP_CHAPTER (id, chapter_number, chapter_name, introduction, book_id, content_link)
--VALUES ('1', 1, 'Chapter One', 'Chapter One', '1',
--'https://novlshared.s3.ap-northeast-1.amazonaws.com/books/Husband+Be+A+Gentleman/chapter1.txt');
--
--
--INSERT INTO STEP_CHAPTER (id, chapter_number, chapter_name, introduction, book_id, content_link)
--VALUES ('2', 2, 'Chapter Two(Part 1)', 'Chapter Two(Part 1)', '1',
--'https://novlshared.s3.ap-northeast-1.amazonaws.com/books/Husband+Be+A+Gentleman/chapter2.txt');
--
--
--INSERT INTO STEP_CHAPTER (id, chapter_number, chapter_name, introduction, book_id, content_link)
--VALUES ('3', 3, 'Chapter Two(Part 2)', 'Chapter Two(Part 2)', '1',
--'https://novlshared.s3.ap-northeast-1.amazonaws.com/books/Husband+Be+A+Gentleman/chapter3.txt');
--
----book2
--INSERT INTO STEP_CHAPTER (id, chapter_number, chapter_name, introduction, book_id, content_link)
--VALUES ('4', 1, 'Chapter One', 'Chapter One', '2',
--'https://novlshared.s3.ap-northeast-1.amazonaws.com/books/Husband+Be+A+Gentleman/chapter1.txt');
--
--
--INSERT INTO STEP_CHAPTER (id, chapter_number, chapter_name, introduction, book_id, content_link)
--VALUES ('5', 2, 'Chapter Two(Part 1)', 'Chapter Two(Part 1)', '2',
--'https://novlshared.s3.ap-northeast-1.amazonaws.com/books/Husband+Be+A+Gentleman/chapter2.txt');
--
--
--INSERT INTO STEP_CHAPTER (id, chapter_number, chapter_name, introduction, book_id, content_link)
--VALUES ('6', 3, 'Chapter Two(Part 2)', 'Chapter Two(Part 2)', '2',
--'https://novlshared.s3.ap-northeast-1.amazonaws.com/books/Husband+Be+A+Gentleman/chapter3.txt');
--
----book3
--INSERT INTO STEP_CHAPTER (id, chapter_number, chapter_name, introduction, book_id, content_link)
--VALUES ('7', 1, 'Chapter One', 'Chapter One', '3',
--'https://novlshared.s3.ap-northeast-1.amazonaws.com/books/Husband+Be+A+Gentleman/chapter1.txt');
--
--
--INSERT INTO STEP_CHAPTER (id, chapter_number, chapter_name, introduction, book_id, content_link)
--VALUES ('8', 2, 'Chapter Two(Part 1)', 'Chapter Two(Part 1)', '3',
--'https://novlshared.s3.ap-northeast-1.amazonaws.com/books/Husband+Be+A+Gentleman/chapter2.txt');
--
--
--INSERT INTO STEP_CHAPTER (id, chapter_number, chapter_name, introduction, book_id, content_link)
--VALUES ('9', 3, 'Chapter Two(Part 2)', 'Chapter Two(Part 2)', '3',
--'https://novlshared.s3.ap-northeast-1.amazonaws.com/books/Husband+Be+A+Gentleman/chapter3.txt');
--
----book4
--INSERT INTO STEP_CHAPTER (id, chapter_number, chapter_name, introduction, book_id, content_link)
--VALUES ('10', 1, 'Chapter One', 'Chapter One', '4',
--'https://novlshared.s3.ap-northeast-1.amazonaws.com/books/Husband+Be+A+Gentleman/chapter1.txt');
--
--
----book5
--INSERT INTO STEP_CHAPTER (id, chapter_number, chapter_name, introduction, book_id, content_link)
--VALUES ('11', 1, 'Chapter One', 'Chapter One', '5',
--'https://novlshared.s3.ap-northeast-1.amazonaws.com/books/Husband+Be+A+Gentleman/chapter1.txt');
--
--
----book6
--INSERT INTO STEP_CHAPTER (id, chapter_number, chapter_name, introduction, book_id, content_link)
--VALUES ('12', 1, 'Chapter One', 'Chapter One', '6',
--'https://novlshared.s3.ap-northeast-1.amazonaws.com/books/Husband+Be+A+Gentleman/chapter1.txt');
--
--
----book7
--INSERT INTO STEP_CHAPTER (id, chapter_number, chapter_name, introduction, book_id, content_link)
--VALUES ('13', 1, 'Chapter One', 'Chapter One', '7',
--'https://novlshared.s3.ap-northeast-1.amazonaws.com/books/Husband+Be+A+Gentleman/chapter1.txt');
--
--
--INSERT INTO STEP_CATEGORY (id, category_name) VALUES ('1', 'Romance');
--INSERT INTO STEP_CATEGORY (id, category_name) VALUES ('2', 'Campus');
--INSERT INTO STEP_CATEGORY (id, category_name) VALUES ('3', 'President');
--INSERT INTO STEP_CATEGORY (id, category_name) VALUES ('4', 'History');
--INSERT INTO STEP_CATEGORY (id, category_name) VALUES ('5', 'Humor');
--
--INSERT INTO STEP_BOOK_CATEGORY_REF (id, book_id, category_id) VALUES ('1', '1', '1');
--INSERT INTO STEP_BOOK_CATEGORY_REF (id, book_id, category_id) VALUES ('2', '2', '1');
--INSERT INTO STEP_BOOK_CATEGORY_REF (id, book_id, category_id) VALUES ('3', '3', '1');
--INSERT INTO STEP_BOOK_CATEGORY_REF (id, book_id, category_id) VALUES ('4', '4', '1');
--INSERT INTO STEP_BOOK_CATEGORY_REF (id, book_id, category_id) VALUES ('5', '5', '1');
--INSERT INTO STEP_BOOK_CATEGORY_REF (id, book_id, category_id) VALUES ('6', '6', '1');
--
--INSERT INTO STEP_BOOK_CATEGORY_REF (id, book_id, category_id) VALUES ('7', '0', '2');
--INSERT INTO STEP_BOOK_CATEGORY_REF (id, book_id, category_id) VALUES ('8', '2', '2');
--INSERT INTO STEP_BOOK_CATEGORY_REF (id, book_id, category_id) VALUES ('9', '3', '2');
--INSERT INTO STEP_BOOK_CATEGORY_REF (id, book_id, category_id) VALUES ('10', '6', '2');
--
--INSERT INTO STEP_BOOK_CATEGORY_REF (id, book_id, category_id) VALUES ('11', '7', '3');
--INSERT INTO STEP_BOOK_CATEGORY_REF (id, book_id, category_id) VALUES ('12', '3', '3');
--INSERT INTO STEP_BOOK_CATEGORY_REF (id, book_id, category_id) VALUES ('13', '5', '3');
--INSERT INTO STEP_BOOK_CATEGORY_REF (id, book_id, category_id) VALUES ('14', '6', '3');
--
--INSERT INTO STEP_BOOK_CATEGORY_REF (id, book_id, category_id) VALUES ('15', '1', '4');
--INSERT INTO STEP_BOOK_CATEGORY_REF (id, book_id, category_id) VALUES ('16', '2', '4');
--INSERT INTO STEP_BOOK_CATEGORY_REF (id, book_id, category_id) VALUES ('17', '3', '4');
--INSERT INTO STEP_BOOK_CATEGORY_REF (id, book_id, category_id) VALUES ('18', '4', '4');
--INSERT INTO STEP_BOOK_CATEGORY_REF (id, book_id, category_id) VALUES ('19', '5', '4');
--INSERT INTO STEP_BOOK_CATEGORY_REF (id, book_id, category_id) VALUES ('20', '6', '4');
--
--INSERT INTO STEP_BOOK_CATEGORY_REF (id, book_id, category_id) VALUES ('21', '0', '5');
--INSERT INTO STEP_BOOK_CATEGORY_REF (id, book_id, category_id) VALUES ('22', '2', '5');
--INSERT INTO STEP_BOOK_CATEGORY_REF (id, book_id, category_id) VALUES ('23', '3', '5');
--INSERT INTO STEP_BOOK_CATEGORY_REF (id, book_id, category_id) VALUES ('24', '4', '5');
--INSERT INTO STEP_BOOK_CATEGORY_REF (id, book_id, category_id) VALUES ('25', '5', '5');
--INSERT INTO STEP_BOOK_CATEGORY_REF (id, book_id, category_id) VALUES ('26', '1', '5');
--
--INSERT INTO STEP_RATING (id, rating, book_id, user_id) VALUES ('1', 4, '0', '0');
--INSERT INTO STEP_RATING (id, rating, book_id, user_id) VALUES ('2', 2, '1', '0');
--INSERT INTO STEP_RATING (id, rating, book_id, user_id) VALUES ('3', 3, '2', '0');
--INSERT INTO STEP_RATING (id, rating, book_id, user_id) VALUES ('4', 5, '3', '0');
--INSERT INTO STEP_RATING (id, rating, book_id, user_id) VALUES ('5', 1, '4', '0');
--
--
--INSERT INTO STEP_VIEW_HISTORY (id, book_id, user_id) VALUES ('1', '0', '0');
--INSERT INTO STEP_VIEW_HISTORY (id, book_id, user_id) VALUES ('6', '1', '0');
--INSERT INTO STEP_VIEW_HISTORY (id, book_id, user_id) VALUES ('7', '2', '0');
--INSERT INTO STEP_VIEW_HISTORY (id, book_id, user_id) VALUES ('11', '4', '0');
--INSERT INTO STEP_VIEW_HISTORY (id, book_id, user_id) VALUES ('12', '5', '0');