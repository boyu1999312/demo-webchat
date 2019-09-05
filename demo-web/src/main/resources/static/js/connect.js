$(function () {
    $("#b-login").on("click", function () {
        var params = $("#f-login").serialize();
        console.log(params)
        $.ajax({
            type: "post",
            datatype: "json",
            url: "/connect/login",
            data: params,
            success: function (result) {
                if(result.code == 200){
                    console.log(result)
                    window.location.href="/"
                }else{
                    console.log(result.msg)
                }
            }
        })
    })
})