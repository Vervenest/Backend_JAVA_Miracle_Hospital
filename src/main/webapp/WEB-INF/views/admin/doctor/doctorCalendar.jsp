<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ include file="../include/header.jsp" %>
<%@ include file="../include/left_navi.jsp" %>
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
                        <h5 class="mb-sm-0 text-uppercase">Doctor Calendar</h5>
                        <div class="page-title-right">
                            <ol class="breadcrumb m-0">
                                <li class="breadcrumb-item text-uppercase"><a href="javascript: void(0);">Calendar</a>
                                </li>
                                <li class="breadcrumb-item active text-uppercase">Doctor Schedule</li>
                            </ol>
                        </div>
                    </div>
                </div>
            </div>
            <!-- end page title -->

            <div class="row">
                <div class="offset-md-10 col-md-2 mb-3">
                    <a href="${pageContext.request.contextPath}/admin/doctor/doctorlist"
                        class="btn btn-sm btn-primary">Doctors List</a>
                </div>
            </div>

            <div class="row">
                <!-- Loader for the Main Page -->
                <div class="page-loader"
                    style="display:none; position:fixed; top:0; left:0; width:100%; height:100%; background:rgba(255, 255, 255, 0.8); text-align:center; z-index:9999;">
                    <img src="${pageContext.request.contextPath}/assets/images/animated_loader_gif.gif" alt="Loading..."
                        style="position:relative; top:50%; transform:translateY(-50%);" />
                </div>

            </div>
            <c:set var="doctorId" value="${requestScope.doctorId}" />
            <c:set var="doctorName" value="${not empty doctor ? doctor.doctorName : 'Unknown'}" />
            <c:set var="appointments" value="${requestScope.appointments}" />
            <div class="row">
                <div class="col-12">

                    <div class="modal fade" id="event-modal" tabindex="-1" aria-hidden="true" role="dialog">
                        <div class="modal-dialog modal-dialog-centered">
                            <div class="modal-content border-0">
                                <div class="modal-header p-3 bg-info-subtle">
                                    <h5 class="modal-title" id="modal-title">Appointment Details</h5>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal"
                                        aria-hidden="true"></button>
                                </div>
                                <div class="modal-body p-4">
                                    <form class="needs-validation view-event" novalidate>
                                        <div class="event-details">
                                            <div class="d-flex mb-2">
                                                <div class="flex-grow-1 d-flex align-items-center">
                                                    <div class="flex-shrink-0 me-3">
                                                        <i class="ri-calendar-event-line text-muted fs-16"></i>
                                                    </div>
                                                    <div class="flex-grow-1">
                                                        <h6 class="d-block fw-semibold mb-0" id="event-start-date-tag">
                                                            07 July, 2025</h6>
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="d-flex align-items-center mb-2">
                                                <div class="flex-shrink-0 me-3">
                                                    <i class="ri-time-line text-muted fs-16"></i>
                                                </div>
                                                <div class="flex-grow-1">
                                                    <h6 class="d-block fw-semibold mb-0">
                                                        <span id="event-timepicker1-tag">12:00 AM</span>
                                                        <!-- - <span id="event-timepicker2-tag">5:30 AM</span> -->
                                                    </h6>
                                                </div>
                                            </div>

                                            <div class="d-flex align-items-center mb-2">
                                                <div class="flex-shrink-0 me-3">
                                                    <i class="ri-user-line text-muted fs-16"></i>
                                                </div>
                                                <div class="flex-grow-1">
                                                    <h6 class="d-block fw-semibold mb-0">Patient Id:
                                                        <span id="event-patient-id-tag">12345</span>
                                                    </h6>
                                                </div>
                                            </div>


                                            <div class="d-flex align-items-center mb-2">
                                                <div class="flex-shrink-0 me-3">
                                                    <i class="ri-user-line text-muted fs-16"></i>
                                                </div>
                                                <div class="flex-grow-1">
                                                    <h6 class="d-block fw-semibold mb-0">Patient Name: <span
                                                            id="event-patient-name-tag">N/A</span></h6>
                                                </div>
                                            </div>

                                            <div class="d-flex align-items-center mb-2">
                                                <div class="flex-shrink-0 me-3">
                                                    <i class="ri-stethoscope-line text-muted fs-16"></i>
                                                </div>
                                                <div class="flex-grow-1">
                                                    <h6 class="d-block fw-semibold mb-0">Doctor: <span
                                                            id="event-doctor-name-tag">N/A</span></h6>
                                                </div>
                                            </div>

                                            <div class="d-flex align-items-center mb-2">
                                                <div class="flex-shrink-0 me-3">
                                                    <i class="ri-hashtag text-muted fs-16"></i>
                                                </div>
                                                <div class="flex-grow-1">
                                                    <h6 class="d-block fw-semibold mb-0">Appointment No: <span
                                                            id="event-appointment-number-tag">N/A</span></h6>
                                                </div>
                                            </div>

                                            <div class="d-flex align-items-center mb-2">
                                                <div class="flex-shrink-0 me-3">
                                                    <i class="ri-key-line text-muted fs-16"></i>
                                                </div>
                                                <div class="flex-grow-1">
                                                    <h6 class="d-block fw-semibold mb-0">Token No: <span
                                                            id="event-token-number-tag">N/A</span></h6>
                                                </div>
                                            </div>

                                        </div>
                                        <div class="text-end">
                                            <button type="button" class="btn btn-soft-danger" data-bs-dismiss="modal">
                                                <i class="ri-close-line align-bottom"></i> Close
                                            </button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                         <h5 class="mb-1">All Appointments for Doctor - <span
                        style="color: #f94d17;">${doctorName}</span></h5>
                        <div class="col-xl-3">
                            <div>
                               
                                <p class="text-muted">Don't miss any appointments events</p>
                                <div class="pe-2 me-n1 mb-3" data-simplebar style="height: 510px">
                                    <div id="upcoming-event-list">
                                        <c:choose>
                                            <c:when test="${not empty appointments}">
                                                <c:forEach var="apt" items="${appointments}">
                                                <div class="card mb-3">
                                                    <div class="card-body">
                                                        <div class="d-flex mb-3">
                                                            <div class="flex-grow-1">
                                                                <i class="mdi mdi-checkbox-blank-circle me-2 text-info"></i>
                                                                <span class="fw-medium">${apt.appointmentDate}</span>
                                                            </div>
                                                            <div class="flex-shrink-0">
                                                                <small class="badge bg-primary-subtle text-primary ms-auto">
                                                                    ${apt.appointmentStartTime}
                                                                </small>
                                                            </div>
                                                        </div>
                                                        <h6 class="card-title fs-16">${apt.patientName}</h6>
                                                        <p class="text-muted mb-0">
                                                            Appointment No: ${apt.appointmentId}<br>
                                                            Token No: ${apt.todayTokenNo}
                                                        </p>
                                                    </div>
                                                </div>
                                                </c:forEach>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="card mb-3"><div class="card-body"><p class="text-muted mb-0">No appointments found for this doctor.</p></div></div>
                                            </c:otherwise>
                                        </c:choose>

                                        <c:if test="${empty appointments}">
                                            <div class="card mb-3">
                                                <div class="card-body">
                                                    <p class="text-muted mb-0">No upcoming appointments for this doctor.</p>
                                                </div>
                                            </div>
                                        </c:if>
                                    </div>


                                </div>
                            </div>
                        </div>
                        <div class="col-xl-9">
                            <div class="card card-h-100">
                                <div class="card-body">
                                    <div id="calendar"></div>
                                </div>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="../include/footer.jsp" %>
