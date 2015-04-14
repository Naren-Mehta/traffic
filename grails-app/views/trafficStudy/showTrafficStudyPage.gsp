<%@ page import="traffic.Dnsbl" %>
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
                        <h3>Enter IP cache parameters</h3>


                        <g:render template="/common/aboutUserDetails"/>
                        <a href="#" role="button" class="btn btn-info"
                           data-target="#aboutUserDetails"
                           data-toggle="modal">About</a>

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
                        <label>Input Batch Size</label>
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
                                    <label>Random number between</label>
                                </td>
                                <td>
                                    <input type="text" name="batchSize1" value="${trafficCO?.batchSize}">

                                    <label>and</label>
                                    <input type="text" name="batchSize2" value="${trafficCO?.batchSize}">
                                </td>
                            </tr>
                        </table>

                    </div>


                    <div class="form-group">
                        <label>File type</label>


                        <g:select name="fileType" class="form-control"
                                  noSelection="['': '- File Selection menu -']"
                                  value="${trafficCO?.fileType}"
                                  from="${["IP address", "System log"]}"/>

                    </div>

                %{--<div class="form-group">--}%
                %{--<label>Please upload System log file</label>--}%
                %{--<input type="file" name="sysLogFile">--}%

                %{--</div>--}%

                    <div class="form-group">
                        <label>Please upload File type selected above</label>
                        <input type="file" name='uploadFiles[]' multiple>
                    </div>

                %{--<div class="form-group">--}%
                %{--<label>Enter Cache Replacement Scheme: 0 for NONE and 1 for FIFO</label>--}%


                %{--<g:select name="replacingScheme" class="form-control"--}%
                %{--noSelection="['': '-Cache Replacement menu -']"--}%
                %{--value="${trafficCO?.replacingScheme}"--}%
                %{--from="${["0", "1"]}"/>--}%

                %{--</div>--}%

                    <div class="form-group">
                        <label>Enter Cache Replacement Scheme: 0 for NONE and 1 for FIFO</label>
                        <input type="text" name="replacingScheme" value="1" readonly class="form-control">

                    </div>

                    <div class="form-group">
                        <label>Local WL Cache size</label>

                        <g:select name="wlCacheSize" class="form-control"
                                  noSelection="['': '-Max no. of entries in WL Cache -']"
                                  value="${trafficCO?.wlCacheSize}"
                                  from="${["100", "500",'1000']}"/>

                        %{--<input type="text" name="wlCacheSize" value="${trafficCO?.wlCacheSize}" class="form-control"--}%
                               %{--placeholder="Max no. of entries in WL Cache">--}%

                    </div>

                    <div class="form-group">
                        <label>Local BL Cache size</label>
                        <g:select name="blCacheSize" class="form-control"
                                  noSelection="['': '-Max no. of entries in BL Cache -']"
                                  value="${trafficCO?.blCacheSize}"
                                  from="${["100", "500",'1000']}"/>
                        %{--<input type="text" name="blCacheSize" value="${trafficCO?.blCacheSize}" class="form-control"--}%
                               %{--placeholder="Max no. of entries in BL Cache ">--}%
                    </div>


                    <div class="form-group">
                        <label>Select DNSBLs</label><br>

                        <g:select name="dnsblNo.id" class="selectpicker form-control" id="example-getting-started"
                                  multiple="multiple" data-selected-text-format="count"
                                  from="${traffic.Dnsbl?.list()}"/>
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
