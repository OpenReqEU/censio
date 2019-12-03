function vote(id , vote) {

    //TODO check  current APP
    var url = '/'+id+'/vote';


    $.ajax({
        url: url,
        type: 'POST',
        data: {'vote': vote},
        processData: true, // Don't process the files
        cache: false,
        success: function(msg)
        {
            $('#votedetails').replaceWith(msg);
        },
        error: function(jqXHR, textStatus, errorThrown)
        {
            // Handle errors here
            console.log('ERRORS: ' + textStatus);
        }
    });
}