function invokeRequest(inputs, page) {
    console.log(inputs);
    if (Array.isArray(inputs)) {
        const errorContainer = document.getElementById('error-container');

        const validationRules = {
            id: { msg: messages.invalidNumber, reg: /^\d+$/ },
            priceByUnit: { msg: messages.filterProduct, reg: /^\d+$/ },
            totalCount: { msg: messages.filterAmount, reg: /^\d+$/ },
            totalPrice: { msg: messages.filterPrice, reg: /^\d+$/,},
            date: { msg: messages.filterDate, reg: /^(0[1-9]|[12]\d|3[01]).(0[1-9]|1[0-2]).\d{4}$/ },
            phone: { msg: messages.filterPhone, reg: /^(\+38)?\s?\(?\d{3}\)?\s?\d{3}\s?\d{2}\s?\d{2}$/}
        }

        for (const name of inputs) {
            const input = document.querySelector(`[name=${name}]`);
            if (!input) continue;

            let value = input.value;

            if (value === '') {
                input.classList.remove('is-invalid');
                continue;
            }

            if (validationRules[name]) {
                const rule = validationRules[name];

                if (!rule.reg.test(value)) {
                    input.classList.add('is-invalid');
                    if (errorContainer) {
                        errorContainer.innerHTML = `
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            ${rule.msg} <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>`;
                    }
                    clearTimeout(timeoutId);
                    return;
                }
            }
            input.classList.remove('is-invalid');
        }

        if (errorContainer) errorContainer.innerHTML = '';

        clearTimeout(timeoutId);
        timeoutId = setTimeout(() => {
            $("#table-data-container_ tbody").empty();
            $("#table-pagination-container_").empty();
            invokeBlockUI();

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
    let page = $(this).data('page');
    invokeRequest(inputs, page);
});

$(document).ready(function () {
    document.addEventListener('htmx:afterRequest', function (evt) {
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