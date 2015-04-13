package traffic

class StaticsResults {


    Integer totalTestData
    Integer foundInWL
    Integer foundInBL
    Integer hitInDNSBL
    Integer missInDNSBL
    BigDecimal totalTimeTaken

    Integer wlCatchSize
    Integer blCatchSize

    Date date

    Traffic traffic

    static constraints = {
        totalTestData(nullable: true)
        foundInWL(nullable: true)
        foundInBL(nullable: true)
        missInDNSBL(nullable: true)
        hitInDNSBL(nullable: true)
        totalTimeTaken(nullable: true)
        date(nullable: true)
        wlCatchSize(nullable: true)
        blCatchSize(nullable: true)
    }
}
