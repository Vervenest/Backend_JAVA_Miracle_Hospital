<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ include file="../include/header.jsp" %>
<%@ include file="../include/left_navi.jsp" %>
<!-- ============================================================== -->
<!-- Start right Content here -->
<!-- ============================================================== -->
<style>
    .col-md-6,
    .col-md-1,
    .col-md-7,
    .col-md-4,
    .col-md-3,
    .col-md-2,
    .col-md-12 {
        padding-top: 8px;
    }

    .choices {
        margin-bottom: 0px !important;
    }

    .input-group>.choices {
        width: 88%;
    }

    table {
        table-layout: fixed;
        width: 100%;
        font-size: 14px;
    }

    td {
        word-wrap: break-word;
        white-space: normal;
    }

    th,
    td {
        padding: 8px;
        text-align: left;
    }

    .pagination {
        float: right !important;
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
                        <h5 class="mb-sm-0 text-uppercase">Appointment</h5>
                        <div class="page-title-right">
                            <ol class="breadcrumb m-0">
                                <li class="breadcrumb-item text-uppercase"><a
                                        href="javascript: void(0);">Appointment</a></li>
                                <li class="breadcrumb-item active text-uppercase">List</li>
                            </ol>
                        </div>
                    </div>
                </div>
            </div>
            <!-- end page title -->

            <div class="row">
                <div class="col-lg-12">
                    <div class="card">
                        <div class="card-body">
                            
                            <form action="${pageContext.request.contextPath}/admin/appointment/appointmentlist" method="post">
                                <div class="row">
                                    <div class="col-md-3">
                                        <label class="form-label"> Date : <span class="text-danger">*</span></label>
                                        <input type="date" class="form-control" id="appointmentDate"
                                            name="appointmentDate" value="${filterAppointmentDate}"
                                            required>
                                    </div>

                                    <div class="col-md-3">
                                        <div class="mb-3">
                                            <label for="choices-multiple-remove-button"
                                                class="form-label">Doctor</label>
                                            <select class="form-control" id="doctor" name="doctor">
                                                <option value="">Select</option>
                                                <c:if test="${not empty doctorList}">
                                                    <c:forEach var="doc" items="${doctorList}">
                                                        <option value="${doc.doctorStringId}"
                                                            <c:if test="${filterDoctorId == doc.doctorStringId}">selected</c:if>
                                                        >${doc.doctorName}</option>
                                                    </c:forEach>
                                                </c:if>
                                            </select>
                                        </div>
                                    </div>

                                    <div class="col-md-3">
                                        <label class="form-label"> Patient Name : <span
                                                class="text-danger">*</span></label>
                                        <input id="patientName" class="form-control" name="patientName"
                                            placeholder="Enter patient" type="text" pattern="[A-Za-z\s]{2,100}"
                                            value="${filterPatientName}">
                                    </div>

                                    <div class="col-md-3">
                                        <button type="submit" class="btn btn-md btn-primary mt-4">Filter</button>
                                    </div>
                                </div>

                            </form>
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
                                    <h5 class="card-title mb-0 text-uppercase">Appointment List</h5>
                                </div>
                                <div class="offset-md-4 col-md-2">
                                    <a href="${pageContext.request.contextPath}/admin/appointment/addappointment"
                                        class="btn btn-sm btn-primary">Add Appointment</a>
                                </div>
                            </div>


                        </div>
                        <!-- Appointment List Table -->
                        <div class="card-body">
                            <div id="scroll-horizontal_wrapper" class="dataTables_wrapper dt-bootstrap5 no-footer">
                                <div class="row">
                                    <div class="col-sm-12">
                                        <div class="dataTables_scroll">
                                            <div class="dataTables_scrollBody">
                                                <table class="table table-bordered table-striped" style="width:100%">
                                                    <thead class="text-uppercase">
                                                        <tr>
                                                            <td>S. No.</td>
                                                            <td>Appointment No</td>
                                                            <td>Token No</td>
                                                            <td>Doctor</td>
                                                            <td>Patient</td>
                                                            <td>Date</td>
                                                            <td>Time</td>
                                                            <td>Status</td>
                                                            <td>Action</td>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <c:choose>
                                                            <c:when test="${not empty appointmentList}">
                                                                <c:forEach var="appt" items="${appointmentList}" varStatus="status">
                                                                <tr>
                                                                    <td>${status.count}</td>
                                                                    <td>${appt.apptId}</td>
                                                                    <td><span class="badge bg-secondary">${appt.todayTokenNo}</span></td>
                                                                    <td data-doctor-id="${appt.doctorId}">${appt.doctorName}</td>
                                                                    <td>${appt.patientName}</td>
                                                                    <td data-appointment-date="${appt.appointmentDate}">${appt.appointmentDate}</td>
                                                                    <td>${appt.appointmentStartTime}</td>
                                                                    <td>
                                                                        <c:choose>
                                                                            <c:when test="${appt.appointmentStatus == '1'}">
                                                                                <span class="badge bg-primary">Open</span>
                                                                            </c:when>
                                                                            <c:when test="${appt.appointmentStatus == '2'}">
                                                                                <span class="badge bg-success">Completed</span>
                                                                            </c:when>
                                                                            <c:when test="${appt.appointmentStatus == '3'}">
                                                                                <span class="badge bg-danger">Cancelled</span>
                                                                            </c:when>
                                                                            <c:when test="${appt.appointmentStatus == '4'}">
                                                                                <span class="badge bg-warning">Delayed</span>
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <span class="badge bg-secondary">${appt.appointmentStatus}</span>
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                    </td>
                                                                    <td>
                                                                        <div class="dropdown">
                                                                            <a href="#" role="button" id="dropdownMenuLink_${status.count}"
                                                                                data-bs-toggle="dropdown" aria-expanded="false">
                                                                                <i class="ri-more-2-fill"></i>
                                                                            </a>
                                                                            <ul class="dropdown-menu">
                                                                                <li><a class="dropdown-item"
                                                                                        href="#" onclick="confirmStatusUpdate('1', '${appt.apptId}', 'Open')">Open</a>
                                                                                </li>
                                                                                <li><a class="dropdown-item"
                                                                                        href="#" onclick="confirmStatusUpdate('2', '${appt.apptId}', 'Completed')">Completed</a>
                                                                                </li>
                                                                                <li><a class="dropdown-item"
                                                                                        href="#" onclick="confirmStatusUpdate('3', '${appt.apptId}', 'Cancelled')">Cancelled</a>
                                                                                </li>
                                                                                <li><a class="dropdown-item rescheduleBtn"
                                                                                        href="#"
                                                                                        data-id="${appt.apptId}"
                                                                                        data-doctor="${appt.doctorId}"
                                                                                        data-date="${appt.appointmentDate}">Reschedule</a>
                                                                                </li>
                                                                                <li><a class="dropdown-item"
                                                                                        href="${pageContext.request.contextPath}/admin/appointment/details/${appt.apptId}">View Details</a>
                                                                                </li>
                                                                            </ul>
                                                                        </div>
                                                                    </td>
                                                                </tr>
                                                                </c:forEach>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <tr><td colspan="9" class="text-center text-muted py-3">No appointments found</td></tr>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </tbody>
                                                </table>

                                                <!-- Reschedule Modal -->
                                                <div class="modal fade" id="rescheduleModal" tabindex="-1"
                                                    aria-labelledby="rescheduleModalLabel" aria-hidden="true">
                                                    <div class="modal-dialog modal-dialog-centered">
                                                        <div class="modal-content">
                                                            <form id="rescheduleForm">
                                                                <div class="modal-header">
                                                                    <h5 class="modal-title" id="rescheduleModalLabel">
                                                                        Reschedule Appointment</h5>
                                                                    <button type="button" class="btn-close"
                                                                        data-bs-dismiss="modal"
                                                                        aria-label="Close"></button>
                                                                </div>
                                                                <div class="modal-body">
                                                                    <input type="hidden" name="appointmentId"
                                                                        id="rescheduleAppointmentId">
                                                                    <input type="hidden" name="doctorId"
                                                                        id="rescheduleDoctorId">

                                                                    <div class="mb-3">
                                                                        <label class="form-label">Date</label>
                                                                        <input type="date" id="rescheduleDate"
                                                                            name="appointmentDate" class="form-control"
                                                                            required>
                                                                    </div>

                                                                    <div class="mb-3">
                                                                        <label class="form-label">Available
                                                                            Slots</label>
                                                                        <select id="rescheduleSlot"
                                                                            name="appointmentStartTime"
                                                                            class="form-select" required>
                                                                            <option value="">Select Slot</option>
                                                                        </select>
                                                                    </div>
                                                                </div>
                                                                <div class="modal-footer">
                                                                    <button type="button" class="btn btn-secondary"
                                                                        data-bs-dismiss="modal">Cancel</button>
                                                                    <button type="submit" class="btn btn-primary">Update
                                                                        Appointment</button>
                                                                </div>
                                                            </form>
                                                        </div>
                                                    </div>
                                                </div>
                                                <!-- End Modal -->

                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
                        <script>
                            $(document).ready(function () {

                                // Open reschedule modal
                                $(document).on('click', '.rescheduleBtn', function (e) {
                                    e.preventDefault();
                                    const appointmentId = $(this).data('id');
                                    const doctorId = $(this).data('doctor');
                                    const date = $(this).data('date');

                                    $('#rescheduleAppointmentId').val(appointmentId);
                                    $('#rescheduleDoctorId').val(doctorId);
                                    $('#rescheduleDate').val(date);
                                    $('#rescheduleSlot').html('<option value="">Select Slot</option>');

                                    $('#rescheduleModal').modal('show');

                                    if (doctorId && date) fetchSlots(doctorId, date);
                                });

                                // Fetch slots on date change
                                $('#rescheduleDate').on('change', function () {
                                    const doctorId = $('#rescheduleDoctorId').val();
                                    const date = $(this).val();
                                    fetchSlots(doctorId, date);
                                });

                                function fetchSlots(doctorId, date) {
                                    if (!doctorId || !date) return;
                                    $('#rescheduleSlot').prop('disabled', true).html('<option>Loading...</option>');

                                    $.post('${pageContext.request.contextPath}/Patientapi/getAvailableSlots', { doctorId, date }, function (res) {
                                        if (res.status === 'success' && res.result && res.result.list && res.result.list.length) {
                                            let html = '<option value="">Select Slot</option>';
                                            res.result.list.forEach(s => {
                                                html += '<option value="' + s.startTime + '" data-end="' + s.endTime + '">' + s.startTime + ' - ' + s.endTime + '</option>';
                                            });
                                            $('#rescheduleSlot').html(html).prop('disabled', false);
                                        } else {
                                            $('#rescheduleSlot').html('<option>No slots available</option>').prop('disabled', true);
                                        }
                                    }, 'json');
                                }

                                // Submit reschedule form
                                $('#rescheduleForm').submit(function (e) {
                                    e.preventDefault();
                                    const appointmentId = $('#rescheduleAppointmentId').val();
                                    const date = $('#rescheduleDate').val();
                                    const startTime = $('#rescheduleSlot').val();
                                    const endTime = $('#rescheduleSlot option:selected').data('end') || '';

                                    $.post('${pageContext.request.contextPath}/adminmodel/rescheduleAppointment',
                                        { appointmentId, appointmentDate: date, appointmentStartTime: startTime, appointmentEndTime: endTime },
                                        function (res) {
                                            try {
                                                if (typeof res === 'string') res = JSON.parse(res);
                                                if (res.status === 'success') {
                                                    $('#rescheduleModal').modal('hide');
                                                    Swal.fire({
                                                        icon: 'success',
                                                        title: 'Success',
                                                        text: res.message || 'Appointment rescheduled successfully',
                                                        timer: 2000,
                                                        showConfirmButton: false
                                                    }).then(() => location.reload());
                                                } else {
                                                    Swal.fire('Error', res.message || 'Failed to reschedule', 'error');
                                                }
                                            } catch (e) {
                                                Swal.fire('Error', 'Something went wrong', 'error');
                                            }
                                        }, 'json');
                                });


                            });
                        </script>
                        <!-- End Appointment List Table -->
                    </div>
                </div>
            </div>
        </div>
    </div>

<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script>
function confirmStatusUpdate(status, appointmentId, label) {
    Swal.fire({
        title: 'Mark as ' + label + '?',
        text: 'This will update the appointment status.',
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#0ab39c',
        cancelButtonColor: '#6c757d',
        confirmButtonText: 'Yes, update it!'
    }).then(result => {
        if (result.isConfirmed) {
            window.location.href = '${pageContext.request.contextPath}/adminmodel/updateAppointmentStatus/' + status + '/' + appointmentId;
        }
    });
}
</script>
<script>
// Fix for missing elements referenced in app.js
document.addEventListener('DOMContentLoaded', function() {
    var els = ['removeNotificationModal', 'notification-actions', 'select-content', 'notificationDropdown'];
    els.forEach(function(id) {
        if (!document.getElementById(id)) {
            var d = document.createElement('div');
            d.id = id;
            d.style.display = 'none';
            document.body.appendChild(d);
        }
    });
});
</script>
<%@ include file="../include/footer.jsp" %>
</html>