layui.use(['form','layer', 'util','table'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    var tableIns = table.render({
        elem: '#operatorList',
        url :'getSystemLogList',
        cellMinWidth : 95,
        page : true,
        toolbar: 'true',
        title: '系统日志',
        defaultToolbar: ['filter', 'exports', 'print'],
        height : "full-100",
        limits : [10,15,20,25],
        limit : 20,
        id : "systemlogListTable",
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: 'userCode', title: '操作用户ID', minWidth:200, align:"center",sort: true},
            {field: 'requestURL', title: '请求URI', minWidth:150, align:"center"},
            {field: 'requestAddress', title: '请求地址', minWidth:150, align:"center"},
            {field: 'operatorType', title: '操作类型', minWidth:100, align:'center',templet:'#operatorType'},
            {field: 'operatorDateTime', title: '操作日期', align:'center',minWidth:200}
        ]]
    });

    //搜索
    $(".search_btn").on("click",function(){
        //console.log($(".searchVal").val());
        if($(".userCode").val() != ''){
            table.reload("systemlogListTable",{
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