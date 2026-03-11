<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ include file="include/header.jsp" %>
<%@ include file="include/left_navi.jsp" %>
<style>
    .table td {
        white-space: normal !important;
        word-wrap: break-word !important;
        word-break: break-word !important;
        overflow-wrap: break-word !important;
        /* adjust width if needed */
    }
</style>
<div class="main-content">
    <div class="page-content">
            <div class="page-loader"
            style="display:none; position:fixed; top:0; left:0; width:100%; height:100%; background:rgba(255, 255, 255, 0.8); text-align:center; z-index:9999;">
            <img src="${pageContext.request.contextPath}/assets/images/animated_loader_gif.gif" alt="Loading..."
                style="position:relative; top:50%; transform:translateY(-50%);" />
        </div>
        <div class="container-fluid">
            <!-- start page title -->
            <div class="row">
                <div class="col-12">
                    <div class="page-title-box d-sm-flex align-items-center justify-content-between">
                        <h5 class="mb-sm-0 text-uppercase">Whatsapp</h5>
                        <form method="post" class="employeelistform" action="${pageContext.request.contextPath}/">
                            <input type="hidden" name="employeelist" class="employeelist" value="active" />
                        </form>
                        <div class="page-title-right">
                            <ol class="breadcrumb m-0">
                                <li class="breadcrumb-item text-uppercase"><a href="javascript: void(0);">Whatsapp</a>
                                </li>
                                <li class="breadcrumb-item active text-uppercase">Logs</li>
                            </ol>
                        </div>
                    </div>
                </div>
            </div>


            <div class="row">
                <div class="col-lg-12">
                    <div class="card">
                        <div class="card-header">
                            <div class="row">
                                <div class="col-md-6">
                                    <h5 class="card-title mb-0 text-uppercase">Whatsapp Logs</h5>
                                </div>

                            </div>


                        </div>
                        <div class="card-body">
                            <div id="" class="">

                                <div class="">
                                    <div class="">
                                        <div class="">
                                            <div class="table-responsive">
                                                <table class="table table-bordered align-middle text-center">
                                                    <thead class="table-light">
                                                        <tr>
                                                            <th>#</th>
                                                            <th>Mobile</th>
                                                            <th>Message</th>
                                                            <th>Status</th>
                                                            <th>Response</th>
                                                            <th>Date & Time</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <c:forEach var="log" items="${logs}" varStatus="st">
                                                            <tr>
                                                                <td>${st.index + 1}</td>
                                                                <td>${fn:escapeXml(log.mobile)}</td>
                                                                <td>${fn:escapeXml(log.message)}</td>
                                                                <td>
                                                                    <c:choose>
                                                                        <c:when test="${log.status == 'success'}">
                                                                            <span class="badge rounded-pill bg-success">${fn:toUpperCase(log.status)}</span>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <span class="badge rounded-pill bg-danger">${fn:escapeXml(log.status)}</span>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </td>
                                                                <td>${fn:escapeXml(log.response)}</td>
                                                                <td>${fn:escapeXml(log.createdAtFormatted)}</td>
                                                            </tr>
                                                        </c:forEach>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<%@ include file="include/footer.jsp" %>
</html>