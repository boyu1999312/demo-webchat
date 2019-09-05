/**
 * 判断访问类型是电脑还是手机
 *
 * @author 何明胜
 *
 * 2017年9月27日
 */

$(function(){
    var mobile_flag = isMobile(); // true为PC端，false为手机端

    if(mobile_flag){
        $("#equipment-span").text("当前登录设备：手机")
    }
});

function isMobile() {
    var userAgentInfo = navigator.userAgent;

    var mobileAgents = [ "Android", "iPhone", "SymbianOS", "Windows Phone", "iPad","iPod"];

    var mobile_flag = false;

    //根据userAgent判断是否是手机
    for (var v = 0; v < mobileAgents.length; v++) {
        if (userAgentInfo.indexOf(mobileAgents[v]) > 0) {
            mobile_flag = true;
            break;
        }
    }

    var screen_width = window.screen.width;
    var screen_height = window.screen.height;

    //根据屏幕分辨率判断是否是手机
    if(screen_width < 500 && screen_height < 900){
        mobile_flag = true;
    }

    return mobile_flag;
}
