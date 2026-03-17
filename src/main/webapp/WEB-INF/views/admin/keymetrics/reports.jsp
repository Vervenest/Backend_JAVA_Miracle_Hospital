<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ include file="../include/header.jsp" %>
<%@ include file="../include/left_navi.jsp" %>
<div class="main-content">
    <div class="page-content">
        <div class="container-fluid">
            <div class="row">
                <div class="col-12">
                    <div class="page-title-box d-sm-flex align-items-center justify-content-between">
                        <h5 class="mb-sm-0 text-uppercase">Key Metrics - Reports</h5>
                        <div class="page-title-right">
                            <ol class="breadcrumb m-0">
                                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/admin/keymetrics/entry">Daily Entry</a></li>
                                <li class="breadcrumb-item active text-uppercase">Reports</li>
                            </ol>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Filter -->
            <div class="card p-3 mb-3">
                <div class="row g-3 align-items-end">
                    <div class="col-md-3">
                        <label class="form-label">From Date</label>
                        <input type="date" id="fromDate" class="form-control" value="${fromDate}">
                    </div>
                    <div class="col-md-3">
                        <label class="form-label">To Date</label>
                        <input type="date" id="toDate" class="form-control" value="${toDate}" max="${today}">
                    </div>
                    <div class="col-md-6 d-flex gap-2">
                        <button id="filterBtn" class="btn btn-primary mt-4">
                            <i class="ri-filter-line me-1"></i> Filter
                        </button>
                        <a href="${pageContext.request.contextPath}/adminmodel/downloadMetricsCsv?fromDate=${fromDate}&toDate=${toDate}"
                           class="btn btn-success mt-4">
                            <i class="ri-download-line me-1"></i> Download CSV
                        </a>
                        <a href="${pageContext.request.contextPath}/admin/keymetrics/entry" class="btn btn-outline-primary mt-4">
                            + New Entry
                        </a>
                    </div>
                </div>
            </div>

            <!-- Summary Cards -->
            <div class="row g-3 mb-3">
                <div class="col-md-3">
                    <div class="card p-3">
                        <small class="text-muted">Total Entries</small>
                        <h4 class="mb-0">${totalEntries}</h4>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card p-3">
                        <small class="text-muted">Days Covered</small>
                        <h4 class="mb-0">${daysCovered}</h4>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card p-3">
                        <small class="text-muted">Date Range</small>
                        <h6 class="mb-0">${fromDate} - ${toDate}</h6>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card p-3">
                        <small class="text-muted">Latest Entry</small>
                        <h6 class="mb-0">${not empty latestEntry ? latestEntry : 'N/A'}</h6>
                    </div>
                </div>
            </div>
            <c:if test="${not empty groupedData}">
    <c:forEach var="dayEntry" items="${groupedData}">
        <div class="card mb-3">
            <div class="card-header d-flex align-items-center justify-content-between">
                <h6 class="mb-0 fw-bold">
                    <i class="ri-calendar-line me-2"></i>
                    ${dayEntry.key}
                    <span class="badge bg-primary ms-2">${dayEntry.value.totalCount} entries</span>
                </h6>
                <a href="${pageContext.request.contextPath}/admin/keymetrics/entry?date=${dayEntry.value.editDate}"
                   class="btn btn-sm btn-outline-secondary">
                    <i class="ri-edit-line me-1"></i> Edit
                </a>
            </div>
            <div class="card-body p-0">
                <c:forEach var="catEntry" items="${dayEntry.value.byCategory}">
                    <div class="border-bottom">
                        <div class="d-flex align-items-center justify-content-between px-3 py-2 bg-light"
                             data-bs-toggle="collapse"
                             data-bs-target="#cat-${dayEntry.value.editDate}-${catEntry.key}"
                             style="cursor:pointer;">
                            <span class="fw-semibold">
                                ${catEntry.key}
                                <span class="badge bg-secondary ms-1">${catEntry.value.size()}</span>
                            </span>
                            <i class="ri-arrow-up-s-line"></i>
                        </div>
                        <div class="collapse show" id="cat-${dayEntry.value.editDate}-${catEntry.key}">
                            <table class="table table-sm table-hover mb-0">
                                <thead class="table-light">
                                    <tr>
                                        <th style="width:50%">Metric</th>
                                        <th style="width:30%">Value</th>
                                        <th style="width:20%">Entered By</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="row" items="${catEntry.value}">
                                        <tr>
                                            <td>${row.itemName}</td>
                                            <td>${not empty row.valueNumeric ? row.valueNumeric : row.valueText}</td>
                                            <td>${row.enteredBy}</td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </c:forEach>
</c:if>

           <!-- Data Table -->
<div class="card p-3">
    <h6 class="text-uppercase fw-bold mb-3">All Entries (Tabular View)</h6>
    <table class="table table-bordered table-hover" id="metricsTable">
        <thead class="table-light text-uppercase small">
            <tr>
                <th>Date</th>
                <th>Department</th>
                <th>Type</th>
                <th>Metric</th>
                <th>Value</th>
                <th>Entered By</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="row" items="${reportData}">
                <tr>
                    <td>${row.entryDate}</td>
                    <td>${row.categoryName}</td>
                    <td>${row.categoryType}</td>
                    <td>${row.itemName}</td>
                    <td>${not empty row.valueNumeric ? row.valueNumeric : row.valueText}</td>
                    <td>${row.enteredBy}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <c:if test="${empty reportData}">
        <div class="text-center py-3">
            <p class="text-muted">No metrics data found for the selected date range.</p>
            <a href="${pageContext.request.contextPath}/admin/keymetrics/entry" class="btn btn-primary">Add Daily Entry</a>
        </div>
    </c:if>
</div>
                          
        </div>
    </div>
</div>
<link rel="stylesheet" href="https://cdn.datatables.net/buttons/2.4.1/css/buttons.bootstrap5.min.css">
<style>
.dt-buttons .btn {
    padding: 2px 8px !important;
    font-size: 12px !important;
    border-radius: 3px !important;
    margin-right: 2px !important;
}
.dataTables_wrapper .dt-buttons {
    margin-bottom: 8px;
}
</style>
<script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/buttons/2.4.1/js/dataTables.buttons.min.js"></script>
<script src="https://cdn.datatables.net/buttons/2.4.1/js/buttons.bootstrap5.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jszip/3.10.1/jszip.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.53/pdfmake.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.53/vfs_fonts.js"></script>
<script src="https://cdn.datatables.net/buttons/2.4.1/js/buttons.html5.min.js"></script>
<script src="https://cdn.datatables.net/buttons/2.4.1/js/buttons.print.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script>
$('#filterBtn').on('click', function() {
    var from = $('#fromDate').val();
    var to = $('#toDate').val();
    window.location.href = '${pageContext.request.contextPath}/admin/keymetrics/reports?fromDate=' + from + '&toDate=' + to;
});

// Initialize DataTable if available
if (typeof $.fn.DataTable !== 'undefined') {
    $('#metricsTable').DataTable({
        dom: 'Bfrtip',
        buttons: ['copy', 'csv', 'excel', 'pdf', 'print']
    });
}
</script>
<%@ include file="../include/footer.jsp" %>
</html>
