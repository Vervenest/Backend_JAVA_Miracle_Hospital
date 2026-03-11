<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ include file="include/header.jsp" %>
<%@ include file="include/left_navi.jsp" %>

<div class="main-content">
   <div class="page-content">
        <div class="page-loader" style="display:none; position:fixed; top:0; left:0; width:100%; height:100%; background:rgba(255, 255, 255, 0.8); text-align:center; z-index:9999;">
            <img src="${pageContext.request.contextPath}/assets/images/animated_loader_gif.gif" alt="Loading..." style="position:relative; top:50%; transform:translateY(-50%);" />
        </div>
        <div class="container-fluid">
         <div class="row">
            <div class="col-12">
               <div class="page-title-box d-sm-flex align-items-center justify-content-between">
                  <h4 class="mb-sm-0">Dashboard</h4>
                  <div class="page-title-right">
                     <ol class="breadcrumb m-0">
                        <li class="breadcrumb-item"><a href="javascript: void(0);">Dashboards</a></li>
                     </ol>
                  </div>
               </div>
            </div>
         </div>
         <div class="row">
            <div class="col-xxl-12">
                            <div class="d-flex flex-column h-100">
                               
                                

<div class="row">
    <!-- Total Doctors -->
    <div class="col-md-6 col-xl-3">
        <div class="card card-animate">
            <div class="card-body">
                <div class="d-flex justify-content-between">
                    <div>
                        <p class="fw-medium text-muted mb-0">Total Doctors</p>
                        <h2 class="mt-4 ff-secondary fw-semibold">
                            <span class="counter-value" data-target="${doctors}"></span>
                        </h2>
                    </div>
                    <div>
                        <div class="avatar-sm flex-shrink-0">
                            <span class="avatar-title bg-primary-subtle rounded-circle fs-2">
                                <i class="ri-stethoscope-line"></i>
                            </span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Total Patients -->
    <div class="col-md-6 col-xl-3">
        <div class="card card-animate">
            <div class="card-body">
                <div class="d-flex justify-content-between">
                    <div>
                        <p class="fw-medium text-muted mb-0">Total Patients</p>
                        <h2 class="mt-4 ff-secondary fw-semibold">
                            <span class="counter-value" data-target="${patients}"></span>
                        </h2>
                    </div>
                    <div>
                        <div class="avatar-sm flex-shrink-0">
                            <span class="avatar-title bg-success-subtle rounded-circle fs-2">
                                <i class="ri-user-heart-line"></i> w
                            </span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Total Appointments -->
    <div class="col-md-6 col-xl-3">
        <div class="card card-animate">
            <div class="card-body">
                <div class="d-flex justify-content-between">
                    <div>
                        <p class="fw-medium text-muted mb-0">Total Appointments</p>
                        <h2 class="mt-4 ff-secondary fw-semibold">
                            <span class="counter-value" data-target="${appointments}"></span>
                        </h2>
                    </div>
                    <div>
                        <div class="avatar-sm flex-shrink-0">
                            <span class="avatar-title bg-warning-subtle rounded-circle fs-2">
                                <i class="ri-calendar-check-line "></i>
                            </span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Total Specialisations -->
<div class="col-md-6 col-xl-3">
    <div class="card card-animate">
        <div class="card-body">
            <div class="d-flex justify-content-between">
                <div>
                    <p class="fw-medium text-muted mb-0">Total Specialisations</p>
                    <h2 class="mt-4 ff-secondary fw-semibold">
                        <span class="counter-value" data-target="${totalSpecialisations}">${totalSpecialisations}</span>
                    </h2>
                </div>
                <div>
                    <div class="avatar-sm flex-shrink-0">
                        <span class="avatar-title bg-danger-subtle rounded-circle fs-2">
                            <i class="ri-heart-pulse-line "></i>
                        </span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


    <!-- Today's Appointments -->
    <div class="col-md-6 col-xl-3">
        <div class="card card-animate">
            <div class="card-body">
                <div class="d-flex justify-content-between">
                    <div>
                        <p class="fw-medium text-muted mb-0">Todays Appointments</p>
                        <h2 class="mt-4 ff-secondary fw-semibold">
                            <span class="counter-value" data-target="${todaysAppointments}"></span>
                        </h2>
                    </div>
                    <div>
                        <div class="avatar-sm flex-shrink-0">
                            <span class="avatar-title bg-info-subtle rounded-circle fs-2">
                                <i class="ri-time-line "></i>
                            </span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


                            </div>
                        </div>
           
         </div>
         
      </div>
   </div>
</div>

<%@ include file="include/footer.jsp" %>
</html>
