# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table course (
  course_name               varchar(255) not null,
  email                     varchar(255),
  science                   varchar(255),
  about_course              TEXT,
  logo_path                 varchar(255),
  current                   boolean,
  constraint pk_course primary key (course_name))
;

create table course_block (
  block_name                varchar(255) not null,
  course_name               varchar(255),
  block_content             TEXT,
  constraint pk_course_block primary key (block_name))
;

create table friends (
  id                        integer not null,
  user_email                varchar(255),
  friend_email              varchar(255),
  constraint pk_friends primary key (id))
;

create table notification (
  notification_id           integer not null,
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
  last_visit                timestamp,
  constraint pk_user primary key (email))
;

create sequence course_seq;

create sequence course_block_seq;

create sequence friends_seq;

create sequence notification_seq;

create sequence user_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists course;

drop table if exists course_block;

drop table if exists friends;

drop table if exists notification;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists course_seq;

drop sequence if exists course_block_seq;

drop sequence if exists friends_seq;

drop sequence if exists notification_seq;

drop sequence if exists user_seq;

