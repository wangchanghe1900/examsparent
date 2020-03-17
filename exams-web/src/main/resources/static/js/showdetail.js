layui.config({
    base : "../js/"
}).extend({
    treeSelect: 'treeSelect'
})
layui.use(['form','layer','treeSelect','upload','laydate','jquery'],function(){
    var form = layui.form,
        layer =  layui.layer,
        $ = layui.$;
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


    function addOption(){
        var data=sessionStorage.getItem("data");
        if(data!=null){
            var questions=JSON.parse(data);
            sessionStorage.removeItem("data");
            $(".questionName").text(questions.questionName);
            $(".resouceName").text(questions.resourceinfo.resourceName);
            //console.log($("#questionStatus").prop("checked"));
            $("#questionStatus").prop("checked",questions.questionStatus==='启用'?true:false);
            var label1="<label class=\"layui-form-label\"><span style=\"color:red\">*</span>选项:</label>";
            $(".add_option").append(label1);

            var label2="<label class=\"layui-form-label\"><span style=\"color:red\">*</span>正确答案:</label>";
            $(".add_answer").append(label2);
            var answer='<div class="layui-input-block inline_asw" style="margin-bottom: 20px">';
            for(var i=0;i< questions.optionsList.length;i++){
                var op='<div class="layui-input-block" style="margin-bottom: 20px">';
                op+='<span style="font-weight:bolder;margin-right: 20px">'+options[i]+':</span>';
                //op+='<div class="layui-inline" style="width: 500px">';
                op+='<span >'+questions.optionsList[i].optionContent +'</span>';
                op+="</div>";
                $(".add_option").append(op);
                if(questions.questionType==="单选题"){
                    if(options[i]===questions.qanswer){
                        answer+='<input type="radio"  name="qAnswer" value="'+options[i]+'" title="'+options[i]+'" checked="" >';

                    }else{
                        answer+='<input type="radio"  name="qAnswer" value="'+options[i]+'" title="'+options[i]+'" >';
                    }

                }else if(questions.questionType==="多选题"){
                    var arr=questions.qanswer.split(",");
                    var tmp="";
                    for(var t in arr){
                        if(options[i]===arr[t]){
                            tmp='<input type="checkbox"  name="qAnswer'+options[i]+'" value="'+options[i]+'" title="'+options[i]+'" lay-skin="primary"  checked>';
                            break;
                        }else{
                            tmp='<input type="checkbox"  name="qAnswer'+options[i]+'" value="'+options[i]+'" title="'+options[i]+'" lay-skin="primary">';
                        }
                    }
                    answer+=tmp;

                }else{
                    if(options[i]===questions.qanswer){
                        answer+='<input type="radio"  name="qAnswer" value="'+options[i]+'" title="'+options[i]+'" checked="" >';

                    }else{
                        answer+='<input type="radio"  name="qAnswer" value="'+options[i]+'" title="'+options[i]+'" >';
                    }
                }

            }
        }
        answer+="</div>";
        $(".add_answer").append(answer);
        form.render();
    }
    addOption();


})