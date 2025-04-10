let path_default_image = contextPath + "/app/uploads/image/default-image.jpg";

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
            let elementById = document.getElementById('hidden-path-to-media_');
            if (elementById) elementById.value = null;
        } else if (fileType.startsWith('video/')) {
            videoElement.src = URL.createObjectURL(file);
            videoElement.style.display = 'block';
            imgElement.style.display = 'none';
            let element = document.getElementById('hidden-path-to-media_');
            if (element) element.value = null;
        } else {
            alert('Підтримуються лише файли зображень або відео.');
        }
    });
}

function addEventListenersImage2() {
    document.querySelectorAll(".btn-select, .btn-delete, .files").forEach(function (button) {
        button.replaceWith(button.cloneNode(true));
    });

    document.querySelectorAll(".btn-select").forEach(function (button) {
        button.addEventListener('click', function () {
            let attribute = this.getAttribute('data-type');
            document.getElementById("file-" + attribute).click();
        });
    });
    document.querySelectorAll(".files").forEach(function (button) {
        button.addEventListener('change', function (event) {
            let attribute = this.getAttribute("data-type");
            const file = event.target.files[0];
            if (!file) return;
            const imageElement = document.getElementById(attribute);
            imageElement.src = URL.createObjectURL(file);
            let $input = $(`input[data-image="${attribute}"]`)[0];
            if ($input) {
                $input.value = '';
            }
        });
    });
    document.querySelectorAll(".btn-delete").forEach(function (button) {
        button.addEventListener('click', function () {
            let attribute = this.getAttribute("data-type");
            let input = document.getElementById("file-" + attribute);
            let elementById = document.getElementById(attribute);
            elementById.src = path_default_image;
            input.value = "";
            let $input = $(`input[data-image="${attribute}"]`)[0];
            if ($input) {
                $input.value = '';
            }
        });
    });
}