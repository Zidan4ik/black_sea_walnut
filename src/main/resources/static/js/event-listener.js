function addEventListenerCounter() {
    $("#arrow-left").on("click", function () {
        let amount = parseInt($("#amount_").val()) || 0;
        amount -= 1;
        $("#amount_").val(amount).attr('value', amount);
    });
    $("#arrow-right").on("click", function () {
        let amount = parseInt($("#amount_").val()) || 0;
        amount += 1;
        $("#amount_").val(amount).attr('value', amount);
    });
}

function addEventListenerNameManager() {
    let lang = getActiveLangTab();
    $("#name-product_").text($(`#name-${lang}_`).val());
    $(document).on('input', `#name-${lang}_`, function () {
        $("#name-product_").text($(this).val());
    });
}