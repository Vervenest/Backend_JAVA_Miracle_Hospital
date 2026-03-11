<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<style>
/* ── Sidebar divider lines between items ── */
#navbar-nav .nav-divider {
    border: none;
    border-top: 1px solid rgba(255,255,255,0.10);
    margin: 3px 16px;
    pointer-events: none;
}

/* ── Icon-only collapsed state ── */
body.sidebar-icon-only .app-menu.navbar-menu {
    width: 60px !important;
    min-width: 60px !important;
    overflow: visible !important;
}
body.sidebar-icon-only .app-menu .navbar-brand-box {
    padding: 6px 0 !important;
    text-align: center !important;
    width: 60px !important;
}
body.sidebar-icon-only .app-menu .navbar-brand-box .logo-lg { display: none !important; }
body.sidebar-icon-only .app-menu .navbar-brand-box .logo-sm {
    display: block !important;
    margin: 0 auto !important;
}
body.sidebar-icon-only .app-menu .navbar-brand-box .logo-sm img {
    height: 32px !important;
    width: auto !important;
}
body.sidebar-icon-only .app-menu #navbar-nav .menu-title { display: none !important; }
body.sidebar-icon-only .app-menu #navbar-nav span[data-key] { display: none !important; }
body.sidebar-icon-only .app-menu #navbar-nav .nav-divider { margin: 2px 6px; }
body.sidebar-icon-only .app-menu #navbar-nav .nav-link {
    padding: 10px 0 !important;
    justify-content: center !important;
    text-align: center !important;
    display: flex !important;
}
body.sidebar-icon-only .app-menu #navbar-nav .nav-link i {
    font-size: 22px !important;
    margin: 0 !important;
    display: block !important;
}
body.sidebar-icon-only .main-content { margin-left: 60px !important; }
body.sidebar-icon-only #scrollbar .container-fluid { padding: 0 !important; }

/* tooltip on hover in icon-only mode */
body.sidebar-icon-only .app-menu #navbar-nav .nav-item { position: relative; }
body.sidebar-icon-only .app-menu #navbar-nav .nav-item:hover .nav-tooltip { display: block; }
.nav-tooltip {
    display: none;
    position: absolute;
    left: 64px;
    top: 50%;
    transform: translateY(-50%);
    background: #2d3a4a;
    color: #fff;
    padding: 5px 12px;
    border-radius: 6px;
    white-space: nowrap;
    font-size: 13px;
    z-index: 9999;
    pointer-events: none;
    box-shadow: 0 2px 8px rgba(0,0,0,0.25);
}
.nav-tooltip::before {
    content: '';
    position: absolute;
    left: -5px;
    top: 50%;
    transform: translateY(-50%);
    border: 5px solid transparent;
    border-right-color: #2d3a4a;
    border-left: none;
}

/* ── Clean header title ── */
.header-app-title {
    display: flex;
    align-items: center;
    font-size: 16px;
    font-weight: 600;
    color: #333;
    margin-left: 4px;
    white-space: nowrap;
}

/* ── Hamburger button styling ── */
#topnav-hamburger-icon {
    display: flex;
    align-items: center;
    justify-content: center;
    background: none;
    border: none;
    padding: 0 12px;
    cursor: pointer;
    color: #555;
    font-size: 20px;
    transition: color 0.2s;
}
#topnav-hamburger-icon:hover {
    color: #1a1a2e;
}
</style>

