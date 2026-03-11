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
        <h5 class="mb-sm-0 text-uppercase">Manage Patients</h5>
        <div class="page-title-right">
          <ol class="breadcrumb m-0">
            <li class="breadcrumb-item"><a href="javascript:void(0);">Patient</a></li>
            <li class="breadcrumb-item active">List</li>
          </ol>
        </div>
      </div>
    </div>
  </div>

  <!-- Search & Filter Bar -->
  <div class="row mb-3">
    <div class="col-md-5">
      <input type="text" id="searchInput" class="form-control"
             placeholder="Search Name/Phone" oninput="filterTable()">
    </div>
    <div class="col-md-4">
      <select id="statusFilter" class="form-select" onchange="filterTable()">
        <option value="">All Status</option>
        <option value="active">Active</option>
        <option value="inactive">Inactive</option>
      </select>
    </div>
    <div class="col-md-3">
      <button class="btn btn-primary w-100" onclick="filterTable()">
        <i class="ri-filter-line me-1"></i> Filter
      </button>
    </div>
  </div>

  <c:if test="${not empty errorMessage}">
    <div class="alert alert-danger">${errorMessage}</div>
  </c:if>

  <div class="row">
    <div class="col-12">
      <div class="card shadow-sm">
        <div class="card-body p-0">
          <div class="table-responsive">
            <table class="table table-bordered align-middle mb-0" id="patientTable">
               <thead class="text-uppercase" style="background:#f3f6f9; color:#000;">
                <tr>
                  <th style="width:100px;" class="ps-3">S. NO.</th>
                  <th>NAME</th>
                  <th>PHONE NUMBER</th>
                  <th>STATUS</th>
                  <th>PREGNANT</th>
                  <th style="width:110px;">ACTION</th>
                </tr>
              </thead>
              <tbody id="patientTableBody">
                <c:choose>
                  <c:when test="${not empty patientList}">
                    <c:forEach var="row" items="${patientList}" varStatus="st">
                      <tr data-name="${fn:toLowerCase(row.patientName)}"
                          data-phone="${row.phoneNumber}"
                          data-active="${(row.isActive == true and row.patientStatus != '0') ? 'active' : 'inactive'}">
                        <td class="ps-3">${st.count}</td>
                        <td><strong>${not empty row.patientName ? row.patientName : '—'}</strong></td>
                        <td>${row.phoneNumber}</td>
                        <td>
                          <c:choose>
                            <c:when test="${row.isActive == true and row.patientStatus != '0'}">
                              <span class="badge rounded-pill"
                                    style="background:#10b981;padding:6px 14px;font-size:12px;">Active</span>
                            </c:when>
                            <c:otherwise>
                              <span class="badge rounded-pill"
                                    style="background:#ef4444;padding:6px 14px;font-size:12px;">Inactive</span>
                            </c:otherwise>
                          </c:choose>
                        </td>
                        <td>
                          <c:choose>
                            <c:when test="${row.isPregnant == '1'}">
                              <span class="badge rounded-pill"
                                    style="background:#f59e0b;padding:5px 12px;font-size:12px;">
                                <i class="ri-checkbox-circle-line me-1"></i>Yes
                              </span>
                              <c:if test="${not empty row.lmpDate}">
                                <div class="text-muted mt-1" style="font-size:11px;">LMP: ${row.lmpDate}</div>
                              </c:if>
                            </c:when>
                            <c:otherwise>
                              <span class="badge rounded-pill"
                                    style="background:#6b7280;padding:5px 12px;font-size:12px;">No</span>
                            </c:otherwise>
                          </c:choose>
                        </td>
                        <td>
                          <a href="${pageContext.request.contextPath}/admin/patient/editpatient/${row.patientStringId}"
                             class="btn btn-sm btn-outline-secondary me-1" title="Edit"
                             style="border-radius:6px;">
                            <i class="ri-edit-box-line"></i>
                          </a>
                          <a href="${pageContext.request.contextPath}/admin/patient/patientDetails/${row.patientStringId}"
                             class="btn btn-sm btn-outline-info" title="View"
                             style="border-radius:6px;">
                            <i class="ri-eye-line"></i>
                          </a>
                        </td>
                      </tr>
                    </c:forEach>
                  </c:when>
                  <c:otherwise>
                    <tr>
                      <td colspan="6" class="text-center text-muted py-4">No patients found</td>
                    </tr>
                  </c:otherwise>
                </c:choose>
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

<script>
function filterTable() {
  var search = document.getElementById('searchInput').value.toLowerCase();
  var status = document.getElementById('statusFilter').value;
  var rows   = document.querySelectorAll('#patientTableBody tr[data-name]');
  rows.forEach(function(row) {
    var name   = row.getAttribute('data-name')   || '';
    var phone  = row.getAttribute('data-phone')  || '';
    var active = row.getAttribute('data-active') || '';
    var matchSearch = name.includes(search) || phone.includes(search);
    var matchStatus = !status || active === status;
    row.style.display = (matchSearch && matchStatus) ? '' : 'none';
  });
}
</script>

<%@ include file="../include/footer.jsp" %>
</html>
