$(document).on('change', 'input:checkbox', function() {
    let isChecked = $(this).prop('checked')
    let extName = $(this).prop('value');
    let data = {
         "extName" : extName
       , "isChecked" : isChecked
    };
    $.ajax({
        type: 'PUT',
        url : '/ext/edit/' + extName,
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify(data)
    }).done(function(response){
        console.log('done'); //저장완료
    }).fail(function (error) {
        alert(error.responseJSON.message);
    });
});

var addExt = function() {
    let data = {
         "extName" : $('#extName').val()
    };
    $.ajax({
        type: 'POST',
        url : '/ext/edit',
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify(data)
    }).done(function(response){
        location.href='/ext/edit';
    }).fail(function (error) {
        alert(error.responseJSON.message);
    });
};

var removeExt = function(extName) {
    let removeVal = extName.replace("  X","");
    let data = {
         "extName" : removeVal
    };
    $.ajax({
        type: 'DELETE',
        url : '/ext/edit/' + removeVal,
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify(data)
    }).done(function(response){
        location.href='/ext/edit';
    }).fail(function (error) {
        alert(error.responseJSON.message);
    });
}