package traffic

import org.springframework.web.multipart.commons.CommonsMultipartFile
import utils.AppUtil


class TrafficUtilService {


    public Boolean performLogic(Traffic traffic) {

        StaticsResults staticsResults = null
        Boolean isSuccessful = false
        IpAddressDocuments ipAddressDocuments = IpAddressDocuments?.findByTraffic(traffic)

        String ipAddressName = ipAddressDocuments?.name
        def webRootDir = AppUtil.staticResourcesDirPath
        String filename = webRootDir + "uploadedFile/ipAddress/${traffic?.id}/" + ipAddressName
        String[] aaa = [filename]

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

            for (int j = 0; j < test.length; j++) {

                if (traffic?.batchType?.equals("fixed")) {
                    batchSize = traffic?.batchSize as Long
                } else {
                    batchSize = randInt(traffic?.batchSize1 as int, traffic?.batchSize2 as int) as Long
                }

                for (int jj = j; jj < (j + batchSize); jj++) {
                    FileInputStream fis = null;
                    fis = new FileInputStream(webRootDir + "/files/WL_Cache.txt");

                    println("---------------WL_Cache-------------------" + webRootDir + "files/BL_Cache.txt")

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
                    System.out.println(" \nSTART of SEARCH.........!!!");
                    // /SEARCHING ARRAY ONE /whitelist cache
                    for (int i = 0; i < a.length; i++) {
                        // try changing here i, j swap
                        if (test[j].equals(a[i])) {
                            is1 = true;
                        }
                    }

                    if (is1) {
                        System.out.println(" it's here in WL!!!");
                        foundinWL++;

                    } else { // /SEARCHING ARRAY TWO /blacklist cache
                        System.out.println(" not in WL...now searching BL ....");
                        boolean is2 = false;
                        for (int i = 0; i < b.length; i++) {
                            if (test[j].equals(b[i])) {
                                is2 = true;
                            }
                        }
                        if (is2) {
                            System.out.println(" it's here in BL!!!");
                            foundinBL++;
                        } else {
                            System.out.println(" not in BL ..now DNSBL query..");

                            // /DOING DNSBL QUERY


                            Boolean ifSuccess = false
                            List<DnsblsUrls> dnsblsUrlses = DnsblsUrls?.findAllByTraffic(traffic)
                            if (dnsblsUrlses) {
                                dnsblsUrlses?.find { DnsblsUrls dnsblsUrl ->

                                    ifSuccess = checkRelay(test[j], dnsblsUrl?.urlName)

                                    if (ifSuccess) {
                                        return true
                                    }
                                }
                            }


                            println("--------------ifSuccess-----------------------" + ifSuccess)


                            if (ifSuccess) {
                                println("-----------in---ifSuccess-------if----------------")

                                System.out.println(" it's a hit in the DNS_BL!");
                                hitindnsbl++;
                                // WRITING IN THE BLACKLIST CACHE
                                if (listA.indexOf(test[j]) < 0) {

                                    if ((traffic?.replacingScheme == 1 as Long) && traffic?.blCacheSize > 0) {
                                        if (listB.size() > traffic?.blCacheSize) {
                                            String bllineToRemove = listB.getFirst();
                                            updateFile(webRootDir + "files/BL_Cache.txt", webRootDir + "files/New_BL_Cache.txt", bllineToRemove);
                                        }
                                    }

                                    BufferedWriter bw_one = new BufferedWriter(
                                            new FileWriter(webRootDir + "files/BL_Cache.txt", true));


                                    bw_one.write(test[j] + "\r\n");
                                    bw_one.close();
                                    System.out
                                            .println("latest size of b (blacklist cache):"
                                            + (b.length + 1));

                                }

                            } else {
                                System.out
                                        .println(" it's a miss in the DNS_BL!.....");
                                missindnsbl++;
                                // WRITING IN THE WHITELIST CACHE
                                if (listB.indexOf(test[j]) < 0) {

                                    if ((traffic?.replacingScheme == 1 as Long) && traffic?.wlCacheSize > 0) {
                                        if (listA.size() > traffic?.wlCacheSize) {
                                            String wllineToRemove = listA.getFirst();
                                            updateFile(webRootDir + "files/WL_Cache.txt", webRootDir + "files/New_WL_Cache.txt", wllineToRemove);
                                        }
                                    }


                                    BufferedWriter bw_two = new BufferedWriter(
                                            new FileWriter(webRootDir + "files/WL_Cache.txt", true));


                                    bw_two.write(test[j] + "\r\n");
                                    bw_two.close();

                                    System.out
                                            .println("latest size a (whitelist cache):"
                                            + (a.length + 1));

                                }
                            }


                        } // DNSBL query
                    }
                }

                j = j + (batchSize - 1);

                // search BL end

                stop = System.currentTimeMillis();

                // writing in the stats


                staticsResults = new StaticsResults()
                staticsResults?.totalTestData = (hitindnsbl + missindnsbl + foundinWL + foundinBL) as Integer
                staticsResults?.foundInWL = foundinWL as Integer
                staticsResults?.foundInBL = foundinBL as Integer
                staticsResults?.hitInDNSBL = hitindnsbl as Integer
                staticsResults?.missInDNSBL = missindnsbl as Integer
                staticsResults?.totalTimeTaken = ((stop - start) / 60000) as BigDecimal
                staticsResults?.date = new Date()
                staticsResults?.traffic = traffic

                AppUtil?.save(staticsResults)

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


    public static int randInt(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }


    private static void updateFile(String srFile, String dtFile, String delItem) throws IOException {

        //if(listB.size()>100){
        File inputFile = new File(srFile);
        File tempFile = new File(dtFile);

        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

        String lineToRemove = delItem;
        //(String)listB.getFirst();
        //String lineToRemove2 =  (String)listB.getLast();

        String currentLine = "";

        //System.out.println(" Last string: "+ lineToRemove2+"  and  first string: "+ lineToRemove);

        while ((currentLine = reader.readLine()) != null) {
            String trimmedLine = currentLine.trim();
            if (trimmedLine.equals(lineToRemove))
                continue;
            writer.write(currentLine);
            writer.write("\r\n");
        }
        writer.close();
        copyfile(dtFile, srFile);
        //} // update portion
    }
}

