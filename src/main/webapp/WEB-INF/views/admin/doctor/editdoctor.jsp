<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ include file="../include/header.jsp" %>
<%@ include file="../include/left_navi.jsp" %>
<div class="main-content">

    <div class="page-content">
        <div class="container-fluid">

            <!-- start page title -->
            <div class="row">
                <div class="col-12">
                    <div class="page-title-box d-sm-flex align-items-center justify-content-between">
                        <h5 class="mb-sm-0">Doctors</h5>

                        <div class="page-title-right">
                            <ol class="breadcrumb m-0">
                                <li class="breadcrumb-item"><a href="javascript: void(0);">Doctors</a></li>
                                <li class="breadcrumb-item active">Edit</li>
                            </ol>
                        </div>

                    </div>
                </div>
            </div>
            <!-- end page title -->



            <div class="row">
                
                <div class="col-lg-12">
                    <a href="${pageContext.request.contextPath}/admin/doctor/doctorlist">
                        <button type="button" class="btn btn-primary btn-label rounded-pill">                        
                            <i class="ri-arrow-left-line label-icon align-middle rounded-pill fs-16 me-2"></i> Back
                        </button>
                    </a>
                </div>

                <!-- flash message -->
                <c:if test="${not empty successMsg}">
                    <div class="alert alert-success alert-dismissible fade show">
                    <svg viewBox="0 0 24 24" width="24" height="24" stroke="currentColor" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round" class="me-2">
                        <polyline points="9 11 12 14 22 4"></polyline>
                        <path d="M21 12v7a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h11"></path>
                    </svg>
                    <strong>${successMsg}</strong>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="btn-close"> </button>
                    </div>
                </c:if>
                <c:if test="${not empty errorMsg}">
                    <div class="alert alert-danger alert-dismissible fade show">
                    <svg viewBox="0 0 24 24" width="24" height="24" stroke="currentColor" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round" class="me-2">
                        <polygon points="7.86 2 16.14 2 22 7.86 22 16.14 16.14 22 7.86 22 2 16.14 2 7.86 7.86 2"></polygon>
                        <line x1="15" y1="9" x2="9" y2="15"></line>
                        <line x1="9" y1="9" x2="15" y2="15"></line>
                    </svg>
                    <strong>${errorMsg}</strong>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="btn-close"> </button>
                    </div>
                </c:if>

                <div class="col-lg-12">
                    <div class="card">
                        <div class="card-header align-items-center d-flex">
                            <h4 class="card-title mb-0 flex-grow-1">Edit Doctor</h4>

                        </div><!-- end card header -->
                        <c:if test="${not empty doctor}">
                        <div class="card-body">
                            <div class="live-preview">
                                <form class="row g-3" id="editDoctorForm" method="post" action="${pageContext.request.contextPath}/adminmodel/editdoctor" enctype="multipart/form-data">
                                    
                                    <div class="col-md-6">
                                        <label class="form-label"> Name : <span class="text-danger">*</span></label>
                                        <input id="doctorName" class="form-control" name="doctorName" placeholder="Enter doctor" type="text" pattern="[A-Za-z\s]{2,100}" value="${doctor.doctorName}" required>
                                    </div>
                                    <div class="col-md-6">
                                        <label class="form-label"> Phone Number : <span class="text-danger">*</span></label>
                                        <input id="doctorPhone" class="form-control" name="doctorPhone" placeholder="Ex: 9xxxxxxxxx" type="text" pattern="[0-9]{10,10}" value="${doctor.phoneNumber}" required>
                                    </div>

                                    <!-- LOCATION DROPDOWN -->
                                    <div class="col-md-6">
                                        <label class="form-label">Location : <span class="text-danger">*</span></label>
                                        <select class="form-control" name="locationId" required>
                                            <option value="">Select Location</option>
                                            <c:forEach var="loc" items="${locationList}">
                                                <option value="${loc.locationId}"
                                                    <c:if test="${loc.locationId == doctor.locationId}">selected</c:if>>
                                                    ${loc.locationName}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <div class="col-md-6">
                                        <div class="mb-3">
                                            <label for="choices-multiple-remove-button" class="form-label">Specialisation</label>
                                            <select class="form-control" id="doctorSpecialisation" name="doctorSpecialisation[]" multiple>
                                                <c:set var="selectedSpecs" value="${fn:split(doctor.specialization, ',')}" />
                                                <c:if test="${not empty activeSpecialisationList}">
                                                    <c:forEach var="specialisation" items="${activeSpecialisationList}">
                                                        <option value="${specialisation.specialisationId}" 
                                                            <c:forEach var="spec" items="${selectedSpecs}">
                                                                <c:if test="${spec == specialisation.specialisationId}">selected</c:if>
                                                            </c:forEach>
                                                        >${specialisation.specialisationName}</option>
                                                    </c:forEach>
                                                </c:if>
                                            </select>
                                        </div>
                                    </div>

                                    <div class="col-md-6" id="scanTypeDiv" style="display:none;">
