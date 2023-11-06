# Little Stepbooks API

## Init
./gradlew initGitHooks

## How to build

```shell script
./gradlew clean build
./gradle bootRun
```

## Database in docker

```shell script
docker run -p 5432:5432 -d \
  --name stepbook_db \
  -e POSTGRES_DB=stepbooks \
  -e POSTGRES_USER=stepbook_user \
  -e POSTGRES_PASSWORD=stepbooks \
  -e PGDATA=/var/lib/postgresql/data/pgdata \
  postgres:13.1-alpine
``` 

### Drop All Tables

```
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;
```


```postgres-sql
CREATE USER stepbook_user WITH PASSWORD '17@6WI4zcA';
GRANT ALL PRIVILEGES ON DATABASE stepbook_stage TO stepbook_user;
GRANT ALL PRIVILEGES ON all tables in schema public TO stepbook_user;
GRANT ALL ON SCHEMA public TO stepbook_user;
```