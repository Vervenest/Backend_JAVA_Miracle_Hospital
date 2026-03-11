<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ include file="../include/header.jsp" %>
<%@ include file="../include/left_navi.jsp" %>

<style>
/* Step 2 background matches screenshots */
.step2-card {
    background: #faf9f7;
    border: none;
    border-radius: 12px;
}
.step2-mobile-input {
    background: #eef4ff;
    border: 1.5px solid #b8d0ff;
    border-radius: 8px;
    font-size: 15px;
    padding: 10px 14px;
    width: 100%;
    outline: none;
}
.step2-mobile-input:focus { border-color: #4a90d9; box-shadow: none; }

.btn-add-new-user {
    width: 100%;
    background: transparent;
    border: 1.5px solid #4a90d9;
    color: #4a90d9;
    border-radius: 8px;
    padding: 10px;
    font-size: 15px;
    cursor: pointer;
    text-align: center;
}
.btn-add-new-user:hover { background: #eef4ff; }

.new-patient-box {
    background: #faf9f7;
    padding: 16px 0 0 0;
}
.new-patient-box .np-label {
    font-size: 13px;
    color: #555;
    margin-bottom: 4px;
    display: block;
}
.new-patient-box input,
.new-patient-box select {
    border: 1px solid #ddd;
    border-radius: 6px;
    padding: 8px 10px;
    font-size: 14px;
    width: 100%;
    background: #fff;
}
.btn-back-step {
    background: #fff;
    border: 1.5px solid #ccc;
    border-radius: 8px;
    padding: 10px 28px;
    font-size: 15px;
    color: #333;
    cursor: pointer;
}
.btn-back-step:hover { background: #f5f5f5; }
.btn-add-new-patient {
    background: #222;
    color: #fff;
    border: none;
    border-radius: 8px;
    padding: 10px 20px;
    font-size: 14px;
    cursor: pointer;
}
.btn-add-new-patient:hover { background: #444; }
</style>

<div class="main-content">
<div class="page-content">
<div class="container-fluid">

  <!-- ══ STEP 1: Select Doctor & Time ══ -->
  <div id="step1">
    <div class="card border-0 shadow-sm" style="border-radius:12px;">
      <div class="card-body p-4">

        <h5 class="fw-semibold mb-4" style="color:#2d3a4a;">
          &#x1F9EC; Select Doctor &amp; Time - Book Appointment
        </h5>

        <div class="row g-3 align-items-end">

          <!-- Location -->
          <div class="col-md-3">
            <label class="form-label fw-medium" style="font-size:14px;">Location</label>
            <select id="locationSelect" class="form-select" style="border-radius:8px;">
              <option value="">All Locations</option>
              <c:forEach var="loc" items="${locationList}">
                <option value="${loc.locationId}">${loc.locationName}</option>
              </c:forEach>
            </select>
          </div>

          <!-- Doctor -->
          <div class="col-md-3">
            <label class="form-label fw-medium" style="font-size:14px;">Doctor</label>
            <select id="doctorId" class="form-select" style="border-radius:8px;" required>
              <option value="">Select Doctor</option>
              <c:forEach var="doctor" items="${doctorList}">
                <option value="${doctor.doctorStringId}"
                        data-scan="${doctor.scanType}"
                        data-name="${doctor.doctorName}"
                        data-location="${doctor.locationId}">
                  ${doctor.doctorName}<c:if test="${not empty doctor.specialization}"> - ${doctor.specialization}</c:if>
                </option>
              </c:forEach>
            </select>
          </div>

          <!-- Date -->
          <div class="col-md-3">
            <label class="form-label fw-medium" style="font-size:14px;">Date</label>
            <input type="date" id="appointmentDate" class="form-control"
                   style="border-radius:8px;"
                   min="<%= java.time.LocalDate.now() %>" required>
          </div>

          <!-- Available Time Slot -->
          <div class="col-md-3">
            <label class="form-label fw-medium" style="font-size:14px;">Available Time Slot</label>
            <select id="appointmentStartTime" class="form-select" style="border-radius:8px;" required disabled>
              <option value="">Select Slot</option>
            </select>
          </div>

          <!-- Scan Type (only for radiologist) -->
          <div class="col-md-3" id="scanTypeDiv" style="display:none;">
            <label class="form-label fw-medium" style="font-size:14px;">Scan Type <span class="text-danger">*</span></label>
            <select class="form-select" id="appointmentScanType" style="border-radius:8px;">
              <option value="">Select Scan</option>
            </select>
          </div>

        </div>

        <div class="mt-4">
          <button type="button" id="goToStep2" class="btn btn-dark px-4" style="border-radius:8px;font-size:15px;">
            Continue &rarr;
          </button>
        </div>

      </div>
    </div>
  </div><!-- /step1 -->


  <!-- ══ STEP 2: Select or Add Patient ══ -->
  <div id="step2" style="display:none;">
    <div class="step2-card shadow-sm p-4">

      <h5 class="fw-bold mb-4" style="color:#2d3a4a;font-size:18px;">
        &#x1F465; Select or Add Patient
      </h5>

      <!-- Mobile Number -->
      <div class="mb-3">
        <label class="fw-medium mb-1" style="font-size:14px;">Mobile Number</label>
        <input type="text" id="userPhone" class="step2-mobile-input"
               placeholder="Enter mobile number" maxlength="10"
               oninput="onPhoneInput(this.value)">
      </div>

      <input type="hidden" id="userId">

      <!-- Existing patients list (shown after search) -->
      <div id="existingPatientsSection" style="display:none;" class="mb-3">
        <div id="existingPatientsList" class="list-group mb-2"></div>
        <button type="button" class="btn-add-new-user" id="showNewPatientFormBtn"
                onclick="showNewPatientForm()">
          + Add New User
        </button>
      </div>

      <!-- Add New User button (shown before search or when no user found) -->
      <div id="addNewUserBtnSection" class="mb-3">
        <button type="button" class="btn-add-new-user" onclick="showNewPatientForm()">
          + Add New User
        </button>
      </div>

      <!-- New Patient Form (hidden by default, shown when Add New User clicked) -->
      <div id="newPatientSection" class="new-patient-box" style="display:none;">
        <div class="fw-semibold mb-3" style="font-size:14px;color:#333;">Add New Patient</div>

        <!-- Row 1: Name, Relation, Phone, Gender, DOB, Age -->
        <div class="row g-2 mb-2">
          <div class="col">
            <span class="np-label">Name</span>
            <input type="text" id="newPatientName" placeholder="">
          </div>
          <div class="col">
            <span class="np-label">Relation</span>
            <input type="text" id="newPatientRelation" value="">
          </div>
          <div class="col">
            <span class="np-label">Phone</span>
            <input type="text" id="newPatientPhone">
          </div>
          <div class="col">
            <span class="np-label">Gender</span>
            <select id="newPatientGender">
              <option value="">Select</option>
              <option>Male</option><option>Female</option><option>Other</option>
            </select>
          </div>
          <div class="col">
            <span class="np-label">DOB</span>
            <input type="date" id="newPatientDOB">
          </div>
          <div class="col" style="max-width:90px;">
            <span class="np-label">Age</span>
            <input type="text" id="newPatientAge">
          </div>
        </div>

        <!-- Row 2: Pregnant, LMP Date (conditional), Expected Delivery (conditional), Add New button -->
        <div class="row g-2 align-items-end mt-1">
          <div class="col-auto" style="min-width:160px;">
            <span class="np-label">Pregnant?</span>
            <select id="newPatientPregnant" onchange="togglePregnancyFields()" style="border-radius:6px;border:1px solid #ddd;padding:8px 10px;width:100%;">
              <option value="0">No</option>
              <option value="1">Yes</option>
            </select>
          </div>

          <!-- LMP Date — only shown when Pregnant = Yes -->
          <div class="col-auto" id="lmpDateCol" style="display:none;min-width:180px;">
            <span class="np-label">LMP Date</span>
            <input type="date" id="newPatientLmpDate" onchange="autoCalcEDD()">
          </div>

          <!-- Expected Delivery — only shown when Pregnant = Yes -->
          <div class="col-auto" id="eddCol" style="display:none;min-width:200px;">
            <span class="np-label">Expected Delivery</span>
            <input type="date" id="newPatientEdd">
          </div>

          <div class="col-auto">
            <button type="button" class="btn-add-new-patient" id="addNewPatientBtn"
                    onclick="addNewPatient()">
              Add New
            </button>
          </div>
        </div>

      </div><!-- /newPatientSection -->

      <!-- Confirm Appointment (shown after patient selected) -->
      <div id="confirmSection" style="display:none;" class="mt-3 mb-3">
        <button type="button" class="btn btn-success px-4" style="border-radius:8px;"
                id="confirmAppointment">
          &#10003; Confirm Appointment
        </button>
      </div>

      <!-- Back button -->
      <div class="mt-4">
        <button type="button" class="btn-back-step" onclick="goBackToStep1()">
          &larr; Back
        </button>
      </div>

    </div>
  </div><!-- /step2 -->

</div>
</div>
</div>

<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script>
const BASE = '${pageContext.request.contextPath}';
let selectedPatientId = null;

// ── LOCATION FILTER ──────────────────────────────────────────────────────────
document.getElementById('locationSelect').addEventListener('change', function() {
    var locId = this.value;
    var doctorSel = document.getElementById('doctorId');
    Array.from(doctorSel.options).forEach(function(opt) {
        if (!opt.value) return;
        var optLoc = opt.getAttribute('data-location') || '';
        opt.style.display = (!locId || optLoc === locId || optLoc === '') ? '' : 'none';
    });
    doctorSel.value = '';
    document.getElementById('appointmentStartTime').innerHTML = '<option value="">Select Slot</option>';
    document.getElementById('appointmentStartTime').disabled = true;
});

// ── STEP 1 → STEP 2 ──────────────────────────────────────────────────────────
document.getElementById('goToStep2').addEventListener('click', function() {
    var doctorId  = document.getElementById('doctorId').value;
    var date      = document.getElementById('appointmentDate').value;
    var startTime = document.getElementById('appointmentStartTime').value;
    var scanDiv   = document.getElementById('scanTypeDiv');
    var scanType  = document.getElementById('appointmentScanType').value;

    if (!doctorId || !date || !startTime) {
        Swal.fire('Warning', 'Please select doctor, date and time slot first', 'warning');
        return;
    }
    if (scanDiv.style.display !== 'none' && !scanType) {
        Swal.fire('Warning', 'Please select a scan type', 'warning');
        return;
    }
    document.getElementById('step1').style.display = 'none';
    document.getElementById('step2').style.display = 'block';
    // Reset step2 state
    document.getElementById('userPhone').value = '';
    document.getElementById('userId').value = '';
    document.getElementById('existingPatientsSection').style.display = 'none';
    document.getElementById('addNewUserBtnSection').style.display = 'block';
    document.getElementById('newPatientSection').style.display = 'none';
    document.getElementById('confirmSection').style.display = 'none';
    selectedPatientId = null;
});

function goBackToStep1() {
    document.getElementById('step2').style.display = 'none';
    document.getElementById('step1').style.display = 'block';
}

// ── LOAD SLOTS ────────────────────────────────────────────────────────────────
function loadSlots() {
    var doctorId = document.getElementById('doctorId').value;
    var date     = document.getElementById('appointmentDate').value;
    var slotSel  = document.getElementById('appointmentStartTime');
    if (!doctorId || !date) {
        slotSel.innerHTML = '<option value="">Select doctor and date first</option>';
        slotSel.disabled = true;
        return;
    }
    slotSel.disabled = true;
    slotSel.innerHTML = '<option>Loading...</option>';
    $.post(BASE + '/Patientapi/getAvailableSlots', { doctorId: doctorId, date: date }, function(res) {
        if (res.status === 'success' && res.result && res.result.list.length) {
            var html = '<option value="">Select Slot</option>';
            res.result.list.forEach(function(s) {
                html += '<option value="' + s.startTime + '" data-end="' + s.endTime + '">' +
                        s.startTime + ' - ' + s.endTime + '</option>';
            });
            slotSel.innerHTML = html;
            slotSel.disabled = false;
        } else {
            slotSel.innerHTML = '<option value="">' + (res.message || 'No slots available') + '</option>';
            slotSel.disabled = true;
        }
    }, 'json').fail(function(xhr) {
        if (xhr.status === 401) {
            Swal.fire('Session expired','Please log in again.','warning')
                .then(function(){ window.location.href = BASE + '/admin/login'; });
            return;
        }
        slotSel.innerHTML = '<option value="">Error loading slots</option>';
        slotSel.disabled = true;
    });
}

document.getElementById('doctorId').addEventListener('change', function() {
    var scanData = this.options[this.selectedIndex].getAttribute('data-scan') || '';
    var scanDiv  = document.getElementById('scanTypeDiv');
    var scanSel  = document.getElementById('appointmentScanType');
    scanDiv.style.display = 'none';
    scanSel.innerHTML = '<option value="">Select Scan</option>';
    if (scanData && this.options[this.selectedIndex].text.toLowerCase().includes('radiologist')) {
        var html = '<option value="">Select Scan</option>';
        scanData.split(',').forEach(function(s) {
            s = s.trim();
            if (s) {
                var label = s.replace(/_/g,' ').toLowerCase().replace(/\b\w/g, function(c){ return c.toUpperCase(); });
                html += '<option value="' + s + '">' + label + '</option>';
            }
        });
        scanSel.innerHTML = html;
        scanDiv.style.display = 'block';
    }
    loadSlots();
});
document.getElementById('appointmentDate').addEventListener('change', loadSlots);

// ── PHONE INPUT (auto-search after 10 digits) ─────────────────────────────────
function onPhoneInput(val) {
    if (val.length === 10) searchByPhone(val);
}

function searchByPhone(phone) {
    document.getElementById('existingPatientsSection').style.display = 'none';
    document.getElementById('addNewUserBtnSection').style.display = 'none';
    document.getElementById('newPatientSection').style.display = 'none';
    document.getElementById('confirmSection').style.display = 'none';
    selectedPatientId = null;

    $.post(BASE + '/Patientapi/getUserByPhone', { phone: phone }, function(res) {
        if (res.status === 'success') {
            document.getElementById('userId').value = res.user.userId;
            document.getElementById('addNewUserBtnSection').style.display = 'none';

            var html = '';
            if (res.patients && res.patients.length) {
                res.patients.forEach(function(p) {
                    html += '<label class="list-group-item list-group-item-action">' +
                        '<input type="radio" name="selectedPatient" value="' + p.patientId + '" class="form-check-input me-2"> ' +
                        '<strong>' + p.patientName + '</strong>' +
                        (p.patientRelation ? ' (' + p.patientRelation + ')' : '') +
                        (p.patientAge ? ' — Age: ' + p.patientAge : '') +
                        '</label>';
                });
            }
            document.getElementById('existingPatientsList').innerHTML = html || '<p class="text-muted small mb-2">No patients yet for this user.</p>';
            document.getElementById('existingPatientsSection').style.display = 'block';
        } else {
            document.getElementById('userId').value = '';
            document.getElementById('addNewUserBtnSection').style.display = 'block';
        }
    }, 'json').fail(function() {
        document.getElementById('addNewUserBtnSection').style.display = 'block';
    });
}

// Select patient radio
$(document).on('change', 'input[name="selectedPatient"]', function() {
    selectedPatientId = $(this).val();
    document.getElementById('confirmSection').style.display = 'block';
});

// ── SHOW NEW PATIENT FORM ────────────────────────────────────────────────────
function showNewPatientForm() {
    document.getElementById('addNewUserBtnSection').style.display = 'none';
    document.getElementById('newPatientSection').style.display = 'block';
    document.getElementById('newPatientPregnant').value = '0';
    togglePregnancyFields();
}

// ── PREGNANT TOGGLE ───────────────────────────────────────────────────────────
function togglePregnancyFields() {
    var val = document.getElementById('newPatientPregnant').value;
    var show = (val === '1');
    document.getElementById('lmpDateCol').style.display = show ? '' : 'none';
    document.getElementById('eddCol').style.display = show ? '' : 'none';
    if (!show) {
        document.getElementById('newPatientLmpDate').value = '';
        document.getElementById('newPatientEdd').value = '';
    }
}

// ── AUTO CALC EDD = LMP + 280 days ───────────────────────────────────────────
function autoCalcEDD() {
    var lmpVal = document.getElementById('newPatientLmpDate').value;
    if (!lmpVal) return;
    var lmp = new Date(lmpVal);
    lmp.setDate(lmp.getDate() + 280);
    var yyyy = lmp.getFullYear();
    var mm   = String(lmp.getMonth() + 1).padStart(2,'0');
    var dd   = String(lmp.getDate()).padStart(2,'0');
    document.getElementById('newPatientEdd').value = yyyy + '-' + mm + '-' + dd;
}

// ── ADD NEW PATIENT ───────────────────────────────────────────────────────────
function addNewPatient() {
    var patientName = document.getElementById('newPatientName').value.trim();
    if (!patientName) { Swal.fire('Warning','Patient name is required','warning'); return; }

    var isPregnant = document.getElementById('newPatientPregnant').value;
    var data = {
        userId:              document.getElementById('userId').value,
        userPhone:           document.getElementById('userPhone').value.trim(),
        patientName:         patientName,
        patientRelation:     document.getElementById('newPatientRelation').value || 'Self',
        patientPhone:        document.getElementById('newPatientPhone').value || document.getElementById('userPhone').value.trim(),
        patientGender:       document.getElementById('newPatientGender').value,
        patientDateOfBirth:  document.getElementById('newPatientDOB').value,
        patientAge:          document.getElementById('newPatientAge').value,
        isPregnant:          isPregnant,
        lmpDate:             isPregnant === '1' ? document.getElementById('newPatientLmpDate').value : '',
        expectedDeliveryDate: isPregnant === '1' ? document.getElementById('newPatientEdd').value : ''
    };

    var btn = document.getElementById('addNewPatientBtn');
    btn.disabled = true; btn.textContent = 'Adding...';

    $.post(BASE + '/Patientapi/addPatient', data, function(res) {
        if (res.status === 'success') {
            var p = res.result;
            if (p.userId) document.getElementById('userId').value = p.userId;
            selectedPatientId = p.patientId;

            var html = '<label class="list-group-item list-group-item-action active">' +
                '<input type="radio" name="selectedPatient" value="' + p.patientId + '" class="form-check-input me-2" checked> ' +
                '<strong>' + p.patientName + '</strong> (' + (p.patientRelation || 'Self') + ')</label>';
            document.getElementById('existingPatientsList').innerHTML += html;
            document.getElementById('existingPatientsSection').style.display = 'block';
            document.getElementById('newPatientSection').style.display = 'none';
            document.getElementById('confirmSection').style.display = 'block';

            // Clear form
            ['newPatientName','newPatientRelation','newPatientPhone','newPatientDOB','newPatientAge','newPatientLmpDate','newPatientEdd'].forEach(function(id) {
                document.getElementById(id).value = '';
            });
            document.getElementById('newPatientGender').value = '';
            document.getElementById('newPatientPregnant').value = '0';
            togglePregnancyFields();

            Swal.fire({ icon:'success', title:'Patient added!', timer:1500, showConfirmButton:false });
        } else {
            Swal.fire('Error', res.message || 'Failed to add patient', 'error');
        }
    }, 'json').fail(function() { Swal.fire('Error','Server error.','error'); })
    .always(function() { btn.disabled = false; btn.textContent = 'Add New'; });
}

// ── CONFIRM APPOINTMENT ───────────────────────────────────────────────────────
document.getElementById('confirmAppointment').addEventListener('click', function() {
    var userId    = document.getElementById('userId').value;
    var doctorId  = document.getElementById('doctorId').value;
    var patientId = selectedPatientId || $('input[name="selectedPatient"]:checked').val();
    var date      = document.getElementById('appointmentDate').value;
    var startTime = document.getElementById('appointmentStartTime').value;
    var endTime   = $('#appointmentStartTime option:selected').data('end') || '';
    var scanType  = document.getElementById('appointmentScanType').value || '';

    var missing = [];
    if (!userId)    missing.push('User not found');
    if (!doctorId)  missing.push('Doctor not selected');
    if (!patientId) missing.push('Patient not selected');
    if (!date)      missing.push('Date not selected');
    if (!startTime) missing.push('Time slot not selected');

    if (missing.length) {
        Swal.fire({ icon:'warning', title:'Missing fields', html: missing.join('<br>') });
        return;
    }
    this.disabled = true; this.textContent = 'Booking...';
    $.post(BASE + '/Patientapi/addAppointment',
        { userId: userId, doctorId: doctorId, patientId: patientId,
          date: date, startTime: startTime, endTime: endTime, scanType: scanType },
        function(res) {
            if (res.status === 'success') {
                Swal.fire({ icon:'success', title:'Appointment Booked!',
                    html: 'Token No: <strong>' + res.tokenNo + '</strong>',
                    confirmButtonText:'View Appointments'
                }).then(function() { window.location.href = BASE + '/admin/appointment/appointmentlist'; });
            } else {
                Swal.fire('Error', res.message || 'Failed to book', 'error');
            }
        }, 'json').fail(function() { Swal.fire('Error','Server error.','error'); })
    .always(function() {
        var btn = document.getElementById('confirmAppointment');
        btn.disabled = false; btn.textContent = '✓ Confirm Appointment';
    });
});
</script>

<%@ include file="../include/footer.jsp" %>
</html>