</html><script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

<script>

    document.addEventListener('DOMContentLoaded', function () {
        var calendarEl = document.getElementById('calendar');

        var calendar = new FullCalendar.Calendar(calendarEl, {
            initialView: 'dayGridMonth',
            headerToolbar: {
                left: 'prev,next today',
                center: 'title',
                right: 'dayGridMonth,timeGridWeek,timeGridDay,listWeek'
            },
            height: 'auto',
            events: [
                <c:forEach var="apt" items="${appointments}">
                     {
                        id: "${apt.appointmentId}",
                        title: "${apt.patientName}",
                        start: "${apt.appointmentDate}T${apt.appointmentStartTime}",
                        extendedProps: {
                            patientId: "${apt.patientId}",
                            patientName: "${apt.patientName}",
                            doctorName: "${apt.doctorName}",
                            appointmentNumber: "${apt.appointmentId}",
                            tokenNumber: "${apt.todayTokenNo}",
                            startTime: "${apt.appointmentStartTime}"
                        },
                        backgroundColor: "#0ab39c",
                        borderColor: "#0ab39c"
                    },
                </c:forEach>
            ],
            eventClick: function (info) {
                const event = info.event;

                document.getElementById('modal-title').innerText = "Appointment Details";
                document.getElementById('event-start-date-tag').innerText = formatDate(event.start);
                document.getElementById('event-timepicker1-tag').innerText = formatTime(event.start);
                document.getElementById('event-patient-id-tag').innerText = event.extendedProps.patientId || 'N/A';
                document.getElementById('event-patient-name-tag').innerText = event.extendedProps.patientName || 'N/A';
                document.getElementById('event-doctor-name-tag').innerText = event.extendedProps.doctorName || 'N/A';
                document.getElementById('event-appointment-number-tag').innerText = event.extendedProps.appointmentNumber || 'N/A';
                document.getElementById('event-token-number-tag').innerText = event.extendedProps.tokenNumber || 'N/A';


                const myModal = new bootstrap.Modal(document.getElementById('event-modal'));
                myModal.show();
            }

        });

        calendar.render();
    });

    function formatDate(date) {
        const options = { day: '2-digit', month: 'long', year: 'numeric' };
        return new Date(date).toLocaleDateString('en-US', options);
    }

    function formatTime(date) {
        const options = { hour: '2-digit', minute: '2-digit', hour12: true };
        return new Date(date).toLocaleTimeString('en-US', options);
    }

</script>

