let path_default_image = "https://t4.ftcdn.net/jpg/04/73/25/49/360_F_473254957_bxG9yf4ly7OBO5I0O5KABlN930GwaMQz.jpg";

function addEventListenersImage() {
    document.getElementById('btn-delete_').addEventListener('click', function () {
        document.getElementById('file_').value = null;
        document.getElementById('img_').src = path_default_image;
        document.getElementById('img_').style.display = 'block';
        document.getElementById('video_').style.display = 'none';
        document.getElementById('hidden-path-to-media_').value = null;
    });

    document.getElementById('btn-select_').addEventListener('click', function () {
        document.getElementById("file_").click();
    });

    document.getElementById('file_').addEventListener('change', function (event) {
        const file = event.target.files[0];
        if (!file) return;

        const imgElement = document.getElementById('img_');
        const videoElement = document.getElementById('video_');

        let fileType = file.type;
        if (fileType.startsWith('image/')) {
            imgElement.src = URL.createObjectURL(file);
            imgElement.style.display = 'block';
            videoElement.style.display = 'none';
            document.getElementById('hidden-path-to-media_').value = null;
        } else if (fileType.startsWith('video/')) {
            const url = URL.createObjectURL(file);
            videoElement.src = url;
            videoElement.style.display = 'block';
            imgElement.style.display = 'none';
            document.getElementById('hidden-path-to-media_').value = null;
        } else {
            alert('Підтримуються лише файли зображень або відео.');
        }
    });
}
