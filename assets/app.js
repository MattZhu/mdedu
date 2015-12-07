$("#quiz_form").submit(function( event ) {
	
	  event.preventDefault();
//	  alert($(this).serialize());
	  Android.getAnswer($( this ).serialize() );
});