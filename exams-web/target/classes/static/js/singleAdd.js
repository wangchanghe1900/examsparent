layui.config({
    base : "../js/"
}).extend({
    treeSelect: 'treeSelect'
})
layui.use(['form','layer','treeSelect','upload','laydate'],function(){
    var form = layui.form,
        layer =  layui.layer,
        $ = layui.jquery;
    var laydate = layui.laydate;
    var upload = layui.upload;
    var formSelects = layui.formSelects;
    var treeSelect= layui.treeSelect;
    var curWwwPath = window.document.location.href;
    var pathName = window.document.location.pathname;
    var pos = curWwwPath.indexOf(pathName);
    var localhostPaht = curWwwPath.substring(0, pos);
    var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
    var webpath=localhostPaht + projectName;

    var options=["A","B","C","D","E","F","G","H","I","J","K","M","N"];
    $("#btnres").click((function () {
        var index = layer.open({
            title : "资源选择",
            type: 2,
            shadeClose: true,
            shade: 0.5,
             anim: 5,
             area: ['700px', '450px'],
            fixed: false, //不固定
            maxmin: false,
            btn:['确定','取消'],
            content : webpath+"/test/checkResourceList",
            success : function(layero, index){
                var body = layui.layer.getChildFrame('body', index);
                body.find(".id").val(index);
                form.render();
            },
            yes: function (index, layero) {
                //var myIframe = window[layero.find('iframe')[0]['name']];
                //myIframe.subshows();
                var resourceId=sessionStorage.getItem("resId");
                var resourceName=sessionStorage.getItem("resourceName");
                    if(resourceId!=null){
                        //console.log(resourceId);
                        $("#resId").val(resourceId);
                        sessionStorage.removeItem("resourceId");
                    }
                    if(resourceName!=null){
                        $("#resourceName").val(resourceName);
                        sessionStorage.removeItem("resourceName");
                    }
                 layer.close(index);
                 },
            btn2: function (index, layero) {
                layer.close(index);
                }

        });
    }));
    var i=0;
    function addOption(){
        var op=sessionStorage.getItem("options");
        var asr=sessionStorage.getItem("qanswer");
        var label1="<label class=\"layui-form-label\"><span style=\"color:red\">*</span>选项:</label>";
        $(".add_option").append(label1);

        var label2="<label class=\"layui-form-label\"><span style=\"color:red\">*</span>正确答案:</label>";
        $(".add_answer").append(label2);
        var answer='<div class="layui-input-block inline_asw" style="margin-bottom: 20px">';
        if(op!=null && asr!=null){
            var opsinfo=JSON.parse(op);
            sessionStorage.removeItem("options");
            sessionStorage.removeItem("qanswer");
            for(;i< opsinfo.length;i++){
                var op='<div class="layui-input-block" style="margin-bottom: 20px">';
                op+='<span style="font-weight:bolder;margin-right: 20px">'+options[i]+':</span>';
                op+='<div class="layui-inline" style="width: 500px">';
                op+='<input type="text" name="option'+options[i]+'" class="layui-input " value="'+opsinfo[i].optionContent+'" />';
                op+="</div></div>";
                $(".add_option").append(op);
                if(options[i]===asr){
                    answer+='<input type="radio"  name="qAnswer" value="'+options[i]+'" title="'+options[i]+'" checked="" >';

                }else{
                    answer+='<input type="radio"  name="qAnswer" value="'+options[i]+'" title="'+options[i]+'" >';
                }
            }
        }else{
            for(;i<4;i++){
                var op='<div class="layui-input-block" style="margin-bottom: 20px">';
                op+='<span style="font-weight:bolder;margin-right: 20px">'+options[i]+':</span>';
                op+='<div class="layui-inline" style="width: 500px">';
                op+='<input type="text" name="option'+options[i]+'" class="layui-input "  />';
                op+="</div></div>";
                $(".add_option").append(op);
                answer+='<input type="radio"  name="qAnswer" value="'+options[i]+'" title="'+options[i]+'" >';
            }
        }
        answer+="</div>";
        $(".add_answer").append(answer);
        form.render("radio");
    }
    addOption();

    $(".addoption_btn").click(function () {
        var op='<div class="layui-input-block" style="margin-bottom: 20px">';
        op+='<span style="font-weight:bolder;margin-right: 20px">'+options[i]+':</span>';
        op+='<div class="layui-inline" style="width: 500px">';
        op+='<input type="text" name="option'+options[i]+'" class="layui-input "  />';
        op+="</div></div>";
        $(".add_option").append(op);
        var answer='<input type="radio"  name="qAnswer" value="'+options[i]+'" title="'+options[i]+'" >';
        //answer+='<div class="layui-unselect layui-form-radio layui-form-radioed"><i class="layui-anim layui-icon layui-anim-scaleSpring"></i><div>'+options[j]+'</div></div>';
        $(".inline_asw").append(answer);
        i++;
        form.render("radio");

    });
    //单选保存
    form.on("submit(singleAdd)",function(data){
        //弹出loading
        var index = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
        // 实际使用时的提交信息
        //console.log(data.field);
        $.post(webpath+"/question/saveQuestionInfo", {
              questionInfo:JSON.stringify(data.field)
            }
            ,function(res){
                if(res.code!=200){
                    top.layer.close(index);
                    layer.msg(res.msg);
                }else{
                    setTimeout(function(){
                        top.layer.close(index);
                        top.layer.msg(res.msg);
                        layer.closeAll("iframe");
                        //刷新父页面
                        parent.location.reload();
                    },1000);
                }
            });
        return false;
    });
/*   function subshow(){
        //layer.msg("test");
        parent.parentshows();
    }*/
    //subshow();

})