//获取系统时间
var newDate = '';
//getLangDate();
//值小于10时，在前面补0
function dateFilter(date){
    if(date < 10){return "0"+date;}
    return date;
}
layui.use(['form','element','layer','jquery'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        element = layui.element;
        $ = layui.jquery;
    //上次登录时间【此处应该从接口获取，实际使用中请自行更换】
    //$(".loginTime").html(newDate.split("日")[0]+"日</br>"+newDate.split("日")[1]);
    //icon动画
    $(".panel a").hover(function(){
        $(this).find(".layui-anim").addClass("layui-anim-scaleSpring");
    },function(){
        $(this).find(".layui-anim").removeClass("layui-anim-scaleSpring");
    })
    $(".panel a").click(function(){
        parent.addTab($(this));
    })

})
