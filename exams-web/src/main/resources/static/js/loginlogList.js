layui.use(['form','layer', 'util','table'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    var tableIns = table.render({
        elem: '#loginList',
        url :'getLoginLogList',
        cellMinWidth : 95,
        page : true,
        toolbar: 'true',
        title: '登录日志',
        defaultToolbar: ['filter', 'exports', 'print'],
        height : "full-100",
        limits : [10,15,20,25],
        limit : 20,
        id : "loginListTable",
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: 'userCode', title: '登录用户ID', minWidth:200, align:"center",sort: true},
            {field: 'requestPath', title: '请求URI', minWidth:150, align:"center"},
            {field: 'requestAddress', title: '登录地址', minWidth:150, align:"center"},
            {field: 'loginStatus', title: '登录状态', minWidth:100, align:'center',templet:'#loginStatus'},
            {field: 'loginDateTime', title: '登录日期', align:'center',minWidth:200}
        ]]
    });

    //搜索
    $(".search_btn").on("click",function(){
        //console.log($(".searchVal").val());
        if($(".userCode").val() != ''){
            table.reload("loginListTable",{
                page: {
                    curr: 1 //重新从第 1 页开始
                },
                where: {
                    userCode: $(".userCode").val()  //搜索的关键字
                }
            })
        }else{
            layer.msg("请输入搜索的内容");
        }
    });

})