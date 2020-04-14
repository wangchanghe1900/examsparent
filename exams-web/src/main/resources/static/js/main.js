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

    function init(){
        var message=sessionStorage.getItem("messageCount");
        if(message!=null){
            $(".message").html(message);
        }else{
            $(".message").html(0);
        };
        var resourceCount=sessionStorage.getItem("resourceCount");
        if(resourceCount != null){
            $(".resoureCount").html(resourceCount);
        }else{
            $.get("resource/getResourceCount",function (data) {
                if(data.code==200){
                    sessionStorage.setItem("resourceCount",data.data);
                    $(".resoureCount").html(data.data);
                }else{
                    $(".resoureCount").html(0);
                }
            });
        }
        var empCount=sessionStorage.getItem("empCount");
        if(empCount != null){
            $(".empCount").html(empCount);
        }else{
            $.get("emp/getEmployeeCount",function (data) {
                if(data.code==200){
                    sessionStorage.setItem("empCount",data.data);
                    $(".empCount").html(data.data);
                }else{
                    $(".empCount").html(0);
                }
            });
        }
        var testCount=sessionStorage.getItem("testCount");
        if(testCount != null){
            $(".testCount").html(testCount);
        }else{
            $.get("test/getTestCount",function (data) {
                if(data.code==200){
                    sessionStorage.setItem("testCount",data.data);
                    $(".testCount").html(data.data);
                }else{
                    $(".testCount").html(0);
                }
            });
        }
        var questionCount=sessionStorage.getItem("questionCount");
        if(questionCount != null){
            $(".questionCount").html(questionCount);
        }else{
            $.get("question/getQuestionCount2",function (data) {
                if(data.code==200){
                    sessionStorage.setItem("questionCount",data.data);
                    $(".questionCount").html(data.data);
                }else{
                    $(".questionCount").html(0);
                }
            });
        }
        var answerCount=sessionStorage.getItem("answerCount");
        if(answerCount != null){
            $(".answerCount").html(answerCount);
        }else{
            $.get("statis/getAnswerCount",function (data) {
                if(data.code==200){
                    sessionStorage.setItem("answerCount",data.data);
                    $(".answerCount").html(data.data);
                }else{
                    $(".answerCount").html(0);
                }
            });
        }

    }
    init();

})
