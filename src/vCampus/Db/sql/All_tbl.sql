INSERT INTO vCampus.tblUser (id, pwd, age, gender, role, email, card, lost) VALUES ('123456789', 'password1', 25, 1, 'ST', 'user001@example.com', '123456789', 0);
INSERT INTO vCampus.tblUser (id, pwd, age, gender, role, email, card, lost) VALUES ('987654321', 'password2', 30, 0, 'TC', 'user002@example.com', '987654321', 1);
INSERT INTO vCampus.tblUser (`id`,`pwd`,`age`,`gender`,`role`,`email`,`card`,`lost`) VALUES ('111111111','111111',1,1,'ST','11111111@qq.com','111111111',0);
INSERT INTO vCampus.tblShopStudent (`id`,`card`,`favorites`,`belongs`,`bill`) VALUES ('111111111','111111111','','','');
INSERT INTO vCampus.tblShopStudent  (`id`,`card`,`favorites`,`belongs`,`bill`) VALUES ('213221798','213221798','2,0','8,10,11','');
INSERT INTO vCampus.tblShopStudent  (`id`,`card`,`favorites`,`belongs`,`bill`) VALUES ('987654321','987654321','1,2','0,1,2,3,4,5,6,7,8,9','');
INSERT INTO vCampus.tblGrade (`id`, `card_id`, `course_name`, `course_id`, `usual`, `mid`, `final`, `total`, `point`, `is_first`, `term`) VALUES ('1', '111111111', '1', '1', 1, 1, 1, 0, 1, 0, '11');
INSERT INTO vCampus.tblGrade (`id`, `card_id`, `course_name`, `course_id`, `usual`, `mid`, `final`, `total`, `point`, `is_first`, `term`) VALUES ('2', '111111111', '2', '2', 1, 1, 1, 1, 1, 1, '11');
INSERT INTO vCampus.tblGrade (`id`, `card_id`, `course_name`, `course_id`, `usual`, `mid`, `final`, `total`, `point`, `is_first`, `term`) VALUES ('3', '111111111', '3', '3', 1, 1, 1, 1, 1, 1, '11');
INSERT INTO `tblstu` (`id`, `card_id`, `name`, `gender`, `birth`, `college`, `grade`, `major`, `email`, `stage`, `honor`, `punish`, `stu_code`) VALUES ('123456789', '111111111', 'Alice', 'F', '1999-01-01', '计算机科学与技术', '2022级', 'Computer Science', 'alice@example.com', '本科', 'Dean\'s List', 'None', 'A123456789123456789');
INSERT INTO `tblstu` (`id`, `card_id`, `name`, `gender`, `birth`, `college`, `grade`, `major`, `email`, `stage`, `honor`, `punish`, `stu_code`) VALUES ('123', '123456778', '111', '1', '2024-09-11', '1111', '1111', '11111', '11111@qq.com', '11', '111', '111', '111');
INSERT INTO `tblstu` (`id`, `card_id`, `name`, `gender`, `birth`, `college`, `grade`, `major`, `email`, `stage`, `honor`, `punish`, `stu_code`) VALUES ('5', '123456789', '11', '男', '2024-09-05', '1', '1', '1', '165151@qq.com', '1', '1', '1', '1');
INSERT INTO `tblstu` (`id`, `card_id`, `name`, `gender`, `birth`, `college`, `grade`, `major`, `email`, `stage`, `honor`, `punish`, `stu_code`) VALUES ('151', '123456799', '5555', '男', '2024-09-05', '2515', '1515', '0155', '165151@qq.com', '51', '1515', '51515', '852741963789632155');
INSERT INTO vcampus.tblcourse (course_ID, course_name, teacher_name, time, start_time, end_time, status, now_num, max_num, classroom, num) VALUES ('CS101', '计算机科学导论', '幸研', 2, 3, 5, '选修', 0, 5, 'J2-204', 1);
INSERT INTO vcampus.tblcourse (course_ID, course_name, teacher_name, time, start_time, end_time, status, now_num, max_num, classroom, num) VALUES ('CS143', '编译原理', '戚哓芳', 5, 3, 5, '必修', 0, 50, 'J3-205', 1);
INSERT INTO vcampus.tblcourse (course_ID, course_name, teacher_name, time, start_time, end_time, status, now_num, max_num, classroom, num) VALUES ('CS144', '计算机网络', '刘波', 1, 9, 12, '必修', 1, 50, 'J1-301', 1);
INSERT INTO vcampus.tblcourse (course_ID, course_name, teacher_name, time, start_time, end_time, status, now_num, max_num, classroom, num) VALUES ('CS229', '机器学习', '贾育衡', 2, 3, 5, '选修', 0, 30, 'J3-305', 1);
INSERT INTO vcampus.tblcourse (course_ID, course_name, teacher_name, time, start_time, end_time, status, now_num, max_num, classroom, num) VALUES ('CS231', '计算机视觉', '周毅', 3, 6, 8, '必修', 0, 50, 'J8-102', 1);
INSERT INTO vcampus.tblteacher (id, course) VALUES ('9999999', 'CS101');
INSERT INTO vcampus.tblteacher (id, course) VALUES ('8888888', 'CS144');