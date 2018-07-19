$.extend(window, {    

	dialog: function(text,btn) {
        var bText = btn || "OK";
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
        });
    },
	
	dialogCancel: function(text,lBtn,rBtn,callback) {
        	var lbtn = lBtn || "NO",
        		rbtn = rBtn || "YES";
        	if(!$(".alert_wrapper").length) {
        		creatDialogCancel();
        	}
        	function creatDialogCancel() {
	        	$("body").append($('<div class="alert_wrapper">'+
	        							'<p class="dialog_content">' + text + '</p>'+
	        							'<p class="dialog_btn">'+
	        								'<span class="dialog_cancel">' + lbtn + '</span>'+
	        								'<span class="dialog_confirm">' + rbtn + '</span>'+
	        							'</p>'+
	        					   '</div>')
	        					);
        	}

        	$(".alert_wrapper").fadeIn(300);
    		$("body").on("click",".dialog_confirm", function() {
    			if( typeof callback === 'function'){
    				//console.log(fn);
    				callback();
    				killDialog();
    			}
    		});

        	$("body").on("click", ".dialog_cancel",function() {
        		$(".alert_wrapper").fadeOut(200,function() {
        			$(this).remove();
        		});
        	});
        },

        killDialog: function() {
        	$(".alert_wrapper").fadeOut(300, function() {
        		$(this).remove();
        	});
        }
})