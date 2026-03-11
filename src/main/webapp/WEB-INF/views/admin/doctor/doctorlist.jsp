<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ include file="../include/header.jsp" %>
<%@ include file="../include/left_navi.jsp" %>

<div class="main-content">
    <div class="page-content">
        <div class="page-loader"
            style="display:none; position:fixed; top:0; left:0; width:100%; height:100%; background:rgba(255,255,255,0.8); text-align:center; z-index:9999;">
            <img src="${pageContext.request.contextPath}/assets/images/animated_loader_gif.gif" alt="Loading..."
                style="position:relative; top:50%; transform:translateY(-50%);" />
        </div>
        <div class="container-fluid">

            <div class="row">
                <div class="col-12">
                    <div class="page-title-box d-sm-flex align-items-center justify-content-between">
                        <h5 class="mb-sm-0 text-uppercase">Doctors</h5>
                        <div class="page-title-right">
                            <ol class="breadcrumb m-0">
                                <li class="breadcrumb-item text-uppercase"><a href="javascript:void(0);">Doctors</a></li>
                                <li class="breadcrumb-item active text-uppercase">List</li>
                            </ol>
                        </div>
                    </div>
                </div>
            </div>

            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger">${errorMessage}</div>
            </c:if>

            <div class="row">
                <div class="col-lg-12">
                    <div class="card">
                        <div class="card-header">
                            <div class="row">
                                <div class="col-md-6">
                                    <h5 class="card-title mb-0 text-uppercase">Doctors List</h5>
                                </div>
                                <div class="col-md-6">
                                    <div class="float-end">
                                        <button class="btn btn-outline-secondary btn-sm text-uppercase" type="button"
                                            data-bs-toggle="offcanvas" data-bs-target="#add-doctor-form-modal"
                                            aria-controls="add-doctor-form-modal">+ Add Doctor</button>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="card-body">
                            <div class="table-responsive">
                                <table class="table table-bordered table-striped align-middle" style="width:100%">
                                    <thead class="text-uppercase" style="background:#f3f6f9; color:#000;">
                                        <tr>
                                            <th>S. No.</th>
                                            <th>Profile Image</th>
                                            <th>Name</th>
                                            <th>Phone Number</th>
                                            <th>Specialisation</th>
                                            <th>Status</th>
                                            <th>Action</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:choose>
                                            <c:when test="${not empty doctorList}">
                                                <c:forEach var="doctor" items="${doctorList}" varStatus="st">
                                                    <tr class="${doctor.doctorStatus != '1' ? 'table-secondary' : ''}"
                                                        style="${doctor.doctorStatus != '1' ? 'opacity:0.6;' : ''}"
                                                        title="${doctor.doctorStatus != '1' ? 'Inactive Doctor' : ''}"
                                                    >
                                                        <td>${st.count}</td>
                                                        <td>
                                                            <c:choose>
                                                                <c:when test="${not empty doctor.profileImage}">
                                                                    <img src="${pageContext.request.contextPath}/assets/doctorProfileImage/${doctor.profileImage}"
                                                                        alt="" class="avatar-xs rounded-circle"
                                                                        style="width:40px;height:40px;object-fit:cover;">
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <div class="avatar-xs rounded-circle bg-light d-flex align-items-center justify-content-center"
                                                                        style="width:40px;height:40px;">
                                                                        <i class="ri-user-3-line text-muted"></i>
                                                                    </div>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </td>
                                                        <td><strong>${doctor.doctorName}</strong></td>
                                                        <td>${doctor.phoneNumber}</td>
                                                        <td>
                                                            <c:set var="specIds" value="${fn:split(doctor.specialization, ',')}" />
                                                            <c:forEach var="specId" items="${specIds}">
                                                                <c:set var="trimmedId" value="${fn:trim(specId)}" />
                                                                <c:choose>
                                                                    <c:when test="${not empty specialisationMap[trimmedId]}">
                                                                        <span class="badge rounded-pill border border-primary text-primary me-1">
                                                                            ${specialisationMap[trimmedId]}
                                                                        </span>
                                                                    </c:when>
                                                                    <c:when test="${not empty trimmedId}">
                                                                        <span class="badge rounded-pill border border-secondary text-secondary me-1">
                                                                            ${trimmedId}
                                                                        </span>
                                                                    </c:when>
                                                                </c:choose>
                                                            </c:forEach>
                                                        </td>
                                                        <td>
                                                            <c:choose>
                                                                <c:when test="${doctor.doctorStatus == '1'}">
                                                                    <a href="${pageContext.request.contextPath}/adminmodel/updateDoctorStatus/0/${doctor.doctorStringId}"
                                                                       onclick="return confirm('Deactivate Dr. ${doctor.doctorName}?')"
                                                                       class="badge rounded-pill bg-success text-decoration-none"
                                                                       title="Click to Deactivate">
                                                                          Active
                                                                    </a>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <a href="${pageContext.request.contextPath}/adminmodel/updateDoctorStatus/1/${doctor.doctorStringId}"
                                                                       onclick="return confirm('Activate Dr. ${doctor.doctorName}?')"
                                                                       class="badge rounded-pill bg-danger text-decoration-none"
                                                                       title="Click to Activate">
                                                                          Inactive
                                                                    </a>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </td>
                                                        <td>
                                                            <a href="${pageContext.request.contextPath}/admin/doctor/editdoctor/${doctor.doctorStringId}"
                                                               class="btn btn-sm btn-outline-primary me-1" title="Edit">
                                                                <i class="ri-edit-box-line"></i>
                                                            </a>
                                                            <a href="${pageContext.request.contextPath}/admin/doctor/doctorTimeSlots/${doctor.doctorStringId}"
                                                               class="btn btn-sm btn-outline-info me-1" title="Time Slots">
                                                                <i class="ri-time-line"></i>
                                                            </a>
                                                            <a href="${pageContext.request.contextPath}/admin/doctor/doctorCalendar/${doctor.doctorStringId}"
                                                               class="btn btn-sm btn-outline-secondary" title="Calendar">
                                                                <i class="ri-calendar-event-line"></i>
                                                            </a>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </c:when>
                                            <c:otherwise>
                                                <tr>
                                                    <td colspan="7" class="text-center text-muted py-4">No doctors found</td>
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

    <!-- Add Doctor Offcanvas -->
    <div class="offcanvas offcanvas-end border-0" tabindex="-1" id="add-doctor-form-modal" style="width:600px;">
        <div class="d-flex align-items-center bg-primary bg-gradient p-3 offcanvas-header">
            <h5 class="m-0 me-2 text-white">Add Doctor</h5>
            <button type="button" class="btn-close btn-close-white ms-auto" id="add-doctor-form-modal-close-btn"
                data-bs-dismiss="offcanvas" aria-label="Close"></button>
        </div>
        <div class="offcanvas-body p-0">
            <div data-simplebar class="h-100">
                <div class="p-4">
                    <form method="post" id="addDoctorForm" enctype="multipart/form-data">
                        <div class="row card-body row-sm errMsgDiv">
                            <div class="col-md-12">
                                <p class="errMsg text-danger"></p>
                            </div>
                        </div>
                        <div class="row g-3">
                            <div class="col-md-6">
                                <label class="form-label">Name <span class="text-danger">*</span></label>
                                <input id="doctorName" class="form-control" name="doctorName"
                                    placeholder="Enter doctor name" type="text"
                                    pattern="[A-Za-z\s]{2,100}" required>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label">Phone Number <span class="text-danger">*</span></label>
                                <input id="doctorPhone" class="form-control" name="doctorPhone"
                                    placeholder="Ex: 9xxxxxxxxx" type="text"
                                    pattern="[0-9]{10,10}" required>
                            </div>
                            <!-- LOCATION DROPDOWN -->
                            <div class="col-md-6">
                                <label class="form-label">Location <span class="text-danger">*</span></label>
                                <select class="form-control" name="locationId" required>
                                    <option value="">Select Location</option>
                                    <c:forEach var="loc" items="${locationList}">
                                        <option value="${loc.locationId}">${loc.locationName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label">Profile Image</label>
                                <input id="doctorProfileImage" class="form-control" name="doctorProfileImage"
                                    type="file" accept="image/*">
                            </div>
                            <div class="col-md-6">
                                <label class="form-label">Specialisation</label>
                                <select class="form-control" id="doctorSpecialisation" name="doctorSpecialisation[]" multiple>
                                    <c:forEach var="specialisation" items="${activeSpecialisationList}">
                                        <option value="${specialisation.specialisationId}">
                                            ${specialisation.specialisationName}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="col-md-6" id="scanTypeDiv" style="display:none;">
                                <label class="form-label">Scan Type</label>
                                <select class="form-control" name="scanType[]" id="scanType">
                                    <option value="">Select Scan</option>
                                    <optgroup label="X-Ray">
                                        <option value="XRAY_GENERAL">X-Ray</option>
                                        <option value="XRAY_CHEST">Chest X-Ray</option>
                                        <option value="XRAY_DENTAL">Dental X-Ray</option>
                                        <option value="MAMMOGRAPHY">Mammography</option>
                                        <option value="FLUOROSCOPY">Fluoroscopy</option>
                                        <option value="ANGIOGRAPHY">Angiography</option>
                                    </optgroup>
                                    <optgroup label="CT Scan">
                                        <option value="CT_GENERAL">CT Scan</option>
                                        <option value="CT_BRAIN">CT Brain</option>
                                        <option value="CT_CHEST">CT Chest</option>
                                        <option value="CT_ABDOMEN">CT Abdomen</option>
                                        <option value="CT_PELVIS">CT Pelvis</option>
                                        <option value="CT_SPINE">CT Spine</option>
                                        <option value="CT_ANGIO">CT Angiography</option>
                                        <option value="HRCT">HRCT</option>
                                    </optgroup>
                                    <optgroup label="MRI">
                                        <option value="MRI_BRAIN">MRI Brain</option>
                                        <option value="MRI_SPINE">MRI Spine</option>
                                        <option value="MRI_KNEE">MRI Knee</option>
                                        <option value="MRI_SHOULDER">MRI Shoulder</option>
                                        <option value="MRI_ABDOMEN">MRI Abdomen</option>
                                        <option value="MRI_PELVIS">MRI Pelvis</option>
                                        <option value="MRI_ANGIO">MRI Angiography</option>
                                        <option value="FMRI">Functional MRI (fMRI)</option>
                                    </optgroup>
                                    <optgroup label="Ultrasound">
                                        <option value="US_ABDOMEN">Ultrasound Abdomen</option>
                                        <option value="US_PELVIS">Pelvic Ultrasound</option>
                                        <option value="US_PREGNANCY">Pregnancy Ultrasound</option>
                                        <option value="US_DOPPLER">Doppler Ultrasound</option>
                                        <option value="US_THYROID">Thyroid Ultrasound</option>
                                        <option value="US_BREAST">Breast Ultrasound</option>
                                        <option value="US_TVS">Transvaginal Scan (TVS)</option>
                                        <option value="ECHO">Echocardiography</option>
                                    </optgroup>
                                    <optgroup label="Nuclear Medicine">
                                        <option value="PET">PET Scan</option>
                                        <option value="PET_CT">PET-CT Scan</option>
                                        <option value="BONE_SCAN">Bone Scan</option>
                                        <option value="THYROID_SCAN">Thyroid Scan</option>
                                        <option value="RENAL_SCAN">Renal Scan</option>
                                    </optgroup>
                                    <optgroup label="Special">
                                        <option value="DEXA">DEXA Scan</option>
                                        <option value="BARIUM_SWALLOW">Barium Swallow</option>
                                        <option value="BARIUM_MEAL">Barium Meal</option>
                                        <option value="HSG">Hysterosalpingography (HSG)</option>
                                    </optgroup>
                                </select>
                            </div>

                            <div class="col-md-12">
                                <label class="form-label">Short Description <span class="text-danger">*</span></label>
                                <textarea name="doctorShortDesc" class="form-control" id="doctorShortDesc" required></textarea>
                            </div>
                            <div class="col-md-12">
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" name="isAdminDoctor"
                                        id="isAdminDoctor" value="YES">
                                    <label class="form-check-label" for="isAdminDoctor">Is admin doctor?</label>
                                </div>
                            </div>
                            <div class="col-12 text-end">
                                <button type="submit" id="addDoctorSubmitBtn" class="btn btn-primary">Add Doctor</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <script>
    $(document).ready(function () {

        // Scan type toggle
        $('#doctorSpecialisation').on('change', function () {
            var isRadiologistSelected = false;
            $('#doctorSpecialisation option:selected').each(function () {
                if ($(this).text().toLowerCase().includes('radiologist')) {
                    isRadiologistSelected = true;
                }
            });
            if (isRadiologistSelected) {
                $('#scanTypeDiv').slideDown();
                $('#scanType').attr('required', true);
            } else {
                $('#scanTypeDiv').slideUp();
                $('#scanType').removeAttr('required').val('');
            }
        });

        // Add Doctor form submit
        $('#addDoctorForm').on('submit', function (e) {
            e.preventDefault();
            $('#addDoctorSubmitBtn').attr('disabled', true);
            var formData = new FormData(this);
            $.ajax({
                type: 'POST',
                url: '${pageContext.request.contextPath}/adminmodel/adddoctor',
                data: formData,
                cache: false,
                contentType: false,
                processData: false,
                beforeSend: function () { $('.page-loader').show(); },
                success: function (data) {
                    var json = (typeof data === 'string') ? $.parseJSON(data) : data;
                    $('.page-loader').hide();
                    $('#addDoctorSubmitBtn').removeAttr('disabled');
                    if (json.status === 'EXIST') {
                        Swal.fire('Already Exists', json.message, 'warning');
                    } else if (json.status === 'SUCCESS') {
                        $('#add-doctor-form-modal-close-btn').trigger('click');
                        Swal.fire({ icon: 'success', title: 'Doctor Added!', text: 'The doctor has been added successfully.', timer: 2000, showConfirmButton: false })
                            .then(() => location.reload());
                    } else {
                        Swal.fire('Error', json.message || 'Something went wrong. Please try again.', 'error');
                    }
                },
                error: function () {
                    $('.page-loader').hide();
                    $('#addDoctorSubmitBtn').removeAttr('disabled');
                    Swal.fire('Error', 'Server error. Please try again.', 'error');
                }
            });
        });

        // Choices.js for specialisation multiselect
        if (typeof Choices !== 'undefined') {
            new Choices(document.querySelector('#doctorSpecialisation'), {
                removeItemButton: true,
                searchEnabled: true,
                placeholder: true,
                placeholderValue: 'Select specializations',
                noResultsText: 'No results found'
            });
        }
    });
    </script>
</div>

<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<%@ include file="../include/footer.jsp" %>
</html>
