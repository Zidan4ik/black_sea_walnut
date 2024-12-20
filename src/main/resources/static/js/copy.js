function copyHTMLBlockImage(fromElementId, toElementId) {
    $(`#${toElementId}`).html($(`#${fromElementId}`).html());
    addEventListenersImage();
    clearBlock(fromElementId);
}

function copyHTMLBlockDate(fromElementId, toElementId) {
    let dateValue = $(`#${fromElementId} input`).val();
    $(`#${toElementId}`).html($(`#${fromElementId}`).html());

    $(`#${toElementId} input`).datepicker({
        format: "dd.mm.yyyy",
        autoclose: true
    });

    if (dateValue) {
        $(`#${toElementId} input`).datepicker("update", dateValue);
    }
    clearBlock(fromElementId);
}

function copyHTMLBlockSave(fromElementId, toElementId) {
    $(`#${toElementId}`).html($(`#${fromElementId}`).html());
    document.getElementById('btn-save_').addEventListener('click', function () {
        console.log('button save was clicked!')
    });
    clearBlock(fromElementId);
}

function clearBlock(elementId) {
    $(`#${elementId}`).empty();
    $(`#${elementId} input`).val('');
}