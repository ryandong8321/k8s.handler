(function($){
	getService();
	checkclick();
	getResult();

	function getService(){
		$.ajax({ 
			type: "post",  
			url : "http://"+window.location.host+"/servicelist?namespace=&servicename=",
			success: function(data){
				console.log(data)
				var str;
					$.each(JSON.parse(data),function(i,val){
					console.log(val)
					str+="<option value='"+val.Service+"'>"+val.Service+"</option>"
				})
				$("#service").html(str);	
			},
			error: function(data){
				console.log(data)
			}
		})
	}

	function checkclick(){
		$("#formGroupB").on("click",".addConfigFileBtn",function(){
        	var str='<div class="form-group pri_form-group">'+
						'<div class="col-md-10 pri_col-md-10">'+
							'<div class="pri_selectG pri_selectFlex">'+
		        				'<label for="" class="control-label pri_selectLabel1">file:</label>'+
								'<dl class="col-sm-3">'+
				    				'<input id="pic" type="file" name = "confFile"/>'+
				    			'</dl>'+
				    			'<label for="" class="control-label pri_selectLabel1">path:</label>'+
								'<dl class="col-sm-3">'+
				    				'<input type="type" name="pathName" class="form-control orgtext" placeholder="upload path">'+
				    			'</dl>'+
				    			'<a class="delFileBtn">delete</a>'+
				    		'</div>'+
				    	'</div>'+
				    '</div>';
            $(".addConfigFile").append(str);
        }) 

		$("#formGroupB").on("click",".delFileBtn",function(){
			$(this).parents(".pri_selectG").remove();	
		})

		$("#deployBtn").click(function(){
			/*$("#updateService").submit();*/
			dialogCancel("Are you sure?","","",btnOK)
			
        })
	}

	function btnOK(){
		$("#updateService").submit();
	}

	function getResult(){
		var result=getQueryString("result");
		if(result!=null && result!=""){
			var text = result;
			var bText = "OK";
	        // console.log($(".alert_wrapper").length === 0)
	        if(!$(".alert_wrapper").length){
	            creatDialog();
	        }
	        function creatDialog() {
	            $("body").append($('<div class="alert_wrapper">'+
	                                    '<p class="dialog_content">' + text + '</p>'+
	                                    '<span class="dialog_close">' + bText + '</span>'+
	                               '</div>')
	                            );
	        }

	        $(".alert_wrapper").fadeIn(300);
	        $(".dialog_close").on("click", function() {
	            $(".alert_wrapper").fadeOut(300,function() {
	                $(this).remove();
	            });
	        })
		}
	}
	function getQueryString(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) return unescape(r[2]);
        return null;
	}
   
})(jQuery);


