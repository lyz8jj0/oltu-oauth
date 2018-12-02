<html>
<head>
    <title>用户登录授权</title>
</head>
<body>
<form id="myForm">
    客户端(用户登录即代表对给予客户端授权)</br>
    <input type="text" name="username" value="lixinyu"/> </br>
    <input type="password" name="password" value="123456"/></br>
    <input type="hidden" name="client_id" value="${client_id}">
    <input type="hidden" name="redirect_uri" value="${redirect_uri}">
    <input type="hidden" name="response_type" value="${response_type}">
    <input type="button" id="ajaxBtn" value="登录授权">
</form>

<script src="/node_modules/jquery/jquery-3.3.1.min.js"></script>
</body>

<script type="text/javascript">
    origin = window.location.origin; // http://localhost:8080
    $(function () {
        //ajax 提交
        $("#ajaxBtn").click(function () {
            var username = $("input[name='username']").val();
            var password = $("input[name='password']").val();
            var client_id = $("input[name='client_id']").val();
            var redirect_uri = $("input[name='redirect_uri']").val();
            var response_type = $("input[name='response_type']").val();
            $.ajax({
                type: "post",
                url: origin + "/signIn",
                data: {username: username, password: password, client_id: client_id, redirect_uri: redirect_uri},
                success: function (res) {
                    if (res.success) {
                        if (redirect_uri == "") { //如果直接访问的登录页面而不是通过oauth跳转过来的则redirect_uri
                            window.location.href = origin;
                        } else {
                            window.location.href = origin + '/oauth2/authorize?response_type=' + response_type + '&client_id=' + client_id + '&redirect_uri=' + redirect_uri;
                        }
                    } else {
                        alert(res.msg);
                    }
                }
            }, "json")
        })
    })

</script>
</html>
