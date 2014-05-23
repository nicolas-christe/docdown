function init() {

	$(window).resize(function() {
		$("#side").resizable( "option", "maxWidth", $(window).width()-100 );
	});

	$(window).unload(function() {
	    var scrollpos = {}
        $(".nav").each(function() {
            var ctx=$(this).attr("data-ctx");
            if (!!ctx) {
                scrollpos[ctx]=[$(this).scrollTop(),$(this).scrollLeft()];
            }
        });
        sessionStorage.setItem("scroll-pos", JSON.stringify(scrollpos));
	});
		
	$("#side").resizable({ 
		handles: 'e', 
		minWidth: 100,
		maxWidth: $(window).width()-100,
		create:function(event, ui) {
			var sizeWidth = sessionStorage.getItem("side-width");
			if (!!sizeWidth) {
				sizeWidth=Math.min(parseInt(sizeWidth),$(window).width()-100)
				$("#side").css({width:parseInt(sizeWidth)+"px"});
				$("#main").css({left:parseInt(sizeWidth)+"px"});
			}
		},
		resize:function(event, ui) {
			$("#main").css({left:ui.size.width});
		},
		stop:function( event, ui ) {
			sessionStorage.setItem("side-width", ui.size.width);
		}
	});
	
	$("#nav-top").resizable({ 
		handles: 's',
		create:function(event, ui) {
			var navTopHeight = sessionStorage.getItem("nav-top-height");
			if (!!navTopHeight) {
				$("#nav-top").css({height:parseInt(navTopHeight)+"%"});
				$("#nav-bottom").css({height:(100-parseInt(navTopHeight))+"%"});
			}
		},
		resize: function( event, ui ) {
			var parent = ui.element.parent();
			$("#nav-top").css({height: ui.element.height()/parent.height()*100+"%"})
			$("#nav-bottom").css({height: (parent.height() - ui.element.height())/parent.height()*100+"%"});
		},
		stop:function( event, ui ) {
			var parent = ui.element.parent();
			sessionStorage.setItem("nav-top-height", ui.size.height/parent.height()*100);
		}
	});

	var scrollPos = JSON.parse(sessionStorage.getItem("scroll-pos"));
    $(".nav").each(function() {
        var ctx=$(this).attr("data-ctx");
        if (!!ctx) {
            var pos = scrollPos[ctx];
            if (!!pos) {
                $(this).scrollTop(pos[0]);
                $(this).scrollLeft(pos[1]);
           }
        }
    });
}