<label class="form-label">Scan Type</label>
<c:set var="scanArr" value="${not empty doctor.scanType ? fn:split(doctor.scanType, ',') : ''}" />
<select class="form-control" name="scanType[]" id="scanType" multiple>
<optgroup label="X-Ray">
<option value="XRAY_GENERAL" <c:forEach var="scan" items="${scanArr}"><c:if test="${scan == 'XRAY_GENERAL'}">selected</c:if></c:forEach>>X-Ray</option>
<option value="XRAY_CHEST" <c:forEach var="scan" items="${scanArr}"><c:if test="${scan == 'XRAY_CHEST'}">selected</c:if></c:forEach>>Chest X-Ray</option>
<option value="XRAY_DENTAL" <c:forEach var="scan" items="${scanArr}"><c:if test="${scan == 'XRAY_DENTAL'}">selected</c:if></c:forEach>>Dental X-Ray</option>
<option value="MAMMOGRAPHY" <c:forEach var="scan" items="${scanArr}"><c:if test="${scan == 'MAMMOGRAPHY'}">selected</c:if></c:forEach>>Mammography</option>
<option value="FLUOROSCOPY" <c:forEach var="scan" items="${scanArr}"><c:if test="${scan == 'FLUOROSCOPY'}">selected</c:if></c:forEach>>Fluoroscopy</option>
<option value="ANGIOGRAPHY" <c:forEach var="scan" items="${scanArr}"><c:if test="${scan == 'ANGIOGRAPHY'}">selected</c:if></c:forEach>>Angiography</option>
</optgroup>
<optgroup label="CT Scan">
<option value="CT_GENERAL" <c:forEach var="scan" items="${scanArr}"><c:if test="${scan == 'CT_GENERAL'}">selected</c:if></c:forEach>>CT Scan</option>
<option value="CT_BRAIN" <c:forEach var="scan" items="${scanArr}"><c:if test="${scan == 'CT_BRAIN'}">selected</c:if></c:forEach>>CT Brain</option>
<option value="CT_CHEST" <c:forEach var="scan" items="${scanArr}"><c:if test="${scan == 'CT_CHEST'}">selected</c:if></c:forEach>>CT Chest</option>
<option value="CT_ABDOMEN" <c:forEach var="scan" items="${scanArr}"><c:if test="${scan == 'CT_ABDOMEN'}">selected</c:if></c:forEach>>CT Abdomen</option>
<option value="CT_PELVIS" <c:forEach var="scan" items="${scanArr}"><c:if test="${scan == 'CT_PELVIS'}">selected</c:if></c:forEach>>CT Pelvis</option>
<option value="CT_SPINE" <c:forEach var="scan" items="${scanArr}"><c:if test="${scan == 'CT_SPINE'}">selected</c:if></c:forEach>>CT Spine</option>
<option value="CT_ANGIO" <c:forEach var="scan" items="${scanArr}"><c:if test="${scan == 'CT_ANGIO'}">selected</c:if></c:forEach>>CT Angiography</option>
<option value="HRCT" <c:forEach var="scan" items="${scanArr}"><c:if test="${scan == 'HRCT'}">selected</c:if></c:forEach>>HRCT</option>
</optgroup>
<optgroup label="MRI">
<option value="MRI_BRAIN" <c:forEach var="scan" items="${scanArr}"><c:if test="${scan == 'MRI_BRAIN'}">selected</c:if></c:forEach>>MRI Brain</option>
<option value="MRI_SPINE" <c:forEach var="scan" items="${scanArr}"><c:if test="${scan == 'MRI_SPINE'}">selected</c:if></c:forEach>>MRI Spine</option>
<option value="MRI_KNEE" <c:forEach var="scan" items="${scanArr}"><c:if test="${scan == 'MRI_KNEE'}">selected</c:if></c:forEach>>MRI Knee</option>
<option value="MRI_SHOULDER" <c:forEach var="scan" items="${scanArr}"><c:if test="${scan == 'MRI_SHOULDER'}">selected</c:if></c:forEach>>MRI Shoulder</option>
<option value="MRI_ABDOMEN" <c:forEach var="scan" items="${scanArr}"><c:if test="${scan == 'MRI_ABDOMEN'}">selected</c:if></c:forEach>>MRI Abdomen</option>
<option value="MRI_PELVIS" <c:forEach var="scan" items="${scanArr}"><c:if test="${scan == 'MRI_PELVIS'}">selected</c:if></c:forEach>>MRI Pelvis</option>
<option value="MRI_ANGIO" <c:forEach var="scan" items="${scanArr}"><c:if test="${scan == 'MRI_ANGIO'}">selected</c:if></c:forEach>>MRI Angiography</option>
<option value="FMRI" <c:forEach var="scan" items="${scanArr}"><c:if test="${scan == 'FMRI'}">selected</c:if></c:forEach>>fMRI</option>
</optgroup>
<optgroup label="Ultrasound">
<option value="US_ABDOMEN" <c:forEach var="scan" items="${scanArr}"><c:if test="${scan == 'US_ABDOMEN'}">selected</c:if></c:forEach>>Ultrasound Abdomen</option>
<option value="US_PELVIS" <c:forEach var="scan" items="${scanArr}"><c:if test="${scan == 'US_PELVIS'}">selected</c:if></c:forEach>>Pelvic Ultrasound</option>
<option value="US_PREGNANCY" <c:forEach var="scan" items="${scanArr}"><c:if test="${scan == 'US_PREGNANCY'}">selected</c:if></c:forEach>>Pregnancy Ultrasound</option>
<option value="US_DOPPLER" <c:forEach var="scan" items="${scanArr}"><c:if test="${scan == 'US_DOPPLER'}">selected</c:if></c:forEach>>Doppler Ultrasound</option>
<option value="US_THYROID" <c:forEach var="scan" items="${scanArr}"><c:if test="${scan == 'US_THYROID'}">selected</c:if></c:forEach>>Thyroid Ultrasound</option>
<option value="US_BREAST" <c:forEach var="scan" items="${scanArr}"><c:if test="${scan == 'US_BREAST'}">selected</c:if></c:forEach>>Breast Ultrasound</option>
<option value="US_TVS" <c:forEach var="scan" items="${scanArr}"><c:if test="${scan == 'US_TVS'}">selected</c:if></c:forEach>>TVS</option>
<option value="ECHO" <c:forEach var="scan" items="${scanArr}"><c:if test="${scan == 'ECHO'}">selected</c:if></c:forEach>>Echocardiography</option>
</optgroup>
<optgroup label="Nuclear Medicine">
<option value="PET" <c:forEach var="scan" items="${scanArr}"><c:if test="${scan == 'PET'}">selected</c:if></c:forEach>>PET Scan</option>
<option value="PET_CT" <c:forEach var="scan" items="${scanArr}"><c:if test="${scan == 'PET_CT'}">selected</c:if></c:forEach>>PET-CT</option>
<option value="BONE_SCAN" <c:forEach var="scan" items="${scanArr}"><c:if test="${scan == 'BONE_SCAN'}">selected</c:if></c:forEach>>Bone Scan</option>
<option value="THYROID_SCAN" <c:forEach var="scan" items="${scanArr}"><c:if test="${scan == 'THYROID_SCAN'}">selected</c:if></c:forEach>>Thyroid Scan</option>
<option value="RENAL_SCAN" <c:forEach var="scan" items="${scanArr}"><c:if test="${scan == 'RENAL_SCAN'}">selected</c:if></c:forEach>>Renal Scan</option>
</optgroup>
</select>
</div>

                                    <c:if test="${not empty doctor.profileImage}">
                                    <div class="col-md-6">
                                        <div class="text-center">
                                            <div class="profile-user position-relative d-inline-block mx-auto  mb-4">
                                                <img src="${pageContext.request.contextPath}/assets/doctorProfileImage/${doctor.profileImage}" class="rounded-circle avatar-xl img-thumbnail user-profile-image" alt="user-profile-image">
                                                <div class="avatar-xs p-0 rounded-circle profile-photo-edit">
                                                    <input id="profile-img-file-input" type="file" class="profile-img-file-input">
                                                    <label for="profile-img-file-input" class="profile-photo-edit avatar-xs">
                                                        <a href="${pageContext.request.contextPath}/adminmodel/deldoctorprofileimg/${doctor.doctorStringId}" onclick="return confirm('Are you sure you want to delete profile image?');">
                                                            <span class="avatar-title rounded-circle bg-light text-body"><i class="ri-delete-bin-line"></i></span>
                                                        </a>
                                                    </label>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <input type="hidden" name="imageStatus" value="EXIST">
                                    </c:if>
                                    <c:if test="${empty doctor.profileImage}">
                                    <div class="col-md-6">
                                        <label class="form-label"> Profile Image : <span class="text-danger">*</span></label>
                                        <input id="doctorProfileImage" class="form-control" name="doctorProfileImage" placeholder="Image" type="file" accept="image/*" >
                                    </div>
                                    <input type="hidden" name="imageStatus" value="NOTEXIST">
                                    </c:if>

                                    <div class="col-md-6">
                                        <label class="form-label"> Short Description : <span class="text-danger">*</span></label>
                                        <textarea name="doctorShortDesc" class="form-control" id="doctorShortDesc" required>${doctor.shortDescription}</textarea>
                                    </div>

                                    <div class="col-md-6">
                                        <label class="form-label"> <br/></label>
                                        <div class="form-check mb-2">
                                            <input class="form-check-input" type="checkbox" name="isAdminDoctor" id="isAdminDoctor" value="YES" <c:if test="${doctor.isAdminDoctor == 'YES'}">checked</c:if> >
                                            <label class="form-check-label" for="isAdminDoctor">
                                                Is admin doctor?
                                            </label>
                                        </div>
                                    </div>

                                    <input type="hidden" name="doctorId" id="doctorId" value="${doctor.doctorStringId}">

                                    <div class="col-12">
                                        <button class="btn btn-primary" type="submit" id="editDoctorSubmitBtn">Save Changes</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                        </c:if>
                    </div>
                </div> <!-- end col -->

            </div>
            <!-- end row -->

        </div>
        
    </div> <!-- container-fluid -->
