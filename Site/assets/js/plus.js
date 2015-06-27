$('a[href^="#"]').click(function(){  
$('[data-spy="scroll"]').each(function () {
  var $spy = $(this).scrollspy('refresh')
});	
    var the_id = $(this).attr("href");  
  
    $('html, body').animate({  
        scrollTop:$(the_id).offset().top  
    }, 'slow');
	
		$('.content-collapse').removeClass('in');
		$('.content-collapse').addClass('collapse');
		$(the_id+' .content-collapse').removeClass('collapse');
		$(the_id+' .content-collapse').addClass('in');
	
    return false;  
}); 
$('[data-toggle="collapse"]').click(function(){
$('[data-spy="scroll"]').each(function () {
  var $spy = $(this).scrollspy('refresh')
});	
});
 $(document).ready(function() {
   if( ! $('#linkCloud').tagcanvas({
     textColour : '#333333',
     outlineThickness : 1,
     maxSpeed : 0.03,
     depth : 0.75
   })) {
     // TagCanvas failed to load
     $('#tagCloudContainer').hide();
   }
  });