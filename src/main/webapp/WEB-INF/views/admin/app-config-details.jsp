<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ include file="include/header.jsp" %>
<%@ include file="include/left_navi.jsp" %>

<div class="main-content">
    <div class="page-content">
        <div class="container-fluid">

            <!-- Page Title -->
            <div class="row mb-3">
                <div class="col-12 d-flex justify-content-between align-items-center">
                    <h5 class="mb-0 text-uppercase">APP CONFIG DETAILS</h5>
                    <a href="${pageContext.request.contextPath}/admin/appconfig" class="btn btn-secondary btn-sm">Back</a>
                </div>
            </div>

            <!-- Versions Table -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="card">
                        <div class="card-header d-flex justify-content-between align-items-center">
                            <h5 class="card-title mb-0">&#x2699; App Versions</h5>
                            <button class="btn btn-primary btn-sm" data-bs-toggle="modal" data-bs-target="#addVersionModal">
                                Add Version
                            </button>
                        </div>
                        <div class="card-body">
                            <div class="table-responsive">
                                <table id="versionTable" class="table table-bordered align-middle">
                                    <thead class="table-dark text-uppercase">
                                        <tr>
                                            <th>#</th>
                                            <th>Version No</th>
                                            <th>Version Code</th>
                                            <th>Status</th>
                                            <th>Title</th>
                                            <th>Message</th>
                                            <th>Redirect URL</th>
                                            <th>Created</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="ver" items="${versionList}" varStatus="st">
                                            <tr>
                                                <td>${st.count}</td>
                                                <td>${ver.versionNo != null ? ver.versionNo : '1.0.'.concat(st.count.toString())}</td>
                                                <td>${ver.versionCode}</td>
                                                <td>${ver.appCurrentStatus}</td>
                                                <td>${ver.title}</td>
                                                <td>${ver.message}</td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${not empty ver.redirectUrl}">
                                                            <a href="${ver.redirectUrl}" target="_blank">Open</a>
                                                        </c:when>
                                                        <c:otherwise>-</c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>${ver.createdAt}</td>
                                                <td>
                                                    <span style="cursor:pointer; font-size:20px; color:#0ab39c;"
                                                        onclick="editVersion('${ver.detailId}','${ver.versionCode}','${ver.appCurrentStatus}','${ver.title}','${ver.message}','${ver.redirectUrl}')"
                                                        data-bs-toggle="modal" data-bs-target="#editVersionModal"
                                                        title="Edit">&#9679;</span>
                                                    <span style="cursor:pointer; font-size:20px; color:#f06548;"
                                                        onclick="deleteVersion('${ver.detailId}')" title="Delete">&#9679;</span>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        <c:if test="${empty versionList}">
                                            <tr><td colspan="9" class="text-center text-muted py-4">No versions added yet</td></tr>
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

<!-- ADD VERSION MODAL -->
<div class="modal fade" id="addVersionModal" tabindex="-1">
    <div class="modal-dialog modal-lg modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header bg-primary text-white">
                <h5 class="modal-title">Add App Version</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body row g-3">
                <input type="hidden" id="add_appId" value="${appConfig.appId}">
                <div class="col-md-6">
                    <label class="form-label">Version No <span class="text-danger">*</span></label>
                    <input type="text" id="add_versionNo" class="form-control" placeholder="e.g. 1.0.0" required>
                </div>
                <div class="col-md-6">
                    <label class="form-label">Version Code <span class="text-danger">*</span></label>
                    <input type="number" id="add_versionCode" class="form-control" placeholder="e.g. 1" required>
                </div>
                <div class="col-md-6">
                    <label class="form-label">Status <span class="text-danger">*</span></label>
                    <select id="add_appStatus" class="form-select">
                        <option value="MANDATORY">MANDATORY</option>
                        <option value="ACTIVE">ACTIVE</option>
                        <option value="FORCED_UPDATE">FORCED_UPDATE</option>
                        <option value="DEPRECATED">DEPRECATED</option>
                        <option value="MAINTENANCE">MAINTENANCE</option>
                    </select>
                </div>
                <div class="col-md-6">
                    <label class="form-label">Title <span class="text-danger">*</span></label>
                    <input type="text" id="add_ver_title" class="form-control" placeholder="e.g. New Update Available" required>
                </div>
                <div class="col-md-12">
                    <label class="form-label">Message</label>
                    <textarea id="add_ver_message" class="form-control" rows="3" placeholder="Message shown to user"></textarea>
                </div>
                <div class="col-md-12">
                    <label class="form-label">Redirect URL</label>
                    <input type="url" id="add_redirectUrl" class="form-control" placeholder="https://play.google.com/...">
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                <button class="btn btn-primary" id="addVersionBtn" onclick="addVersion()">Save</button>
            </div>
        </div>
    </div>
</div>

