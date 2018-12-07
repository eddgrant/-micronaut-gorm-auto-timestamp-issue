package com.eddgrant

import grails.gorm.annotation.Entity
import groovy.transform.Canonical

@Entity
@Canonical
class MicroserviceStateDetails {

    static constraints = {
        dateCreated nullable: true
        lastUpdated nullable: true
    }

    static mapping = {
        autoTimestamp true
    }

    String serviceName
    String serviceVersion
    String zone
    String artefactName

    int healthyInstanceCount
    int unhealthyInstanceCount
    Set<String> availabilityZones

    Date dateCreated
    Date lastUpdated
}
