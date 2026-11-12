This directory is mounted into the MySQL container as `/docker-entrypoint-initdb.d`.

It is intentionally empty by default: the Spring Boot backend manages the schema
automatically via Hibernate (`spring.jpa.hibernate.ddl-auto: update`), so tables are
created on first startup based on the JPA entities.

If you prefer to bootstrap the full production schema (14 tables, views, stored
procedures, triggers, seed data) from `Database/vmv-agromart-schema.sql` instead,
copy that file into this folder before running `docker compose up` for the first
time — MySQL will execute any `.sql` file found here on initial container creation.
Note that the entity-managed schema and the hand-written schema differ slightly in
column naming, so pick one approach rather than mixing them.
