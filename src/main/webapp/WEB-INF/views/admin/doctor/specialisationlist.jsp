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
        table-layout: auto;
        width: auto;
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
        <div class="page-loader" style="display:none; position:fixed; top:0; left:0; width:100%; height:100%; background:rgba(255, 255, 255, 0.8); text-align:center; z-index:9999;">
            <img src="${pageContext.request.contextPath}/assets/images/animated_loader_gif.gif" alt="Loading..." style="position:relative; top:50%; transform:translateY(-50%);" />
        </div>
        <div class="container-fluid">
            <!-- start page title -->
            <div class="row">
                <div class="col-12">
                    <c:if test="${not empty dbError}">
                        <div class="alert alert-danger">Database error: ${dbError}</div>
                    </c:if>
                    <div class="page-title-box d-sm-flex align-items-center justify-content-between">
                        <h5 class="mb-sm-0 text-uppercase">Specialisation</h5>
                        <span class="text-muted ms-3">Total: ${fn:length(specialisationList)}</span>
                        <form method="post" class="employeelistform" action="${pageContext.request.contextPath}/">
                            <input type="hidden" name="employeelist" class="employeelist" value="active" />
                        </form>
                        <div class="page-title-right">
                            <ol class="breadcrumb m-0">
                                <li class="breadcrumb-item text-uppercase"><a href="javascript: void(0);">Specialisation</a></li>
                                <li class="breadcrumb-item active text-uppercase">List</li>
                            </ol>
                        </div>
                    </div>
                </div>
            </div>
            <!-- end page title -->
            <!-- <div class="row">
            <div class="offset-md-9 col-md-3">
                <a href="${pageContext.request.contextPath}/recruitmentPortal/placement/addPlacement">
                    <button type="button" class="btn btn-primary btn-label rounded-pill">
                        <i class="ri-add-circle-line label-icon align-middle rounded-pill fs-16 me-2"></i> Onboard placement
                    </button>
                </a>
            </div>
            </div> -->

            <div class="row">
                <div class="col-lg-12">
                    <div class="card">
                        <div class="card-header">
                            <div class="row">
                                <div class="col-md-6">
                                    <h5 class="card-title mb-0 text-uppercase">Specialisation List</h5>
                                </div>
                                <div class="col-md-6">
                                    <div class="float-end">

                                        <button class="btn btn-outline-secondary btn-sm text-uppercase" type="button" data-bs-toggle="offcanvas" data-bs-target="#add-specialisation-form-modal" aria-controls="add-specialisation-form-modal">+ Add Specialisation</button>

                                    </div>
                                </div>
                            </div>


                        </div>
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
                                                            <td>Specialisation Name</td>
                                                            <td>Status</td>
                                                            <td>Action</td>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <c:if test="${not empty specialisationList}">
                                                            <c:forEach var="specialisation" items="${specialisationList}" varStatus="status">
                                                        <tr>
                                                            <td>${status.count}</td>
                                                            <td>${specialisation.specialisationName}</td>
                                                            <td>
                                                                <c:if test="${specialisation.specialisationStatus == '1'}">
                                                                <a href="${pageContext.request.contextPath}/adminmodel/updateSpecialisationStatus/0/${specialisation.specialisationId}">
                                                                    <span class="badge rounded-pill bg-success">Active</span>
                                                                </a>
                                                                </c:if>
                                                                <c:if test="${specialisation.specialisationStatus == '0'}">
                                                                <a href="${pageContext.request.contextPath}/adminmodel/updateSpecialisationStatus/1/${specialisation.specialisationId}">
                                                                    <span class="badge rounded-pill bg-danger">In-Active</span>
                                                                </a>
                                                                </c:if>
                                                            </td>
                                                            <td>
                                                                <a href="${pageContext.request.contextPath}/adminmodel/updateSpecialisationStatus/2/${specialisation.specialisationId}">
                                                                    <i class="ri-delete-bin-line"></i>
                                                                </a>
                                                            </td>
                                                        </tr>
                                                            </c:forEach>
                                                        </c:if>
                                                        <c:if test="${empty specialisationList}">
                                                            <tr><td colspan="4" class="text-center">No specialisations found</td></tr>
                                                        </c:if>
                                                </table>

                                            </div>
                                        </div>
                                    </div>
                                </div>

                            </div>


                        </div>

                    </div>
                </div>
                <!-- end row -->
            </div>
        </div>
    </div>



