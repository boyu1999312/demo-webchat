//个人信息
var USER_NICKNAME = $("#sid").val();
var USER_UID = $("#uId").val();

var lockReconnect = false;//避免重复连接
var wsUrl = "ws://server.natappfree.cc:36012/webServer/" + USER_UID;
var ws;
var tt;

//弹出框状态
var POP_OPEN = false;

//当前域名
var localhost = document.domain

function createWebSocket() {
    try {
        ws = new WebSocket(wsUrl);
        init();
    } catch(e) {
        console.log('catch');
        reconnect(wsUrl);
    }
}
function init() {
    ws.onclose = function () {
        console.log('链接关闭');
        $("#heart").text("断开");
        $("#content").empty();
        reconnect(wsUrl);
    };
    ws.onerror = function() {
        console.log('发生异常了');
        $("#heart").text("断开");
        reconnect(wsUrl);
    };
    ws.onopen = function () {
        //心跳检测重置
        heartCheck.start();
    };
    ws.onmessage = function (event) {
        //拿到任何消息都说明当前连接是正常的
        console.log('接收到消息');
        $("#heart").text("在线")
        var result = null
        try {
            result = JSON.parse(event.data)
            var flag = result.id == USER_UID
            var cla = null
            console.log("消息类型：" + result.type)
            switch(result.type){
                case 2:
                    console.log("文本")
                    cla = flag
                        ? "<div style='clear:both'><div class='my-div'>"
                        + "<span class='my-name-span'>" + result.nickName + "</span>"
                        + "<span class='my-nbsp-span'>&nbsp;&nbsp;</span>"
                        + "<pre class='my-text-pre' time='" + timeStamp2String(result.time) + "'>" + result.msg + "</pre>"+ "</div>"

                        : "<div style='clear:both'><div class='other-div'>"
                        + "<span class='other-name-span'>" + result.nickName
                        + "</span>" + "<span class='other-nbsp-span'>&nbsp;&nbsp;</span>"
                        + "<pre class='other-text-pre' time='" + timeStamp2String(result.time) + "'>" + result.msg + "</pre>"+ "</div>"

                    $("#content").append(cla)
                    var scrollHeight = $('#content').prop("scrollHeight");
                    $('#content').animate({scrollTop:scrollHeight}, 10);
                break;
                case 3:
                    console.log("图片")
                    cla = flag
                        ? "<div style='clear:both'><div class='my-div'>"
                        + "<span class='my-name-span'>" + result.nickName + "</span>"
                        + "<span class='my-nbsp-span'>&nbsp;&nbsp;</span>"
                        + "<img class='my-img' src='" + "http://" + localhost + result.msg + "' time='" + timeStamp2String(result.time) + "'/></div>"

                        : "<div style='clear:both'><div class='other-div'>"
                        + "<span class='other-name-span'>" + result.nickName
                        + "</span>" + "<span class='other-nbsp-span'>&nbsp;&nbsp;</span>"
                        + "<img class='other-img' src='" + "http://" + localhost + result.msg + "' time='" + timeStamp2String(result.time) + "'/></div>"

                    $("#content").append(cla)
                    var scrollHeight = $('#content').prop("scrollHeight");
                    $('#content').animate({scrollTop:scrollHeight}, 10);
                break;
            }
        }catch (e) {
            console.log(e)
            console.log(event.data)
        }
        heartCheck.start();
    }
}
function reconnect(url) {
    if(lockReconnect) {
        return;
    };
    lockReconnect = true;
    //没连接上会一直重连，设置延迟避免请求过多
    tt && clearTimeout(tt);
    tt = setTimeout(function () {
        createWebSocket(url);
        lockReconnect = false;
    }, 4000);
}
//心跳检测
var heartCheck = {
    timeout: 3000,
    timeoutObj: null,
    serverTimeoutObj: null,
    start: function(){
        console.log('start');
        var self = this;
        this.timeoutObj && clearTimeout(this.timeoutObj);
        this.serverTimeoutObj && clearTimeout(this.serverTimeoutObj);
        this.timeoutObj = setTimeout(function(){
            //这里发送一个心跳，后端收到后，返回一个心跳消息，
            console.log('55555');
            ws.send(JSON.stringify({
                id: USER_UID,
                msg: "心跳信息~^^~~^~",
                nickName: USER_NICKNAME,
                type: 1,
                time: new Date().getTime()
            }));
            self.serverTimeoutObj = setTimeout(function() {
                console.log(111);
                console.log(ws);
                ws.close();
                // createWebSocket();
            }, self.timeout);
        }, this.timeout)
    }
}

