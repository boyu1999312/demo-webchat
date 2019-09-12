//毫秒值转换yyyy-MM-dd HH::mm:ss
function timeStamp2String(time){
    var datetime = new Date();
    datetime.setTime(time);
    var year = datetime.getFullYear();
    var month = datetime.getMonth() + 1 < 10 ? "0" + (datetime.getMonth() + 1) : datetime.getMonth() + 1;
    var date = datetime.getDate() < 10 ? "0" + datetime.getDate() : datetime.getDate();
    var hour = datetime.getHours()< 10 ? "0" + datetime.getHours() : datetime.getHours();
    var minute = datetime.getMinutes()< 10 ? "0" + datetime.getMinutes() : datetime.getMinutes();
    var second = datetime.getSeconds()< 10 ? "0" + datetime.getSeconds() : datetime.getSeconds();
    return year + "-" + month + "-" + date+" "+hour+":"+minute+":"+second;
}


//点击pre元素显示发送时间
function clickPreTime(e) {
    var $v_id = $(e.target)
    if($v_id.attr("time") == null || $v_id.attr("time") == ""){
        return
    }
    var x = e.pageX
    var y = e.pageY
    var $i = $("<span></span>").text($v_id.attr("time"))
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
}

//点击图片弹出图片div
function clickImgDiv(e) {
    if($("#pop-img-div").length > 0){
        $("#pop-img-div").hide(1000, function () {
            $("#pop-img-div").remove()
        })
    }else {
        var $v_id = $(e.target)
        if($v_id.attr("imgtime") == null || $v_id.attr("imgtime") == ""){
            return
        }
        var $imgDiv = $("<div id='pop-img-div' class='pinch-zoom'><img class='pop-div-img' src='"
            + $v_id[0].src + "'><p class='img-time'>" + $v_id.attr("imgTime") + "</p></div>")
        $imgDiv.css({
            "left": 0,
            "top": 0,
            "position": "absolute",
            "width": $("#circle").width(),
            "height": $("#circle").height(),
            "background": "#3B3B3B",
            "z-index": 99999,
            "opacity": 70
        })
        $("body").append($imgDiv)
    }

}