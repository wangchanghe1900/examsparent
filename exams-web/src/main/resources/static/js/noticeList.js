layui.config({
    base : "../js/"
}).extend({
    treeSelect: 'treeSelect'
})
layui.use(['form','layer','treeSelect', 'util','laydate'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laydate=layui.laydate;
    var treeSelect= layui.treeSelect;
    var curWwwPath = window.document.location.href;
    var pathName = window.document.location.pathname;
    var pos = curWwwPath.indexOf(pathName);
    var localhostPaht = curWwwPath.substring(0, pos);
    var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
    var webpath = localhostPaht + projectName;

    //显示页面按钮
    $.get("sysmenu/btnAuthroInfo",{"btn":"notice"},
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
            {field: 'username', title: '用户名', minWidth:100, align:"center"},
            {field: 'realname', title: '真实姓名', minWidth:100, align:"center"},
            {field: 'mobile', title: '移动电话', minWidth:100, align:"center"},
            {field: 'email', title: '用户邮箱', minWidth:200, align:'center',templet:function(d){
                    return '<a class="layui-blue" href="mailto:'+d.email+'">'+d.email+'</a>';
                }},
            {field: 'deptname', title: '所属部门', align:'center',templet:function(d){
                    return d.sysDept.deptname;
                }},
            {field: 'rolesname', title: '权限名称',  minWidth:150 ,align:'center',templet:function(d){
                    var rolename="";
                    for(var i=0;i<d.roles.length;i++){
                        rolename+=d.roles[i].name+",";
                    }
                    rolename=rolename.substring(0,rolename.length-1);
                    return rolename;
                }},
            {field: 'status', title: '用户状态',  align:'center',templet:function(d){
                    return d.status == "1" ? "正常使用" : "禁止使用";
                }},
            {field: 'lastlogintime', title: '最后登录时间', align:'center',minWidth:150},
            {title: '操作', minWidth:220, templet:'#userListBar',fixed:"right",align:"center"}
        ]]
    });

    //搜索【此功能需要后台配合，所以暂时没有动态效果演示】
    $(".search_btn").on("click",function(){
        //console.log($(".searchVal").val());
        if($(".searchVal").val() != ''){
            table.reload("userListTable",{
                page: {
                    curr: 1 //重新从第 1 页开始
                },
                where: {
                    realname: $(".searchVal").val()  //搜索的关键字
                }
            })
        }else{
            layer.msg("请输入搜索的内容");
        }
    });

})