//发送按钮
function sendBtn() {
    $("#send-btn").on("click",function () {
        var text = $("#send-text").val()
        if(text == null || /^\s*$/.test(text)){
            $("#send-text").val("")
            return;
        }
        var params = {
            id: USER_UID,
            msg: text,
            nickName: USER_NICKNAME,
            type: 2,
            time: new Date().getTime()
        }
        ws.send(JSON.stringify(params))
        console.log(JSON.stringify(params))
        $("#content").append("<div style='clear:both'><div class='my-div'>"
            + "<span class='my-name-span'>" + USER_NICKNAME
            + "</span>" + "<span class='my-nbsp-span'>&nbsp;&nbsp;</span>"
            + "<pre class='my-text-pre' time='" + timeStamp2String(params.time) + "'>" + text + "</pre>"+ "</div>")
        $("#send-text").val("")


        $("#send-btn").animate({width:'0'}, 200)
        $("#send-btn").hide(200)

        if(POP_OPEN){
            var pop = $("#pop-img-div")
            var content = $("#top-div")
            content.animate({bottom:'0'})
            pop.animate({top:'100%'})
            pop.hide(200)
            POP_OPEN = false
        }

        setTimeout(function () {
            $("#plus-a").show(200)
            $("#plus-a").animate({width:'190'}, 200)
        },100)

        var scrollHeight = $('#content').prop("scrollHeight");
        $('#content').animate({scrollTop:scrollHeight}, 10);
        $("#send-text").focus();
    })
}

//输入框点击跳到最下面
$("#send-text").on("click", function () {
    var scrollHeight = $('#content').prop("scrollHeight");
    $('#content').animate({scrollTop:scrollHeight}, 1000);
})

//退出事件
function user_exit() {
    $.ajax({
        type: "get",
        datatype: "json",
        url: "/connect/loginout",
        success: function (result) {
            if(result.code == 200){
                alert("已退出！")
                window.location.href="/"
            }else{
                alert("退出失败!")
            }
        }
    })
}

//div适应屏幕
function changeWindow() {

    $("#circle").css("height", $(window).height())
}
//弹出框按钮
$("#pop-a").on("click", function () {
    if(POP_OPEN){
        $("#send-text").focus()
    }
    POP_OPEN = true;
    var pop = $("#pop-img-div")
    var content = $("#top-div")
    content.animate({bottom:'50%'})
    pop.show()
    pop.animate({top:'50%'})
})
//单击content-div
$("#content").on("click",function () {
    var pop = $("#pop-img-div")
    var content = $("#top-div")
    if(POP_OPEN){
        content.animate({bottom:'0'})
        pop.animate({top:'100%'})
        pop.hide(200)
        POP_OPEN = false
    }
})
//单击pre(聊天气泡)显示发送时间
$(document).click(function (e) {
    var $v_id = $(e.target)
    if($v_id.attr("time") == null || $v_id.attr("time") == ""){
        return
    }
    var x = e.pageX
    var y = e.pageY
    var $i = $("<p></p>").text($v_id.attr("time"))
    $i.css({
        "font-size": "45px",
        "z-index": 99999,
        "top": y - 20,
        "left": x,
        "position": "absolute",
        "color": "white",
        "background": "black",
        "border-radius": "15px",
        "padding": "10px",
        "cursor":"default",
        "-moz-user-select": "none",
        "-webkit-user-select": "none",
        "-ms-user-select": "none",
        "-khtml-user-select": "none",
        "user-select": "none"
    })
    $("body").append($i)
    $i.animate( {"top":y-180,"opacity":0}, 2000, function(){$i.remove();});     //动画消除
    e.stopPropagation();
})

//消息按钮弹出与缩回
$("#send-text").on("input propertychange", function () {
    if(/^\s*$/.test($(this).val()) || $(this).val() == null){
        $("#send-btn").animate({width:'0'}, 200)
        $("#send-btn").hide(200)

        setTimeout(function () {
            $("#plus-a").show(200)
            $("#plus-a").animate({width:'190'}, 200)
        },100)
    }else {
        $("#plus-a").animate({width:'0'}, 200)
        $("#plus-a").hide(200)
        setTimeout(function () {
            $("#send-btn").show()
            $("#send-btn").animate({width:'190'}, 200)
        },100)
    }
})


//图片上传
$("#img-upload").on("change", function (e) {
    var file = e.currentTarget.files[0]
    if(file == null || file == ""){
        alert("图片为空")
        return
    }
    var fd = new FormData()
    fd.append("file", file)
    fd.append("cUid", USER_UID)
    fd.append("cType", 1)
    fd.append("cCreatedTime", new Date().getTime())
    fd.append("nickName", USER_NICKNAME)
    console.log(fd)
    $.ajax({
        type: "post",
        url: "/img/upload",
        data: fd,
        dataType: "json",
        processData: false,  // processData和contentType需设置为false
        contentType: false,
        success: function (result) {
            if(result.code == 200){
                var imgVo = result.data
                console.log(imgVo)
                ws.send(JSON.stringify({
                    id: USER_UID,
                    msg: imgVo.msg,
                    nickName: USER_NICKNAME,
                    type: imgVo.type,
                    time: imgVo.time
                }))
                $("#content").append(
                    "<div style='clear:both'><div class='my-div'>"
                    + "<span class='my-name-span'>" + imgVo.nickName + "</span>"
                    + "<span class='my-nbsp-span'>&nbsp;&nbsp;</span>"
                    + "<img class='my-img' src='" + "http://" + localhost + imgVo.msg + "' time='" + timeStamp2String(imgVo.time) + "'/></div>"
                )
            }else{
                console.log("图片上传错误:" + result)
            }
        }
    })

})


changeWindow();
createWebSocket(wsUrl);
sendBtn();
sendBtn();