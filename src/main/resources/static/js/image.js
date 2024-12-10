let currentFile = null;
let path_default_image = "https://t4.ftcdn.net/jpg/04/73/25/49/360_F_473254957_bxG9yf4ly7OBO5I0O5KABlN930GwaMQz.jpg";


function addEventListenersImage() {
    document.getElementById('btn-delete_').addEventListener('click', function () {
        console.log('delete');
        document.getElementById('file-input_').value = null;
        // document.getElementById('img_').src = path_to_image = path_default_image;
        document.getElementById('img_').src = path_default_image;
        document.getElementById('img_').style.display = 'block';
        document.getElementById('video_').style.display = 'none';
        currentFile = null;
    });

    document.getElementById('btn-select_').addEventListener('click', function () {
        console.log('select');
        document.getElementById("file-input_").click();
    });

    document.getElementById('file-input_').addEventListener('change', function (event) {
        console.log('input');
        const file = event.target.files[0];
        if (!file) return;

        const imgElement = document.getElementById('img_');
        const videoElement = document.getElementById('video_');

        let fileType = file.type;
        console.log('file type:',fileType);
        if (fileType.startsWith('image/')) {
            imgElement.src = URL.createObjectURL(file);
            imgElement.style.display = 'block';
            videoElement.style.display = 'none';
            currentFile = file;
        } else if (fileType.startsWith('video/')) {
            const url = URL.createObjectURL(file);
            videoElement.src = url;
            videoElement.style.display = 'block';
            imgElement.style.display = 'none';
            currentFile = file;
        } else {
            alert('Підтримуються лише файли зображень або відео.');
        }
    });
}
