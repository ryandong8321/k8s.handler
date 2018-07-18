(function($){
	btnClick();
	getResult();
	
	function page(){
		html='<form class="addService" action="/fileUpload" method="post" enctype="multipart/form-data">'+
				'<div class="formGroup">'+
							'<div class="infoBox">'+
								'<div class="form-group pri_inv-formG">'+
								    '<label for="" class="col-md-12 pri_invLabel_titColor"><i>yml</i><b></b></label>'+
								'</div>'+
								'<div class="form-group pri_form-group">'+
									    '<div class="col-md-10 pri_col-md-10">'+
									    	'<dl class="col-sm-3">'+		    									
		    									'<input id="pic" type="file" name = "yamlFile" />'+
		    								'</dl>'+
									    '</div>'+
								'</div>'+
								'<div class="form-group pri_inv-formG">'+
								    '<label for="" class="col-md-12 pri_invLabel_titColor"><i>files</i><b></b></label>'+
								'</div>'+
							   	'<div class="form-group pri_form-group">'+
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
								'</div>'+
								'<div class="addConfigFileDiv">'+
									'<div class="addConfigFile">'+
									'</div>'+
									'<div class="form-group pri_form-group">'+								    
									    '<div class="col-md-10 pri_col-md-10">'+
									    	'<div class="pri_selectG pri_selectFlex">'+
											    '<label for="" class="control-label pri_selectLabel1"></label>'+
											    '<div class="pri_inv_checkBox">'+
					                                '<a class="addConfigFileBtn">add more files+</a>'+
					                            '</div>'+
											'</div>'+
									    '</div>'+       
									'</div>'+
								'</div>'+
							'</div>'+
							'<div class="form-group pri_form-group">'+
							    '<div class="col-md-10 pri_col-md-10">'+
							    	'<div class="pri_selectG pri_selectFlex">'+
									    '<label for="" class="control-label pri_selectLabel1"></label>'+
									    '<div class="pri_inv_checkBox">'+
			                                '<a class="delBtn">delete</a>'+
			                            '</div>'+
									'</div>'+
							    '</div>'+      
							'</div>'+
					    '</div>'+
					'</form>';

                $("#formGroupB").append(html);
                if($(".formGroup").length<2){
                	$(".delBtn").hide();
                }else{
                	$(".delBtn").show();
                }
	}

	function btnClick(){
	    
		var html="";
		$("#inv_addBtn").click(function(){
            page()
		})

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
			$(this).parent().parent().parent().parent().siblings().append(str);
		})

		$("#formGroupB").on("click",".delBtn",function(){
           $(this).parents(".formGroup").remove();	
           if($(".formGroup").length<2){
            	$(".delBtn").hide();
            }else{
            	$(".delBtn").show();
            }
        }) 
        

        $("#deployBtn").click(function(){
        	dialogCancel("Are you sure?","","",btnOK)
        	
        })

        $("#formGroupB").on("click",".delFileBtn",function(){
			$(this).parents(".pri_selectG").remove();	
		})
	}

	function btnOK(){
		$.each($(".addService"),function(){
        		console.log($(this))
        		$(this).submit();
        })
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
   
})(jQuery);


