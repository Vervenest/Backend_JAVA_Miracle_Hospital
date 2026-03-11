<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ include file="../include/header.jsp" %>
<%@ include file="../include/left_navi.jsp" %>

<div class="main-content">
<div class="page-content">
<div class="container-fluid">

  <div class="row mb-3">
    <div class="col-12">
      <div class="page-title-box d-sm-flex align-items-center justify-content-between">
        <h5 class="mb-sm-0 text-uppercase">Patient</h5>
        <div class="page-title-right">
          <ol class="breadcrumb m-0">
            <li class="breadcrumb-item">
              <a href="${pageContext.request.contextPath}/admin/patient/patientlist">PATIENT</a>
            </li>
            <li class="breadcrumb-item active">DETAILS</li>
          </ol>
        </div>
      </div>
    </div>
  </div>

  <c:if test="${not empty errorMessage}">
    <div class="alert alert-danger">${errorMessage}</div>
  </c:if>

  <c:if test="${not empty patient}">

    <div class="card shadow-sm border-0">
      <div class="card-body">

        <!-- Title -->
        <h5 class="fw-bold mb-3">
          PATIENT DETAILS - <span style="color:#e07a1f;">(${fn:toUpperCase(patient.patientName)})</span>
        </h5>

        <!-- Tabs -->
        <ul class="nav nav-tabs mb-4" id="patientTab">
          <li class="nav-item">
            <a class="nav-link active" id="profile-tab" data-bs-toggle="tab" href="#profileTab">
              Profile &amp; Pregnancy
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link" id="appt-tab" data-bs-toggle="tab" href="#apptTab">
              Appointments
            </a>
          </li>
        </ul>

        <div class="tab-content" id="patientTabContent">

          <!-- ── PROFILE & PREGNANCY TAB ── -->
          <div class="tab-pane fade show active" id="profileTab">
            <div class="row">

              <!-- Basic Information -->
              <div class="col-lg-6">
                <h6 class="text-muted mb-3">Basic Information</h6>
                <table class="table table-borderless" style="font-size:15px;">
                  <tbody>
                    <tr>
                      <td style="width:140px;color:#555;font-weight:500;">Name</td>
                      <td>${not empty patient.patientName ? patient.patientName : '—'}</td>
                    </tr>
                    <tr>
                      <td style="color:#555;font-weight:500;">Phone</td>
                      <td>${not empty patient.phoneNumber ? patient.phoneNumber : '—'}</td>
                    </tr>
                    <tr>
                      <td style="color:#555;font-weight:500;">Relation</td>
                      <td>${not empty patient.relation ? patient.relation : '—'}</td>
                    </tr>
                    <tr>
                      <td style="color:#555;font-weight:500;">Gender</td>
                      <td>${not empty patient.patientGender ? patient.patientGender : '—'}</td>
                    </tr>
                    <tr>
                      <td style="color:#555;font-weight:500;">DOB</td>
                      <td>${not empty patient.patientDateOfBirth ? patient.patientDateOfBirth : '—'}</td>
                    </tr>
                    <tr>
                      <td style="color:#555;font-weight:500;">Age</td>
                      <td>${not empty patient.patientAge ? patient.patientAge : '—'}</td>
                    </tr>
                  </tbody>
                </table>
              </div>

              <!-- Pregnancy Information -->
              <div class="col-lg-6">
                <h6 class="text-muted mb-3">Pregnancy Information</h6>
                <c:choose>
                  <c:when test="${patient.isPregnant == '1'}">
                    <div class="rounded p-3" style="background:#fffbeb;border:1px solid #fde68a;">
                      <div class="mb-3" style="color:#d97706;font-weight:600;">
                        <i class="ri-alert-line me-1"></i> Pregnant Patient
                      </div>
                      <table class="table table-borderless mb-0" style="font-size:15px;">
                        <tbody>
                          <tr>
                            <td style="width:160px;font-weight:500;">Status</td>
                            <td>
                              <span class="badge"
                                    style="background:#f59e0b;padding:6px 14px;font-size:13px;border-radius:20px;">
                                Currently Pregnant
                              </span>
                            </td>
                          </tr>
                          <tr>
                            <td style="font-weight:500;">LMP Date</td>
                            <td>${not empty patient.lmpDate ? patient.lmpDate : '—'}</td>
                          </tr>
                          <tr>
                            <td style="font-weight:500;">Expected Delivery</td>
                            <td>${not empty patient.expectedDeliveryDate ? patient.expectedDeliveryDate : '—'}</td>
                          </tr>
                          <tr>
                            <td style="font-weight:500;">Gestational Age</td>
                            <td id="gestationalAge">—</td>
                          </tr>
                        </tbody>
                      </table>
                    </div>
                  </c:when>
                  <c:otherwise>
                    <div class="rounded p-3" style="background:#f9fafb;border:1px solid #e5e7eb;">
                      <p class="text-muted mb-0">Not pregnant</p>
                    </div>
                  </c:otherwise>
                </c:choose>

                <!-- Edit Button -->
                <div class="mt-3">
                  <a href="${pageContext.request.contextPath}/admin/patient/editpatient/${patient.patientStringId}"
                     class="btn btn-primary btn-sm">
                    <i class="ri-edit-box-line me-1"></i> Edit Information
                  </a>
                </div>
              </div>

            </div>
          </div><!-- end profileTab -->

          <!-- ── APPOINTMENTS TAB ── -->
          <div class="tab-pane fade" id="apptTab">
            <div class="table-responsive">
              <table class="table table-bordered align-middle">
                <thead class="table-light text-uppercase" style="font-size:13px;">
                  <tr>
                    <th style="width:50px;">#</th>
                    <th>Doctor</th>
                    <th>Date</th>
                    <th>Time</th>
                    <th>Status</th>
                  </tr>
                </thead>
                <tbody>
                  <c:choose>
                    <c:when test="${not empty appointmentList}">
                      <c:forEach var="appt" items="${appointmentList}" varStatus="st">
                        <tr>
                          <td>${st.count}</td>
                          <td>${not empty appt.doctorName ? appt.doctorName : '—'}</td>
                          <td>${appt.appointmentDate}</td>
                          <td>${appt.appointmentStartTime}</td>
                          <td>
                            <c:choose>
                              <c:when test="${appt.appointmentStatus == '1'}">
                                <span class="badge bg-primary">Scheduled</span>
                              </c:when>
                              <c:when test="${appt.appointmentStatus == '2'}">
                                <span class="badge bg-success">Completed</span>
                              </c:when>
                              <c:when test="${appt.appointmentStatus == '3'}">
                                <span class="badge bg-danger">Cancelled</span>
                              </c:when>
                              <c:otherwise>
                                <span class="badge bg-secondary">${appt.appointmentStatus}</span>
                              </c:otherwise>
                            </c:choose>
                          </td>
                        </tr>
                      </c:forEach>
                    </c:when>
                    <c:otherwise>
                      <tr>
                        <td colspan="5" class="text-center text-muted py-4">No appointments found</td>
                      </tr>
                    </c:otherwise>
                  </c:choose>
                </tbody>
              </table>
            </div>
          </div><!-- end apptTab -->

        </div><!-- tab-content -->
      </div>
    </div>

  </c:if>

  <c:if test="${empty patient}">
    <div class="alert alert-warning">Patient not found.</div>
  </c:if>

</div>
</div>
</div>

<script>
// Calculate gestational age from LMP date
(function() {
  var lmpStr = '${patient.lmpDate}';
  if (!lmpStr) return;
  var lmp  = new Date(lmpStr);
  var today = new Date();
  var diffMs   = today - lmp;
  var diffDays = Math.floor(diffMs / (1000 * 60 * 60 * 24));
  if (diffDays < 0) return;
  var weeks = Math.floor(diffDays / 7);
  var days  = diffDays % 7;
  var el = document.getElementById('gestationalAge');
  if (el) el.textContent = weeks + ' weeks ' + days + ' days';
})();
</script>

<%@ include file="../include/footer.jsp" %>
</html>
