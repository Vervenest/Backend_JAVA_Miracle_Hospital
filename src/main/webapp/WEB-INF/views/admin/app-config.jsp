<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ include file="include/header.jsp" %>
<%@ include file="include/left_navi.jsp" %>

<div class="main-content">
    <div class="page-content">
        <div class="container-fluid">

            <div class="row">
                <div class="col-lg-12">
                    <div class="card">
                        <div class="card-header d-flex justify-content-between align-items-center">
                            <h5 class="card-title mb-0 text-uppercase">&#x2699; App Configurations</h5>
                            <button class="btn btn-primary btn-sm" data-bs-toggle="modal" data-bs-target="#addAppModal">
                                Add App
                            </button>
                        </div>
                        <div class="card-body">
                            <div class="table-responsive">
                                <table id="appConfigTable" class="table table-bordered align-middle">
                                    <thead class="table-dark text-uppercase">
                                        <tr>
                                            <th>#</th>
                                            <th>App ID</th>
                                            <th>Title</th>
                                            <th>Status</th>
                                            <th>Message</th>
                                            <th>Created</th>
                                            <th>Updated</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="app" items="${appList}" varStatus="st">
                                            <tr>
                                                <td>${st.count}</td>
                                                <td>${app.appId}</td>
                                                <td>${app.title}</td>
                                                <td>
                                                    <span class="badge rounded-pill ${app.appStatus == 'ACTIVE' ? 'bg-success' : 'bg-danger'}">
                                                        ${app.appStatus}
                                                    </span>
                                                </td>
                                                <td>${app.message}</td>
                                                <td>${app.createDatetime}</td>
                                                <td>${app.updateDatetime}</td>
                                                <td>
                                                    <!-- Green dot = activate, Red dot = deactivate -->
                                                    <span style="cursor:pointer; font-size:20px; color:#0ab39c;"
                                                        onclick="updateAppStatus('${app.appId}','ACTIVE')" title="Set Active">&#9679;</span>
                                                    <span style="cursor:pointer; font-size:20px; color:#f06548;"
                                                        onclick="updateAppStatus('${app.appId}','STOP')" title="Set Inactive">&#9679;</span>
                                                    <a href="${pageContext.request.contextPath}/admin/appConfigDetails?appId=${app.appId}"
                                                       class="btn btn-primary btn-sm ms-1">View</a>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        <c:if test="${empty appList}">
                                            <tr><td colspan="8" class="text-center text-muted py-4">No apps configured yet</td></tr>
                                        </c:if>
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

<!-- ADD APP MODAL -->
<div class="modal fade" id="addAppModal" tabindex="-1">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header bg-primary text-white">
                <h5 class="modal-title">Add App</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <div class="mb-3">
                    <label class="form-label">App ID <span class="text-danger">*</span></label>
                    <input type="text" id="add_appId" class="form-control" placeholder="e.g. com.vervenest.patient" required>
                </div>
                <div class="mb-3">
                    <label class="form-label">Title <span class="text-danger">*</span></label>
                    <input type="text" id="add_title" class="form-control" placeholder="App title" required>
                </div>
                <div class="mb-3">
                    <label class="form-label">Message</label>
                    <textarea id="add_message" class="form-control" rows="3" placeholder="Status message shown to users"></textarea>
                </div>
                <div class="mb-3">
                    <label class="form-label">Status</label>
                    <select id="add_status" class="form-select">
                        <option value="ACTIVE">ACTIVE</option>
                        <option value="MAINTENANCE">MAINTENANCE</option>
                        <option value="STOP">STOP</option>
                    </select>
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                <button class="btn btn-primary" id="addAppBtn" onclick="addApp()">Save</button>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script>
$(document).ready(function() {
    if ($.fn.DataTable) {
        $('#appConfigTable').DataTable({ pageLength: 10 });
    }
});

function updateAppStatus(appId, status) {
    $.ajax({
        type: 'POST',
        url: '${pageContext.request.contextPath}/adminmodel/updateAppConfig',
        data: { appId: appId, appStatus: status },
        success: function() { location.reload(); },
        error: function() { Swal.fire('Error', 'Failed to update status.', 'error'); }
    });
}

function addApp() {
    var appId   = $('#add_appId').val().trim();
    var title   = $('#add_title').val().trim();
    var message = $('#add_message').val().trim();
    var status  = $('#add_status').val();

    if (!appId || !title) {
        Swal.fire('Validation', 'App ID and Title are required.', 'warning');
        return;
    }

    $('#addAppBtn').prop('disabled', true).html('<span class="spinner-border spinner-border-sm"></span> Saving...');

    $.ajax({
        type: 'POST',
        url: '${pageContext.request.contextPath}/adminmodel/addAppConfig',
        data: { appId: appId, title: title, message: message, appStatus: status },
        success: function(data) {
            $('#addAppBtn').prop('disabled', false).html('Save');
            var json = typeof data === 'string' ? JSON.parse(data) : data;
            if (json.status === 'SUCCESS') {
                $('#addAppModal').modal('hide');
                Swal.fire({ icon: 'success', title: 'Added!', timer: 1800, showConfirmButton: false })
                    .then(() => location.reload());
            } else {
                Swal.fire('Error', json.message || 'Failed to add.', 'error');
            }
        },
        error: function() {
            $('#addAppBtn').prop('disabled', false).html('Save');
            Swal.fire('Error', 'Server error.', 'error');
        }
    });
}
</script>

<%@ include file="include/footer.jsp" %>
</html>
