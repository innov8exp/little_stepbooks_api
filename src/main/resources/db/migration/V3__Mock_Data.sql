
INSERT INTO STEP_ADMIN_USER (id, username, email, password, nickname, role)
VALUES ('0', 'admin', 'admin@example.com', '{bcrypt}$2a$10$Mauvb3WBioPsOf9hZHX7l.np69XxobcoDn.kOEvcuu6YSafmqgQ6q', '系统管理员', 'ADMIN');

INSERT INTO STEP_USER (id, username, email, password, nickname, phone, role)
VALUES ('0', 'admin', 'admin@example.com', '{bcrypt}$2a$10$Mauvb3WBioPsOf9hZHX7l.np69XxobcoDn.kOEvcuu6YSafmqgQ6q', '系统管理员', '133567890897', 'NORMAL_USER');

INSERT INTO STEP_CLASSIFICATION (id, classification_name, min_age, max_age, description)
VALUES ('0', '0-3岁', 0, 3, '适合0-3岁阅读观看');
INSERT INTO STEP_CLASSIFICATION (id, classification_name, min_age, max_age, description)
VALUES ('1', '3-5岁', 3, 5, '适合3-5岁阅读观看');
INSERT INTO STEP_CLASSIFICATION (id, classification_name, min_age, max_age, description)
VALUES ('2', '5-10岁', 5, 10, '适合5-10岁阅读观看');
INSERT INTO STEP_CLASSIFICATION (id, classification_name, min_age, max_age, description)
VALUES ('3', '10岁以上', 10, 99, '适合10岁以上阅读观看');

