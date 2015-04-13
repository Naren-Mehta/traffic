<html>
<head>
    <meta name="layout" content="main"/>
    <title>Traffic Study</title>
</head>

<body>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <div class="col-md-2"></div>

            <div class="col-md-8">

                <h1>Result</h1>

                <div class="form-group">
                    <g:link controller="trafficStudy" action="showTrafficStudyPage"
                            class="btn btn-primary btn-lg">Simulate again</g:link>
                </div>

                <g:render template="/common/aboutResultPage"/>
                <a href="#" role="button" class="btn btn-info"
                   data-target="#aboutResultPage"
                   data-toggle="modal">About</a>


                <table class="table table-bordered table-responsive">

                    <tr>
                        <td>Total test data</td>
                        <td>Found in WL</td>
                        <td>Found in BL</td>
                        <td>Hit in DNSBL</td>
                        <td>Miss in DNSBL</td>
                        <td>Total time taken</td>

                        <td>WL cache size</td>
                        <td>BL cache size</td>

                        <td>Date</td>
                    </tr>

                    <g:each in="${staticsResultsList}" var="staticsResult">
                        <tr>
                            <td>${staticsResult?.totalTestData}</td>
                            <td>${staticsResult?.foundInWL}</td>
                            <td>${staticsResult?.foundInBL}</td>
                            <td>${staticsResult?.hitInDNSBL}</td>
                            <td>${staticsResult?.missInDNSBL}</td>
                            <td>${staticsResult?.totalTimeTaken} seconds</td>

                            <td>${staticsResult?.wlCatchSize}</td>
                            <td>${staticsResult?.blCatchSize}</td>

                            <td>${staticsResult?.date}</td>
                        </tr>
                    </g:each>

                </table>

            </div>

            <div class="col-md-2"></div>
        </div>
    </div>
</div>
</body>
</html>