<!-- Begin page -->
<div id="layout-wrapper">

    <header id="page-topbar">
        <div class="layout-width">
            <div class="navbar-header">
                <div class="d-flex align-items-center">

                    <!-- Sidebar logo area (hidden on horizontal layouts) -->
                    <div class="navbar-brand-box horizontal-logo">
                        <a href="" class="logo logo-dark">
                            <span class="logo-sm"><img src="${pageContext.request.contextPath}/assets/images/miracle-logo-fav.jpeg" alt="logo" height="36"></span>
                            <span class="logo-lg"><img src="${pageContext.request.contextPath}/assets/images/miracle-logo.jpeg" alt="logo" height="36"></span>
                        </a>
                        <a href="" class="logo logo-light mt-2 mb-2">
                            <span class="logo-sm"><img src="${pageContext.request.contextPath}/assets/images/miracle-logo-fav.jpeg" alt="logo" height="36"></span>
                            <span class="logo-lg"><img src="${pageContext.request.contextPath}/assets/images/miracle-logo.jpeg" alt="logo" height="36"></span>
                        </a>
                    </div>

                    <!-- Hamburger toggle -->
                    <button type="button"
                            id="topnav-hamburger-icon"
                            onclick="toggleSidebar()"
                            title="Toggle sidebar">
                        <i id="hamburger-icon-i" class="ri-menu-line"></i>
                    </button>

                    <!-- Page title -->
                    <span class="header-app-title">Miracle Hospital</span>

                </div>

                <div class="d-flex align-items-center">
                    <div class="dropdown ms-sm-3 header-item topbar-user">
                        <button type="button" class="btn d-flex align-items-center gap-2 px-3"
                                id="page-header-user-dropdown"
                                data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false"
                                style="font-weight:600; font-size:14px; color:#333;">
                            <i class="ri-user-line fs-18 text-muted"></i>
                            <span>${user_name}</span>
                        </button>
                        <div class="dropdown-menu dropdown-menu-end">
                            <h6 class="dropdown-header">Welcome ${user_name}!</h6>
                            <div class="dropdown-divider"></div>
                            <a class="dropdown-item" href="${pageContext.request.contextPath}/admin/logout">
                                <i class="mdi mdi-logout text-muted fs-16 align-middle me-1"></i>
                                <span class="align-middle" data-key="t-logout">Logout</span>
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </header>

    <!-- ========== App Menu ========== -->
    <div class="app-menu navbar-menu">
        <div class="navbar-brand-box">
            <a href="" class="logo logo-light mt-2 mb-2">
                <span class="logo-sm">
                    <img src="${pageContext.request.contextPath}/assets/images/miracle-logo-fav.jpeg" alt="logo" height="48">
                </span>
                <span class="logo-lg">
                    <img src="${pageContext.request.contextPath}/assets/images/miracle-logo.jpeg" alt="logo" height="48">
                </span>
            </a>
            <button type="button" class="btn btn-sm p-0 fs-20 header-item float-end btn-vertical-sm-hover" id="vertical-hover">
                <i class="ri-record-circle-line"></i>
            </button>
        </div>

        <div id="scrollbar">
            <div class="container-fluid">
                <div id="two-column-menu"></div>

                <ul class="navbar-nav" id="navbar-nav">
                    <li class="menu-title"><span data-key="t-menu">Menu</span></li>

                    <!-- Dashboard -->
                    <li class="nav-item">
                        <a class="nav-link menu-link" href="${pageContext.request.contextPath}/admin/dashboard">
                            <i class="ri-dashboard-line"></i>
                            <span data-key="t-widgets">Dashboard</span>
                        </a>
                        <span class="nav-tooltip">Dashboard</span>
                    </li>

                    <li class="nav-divider"></li>

                    <!-- Appointments -->
                    <li class="nav-item">
                        <a class="nav-link menu-link" href="${pageContext.request.contextPath}/admin/appointment/appointmentlist">
                            <i class="ri-calendar-check-line"></i>
                            <span data-key="t-widgets">Manage Appointments</span>
                        </a>
                        <span class="nav-tooltip">Manage Appointments</span>
                    </li>

                    <li class="nav-divider"></li>

                    <!-- Doctors -->
                    <li class="nav-item">
                        <a class="nav-link menu-link" href="${pageContext.request.contextPath}/admin/doctor/doctorlist">
                            <i class="ri-stethoscope-line"></i>
                            <span data-key="t-widgets">Manage Doctors</span>
                        </a>
                        <span class="nav-tooltip">Manage Doctors</span>
                    </li>

                    <li class="nav-divider"></li>

                    <!-- Patients -->
                    <li class="nav-item">
                        <a class="nav-link menu-link" href="${pageContext.request.contextPath}/admin/patient/patientlist">
                            <i class="ri-user-heart-line"></i>
                            <span data-key="t-widgets">Manage Patients</span>
                        </a>
                        <span class="nav-tooltip">Manage Patients</span>
                    </li>

                    <li class="nav-divider"></li>

                    <!-- Specialisations -->
                    <li class="nav-item">
                        <a class="nav-link menu-link" href="${pageContext.request.contextPath}/admin/doctor/specialisationlist">
                            <i class="ri-heart-pulse-line"></i>
                            <span data-key="t-widgets">Manage Specialisations</span>
                        </a>
                        <span class="nav-tooltip">Manage Specialisations</span>
                    </li>

                    <li class="nav-divider"></li>

                    <!-- Locations -->
                    <li class="nav-item">
                        <a class="nav-link menu-link" href="${pageContext.request.contextPath}/admin/locations/locationslist">
                            <i class="ri-map-pin-line"></i>
                            <span data-key="t-widgets">Manage Locations</span>
                        </a>
                        <span class="nav-tooltip">Manage Locations</span>
                    </li>

                    <li class="nav-divider"></li>

                    <!-- Whatsapp Logs -->
                    <li class="nav-item">
                        <a class="nav-link menu-link" href="${pageContext.request.contextPath}/admin/whatsappLogs">
                            <i class="ri-whatsapp-line"></i>
                            <span data-key="t-widgets">Whatsapp Logs</span>
                        </a>
                        <span class="nav-tooltip">Whatsapp Logs</span>
                    </li>

                    <li class="nav-divider"></li>

                    <!-- App Config -->
                    <li class="nav-item">
                        <a class="nav-link menu-link" href="${pageContext.request.contextPath}/admin/appconfig">
                            <i class="ri-settings-3-line"></i>
                            <span data-key="t-widgets">App Config</span>
                        </a>
                        <span class="nav-tooltip">App Config</span>
                    </li>

                </ul>
            </div>
        </div>

        <div class="sidebar-background"></div>
    </div>
    <!-- Left Sidebar End -->
    <div class="vertical-overlay" onclick="closeSidebarMobile()"></div>

<script>
function toggleSidebar() {
    var body = document.body;
    var icon = document.getElementById('hamburger-icon-i');
    body.classList.toggle('sidebar-icon-only');
    var collapsed = body.classList.contains('sidebar-icon-only');
    // Switch icon: arrow when collapsed, menu when expanded
    icon.className = collapsed ? 'ri-arrow-right-line' : 'ri-menu-line';
    // Persist across page loads
    localStorage.setItem('sidebarCollapsed', collapsed ? '1' : '0');
}

function closeSidebarMobile() {
    document.documentElement.classList.remove('sidebar-enable');
}

// Restore sidebar state on load
(function() {
    var icon = document.getElementById('hamburger-icon-i');
    if (localStorage.getItem('sidebarCollapsed') === '1') {
        document.body.classList.add('sidebar-icon-only');
        if (icon) icon.className = 'ri-arrow-right-line';
    } else {
        if (icon) icon.className = 'ri-menu-line';
    }
})();
</script>
