<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ include file="../include/header.jsp" %>
<%@ include file="../include/left_navi.jsp" %>
<div class="main-content">
    <div class="page-content">
        <div class="container-fluid">

            <div class="row">
                <div class="col-12">
                    <div class="page-title-box d-sm-flex align-items-center justify-content-between">
                        <h5 class="mb-sm-0 text-uppercase">Appointment</h5>
                        <div class="page-title-right">
                            <ol class="breadcrumb m-0">
                                <li class="breadcrumb-item text-uppercase">
                                    <a href="${pageContext.request.contextPath}/admin/appointment/appointmentlist">Appointment</a>
                                </li>
                                <li class="breadcrumb-item active text-uppercase">List</li>
                            </ol>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-lg-12">
                    <div class="card">
                        <div class="card-body">

                            <h4 class="mb-3 fw-semibold">Appointment Details</h4>

                            <div class="row g-3 mb-4">
                                <div class="col-md-3">
                                    <div class="border rounded-3 p-3 bg-light">
                                        <small class="text-muted d-block">Token No #</small>
                                        <h6 class="mb-0">${appointment.todayTokenNo}</h6>
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <div class="border rounded-3 p-3 bg-light">
                                        <small class="text-muted d-block">Appointment ID</small>
                                        <h6 class="mb-0">${appointment.appointmentId}</h6>
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <div class="border rounded-3 p-3 bg-light">
                                        <small class="text-muted d-block">Doctor</small>
                                        <h6 class="mb-0">${appointment.doctorName}</h6>
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <div class="border rounded-3 p-3 bg-light">
                                        <small class="text-muted d-block">Patient</small>
                                        <h6 class="mb-0">${appointment.patientName}</h6>
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <div class="border rounded-3 p-3 bg-light">
                                        <small class="text-muted d-block">Location</small>
                                        <h6 class="mb-0">${not empty appointment.locationName ? appointment.locationName : 'N/A'}</h6>
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <div class="border rounded-3 p-3 bg-light">
                                        <small class="text-muted d-block">Scan Type</small>
                                        <h6 class="mb-0">${not empty appointment.scanType ? appointment.scanType : 'N/A'}</h6>
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <div class="border rounded-3 p-3 bg-light">
                                        <small class="text-muted d-block">Date</small>
                                        <h6 class="mb-0">${appointment.appointmentDate}</h6>
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <div class="border rounded-3 p-3 bg-light">
                                        <small class="text-muted d-block">Time</small>
                                        <h6 class="mb-0">${appointment.appointmentStartTime}</h6>
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <div class="border rounded-3 p-3 bg-light">
                                        <small class="text-muted d-block">End Time</small>
                                        <h6 class="mb-0">${appointment.appointmentEndTime}</h6>
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <div class="border rounded-3 p-3 bg-light">
                                        <small class="text-muted d-block">Status</small>
                                        <h6 class="mb-0">
                                            <c:choose>
                                                <c:when test="${appointment.appointmentStatus == '1'}"><span class="badge bg-primary">Open</span></c:when>
                                                <c:when test="${appointment.appointmentStatus == '2'}"><span class="badge bg-success">Completed</span></c:when>
                                                <c:when test="${appointment.appointmentStatus == '3'}"><span class="badge bg-danger">Cancelled</span></c:when>
                                                <c:when test="${appointment.appointmentStatus == '4'}"><span class="badge bg-warning">Delayed</span></c:when>
                                                <c:otherwise><span class="badge bg-secondary">${appointment.appointmentStatus}</span></c:otherwise>
                                            </c:choose>
                                        </h6>
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <div class="border rounded-3 p-3 bg-light">
                                        <small class="text-muted d-block">Created At</small>
                                        <h6 class="mb-0">${appointment.createdAt}</h6>
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <div class="border rounded-3 p-3 bg-light">
                                        <small class="text-muted d-block">Last Updated</small>
                                        <h6 class="mb-0">${appointment.updatedAt}</h6>
                                    </div>
                                </div>
                            </div>

                            <h5 class="fw-semibold mb-2">Uploaded Documents</h5>
                            <div class="table-responsive mb-4">
                                <table class="table table-bordered table-hover align-middle">
                                    <thead class="table-light text-uppercase small">
                                        <tr>
                                            <th width="5%">#</th>
                                            <th width="25%">Type</th>
                                            <th width="40%">File</th>
                                            <th width="20%">Uploaded</th>
                                            <th width="10%">Action</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:choose>
                                            <c:when test="${not empty documents}">
                                                <c:forEach var="doc" items="${documents}" varStatus="st">
                                                    <tr>
                                                       <td>${st.count}</td>
                                                       <td>${doc.docType}</td>
                                                       <td><a href="${doc.url}" target="_blank" class="btn btn-sm btn-outline-secondary" type="button" 
                                                         onclick="window.open('${doc.url}','_blank','toolbar=0,location=0,menubar=0'); return false;">View</a></td>
                                                       <td>${doc.uploadedAt}</td>
                                                       <td><a href="${pageContext.request.contextPath}/adminmodel/deleteDocument/${doc.id}" 
                                                            onclick="return confirm('Delete this document?')" 
                                                            class="btn btn-sm btn-outline-danger">Delete</a></td>
                                                    </tr>
                                                </c:forEach>
                                            </c:when>
                                            <c:otherwise>
                                                <tr><td colspan="5" class="text-center text-muted py-3">No documents uploaded</td></tr>
                                            </c:otherwise>
                                        </c:choose>
                                    </tbody>
                                </table>
                            </div>

                            <h5 class="fw-semibold mb-3">Upload New Document</h5>
                            <h5 class="mt-4">Upload New Report</h5>
                            <form method="post" enctype="multipart/form-data"
                                action="${pageContext.request.contextPath}/adminmodel/uploadAppointmentDocument">
                                <input type="hidden" name="appointmentId" value="${appointment.appointmentId}">
                                <input type="hidden" name="patientId" value="${appointment.patientId}">
                                <div class="row">
                                    <div class="col-md-4">
                                        <label>Document Type</label>
                                        <select name="docType" class="form-control" required>
                                            <option value="">Select</option>
                                            <option value="SCAN_REPORT">Scan Report</option>
                                            <option value="LAB_REPORT">Lab Report</option>
                                            <option value="PRESCRIPTION">Prescription</option>
                                            <option value="OTHER">Other</option>
                                        </select>
                                    </div>
                                    <div class="col-md-4">
                                        <label>File</label>
                                        <input type="file" name="documentFile" class="form-control" required>
                                    </div>
                                    <div class="col-md-4 mt-4">
                                        <button class="btn btn-primary">Upload</button>
                                    </div>
                                </div>
                            </form>

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<%@ include file="../include/footer.jsp" %>
</html>
