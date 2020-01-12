<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<title>Upload a solution file</title>
</head>
<body>
<input id="fileupload" type="file" name="files[]" data-url="upload" multiple>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script src="js/vendor/jquery.ui.widget.js"></script>
<script src="js/jquery.iframe-transport.js"></script>
<script src="js/jquery.fileupload.js"></script>
<script>
$(function () {
    $('#fileupload').fileupload({
        dataType: 'json',
        limitMultiFileUploadSize: 1000000,
        done: function (e, data) {
            window.location.href = "/csvdemo"
            $.each(data.result.files, function (index, file) {
                $('<p/>').text(file.name).appendTo(document.body);
            });
        }
    });
});
</script>
</body>
</html>