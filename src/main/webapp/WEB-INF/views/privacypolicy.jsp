<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<html lang="en" data-layout="vertical" data-topbar="light" data-sidebar="dark" data-sidebar-size="lg" data-sidebar-image="none" data-preloader="disable">


<head>

    <meta charset="utf-8" />
    <title>Miracle Hospital</title>
    <meta name="google-signin-client_id" content="439417909033-317e45ip3736ek1hap8detj2hpo3q6dq.apps.googleusercontent.com">

    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta content="Sign In | Business Management Tool" name="description" />
    <meta content="Themesbrand" name="author" />
    <!-- App favicon -->
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/assets/images/fav-icon.png">

    <!-- Layout config Js -->
    <script src="${pageContext.request.contextPath}/assets/js/layout.js"></script>
    <!-- Bootstrap Css -->
    <link href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
    <!-- Icons Css -->
    <link href="${pageContext.request.contextPath}/assets/css/icons.min.css" rel="stylesheet" type="text/css" />
    <!-- App Css-->
    <link href="${pageContext.request.contextPath}/assets/css/app.min.css" rel="stylesheet" type="text/css" />
    <!-- custom Css-->
    <link href="${pageContext.request.contextPath}/assets/css/custom.min.css" rel="stylesheet" type="text/css" />
    <style>
        div#buttonDiv {
            text-align: center;
            padding-left: 89px;
        }

        .auth-one-bg .bg-overlay {
            /* background: -webkit-gradient(linear,left top,right top,from(#364574),to(#405189)); */
            background: linear-gradient(to right, #175e5d, #005b4f);
            opacity: .9;
        }
    </style>

    <script>
        var scriptBaseUrl = "${pageContext.request.contextPath}/";
    </script>
</head>

<body>
    
    <div class="page-content">

        <div class="row justify-content-center">
            <div class="col-lg-10">
                <div class="card">
                    <div class="bg-warning-subtle position-relative">
                        <div class="card-body p-5">
                            <div class="text-center">
                                <h3>Privacy Policy</h3>
                                <p class="mb-0 text-muted">Last update: Apr, 2025</p>
                            </div>
                        </div>
                        <div class="shape">
                            <svg xmlns="http://www.w3.org/2000/svg" version="1.1" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:svgjs="http://svgjs.com/svgjs" width="1440" height="60" preserveAspectRatio="none" viewBox="0 0 1440 60">
                                <g mask="url(&quot;#SvgjsMask1001&quot;)" fill="none">
                                    <path d="M 0,4 C 144,13 432,48 720,49 C 1008,50 1296,17 1440,9L1440 60L0 60z" style="fill: var(--vz-secondary-bg);"></path>
                                </g>
                                <defs>
                                    <mask id="SvgjsMask1001">
                                        <rect width="1440" height="60" fill="#ffffff"></rect>
                                    </mask>
                                </defs>
                            </svg>
                        </div>
                    </div>


                    <div class="card-body p-4">
                        <p class="text-muted">
                            Miracle Hospital (“we”, “us”, or “our”) respects your privacy and is committed to protecting 
                            the personal information you share with us when using our mobile applications — 
                            <strong>Miracle Hospital Patient App</strong> and <strong>Miracle Hospital Doctor App</strong> 
                            (collectively, “the Apps”).
                        </p>
                        <p>
                            By using our Apps, you agree to the collection, use, and disclosure of your information as described in this Privacy Policy.
                        </p>
                        <div class="d-flex">
                            <div class="flex-shrink-0 me-3">
                                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-check-circle text-success icon-dual-success icon-xs"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path><polyline points="22 4 12 14.01 9 11.01"></polyline></svg>
                            </div>
                            <div class="flex-grow-1">
                                <h5>⁠Information We Collect</h5>
                                <p class="text-muted">
                                    We collect minimal and necessary information for the purpose of providing our services:
                                </p>
                                <ul class="text-muted">
                                    <li><strong>Personal Information:</strong> Mobile number (for OTP-based login), name, specialty, and appointment details.</li>
                                    <li><strong>App Usage Data:</strong> Appointment status updates, chat messages, calendar selections.</li>
                                    <li><strong>Optional Info:</strong> Feedback or messages you choose to provide.</li>
                                </ul>
                                
                            </div>
                        </div>
                        
                        <div class="d-flex">
                            <div class="flex-shrink-0 me-3">
                                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-check-circle text-success icon-dual-success icon-xs"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path><polyline points="22 4 12 14.01 9 11.01"></polyline></svg>
                            </div>
                            <div class="flex-grow-1">
                                <h5>How We Use Your Information</h5>
                                <p class="text-muted">Our app requires the following SMS permissions:</p>
                                <ul class="text-muted vstack gap-2">
                                    <li>Authenticate users via OTP</li>
                                    <li>Display relevant appointments</li>
                                    <li>Enable communication between doctors and patients</li>
                                    <li>Maintain visit history and appointment status</li>
                                    <li>Improve app features and provide support</li>
                                </ul>
                            </div>
                        </div>

                        <div class="d-flex">
                            <div class="flex-shrink-0 me-3">
                                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-check-circle text-success icon-dual-success icon-xs"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path><polyline points="22 4 12 14.01 9 11.01"></polyline></svg>
                            </div>
                            <div class="flex-grow-1">
                                <h5>Data Sharing</h5>
                                <p class="text-muted">We do <strong>not sell</strong> your personal data. We may only share data with:</p>
                                <ul class="text-muted vstack gap-2">
                                    <li>Authorized hospital staff or internal systems</li>
                                    <li>Legal authorities when required by law</li>
                                </ul>
                            </div>
                        </div>
                        
                        <div class="d-flex">
                            <div class="flex-shrink-0 me-3">
                                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-check-circle text-success icon-dual-success icon-xs"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path><polyline points="22 4 12 14.01 9 11.01"></polyline></svg>
                            </div>
                            <div class="flex-grow-1">
                                <h5>Data Retention</h5>
                                <p class="text-muted">We retain your data only as long as needed to provide services or as required by law.</p>
                                
                            </div>
                        </div>

                        <div class="d-flex">
                            <div class="flex-shrink-0 me-3">
                                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-check-circle text-success icon-dual-success icon-xs"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path><polyline points="22 4 12 14.01 9 11.01"></polyline></svg>
                            </div>
                            <div class="flex-grow-1">
                                <h5>Security</h5>
                                <p class="text-muted">We use standard security measures to protect your data, but no system is 100% secure.</p>
                                
                            </div>
                        </div>
                        <div class="d-flex">
                            <div class="flex-shrink-0 me-3">
                                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-check-circle text-success icon-dual-success icon-xs"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path><polyline points="22 4 12 14.01 9 11.01"></polyline></svg>
                            </div>
                            <div class="flex-grow-1">
                                <h5>Your Rights</h5>
                                <ul class="text-muted vstack gap-2">
                                    <li>Request account/data deletion anytime</li>
                                    <li>Update or correct profile info via the app</li>
                                    <li>Contact us for privacy concerns</li>
                                </ul>
                                <!-- <p class="text-muted">
                                    For account deletion, please visit: 
                                    <a href="https://miraclehospital.care/delete-account.php" target="_blank">
                                    https://miraclehospital.care/delete-account.php
                                    </a>
                                </p> -->
                            </div>
                        </div>
                        <div class="d-flex">
                            <div class="flex-shrink-0 me-3">
                                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-check-circle text-success icon-dual-success icon-xs"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path><polyline points="22 4 12 14.01 9 11.01"></polyline></svg>
                            </div>
                            <div class="flex-grow-1">
                                <h5>Children's Privacy</h5>
                                <p class="text-muted">Our apps are intended for users aged 18 and above. If we discover data collected from a minor 
                                without consent, it will be deleted.</p>
                                <p class="text-muted">vishnu@vervenest.com</p>
                                
                            </div>
                        </div>
                        <div class="d-flex">
                            <div class="flex-shrink-0 me-3">
                                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-check-circle text-success icon-dual-success icon-xs"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path><polyline points="22 4 12 14.01 9 11.01"></polyline></svg>
                            </div>
                            <div class="flex-grow-1">
                                <h5>Policy Updates</h5>
                                <p class="text-muted">We may update this policy from time to time. Changes will be reflected on this page with a new effective date.</p>
                                
                            </div>
                        </div>
                        <div class="d-flex">
                            <div class="flex-shrink-0 me-3">
                                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-check-circle text-success icon-dual-success icon-xs"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path><polyline points="22 4 12 14.01 9 11.01"></polyline></svg>
                            </div>
                            <div class="flex-grow-1">
                                <h5>Contact Us</h5>
                                <p class="text-muted">For questions or concerns, please contact us at:<br/>
                                <a href="mailto:vishnu@vervenest.com">vishnu@vervenest.com</a><br>
                                <a href="https://vervenest.com/demo/miraclehospital/" target="_blank">https://vervenest.com/demo/miraclehospital/</a>
                                </p>
                                
                            </div>
                        </div>
                        
                        <div class="d-flex">
                            <div class="flex-shrink-0 me-3">
                                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-check-circle text-success icon-dual-success icon-xs"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path><polyline points="22 4 12 14.01 9 11.01"></polyline></svg>
                            </div>
                            <div class="flex-grow-1">
                                <h5>Account Deletion</h5>
                                <p class="text-muted">
                                    If you wish to delete your account and all associated data, please send an email to [vishnu@vervenest.com] with the subject line “Delete My Account” and include your registered name and phone number/email. Our team will process your request as soon as possible.<br/>
                                </p>
                                
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- JAVASCRIPT -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js" type="text/javascript"></script>

    <script src="${pageContext.request.contextPath}/assets/libs/bootstrap/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/libs/simplebar/simplebar.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/libs/node-waves/waves.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/libs/feather-icons/feather.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/pages/plugins/lord-icon-2.1.0.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/plugins.js"></script>
    <!-- Modal Js -->
    <script src="${pageContext.request.contextPath}/assets/js/pages/modal.init.js"></script>
    <!-- prismjs plugin -->

    <script src="${pageContext.request.contextPath}/assets/libs/prismjs/prism.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/pages/form-validation.init.js"></script>

    <script src="${pageContext.request.contextPath}/assets/libs/choices.js/public/assets/scripts/choices.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/libs/simplebar/simplebar.min.js"></script>

    <script>
        document.getElementById('password-addon').addEventListener('click', function() {
            const passwordInput = document.getElementById('password-input');
            const passwordAddon = this.querySelector('i');

            if (passwordInput.type === 'password') {
                passwordInput.type = 'text';
                passwordAddon.classList.remove('ri-eye-fill');
                passwordAddon.classList.add('ri-eye-off-fill');
            } else {
                passwordInput.type = 'password';
                passwordAddon.classList.remove('ri-eye-off-fill');
                passwordAddon.classList.add('ri-eye-fill');
            }
        });
    </script>



</body>

</html>
