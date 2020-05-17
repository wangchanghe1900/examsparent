var showNum=7,examPassScore=80;
var unstudypageNum=1,unstudytotal=0;
var studyPageNum=1,studyTotal=0;
var myInfo={};

$(function(){
	//来源检查
	var referrer = document.referrer;
	/*
    if (referrer==null||referrer==""||(referrer.indexOf("http://localhost:10086/examsweb/")<0 && referrer.indexOf("http://20.1.139.26:10086/examsweb/")<0)) {
    	window.location.href='/examsweb/portal/login.html';
    }*/
	
	var curdt=new Date();
	var curmd=(curdt.getMonth()+1)+"月"+curdt.getDate()+"日";
	var curtm=curdt.getHours()+"时"+curdt.getMinutes()+"分";
	//$("#curdatelable").html(curmd);  
	//$("#curtimelable").html(curtm); 
	myInfo=getMyInfo();
	if(myInfo.userid==null||myInfo.userid==""){
		window.location.href='/examsweb/portal/login.html';
	}
	$("#usernamelable1").html(myInfo.realname);
	
	startLoading();
	loadcontent(1,myInfo);
	
	setTimeout(function(){
          droploadfn($("#studyDropload"));
      },1000);
	

 	$("#index").click(
 		function(){
 			window.location.href='/examsweb/portal/unStudy.html';
 		}
 	);
 	$("#explore").click(
 	 	function(){
 	 	}
 	);
 	$("#my").click(
 		
 	 	 function(){
 	 		window.location.href='/examsweb/portal/myPage.html';
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
			//已学习	
			var  material=[{'a':"pdf/alphatrans.pdf", 'b':3},{'a':21, 'b':13},{'a':22, 'b':23},{'a':24, 'b':23},{'a':42, 'b':43},{'a':34, 'b':13}];
			//参数
			var code="";
			//{"empID"："","showNum":10,"pageNum":1}
			code="{\"empID\":\""+myInfo.userid+"\",\"showNum\":\""+showNum+"\",\"pageNum\":\""+1+"\"}";
			//loadStudyData(code,"learnedresource.html",1,0);
			loadStudyData(code,"/examsweb/api/resource/learnedResource",1,0);
			
		}else{
			//我的
			
		}
	
	}
	
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
 	        domNoData  : '<div class="dropload-noData">已经到底了</div>'
 	    },
 	    loadUpFn : function(me){
 	    	//参数
 			var code="";
 			//{"empID"："","showNum":10,"pageNum":1}
 			code="{\"empID\":\""+myInfo.userid+"\",\"showNum\":\""+showNum+"\",\"pageNum\":\""+1+"\"}";
 			if($(dropElement).attr("id")=="nostudyDropload"){
 				loadStudyData(code,"/examsweb/api/resource/learnedResource",0,0);
 			}else{
 				if($(dropElement).attr("id")=="studyDropload"){
 	 				loadStudyData(code,"/examsweb/api/resource/learnedResource",1,0);
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
	 	    		//urlstr="unlearnedresourcemore"+(unstudypageNum -1)+".html"
	 	    		urlstr ="/examsweb/api/resource/learnedResource";
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
 		 	    		//urlstr="learnedresourcemore"+(studyPageNum -1)+".html"
 		 	    		urlstr ="/examsweb/api/resource/learnedResource";
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
	    	window.location.href='/examsweb/portal/trainppt.html?file='+$(me).next().val();
	        break;
	     case "1":
	     case "2":
	    	window.location.href='/examsweb/portal/trainvideo.html?file='+$(me).next().val();
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
			window.location.href='/examsweb/portal/exam.html';
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
            		$("#content_study").append('<p><strong class="d-block text-gray-dark text-center">没有内容</strong></p>');
        			break;
        		case 2:
        			//$("#content_my").append('<p><strong class="d-block text-gray-dark text-center">没有内容</strong></p>');
        			break;
        		default:
        			alert("no this page data");
        	} 
			return false;
		}
    });
}
