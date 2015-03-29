package traffic

class Traffic {


    Long replacingScheme
    Long wlCacheSize
    Long blCacheSize
    String fileType
    Long batchSize

    String batchType
    Long batchSize1
    Long batchSize2

    static hasMany = [dnsblsUrls: DnsblsUrls]

    static constraints = {
        replacingScheme nullable: false
        wlCacheSize nullable: false
        blCacheSize nullable: false
        fileType nullable: false

        batchType nullable: false
        batchSize nullable: true
        batchSize1 nullable: true
        batchSize2 nullable: true
    }
}
