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
        if(url){
            saveRequest(url);
        }
    });
    clearBlock(fromElementId);
}

function copyHTMLBlockHeader(fromElementId, toElementId) {
    let checked = $(`#${fromElementId} input`).prop('checked');
    let articleValue = $(`#${fromElementId} input[data-name="articleId"]`).prop('value');
    let amountValue = $(`#${fromElementId} input[data-name="amount"]`).prop('value');
    $(`#${toElementId}`).html($(`#${fromElementId}`).html());
    $(`#${toElementId} input`).prop('checked', checked);
    $(`#${toElementId} input[data-name="articleId"]`).prop('value',articleValue);
    $(`#${toElementId} input[data-name="amount"]`).prop('value',amountValue);
    addEventListenerCounter();
    addEventListenerNameManager();
    clearBlock(fromElementId);
}

function copyHTMLBlockSwitch(fromElementId, toElementId){
    let checked = $(`#${fromElementId} input`).prop('checked');
    $(`#${toElementId}`).html($(`#${fromElementId}`).html());
    $(`#${toElementId} input`).prop('checked', checked);
    clearBlock(fromElementId);
}

function clearBlock(elementId) {
    $(`#${elementId}`).empty();
    $(`#${elementId} input`).val('');
}

function getActiveLangTab() {
    return document.querySelector('button.nav-link.active').getAttribute('data-lang');
}