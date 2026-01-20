let lastSerializedData = "";

function invokeRequest(inputs, page) {
    if (Array.isArray(inputs)) {
        const errorContainer = document.getElementById('error-container');

        const validationRules = {
            id: {msg: messages.invalidNumber, reg: /^\d+$/},
            priceByUnit: {msg: messages.filterProduct, reg: /^\d+$/},
            totalCount: {msg: messages.filterAmount, reg: /^\d+$/},
            totalPrice: {msg: messages.filterPrice, reg: /^\d+$/,},
            date: {msg: messages.filterDate, reg: /^(0[1-9]|[12]\d|3[01]).(0[1-9]|1[0-2]).\d{4}$/},
            phone: {msg: messages.filterPhone, reg: /^(\+38)?\s?\(?\d{3}\)?\s?\d{3}\s?\d{2}\s?\d{2}$/}
        }

        let hasError = false;
        let currentData = {};

        for (const name of inputs) {
            const input = document.querySelector(`[name=${name}]`);
            if (!input) continue;

            input.classList.remove('is-invalid');
            let value = input.value;
            currentData[name] = value;

            if (value !== '' && validationRules[name]) {
                const rule = validationRules[name];

                if (!rule.reg.test(value)) {
                    input.classList.add('is-invalid');
                    hasError = true;
                    errorContainer.innerHTML = `
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            ${rule.msg} <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>`;
                    clearTimeout(timeoutId);
                    return;
                }
            }
            input.classList.remove('is-invalid');
        }

        if (hasError) {
            clearTimeout(timeoutId);
            return;
        }

        const currentSerialized = JSON.stringify(currentData) + page + $('[name=size]').val();
        if (currentSerialized === lastSerializedData) {
            return;
        }
        lastSerializedData = currentSerialized;

        if (errorContainer) errorContainer.innerHTML = '';

        invokeBlockUI();
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
        }, 500);
    }
}

$(document).on('click', '.pagination a', function () {
    let $li = $(this).parent('li');
    const isActive = $li.hasClass('active');
    const page = $(this).data('page');

    if (!isActive) {
        invokeRequest(inputs, page);
    }
});

$(document).on("change", ".select-card-block-spinner", function () {
    invokeBlockUI();
});

$(document).ready(function () {
    document.addEventListener('htmx:afterRequest', function (evt) {
        console.log(evt.detail.target.id);
        if (evt.detail.target.id === 'table-data-container_' ||
            evt.detail.target.id === 'table-pagination-container_') {
            $("#card-block").unblock();
        }
    });
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