<%@ taglib uri="jakarta.tags.core" prefix="c" %>
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
                        <h5 class="mb-sm-0 text-uppercase">Edit Appointment</h5>
                        <div class="page-title-right">
                            <ol class="breadcrumb m-0">
                                <li class="breadcrumb-item text-uppercase"><a style="color:#fff"
                                        href="${pageContext.request.contextPath}/admin/appointment/appointmentlist"
                                        class="btn btn-sm btn-primary"> Back to Appointment List</a></li>
                                <li class="breadcrumb-item text-uppercase"><a href="javascript: void(0);">
                                        Appointment</a></li>
                                <li class="breadcrumb-item active text-uppercase">Edit</li>
                            </ol>
                        </div>
                    </div>
                </div>
            </div>
            <!-- end page title -->

            
            <div class="row">
                <!-- Loader for the Main Page -->
                <div class="page-loader"
                    style="display:none; position:fixed; top:0; left:0; width:100%; height:100%; background:rgba(255, 255, 255, 0.8); text-align:center; z-index:9999;">
                    <img src="${pageContext.request.contextPath}/assets/images/animated_loader_gif.gif" alt="Loading..."
                        style="position:relative; top:50%; transform:translateY(-50%);" />
                </div>
                <div class="col-lg-12">
                    <div class="card">
                        <div class="card-body">
                            
                            <form class="ajax-form" action="${pageContext.request.contextPath}/adminmodel/editappointment" method="post">
                                <div class="row">
                                    <!-- Doctor -->
                                    <div class="col-md-4 mb-3">
                                        <label class="form-label">Doctor Name</label>
                                        <select class="form-control" name="doctorId" required>
                                            <option value="">Select</option>
                                            
                                                    <option 
                                                        value="">${doctorName}</option>
                                                
                                        </select>
                                    </div>

                                    <!-- Contact -->
                                    <div class="col-md-3 mb-3">
                                        <label class="form-label">Contact</label>
                                        <input type="number" name="patientPhone" class="form-control" required
                                            placeholder="Enter phone" pattern="[0-9]{10,10}" value="${patientPhone}" >
                                    </div>

                                    <!-- Patient Name (Dropdown or Manual Input) -->
                                    <div class="col-md-4 mb-3">
                                        <label class="form-label">Patient Name</label>
                                        <select class="form-control" name="patientId" id="patientDropdown" required>
                                            <option value="">Select</option>
                                            
                                                    <option value=""  >
                                                        ${patientName}
                                                    </option>
                                                
                                        </select>
                                        <!-- Manual input will be inserted dynamically via JS -->
                                    </div>

                                    
                                    <!-- Gender -->
                                    <div class="col-md-3 mb-3">
                                        <label class="form-label d-block">Gender</label>
                                        <div class="form-check form-check-inline">
                                            <input class="form-check-input" type="radio" name="patientGender" 
                                                value="Male" checked>
                                            <label class="form-check-label">Male</label>
                                        </div>
                                        <div class="form-check form-check-inline">
                                            <input class="form-check-input" type="radio" name="patientGender" 
                                                value="Female">
                                            <label class="form-check-label">Female</label>
                                        </div>
                                    </div>

                                    <!-- Age -->
                                    <div class="col-md-3 mb-3">
                                        <label class="form-label">Age</label>
                                        <input type="number" name="patientAge" class="form-control" required value="${patientAge}"
                                            placeholder="Age">
                                    </div>

                                  
                                    <!-- Appointment Date -->
                                    <div class="col-md-3 mb-3">
                                        <label class="form-label">Appointment Date</label>
                                        <input type="date" name="appointmentStartDate" class="form-control" required value="${appointmentDate}">
                                    </div>

                                    <!-- Appointment Time -->
                                    <div class="col-md-3 mb-3">
                                        <label class="form-label">Appointment Time</label>
                                        <input type="time" name="appointmentTime" class="form-control" required value="${appointmentStartTime}">
                                    </div>

                                    

                                    <!-- Submit -->
                                    <div class="col-md-4" style="margin-top:27px;">
                                        <input type="hidden" name="appointmentId" value="${appointmentId}">
                                        <button type="submit" class="btn btn-outline-warning text-uppercase">
                                            <i class="ri-filter-line me-1 align-bottom"></i> Update
                                        </button>
                                    </div>
                                </div>
                            </form>
                        </div>

                    </div>
                </div>
            </div>

        </div>
    </div>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
   

