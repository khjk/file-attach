var fileUpload = function() {
    let formData = new FormData($("#upldForm")[0]);
    let desc = $("#descInput").val();
    let fileInput = $("#fileInput")[0];
    if(fileInput === undefined || fileInput.files.length == 0) {
        alert("파일을 1개이상 선택해주세요.");
        return;
    }
    if(desc == undefined || desc.trim() == "") {
        alert("파일 설명을 작성해주세요.");
        return;
    }
    $.ajax({
        contentType : false,
        processData : false,
        data: formData,
        url : '/files/new',
        type: 'POST',
    }).done(function(response){
        location.href='/files/' + response.fileId;
    }).fail(function (error) {
        alert(error.responseJSON.message);
    });
};