<!-- add-specialisation-form-modal -->
<div class="offcanvas offcanvas-end border-0" tabindex="-1" id="add-specialisation-form-modal" style="width: 600px;">
    <div class="d-flex align-items-center bg-primary bg-gradient p-3 offcanvas-header">
        <h5 class="m-0 me-2 text-white">Add Specialisation</h5>

        <button type="button" class="btn-close btn-close-white ms-auto" id="add-specialisation-form-modal-close-btn" data-bs-dismiss="offcanvas" aria-label="Close"></button>
    </div>
    <div class="offcanvas-body p-0">
        <div data-simplebar class="h-100">
            <div class="p-4">

                <form method="post" id="addSpecialisationForm" enctype="multipart/form-data">
                    <div class="row card-body row-sm errMsgDiv">
                        <div class="col-md-12" style="margin-bottom: 0;">
                            <p class="errMsg"></p>
                        </div>
                    </div>
                    <div class="row card-body row-sm">
                        <!-- <hr> -->
                        
                        <div class="col-md-6">
                            <label class="form-label"> Specialisation Name : <span class="text-danger">*</span></label>
                            <input id="specialisationName" class="form-control" name="specialisationName" placeholder="Enter specialisation" type="text" pattern="[A-Za-z\s]{2,100}" required>
                        </div>

                        <div class="offset-md-6 col-md-6">
                            <div class="hstack gap-2 justify-content-end">
                                <button type="submit" id="addSpecialisationSubmitBtn" class="btn btn-primary">Add</button>
                            </div>
                        </div>
                    </div><!-- row -->
                </form>

            </div>

        </div>

    </div>

</div>



<script>

    $(document).ready(function() {

        // Function to validate the form
        function validateForm() {
            var isValid = true;
            $('#addSpecialisationForm input[required], #addSpecialisationForm select[required]').each(function() {
                if ($.trim($(this).val()) === '') {
                    isValid = false;
                    $(this).addClass('is-invalid');
                } else {
                    $(this).removeClass('is-invalid');
                }
            });
            return isValid;
        }

        $("#addSpecialisationForm").on("submit", function(e) {
            e.preventDefault();
            $("#addSpecialisationSubmitBtn").attr("disabled", true);
            if (!validateForm()) return; // Stop submission if form is not valid
            var formData = new FormData(this);
            $.ajax({
                type: "POST",
                url: "${pageContext.request.contextPath}/adminmodel/addspecialisation",
                data: formData,
                cache: false,
                contentType: false,
                processData: false,
                beforeSend: function() {
                    console.log('Sending addSpecialisation request');
                    $('.page-loader').show();
                },
                success: function(data) {
                    console.log('addSpecialisation response', data);
                    var json;
                    if (typeof data === 'string') {
                        try {
                            json = JSON.parse(data);
                        } catch (ex) {
                            console.error('Error parsing string response', ex, data);
                            $('.errMsg').text('*Invalid server response. See console for details').css('color','red').show();
                            return;
                        }
                    } else {
                        json = data; // already an object
                    }
                    if (json.status == 'EXIST') {
                        Swal.fire('Already Exists', json.message, 'warning');
                    } else if (json.status == 'SUCCESS') {
                        $('#add-specialisation-form-modal-close-btn').trigger("click");
                        Swal.fire({ icon: 'success', title: 'Added!', text: 'Specialisation added successfully.', timer: 2000, showConfirmButton: false })
                            .then(() => location.reload());
                    } else if (json.status == 'FAIL') {
                        Swal.fire('Error', 'Something went wrong. Please try again.', 'error');
                    } else {
                        $('.errMsg').text('*Unexpected response from server').css('color','red').show();
                    }
                },
                error: function(xhr, status, error) {
                    console.error('Ajax error', status, error, xhr);
                    $('.errMsg').text('*Error occurred while processing the request. Please try again!!!').css('color', 'red').show();
                },
                complete: function() {
                    $('.page-loader').hide();
                    $("#addSpecialisationSubmitBtn").removeAttr("disabled");
                }
            });
        });

    });
</script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script>
function confirmAction(msg) {
    // Will be overridden by async version below - this is sync fallback
    return confirm(msg);
}
</script>
<%@ include file="../include/footer.jsp" %>
</html>