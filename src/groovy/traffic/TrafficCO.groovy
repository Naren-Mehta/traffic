package traffic

import grails.validation.Validateable
import org.springframework.web.multipart.commons.CommonsMultipartFile


@Validateable
class TrafficCO {

    CommonsMultipartFile sysLogFile

    String fileType
    String replacingScheme
    String wlCacheSize
    String blCacheSize
//    String dnsblNo
    Long batchSize

    static constraints = {
        fileType nullable: false
        replacingScheme nullable: false
        wlCacheSize nullable: false
        blCacheSize nullable: false
//        dnsblNo nullable: false
        batchSize nullable: false

    }

}
