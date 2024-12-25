function invokeRequest(inputs, page) {
    if (Array.isArray(inputs)) {
        $("#table-data-container_ tbody").empty();
        $("#table-pagination-container_").empty();
        clearTimeout(timeoutId);
        timeoutId = setTimeout(() => {
            const params = new URLSearchParams();

            inputs.forEach((name) => {
                const input = document.querySelector(`[name=${name}]`);
                if (input) {
                    params.append(name, input.value);
                }
            });
            console.log(page);
            params.append('page', page);
            params.append('languageCode', $('[name=languageCode]').val());
            params.append('size', $('[name=size]').val());

            htmx.ajax('GET', `${pathToTable}?${params.toString()}`, {
                target: '#table-data-container_',
                swap: 'beforeend'
            });
            htmx.ajax('GET', `${pathToPagination}?${params.toString()}`, {
                target: '#table-pagination-container_',
                swap: 'innerHTML'
            });
        }, 1000);
    }
}

$(document).on('click', '.pagination a', function () {
    let page = $(this).data('page');
    invokeRequest(inputs, page);
});

$(document).ready(function () {
    invokeRequest(inputs, 0);
});

document.addEventListener('input', (event) => {
    const target = event.target;
    if (inputs.includes(target.name)) {
        invokeRequest(inputs, 0);
    }
});

$("[name=size]").on('change', function () {
    invokeRequest(inputs, 0);
});
