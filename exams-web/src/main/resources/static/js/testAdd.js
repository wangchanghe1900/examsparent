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

    var deptId;
    treeSelect.render({
        // 选择器
        elem: '#tree2',
        // 数据
        data: webpath+'/dept/getDeptAllInfo',
        // 异步加载方式：get/post，默认get
        type: 'get',
        // 占位符
        placeholder: '请选择考试部门',
        // 是否开启搜索功能：true/false，默认false
        search: true,
        style: {
            folder: {
                enable: false
            },
            line: {
                enable: true
            }
        },
        // 点击回调
        click: function(d){
            deptId= d.current.id;
        },
        // 加载完成后的回调函数
        success: function (d) {
            var deptId=window.sessionStorage.getItem("deptId");
            if(deptId!=null){
                treeSelect.checkNode('tree2', deptId);
                window.sessionStorage.setItem("deptId","");
            }

            //                选中节点，根据id筛选
            //                treeSelect.checkNode('tree', 3);

            //                获取zTree对象，可以调用zTree方法
            //                var treeObj = treeSelect.zTree('tree');
            //                console.log(treeObj);

            //                刷新树结构
            //                treeSelect.refresh();
        }
    });


    form.verify({
        notNull : function(value, item){
            if(deptId==null){
                return "请选择考试部门";
            }
        }
    });
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
            content : "checkResourceList",
            success : function(layero, index){
                var body = layui.layer.getChildFrame('body', index);
                body.find(".id").val(index);
                form.render();
            },
            yes: function (index, layero) {
                var resourceId=sessionStorage.getItem("resId");
                var resourceName=sessionStorage.getItem("resourceName");
                    if(resourceId!=null){
                        //console.log(resourceId);
                        $("#resId").val(resourceId);
                        loadTestCount(resourceId);
                        window.sessionStorage.removeItem("resourceId");

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

    function loadTestCount(resId){
        $.get(webpath+"/question/getQuestionCount",{
            'questionStatus': '启用',
            'resId': resId
        },function(data){
            var testCount=window.sessionStorage.getItem("testCount");
            for(var i=1;i<=data;i++){
                $('.testCount').append(new Option(i, i));
                if(i==testCount){
                    $('.testCount').val(testCount);
                    window.sessionStorage.removeItem("testCount");
                }
            }
            layui.form.render("select");
        })
    }
    var res_Id= window.sessionStorage.getItem("res_Id");
    if(res_Id!==null && res_Id!==""){
        sessionStorage.removeItem("res_Id");
        loadTestCount(res_Id);

    }
    var startDate=window.sessionStorage.getItem("startDate");
    var endDate= window.sessionStorage.getItem("endDate");
    if(startDate!=null){
        startDate=startDate.substring(0,10);
        window.sessionStorage.setItem("startDate","");
    }else{
        startDate=new Date();
        startDate.setDate(startDate.getDate()+1);
    }
    if(endDate!=null){
        endDate=endDate.substring(0,10);
        window.sessionStorage.setItem("endtDate","");
    }else{
        endDate=new Date();
        endDate.setDate(endDate.getDate()+30);
    }

    laydate.render({
        elem: '#startDate'
        ,format:'yyyy-MM-dd'
        ,value: startDate
        ,min: 0
        ,trigger: 'click'
        ,theme: 'molv'
    });
    laydate.render({
        elem: '#endDate'
        ,format:'yyyy-MM-dd'
        ,value: endDate
        ,max: 365
        ,trigger: 'click'
        ,theme: 'molv'
    });
    var uploadInst=upload.render({
        elem: '#imgupload'
        ,url: 'testInfoSave' //改成您自己的上传接口
        ,auto: false
        ,accept:'images'
        ,acceptMime: 'image/*'
        ,field:'imginfo'
        ,size: 1024*5
        ,data: {
              testInfo: function(){
                  var data = form.val('example');
                  var imgUrl=window.sessionStorage.getItem("imgUrl");
                  if(imgUrl!=null){
                      window.sessionStorage.setItem("imgUrl","");
                  }
                  data.imgUrl=imgUrl;
                  data.deptId=deptId;
                  return JSON.stringify(data);
              }

        }
        ,bindAction: '#btnupload'
        ,choose: function(obj){ //obj参数包含的信息，跟 choose回调完全一致，可参见上文。
            //layer.load(); //上传loading
            obj.preview(function(index, file, result){
                //console.log(result);
                $('#showimg').attr('src', result); //图片链接（base64）
            });

        }
        ,before:function (obj) {
            layer.load(); //上传loading
        }
        ,done: function(res, index, upload){
            if(res.code==0){
                layer.closeAll('loading'); //关闭loading
                setTimeout(function(){
                    top.layer.msg(res.msg);
                    layer.closeAll("iframe");
                    //刷新父页面
                    parent.location.reload();
                },1000);
            }else{
                layer.closeAll('loading');
                layer.msg(res.msg);

            };

        }
        ,error: function(index, upload){
            layer.closeAll('loading');
            //layer.msg("上传图片失败",{icon:5})
/*            var demoText = $('#demoText');
            demoText.html('<span style="color: #FF5722;">上传失败</span> <a class="layui-btn layui-btn-xs demo-reload">重试</a>');
            demoText.find('.demo-reload').on('click', function(){
                uploadInst.upload();
            });*/
        }
    });

})