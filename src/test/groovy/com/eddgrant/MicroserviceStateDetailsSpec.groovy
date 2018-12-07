package com.eddgrant

import grails.gorm.transactions.Transactional
import io.micronaut.context.ApplicationContext
import io.micronaut.runtime.server.EmbeddedServer
import org.grails.orm.hibernate.HibernateDatastore
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification


class MicroserviceStateDetailsSpec extends Specification {

    @Shared
    @AutoCleanup
    EmbeddedServer embeddedServer = ApplicationContext.build()
                                                      .packages("com.eddgrant")
                                                      .run(EmbeddedServer)

    @Transactional
    void "GORM should manage dateCreated and lastUpdated automatically"() {
        given:
        final String serviceName = "my-first-service"
        final String serviceVersion = "0.0.1"
        final String zone = "public"
        final String artefactName = "my-first-service"

        when:
        HibernateDatastore hibernateDatastore = embeddedServer.applicationContext.getBean(HibernateDatastore)

        then:
        hibernateDatastore.autoTimestampEventListener

        when:
        MicroserviceStateDetails microserviceStateDetails = new MicroserviceStateDetails(serviceName: serviceName,
                                                                                         serviceVersion: serviceVersion,
                                                                                         artefactName: artefactName,
                                                                                         zone: zone,
                                                                                         healthyInstanceCount: 2,
                                                                                         unhealthyInstanceCount: 0,
                                                                                         availabilityZones: ["eu-west-1"]).save(flush: true)

        then:
        microserviceStateDetails.dateCreated // Test fails here.
        microserviceStateDetails.lastUpdated

        when:
        microserviceStateDetails = MicroserviceStateDetails.findByServiceName(serviceName)

        then:
        microserviceStateDetails.dateCreated
        microserviceStateDetails.lastUpdated

        final Date dateCreated = microserviceStateDetails.dateCreated
        final Date lastUpdated = microserviceStateDetails.lastUpdated

        when:
        microserviceStateDetails.serviceVersion = "0.0.2"
        microserviceStateDetails.save(flush: true)
        microserviceStateDetails = MicroserviceStateDetails.findByServiceName(serviceName)

        then:
        microserviceStateDetails.serviceVersion == "0.0.2"
        dateCreated == microserviceStateDetails.dateCreated
        lastUpdated != microserviceStateDetails.lastUpdated
    }
}
