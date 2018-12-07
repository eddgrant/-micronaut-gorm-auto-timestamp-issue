# micronaut-gorm-auto-timestamp-issue

Given the following method in `AbstractPersistenceEventListener`:

    public boolean supportsSourceType(final Class<?> sourceType) {
        // ensure that this listener only handles its events (e.g. if Mongo and Redis are both installed)
        return datastore.getClass().isAssignableFrom(sourceType);
    }

When called with argument: `org.grails.orm.hibernate.HibernateDatastore` when `dataStore.getClass()` is `org.grails.orm.hibernate.HibernateDatastore$2` the method is returning `false`. I'm wondering if it should be returning `true` and wondered if the `$2` is significant here?

[Discussed in Gitter](https://gitter.im/micronautfw/questions?at=5c0a94005e409525032d5dc7) and raising as a potential bug.

There's a test that demonstrates the issue in `MicroserviceStateDetailsSpec`.
