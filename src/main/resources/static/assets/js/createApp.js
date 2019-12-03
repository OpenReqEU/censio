function addUser() {

    var t = $('#newUserName').val().trim();


    //TODO check  current APP
    var url = '/apps/addUser';
    var user = t;

    $('#newUserName').val("");

    $.ajax({
        url: url,
        type: 'POST',
        data: {'user': user},
        processData: true, // Don't process the files
        cache: false,
        success: function(msg)
        {
            $('#userlist').replaceWith(msg);
        },
        error: function(jqXHR, textStatus, errorThrown)
        {
            // Handle errors here
            console.log('ERRORS: ' + textStatus);
        }
    });
}

function removeUser(username) {

    var t = username;

    //TODO check  current APP
    var url = '/apps/removeUser';
    var user = t;
    console.log("Call method")

    $.ajax({
        url: url,
        type: 'POST',
        data: {'user': user},
        processData: true, // Don't process the files
        cache: false,
        success: function(msg)
        {
            $('#userlist').replaceWith(msg);
        },
        error: function(jqXHR, textStatus, errorThrown)
        {
            // Handle errors here
            console.log('ERRORS: ' + textStatus);
        }
    });
}

function resendEmail(username) {

    var t = username;

    //TODO check  current APP
    var url = '/apps/resendEmail';
    var user = t;
    console.log("Call method")

    $.ajax({
        url: url,
        type: 'POST',
        data: {'user': user},
        processData: true, // Don't process the files
        cache: false,
        success: function(msg)
        {
        },
        error: function(jqXHR, textStatus, errorThrown)
        {
            // Handle errors here
            console.log('ERRORS: ' + textStatus);
        }
    });
}

function addtaskinformation() {

    var t = $('#newtask').val();


    //TODO check  current APP
    var url = '/apps/addTask';
    var task = t;

    $('#newtask').val("");

    $.ajax({
        url: url,
        type: 'POST',
        data: {'information': task},
        processData: true, // Don't process the files
        cache: false,
        success: function(msg)
        {
            $('#tasklist').replaceWith(msg);
        },
        error: function(jqXHR, textStatus, errorThrown)
        {
            // Handle errors here
            console.log('ERRORS: ' + textStatus);
        }
    });
}

