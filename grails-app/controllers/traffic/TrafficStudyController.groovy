package traffic

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

            request.getFiles("uploadFiles[]").each { file ->

                if (trafficCO?.fileType?.equals("IP address")) {
                    trafficUtilService?.storeIpAddress(file, traffic)
                } else {
                    trafficUtilService?.storeSystemLogs(sysLogFile, traffic)
                }
            }

            Boolean isDone = false
            Boolean isSuccessful = false
            try {
                isSuccessful = trafficUtilService?.performLogic(traffic)
            }
            catch (Exception e) {

            }


            List<StaticsResults> staticsResultsList = StaticsResults?.findAllByTraffic(traffic)

            if (isSuccessful) {
                render(view: "/trafficStudy/showResult", model: [staticsResultsList: staticsResultsList])

            } else {
                render(view: "/trafficStudy/showResult", model: [staticsResultsList: staticsResultsList])
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
