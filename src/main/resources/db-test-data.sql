--INSERT INTO articles(author, title, content, creationTimestamp) VALUES ('admin', 'Почему утки плавают', 'Тут должна быть смешная шутка, но ее нет', 1547645756880);
--INSERT INTO articles(author, title, content, creationTimestamp) VALUES ('freddy', 'Очень важная заметка', 'Почему бы ее не удалить', 1547661756775);
--INSERT INTO articles(author, title, content, creationTimestamp, identicon) VALUES ('admin','Lorem Ipsum','Lorem Ipsum id est laborum.',1547645756880,FILE_READ('classpath:/com/package/resource/logo.png');

--freddy - doctorwhothebest
--admin - password
INSERT INTO users(name, password, email, roles) VALUES ('freddy', '$2a$10$VTRvGRwpuouPYlifOFU00.qoAESKGqgpcchoJ353vkv59G7TIUSCG', 'buyboozee@gmail.com', 'ROLE_EDITOR');
INSERT INTO users(name, password, email, roles) VALUES ('admin', '$2a$10$/65XVr5RZqq6fx4nUQRxpOmxgMhY21dYkikb2IxZCZ0hARJ9rG/di', 'admin@gmail.com', 'ROLE_EDITOR,ROLE_ADMIN');
