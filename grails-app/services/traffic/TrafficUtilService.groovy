package traffic

import org.apache.commons.io.FileUtils
import org.springframework.web.multipart.commons.CommonsMultipartFile
import utils.AppUtil

import java.util.regex.Pattern


class TrafficUtilService {


    public Boolean performLogic(Traffic traffic) {
        def webRootDir = AppUtil.staticResourcesDirPath

        StaticsResults staticsResults = null
        Boolean isSuccessful = false

        List<String> totalFiles = []

        List<IpAddressDocuments> ipAddressDocumentsList = IpAddressDocuments?.findAllByTraffic(traffic)

        if (ipAddressDocumentsList) {
            ipAddressDocumentsList?.each { IpAddressDocuments ipAddressDocuments ->
                String ipAddressName = ipAddressDocuments?.name
                String filename = webRootDir + "uploadedFile/ipAddress/${traffic?.id}/" + ipAddressName
                totalFiles?.add(filename)
            }
        }



        int length = totalFiles?.size()
        String[] aaa = new String[length]
        totalFiles?.eachWithIndex { String entry, int i ->
            aaa[i] = entry
        }

        // String list =
        // "C:\\Users\\Ashok\\workspace\\TrafficStudyNew\\src\\IPList.txt";


        for (int intt = 0; intt < aaa.length; intt++) {
            long start = System.currentTimeMillis();


            String[] test;
            String[] a;
            String[] b;
            List listA = null, listB = null, listA1 = null;

            FileInputStream f1 = null

            try {
                f1 = new FileInputStream(aaa[intt]);
            } catch (Exception e) {
            }


            BufferedInputStream b1 = new BufferedInputStream(f1);

            DataInputStream d1 = new DataInputStream(b1);

            String s1 = null;
            listA1 = new LinkedList();


            while ((s1 = d1.readLine()) != null) {
                StringTokenizer sss1 = new StringTokenizer(s1, " ");
                while (sss1.hasMoreTokens()) {
                    listA1.add(sss1.nextToken());
                }
            }

            test = (String[]) listA1.toArray(new String[0]);

            int foundinWL = 0;
            int foundinBL = 0;
            int missindnsbl = 0;
            int hitindnsbl = 0;
            a = new String[0];
            b = new String[0];

            Long batchSize

            long stop = 0

            if (traffic?.batchType?.equals("fixed")) {
                batchSize = traffic?.batchSize as Long
            } else {
                batchSize = randInt(traffic?.batchSize1 as int, traffic?.batchSize2 as int) as Long
            }


            for (int j = 0; j < test.length; j++) {
                for (int jj = j; jj < (j + batchSize); jj++) {
                    FileInputStream fis = null;
                    fis = new FileInputStream(webRootDir + "/files/WL_Cache.txt");

                    BufferedInputStream bis = new BufferedInputStream(fis);
                    DataInputStream dis = new DataInputStream(bis);
                    String str = null;
                    listA = new LinkedList();

                    while ((str = dis.readLine()) != null) {

                        StringTokenizer st = new StringTokenizer(str, " ");
                        while (st.hasMoreTokens()) {
                            listA.add(st.nextToken());
                        }
                    }

                    a = (String[]) listA.toArray(new String[0]);


                    FileInputStream ffis = new FileInputStream(webRootDir + "files/BL_Cache.txt");

                    BufferedInputStream bbis = new BufferedInputStream(ffis);
                    DataInputStream ddis = new DataInputStream(bbis);
                    String sstr = null;
                    listB = new LinkedList();
                    while ((sstr = ddis.readLine()) != null) {
                        StringTokenizer sst = new StringTokenizer(sstr, " ");
                        while (sst.hasMoreTokens()) {
                            listB.add(sst.nextToken());
                        }
                    }

                    b = (String[]) listB.toArray(new String[0]);

                    boolean is1 = false;
                    println(" \nSTART of SEARCH.........!!!");
                    // /SEARCHING ARRAY ONE /whitelist cache


                    for (int i = 0; i < a.length; i++) {
                        if (test.length > jj) {
                            // try changing here i, j swap
                            if (test[jj].equals(a[i])) {
                                is1 = true;
                            }
                        }
                    }
                    if (is1) {
                        println(" it's here in WL!!!");
                        foundinWL++;

                    } else { // /SEARCHING ARRAY TWO /blacklist cache
                        println(" not in WL...now searching BL ....");
                        boolean is2 = false;
                        for (int i = 0; i < b.length; i++) {

                            if (test.length > jj) {
                                if (test[jj].equals(b[i])) {
                                    is2 = true;
                                }
                            }

                        }


                        if (is2) {
                            println(" it's here in BL!!!");
                            foundinBL++;
                        } else {
                            println(" not in BL ..now DNSBL query..");

                            // /DOING DNSBL QUERY
                            Boolean ifSuccess = false
                            List<DnsblsUrls> dnsblsUrlses = DnsblsUrls?.findAllByTraffic(traffic)
                            if (dnsblsUrlses) {
                                dnsblsUrlses?.find { DnsblsUrls dnsblsUrl ->
                                    if (test.length > jj) {
                                        ifSuccess = checkRelay(test[jj], dnsblsUrl?.urlName)
                                    }

                                    if (ifSuccess) {
                                        return true
                                    }
                                }
                            }

                            if (ifSuccess) {
                                println(" it's a hit in the DNS_BL!");
                                hitindnsbl++;
                                // WRITING IN THE BLACKLIST CACHE
                                if (listA.indexOf(test[jj]) < 0 && (test.length > jj)) {

                                    if (traffic?.blCacheSize > 0) {
                                        String file = webRootDir + "files/BL_Cache.txt"
                                        List list = giveList(file)

                                        if (list.size() >= traffic?.blCacheSize) {
                                            println("---------------------listA.size()-------its a hit------------" + list.size())
                                            removeFirstLine(file, traffic?.replacingScheme)
                                        }
                                    }

                                    BufferedWriter bw_one = new BufferedWriter(
                                            new FileWriter(webRootDir + "files/BL_Cache.txt", true));


                                    bw_one.write(test[jj] + "\r\n");
                                    bw_one.close();
                                    System.out
                                            .println("latest size of b (blacklist cache):"
                                            + (b.length + 1));

                                }

                            } else {
                                println(" it's a miss in the DNS_BL!.....");
                                if (test.length > jj) {
                                    missindnsbl++;

                                    if (listB.indexOf(test[jj]) < 0) {

                                        if (traffic?.wlCacheSize > 0) {

                                            String file = webRootDir + "files/WL_Cache.txt"

                                            List list = giveList(file)
                                            if (list.size() >= traffic?.wlCacheSize) {
                                                println("---------------------listA.size()-------its a miss------------" + list.size())

                                                removeFirstLine(file, traffic?.replacingScheme)
                                            }
                                        }


                                        BufferedWriter bw_two = new BufferedWriter(
                                                new FileWriter(webRootDir + "files/WL_Cache.txt", true));

                                        bw_two.write(test[jj] + "\r\n");
                                        bw_two.close();

                                        System.out
                                                .println("latest size a (whitelist cache):"
                                                + (a.length + 1));

                                    }
                                }
                                // WRITING IN THE WHITELIST CACHE

                            }


                        } // DNSBL query
                    }

                }

                // search BL end

                stop = System.currentTimeMillis();

                // writing in the stats


                String file = webRootDir + "/files/WL_Cache.txt"
                List list = giveList(file)
                println("------11------" + list.size());

                String file1 = webRootDir + "/files/BL_Cache.txt"
                List list1 = giveList(file1)
                println("-------22------" + list.size());

                staticsResults = new StaticsResults()


                staticsResults?.totalTestData = (hitindnsbl + missindnsbl + foundinWL + foundinBL) as Integer
                staticsResults?.foundInWL = foundinWL as Integer
                staticsResults?.foundInBL = foundinBL as Integer
                staticsResults?.hitInDNSBL = hitindnsbl as Integer
                staticsResults?.missInDNSBL = missindnsbl as Integer
                staticsResults?.totalTimeTaken = ((stop - start) / 60000) as BigDecimal
                staticsResults?.date = new Date()

                staticsResults?.wlCatchSize = list.size() as Integer
                staticsResults?.blCatchSize = list1.size() as Integer

                staticsResults?.traffic = traffic

                AppUtil?.save(staticsResults)


                j = j + (batchSize - 1);

            } // array of strings end


            BufferedWriter buff = new BufferedWriter(
                    new FileWriter(webRootDir + "files/statistics.txt", true));
            buff.write("\r\n");
            buff.write(" total test data: "
                    + (hitindnsbl + missindnsbl + foundinWL + foundinBL)
                    + " \r\n");
            buff.write("found in WL=" + foundinWL + " , found in BL="
                    + foundinBL + " \r\n");
            buff.write("hit in DNSBL=" + hitindnsbl + " , miss in dnsbl="
                    + missindnsbl + "\r\n");
            buff.write("total time taken: " + (stop - start) / 60000
                    + " min \r\n");
            buff.write("Date = " + new Date());
            buff.write("\r\n");
            buff.close();

            println("\n\n FINITO!");

            isSuccessful = true

            //
        }
        //

        return isSuccessful

    }


