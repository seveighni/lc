# Logistics Company

## Summary

This is an educational project implementation of a logistics company manager.

## Development

### Startup

The easiest way to start developing the project is by using `docker-compose`. Run the following command which will spin up a `postgres` and `pgadmin` containers in you docker environment:

```bash
docker-compose up -d
```

The `postgres` db can be accessed over the localhost interface on port `3001` and the `pgadmin` can be accessed on `http://localhost:3002`. To change these defaults please edit the [docker-compose.yaml](./docker-compose.yaml) file.

Now the java app can be started as well. If there is a need to edit the default configurations please do so via the [application.properties](./src/main/resources/application.properties) file.

### Cleanup

To clean up the dev environment, stop the java app and then run:

```bash
docker-compose down
```

Directories holding `postgres` and `pgadmin` data can also be deleted. They should be located in the root directory of the project and are named `db` and `pgadmin-data` respectively.
