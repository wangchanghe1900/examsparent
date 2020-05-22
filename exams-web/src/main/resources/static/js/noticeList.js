layui.config({
    base : "../js/"
}).extend({
    treeSelect: 'treeSelect'
})
layui.use(['form','layer','treeSelect', 'util','laydate','table'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table,
        laydate=layui.laydate;
    var treeSelect= layui.treeSelect;
    var curWwwPath = window.document.location.href;
    var pathName = window.document.location.pathname;
    var pos = curWwwPath.indexOf(pathName);
    var localhostPaht = curWwwPath.substring(0, pos);
    var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
    var webpath = localhostPaht + projectName;

    //显示页面按钮
    $.get(webpath+"/sysmenu/btnAuthroInfo",{"btn":"notice"},
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

    var tableIns = table.render({
        elem: '#noticeList',
        url :'getNoticeList',
        cellMinWidth : 95,
        page : true,
        height : "full-100",
        limits : [10,15,20,25],
        limit : 20,
        id : "noticeListTable",
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: 'title', title: '公告标题', minWidth:200, align:"center"},
            {field: 'createTime', title: '发布日期', minWidth:150, align:"center",sort: true},
            {field: 'createUser', title: '创建人', minWidth:100, align:"center"},
            {field: 'status', title: '公告状态', minWidth:180, align:'center',templet:'#noticeStatus'},
            {field: 'deptList', title: '接收部门', align:'center',minWidth:200,templet:function(d){
                    var deptnames="";
                    for(var i=0;i<d.deptList.length;i++){
                        deptnames+=d.deptList[i]+",";
                    }
                    deptnames=deptnames.substring(0,deptnames.length-1);
                    return deptnames;
                }},
            {field: 'readerCount', title: '已读数量', align:'center',minWidth:100},
            {field: 'receiveCount', title: '接收数量', align:'center',minWidth:100},
            {title: '操作', minWidth:220, templet:'#noticeListBar',fixed:"right",align:"center"}
        ]]
    });

    //搜索
    $(".search_btn").on("click",function(){
        //console.log($(".searchVal").val());
        if($(".searchVal").val() != ''){
            table.reload("noticeListTable",{
                page: {
                    curr: 1 //重新从第 1 页开始
                },
                where: {
                    title: $(".searchVal").val()  //搜索的关键字
                }
            })
        }else{
            layer.msg("请输入搜索的内容");
        }
    });
    //添加用户
    function addNotice(){
        var index = layui.layer.open({
            title : "新增公告",
            type : 2,
            skin: 'layui-layer-lan',
            content : "addNoticeList",
            success : function(layero, index){
                var body = layui.layer.getChildFrame('body', index);
                setTimeout(function(){
                    layui.layer.tips('点击此处返回公告列表', '.layui-layer-setwin .layui-layer-close', {
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
    $(".addnotice_btn").click(function(){
        addNotice();
    });
    //编辑公告
    function editNotice(edit){
        var index = layui.layer.open({
            title : "编辑公告信息",
            skin: 'layui-layer-lan',
            type : 2,
            content : "editNoticeList",
            success : function(layero, index){
                var body = layui.layer.getChildFrame('body', index);
                if(edit){
                    body.find(".content").val(edit.content);
                    form.render();
                }
                setTimeout(function(){
                    layui.layer.tips('点击此处返回员工列表', '.layui-layer-setwin .layui-layer-close', {
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
    //批量删除
    $(".delAll_btn").click(function(){
        var checkStatus = table.checkStatus('noticeListTable'),
            data = checkStatus.data,
            noticeIds ="" ;//[];
        if(data.length > 0) {
            for (var i in data) {
                //userId.push(data[i].id);
                noticeIds +=data[i].id+",";
            }
            noticeIds=noticeIds.substring(0,noticeIds.length-1);
            layer.confirm('确定删除选中公告信息？',{icon:3, title:'提示信息'},function(index){
                $.post("delNoticeByIds",{
                    ids : noticeIds  //将需要删除的newsId作为参数传入
                },function(data){
                    tableIns.reload();
                    layer.close(index);
                })
            });
        }else{
            layer.msg("请选择需要删除的公告");
        }
    });
    form.on('switch(noticeStatus)', function(data){
        var index = layer.msg('修改中，请稍候',{icon: 16,time:false,shade:0.8});
        var id=this.value;
        setTimeout(function(){
            //layer.close(index);
            if(data.elem.checked){
                $.get("publishNotice",{
                    id : id,
                    status:'发布'
                },function(data){
                    layer.close(index);
                    tableIns.reload();
                    if(data.msg==null){
                        layer.msg("您没有权限");
                    }else{
                        layer.msg(data.msg);
                    }
                })

            }else{
                    $.get("publishNotice",{
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
    //浏览详情
    function showDetail(edit){
        top.layui.layer.open({
            title : "公告预览",
            type : 2,
            anim: 5,
            area: ['700px', '550px'],
            content : "notice/showDetailList",
            success : function(layero, index){
                //var body = layui.layer.getChildFrame('body', index);
            }
        })
    }
    //列表操作
    table.on('tool(noticeList)', function(obj){
        var layEvent = obj.event,
            data = obj.data;

        if(layEvent === 'edit'){ //编辑
            //sessionStorage.setItem("noticeInfo",JSON.stringify(data));
            sessionStorage.setItem("noticeId",data.id);
            editNotice(data);
        }else if(layEvent === 'del'){ //删除
            layer.confirm('确定删除此公告信息？',{icon:3, title:'提示信息'},function(index){
                $.get("delNoticeById",{
                    id : data.id  //将需要删除的newsId作为参数传入
                },function(data){
                    tableIns.reload();
                    layer.close(index);
                })
            });
        }else if(layEvent === 'detail'){
            sessionStorage.setItem("noticeInfo",JSON.stringify(data));
            showDetail(data);
        }
    });
})