layui.use(['form','layer','table','laytpl'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laytpl = layui.laytpl,
        table = layui.table;
    //提取网页根目录
    var curWwwPath = window.document.location.href;
    var pathName = window.document.location.pathname;
    var pos = curWwwPath.indexOf(pathName);
    var localhostPaht = curWwwPath.substring(0, pos);
    var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
    var webpath=localhostPaht + projectName;
    //var resourceWeb="http://192.168.1.5:8789";
    //var resourceWeb="http://20.1.198.11:8786";
    //显示页面按钮
    $.get(webpath+"/sysmenu/btnAuthroInfo",{"btn":"test"},
        function (data) {
            if(data){
                if(data.isFind){
                    $('div:first').removeClass("disp");
                }
                if(data.isAdd){
                    $('div:nth-child(2)').removeClass("disp");
                }
                if(data.isBatchDel){
                    $('div:nth-child(3)').removeClass("disp");
                }
            }
    });
    //试卷列表
    var tableIns = table.render({
        elem: '#testList',
        url :'getTestList',
        cellMinWidth : 95,
        page : true,
        height : "full-100",
        limits : [10,15,20,25],
        limit : 20,
        id : "testListTable",
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: 'id', title: '试卷ID', minWidth:80, align:"center"},
            {field: 'testName', title: '试卷名称', minWidth:200, align:"center"},
            {field: 'resourceName', title: '资源名称', minWidth:200, align:"center",templet:function(d){
                    return d.resourceinfo.resourceName;
                }},
            {field: 'deptname', title: '考试部门', align:'center',templet:function(d){
                    return d.sysDept.deptname;
                }},
            {field: 'testCount', title: '试题数', minWidth:100, align:"center"},
           {field: 'testStatus', title: '考试状态', align:'center',minWidth:180,templet:'#testStatus'},
            {field: 'createUser', title: '创建人', align:'center',minWidth:120},
            {field: 'createTime', title: '创建日期', minWidth:150, align:"center"},
            {field: 'updateTime', title: '更新日期', align:'center',minWidth:150},
            {title: '操作', minWidth:200, templet:'#testListBar',fixed:"right",align:"center"}
        ]]
    });

    //搜索【此功能需要后台配合，所以暂时没有动态效果演示】
    $(".search_btn").on("click",function(){
        //console.log($(".searchVal").val());
        if($(".searchVal").val() != ''){
            table.reload("testListTable",{
                page: {
                    curr: 1 //重新从第 1 页开始
                },
                where: {
                    testName: $(".searchVal").val()  //搜索的关键字
                }
            })
        }else{
            layer.msg("请输入搜索的内容");
        }
    });

    //编辑视频信息
    function editTest(edit){
        var index = layui.layer.open({
            id: "edit",
            title : "编辑考试信息",
            type : 2,
            content : "editTestInfoList",
            success : function(layero, index){
                var body = layui.layer.getChildFrame('body', index);
                if(edit){
                    //console.log(edit);
                    body.find(".id").val(edit.id);
                    body.find(".testname").val(edit.testName);
                    body.find("#resId").val(edit.resId);
                    body.find(".resourceName").val(edit.resourceinfo.resourceName);
                    body.find("#showimg").attr("src",webpath+"/upload"+edit.imgUrl);
                    /*body.find("#showimg").attr("src",resourceWeb+edit.imgUrl);*/
                    /*console.log(edit.testStatus=='发布'?true:false);*/
                    body.find("#testStatus").prop("checked",edit.testStatus=='发布'? true:false);
                    body.find(".remark").val(edit.remark);
                    form.render();
                }
                setTimeout(function(){
                    layui.layer.tips('点击此处返回资源列表', '.layui-layer-setwin .layui-layer-close', {
                        tips: 3
                    });
                },500)
            },
            cancel: function(index, layero){
                location.reload();
                return false;
            }
        })
        layui.layer.full(index);
        window.sessionStorage.setItem("index",index);
        //改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
        $(window).on("resize",function(){
            layui.layer.full(window.sessionStorage.getItem("index"));
        })
    };

    //添加试卷
    function addTest(){
        var index = layui.layer.open({
            id:"addtest",
            title : "新增试卷信息",
            type : 2,
            content : "testAdd",
            success : function(layero, index){
                var body = layui.layer.getChildFrame('body', index);
                setTimeout(function(){
                    layui.layer.tips('点击此处返回试卷列表', '.layui-layer-setwin .layui-layer-close', {
                        tips: 3
                    });
                },500)
            },
            cancel: function(index, layero){
                location.reload();
                return false;
            }
        })
        layui.layer.full(index);
        window.sessionStorage.setItem("index",index);
        //改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
        $(window).on("resize",function(){
            layui.layer.full(window.sessionStorage.getItem("index"));
        })
    };
    $(".addTest_btn").click(function(){
       addTest();
    });

    //批量删除
    $(".delAll_btn").click(function(){
        var checkStatus = table.checkStatus('testListTable'),
            data = checkStatus.data,
            ids ="" ;//[];
            pathFiles="";
        if(data.length > 0) {
            for (var i in data) {
                //userId.push(data[i].id);
                ids +=data[i].id+",";
                pathFiles +=data[i].url+",";
            }
            ids=ids.substring(0,ids.length-1);
            pathFiles=pathFiles.substring(0,pathFiles.length-1);
            layer.confirm('确定删除选中考试信息？',{icon:3, title:'提示信息'},function(index){
                $.post("delTestInfoByIds",{
                    ids : ids,
                    files: pathFiles
                },function(data){
                    layer.close(index);
                    layer.msg(data.msg);
                    tableIns.reload();
                })
            });
        }else{
            layer.msg("请选择需要删除的资源");
        }
    });
    form.on('switch(status)', function(data){
        layer.msg("ceshi");
        var index = layer.msg('修改中，请稍候',{icon: 16,time:false,shade:0.8});
        var id=this.value;
        setTimeout(function(){
            //layer.close(index);
            if(data.elem.checked){
                $.get("publishTest",{
                    id : id,
                    status:'发布'
                },function(data){
                    layer.close(index);
                    tableIns.reload();
                    if(data.msg==null){
                        layer.msg("您没有权限");
                    }else{
                        layer.msg(data.msg+"成功");
                    }
                })

            }else{
                $.get("publishTest",{
                    id : id,
                    status:'未发布'
                },function(data){
                    layer.close(index);
                    tableIns.reload();
                    if(data.msg==null){
                        layer.msg("您没有权限");
                    }else{
                        layer.msg(data.msg);
                    }

                })
            }
        },500);
    });
    //列表操作
    table.on('tool(testList)', function(obj){
        var layEvent = obj.event,
            data = obj.data;

        if(layEvent === 'edit'){ //编辑
            //console.log(data.sysDept.id);
            window.sessionStorage.setItem("deptId",data.sysDept.id);
            window.sessionStorage.setItem("res_Id",data.resId);
            window.sessionStorage.setItem("startDate",data.examsStartTime);
            window.sessionStorage.setItem("endDate",data.examsEndTime);
            window.sessionStorage.setItem("imgUrl",data.imgUrl);
            window.sessionStorage.setItem("testCount",data.testCount);
            //window.sessionStorage.setItem("testStatus",data.testStatus);
            editTest(data);
        }else if(layEvent === 'del'){ //删除
            layer.confirm('确定删除此试卷？',{icon:3, title:'提示信息'},function(index){
                 $.get("delTestInfoById",{
                     id : data.id,  //将需要删除的newsId作为参数传入
                     url: data.imgUrl
                 },function(data){
                     layer.msg(data.msg);
                     tableIns.reload();
                     layer.close(index);
                })
            });
        }
    });

})
