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

    var deptId="";

    function initempInfo(){
        var data=sessionStorage.getItem("empinfo");
        if(data!=null){
            sessionStorage.removeItem("empinfo");
            var empinfo=JSON.parse(data);
            deptId=empinfo.deptId;
            form.val('empInfo', {
                "employeeName": empinfo.employeeName
                ,"mobile" : empinfo.mobile
                ,"capacity":empinfo.capacity
                ,"cardId":empinfo.cardId
                ,"nickName":empinfo.nickName
                ,"bankName":empinfo.bankName
                ,"bankCard":empinfo.bankCard
                ,"officePhone":empinfo.officePhone
                ,"entryTime":empinfo.entryTime
                ,"contractSTime":empinfo.contractSTime
                ,"contractETime":empinfo.contractETime
                ,"nation":empinfo.nation
                ,"nativePlace":empinfo.nativePlace
                ,"political":empinfo.political
                ,"education":empinfo.education
                ,"graduateSchool":empinfo.graduateSchool
                ,"major":empinfo.major
                ,"graduateDate":empinfo.graduateDate
                ,"contacts":empinfo.contacts
                ,"linkPhone":empinfo.linkPhone
                ,"sendCompany":empinfo.sendCompany
                ,"jobNumber1":empinfo.jobNumber1
                ,"jobNumber2":empinfo.jobNumber2
                ,"registeredPlace":empinfo.registeredPlace
                ,"workType":empinfo.workType
                ,"trainNum":empinfo.trainNum
                ,"identitys":empinfo.identitys
                ,"educationNum":empinfo.educationNum
                ,"mealCard":empinfo.mealCard
                ,"employeeStatus":empinfo.employeeStatus
                ,"mainwork":empinfo.mainwork
            });
        }
    }
    initempInfo();
    treeSelect.render({
        // 选择器
        elem: '#depttree',
        // 数据
        data: webpath+'/dept/getDeptAllInfo',
        // 异步加载方式：get/post，默认get
        type: 'get',
        // 占位符
        placeholder: '选择所属部门',
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
            treeSelect.checkNode('depttree', deptId);
        }
    });

    form.verify({
        notNull : function(value, item){
            if(deptId==null){
                return "请选择所属部门";
            }
        },
        verifyStatus: function (value,item) {
            if(value==null || value===""){
                return "请选择员工状态";
            }
        }
    });
    laydate.render({
        elem: '.entryTime'
        ,format:'yyyy-MM-dd'
        ,trigger: 'click'
        ,theme: 'molv'
    });
    laydate.render({
        elem: '.contractSTime'
        ,format:'yyyy-MM-dd'
        ,trigger: 'click'
        ,theme: 'molv'
    });
    laydate.render({
        elem: '.contractETime'
        ,format:'yyyy-MM-dd'
        ,trigger: 'click'
        ,theme: 'molv'
    });
    laydate.render({
        elem: '#graduateDate'
        ,format:'yyyy-MM-dd'
        ,trigger: 'click'
        ,theme: 'molv'
    });
    form.on("submit(addEmployee)",function(data){
        data.field.deptId=deptId;
        //弹出loading
        var index = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
        // 实际使用时的提交信息
        $.post(webpath+"/emp/saveEmployeeInfo", {
              employeeInfo: JSON.stringify(data.field)
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

})