    public static boolean checkRelay(String hostName, String dnsblNo) {

        try {
            InetAddress.getByName(invertIPAddress(hostName) + "."
                    + dnsblNo);
            return true;
        } catch (UnknownHostException e) {
            return false;
        }
    }


    protected static String invertIPAddress(String originalIPAddress) {

        StringTokenizer t = new StringTokenizer(originalIPAddress, ".");
        String inverted = t.nextToken();

        while (t.hasMoreTokens()) {
            inverted = t.nextToken() + "." + inverted;
        }

        return inverted;

    }


    def storeIpAddress(CommonsMultipartFile uploadedFile, Traffic traffic) {
        if (uploadedFile?.bytes) {
            def webRootDir = AppUtil.staticResourcesDirPath
            def userDir = new File(webRootDir, "/uploadedFile/ipAddress/${traffic?.id}/")
            userDir.mkdirs()

            String fileName = uploadedFile.originalFilename.trim()
            uploadedFile.transferTo(new File(userDir, fileName))
            IpAddressDocuments ipAddressDocuments = new IpAddressDocuments()
            ipAddressDocuments.name = fileName
            ipAddressDocuments.contentType = uploadedFile.contentType
            ipAddressDocuments.traffic = traffic

            AppUtil?.save(ipAddressDocuments)
        }
    }

