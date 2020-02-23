<!DOCTYPE HTML>
<html>
<style>

#fileupload {
    position: absolute;
    top: 50%;
    left: 50%;
}
#headerImage{
    position: absolute;
    top: 20%;
    left: 36%;
}

</style>
<head>
<meta charset="utf-8">
<head>
<img id="headerImage" src="https://staff.napier.ac.uk/services/corporateaffairs/photography-logos-branding/PublishingImages/ENU%20logo%20Print.jpg">
</head>
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

        limitMultiFileUploadSize: 1000000,
        done: function (e, data) {
            window.location.href = "/csvdemo";
            $.each(data.result.files, function (index, file) {
                $('<p/>').text(file.name).appendTo(document.body);
            });
        }

    });
});
</script>
</body>
</html>