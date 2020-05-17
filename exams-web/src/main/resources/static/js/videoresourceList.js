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
    //显示页面按钮
    $.get(webpath+"/sysmenu/btnAuthroInfo",{"btn":"video"},
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
    //用户列表
    var tableIns = table.render({
        elem: '#resourceList',
        url :'getCommResourceList?resourceType=1',
        cellMinWidth : 95,
        page : true,
        height : "full-100",
        limits : [10,15,20,25],
        limit : 20,
        id : "commListTable",
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: 'id', title: '资源ID', minWidth:50, align:"center"},
            {field: 'resourceName', title: '资源名称', minWidth:200, align:"center"},
            {field: 'resourceType', title: '资源类型', minWidth:100, align:"center",templet:function (d) {
                var resourceName;
                if(d.resourceType==0){
                    resourceName="普通资源";
                }
                if(d.resourceType==1){
                    resourceName="视频资源";
                }
                if(d.resourceType==2){
                    resourceName="音频资源";
                }
                return resourceName;
            }
            },
            {field: 'userName', title: '上传用户', minWidth:150, align:"center",templet:function (d) {
                 return d.user.realname;
                }},
            {field: 'deptName', title: '所属部门', align:'center',templet:function(d){
                return d.sysDept.deptname;
            }},
            {field: 'url', title: '资源地址', minWidth:300, align:"center"},
            {field: 'createTime', title: '上传日期', minWidth:150, align:"center"},
            {field: 'updateTime', title: '更新日期', align:'center',minWidth:150},
            {title: '操作', minWidth:200, templet:'#resourceListBar',fixed:"right",align:"center"}
        ]]
    });

    //搜索【此功能需要后台配合，所以暂时没有动态效果演示】
    $(".search_btn").on("click",function(){
        //console.log($(".searchVal").val());
        if($(".searchVal").val() != ''){
            table.reload("commListTable",{
                page: {
                    curr: 1 //重新从第 1 页开始
                },
                where: {
                    resourceName: $(".searchVal").val()  //搜索的关键字
                }
            })
        }else{
            layer.msg("请输入搜索的内容");
        }
    });

    //编辑视频信息
    function editVideoResource(edit){
        var index = layui.layer.open({
            title : "编辑资源",
            type : 2,
            content : "editVideoResourceList",
            success : function(layero, index){
                var body = layui.layer.getChildFrame('body', index);
                if(edit){
                    //console.log(edit);
                    body.find(".id").val(edit.id);
                    body.find(".resourcename").val(edit.resourceName);
                    body.find(".filepath").html(edit.url);
                    body.find(".url").val(edit.url);
                    body.find("#videotmp").attr("src",webpath+"/upload"+edit.url);
                    //body.find(".userSex input[value="+edit.userSex+"]").prop("checked","checked");  //性别
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

    //添加用户
    function addVideoResource(){
        var index = layui.layer.open({
            title : "新增视频资源",
            type : 2,
            content : "videoResourceAdd",
            success : function(layero, index){
                var body = layui.layer.getChildFrame('body', index);
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
    $(".addResource_btn").click(function(){
       addVideoResource();
    });

    //批量删除
    $(".delAll_btn").click(function(){
        var checkStatus = table.checkStatus('commListTable'),
            data = checkStatus.data,
            resIds ="" ;//[];
            files="";
        if(data.length > 0) {
            for (var i in data) {
                //userId.push(data[i].id);
                resIds +=data[i].id+",";
                files +=data[i].url+",";
            }
            resIds=resIds.substring(0,resIds.length-1);
            files=files.substring(0,files.length-1);
            layer.confirm('确定删除选中资源？',{icon:3, title:'提示信息'},function(index){
                $.post("delResourceByIds",{
                    ids : resIds,
                    filesPath: files
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
    //视频浏览
    function videoList(data) {
        layer.open({
            title: false,
            type: 2,
            area: ['430px', '330px'],
            shade: 0.8,
            closeBtn: 0,
            shadeClose: true,
            content : "resource/videoDetailView",
            success : function(layero, index){
                var body = layer.getChildFrame('body', index);
                if(data){
                    body.find("#videotmp").attr("src",webpath+"/upload"+data.url);
                    form.render();
                }
            }
        });
    }
    //列表操作
    table.on('tool(resourceList)', function(obj){
        var layEvent = obj.event,
            data = obj.data;

        if(layEvent === 'edit'){ //编辑
            editVideoResource(data);
        }else if(layEvent === 'del'){ //删除
            layer.confirm('确定删除此资源？',{icon:3, title:'提示信息'},function(index){
                 $.get("delResourceById",{
                     id : data.id,  //将需要删除的newsId作为参数传入
                     url:data.url
                 },function(data){
                     layer.msg(data.msg);
                     tableIns.reload();
                     layer.close(index);
                })
            });
        }else if(layEvent === 'detail'){
            videoList(data);
        }
    });

})
