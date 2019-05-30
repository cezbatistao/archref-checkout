--liquibase formatted sql
--changeset carlosz:1

create table hibernate_sequence (next_val bigint) engine=InnoDB;

insert into hibernate_sequence values ( 1 );

create table product (id bigint not null, code varchar(255) not null, name varchar(255) not null, value decimal(19,2) not null, primary key (id)) engine=InnoDB;
create table purchase (id bigint not null, quantity integer not null, order_id bigint not null, product_id bigint not null, primary key (id)) engine=InnoDB;
create table purchase_order (id bigint not null, create_date datetime(6), modify_date datetime(6), order_number bigint not null, username varchar(255) not null, value decimal(19,2) not null, primary key (id)) engine=InnoDB;
create table stock (id bigint not null, total integer not null, product_id bigint not null, primary key (id)) engine=InnoDB;

alter table product add constraint UK_h3w5r1mx6d0e5c6um32dgyjej unique (code);
alter table purchase_order add constraint UK_iy9qppehbrfl4lblrjqhxkfbh unique (order_number);
alter table purchase add constraint FK6lsv1xu7t90cauap7t0ol4ali foreign key (order_id) references purchase_order (id);
alter table purchase add constraint FK3s4jktret4nl7m8yhfc8mfrn5 foreign key (product_id) references product (id);
alter table stock add constraint FKjghkvw2snnsr5gpct0of7xfcf foreign key (product_id) references product (id);

--rollback drop table if exists hibernate_sequence;
--rollback drop table if exists product;
--rollback drop table if exists purchase;
--rollback drop table if exists purchase_order;
--rollback drop table if exists stock;