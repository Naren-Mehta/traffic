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
                <g:form controller="trafficStudy" action="saveTrafficStudyPage" class="form-horizontal"
                        enctype="multipart/form-data" method="POST">

                    <div class="form-group text-left">
                        <h3>Welcome to the IP Cache</h3>


                        <g:render template="/common/aboutUserDetails"/>
                        <a href="#" role="button" class="btn btn-info"
                           data-target="#aboutUserDetails"
                           data-toggle="modal">About This Page</a>

                    </div>


                    <div class="form-group">
                    %{--<div class="col-sm-6 col-sm-offset-2">--}%
                        <g:hasErrors bean="${trafficCO}">
                            <ul class="alert alert-danger" role="alert" style="list-style: none">
                                <g:eachError bean="${trafficCO}" var="error">
                                    <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message
                                            error="${error}"/></li>
                                </g:eachError>
                            </ul>
                        </g:hasErrors>
                        <g:if test="${flash.message}">
                            <ul class="alert alert-danger" role="alert">
                                ${flash.message}
                            </ul>
                        </g:if>
                        ${flash.clear()}

                    %{--</div>--}%
                    </div>


                    <div class="form-group">
                        <label>Input batch size</label>
                    </div>

                    <div class="form-group">
                        <table class="table table-bordered">
                            <tr>
                                <td>
                                    <g:radio name="batchType" value="fixed" checked="checked"/>
                                    <label>Fixed</label>
                                </td>
                                <td>
                                    <input type="text" name="batchSize" value="${trafficCO?.batchSize}"
                                           placeholder="Input batch type">
                                </td>
                            </tr>

                            <tr>
                                <td>
                                    <g:radio name="batchType" value="random"/>
                                    <label>Rendom numbers between</label>
                                </td>
                                <td>
                                    <input type="text" name="batchSize1" value="${trafficCO?.batchSize}">

                                    <label>To</label>
                                    <input type="text" name="batchSize2" value="${trafficCO?.batchSize}">
                                </td>
                            </tr>
                        </table>

                    </div>


                    <div class="form-group">
                        <label>Choose you file type</label>


                        <g:select name="fileType" class="form-control"
                                  noSelection="['': '-select File selection menu -']"
                                  value="${trafficCO?.fileType}"
                                  from="${["IP address", "System log"]}"/>

                    </div>

                %{--<div class="form-group">--}%
                %{--<label>Please upload System log file</label>--}%
                %{--<input type="file" name="sysLogFile">--}%

                %{--</div>--}%

                    <div class="form-group">
                        <label>Please upload Ip list file</label>
                        <input type="file" name='uploadFiles[]' multiple>
                    </div>

                    <div class="form-group">
                        <label>Enter Replacing Sceheme . 1 for LRU and 2 for FIFO</label>


                        <g:select name="replacingScheme" class="form-control"
                                  noSelection="['': '-select File selection menu -']"
                                  value="${trafficCO?.replacingScheme}"
                                  from="${["1", "2"]}"/>

                    </div>

                    <div class="form-group">
                        <label>Initial local WL Cache size</label>
                        <input type="text" name="wlCacheSize" value="${trafficCO?.wlCacheSize}" class="form-control"
                               placeholder="WL Cache size">

                    </div>

                    <div class="form-group">
                        <label>Initial local BL Cache size</label>

                        <input type="text" name="blCacheSize" value="${trafficCO?.blCacheSize}" class="form-control"
                               placeholder="BL Cache size">
                    </div>


                    <div class="form-group">
                        <label>Select Type of DNSBLs</label><br>

                        <g:select name="dnsblNo.id" class="selectpicker form-control" id="example-getting-started"
                                  multiple="multiple" data-selected-text-format="count"
                                  from="${["zen.spamhaus.org", "l2.apews.org",
                                           "dnsbl.sorbs.net", "spam.dnsbl.sorbs.net",
                                           "bl.mailspike.net", "hostkarma.junkemailfilter.com", "dnsbl-1.uceprotect.net",
                                           "dnsbl-2.uceprotect.net", "dnsbl-3.uceprotect.net", "b.barracudacentral.org",
                                           "cbl.abuseat.org", "bl.tiopan.com", "psbl.surriel.com", "db.wpbl.info", "bl.spameatingmonkey.net",
                                           "dnsbl.inps.de", "all.spamrats.com", "truncate.gbudb.net", "ips.backscatterer.org", "ubl.unsubscore.com"]}"/>
                    </div>

                    <div class="form-group">
                        <input type="submit" class="btn btn-primary btn-lg" value="Simulate"/>
                    </div>
                </g:form>

            </div>

            <div class="col-md-2"></div>
        </div>
    </div>
</div>


<script type="text/javascript">
    $(document).ready(function () {
        $('#example-getting-started').multiselect();
    });
</script>

</body>
</html>
