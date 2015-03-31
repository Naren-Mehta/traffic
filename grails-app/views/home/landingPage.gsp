<html>
<head>
    <meta name="layout" content="main"/>
    <title>IP caches</title>
</head>

<body>
<div class="container">
    <div class="row">

        <div class="form-group text-center">
            <h2>IP cache - Traffic Study</h2>

        </div>


        <div class="span4">
            <div class="">
                <dl class="dl-horizontal">
                    <dt>About</dt>
                    <dd>We implement IP address caches to filter out the IP addresses.
                    The local caches (WL and BL) initially are empty and get filled up according to the responses from the DNSBL queries.
                    An IP address is added to the WL cache for all ‘misses’ or negative responses from the DNSBL and to the BL cache for all ‘hits’ or positive responses.

                    </dd>
                </dl>
                <dl class="dl-horizontal">
                    <dt>Batches</dt>
                    <dd>
                        Batch can be defined as the total number of IP addresses sent for the DNSBL query for single simulation run
                        of the code. For example a list extracted from a logged file, such as: 1,23,5,3,6,8,43,2,1,34,5,6,7
                        could be divided into three batches of 1,23,5,3,6 and 8,43,2,1 and 34,5,6,7 in order. The goals are:

                        <div><br></div>

                        <div class="span4">
                            <div class="">
                                <dl class="dl-horizontal">
                                    <dt>1</dt>
                                    <dd>To observe the growth of the hit rates as we fill up the caches,
                                     </dd>
                                </dl>
                                <dl class="dl-horizontal">
                                    <dt>2</dt>
                                    <dd>
                                        To find where the hit rates begin to saturate to get an estimation of the optimal cache
                                        size.
                                    </dd>
                                </dl>
                            </div>
                        </div>

                    </dd>
                </dl>
                <dl class="dl-horizontal">
                    <dt>Process</dt>
                    <dd>
                    The senders’ IP addresses are first compared with the list in the local caches. If a sender is on the WL, further search in the BL cache and the external DNSBL query is not required; it will be sent directly to the user’s mail box (Hit++ in WL updated).
                    Otherwise, the entry is searched at the BL cache and if it is found here (Hit++ in BL updated), the e-mail is rejected.
                    If the IP address is not found in either of the caches (Miss++ in local caches), an external DNSBL query is carried out with that IP address to determine if it is listed in one or more DNSBLs.</dd>
                </dl>


                <dl class="dl-horizontal">
                    <dt>Memory Trace</dt>
                    <dd>Syslog</dd>
                    <dd>IP List</dd>
                </dl>

            </div>
        </div>


        <div class="col-md-12">
            <div class="col-md-2"></div>

            <div class="col-md-8">

                <div class="form-group text-center">
                    <g:link controller="trafficStudy" action="showTrafficStudyPage"
                            class="btn btn-primary btn-lg">Enter Traffic Study Parameters</g:link>
                </div>

            </div>

            <div class="col-md-2"></div>

        </div>
    </div>
</div>
</body>
</html>