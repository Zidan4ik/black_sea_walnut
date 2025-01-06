function copyHTMLBlockImage(fromElementId, toElementId) {
    $(`#${toElementId}`).html($(`#${fromElementId}`).html());
    addEventListenersImage();
    clearBlock(fromElementId);
}

function copyHTMLBlockTextImage(fromElementId, toElementId) {
    $(`#${toElementId}`).html($(`#${fromElementId}`).html());
    addEventListenersImage2();
    clearBlock(fromElementId);
}

function copyHTMLBlockInput(fromElementId, toElementId) {
    let valueFrom = $(`#${fromElementId} input`).val();
    $(`#${toElementId}`).html($(`#${fromElementId}`).html());
    $(`#${toElementId} input`).val(valueFrom);
    $(`#${fromElementId}`).empty();
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
        saveRequest(url);
    });
    clearBlock(fromElementId);
}

function copyHTMLBlockHeader(fromElementId, toElementId) {
    let checked = $(`#${fromElementId} input`).prop('checked');
    $(`#${toElementId}`).html($(`#${fromElementId}`).html());
    $(`#${toElementId} input`).prop('checked', checked);
    addEventListenerCounter();
    addEventListenerNameManager();
    clearBlock(fromElementId);
}

function clearBlock(elementId) {
    $(`#${elementId}`).empty();
    $(`#${elementId} input`).val('');
}