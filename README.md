# README #

* This is sample template project with user management and authentication capabilities.
* This can be taken as base for creating new projects with user management.
* You need to create database and run application


1) connect to postgres database
   * psql -h localhost -U root -d postgres

2) Create new database
   * create database template;
   * grant all privileges on database template to root;
3) Run application and signup new admin user
   * and edit in db table "users" to set is_admin = true for that user
   * change role to ROLE_ADMIN.
   * Change status to ACTIVE.
   * Change enabled to true.
   * Change account_non_locked to true.
4) Login with admin email and password and create regular user.
