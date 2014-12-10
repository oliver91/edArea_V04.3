# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table course (
  course_name               varchar(255) not null,
  email                     varchar(255),
  science                   varchar(255),
  about_course              TEXT,
  logo_path                 varchar(255),
  current                   tinyint(1) default 0,
  constraint pk_course primary key (course_name))
;

create table course_block (
  block_name                varchar(255) not null,
  course_name               varchar(255),
  block_content             TEXT,
  constraint pk_course_block primary key (block_name))
;

create table friends (
  id                        integer auto_increment not null,
  user_email                varchar(255),
  friend_email              varchar(255),
  constraint pk_friends primary key (id))
;

create table notification (
  notification_id           integer auto_increment not null,
  email_from                varchar(255),
  email_to                  varchar(255),
  notification_message      varchar(255),
  type                      integer,
  constraint pk_notification primary key (notification_id))
;

create table user (
  email                     varchar(255) not null,
  id                        integer,
  name                      varchar(255),
  country                   varchar(255),
  city                      varchar(255),
  birth_date                varchar(255),
  user_type                 integer,
  password                  varchar(255),
  last_visit                datetime,
  constraint pk_user primary key (email))
;




# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table course;

drop table course_block;

drop table friends;

drop table notification;

drop table user;

SET FOREIGN_KEY_CHECKS=1;

