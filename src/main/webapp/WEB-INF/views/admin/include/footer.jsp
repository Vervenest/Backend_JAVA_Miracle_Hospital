<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<style>
thead {
    font-weight:bold;
}
</style>
<div id="preloader">
        <div id="status">
            <div class="spinner-border text-primary avatar-sm" role="status">
                <span class="visually-hidden">Loading...</span>
            </div>
        </div>
    </div>
<footer class="footer">

                <div class="container-fluid">
                    <div class="row">
                        <div class="col-sm-6">
                            <script>document.write(new Date().getFullYear())</script> © Vervenest.
                        </div>
                        <div class="col-sm-6">
                            <div class="text-sm-end d-none d-sm-block">
                                Powered by Vervenest
                            </div>
                        </div>
                    </div>
                </div>
            </footer>
        </div>
        <!-- end main content-->

    </div>
    <!-- END layout-wrapper -->

    <script>
//     $("body").on("contextmenu", function(e) {
//         return false;
//     });

//     $(document).keydown(function (event) {
//     if (event.keyCode == 123) { // Prevent F12
//         return false;
//     } else if (event.ctrlKey && event.shiftKey && event.keyCode == 73) { // Prevent Ctrl+Shift+I        
//         return false;
//     }
// });
    </script>


    <!--start back-to-top-->
    <button onclick="topFunction()" class="btn btn-danger btn-icon" id="back-to-top">
        <i class="ri-arrow-up-line"></i>
    </button>
    <!--end back-to-top-->

    <!--preloader-->
    
   

    <!-- JAVASCRIPT -->
    <script src="${pageContext.request.contextPath}/assets/libs/bootstrap/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/libs/simplebar/simplebar.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/libs/node-waves/waves.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/libs/feather-icons/feather.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/pages/plugins/lord-icon-2.1.0.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/plugins.js"></script>

    <!-- calendar min js -->
    <script src="${pageContext.request.contextPath}/assets/libs/fullcalendar/index.global.min.js"></script>

    <!-- Calendar init -->
    <script src="${pageContext.request.contextPath}/assets/js/pages/calendar.init.js"></script>
    
    <!-- Modal Js -->
    <script src="${pageContext.request.contextPath}/assets/js/pages/modal.init.js"></script>
    <!-- prismjs plugin -->
    
    <script src="${pageContext.request.contextPath}/assets/libs/prismjs/prism.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/pages/form-validation.init.js"></script>
    
    <!-- <script src="${pageContext.request.contextPath}/assets/libs/choices.js/public/assets/scripts/choices.min.js"></script> -->
    <script src="${pageContext.request.contextPath}/assets/libs/simplebar/simplebar.min.js"></script>
    <!-- <script src="${pageContext.request.contextPath}/assets/bootstrap-datepicker.min.js"></script>    -->
    
    
    <!--datatable js-->
    <!-- <script src="https://code.jquery.com/jquery-3.6.0.min.js" integrity="sha256-/xUj+3OJU5yExlq6GSYGSHk7tPXikynS7ogEvDej/m4=" crossorigin="anonymous"></script> -->
    <script src="${pageContext.request.contextPath}/assets/js/datatables/jquery.dataTables.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/datatables/dataTables.bootstrap5.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/datatables/dataTables.responsive.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/datatables/dataTables.buttons.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/datatables/buttons.print.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/datatables/buttons.html5.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/datatables/vfs_fonts.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/datatables/pdfmake.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/datatables/jszip.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/pages/datatables.init.js"></script>

    <script src="${pageContext.request.contextPath}/assets/js/datatables/dataTables.fixedColumns.min.js"></script>

    <script src="${pageContext.request.contextPath}/assets/js/jquery-input-mask-phone-number.js"></script>

    <!-- App js -->
    <script src="${pageContext.request.contextPath}/assets/js/app.js"></script>

    <script>
        $(document).ready(function () {
            $('#scroll-horizontal-data').DataTable({
                "ordering":false,
                "paging": false,
                "searching": false,
                "info": false,
                //"scrollY": 400,
                "scrollX": true,
                "scrollCollapse": true,
                "fixedColumns": {
                    left: 3 //Your column number here
                },
            });
        });
    </script>

    <!--preloader-->
   

    <script>
  document.addEventListener('DOMContentLoaded', function() {
    // Show preloader when the page starts loading (before navigation)
    function showPreloader() {
        document.getElementById('preloader').style.display = 'block';
    }

    // Hide preloader after page and DataTable fully loaded
    function hidePreloader() {
        document.getElementById('preloader').style.display = 'none';
    }

    // Attach click event to all links in the navbar, including dropdown items
    document.querySelectorAll('#navbar-nav a.nav-link').forEach(function(link) {
        link.addEventListener('click', function(event) {
            // Check if the link has a data-bs-toggle attribute (which indicates a dropdown toggle)
            if (link.getAttribute('data-bs-toggle') !== 'collapse') {
                console.log('Navbar link clicked');
                showPreloader();
            }
        });
    });

    // Attach the event to dropdown links specifically
    document.querySelectorAll('.menu-dropdown a.nav-link').forEach(function(link) {
        link.addEventListener('click', function() {
            console.log('Dropdown link clicked');
            showPreloader();
        });
    });

    // Listen for the window 'load' event to hide the preloader
    window.addEventListener('load', function() {
        // Check if DataTable exists
        if ($.fn.dataTable.isDataTable('#scroll-horizontal')) {
            // Wait for DataTable to complete loading
            $('#scroll-horizontal').on('init.dt', function() {
                hidePreloader();
            });
            hidePreloader();
        } else {
            // If no DataTable, hide preloader immediately after load
            hidePreloader();
        }
    });

    // Listen for beforeunload event to show preloader before leaving the page
    window.addEventListener('beforeunload', function() {
        console.log('Page is unloading');
        showPreloader();
    });
});

</script>
</body>


</html>
