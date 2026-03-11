<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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
            <div class="row">
                <div class="col-12">
                    <div class="page-title-box d-sm-flex align-items-center justify-content-between">
                        <h5 class="mb-sm-0 text-uppercase">Doctors TimeSlots</h5>
                        <div class="page-title-right">
                            <ol class="breadcrumb m-0">
                                <li class="breadcrumb-item text-uppercase"><a href="javascript: void(0);">Doctors</a></li>
                                <li class="breadcrumb-item active text-uppercase">TimeSlots</li>
                            </ol>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-lg-12">
                    <div class="container-fluid">
                        <div class="card shadow p-4">
                            <h5>Available Timeslots - Doctor <span class="text-danger">(${fn:toUpperCase(doctorName)})</span></h5>
                            <form action="${pageContext.request.contextPath}/adminmodel/saveWeeklySlots" method="post">
                                <input type="hidden" name="doctorId" value="${doctorId}">
                                <div class="table-responsive mt-3">
                                    <table class="table table-bordered align-middle">
                                        <thead class="table-light">
                                            <tr>
                                                <th>Sr. No</th>
                                                <th>Day</th>
                                                <th>Start Time</th>
                                                <th>End Time</th>
                                                <th>Mark as Leave</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="day" items="${days}" varStatus="status">
                                                <c:set var="existing" value="${slots[day]}" />
                                                <c:set var="isLeave" value="${existing != null && existing.isLeave == true}" />
                                                <tr>
                                                    <td>${status.count}</td>
                                                    <td>${day}</td>
                                                    <td>
                                                        <input type="time" class="form-control"
                                                            name="startTime[${day}]"
                                                            value="${existing != null ? existing.startTime : ''}"
                                                            <c:if test="${isLeave}">disabled</c:if>>
                                                    </td>
                                                    <td>
                                                        <input type="time" class="form-control"
                                                            name="endTime[${day}]"
                                                            value="${existing != null ? existing.endTime : ''}"
                                                            <c:if test="${isLeave}">disabled</c:if>>
                                                    </td>
                                                    <td class="text-center">
                                                        <input type="checkbox" name="isLeave[${day}]" value="1"
                                                            <c:if test="${isLeave}">checked</c:if>
                                                            onchange="toggleDayFields(this, '${day}')">
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                                <div class="text-end mt-3">
                                    <button type="submit" class="btn btn-primary px-4">Save Time Slots</button>
                                    <a href="${pageContext.request.contextPath}/admin/doctor/doctorlist" class="btn btn-secondary">Back to Doctors List</a>
                                </div>
                            </form>
                        </div>

                        <script>
                            function toggleDayFields(checkbox, day) {
                                const startInput = document.querySelector(`input[name='startTime[${day}]']`);
                                const endInput   = document.querySelector(`input[name='endTime[${day}]']`);
                                if (checkbox.checked) {
                                    startInput.disabled = true;
                                    endInput.disabled   = true;
                                    startInput.value    = '';
                                    endInput.value      = '';
                                } else {
                                    startInput.disabled = false;
                                    endInput.disabled   = false;
                                }
                            }
                        </script>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script>
    // Show success popup when redirected back with ?saved=1
    const _p = new URLSearchParams(window.location.search);
    if (_p.get('saved') === '1') {
        Swal.fire({ icon: 'success', title: 'Saved!', text: 'Time slots updated successfully.', timer: 2000, showConfirmButton: false });
        history.replaceState({}, '', window.location.pathname);
    }
</script>

<%@ include file="../include/footer.jsp" %>
</html>
