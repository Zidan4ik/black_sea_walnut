let fileImage = null;
let path_default_image = "https://t4.ftcdn.net/jpg/04/73/25/49/360_F_473254957_bxG9yf4ly7OBO5I0O5KABlN930GwaMQz.jpg";


function addEventListenersImage() {
    document.getElementById('btn-delete_').addEventListener('click', function () {
        console.log('delete');
        document.getElementById('file-input_').value = null;
        // document.getElementById('img_').src = path_to_image = path_default_image;
        document.getElementById('img_').src = path_default_image;
        fileImage = null;
    });

    document.getElementById('btn-select_').addEventListener('click', function () {
        console.log('select');
        document.getElementById("file-input_").click();
    });

    document.getElementById('file-input_').addEventListener('change', function (event) {
        console.log('input');
        const file = event.target.files[0];
        if (file) {
            const imgElement = document.getElementById('img_');
            imgElement.src = URL.createObjectURL(file);
            fileImage = file;
        }
    });
}
