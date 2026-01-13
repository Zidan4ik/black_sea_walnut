function invokeRequest(inputs, page) {
    if (Array.isArray(inputs)) {
        const errorContainer = document.getElementById('error-container');

        for (const name of inputs) {
            const input = document.querySelector(`[name=${name}]`);
            if (input) {
                let value = input.value;

                if (name === 'id' && value !== '' && !/^\d+$/.test(value)) {
                    input.classList.add('is-invalid');
                    if (errorContainer) {
                        errorContainer.innerHTML = `
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            ${messages.invalidNumber} <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>`;
                    }
                    clearTimeout(timeoutId);
                    return;
                }
                input.classList.remove('is-invalid');
            }
        }
        if (errorContainer) errorContainer.innerHTML = '';

        clearTimeout(timeoutId);
        timeoutId = setTimeout(() => {
            $("#table-data-container_ tbody").empty();
            $("#table-pagination-container_").empty();

            const params = new URLSearchParams();
            for (const name of inputs) {
                const input = document.querySelector(`[name=${name}]`);
                if (input) params.append(name, input.value);
            }

            params.append('page', page);
            params.append('languageCode', document.documentElement.lang);
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