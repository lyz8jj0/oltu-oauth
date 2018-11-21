/**
 * Created by Administrator on 2018/11/8.
 */
/*login-in*/
var origin = window.location.origin;
layui.use(['form','layer'], function(){
    var form = layui.form;
    var layer = layui.layer;


    form.on('submit(LAY-user-login-submit)', function(obj){
        console.log(obj);
        var objData = obj.field;
        var bakeurl = objData.backurl;
        var client_id = objData.client_id;
        var picture_uuid = objData.picture_uuid;
        var txtUserName = objData.username;
        var txtPassword = objData.password;
        var txtCode = objData.vercode;
        $.post(origin+'/login',{txtUserName:txtUserName,txtPassword:txtPassword,txtCode:txtCode,pictureUuid:picture_uuid},function(data){
            console.log(data);
            if(data.success){
                if(bakeurl==""){
                    window.location.href = origin;
                }else{
                    window.location.href = origin+'/oauth2/authorize?response_type=code&client_id='+client_id+'&redirect_uri='+bakeurl;
                    //http://localhost:8080/oauth2/authorize?response_type=code&client_id=CLIENT001&redirect_uri=http://www.baidu.com
                }
            }else{
                $('.code-box img').attr('src','captcha?uuid='+picture_uuid);
                $('input[name="vercode"]').val('');
                layer.msg(data.msg);
            }
        },'json')
    })
});
function codeC(){
    $('input[name="vercode"]').val('');
    var codeIMG = $('input[name="picture_uuid"]').val();
    $('.code-box img').attr('src','captcha?uuid='+codeIMG);
};
