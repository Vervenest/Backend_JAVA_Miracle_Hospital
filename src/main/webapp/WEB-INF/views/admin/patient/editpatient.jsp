<%@ taglib uri="jakarta.tags.core" prefix="c" %>
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
              <a href="${pageContext.request.contextPath}/admin/patient/patientlist">Patient</a>
            </li>
            <li class="breadcrumb-item active">Edit</li>
          </ol>
        </div>
      </div>
    </div>
  </div>

  <!-- Back Button -->
  <div class="d-flex justify-content-end mb-3">
    <a href="${pageContext.request.contextPath}/admin/patient/patientDetails/${patientId}"
       class="btn btn-primary">
      <i class="ri-arrow-left-line me-1"></i> Back
    </a>
  </div>

  <div class="row">
    <div class="col-lg-10 offset-lg-1">
      <div class="card shadow-sm border-0">
        <div class="card-body p-4">

          <h5 class="fw-bold mb-4">Edit Patient</h5>

          <div id="formMsg" class="alert" style="display:none;"></div>

          <form id="editPatientForm" class="row g-3">
            <input type="hidden" name="patientId" value="${patientId}">

            <!-- Row 1: Name, Relation, Phone, Gender -->
            <div class="col-md-3">
              <label class="form-label fw-medium">
                Patient Name <span class="text-danger">*</span>
              </label>
              <input type="text" name="patientName" class="form-control"
                     value="${patientName}" required>
            </div>

            <div class="col-md-3">
              <label class="form-label fw-medium">Relation</label>
              <input type="text" name="patientRelation" class="form-control"
                     value="${patientRelation}">
            </div>

            <div class="col-md-3">
              <label class="form-label fw-medium">
                Phone Number <span class="text-danger">*</span>
              </label>
              <input type="text" name="patientPhone" class="form-control"
                     value="${patientPhone}" pattern="[0-9]{10}" required>
            </div>

            <div class="col-md-3">
              <label class="form-label fw-medium">Gender</label>
              <select name="patientGender" class="form-select">
                <option value="">Select</option>
                <option value="Male"   ${patientGender == 'Male'   ? 'selected' : ''}>Male</option>
                <option value="Female" ${patientGender == 'Female' ? 'selected' : ''}>Female</option>
                <option value="Other"  ${patientGender == 'Other'  ? 'selected' : ''}>Other</option>
              </select>
            </div>

            <!-- Row 2: DOB, Age, Is Pregnant, LMP Date -->
            <div class="col-md-3">
              <label class="form-label fw-medium">Date of Birth</label>
              <input type="text" name="patientDateOfBirth" class="form-control"
                     placeholder="YYYY-MM-DD" value="${patientDateOfBirth}">
            </div>

            <div class="col-md-3">
              <label class="form-label fw-medium">Age</label>
              <input type="text" name="patientAge" class="form-control"
                     placeholder="Age" value="${patientAge}">
            </div>

            <div class="col-md-3">
              <label class="form-label fw-medium">Is Pregnant?</label>
              <select name="isPregnant" id="isPregnantSelect" class="form-select"
                      onchange="togglePregnancyFields()">
                <option value="0" ${(empty patientIsPregnant or patientIsPregnant == '0') ? 'selected' : ''}>No</option>
                <option value="1" ${patientIsPregnant == '1' ? 'selected' : ''}>Yes</option>
              </select>
            </div>

            <div class="col-md-3">
              <label class="form-label fw-medium">LMP Date</label>
              <input type="date" name="lmpDate" id="lmpDateInput" class="form-control"
                     value="${patientLmpDate}" onchange="calcExpectedDelivery()">
            </div>

            <!-- Row 3: Expected Delivery Date -->
            <div class="col-md-4" id="expectedDeliveryRow">
              <label class="form-label fw-medium">Expected Delivery Date</label>
              <input type="date" name="expectedDeliveryDate" id="expectedDeliveryInput"
                     class="form-control" value="${patientExpectedDeliveryDate}">
              <div class="text-muted mt-1" style="font-size:12px;">
                Auto-calculated (LMP + 280 days)
              </div>
            </div>

            <!-- Submit -->
            <div class="col-12 mt-3">
              <button type="submit" id="updateBtn" class="btn btn-primary px-4">
                Update
              </button>
            </div>

          </form>

        </div>
      </div>
    </div>
  </div>

</div>
</div>
</div>

<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script>
var BASE = '${pageContext.request.contextPath}';

// Show/hide LMP + EDD based on pregnant toggle
function togglePregnancyFields() {
  var val = document.getElementById('isPregnantSelect').value;
  var lmpRow = document.getElementById('lmpDateInput').closest('.col-md-3');
  var eddRow = document.getElementById('expectedDeliveryRow');
  if (val === '1') {
    lmpRow.style.display = '';
    eddRow.style.display = '';
  } else {
    lmpRow.style.display = '';  // keep visible but optional
    eddRow.style.display = '';
  }
}

// Auto-calculate Expected Delivery = LMP + 280 days
function calcExpectedDelivery() {
  var lmpVal = document.getElementById('lmpDateInput').value;
  if (!lmpVal) return;
  var lmp = new Date(lmpVal);
  lmp.setDate(lmp.getDate() + 280);
  var yyyy = lmp.getFullYear();
  var mm   = String(lmp.getMonth() + 1).padStart(2, '0');
  var dd   = String(lmp.getDate()).padStart(2, '0');
  document.getElementById('expectedDeliveryInput').value = yyyy + '-' + mm + '-' + dd;
}

// On load: calculate if LMP already set
window.addEventListener('DOMContentLoaded', function() {
  togglePregnancyFields();
  var lmpVal = document.getElementById('lmpDateInput').value;
  var eddVal = document.getElementById('expectedDeliveryInput').value;
  if (lmpVal && !eddVal) calcExpectedDelivery();
});

// Form submit
document.getElementById('editPatientForm').addEventListener('submit', function(e) {
  e.preventDefault();
  var btn = document.getElementById('updateBtn');
  btn.disabled = true;
  btn.textContent = 'Updating...';

  var formData = new FormData(this);
  var params = new URLSearchParams();
  formData.forEach(function(v, k) { params.append(k, v); });

  fetch(BASE + '/adminmodel/updatePatient', {
    method: 'POST',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    body: params.toString()
  })
  .then(function(r) { return r.json(); })
  .then(function(res) {
    if (res.status === 'success') {
      Swal.fire({ icon: 'success', title: 'Updated!', text: res.message,
        confirmButtonText: 'Back to List'
      }).then(function() {
        window.location.href = BASE + '/admin/patient/patientlist';
      });
    } else {
      Swal.fire('Error', res.message || 'Update failed', 'error');
    }
  })
  .catch(function() {
    Swal.fire('Error', 'Server error. Please try again.', 'error');
  })
  .finally(function() {
    btn.disabled = false;
    btn.textContent = 'Update';
  });
});
</script>

<%@ include file="../include/footer.jsp" %>
</html>
