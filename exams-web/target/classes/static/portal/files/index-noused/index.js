var showNum=7,examPassScore=80;
var unstudypageNum=1,unstudytotal=0;
var studyPageNum=1,studyTotal=0;
var myInfo={};

$(function(){
	//来源检查
	var referrer = document.referrer;
    if (referrer==null||referrer==""||(referrer.indexOf("http://localhost/testH5/")<0 && referrer.indexOf("http://192.168.1.2/testH5/")<0)) {
    	window.location.href='/testH5/login.html';
    }
	//设置高度
	//var viewheight= document.documentElement.clientWidth;
    //var viewheight=window.innerHeight -54 -105;
	//$("#outer").height(viewheight);
	
	var curdt=new Date();
	var curmd=(curdt.getMonth()+1)+"月"+curdt.getDate()+"日";
	var curtm=curdt.getHours()+"时"+curdt.getMinutes()+"分";
	//curdt.toLocaleDateString()
	//curdt.toLocaleTimeString()
	$("#curdatelable").html(curmd);  
	$("#curtimelable").html(curtm); 
	myInfo=getMyInfo();
	if(myInfo.userid==null||myInfo.userid==""){
		window.location.href='/testH5/login.html';
	}
	$("#dplable0").html(myInfo.department);  
	$("#usernamelable0").html(myInfo.realname);
	$("#usernamelable1").html(myInfo.realname);
	$("#dplable2").html(myInfo.department);  
	$("#usernamelable2").html(myInfo.realname); 
	$("#accnamelable").html(myInfo.username);
	
	loadcontent(0,myInfo);
	
	setTimeout(function(){
          droploadfn($("#nostudyDropload"));
          //droploadfn($("#studyDropload"));
      },1000);
	

 	$("#index").click(
 		function(){
 			loadcontent(0,myInfo);
 		}
 	);
 	$("#explore").click(
 	 	function(){
 	 		loadcontent(1,myInfo);
 	 	}
 	);
 	$("#my").click(
 		
 	 	 function(){
 	 	 	loadcontent(2,myInfo);
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
 	 				"url":"emptestinfo.html",//方法请求到/emp/logint路径
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
 	
});

function loadcontent( pagenum,myInfo){
	//console.log(myInfo);
	//未学习
	if(pagenum==0){
		//获取未学习材料列表
		var  material=[{'a':"pdf/alphatrans.pdf", 'b':3},{'a':21, 'b':13},{'a':22, 'b':23},{'a':24, 'b':23},{'a':42, 'b':43},{'a':34, 'b':13}];
		//参数
		var code="";
		//{"empID"："","showNum":10,"pageNum":1}
		code="{\"empID\":\""+myInfo.userid+"\",\"showNum\":\""+showNum+"\",\"pageNum\":\""+1+"\"}";
		//alert(code);
		loadStudyData(code,"unlearnedresource.html",0,0);
		
	}else{
		if(pagenum==1){
			//已学习	
			var  material=[{'a':"pdf/alphatrans.pdf", 'b':3},{'a':21, 'b':13},{'a':22, 'b':23},{'a':24, 'b':23},{'a':42, 'b':43},{'a':34, 'b':13}];
			//参数
			var code="";
			//{"empID"："","showNum":10,"pageNum":1}
			code="{\"empID\":\""+myInfo.userid+"\",\"showNum\":\""+showNum+"\",\"pageNum\":\""+1+"\"}";
			loadStudyData(code,"learnedresource.html",1,0);
		}else{
			//我的
			var code="";
			//{"empID"：""}
			code="{\"empID\":\""+myInfo.userid+"\"}";
			loadStudyData(code,"emptestinfo.html",2,0);
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

function droploadfn(dropElement){
	var dropload = dropElement.dropload({
 	    domUp : {
 	        domClass   : 'dropload-up',
 	        domRefresh : '<div class="dropload-refresh">↓下拉刷新</div>',
 	        domUpdate  : '<div class="dropload-update">↑释放更新</div>',
 	        domLoad    : '<div class="dropload-load"><span class="loading"></span>加载中...</div>'
 	    },
 	    domDown : {
 	        domClass   : 'dropload-down',
 	        domRefresh : '<div class="dropload-refresh">↑上拉加载更多</div>',
 	        domLoad    : '<div class="dropload-load"><span class="loading"></span>加载中...</div>',
 	        domNoData  : '<div class="dropload-noData">暂无数据</div>'
 	    },
 	    loadUpFn : function(me){
 	    	//参数
 			var code="";
 			//{"empID"："","showNum":10,"pageNum":1}
 			code="{\"empID\":\""+myInfo.userid+"\",\"showNum\":\""+showNum+"\",\"pageNum\":\""+1+"\"}";
 			if($(dropElement).attr("id")=="nostudyDropload"){
 				loadStudyData(code,"unlearnedresource.html",0,0);
 			}else{
 				if($(dropElement).attr("id")=="studyDropload"){
 	 				loadStudyData(code,"learnedresource.html",1,0);
 	 			}
 			}
 			// 为了测试，延迟1秒加载
            setTimeout(function(){
                // 每次数据加载完，必须重置
                dropload.resetload();
            },1000);
 			
 	    },
 	    loadDownFn : function(me){
 	    	
 	    	if($(dropElement).attr("id")=="nostudyDropload"){
 	    		//alert("unstusy num="+((unstudypageNum -1) * showNum)+";total="+unstudytotal);
 	    		if(((unstudypageNum -1) * showNum) < unstudytotal){
	 	    		var urlstr="";
	 	    		urlstr="unlearnedresourcemore"+(unstudypageNum -1)+".html"
	 	 	    	//参数
	 	 			var code="";
	 	    		//{"empID"："","showNum":10,"pageNum":1}
	 	 			code="{\"empID\":\""+myInfo.userid+"\",\"showNum\":\""+showNum+"\",\"pageNum\":\""+unstudypageNum+"\"}";
	 	 			loadStudyData(code,urlstr,0,1);
	 	 			// 为了测试，延迟1秒加载
	 	            setTimeout(function(){
	 	                // 每次数据加载完，必须重置
	 	                dropload.resetload();
	 	            },1000);
 	    		}else{
 	 	    		// 每次数据加载完，必须重置
 	 	    		dropload.isData=false;
 	                dropload.resetload();
 	    		}
 	    	}else{
 	    		if($(dropElement).attr("id")=="studyDropload"){
 		 	    	//alert("study num="+((studyPageNum -1) * showNum)+";total="+studyTotal);
 		 	    	if(((studyPageNum -1) * showNum) < studyTotal){
 		 	    		var urlstr="";
 		 	    		urlstr="learnedresourcemore"+(studyPageNum -1)+".html"
 		 	 	    	//参数
 		 	 			var code="";
 		 	    		//{"empID"："","showNum":10,"pageNum":1}
 		 	 			code="{\"empID\":\""+myInfo.userid+"\",\"showNum\":\""+showNum+"\",\"pageNum\":\""+studyPageNum+"\"}";
 		 	 			loadStudyData(code,urlstr,1,1);
 		 	 			setTimeout(function(){
 		 	                // 每次数据加载完，必须重置
 		 	                dropload.resetload();
 		 	            },1000);
 	 	    		}else{
 	 	 	    		// 每次数据加载完，必须重置
 	 	 	    		dropload.isData=false;
 	 	                dropload.resetload();
 	 	    		}
 	 	    	}
 	    	}
 	    }
 	});
 	
}

function btonclick(me){
	//alert("click,"+$(me).attr("data-para")+",next-val:"+$(me).next().val());
	if($(me).attr("data-para")=="study"){
		//清除考试数据
		localStorage.clear();
		//记录当前学习材料的开是时间
		setCookie("materialID",$(me).attr("data-id"),null);
    	setCookie("materialst",new Date().toUTCString(),null);
    	setCookie("materialType",$(me).attr("data-type"),null);
    	setCookie("examName",$(me).attr("data-exmaname"),null);
    	setCookie("materialimg",$(me).attr("data-img"),null);
    	setCookie("materialURL",$(me).next().val(),null);
    	setCookie("outExamTimes",0,null);
    	setCookie("examID",$(me).attr("data-exmaid"),null);
    	setCookie("examPageNum",1,null);
    	setCookie("orderNum",1,null);
		switch($(me).attr("data-type")) {
		  //（0：PPT，1：视频，2：音频)
	     case "0":
	    	window.location.href='trainppt.html?file='+$(me).next().val();
	        break;
	     case "1":
	     case "2":
	    	window.location.href='trainvideo.html?file='+$(me).next().val();
	        break;
	     default:
	    	alert("material type is not ppt or mp4!");
	} 

		
	}else{
		if($(me).attr("data-para")=="examstudy"){
			//清除考试数据
			localStorage.clear();
			//记录当前学习材料的开是时间
			setCookie("materialID",$(me).attr("data-id"),null);
	    	setCookie("materialst",null,null);
	    	setCookie("materialType",$(me).attr("data-type"),null);
	    	setCookie("examName",$(me).attr("data-exmaname"),null);
	    	setCookie("materialimg",$(me).attr("data-img"),null);
	    	setCookie("materialURL",$(me).next().val(),null);
	    	setCookie("outExamTimes",0,null);
	    	setCookie("examID",$(me).attr("data-exmaid"),null);
	    	setCookie("examPageNum",1,null);
	    	setCookie("examST",new Date().toUTCString(),null);
	    	setCookie("orderNum",1,null);
			window.location.href='exam.html';
		}else{
			alert("no data-para");
		}
	}
		
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
 	         	if(json.code=="200"){
          			unstudypageNum=json.data.pageNum +1;
              		unstudytotal=json.data.totalNum;
              		material=json.data.materialList;
              		$("#materialnum").html(json.data.totalNum);
         		
 				}else{
 					material=[];
 				}
 	         	
 	         	//动态显示内容
 	         	if(isMoreData==0){
 	         		$("#content_nostudy").empty();//显示前 清空原列表
 	         	}
 	    		
 	    		if(material.length<1){
 	    			$("#content_nostudy").append('<p><strong class="d-block text-gray-dark text-center">没有内容</strong></p>');
 	    		}else{
 	    			var div ="";
 	    	      	for(var i=0;i<material.length;i++){
 	    	      		//（0：PPT，1：视频，2：音频)
 	    	       		div += '<div class="media text-muted pt-3">'+
 	    						'	<img class="bd-placeholder-img mr-2 rounded" width="100" height="80" src="'+material[i].materialImg+'"/>'+
 	    						'	<p class="media-body pb-3 mb-0 small lh-125 border-bottom border-gray">'+
 	    						'		<strong class="d-block text-gray-dark">'+material[i].materialName+'</strong>'+
 	    						'		<small>'+'发布时间 : '+material[i].publishDate+'</small><br>'+
 	    						'		<small>'+'开始时间 : '+material[i].startDate+'</small><br>'+
 	    						'		<small>'+'结束时间 : '+material[i].finishDate+'</small><br></p>'+
 	    						'	<div class="btn-group-vertical">'+
 	    						'		<button type="button" class="btn btn-primary btn-sm m-1" data-id="'+material[i].materialID+'" data-exmaid="'+material[i].examID+'" data-exmaname="'+material[i].examName+
 			        	      		'" data-para="study" data-type="'+material[i].materialType+'"  data-img="'+material[i].materialImg+'" onclick="btonclick(this);">学习</button>'+
 			        	      		'       <input type="text" value="'+material[i].materialURL+'" hidden="true" >'+
 			        	      		'		<button type="button" class="btn btn-primary btn-sm m-1 " data-id="'+material[i].materialID+'" data-exmaid="'+material[i].examID+'" data-exmaname="'+material[i].examName+
 			        	      		'" data-para="examstudy" data-type="'+material[i].materialType+'" data-img="'+material[i].materialImg+'" onclick="btonclick(this);">考试</button>'+
 			        	      		'       <input type="text" value="'+material[i].materialURL+'" hidden="true">'+
 	    						'	</div>'+
 	    						'</div>'
 	    	      	}
 	    	      	$("#content_nostudy").append(div);
 	    		}
 	    		if(isMoreData==0){
 	    			$("#index").addClass("index-active");
 	 	    		$("#explore").removeClass("index-active");
 	 	    		$("#my").removeClass("index-active");
 	 	    		$("#study-content").hide();
 	 	    		$("#nostudy-content").show();
 	 	    		$("#my-content").hide();
 	    		}
 	    		alert($("#content_nostudy").height());
 	    		alert($("#mybody").height());
 	    		alert($("#footer-menu").height());
      	        break;
      	     case 1:
      	    	//已学
          		if(json.code=="200"){
              		studyPageNum=json.data.pageNum +1;
              		studyTotal=json.data.totalNum;
              		material=json.data.learnedMaterialList;
              		$("#studyNum").html(json.data.totalNum);
              		$("#examNum").html(json.data.examNum);
              		//已通过数量
              		$("#examPassNum").html(json.data.examPassNum);
     			}else{
     				material=[];
     			}
          		var examPassNum=0,examNum=0;
              	//动态显示内容
          		if(isMoreData==0){
          			$("#content_study").empty();//显示前 清空原列表
          		}
         		if(material.length<1){
         			$("#content_study").append('<p><strong class="d-block text-gray-dark text-center">没有内容</strong></p>');
         		}else{
         			var div ="";
         	      	for(var i=0;i<material.length;i++){
         	      		//（0：PPT，1：视频，2：音频)
         	       		div += '<div class="media text-muted pt-3">'+
         						'	<img class="bd-placeholder-img mr-2 rounded" width="100" height="80" src="'+material[i].materialImg+'"/>'+
         						'	<p class="media-body pb-3 mb-0 small lh-125 border-bottom border-gray">'+
         						'		<strong class="d-block text-gray-dark">'+material[i].materialName+'</strong>'+
         						'		<small>'+'发布时间 : '+material[i].publishDate+'</small><br>'+
         						'		<small>'+'已学：'+material[i].studyTimes+' 次， '+material[i].studyDuration+' 分钟</small><br>'+
     							'       <small>'+'已考 ： '+material[i].examTimes+' 次， 最高 '+material[i].maxScores+' 分 </small><br></p>'+
         						'	<div class="btn-group-vertical">'+
         						'		<button type="button" class="btn btn-primary btn-sm m-1" data-id="'+material[i].materialID+'" data-exmaid="'+material[i].examID+'" data-exmaname="'+material[i].examName+
  		        	      		'" data-para="study" data-type="'+material[i].materialType+'"  data-img="'+material[i].materialImg+'" onclick="btonclick(this);">再学</button>'+
  		        	      		'       <input type="text" value="'+material[i].materialURL+'" hidden="true" >'+
  		        	      		'		<button type="button" class="btn btn-primary btn-sm m-1 " data-id="'+material[i].materialID+'" data-exmaid="'+material[i].examID+'" data-exmaname="'+material[i].examName+
  		        	      		'" data-para="examstudy" data-type="'+material[i].materialType+'" data-img="'+material[i].materialImg+'" onclick="btonclick(this);">再考</button>'+
  		        	      		'       <input type="text" value="'+material[i].materialURL+'" hidden="true">'+
         						'	</div>'+
         						'</div>';
         	       	
         	      	}
         	      	$("#content_study").append(div);
         		}
         		
         		if(isMoreData==0){
	         		$("#index").removeClass("index-active");
	     			$("#explore").addClass("index-active");
	     			$("#my").removeClass("index-active");
	     			$("#study-content").show();
	     			$("#nostudy-content").hide();
	     			$("#my-content").hide();
         		}
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
            		$("#content_nostudy").append('<p><strong class="d-block text-gray-dark text-center">没有内容</strong></p>');
        			break;
        		case 1:
        			//已学
            		$("#content_study").append('<p><strong class="d-block text-gray-dark text-center">没有内容</strong></p>');
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
	window.location.href="login.html";
}