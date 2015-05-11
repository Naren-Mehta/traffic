package traffic

import org.apache.commons.io.FileUtils
import org.springframework.web.multipart.commons.CommonsMultipartFile
import utils.AppUtil

class TrafficStudyController {


    def trafficUtilService

    def showTrafficStudyPage = {

    }

    def saveTrafficStudyPage = { TrafficCO trafficCO ->

        def webRootDir = AppUtil.staticResourcesDirPath

        PrintWriter writer = new PrintWriter(webRootDir + "/files/WL_Cache.txt");
        writer.print("");
        writer.close();

        PrintWriter writer1 = new PrintWriter(webRootDir + "/files/BL_Cache.txt");
        writer.print("");
        writer.close();


        CommonsMultipartFile sysLogFile = trafficCO?.sysLogFile as CommonsMultipartFile


        if (trafficCO?.validate()) {

            Traffic traffic = new Traffic()

            traffic.fileType = trafficCO?.fileType
            traffic.replacingScheme = trafficCO?.replacingScheme
            traffic.wlCacheSize = trafficCO?.wlCacheSize
            traffic.blCacheSize = trafficCO?.blCacheSize
            traffic.batchSize = trafficCO?.batchSize

            traffic.batchType = trafficCO?.batchType
            traffic.batchSize1 = trafficCO?.batchSize1
            traffic.batchSize2 = trafficCO?.batchSize2

            AppUtil?.save(traffic)


            def list = params?.dnsblNo?.id
            if (list instanceof String) {
                storeDnsblsUrl(traffic, list)
            } else if (list.toString().contains(',')) {
                list?.each { String url ->
                    storeDnsblsUrl(traffic, url)
                }
            } else {
                println("----------in else------none---------")
            }

            Boolean isSuccessful = false


            request.getFiles("uploadFiles[]").each { file ->

                if (trafficCO?.fileType?.equals("IP address")) {
                    println("-------------IP address--file----------------")
                    try {
                        trafficUtilService?.storeIpAddress(file, traffic)
                        isSuccessful = trafficUtilService?.performLogic(traffic)

                    } catch (Exception e) {
                        println("----------------------ip address file cant store-=-------------")
                    }
                } else {
                    println("-------------syslog--file----------------")
                    try {
                        trafficUtilService?.storeSystemLogs(file, traffic)
                        trafficUtilService?.storeIpAddressFromSyslogFile(file, traffic)
                        isSuccessful = trafficUtilService?.performLogic(traffic)

                    } catch (Exception e) {
                        println("----------------------ip address file cant store-=-------------")
                    }
                }
            }


            List<StaticsResults> staticsResultsList = StaticsResults?.findAllByTraffic(traffic)

            if (isSuccessful) {
                render(view: "/trafficStudy/showResult", model: [staticsResultsList: staticsResultsList])

            } else {
                if (trafficCO?.fileType?.equals("IP address")) {
                    flash.message = "Please inter a valid Ip address. Maximum size will be 1.0 mb"

                } else {
                    flash.message = "Please Insert a vaild Syslog file. Maximum size will be 1.0 mb"
                }
                render(view: "/trafficStudy/showTrafficStudyPage", model: [staticsResultsList: staticsResultsList])
            }


        } else {
            println("---------------------------------else---------------")
            render(view: "/trafficStudy/showTrafficStudyPage", model: [trafficCO: trafficCO])
        }

    }


    def showResult() {

        StaticsResults staticsResults = StaticsResults?.get(params?.staticsResultsID as Long)
        render(view: "/trafficStudy/showResult", model: [staticsResults: staticsResults])
    }


    public storeDnsblsUrl(Traffic traffic, String url) {
        DnsblsUrls dnsblsUrls = new DnsblsUrls()
        dnsblsUrls?.urlName = url
        dnsblsUrls?.traffic = traffic
        AppUtil?.save(dnsblsUrls)
        traffic?.addToDnsblsUrls(dnsblsUrls)
        AppUtil?.save(traffic)
    }

}
