# Little Stepbooks API

## How to build

```shell script
./gradlew clean build
./gradle bootRun
```

## Database in docker

```shell script
docker run -p 5432:5432 -d \
  --name stepbook_db \
  -e POSTGRES_DB=stepbook \
  -e POSTGRES_USER=stepbook_user \
  -e POSTGRES_PASSWORD=stepbook \
  -e PGDATA=/var/lib/postgresql/data/pgdata \
  postgres:13.1-alpine
``` 

### Drop All Tables

```
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;
```
