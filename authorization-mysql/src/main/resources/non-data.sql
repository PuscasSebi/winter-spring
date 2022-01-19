
 INSERT INTO PERMISSION (ID,NAME) VALUES
 (1,'create_profile'),
 (2,'read_profile'),
 (3,'update_profile'),
 (4,'delete_profile');

 INSERT INTO role (ID,NAME) VALUES
		(1,'ROLE_admin'),(2,'ROLE_editor'),(3,'ROLE_operator');

 INSERT INTO PERMISSION_ROLE (PERMISSION_ID, ROLE_ID) VALUES
     (1,1), /*create-> admin */
     (2,1), /* read admin */
     (3,1), /* update admin */
     (4,1), /* delete admin */
     (2,2),  /* read Editor */
     (3,2),  /* update Editor */
     (2,3);  /* read operator */


 insert into user (id, username,password, email, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked) VALUES ('1', 'admin','{bcrypt}$2a$12$xVEzhL3RTFP1WCYhS4cv5ecNZIf89EnOW4XQczWHNB/Zi4zQAnkuS', 'sebastian.voievod@gmail.com', true, true, true, true);
 insert into  user (id, username,password, email, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked) VALUES ('2', 'puscas', '{bcrypt}$2a$08$Lxo5hVnkSlqQlenQKrUAceoHMXa7iIfn2j62HK66R70O80kNgJJNi','sebastian.voievod@gmail.com', true, true, true, true);
 insert into  user (id, username,password, email, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked) VALUES ('3', 'user', '{bcrypt}$2a$12$udISUXbLy9ng5wuFsrCMPeQIYzaKtAEXNJqzeprSuaty86N4m6emW','sebastian.voievod@gmail.com', true, true, true, true);
 /*
 passowrds:
 admin - admin
 puscas - puscasenabled
 user - user
 */


INSERT INTO ROLE_USER (ROLE_ID, USER_ID)
    VALUES
    (1, 1), /* admin-admin */
    (2, 2), /* puscas-editor */
    (3, 3); /* user-operatorr */ ;