</div>
<!-- End Page-content -->



</div>

<script>
$(document).ready(function() {

    var doctorSpecialisationElement = document.querySelector('#doctorSpecialisation');

    var doctorSpecialisationChoicesInstance = new Choices(doctorSpecialisationElement, {
        removeItemButton: true,
        searchEnabled: true,
        placeholder: true,
        placeholderValue: 'Select specializations',
        noResultsText: 'No results found'
    });

    function toggleScanDropdown() {

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
    }

    // Run on page load (important for edit page)
    toggleScanDropdown();

    // Run on change
    $('#doctorSpecialisation').on('change', function () {
        toggleScanDropdown();
    });

});
</script>

<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script>
$(document).ready(function() {
    $('#editDoctorForm').on('submit', function(e) {
        e.preventDefault();
        var btn = $('#editDoctorSubmitBtn');
        btn.prop('disabled', true).text('Saving...');
        var formData = new FormData(this);
        $.ajax({
            type: 'POST',
            url: '${pageContext.request.contextPath}/adminmodel/editdoctor',
            data: formData,
            cache: false,
            contentType: false,
            processData: false,
            success: function(data) {
                btn.prop('disabled', false).text('Save Changes');
                var json = (typeof data === 'string') ? JSON.parse(data) : data;
                if (json.status === 'SUCCESS') {
                    Swal.fire({ icon: 'success', title: 'Updated!', text: 'Doctor details have been updated successfully.', timer: 2000, showConfirmButton: false })
                        .then(() => window.location.href = '${pageContext.request.contextPath}/admin/doctor/doctorlist');
                } else {
                    Swal.fire('Error', json.message || 'Failed to update doctor. Please try again.', 'error');
                }
            },
            error: function() {
                btn.prop('disabled', false).text('Save Changes');
                Swal.fire('Error', 'Server error. Please try again.', 'error');
            }
        });
    });
});
</script>
<%@ include file="../include/footer.jsp" %>
</html>
