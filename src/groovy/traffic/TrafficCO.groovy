package traffic

import grails.validation.Validateable
import org.springframework.web.multipart.commons.CommonsMultipartFile


@Validateable
class TrafficCO {

    CommonsMultipartFile sysLogFile

    String fileType
    Long replacingScheme
    Long wlCacheSize
    Long blCacheSize
//    String dnsblNo
    String batchType
    Long batchSize
    Long batchSize1
    Long batchSize2

    static constraints = {
        fileType nullable: false
        replacingScheme nullable: false
        wlCacheSize nullable: false
        blCacheSize nullable: false

        batchType nullable: false
        batchSize nullable: true
        batchSize1 nullable: true
        batchSize2 nullable: true

    }

}
