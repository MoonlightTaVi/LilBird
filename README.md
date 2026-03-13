A **database service** that provides access to filtering, viewing, organising
and managing the information about _birds, all of their possible names,
gallery of photos, audio records of their voices_ 
and various other user/host-specified information.

The service is meant to be used either locally (powered by `H2` database)
or through a remote host connection 
(`db-dev.properties` must be updated for this purpose). In any case, it can only be viewed through a web-browser.

Technically, the service may be used to manage any other kind of information; but it is specifically designed for the ornithology.

A small hobbyist full-stack project.

# `.env` variables

Use `SPRING_PROFILE=dev` unless performing QA tests.
Update the name of the database and its login credentials, if necessary.

```properties
SPRING_PROFILE=test # 'test' or 'dev'
DB_USER=sa
DB_PASSWORD=sa
DB_NAME=lilbird-db
```