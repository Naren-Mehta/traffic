package traffic

class DnsblsUrls {

    String urlName

    static belongsTo = [traffic: Traffic]

    static constraints = {
        traffic(nullable: true)
        urlName(nullable: true)
    }
}
