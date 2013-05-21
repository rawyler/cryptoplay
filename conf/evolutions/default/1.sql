# --- Created by Slick DDL
# To stop Slick DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table "users" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,"email" VARCHAR NOT NULL,"password" VARCHAR NOT NULL,"admin" BOOLEAN DEFAULT false NOT NULL,"privateKey" VARCHAR NOT NULL,"publicKey" VARCHAR NOT NULL,"lastLoginAt" DATE,"firstName" VARCHAR,"lastName" VARCHAR,"authMode" VARCHAR DEFAULT 'DB' NOT NULL,"preferredLocale" VARCHAR DEFAULT 'en' NOT NULL,"createdAt" DATE DEFAULT {d '2013-05-21'} NOT NULL,"updatedAt" DATE DEFAULT {d '2013-05-21'} NOT NULL);

# --- !Downs

drop table "users";

