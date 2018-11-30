<html>
<head>
    <title>第一次回调</title>
</head>
<body>

<form action="/login" method="post">
    客户端接收code,并即将使用该code进行下次post请求来获取token </br>
    <#--<input type="text" name="client_id" value="${code}">-->
    <input size="60" type="text" name="code" value=${code}>
    <#--<input type="hidden" name="backurl" value="${redirect_uri}">-->

    <input type="submit" value="请求">
</form>
</body>
</html>