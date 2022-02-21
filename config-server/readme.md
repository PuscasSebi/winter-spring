ai jdbc app siti trebe tabele ca acolo tine figs..
use authorization;

create table PROPERTIES (id integer not null auto_increment, CREATED_ON datetime ,APPLICATION varchar(255), PROFILE varchar(255), LABEL varchar(255), PROP_KEY varchar(255), VALUE varchar(255), primary key (id)) engine=InnoDB;



INSERT INTO properties (CREATED_ON, APPLICATION, PROFILE, LABEL, PROP_KEY, VALUE) VALUES (NULL,'devglan','dev','latest','test-property','This is my test value');


in client ai un endpoint de refresh
localhost:8180/item-api/actuator/refresh

clientu se conecteaza si permiti getul de la Ip up clientului sa-si poata lua figurile, un fel de securitate dar
Nush ce sa zice cu asta, nu e periculos atata timp cat nu ai sensitive info , restu de post sau alte metode nu sunt permise deci n are ce sa faca cu date encryptate din get gen
