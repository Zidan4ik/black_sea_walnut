function addEventListenerCounter() {
    $(`[data-name="arrow-left"], [data-name="arrow-right"]`).off("click").on("click", function () {
        const isRight = $(this).data("name") === "arrow-right";
        const $parent = $(this).parent();
        const $input = $parent.find(`[data-name="amount"]`);
        let amount = parseInt($input.val(), 10) || 0;
        if (isRight) {
            amount += 1;
        } else {
            amount = amount > 1 ? amount - 1 : 1;
        }
        $(`[data-name="amount"]`).val(amount).attr('value', amount)
    });
}

function addEventListenerNameManager() {
    $(document).on('input', `[data-sync="name"]`, function () {
        let value = $(this).val();
        const $container = $(this).closest(`div.tab-pane`);
        const $h4 = $container.find('[data-name="name-product"]');
        $h4.text(value);
    });
}

function addEventListenerSyncFields(selectors) {
    $(document).on('input change', selectors, function () {
        const $input = $(this);
        const dataName = $input.data('name');

        if (!dataName) return;

        const $allElements = $(`[data-name="${dataName}"]`);
        const val = $input.val();

        if ($input.is(':checkbox')) {
            $allElements.prop('checked', this.checked);
        } else {
            $allElements.val(val);

            const isValid = /^\d+$/.test(val) || val === '';

            $allElements.each(function () {
                const $el = $(this);
                const $parent = $el.parent();

                const $targetContainer = ($parent.hasClass('input-group-merge') || $parent.hasClass('amount-block')) ? $parent.parent() : $parent;
                if (isValid) {
                    $targetContainer.find('.error-message').remove();
                } else {
                    if ($targetContainer.find('.error-message').length === 0) {
                        const errorHtml = `
                           <small class="error-message text-danger fw-bold mt-1 d-block">
                                <i class="bi bi-exclamation-circle me-1"></i> ${messages.invalidNumber}
                            </small>`;
                        $targetContainer.append(errorHtml);
                    }
                }
            });
        }
    });
}