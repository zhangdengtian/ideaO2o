/**
 * Created by BlueMelancholy on 2019/2/15/015.
 */
$(function () {
    // 登录验证的controller url
    var loginUrl = '/o2o/local/logincheck';
    // 从地址栏的URL里获取usertype
    // usertype=1则为customer,其余为shopowner
    var usertype = getQueryString('usertype');
    // 登录次数，累积登录三次失败之后自动弹出验证码要求输入
    var loginCount = 0;
    $('#submit').click(function () {
        // 获取输入的帐号
        var userName = $('#username').val();
        // 获取输入的密码
        var password = $('#psw').val();
        // 获取验证码信息
        var verifyCodeActual = $('#j_captcha').val();
        // 是否需要验证码验证，默认为false,即不需要
        var needVerify = false;
        //如果登录三次都失败，则需要输入验证码
        if (loginCount >= 3) {
            if (!verifyCodeActual) {
                $.toast("请输入验证码");
                return;
            } else {
                needVerify = true;
            }
        }
        $.ajax({
            url: loginUrl,
            async: false,
            cache: false,
            type: "post",
            dataType: 'json',
            data: {
                userName: userName,
                password: password,
                verifyCodeContent: verifyCodeActual,
                //是否需要做验证码校验
                needVerify: needVerify
            },
            success: function (data) {
                if (data.success) {
                    $.toast("登录成功");
                    if (usertype == 1) {
                        // 若用户在前端展示系统页面则自动链接到前端展示系统首页
                        window.location.href = '/o2o/fronted/index';
                    } else {
                        // 若用户是在店家管理系统页面则自动链接到店铺列表页中
                        window.location.href = '/o2o/shopadmin/shoplist';
                    }
                } else {
                    $.toast('登录失败！' + data.errMsg);
                    loginCount++;
                    if (loginCount >= 3) {
                        // 登录失败三次，需要做验证码校验
                        $('#verifyPart').show();
                    }
                }
            }
        });
    });
})