    def storeIpAddressFromSyslogFile(CommonsMultipartFile uploadedFile, Traffic traffic) {
        if (uploadedFile?.bytes) {
            def webRootDir = AppUtil.staticResourcesDirPath
            String fileName = uploadedFile.originalFilename.trim()

            def ipsList = upload(uploadedFile)
            StringBuffer data = new StringBuffer()
            File extractedFile = new File(webRootDir, "/uploadedFile/ipAddress/${traffic?.id}/ipFromSyslogFile${fileName}")
            ipsList.each { ip ->
                data.append("${ip}\n")
            }
            FileUtils.writeStringToFile(extractedFile, data.toString());

            IpAddressDocuments ipAddressDocuments = new IpAddressDocuments()
            ipAddressDocuments.name = fileName
            ipAddressDocuments.contentType = uploadedFile.contentType
            ipAddressDocuments.traffic = traffic

            AppUtil?.save(ipAddressDocuments)
        }
    }

    def storeSystemLogs(CommonsMultipartFile uploadedFile, Traffic traffic) {
        if (uploadedFile?.bytes) {
            def webRootDir = AppUtil.staticResourcesDirPath
            def userDir = new File(webRootDir, "/uploadedFile/sysLogs/${traffic?.id}/")
            userDir.mkdirs()

            String fileName = uploadedFile.originalFilename.trim()
            uploadedFile.transferTo(new File(userDir, fileName))
            SystemLogsDocuments systemLogsDocuments = new SystemLogsDocuments()
            systemLogsDocuments.name = fileName
            systemLogsDocuments.contentType = uploadedFile.contentType
            systemLogsDocuments.traffic = traffic

            AppUtil?.save(systemLogsDocuments)
        }
    }


