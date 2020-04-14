layui.use(['form','layer','table','laytpl','jquery'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laytpl = layui.laytpl,
        table = layui.table,
        $ = layui.$;
    //提取网页根目录
    var curWwwPath = window.document.location.href;
    var pathName = window.document.location.pathname;
    var pos = curWwwPath.indexOf(pathName);
    var localhostPaht = curWwwPath.substring(0, pos);
    var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
    var webpath=localhostPaht + projectName;
    //显示页面按钮
    $.get(webpath+"/sysmenu/btnAuthroInfo",{"btn":"question"},
        function (data) {
            if(data){
                if(data.isFind){
                    $('div:first').removeClass("disp");
                }

                if(data.isSingleAdd){
                    $('div:nth-child(2)').removeClass("disp");
                }
                if(data.isMultipleAdd){
                    $('div:nth-child(3)').removeClass("disp");
                }
                if(data.isJudgeAdd){
                    $('div:nth-child(4)').removeClass("disp");
                }
                if(data.isBatchDel){
                    $('div:nth-child(5)').removeClass("disp");
                }

                if(data.isImport){
                    $('div:nth-child(6)').removeClass("disp");
                }

            }
    });
    //用户列表
    var tableIns = table.render({
        elem: '#questionList',
        url :'getquestionList',
        cellMinWidth : 95,
        page : true,
        height : "full-100",
        limits : [10,15,20,25],
        limit : 20,
        id : "questionListTable",
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: 'questionName', title: '题目名称', minWidth:300, align:"center"},
            {field: 'questionType', title: '题目类型', minWidth:100, align:"center"},
            {field: 'resId', title: '考题所属资源', minWidth:100, align:"center",templet:function (d) {
                    return d.resourceinfo.resourceName;
                }},
            {field: 'questionStatus', title: '题目状态', minWidth:100, align:"center",templet:'#questionStatus'},
            {field: 'createTime', title: '创建日期', align:'center',minWidth:80},
            {field: 'updateTime', title: '修改日期', align:'center',minWidth:120},
            {title: '操作', minWidth:200, templet:'#questionListBar',fixed:"right",align:"center"}
        ]]
    });

    //搜索【此功能需要后台配合，所以暂时没有动态效果演示】
    $(".search_btn").on("click",function(){
        //console.log($(".searchVal").val());
        table.reload("questionListTable",{
            page: {
                curr: 1 //重新从第 1 页开始
            },
            where: {
                questionName: $(".questionName").val(),
                resourceName: $(".resourceName").val()
            }
        })

    });
     //单选新建
    $(".addSingle_btn").click(function () {
        //layer.msg("test");
        var index = layui.layer.open({
            title : "单选新增",
            type : 2,
            anim: 2,
            shade: false,
            content : "addSingleList",
            success : function(layero, index){
                //var body = layui.layer.getChildFrame('body', index);
                setTimeout(function(){
                    layui.layer.tips('点击此处返回考题列表', '.layui-layer-setwin .layui-layer-close', {
                        tips: 3
                    });
                },500);
            },
            cancel: function(index, layero){
                location.reload();
                return false;
            }
        });
        layui.layer.full(index);
        window.sessionStorage.setItem("index",index);
        //改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
        $(window).on("resize",function(){
            layui.layer.full(window.sessionStorage.getItem("index"));
        })
    });

    //多选新建
    $(".addMult_btn").click(function () {
        //layer.msg("test");
        var index = layui.layer.open({
            title : "多选新增",
            type : 2,
            content : "addMultipleList",
            success : function(layero, index){
                var body = layui.layer.getChildFrame('body', index);
                setTimeout(function(){
                    layui.layer.tips('点击此处返回考题列表', '.layui-layer-setwin .layui-layer-close', {
                        tips: 3
                    });
                },500)
            },
            cancel: function(index, layero){
                location.reload();
                return false;
            }
        });
        layui.layer.full(index);
        window.sessionStorage.setItem("index",index);
        //改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
        $(window).on("resize",function(){
            layui.layer.full(window.sessionStorage.getItem("index"));
        })
    });

    //判断新建
    $(".addJudge_btn").click(function () {
        //layer.msg("test");
        var index = layui.layer.open({
            title : "判断新增",
            type : 2,
            content : "addJudgeList",
            success : function(layero, index){
                var body = layui.layer.getChildFrame('body', index);
                setTimeout(function(){
                    layui.layer.tips('点击此处返回考题列表', '.layui-layer-setwin .layui-layer-close', {
                        tips: 3
                    });
                },500)
            },
            cancel: function(index, layero){
                location.reload();
                return false;
            }
        });
        layui.layer.full(index);
        window.sessionStorage.setItem("index",index);
        //改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
        $(window).on("resize",function(){
            layui.layer.full(window.sessionStorage.getItem("index"));
        })
    });
    //编辑考题
    function editQuestion(edit){
        var quest="";
        if(edit){
            if(edit.questionType==="单选题"){
                quest="singleAdd";
            }else if(edit.questionType==="多选题"){
                quest="multipleAdd";
            }else{
                quest="judgeAdd";
            }
        }
        var index =layui.layer.open({
            title : "编辑考题",
            type : 2,
            anim: 5,
            skin: 'layui-layer-lan',
            content : "editQuestionList?quesName="+quest,
            success : function(layero, index){
                var body = layui.layer.getChildFrame('body', index);
                //parent.showImg();
                if(edit){
                    body.find("#id").val(edit.id);
                    body.find(".questionName").val(edit.questionName);
                    body.find(".resourceName").val(edit.resourceinfo.resourceName);
                    body.find("#resId").val(edit.resId);
                    body.find("#questionStatus").prop("checked",edit.questionStatus==='启用'?true:false);
                    ///console.log(edit.qanswer);
                }
                //$(layero).find("iframe").contents();
                //var questionName = $(layero).find("iframe").contents().find(".questionName");//拿到iframe元素

                setTimeout(function(){
                    layui.layer.tips('点击此处返回考题列表', '.layui-layer-setwin .layui-layer-close', {
                        tips: 3
                    });
                },500)
            },
            cancel: function(index, layero){
                location.reload();
                return false;
            }
        });
        layui.layer.full(index);
        window.sessionStorage.setItem("index",index);
        //改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
        $(window).on("resize",function(){
            layui.layer.full(window.sessionStorage.getItem("index"));
        })
    };

    //批量删除
    $(".delAll_btn").click(function(){
        var checkStatus = table.checkStatus('questionListTable'),
            data = checkStatus.data,
            ids ="" ;//[];
        if(data.length > 0) {
            for (var i in data) {
                //userId.push(data[i].id);
                ids +=data[i].id+",";
            }
            ids=ids.substring(0,ids.length-1);
            layer.confirm('确定删除选中考题？',{icon:3, title:'提示信息'},function(index){
                $.post("delQuestionByIds",{
                    ids : ids  //将需要删除的newsId作为参数传入
                },function(data){
                    tableIns.reload();
                    layer.close(index);
                })
            });
        }else{
            layer.msg("请选择需要删除的考题");
        }
    });

    $(".import_btn").click(function () {
        layui.layer.open({
            title : "导入考题库",
            type : 2,
            anim: 5,
            skin: 'layui-layer-rim',
            area: ['500px', '300px'],
            content : "importQuestionList",
            success : function(layero, index){
                //var body = layui.layer.getChildFrame('body', index);


            }
        })
    });
    function showDetail(edit){
        top.layui.layer.open({
            title : "考题详情",
            type : 2,
            anim: 5,
            skin: 'layui-layer-lan',
            area: ['700px', '550px'],
            content : "question/showDetailList",
            success : function(layero, index){
                //var body = layui.layer.getChildFrame('body', index);

            }
        })
    }
    //列表操作
    table.on('tool(questionList)', function(obj){
        var layEvent = obj.event,
            data = obj.data;

        if(layEvent === 'edit'){ //编辑
            //console.log(data.optionsList);
            sessionStorage.setItem("options",JSON.stringify(data.optionsList));
            sessionStorage.setItem("qanswer",data.qanswer);
            editQuestion(data);
        }else if(layEvent === 'del'){ //删除
            layer.confirm('确定删除此考题信息？',{icon:3, title:'提示信息'},function(index){
                 $.get("delQuestionById",{
                     id : data.id  //将需要删除的newsId作为参数传入
                 },function(data){
                     layer.msg(data.msg);
                    tableIns.reload();

                })
            });
        }else if(layEvent === 'detail'){
            sessionStorage.setItem("data",JSON.stringify(data));
            showDetail(data);
        }
    });


})
