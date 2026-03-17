<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ include file="../include/header.jsp" %>
<%@ include file="../include/left_navi.jsp" %>
<style>
#metricTabs .nav-link {
    color: #555;
    background-color: #f8f9fa;
    border: 1px solid #dee2e6;
    border-bottom: none;
    margin-right: 3px;
}
#metricTabs .nav-link.active {
    color: #ffffff !important;
    background-color: #405189 !important;
    border-color: #405189 !important;
    font-weight: 600;
}
#metricTabs .nav-link:hover:not(.active) {
    color: #405189;
    background-color: #e9ecef;
}
</style>
<div class="main-content">      
    <div class="page-content">
        <div class="container-fluid">
            <div class="row">
                <div class="col-12">
                    <div class="page-title-box d-sm-flex align-items-center justify-content-between">
                        <h5 class="mb-sm-0 text-uppercase">Key Metrics - Daily Entry</h5>
                        <div class="page-title-right">
                            <ol class="breadcrumb m-0">
                                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/admin/keymetrics/reports">Reports</a></li>
                                <li class="breadcrumb-item active text-uppercase">Daily Entry</li>
                            </ol>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Date Picker -->
            <div class="row mb-3">
                <div class="col-md-4">
                    <div class="card p-3">
                        <label class="form-label fw-semibold">Select Date</label>
                        <input type="date" id="entryDate" class="form-control" value="${today}">
                    </div>
                </div>
            </div>

            <!-- Tabs -->
            <ul class="nav nav-tabs mb-0" id="metricTabs">
                <li class="nav-item">
                    <a class="nav-link active" data-bs-toggle="tab" href="#tab-operational">
                        <i class="ri-hospital-line me-1"></i> Operational
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" data-bs-toggle="tab" href="#tab-marketing">
                        <i class="ri-bar-chart-line me-1"></i> Marketing
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" data-bs-toggle="tab" href="#tab-finance">
                        <i class="ri-money-dollar-circle-line me-1"></i> Finance
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" data-bs-toggle="tab" href="#tab-statutory">
                        <i class="ri-file-list-3-line me-1"></i> Statutory Compliance
                    </a>
                </li>
            </ul>

            <div class="tab-content">
                <!-- OPERATIONAL TAB -->
                <div class="tab-pane fade show active" id="tab-operational">
                    <div class="card border-top-0 rounded-0 rounded-bottom p-4">
                        <c:forEach var="cat" items="${operationalCategories}">
                            <h5 class="text-uppercase fw-bold mb-3 mt-2">${cat.categoryName}</h5>
                            <div class="row g-3 mb-4">
                                <c:forEach var="item" items="${cat.items}">
                                    <div class="col-md-6">
                                        <label class="form-label">${item.itemName}</label>
                                        <c:choose>
                                            <c:when test="${item.itemType == 'number'}">
                                                <input type="number" class="form-control metric-input"
                                                    data-item-id="${item.itemId}"
                                                    placeholder="Enter value"
                                                    value="${item.existingValue}">
                                            </c:when>
                                            <c:otherwise>
                                                <textarea class="form-control metric-input"
                                                    data-item-id="${item.itemId}"
                                                    placeholder="Enter details"
                                                    rows="2">${item.existingValue}</textarea>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </c:forEach>
                            </div>
                            <hr>
                        </c:forEach>
                    </div>
                </div>

                <!-- MARKETING TAB -->
                <div class="tab-pane fade" id="tab-marketing">
                    <div class="card border-top-0 rounded-0 rounded-bottom p-4">
                        <c:forEach var="cat" items="${marketingCategories}">
                            <h5 class="text-uppercase fw-bold mb-3 mt-2">${cat.categoryName}</h5>
                            <div class="row g-3 mb-4">
                                <c:forEach var="item" items="${cat.items}">
                                    <div class="col-md-6">
                                        <label class="form-label">${item.itemName}</label>
                                        <c:choose>
                                            <c:when test="${item.itemType == 'number'}">
                                                <input type="number" class="form-control metric-input"
                                                    data-item-id="${item.itemId}"
                                                    placeholder="Enter value"
                                                    value="${item.existingValue}">
                                            </c:when>
                                            <c:otherwise>
                                                <textarea class="form-control metric-input"
                                                    data-item-id="${item.itemId}"
                                                    placeholder="Enter details"
                                                    rows="2">${item.existingValue}</textarea>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:forEach>
                    </div>
                </div>

                <!-- FINANCE TAB -->
                <div class="tab-pane fade" id="tab-finance">
                    <div class="card border-top-0 rounded-0 rounded-bottom p-4">
                        <c:forEach var="cat" items="${financeCategories}">
                            <h5 class="text-uppercase fw-bold mb-3 mt-2">${cat.categoryName}</h5>
                            <div class="row g-3 mb-4">
                                <c:forEach var="item" items="${cat.items}">
                                    <div class="col-md-6">
                                        <label class="form-label">${item.itemName}</label>
                                        <c:choose>
                                            <c:when test="${item.itemType == 'number'}">
                                                <input type="number" class="form-control metric-input"
                                                    data-item-id="${item.itemId}"
                                                    placeholder="Enter value"
                                                    value="${item.existingValue}">
                                            </c:when>
                                            <c:otherwise>
                                                <textarea class="form-control metric-input"
                                                    data-item-id="${item.itemId}"
                                                    placeholder="Enter details"
                                                    rows="2">${item.existingValue}</textarea>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:forEach>
                    </div>
                </div>

                <!-- STATUTORY COMPLIANCE TAB -->
                <div class="tab-pane fade" id="tab-statutory">
                    <div class="card border-top-0 rounded-0 rounded-bottom p-4">
                        <h5 class="text-uppercase fw-bold mb-3">Statutory Compliance Reminders</h5>
                        <table class="table table-bordered">
                            <thead class="table-light">
                                <tr>
                                    <th>COMPLIANCE</th>
                                    <th>FREQUENCY</th>
                                    <th>DUE DATE</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr class="table-secondary"><td colspan="3"><strong>Monthly</strong></td></tr>
                                <c:forEach var="sc" items="${statutoryCompliances}">
                                    <c:if test="${sc.frequency == 'monthly'}">
                                        <tr>
                                            <td>${sc.complianceName}</td>
                                            <td><span class="badge bg-info">Monthly</span></td>
                                            <td>${sc.dueDescription}</td>
                                        </tr>
                                    </c:if>
                                </c:forEach>
                                <tr class="table-secondary"><td colspan="3"><strong>Quarterly</strong></td></tr>
                                <c:forEach var="sc" items="${statutoryCompliances}">
                                    <c:if test="${sc.frequency == 'quarterly'}">
                                        <tr>
                                            <td>${sc.complianceName}</td>
                                            <td><span class="badge bg-warning text-dark">Quarterly</span></td>
                                            <td>${sc.dueDescription}</td>
                                        </tr>
                                    </c:if>
                                </c:forEach>
                                <tr class="table-secondary"><td colspan="3"><strong>Yearly</strong></td></tr>
                                <c:forEach var="sc" items="${statutoryCompliances}">
                                    <c:if test="${sc.frequency == 'yearly'}">
                                        <tr>
                                            <td>${sc.complianceName}</td>
                                            <td><span class="badge bg-success">Yearly</span></td>
                                            <td>${sc.dueDescription}</td>
                                        </tr>
                                    </c:if>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <!-- Save Button -->
            <div class="d-flex justify-content-end gap-2 mt-3 mb-4">
                <a href="${pageContext.request.contextPath}/admin/keymetrics/reports" class="btn btn-outline-secondary">View Reports</a>
                <button id="saveMetricsBtn" class="btn btn-primary">
                    <i class="ri-save-line me-1"></i> Save Metrics
                </button>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script>