<!-- EDIT VERSION MODAL -->
<div class="modal fade" id="editVersionModal" tabindex="-1">
    <div class="modal-dialog modal-lg modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header bg-success text-white">
                <h5 class="modal-title">Edit App Version</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body row g-3">
                <input type="hidden" id="edit_detailId">
                <div class="col-md-6">
                    <label class="form-label">Version Code</label>
                    <input type="number" id="edit_versionCode" class="form-control" required>
                </div>
                <div class="col-md-6">
                    <label class="form-label">Status</label>
                    <select id="edit_appStatus" class="form-select">
                        <option value="MANDATORY">MANDATORY</option>
                        <option value="ACTIVE">ACTIVE</option>
                        <option value="FORCED_UPDATE">FORCED_UPDATE</option>
                        <option value="DEPRECATED">DEPRECATED</option>
                        <option value="MAINTENANCE">MAINTENANCE</option>
                    </select>
                </div>
                <div class="col-md-12">
                    <label class="form-label">Title</label>
                    <input type="text" id="edit_ver_title" class="form-control" required>
                </div>
                <div class="col-md-12">
                    <label class="form-label">Message</label>
                    <textarea id="edit_ver_message" class="form-control" rows="3"></textarea>
                </div>
                <div class="col-md-12">
                    <label class="form-label">Redirect URL</label>
                    <input type="url" id="edit_redirectUrl" class="form-control">
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                <button class="btn btn-success" id="editVersionBtn" onclick="updateVersion()">Update</button>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script>
$(document).ready(function() {
    if ($.fn.DataTable) {
        $('#versionTable').DataTable({ pageLength: 10, order: [[1, 'desc']] });
    }
});

function editVersion(detailId, versionCode, status, title, message, redirectUrl) {
    $('#edit_detailId').val(detailId);
    $('#edit_versionCode').val(versionCode);
    $('#edit_appStatus').val(status);
    $('#edit_ver_title').val(title);
    $('#edit_ver_message').val(message);
    $('#edit_redirectUrl').val(redirectUrl);
}

function addVersion() {
    var appId       = $('#add_appId').val();
    var versionNo   = $('#add_versionNo').val().trim();
    var versionCode = $('#add_versionCode').val();
    var status      = $('#add_appStatus').val();
    var title       = $('#add_ver_title').val().trim();
    var message     = $('#add_ver_message').val().trim();
    var redirectUrl = $('#add_redirectUrl').val().trim();

    if (!versionCode || !title) {
        Swal.fire('Validation', 'Version Code and Title are required.', 'warning');
        return;
    }

    $('#addVersionBtn').prop('disabled', true).html('<span class="spinner-border spinner-border-sm"></span> Saving...');

    $.ajax({
        type: 'POST',
        url: '${pageContext.request.contextPath}/adminmodel/addAppConfigVersion',
        data: { appId: appId, versionNo: versionNo, versionCode: versionCode, appCurrentStatus: status, title: title, message: message, redirectUrl: redirectUrl },
        success: function(data) {
            $('#addVersionBtn').prop('disabled', false).html('Save');
            var json = typeof data === 'string' ? JSON.parse(data) : data;
            if (json.status === 'SUCCESS') {
                $('#addVersionModal').modal('hide');
                Swal.fire({ icon: 'success', title: 'Added!', timer: 1800, showConfirmButton: false })
                    .then(() => location.reload());
            } else {
                Swal.fire('Error', json.message || 'Failed.', 'error');
            }
        },
        error: function() {
            $('#addVersionBtn').prop('disabled', false).html('Save');
            Swal.fire('Error', 'Server error.', 'error');
        }
    });
}

function updateVersion() {
    var detailId    = $('#edit_detailId').val();
    var versionCode = $('#edit_versionCode').val();
    var status      = $('#edit_appStatus').val();
    var title       = $('#edit_ver_title').val().trim();
    var message     = $('#edit_ver_message').val().trim();
    var redirectUrl = $('#edit_redirectUrl').val().trim();

    if (!title) {
        Swal.fire('Validation', 'Title is required.', 'warning');
        return;
    }

    $('#editVersionBtn').prop('disabled', true).html('<span class="spinner-border spinner-border-sm"></span> Updating...');

    $.ajax({
        type: 'POST',
        url: '${pageContext.request.contextPath}/adminmodel/updateAppConfigVersion',
        data: { detailId: detailId, versionCode: versionCode, appCurrentStatus: status, title: title, message: message, redirectUrl: redirectUrl },
        success: function(data) {
            $('#editVersionBtn').prop('disabled', false).html('Update');
            var json = typeof data === 'string' ? JSON.parse(data) : data;
            if (json.status === 'SUCCESS') {
                $('#editVersionModal').modal('hide');
                Swal.fire({ icon: 'success', title: 'Updated!', timer: 1800, showConfirmButton: false })
                    .then(() => location.reload());
            } else {
                Swal.fire('Error', json.message || 'Failed.', 'error');
            }
        },
        error: function() {
            $('#editVersionBtn').prop('disabled', false).html('Update');
            Swal.fire('Error', 'Server error.', 'error');
        }
    });
}

function deleteVersion(detailId) {
    Swal.fire({
        title: 'Delete Version?',
        text: 'This action cannot be undone.',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        confirmButtonText: 'Delete'
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                type: 'POST',
                url: '${pageContext.request.contextPath}/adminmodel/deleteAppConfigVersion',
                data: { detailId: detailId },
                success: function(data) {
                    var json = typeof data === 'string' ? JSON.parse(data) : data;
                    if (json.status === 'SUCCESS') {
                        Swal.fire({ icon: 'success', title: 'Deleted!', timer: 1500, showConfirmButton: false })
                            .then(() => location.reload());
                    } else {
                        Swal.fire('Error', json.message || 'Failed.', 'error');
                    }
                },
                error: function() { Swal.fire('Error', 'Server error.', 'error'); }
            });
        }
    });
}
</script>

<%@ include file="include/footer.jsp" %>
</html>
