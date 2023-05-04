CREATE TABLE "users"
(
    "id"                 bigserial    NOT NULL PRIMARY KEY,
    "guid"               uuid         NOT NULL,
    "account_id"         bigint       NOT NULL,
    "account_guid"       uuid         NULL,
    "display_name"       varchar(128)          DEFAULT NULL,
    "first_name"         varchar(128)          DEFAULT NULL,
    "last_name"          varchar(128)          DEFAULT NULL,
    "password"           char(128)    NOT NULL DEFAULT '',
    "salt"               char(128)    NOT NULL DEFAULT '',
    "email"              varchar(128) NOT NULL DEFAULT '',
    "created_at"         timestamp    NOT NULL
);

CREATE UNIQUE INDEX "users_guid" ON "users" ("guid");
