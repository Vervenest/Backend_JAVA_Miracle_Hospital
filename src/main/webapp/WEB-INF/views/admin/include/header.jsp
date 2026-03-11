<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<html lang="en" data-layout="vertical" data-topbar="light" data-sidebar="dark" data-sidebar-size="lg" data-sidebar-image="none" data-preloader="enable" >
<!-- oncontextmenu="return false" -->

<head>

    <meta charset="utf-8" />
    <title>Dashboard | Miracle Hospital</title>
    <meta name="google-signin-client_id" content="439417909033-317e45ip3736ek1hap8detj2hpo3q6dq.apps.googleusercontent.com">

    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta content="Dashboard | Business Management Tool" name="description" />
    <meta content="Themesbrand" name="author" />
    <!-- App favicon -->
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/assets/images/miracle-logo-fav.jpeg">


    <!-- fullcalendar css -->
    <link href="${pageContext.request.contextPath}/assets/libs/fullcalendar/main.min.css" rel="stylesheet" type="text/css" />

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
    <!-- Jquery Js -->
    <script src="${pageContext.request.contextPath}/assets/js/jquery.min.js"></script>

    <!-- custom Css-->
    <link href="${pageContext.request.contextPath}/assets/css/parsley.css" rel="stylesheet" type="text/css" />
    <link href="${pageContext.request.contextPath}/assets/css/form-wizard.css" rel="stylesheet" type="text/css" />
    
    
    <!-- Jquery Js -->
    <script src="${pageContext.request.contextPath}/assets/js/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/libs/choices.js/public/assets/scripts/choices.min.js"></script>
    
    <script>
      var scriptBaseUrl = "${pageContext.request.contextPath}/";
    </script>

</head>
<style>
  .error {
    color: red;
    margin-top: 10px;
}
button.btn.btn-primary.btn-label.rounded-pill {
    margin-bottom: 39px;
    float: right;
}
</style>
<body>
<jsp:include page="/WEB-INF/views/admin/include/left_navi.jsp"/>