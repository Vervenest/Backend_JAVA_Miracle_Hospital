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
        <h5 class="mb-sm-0 text-uppercase">Locations</h5>
        <div class="page-title-right">
          <ol class="breadcrumb m-0">
            <li class="breadcrumb-item text-uppercase">
              <a href="javascript:void(0);">Locations</a>
            </li>H
            <li class="breadcrumb-item active text-uppercase">List</li>
          </ol>
        </div>
      </div>
    </div>
  </div>

  <div class="row">
    <div class="col-lg-12">
      <div class="card shadow-sm">
        <div class="card-header d-flex justify-content-between align-items-center">
          <h5 class="card-title mb-0 text-uppercase">Locations List</h5>
          <button class="btn btn-outline-secondary btn-sm text-uppercase"
                  onclick="openAddModal()">
            + ADD LOCATION
          </button>
        </div>
        <div class="card-body p-0">
          <div class="table-responsive">
            <table class="table table-bordered table-striped align-middle">
              <thead class="text-uppercase">
                <tr>
                  <th style="width:80px;">S. NO.</th>
                  <th style="width:250px;">LOCATION NAME</th>
                  <th style="width:120px;">STATUS</th>
                  <th style="width:120px;">ACTION</th>
                </tr>
              </thead>
              <tbody>
                <c:choose>
                  <c:when test="${not empty locationList}">
                    <c:forEach var="loc" items="${locationList}" varStatus="st">
                      <tr>
                        <td>${st.count}</td>
                        <td><strong>${loc.locationName}</strong></td>
                        <td>
  <c:choose>
    <c:when test="${loc.locationStatus == '1'}">
      <a href="${pageContext.request.contextPath}/adminmodel/updateLocationStatus/0/${loc.locationId}"
         class="badge rounded-pill bg-success px-3 py-2 text-white text-decoration-none"
         style="cursor:pointer;"
         onclick="return confirm('Deactivate ${fn:escapeXml(loc.locationName)}?')">
        Active
      </a>
    </c:when>
    <c:otherwise>
      <a href="${pageContext.request.contextPath}/adminmodel/updateLocationStatus/1/${loc.locationId}"
         class="badge rounded-pill bg-danger px-3 py-2 text-white text-decoration-none"
         style="cursor:pointer;"
         onclick="return confirm('Activate ${fn:escapeXml(loc.locationName)}?')">
        Inactive
      </a>
    </c:otherwise>
  </c:choose>
</td>
                        <td>
                          <!-- Edit -->
                          <button class="btn btn-sm btn-outline-primary me-1"
                                  title="Edit"
                                  onclick="openEditModal('${loc.locationId}', '${fn:escapeXml(loc.locationName)}')">
                            <i class="ri-edit-box-line"></i>
                          </button>
                          <!-- Delete -->
                          <a href="${pageContext.request.contextPath}/adminmodel/updateLocationStatus/2/${loc.locationId}"
                             class="btn btn-sm btn-outline-danger"
                             title="Delete"
                             onclick="return confirm('Delete ${fn:escapeXml(loc.locationName)}?')">
                            <i class="ri-delete-bin-line"></i>
                          </a>
                        </td>
                      </tr>
                    </c:forEach>
                  </c:when>
                  <c:otherwise>
                    <tr>
                      <td colspan="4" class="text-center text-muted py-4">No locations found</td>
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

<!-- ── ADD LOCATION MODAL ── -->
<div class="modal fade" id="addLocationModal" tabindex="-1">
  <div class="modal-dialog modal-dialog-end" style="position:fixed;right:0;top:0;margin:0;height:100%;max-width:420px;width:100%;">
    <div class="modal-content h-100 rounded-0">
      <div class="modal-header" style="background:#3b5998;">
        <h5 class="modal-title text-white">Add Location</h5>
        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
      </div>
      <div class="modal-body p-4">
        <div class="mb-3">
          <label class="form-label">Location Name : <span class="text-danger">*</span></label>
          <input type="text" id="addLocationName" class="form-control"
                 placeholder="Enter location name" required>
        </div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary px-4" onclick="submitAddLocation()">Save</button>
      </div>
    </div>
  </div>
</div>

<!-- ── EDIT LOCATION MODAL ── -->
<div class="modal fade" id="editLocationModal" tabindex="-1">
  <div class="modal-dialog modal-dialog-end" style="position:fixed;right:0;top:0;margin:0;height:100%;max-width:420px;width:100%;">
    <div class="modal-content h-100 rounded-0">
      <div class="modal-header" style="background:#3b5998;">
        <h5 class="modal-title text-white">Edit Location</h5>
        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
      </div>
      <div class="modal-body p-4">
        <input type="hidden" id="editLocationId">
        <div class="mb-3">
          <label class="form-label">Location Name : <span class="text-danger">*</span></label>
          <input type="text" id="editLocationName" class="form-control" required>
        </div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary px-4" onclick="submitEditLocation()">Save</button>
      </div>
    </div>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script>
var BASE = '${pageContext.request.contextPath}';

function openAddModal() {
  document.getElementById('addLocationName').value = '';
  new bootstrap.Modal(document.getElementById('addLocationModal')).show();
}

function openEditModal(locationId, locationName) {
  document.getElementById('editLocationId').value = locationId;
  document.getElementById('editLocationName').value = locationName;
  new bootstrap.Modal(document.getElementById('editLocationModal')).show();
}

function submitAddLocation() {
  var name = document.getElementById('addLocationName').value.trim();
  if (!name) { Swal.fire('Error', 'Location name is required', 'error'); return; }

  fetch(BASE + '/adminmodel/addlocation', {
    method: 'POST',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    body: 'locationName=' + encodeURIComponent(name)
  })
  .then(function(r) { return r.json(); })
  .then(function(res) {
    if (res.status === 'SUCCESS') {
      Swal.fire({ icon: 'success', title: 'Added!', text: res.message,
        timer: 1800, showConfirmButton: false })
        .then(function() { location.reload(); });
    } else if (res.status === 'EXIST') {
      Swal.fire('Already Exists', res.message, 'warning');
    } else {
      Swal.fire('Error', res.message || 'Failed', 'error');
    }
  }).catch(function() { Swal.fire('Error', 'Server error', 'error'); });
}

function submitEditLocation() {
  var id   = document.getElementById('editLocationId').value;
  var name = document.getElementById('editLocationName').value.trim();
  if (!name) { Swal.fire('Error', 'Location name is required', 'error'); return; }

  fetch(BASE + '/adminmodel/updatelocation', {
    method: 'POST',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    body: 'locationId=' + encodeURIComponent(id) + '&locationName=' + encodeURIComponent(name)
  })
  .then(function(r) { return r.json(); })
  .then(function(res) {
    if (res.status === 'SUCCESS') {
      Swal.fire({ icon: 'success', title: 'Updated!', text: res.message,
        timer: 1800, showConfirmButton: false })
        .then(function() { location.reload(); });
    } else {
      Swal.fire('Error', res.message || 'Failed', 'error');
    }
  }).catch(function() { Swal.fire('Error', 'Server error', 'error'); });
}
</script>

<%@ include file="../include/footer.jsp" %>
</html>
