# Little Stepbooks API

## How to build

```shell script
./gradlew clean build
./gradle bootRun
```

## Database in docker

```shell script
docker run -p 5432:5432 -d \
  --name novlnovl_db \
  -e POSTGRES_DB=novlnovl \
  -e POSTGRES_USER=novlnovl_user \
  -e POSTGRES_PASSWORD=novlnovl \
  -e PGDATA=/var/lib/postgresql/data/pgdata \
  postgres:13.1-alpine
``` 

### Drop All Tables

```
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;
```
