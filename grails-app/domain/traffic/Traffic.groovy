package traffic

class Traffic {


    String replacingScheme
    String wlCacheSize
    String blCacheSize
    String fileType
    Long batchSize

    static hasMany = [dnsblsUrls: DnsblsUrls]

    static constraints = {
        replacingScheme nullable: false
        wlCacheSize nullable: false
        blCacheSize nullable: false
        fileType nullable: false

    }
}
