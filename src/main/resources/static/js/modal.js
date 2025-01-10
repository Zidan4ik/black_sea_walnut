
function invokeCreateModal() {
    let modal = new bootstrap.Modal(document.querySelector(".modalEntity_"));
    const filterElements = $('.fields-entity');
    filterElements.map(function () {
        document.getElementById(this.name + '_').value = null;
    });

    document.getElementById('modal-title_').innerText = `Add new ${name_entity}`;
    document.getElementById('submit-modal_').innerText = 'Add';

    let image_container = document.getElementById('fileImage_');
    if (image_container) {
        image_container.src = path_to_image = path_default_image;
    }

    document.getElementById('submit-modal_').onclick = function () {
        requestSaveEntity(pathForSavingEntity + `add`);
    };
    modal.show();
}

function invokeDeleteModal() {
    let modal = new bootstrap.Modal(document.querySelector(".modalEntity_"));
    modal.show();
}
