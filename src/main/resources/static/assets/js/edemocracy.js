
function addUserToEdemocracy(url) {
	
    var t = $('#newEdemocracyUserName').val().trim();
    
    var user = t;

    $('#newEdemocracyUserName').val("");

    $.ajax({
        url: url,
        type: 'POST',
        data: {'user': user},
        processData: true, // Don't process the files
        cache: false,
        success: function(msg)
        {
            $('#edemocracyUserlist').replaceWith(msg);
        },
        error: function(jqXHR, textStatus, errorThrown)
        {
            // Handle errors here
            console.log('ERRORS: ' + textStatus);
        }
    });
};