let context_full_path = window.location.pathname;
let content = [];
let currentMetaData = {
    page: null,
    size: null,
    totalElements: null,
    totalPages: null
};

function generatePageSequence(currentPage, totalPages) {
    const startPage = currentPage > 2 ? currentPage - 2 : 0;
    const endPage = currentPage + 2 < totalPages ? currentPage + 2 : totalPages - 1;
    const pageSequence = [];
    for (let i = startPage; i <= endPage; i++) {
        pageSequence.push(i);
    }
    return pageSequence;
}

function getFilters(filters) {
    let filters_ = {};
    filters.each(function () {
        const key = $(this).attr('name');
        const value = $(this).val();
        if (key) {
            filters_[key] = value;
        }
    });
    return filters_;
}

function loadTable(data) {
    const containerTable = document.getElementById("table-data-container_");
    containerTable.innerHTML = '';
    const noExistText = getCurrentLang() === 'uk' ? 'Дані в таблиці відсутні' : 'No data available in table';
    if (data.length > 0) {
        data.forEach(function (element) {
            containerTable.innerHTML += getRowData(element);
        });
    } else {
        containerTable.innerHTML += `<td colspan="4" style="text-align: center; color: gray; font-style: italic;">
${noExistText}
    </td>`;
    }
}

function getPageWithFilter(page, size) {
    const filterElements = $('.for-filter');
    const filters = getFilters(filterElements);
    const lang = document.documentElement.lang;
    console.log('current lang: ', lang);
    console.log(filters);
    console.log(context_full_path);
    $.ajax({
        type: "GET",
        url: context_full_path + '/data',
        data: {
            page: page,
            size: size,
            languageCode: lang,
            ...filters
        },
        success: function (data) {
            console.log(data);
            content = data.content;
            currentMetaData = data.metadata;
            const containerTable = document.getElementById("table-data-container_");
            containerTable.innerHTML = '';
            if (content.length > 0) {
                content.forEach(function (element) {
                    containerTable.innerHTML += getRowData(element);
                });
            } else {
                const fields = document.querySelectorAll('.fields-entity');
                console.log(fields.length + 2);
                containerTable.innerHTML = `<tr>
                                <td colspan="${fields.length + 2}" style="text-align: center; vertical-align: middle;">
                                No matching records found</td>
                                </tr>`
            }
            updateEntries(currentMetaData.totalElements, currentMetaData.size, currentMetaData.page);
        }
    });
}

function getRowData(element) {
    let row = '<tr>';
    // console.log(element);
    Object.keys(element).forEach(field => {
        const value = element[field] !== null ? element[field] : '';
        row += `<td class="divided-text">${value}</td>`;
    });

    row += '</tr>';
    return row;
}

function handleInputChange(input) {
    if (input.name === 'id') {
        if (/[^0-9]/.test(input.value)) {
            return;
        }
    }
    getPageWithFilter(currentMetaData.page, currentMetaData.size);
}