-- 书籍配图
INSERT INTO STEP_MEDIA(id, file_name, access_permission, asset_domain, object_name, object_key, bucket_name, store_path, object_url)
VALUES ('0', 'test2.jpg', 'PUBLIC', 'BOOK', '041961e3-25b6-4d29-a6a6-60a69adb5185.jpg', 'book/041961e3-25b6-4d29-a6a6-60a69adb5185.jpg', 'stepbook.public', 'book/', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/book/041961e3-25b6-4d29-a6a6-60a69adb5185.jpg');
INSERT INTO STEP_MEDIA(id, file_name, access_permission, asset_domain, object_name, object_key, bucket_name, store_path, object_url)
VALUES ('1', 'test1.jpg', 'PUBLIC', 'BOOK', '53f36f41-7780-4625-8e95-ab40e3fffacc.png', 'book/53f36f41-7780-4625-8e95-ab40e3fffacc.png', 'stepbook.public', 'book/', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/book/53f36f41-7780-4625-8e95-ab40e3fffacc.png');
INSERT INTO STEP_MEDIA(id, file_name, access_permission, asset_domain, object_name, object_key, bucket_name, store_path, object_url)
VALUES ('2', 'test.jpg', 'PUBLIC', 'BOOK', 'be4dd540-2caf-437a-a4f0-d1b0e73d4784.jpg', 'book/be4dd540-2caf-437a-a4f0-d1b0e73d4784.jpg', 'stepbook.public', 'book/', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/book/be4dd540-2caf-437a-a4f0-d1b0e73d4784.jpg');
INSERT INTO STEP_MEDIA(id, file_name, access_permission, asset_domain, object_name, object_key, bucket_name, store_path, object_url)
VALUES ('3', 'WX20231124-231114@2x.png', 'PUBLIC', 'BOOK', 'efaff571-8ab2-4ac0-8732-dd7f97fcda75.png', 'book/efaff571-8ab2-4ac0-8732-dd7f97fcda75.png', 'stepbook.public', 'book/', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/book/efaff571-8ab2-4ac0-8732-dd7f97fcda75.png');
INSERT INTO STEP_MEDIA(id, file_name, access_permission, asset_domain, object_name, object_key, bucket_name, store_path, object_url)
VALUES ('4', 'color_bg__gluvmsph8ymy_large_2x.jpg', 'PUBLIC', 'BOOK', '8c4b408f-46de-47b2-9e50-fe7212846a2f.jpg', 'book/8c4b408f-46de-47b2-9e50-fe7212846a2f.jpg', 'stepbook.public', 'book/', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/book/8c4b408f-46de-47b2-9e50-fe7212846a2f.jpg');

-- 内容配图，音频和课程
INSERT INTO STEP_MEDIA(id, file_name, access_permission, asset_domain, object_name, object_key, bucket_name, store_path, object_url)
VALUES ('00', 'Astronomia-Vicetone_Tony_Igy.mp3', 'PRIVATE', 'BOOK', 'book/c4c36839-11ae-4a4d-b5d4-18bc0e7c3446.mp3', 'book/c4c36839-11ae-4a4d-b5d4-18bc0e7c3446.mp3', 'stepbook.stage', 'book/', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.stage/book/c4c36839-11ae-4a4d-b5d4-18bc0e7c3446.mp3');
INSERT INTO STEP_MEDIA(id, file_name, access_permission, asset_domain, object_name, object_key, bucket_name, store_path, object_url)
VALUES ('01', 'Alone-Alan_Walker.mp3', 'PRIVATE', 'BOOK', 'e614f8e5-f4f8-4d04-90e2-a88b363e2426.mp3', 'book/e614f8e5-f4f8-4d04-90e2-a88b363e2426.mp3', 'stepbook.stage', 'book/', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.stage/book/e614f8e5-f4f8-4d04-90e2-a88b363e2426.mp3');
INSERT INTO STEP_MEDIA(id, file_name, access_permission, asset_domain, object_name, object_key, bucket_name, store_path, object_url)
VALUES ('02', '4.mp4', 'PRIVATE', 'COURSE', '60e58a1a-687c-40fa-9e12-a780480f2898.mp4', 'course/60e58a1a-687c-40fa-9e12-a780480f2898.mp4', 'stepbook.stage', 'course/', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.stage/course/60e58a1a-687c-40fa-9e12-a780480f2898.mp4');
INSERT INTO STEP_MEDIA(id, file_name, access_permission, asset_domain, object_name, object_key, bucket_name, store_path, object_url)
VALUES ('03', 'large_2x.mp4', 'PRIVATE', 'COURSE', 'd25f4648-4650-4dc7-b52a-3521da510519.mp4', 'course/d25f4648-4650-4dc7-b52a-3521da510519.mp4', 'stepbook.stage', 'course/', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.stage/course/d25f4648-4650-4dc7-b52a-3521da510519.mp4');
INSERT INTO STEP_MEDIA(id, file_name, access_permission, asset_domain, object_name, object_key, bucket_name, store_path, object_url)
VALUES ('04', 'WX20231124-231217@2x.png', 'PRIVATE', 'BOOK', 'f47d587a-696a-4f05-85e1-d14c24a7b917.png', 'book/f47d587a-696a-4f05-85e1-d14c24a7b917.png', 'stepbook.stage', 'book/', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.stage/book/f47d587a-696a-4f05-85e1-d14c24a7b917.png');
INSERT INTO STEP_MEDIA(id, file_name, access_permission, asset_domain, object_name, object_key, bucket_name, store_path, object_url)
VALUES ('05', 'test2.jpg', 'PRIVATE', 'BOOK', 'd2ccd3d4-17d8-45d7-be84-45bcfe63778c.jpg', 'book/d2ccd3d4-17d8-45d7-be84-45bcfe63778c.jpg', 'stepbook.stage', 'book/', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.stage/book/d2ccd3d4-17d8-45d7-be84-45bcfe63778c.jpg');

-- 产品配图
INSERT INTO STEP_MEDIA(id, file_name, access_permission, asset_domain, object_name, object_key, bucket_name, store_path, object_url)
VALUES ('001', 'test.jpg', 'PUBLIC', 'PRODUCT', '981ec323-12d1-4088-a1ba-461ac0de89e8.jpg', 'product/981ec323-12d1-4088-a1ba-461ac0de89e8.jpg', 'stepbook.public', 'product/', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/product/981ec323-12d1-4088-a1ba-461ac0de89e8.jpg');
INSERT INTO STEP_MEDIA(id, file_name, access_permission, asset_domain, object_name, object_key, bucket_name, store_path, object_url)
VALUES ('002', 'test1.jpg', 'PUBLIC', 'PRODUCT', 'fc8b7ed7-2b53-49be-8b4d-cd4c2d9ff15a.jpg', 'product/fc8b7ed7-2b53-49be-8b4d-cd4c2d9ff15a.jpg', 'stepbook.public', 'product/', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/product/fc8b7ed7-2b53-49be-8b4d-cd4c2d9ff15a.jpg');
INSERT INTO STEP_MEDIA(id, file_name, access_permission, asset_domain, object_name, object_key, bucket_name, store_path, object_url)
VALUES ('003', 'test2.jpg', 'PUBLIC', 'PRODUCT', 'd86913e9-3a44-4b29-9403-bd16ac2b8584.jpg', 'product/d86913e9-3a44-4b29-9403-bd16ac2b8584.jpg', 'stepbook.public', 'product/', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/product/d86913e9-3a44-4b29-9403-bd16ac2b8584.jpg');
INSERT INTO STEP_MEDIA(id, file_name, access_permission, asset_domain, object_name, object_key, bucket_name, store_path, object_url)
VALUES ('004', 'test3.png', 'PUBLIC', 'PRODUCT', '57e70bc5-7967-4229-b147-c76a21ecd262.png', 'product/57e70bc5-7967-4229-b147-c76a21ecd262.png', 'stepbook.public', 'product/', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/product/57e70bc5-7967-4229-b147-c76a21ecd262.png');
INSERT INTO STEP_MEDIA(id, file_name, access_permission, asset_domain, object_name, object_key, bucket_name, store_path, object_url)
VALUES ('005', 'test4.jpg', 'PUBLIC', 'PRODUCT', 'e0ff2c40-fab6-4be0-b549-f1cc79aa78a8.jpg', 'product/e0ff2c40-fab6-4be0-b549-f1cc79aa78a8.jpg', 'stepbook.public', 'product/', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/product/e0ff2c40-fab6-4be0-b549-f1cc79aa78a8.jpg');
INSERT INTO STEP_MEDIA(id, file_name, access_permission, asset_domain, object_name, object_key, bucket_name, store_path, object_url)
VALUES ('006', 'test5.jpg', 'PUBLIC', 'PRODUCT', 'ee248eac-b3f6-4953-bcad-271cb6e52653.jpg', 'product/ee248eac-b3f6-4953-bcad-271cb6e52653.jpg', 'stepbook.public', 'product/', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/product/ee248eac-b3f6-4953-bcad-271cb6e52653.jpg');

-- 广告配图
INSERT INTO STEP_MEDIA(id, file_name, access_permission, asset_domain, object_name, object_key, bucket_name, store_path, object_url)
VALUES ('0001', 'WX20231124-231114@2x.png', 'PUBLIC', 'ADVERTISEMENT', '25ea0798-7f0a-4064-af71-c74654e40f1a.png', 'advertisement/25ea0798-7f0a-4064-af71-c74654e40f1a.png', 'stepbook.public', 'advertisement/', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/advertisement/25ea0798-7f0a-4064-af71-c74654e40f1a.png');
INSERT INTO STEP_MEDIA(id, file_name, access_permission, asset_domain, object_name, object_key, bucket_name, store_path, object_url)
VALUES ('0002', 'WX20231124-231217@2x.png', 'PUBLIC', 'ADVERTISEMENT', 'c92ac67f-efe7-42bf-bb3f-3aae6efb5b71.png', 'advertisement/c92ac67f-efe7-42bf-bb3f-3aae6efb5b71.png', 'stepbook.public', 'advertisement/', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/advertisement/c92ac67f-efe7-42bf-bb3f-3aae6efb5b71.png');
INSERT INTO STEP_MEDIA(id, file_name, access_permission, asset_domain, object_name, object_key, bucket_name, store_path, object_url)
VALUES ('0003', 'WX20231124-231302@2x.png', 'PUBLIC', 'ADVERTISEMENT', '98b444ef-4da8-4f3c-ab8a-d4bb1c06c511.png', 'advertisement/98b444ef-4da8-4f3c-ab8a-d4bb1c06c511.png', 'stepbook.public', 'advertisement/', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/advertisement/98b444ef-4da8-4f3c-ab8a-d4bb1c06c511.png');


INSERT INTO STEP_BOOK(id, book_name, description, author, book_img_id, book_img_url, duration)
VALUES ('0', '我是霸王龙', '我是霸王龙', '宫西达也', '0', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/book/041961e3-25b6-4d29-a6a6-60a69adb5185.jpg', '03:00');
INSERT INTO STEP_BOOK(id, book_name, description, author, book_img_id, book_img_url, duration)
VALUES ('1', '你看起来好像很好吃', '你看起来好像很好吃', '宫西达也', '1', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/book/53f36f41-7780-4625-8e95-ab40e3fffacc.png', '03:00');
INSERT INTO STEP_BOOK(id, book_name,description, author, book_img_id, book_img_url, duration)
VALUES ('2', '永远永远在一起', '永远永远在一起', '宫西达也', '2', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/book/be4dd540-2caf-437a-a4f0-d1b0e73d4784.jpg', '03:00');
INSERT INTO STEP_BOOK(id, book_name,description, author, book_img_id, book_img_url, duration)
VALUES ('3', '温柔的和体贴的','温柔的和体贴的', '宫西达也', '3', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/book/efaff571-8ab2-4ac0-8732-dd7f97fcda75.png', '03:00');
INSERT INTO STEP_BOOK(id, book_name,description, author, book_img_id, book_img_url, duration)
VALUES ('4', '最爱的，是我','最爱的，是我', '宫西达也', '4', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/book/8c4b408f-46de-47b2-9e50-fe7212846a2f.jpg', '03:00');
INSERT INTO STEP_BOOK(id, book_name,description, author, book_img_id, book_img_url, duration)
VALUES ('5', '数学思维(强化训练1)','数学思维(强化训练1)', '北京出版社', '1', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/book/53f36f41-7780-4625-8e95-ab40e3fffacc.png', '03:00');
INSERT INTO STEP_BOOK(id, book_name,description, author, book_img_id, book_img_url, duration)
VALUES ('6', '数学思维(强化训练2)','数学思维(强化训练2)', '北京出版社', '2', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/book/be4dd540-2caf-437a-a4f0-d1b0e73d4784.jpg', '03:00');
INSERT INTO STEP_BOOK(id, book_name,description, author, book_img_id, book_img_url, duration)
VALUES ('7', '睡前小故事(认知篇)','睡前小故事(认知篇)', '湖南少年儿童出版社', '1', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/book/53f36f41-7780-4625-8e95-ab40e3fffacc.png', '03:00');
INSERT INTO STEP_BOOK(id, book_name,description, author, book_img_id, book_img_url, duration)
VALUES ('8', '睡前小故事(安全篇)','睡前小故事(安全篇)', '湖南少年儿童出版社', '2', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/book/be4dd540-2caf-437a-a4f0-d1b0e73d4784.jpg', '03:00');
INSERT INTO STEP_BOOK(id, book_name,description, author, book_img_id, book_img_url, duration)
VALUES ('9', '睡前小故事(生活篇)','睡前小故事(生活篇)', '湖南少年儿童出版社', '3', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/book/efaff571-8ab2-4ac0-8732-dd7f97fcda75.png', '03:00');

INSERT INTO STEP_BOOK_CHAPTER(id, book_id, chapter_no, chapter_name, img_id, audio_id)
VALUES ('0', '0', '0', '第一页', '04', '00');
INSERT INTO STEP_BOOK_CHAPTER(id, book_id, chapter_no, chapter_name, img_id, audio_id)
VALUES ('1', '0', '1', '第二页', '05', '01');
INSERT INTO STEP_BOOK_CHAPTER(id, book_id, chapter_no, chapter_name, img_id, audio_id)
VALUES ('2', '0', '2', '第三页', '04', '00');
INSERT INTO STEP_BOOK_CHAPTER(id, book_id, chapter_no, chapter_name, img_id, audio_id)
VALUES ('3', '0', '3', '第四页', '05', '01');
INSERT INTO STEP_BOOK_CHAPTER(id, book_id, chapter_no, chapter_name, img_id, audio_id)
VALUES ('4', '1', '1', '第一页', '04', '00');
INSERT INTO STEP_BOOK_CHAPTER(id, book_id, chapter_no, chapter_name, img_id, audio_id)
VALUES ('5', '1', '2', '第二页', '05', '01');
INSERT INTO STEP_BOOK_CHAPTER(id, book_id, chapter_no, chapter_name, img_id, audio_id)
VALUES ('6', '1', '3', '第三页', '04', '00');
INSERT INTO STEP_BOOK_CHAPTER(id, book_id, chapter_no, chapter_name, img_id, audio_id)
VALUES ('7', '1', '4', '第四页', '05', '01');

INSERT INTO STEP_COURSE(id, book_id, name, author, duration, cover_img_id, cover_img_url, video_id, course_nature)
VALUES ('0', '0', '霸王龙试听', '宫西达也', '03:00', '0', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/book/041961e3-25b6-4d29-a6a6-60a69adb5185.jpg', '02', 'TRIAL');
INSERT INTO STEP_COURSE(id, book_id, name, author, duration, cover_img_id, cover_img_url, video_id, course_nature)
VALUES ('1', '0', '霸王龙精讲', '宫西达也', '03:00', '1', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/book/53f36f41-7780-4625-8e95-ab40e3fffacc.png', '03', 'NEED_TO_PAY');
INSERT INTO STEP_COURSE(id, book_id, name, author, duration, cover_img_id, cover_img_url, video_id, course_nature)
VALUES ('2', '1', '你看起来好像很好吃试听', '宫西达也', '03:00', '2', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/book/be4dd540-2caf-437a-a4f0-d1b0e73d4784.jpg', '03', 'TRIAL');
INSERT INTO STEP_COURSE(id, book_id, name, author, duration, cover_img_id, cover_img_url, video_id, course_nature)
VALUES ('3', '1', '你看起来好像很好吃精讲', '宫西达也', '03:00', '3', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/book/efaff571-8ab2-4ac0-8732-dd7f97fcda75.png', '02', 'NEED_TO_PAY');

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

INSERT INTO STEP_PRODUCT(id, sku_code, sku_name, price, product_nature, book_set_id, cover_img_id, cover_img_url, status, materials, sales_platforms)
VALUES ('0', '001', '宫西达也恐龙系列(全5册)', 39.00, 'PHYSICAL', '1', '001', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/product/981ec323-12d1-4088-a1ba-461ac0de89e8.jpg', 'ON_SHELF', 1, 1);
INSERT INTO STEP_PRODUCT(id, sku_code, sku_name, price, product_nature, book_set_id, cover_img_id, cover_img_url, status, materials, sales_platforms)
VALUES ('1', '002', '宫西达也恐龙系列(全5册)+课程', 29.00, 'PHYSICAL', '1', '002', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/product/fc8b7ed7-2b53-49be-8b4d-cd4c2d9ff15a.jpg', 'ON_SHELF', 3, 1);
INSERT INTO STEP_PRODUCT(id, sku_code, sku_name, price, product_nature, book_set_id, cover_img_id, cover_img_url, status, materials, sales_platforms)
VALUES ('2', '003', '宫西达也恐龙系列课程', 19.00, 'VIRTUAL', '1', '003', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/product/d86913e9-3a44-4b29-9403-bd16ac2b8584.jpg', 'ON_SHELF', 4, 1);
INSERT INTO STEP_PRODUCT(id, sku_code, sku_name, price, product_nature, book_set_id, cover_img_id, cover_img_url, status, materials, sales_platforms)
VALUES ('3', '004', '睡前小故事(3册)', 29.00, 'PHYSICAL', '2', '004', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/product/d86913e9-3a44-4b29-9403-bd16ac2b8584.jpg', 'ON_SHELF', 1, 1);
INSERT INTO STEP_PRODUCT(id, sku_code, sku_name, price, product_nature, book_set_id, cover_img_id, cover_img_url, status, materials, sales_platforms)
VALUES ('4', '005', '睡前小故事(3册)+课程', 19.00, 'PHYSICAL', '2', '005', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/product/e0ff2c40-fab6-4be0-b549-f1cc79aa78a8.jpg', 'ON_SHELF', 3, 1);
INSERT INTO STEP_PRODUCT(id, sku_code, sku_name, price, product_nature, book_set_id, cover_img_id, cover_img_url, status, materials, sales_platforms)
VALUES ('5', '006', '睡前小故事课程', 9.00, 'VIRTUAL', '2', '006', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/product/ee248eac-b3f6-4953-bcad-271cb6e52653.jpg', 'ON_SHELF', 4, 1);
INSERT INTO STEP_PRODUCT(id, sku_code, sku_name, price, product_nature, book_set_id, cover_img_id, cover_img_url, status, materials, sales_platforms)
VALUES ('6', '007', '数学思维(2册)', 29.00, 'PHYSICAL', '3', '001', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/product/981ec323-12d1-4088-a1ba-461ac0de89e8.jpg', 'ON_SHELF', 1, 1);
INSERT INTO STEP_PRODUCT(id, sku_code, sku_name, price, product_nature, book_set_id, cover_img_id, cover_img_url, status, materials, sales_platforms)
VALUES ('7', '008', '数学思维(2册)+课程', 19.00, 'PHYSICAL', '3', '002', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/product/fc8b7ed7-2b53-49be-8b4d-cd4c2d9ff15a.jpg', 'ON_SHELF', 3, 1);
INSERT INTO STEP_PRODUCT(id, sku_code, sku_name, price, product_nature, book_set_id, cover_img_id, cover_img_url, status, materials, sales_platforms)
VALUES ('8', '009', '数学思维课程', 9.00, 'VIRTUAL', '3', '003', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/product/d86913e9-3a44-4b29-9403-bd16ac2b8584.jpg', 'ON_SHELF', 4, 1);

INSERT INTO STEP_PRODUCT_BOOK_REF(id, product_id, book_id)
VALUES ('0', '0', '0');
INSERT INTO STEP_PRODUCT_BOOK_REF(id, product_id, book_id)
VALUES ('1', '0', '1');
INSERT INTO STEP_PRODUCT_BOOK_REF(id, product_id, book_id)
VALUES ('2', '0', '2');
INSERT INTO STEP_PRODUCT_BOOK_REF(id, product_id, book_id)
VALUES ('3', '0', '3');
INSERT INTO STEP_PRODUCT_BOOK_REF(id, product_id, book_id)
VALUES ('4', '0', '4');

INSERT INTO STEP_PRODUCT_BOOK_REF(id, product_id, book_id)
VALUES ('5', '1', '0');
INSERT INTO STEP_PRODUCT_BOOK_REF(id, product_id, book_id)
VALUES ('6', '1', '1');
INSERT INTO STEP_PRODUCT_BOOK_REF(id, product_id, book_id)
VALUES ('7', '1', '2');
INSERT INTO STEP_PRODUCT_BOOK_REF(id, product_id, book_id)
VALUES ('8', '1', '3');
INSERT INTO STEP_PRODUCT_BOOK_REF(id, product_id, book_id)
VALUES ('9', '1', '4');

INSERT INTO STEP_PRODUCT_BOOK_REF(id, product_id, book_id)
VALUES ('10', '2', '0');
INSERT INTO STEP_PRODUCT_BOOK_REF(id, product_id, book_id)
VALUES ('11', '2', '1');
INSERT INTO STEP_PRODUCT_BOOK_REF(id, product_id, book_id)
VALUES ('12', '2', '2');
INSERT INTO STEP_PRODUCT_BOOK_REF(id, product_id, book_id)
VALUES ('13', '2', '3');
INSERT INTO STEP_PRODUCT_BOOK_REF(id, product_id, book_id)
VALUES ('14', '2', '4');

INSERT INTO STEP_PRODUCT_BOOK_REF(id, product_id, book_id)
VALUES ('15', '3', '7');
INSERT INTO STEP_PRODUCT_BOOK_REF(id, product_id, book_id)
VALUES ('16', '3', '8');
INSERT INTO STEP_PRODUCT_BOOK_REF(id, product_id, book_id)
VALUES ('17', '3', '9');

INSERT INTO STEP_PRODUCT_BOOK_REF(id, product_id, book_id)
VALUES ('18', '4', '7');
INSERT INTO STEP_PRODUCT_BOOK_REF(id, product_id, book_id)
VALUES ('19', '4', '8');
INSERT INTO STEP_PRODUCT_BOOK_REF(id, product_id, book_id)
VALUES ('20', '4', '9');

INSERT INTO STEP_PRODUCT_BOOK_REF(id, product_id, book_id)
VALUES ('21', '5', '7');
INSERT INTO STEP_PRODUCT_BOOK_REF(id, product_id, book_id)
VALUES ('22', '5', '8');
INSERT INTO STEP_PRODUCT_BOOK_REF(id, product_id, book_id)
VALUES ('23', '5', '9');

INSERT INTO STEP_PRODUCT_BOOK_REF(id, product_id, book_id)
VALUES ('24', '6', '5');
INSERT INTO STEP_PRODUCT_BOOK_REF(id, product_id, book_id)
VALUES ('25', '6', '6');

INSERT INTO STEP_PRODUCT_BOOK_REF(id, product_id, book_id)
VALUES ('26', '7', '5');
INSERT INTO STEP_PRODUCT_BOOK_REF(id, product_id, book_id)
VALUES ('27', '7', '6');

INSERT INTO STEP_PRODUCT_BOOK_REF(id, product_id, book_id)
VALUES ('28', '8', '5');
INSERT INTO STEP_PRODUCT_BOOK_REF(id, product_id, book_id)
VALUES ('29', '8', '6');


INSERT INTO STEP_PRODUCT_COURSE_REF(id, product_id, book_id, course_id)
VALUES ('0', '0', '0', '0');
INSERT INTO STEP_PRODUCT_COURSE_REF(id, product_id, book_id, course_id)
VALUES ('1', '0', '0', '1');
INSERT INTO STEP_PRODUCT_COURSE_REF(id, product_id, book_id, course_id)
VALUES ('2', '0', '1', '2');
INSERT INTO STEP_PRODUCT_COURSE_REF(id, product_id, book_id, course_id)
VALUES ('3', '0', '1', '3');
INSERT INTO STEP_PRODUCT_COURSE_REF(id, product_id, book_id, course_id)
VALUES ('4', '1', '0', '0');
INSERT INTO STEP_PRODUCT_COURSE_REF(id, product_id, book_id, course_id)
VALUES ('5', '1', '0', '1');
INSERT INTO STEP_PRODUCT_COURSE_REF(id, product_id, book_id, course_id)
VALUES ('6', '1', '1', '2');
INSERT INTO STEP_PRODUCT_COURSE_REF(id, product_id, book_id, course_id)
VALUES ('7', '1', '1', '3');
INSERT INTO STEP_PRODUCT_COURSE_REF(id, product_id, book_id, course_id)
VALUES ('8', '2', '0', '0');
INSERT INTO STEP_PRODUCT_COURSE_REF(id, product_id, book_id, course_id)
VALUES ('9', '2', '0', '1');
INSERT INTO STEP_PRODUCT_COURSE_REF(id, product_id, book_id, course_id)
VALUES ('10', '2', '1', '2');
INSERT INTO STEP_PRODUCT_COURSE_REF(id, product_id, book_id, course_id)
VALUES ('11', '2', '1', '3');


INSERT INTO STEP_PRODUCT_MEDIA_REF(id, product_id, media_id, media_url)
VALUES ('0', '0', '001', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/product/981ec323-12d1-4088-a1ba-461ac0de89e8.jpg');
INSERT INTO STEP_PRODUCT_MEDIA_REF(id, product_id, media_id, media_url)
VALUES ('1', '0', '002', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/product/fc8b7ed7-2b53-49be-8b4d-cd4c2d9ff15a.jpg');
INSERT INTO STEP_PRODUCT_MEDIA_REF(id, product_id, media_id, media_url)
VALUES ('2', '1', '003', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/product/d86913e9-3a44-4b29-9403-bd16ac2b8584.jpg');
INSERT INTO STEP_PRODUCT_MEDIA_REF(id, product_id, media_id, media_url)
VALUES ('3', '1', '004', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/product/e0ff2c40-fab6-4be0-b549-f1cc79aa78a8.jpg');
INSERT INTO STEP_PRODUCT_MEDIA_REF(id, product_id, media_id, media_url)
VALUES ('4', '2', '005', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/product/ee248eac-b3f6-4953-bcad-271cb6e52653.jpg');
INSERT INTO STEP_PRODUCT_MEDIA_REF(id, product_id, media_id, media_url)
VALUES ('5', '2', '006', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/product/ee248eac-b3f6-4953-bcad-271cb6e52653.jpg');
INSERT INTO STEP_PRODUCT_MEDIA_REF(id, product_id, media_id, media_url)
VALUES ('6', '3', '001', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/product/981ec323-12d1-4088-a1ba-461ac0de89e8.jpg');
INSERT INTO STEP_PRODUCT_MEDIA_REF(id, product_id, media_id, media_url)
VALUES ('7', '3', '002', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/product/fc8b7ed7-2b53-49be-8b4d-cd4c2d9ff15a.jpg');
INSERT INTO STEP_PRODUCT_MEDIA_REF(id, product_id, media_id, media_url)
VALUES ('8', '4', '003', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/product/d86913e9-3a44-4b29-9403-bd16ac2b8584.jpg');
INSERT INTO STEP_PRODUCT_MEDIA_REF(id, product_id, media_id, media_url)
VALUES ('9', '4', '004', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/product/e0ff2c40-fab6-4be0-b549-f1cc79aa78a8.jpg');
INSERT INTO STEP_PRODUCT_MEDIA_REF(id, product_id, media_id, media_url)
VALUES ('10', '5', '005', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/product/ee248eac-b3f6-4953-bcad-271cb6e52653.jpg');
INSERT INTO STEP_PRODUCT_MEDIA_REF(id, product_id, media_id, media_url)
VALUES ('11', '5', '006', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/product/ee248eac-b3f6-4953-bcad-271cb6e52653.jpg');
INSERT INTO STEP_PRODUCT_MEDIA_REF(id, product_id, media_id, media_url)
VALUES ('12', '6', '001', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/product/981ec323-12d1-4088-a1ba-461ac0de89e8.jpg');
INSERT INTO STEP_PRODUCT_MEDIA_REF(id, product_id, media_id, media_url)
VALUES ('13', '6', '002', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/product/fc8b7ed7-2b53-49be-8b4d-cd4c2d9ff15a.jpg');
INSERT INTO STEP_PRODUCT_MEDIA_REF(id, product_id, media_id, media_url)
VALUES ('14', '7', '003', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/product/d86913e9-3a44-4b29-9403-bd16ac2b8584.jpg');
INSERT INTO STEP_PRODUCT_MEDIA_REF(id, product_id, media_id, media_url)
VALUES ('15', '7', '004', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/product/e0ff2c40-fab6-4be0-b549-f1cc79aa78a8.jpg');
INSERT INTO STEP_PRODUCT_MEDIA_REF(id, product_id, media_id, media_url)
VALUES ('16', '8', '005', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/product/ee248eac-b3f6-4953-bcad-271cb6e52653.jpg');
INSERT INTO STEP_PRODUCT_MEDIA_REF(id, product_id, media_id, media_url)
VALUES ('17', '8', '006', 'https://s3.cn-north-1.amazonaws.com.cn/stepbook.public/product/ee248eac-b3f6-4953-bcad-271cb6e52653.jpg');

INSERT INTO STEP_PRODUCT_CLASSIFICATION_REF(id, product_id, classification_id)
VALUES ('0', '0', '0');
INSERT INTO STEP_PRODUCT_CLASSIFICATION_REF(id, product_id, classification_id)
VALUES ('1', '0', '1');
INSERT INTO STEP_PRODUCT_CLASSIFICATION_REF(id, product_id, classification_id)
VALUES ('2', '1', '0');
INSERT INTO STEP_PRODUCT_CLASSIFICATION_REF(id, product_id, classification_id)
VALUES ('3', '1', '1');
INSERT INTO STEP_PRODUCT_CLASSIFICATION_REF(id, product_id, classification_id)
VALUES ('4', '2', '0');
INSERT INTO STEP_PRODUCT_CLASSIFICATION_REF(id, product_id, classification_id)
VALUES ('5', '2', '1');
INSERT INTO STEP_PRODUCT_CLASSIFICATION_REF(id, product_id, classification_id)
VALUES ('6', '3', '0');
INSERT INTO STEP_PRODUCT_CLASSIFICATION_REF(id, product_id, classification_id)
VALUES ('7', '3', '1');
INSERT INTO STEP_PRODUCT_CLASSIFICATION_REF(id, product_id, classification_id)
VALUES ('8', '4', '0');
INSERT INTO STEP_PRODUCT_CLASSIFICATION_REF(id, product_id, classification_id)
VALUES ('9', '4', '1');
INSERT INTO STEP_PRODUCT_CLASSIFICATION_REF(id, product_id, classification_id)
VALUES ('10', '5', '1');
INSERT INTO STEP_PRODUCT_CLASSIFICATION_REF(id, product_id, classification_id)
VALUES ('11', '5', '2');
INSERT INTO STEP_PRODUCT_CLASSIFICATION_REF(id, product_id, classification_id)
VALUES ('12', '6', '0');
INSERT INTO STEP_PRODUCT_CLASSIFICATION_REF(id, product_id, classification_id)
VALUES ('13', '6', '1');
INSERT INTO STEP_PRODUCT_CLASSIFICATION_REF(id, product_id, classification_id)
VALUES ('14', '7', '0');
INSERT INTO STEP_PRODUCT_CLASSIFICATION_REF(id, product_id, classification_id)
VALUES ('15', '7', '1');
INSERT INTO STEP_PRODUCT_CLASSIFICATION_REF(id, product_id, classification_id)
VALUES ('16', '8', '0');
INSERT INTO STEP_PRODUCT_CLASSIFICATION_REF(id, product_id, classification_id)
VALUES ('17', '8', '1');



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

