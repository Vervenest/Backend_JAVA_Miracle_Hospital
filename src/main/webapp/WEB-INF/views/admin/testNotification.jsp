<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ include file="./include/header.jsp" %>
<%@ include file="./include/left_navi.jsp" %>
<div class="main-content">
    <div class="page-content">
        <div class="container-fluid">
            <div class="row">
                <div class="col-12">
                    <div class="page-title-box d-sm-flex align-items-center justify-content-between">
                        <h5 class="mb-sm-0 text-uppercase">Test Notifications</h5>
                        <div class="page-title-right">
                            <ol class="breadcrumb m-0">
                                <li class="breadcrumb-item active text-uppercase">Test Notifications</li>
                            </ol>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-lg-12">
                    <div class="card">
                        <div class="card-body">
                            <h5 class="fw-semibold mb-4">
                                <i class="ri-notification-3-line me-2"></i> Test Push Notification
                            </h5>
                            <div class="row g-3">
                                <div class="col-md-6">
                                    <label class="form-label">Target FCM Token</label>
                                    <textarea id="fcmToken" class="form-control" rows="4"
                                        placeholder="Use a device token from your mobile app (patient or doctor)."></textarea>
                                    <small class="text-muted">Use a device token from your mobile app (patient or doctor).</small>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">Send As</label>
                                    <select id="sendAs" class="form-control">
                                        <option value="patient">Patient (patient creds)</option>
                                        <option value="doctor">Doctor (doctor creds)</option>
                                    </select>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">Title</label>
                                    <input id="notifTitle" type="text" class="form-control" placeholder="Notification title">
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">Body</label>
                                    <input id="notifBody" type="text" class="form-control" placeholder="Notification body">
                                </div>
                                <div class="col-12">
                                    <button id="sendBtn" class="btn btn-primary">Send Test Notification</button>
                                </div>
                                <div class="col-12">
                                    <label class="form-label">Response</label>
                                    <textarea id="responseArea" class="form-control bg-light" rows="5" readonly></textarea>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
$('#sendBtn').on('click', function () {
    var token = $('#fcmToken').val().trim();
    var title = $('#notifTitle').val().trim();
    var body  = $('#notifBody').val().trim();
    var sendAs = $('#sendAs').val();

    if (!token || !title || !body) {
        Swal.fire('Missing Fields', 'Please fill in FCM token, title and body.', 'warning');
        return;
    }

    $('#sendBtn').attr('disabled', true).text('Sending...');
    $('#responseArea').val('');

    $.ajax({
        type: 'POST',
        url: '${pageContext.request.contextPath}/adminmodel/testNotification',
        data: { fcmToken: token, title: title, body: body, sendAs: sendAs },
        success: function (res) {
            $('#responseArea').val(JSON.stringify(res, null, 2));
            if (res.status === 'success') {
                Swal.fire({ icon: 'success', title: 'Sent!', text: 'Notification sent successfully.', timer: 2000, showConfirmButton: false });
            } else {
                Swal.fire('Failed', res.message || 'Failed to send notification.', 'error');
            }
        },
        error: function () {
            $('#responseArea').val('Server error. Please try again.');
            Swal.fire('Error', 'Server error.', 'error');
        },
        complete: function () {
            $('#sendBtn').removeAttr('disabled').text('Send Test Notification');
        }
    });
});
</script>

<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<%@ include file="./include/footer.jsp" %>
</html>