    def upload(def file) {
        def ipsList = []
        def reader = new BufferedReader(new InputStreamReader(file.inputStream));
        String line
        String ipRegex = "\\d{1,3}(?:\\.\\d{1,3}){3}(?::\\d{1,5})?";
        Pattern ipPattern = Pattern.compile(ipRegex);
        while ((line = reader.readLine()) != null) {
            def matcher = ipPattern.matcher(line)
            if (matcher.find()) {
                ipsList.add(matcher.group())
            }
        }
        return ipsList
    }


    public static int randInt(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }


    public static void removeFirstLine(String file, Long cacheReplacement) throws IOException {

        List listA1 = null;

        // FileInputStream f1 = new
        // FileInputStream("/home/narendra/Desktop/iplist1.txt");

        FileInputStream f1 = new FileInputStream(file)
        BufferedInputStream b1 = new BufferedInputStream(f1);
        DataInputStream d1 = new DataInputStream(b1);
        String s1 = null;
        listA1 = new LinkedList();

        while ((s1 = d1.readLine()) != null) {
            StringTokenizer sss1 = new StringTokenizer(s1, " ");
            while (sss1.hasMoreTokens()) {
                listA1.add(sss1.nextToken());
            }
        }

        // test = (String[]) listA1.toArray(new String[0]);

        System.out.println("-----------1------------------" + listA1.size());
        System.out.println("-----------cacheReplacement------------------" + cacheReplacement);

        if (cacheReplacement == 1 as Long) {

            println("----------------------listA1?.first()---------------" + listA1?.first())
            listA1.removeFirst()

        } else if (cacheReplacement == 2 as Long) {
            println("----------------------listA1?.last()---------------" + listA1?.last())
            listA1.removeLast()

        } else if (cacheReplacement == 3 as Long) {
            int maximum = listA1.size() - 1
            int minimum = 2
            int randomNum = minimum + (int) (Math.random() * maximum);
            println("----------------------listA1-----random---------" + listA1?.get(randomNum))

            listA1.remove(randomNum)
        } else {

        }



        System.out.println("-----------2------------------" + listA1.size());

        PrintWriter writer = new PrintWriter(file)
        writer.print("");
        writer.close();

        String[] test = (String[]) listA1.toArray(new String[0]);

        for (int j = 0; j < test.length; j++) {
            BufferedWriter bw_one = new BufferedWriter(new FileWriter(file, true));
            bw_one.write(test[j] + "\r\n");
            bw_one.close();
        }

    }


    public List giveList(String file) {
        LinkedList listA1 = null;

        // FileInputStream f1 = new
        // FileInputStream("/home/narendra/Desktop/iplist1.txt");

        FileInputStream f1 = new FileInputStream(file)
        BufferedInputStream b1 = new BufferedInputStream(f1);
        DataInputStream d1 = new DataInputStream(b1);
        String s1 = null;
        listA1 = new LinkedList();

        while ((s1 = d1.readLine()) != null) {
            StringTokenizer sss1 = new StringTokenizer(s1, " ");
            while (sss1.hasMoreTokens()) {
                listA1.add(sss1.nextToken());
            }
        }

        return listA1
    }

}

