var lockReconnect = false;//避免重复连接
var wsUrl = "ws://server.natappfree.cc:33195/webServer/" + 1;
var ws;
var tt;


//个人信息
var USER_NICKNAME = $("#sid").val();
var USER_UID = $("#uId").val();

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
            if(result.type == 2){
                var cla = result.id == USER_UID
                    ? "<div style='clear:both'><div class='my-div'>" + "<span class='my-name-span'>" + result.nickName + "</span>" + "<span class='my-nbsp-span'>&nbsp;&nbsp;</span>" + "<span class='my-text-span'>" + result.msg + "</span>"+ "</div>"
                    : "<div style='clear:both'><div class='other-div'>" + "<span class='other-name-span'>" + result.nickName + "</span>" + "<span class='other-nbsp-span'>&nbsp;&nbsp;</span>" + "<span class='other-text-span'>" + result.msg + "</span>"+ "</div>"
                $("#content").append(cla)
                var scrollHeight = $('#content').prop("scrollHeight");
                $('#content').animate({scrollTop:scrollHeight}, 10);
            }
        }catch (e) {
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
        $("#content").append("<div style='clear:both'><div class='my-div'>" + "<span class='my-name-span'>" + USER_NICKNAME + "</span>" + "<span class='my-nbsp-span'>&nbsp;&nbsp;</span>" + "<span class='my-text-span'>" + text + "</span>"+ "</div>")
        $("#send-text").val("")
        var scrollHeight = $('#content').prop("scrollHeight");
        $('#content').animate({scrollTop:scrollHeight}, 10);
    })
}

//退出按钮
$("#login-out").on("click", function () {
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
})

//div适应屏幕
function changeWindow() {

    $("#circle").css("height", $(window).height())
}