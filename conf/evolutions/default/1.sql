# --- Created by Slick DDL
# To stop Slick DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table "accounts" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"groupId" BIGINT NOT NULL,"name" VARCHAR NOT NULL,"username" VARCHAR NOT NULL,"password" VARCHAR NOT NULL,"description" VARCHAR,"createdAt" TIMESTAMP NOT NULL,"updatedAt" TIMESTAMP NOT NULL);
create table "groups" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"teamId" BIGINT NOT NULL,"name" VARCHAR NOT NULL,"description" VARCHAR,"createdAt" TIMESTAMP NOT NULL,"updatedAt" TIMESTAMP NOT NULL);
create table "teammembers" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"teamId" BIGINT NOT NULL,"userId" BIGINT NOT NULL,"password" VARCHAR NOT NULL,"admin" BOOLEAN DEFAULT false NOT NULL,"locked" BOOLEAN DEFAULT false NOT NULL,"createdAt" TIMESTAMP NOT NULL,"updatedAt" TIMESTAMP NOT NULL);
create unique index "idx_team_user" on "teammembers" ("teamId","userId");
create table "teams" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"name" VARCHAR NOT NULL,"description" VARCHAR,"noAdmin" BOOLEAN DEFAULT false NOT NULL,"noRoot" BOOLEAN DEFAULT false NOT NULL,"createdAt" TIMESTAMP NOT NULL,"updatedAt" TIMESTAMP NOT NULL);
create table "users" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"email" VARCHAR NOT NULL,"password" VARCHAR NOT NULL,"admin" BOOLEAN DEFAULT false NOT NULL,"privateKey" VARCHAR NOT NULL,"publicKey" VARCHAR NOT NULL,"lastLoginAt" TIMESTAMP,"firstName" VARCHAR,"lastName" VARCHAR,"authMode" VARCHAR DEFAULT 'DBAuth' NOT NULL,"preferredLocale" VARCHAR DEFAULT 'en' NOT NULL,"createdAt" TIMESTAMP NOT NULL,"updatedAt" TIMESTAMP NOT NULL);
alter table "accounts" add constraint "fk_accounts_group" foreign key("groupId") references "groups"("id") on update NO ACTION on delete NO ACTION;
alter table "groups" add constraint "fk_groups_team" foreign key("teamId") references "teams"("id") on update NO ACTION on delete NO ACTION;
alter table "teammembers" add constraint "fk_member_user" foreign key("userId") references "users"("id") on update NO ACTION on delete NO ACTION;
alter table "teammembers" add constraint "fk_member_team" foreign key("teamId") references "teams"("id") on update NO ACTION on delete NO ACTION;

# --- !Downs

alter table "accounts" drop constraint "fk_accounts_group";
alter table "groups" drop constraint "fk_groups_team";
alter table "teammembers" drop constraint "fk_member_user";
alter table "teammembers" drop constraint "fk_member_team";
drop table "accounts";
drop table "groups";
drop table "teammembers";
drop table "teams";
drop table "users";