$('#saveMetricsBtn').on('click', function() {
    var date = $('#entryDate').val();
    if (!date) { Swal.fire('Error', 'Please select a date', 'error'); return; }

    var entries = [];
    $('.metric-input').each(function() {
        var val = $(this).val().trim();
        if (val !== '') {
            entries.push({ itemId: $(this).data('item-id'), value: val });
        }
    });

    if (entries.length === 0) { Swal.fire('Warning', 'Please enter at least one value', 'warning'); return; }

    $('#saveMetricsBtn').attr('disabled', true).text('Saving...');

    $.ajax({
        type: 'POST',
        url: '${pageContext.request.contextPath}/adminmodel/saveMetrics',
        contentType: 'application/json',
        data: JSON.stringify({ entryDate: date, entries: entries }),
        success: function(res) {
            if (res.status === 'success') {
                Swal.fire({ icon: 'success', title: 'Saved!', text: 'Metrics saved successfully.', timer: 2000, showConfirmButton: false });
            } else {
                Swal.fire('Error', res.message, 'error');
            }
        },
        error: function() { Swal.fire('Error', 'Server error', 'error'); },
        complete: function() { $('#saveMetricsBtn').removeAttr('disabled').html('<i class="ri-save-line me-1"></i> Save Metrics'); }
    });
});

// Reload entries when date changes
$('#entryDate').on('change', function() {
    window.location.href = '${pageContext.request.contextPath}/admin/keymetrics/entry?date=' + $(this).val();
});
// Fix tab active styling
document.querySelectorAll('#metricTabs .nav-link').forEach(function(tab) {
    tab.addEventListener('click', function() {
        document.querySelectorAll('#metricTabs .nav-link').forEach(function(t) {
            t.classList.remove('active');
        });
        this.classList.add('active');
    });
});
</script>
<%@ include file="../include/footer.jsp" %>
</html>
