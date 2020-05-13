var showNum=7,examPassScore=80;
var unstudypageNum=1,unstudytotal=0;
var studyPageNum=1,studyTotal=0;
var myInfo={};

$(function(){
	//来源检查
	var referrer = document.referrer;
	 if (referrer==null||referrer==""||(referrer.indexOf("http://localhost:10086/examsweb/")<0 && referrer.indexOf("http://20.1.139.26:10086/examsweb/")<0)) {
    	window.location.href='/examsweb/portal/login.html';
    }

	myInfo=getMyInfo();
	if(myInfo.userid==null||myInfo.userid==""){
		window.location.href='/examsweb/portal/login.html';
	}
	
	startLoading();
	$("#dplable2").html(myInfo.department);  
	$("#usernamelable2").html(myInfo.realname); 
	$("#accnamelable").html(myInfo.username);
	
	loadcontent(2,myInfo);
	
 	$("#index").click(
 		function(){
 			window.location.href='/examsweb/portal/unStudy.html';
 		}
 	);
 	$("#explore").click(
 	 	function(){
 	 		window.location.href='/examsweb/portal/study.html';
 	 	}
 	);
 	$("#my").click(
 	 	 function(){

 	 	 }
 	);
 	
 	$("#modiPWModalBtn").click(
 	 	 function(){
 	 		 var errMsg=validcheck();
 	 		 if(errMsg==null){
 	 			 //提交
 	 			var oldPassWord=$("#oldPassWord").val();
 	 			var newPassWord=$("#newPassWord").val();
 	 			//参数
 	 			var code="";
 	 			//{"empID"："","oldPassword":"","newPassword":""}
 	 			code="{\"empID\":\""+myInfo.userid+"\",\"oldPassword\":\""+oldPassWord+"\",\"newPassword\":\""+newPassWord+"\"}";
 	 			//加密
 	 			encode=aesEncryptJava(code,abc,abc);
 	 			var curdt=dateFtt("yyyyMMddhhmmssS",new Date());
 	 			var post_json={};
 	 			post_json["code"]=encode;
 	 			post_json["timestamp"]=curdt;
 	 			//获取数据
 	 			$.ajax({
 	 				 //"url":"resetPwd.html",//方法请求到/emp/logint路径
 	 				 "url":"/examsweb/api/emp/resetPwd",
 	 		         "data":post_json,	//传过去的数据，现在没有 null 或者直接删除掉这行
 	 		         "type":"post",//请求方式
 	 		         "dataType":"json",//数据类型
 	 		         "contentType":"application/x-www-form-urlencoded; charset=utf-8",
 	 		         "success":function(json) {
 	 		 	         if(json.code=="200"){
 	 		 	        	 //提示
 	 		 	        	$("#modiPWModalCancle").click();
 	 	 		 			$("#tipMsg").html("密码更新成功!");
 	 	 		 	 		$('#tipDiv').css("display","");
 	 	 		 	 		$("#tipDiv").show();
 	 	 		 	 		setTimeout(function(){
 	 	 		 	 			$("#tipDiv").hide();
 	 	 		 	 		},2000);
 	 	 		 	 		
 	 		 			}else{
	 	 		 			//提示
	 	 		 			$("#tipMsg").html(errMsg);
	 	 		 	 		$('#tipDiv').css("display","");
	 	 		 	 		$("#tipDiv").show();
	 	 		 	 		return false;
 	 		 			}
 	 		         },
 	 		         
 	 		         "error":function(error){
 	 		        	console.log(error);
 	 		        	//提示
 	 	 	 			$("#tipMsg").html(errMsg);
 	 	 	 			$('#tipDiv').css("display","");
 	 	 	 			$("#tipDiv").show();
 	 					return false;
 	 				}
 	 		    });
 	 			
 	 		 }else{
 	 			 //提示
 	 			 $("#tipMsg").html(errMsg);
 	 			 $('#tipDiv').css("display","");
 	 			 $("#tipDiv").show();
 	 		 }
 	 	 	
 	 	 }
 	);
 	
 	setTimeout(function(){
 		completeLoading();
    },1000);
});

function loadcontent( pagenum,myInfo){
	//未学习
	if(pagenum==0){
		
	}else{
		if(pagenum==1){
			
		}else{
			//我的
			var code="";
			//{"empID"：""}
			code="{\"empID\":\""+myInfo.userid+"\"}";
			loadStudyData(code,"/examsweb/api/emp/emptestinfo",2,0);
		}
	
	}
	
}
function checkPW(data){
    var pwdRegex = new RegExp('(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[^a-zA-Z0-9]).{8,30}');  
    if (!pwdRegex.test(data)) {    
    	return "您的密码复杂度太低（密码中必须包含大小写字母、数字、特殊字符,8-30位）";
    }
    return null;
    
}
function validcheck(){
	if($("#oldPassWord").val()==null||$("#oldPassWord").val()=="") return "原密码不能为空!";
	if($("#newPassWord").val()==null||$("#newPassWord").val()=="") return "新密码不能为空!";
	if($("#newPassWord2").val()==null||$("#newPassWord2").val()=="") return "确认密码不能为空!";
	var oldPassWord=$("#oldPassWord").val();
	var newPassWord=$("#newPassWord").val();
	var newPassWord2=$("#newPassWord2").val();
	if(newPassWord!=newPassWord2) return "2次密码不一致!";
	return checkPW(newPassWord);
}


function loadStudyData(jsonStr,url,isStudy,isMoreData){
	//加密
	encode=aesEncryptJava(jsonStr,abc,abc);
	var curdt=dateFtt("yyyyMMddhhmmssS",new Date());
	var post_json={};
	post_json["code"]=encode;
	post_json["timestamp"]=curdt;
	//获取数据
	$.ajax({
		//"url":"unlearnedresource.html",//方法请求到/emp/logint路径
		"url":url,
         "data":post_json,	//传过去的数据，现在没有 null 或者直接删除掉这行
         "type":"post",//请求方式
         "dataType":"json",//数据类型
         "contentType":"application/x-www-form-urlencoded; charset=utf-8",
         "success":function(json) {
         	switch(isStudy) {
      		  //（0：未学，1：已学，2：我的)
      	     case 0:
      	    	//未学
 	         	
      	        break;
      	     case 1:
      	    	//已学
          		
      	    	break;
      	     case 2:
      	    	 //我的
      	    	 var materialTotalNum=0,studyNum=0,examTotalNum=0,examNum=0,studyTimes=0;
      	    	 var studyDuration=0,examTimes=0,examPassNum;
      	    	if(json.code=="200"){
      	    		materialTotalNum=json.data.materialTotalNum;
      	    		studyNum=json.data.studyNum;
      	    		examTotalNum=json.data.examTotalNum;
      	    		examNum=json.data.examNum;
      	    		studyTimes=json.data.studyTimes;
      	    		studyDuration=json.data.studyDuration;
      	    		examTimes=json.data.examTimes;
      	    		examPassNum=json.data.examPassNum;
      	    	}	
      	    	
      	    	$("#content_my").empty();//显示前 清空原列表
    			var div = '<h6 class="border-bottom border-gray pb-2 mb-3 my-content-font">'+
    				'		<div class="container pl-0"><div class="row"><div class="col">'+
    				'		 <span>材料共：</span><span class="my-content-num">'+materialTotalNum+'</span><br>'+
    				'		 <span>试卷共：</span><span class="my-content-num">'+examTotalNum+'</span><br>'+
    				'		 <span>达标共：</span><span class="my-content-num">'+examPassNum+'</span>'+
    				'		</div><div class="col">'+
    				'		 <span >已学：</span><span class="my-content-num">'+studyNum+'</span><br>'+
    				'		 <span >已考：</span><span class="my-content-num">'+examNum+'</span>'+
    				'		</div></div></div>'+
    				//'		 <span>通过：</span><span class="my-content-num">'+'11'+'</span>'+
    				'	   </h6>'+
    				'      <div ><h6 class="border-bottom border-gray pb-2 mb-3 my-content-font">'+
    				'        <span >学习总次数：</span><span class="my-content-num">'+studyTimes+'</span><br>'+
    				'		 <span>学习总时长：</span><span  class="my-content-num">'+studyDuration+'</span><br>'+
    				'        <span>考试总次数：</span><span class="my-content-num">'+examTimes+'</span>'+
    				'		</div>'+
    				'      </h6>'+
    				'  	   <div>'+
    				//'         <button class="btn btn-large btn-block btn-primary btn-sm" data-para="nostudy" >学习记录</button>'+
    		        '         <button class="btn btn-large btn-block btn-primary  btn-sm" data-para="nostudy" data-toggle="modal" data-target="#modiPWModal" onclick="modiPwdOnClick(this);">修改密码</button>'+
    		        '		  <button class="btn btn-large btn-block btn-primary  btn-sm" data-para="nostudy"  onclick="quitOnClick(this);">退出登陆</button>'+
    			    '      </div>';
      	    	
    			$("#content_my").append(div);
    			
    			if(isMoreData==0){
	    			$("#index").removeClass("index-active");
	    			$("#explore").removeClass("index-active");
	    			$("#my").addClass("index-active");
	    			$("#study-content").hide();
	    			$("#nostudy-content").hide();
	    			$("#my-content").show();
    			}
      	        break;
      	     default:
      	    	alert("no this page data");
         	} 
         		
         },
         
         "error":function(error){
        	console.log(error);
        	switch($(me).attr("data-type")) {
        		//（0：未学，1：已学，2：我的)
        		case 0:
        			//未学
            		//$("#content_nostudy").append('<p><strong class="d-block text-gray-dark text-center">没有内容</strong></p>');
        			break;
        		case 1:
        			//已学
            		//$("#content_study").append('<p><strong class="d-block text-gray-dark text-center">没有内容</strong></p>');
        			break;
        		case 2:
        			$("#content_my").append('<p><strong class="d-block text-gray-dark text-center">没有内容</strong></p>');
        			break;
        		default:
        			alert("no this page data");
        	} 
			return false;
		}
    });
}

function modiPwdOnClick(me){
	$("#oldPassWord").val("");
	$("#newPassWord").val("");
	$("#newPassWord2").val("");
	$("#modiPWModal").show();
}

function quitOnClick(me){
	//清除考试数据
	localStorage.clear();
	//记录当前学习材料的开是时间
	setCookie("materialID","",0);
	setCookie("materialst",null,0);
	setCookie("materialType","",0);
	setCookie("examName","",0);
	setCookie("materialimg","",0);
	setCookie("materialURL","",0);
	setCookie("outExamTimes",0,0);
	setCookie("examID",$(me).attr("data-exmaid"),0);
	setCookie("examPageNum",0,0);
	setCookie("examST",null,0);
	setCookie("orderNum",0,0);
	
	setCookie("userid","",0);
	setCookie("username","",0);
	setCookie("realname","",0);
	setCookie("deptId","",0);
	setCookie("deptname","",0);
	setCookie("userimg","",0);
	setCookie("logintimes",0,0);
	window.location.href="/examsweb/portal/login.html";
}