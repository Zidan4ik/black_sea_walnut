function clearValid() {
    document.querySelectorAll(".error-message").forEach(function (element) {
        element.remove();
    });
    document.querySelectorAll(".errorMy").forEach(function (element) {
        element.classList.remove("errorMy");
    });
}

function validate2(data) {
    Object.entries(data).forEach(function ([field, message]) {
        const inputFields = document.querySelectorAll(`[data-name="${field}"]`);
        if (inputFields.length > 0) {
            console.log("Field:", field, "Message:", message);

            inputFields.forEach(inputField => {
                inputField.classList.add("errorMy");
                let errorMessage = document.createElement("small");
                errorMessage.className = "error-message text-danger fw-bold mt-1 d-block";
                errorMessage.innerText = message;

                const parent = inputField.parentElement;
                if (parent.classList.contains('input-group-merge')) {
                    parent.parentElement.appendChild(errorMessage);
                } else if (inputField.classList.contains("files")) {
                    parent.append(errorMessage);
                } else if (field === 'amount') {
                    const amountBlock = inputField.closest('.error-message-server') || parent;
                    amountBlock.appendChild(errorMessage);
                } else {
                    parent.appendChild(errorMessage);
                }
            });
        }
    });
}

function validate3(data) {
    Object.entries(data).forEach(function ([field, message]) {
        const inputFields = document.querySelectorAll(`[data-error="${field}"]`);
        if (inputFields.length > 0) {
            inputFields.forEach(inputField => {
                inputField.classList.add("errorMy");
                let errorMessage = document.createElement("small");
                errorMessage.className = "error-message text-danger fw-bold mt-1 d-block";
                errorMessage.innerText = message;

                const parent = inputField.parentElement;
                if (inputField.classList.contains("files")) {
                    parent.append(errorMessage);
                } else {
                    parent.appendChild(errorMessage);
                }
            });
        }
    });
}

function validate4(data) {
    Object.entries(data).forEach(function ([field, message]) {
        const inputFields = document.querySelectorAll(`[data-error="${field}"]`);
        inputFields.forEach((inputField) => {
            inputField.classList.add("errorMy");

            let div = document.createElement("div");
            div.style.marginBottom = '10px';
            let errorMessage = document.createElement("span");
            errorMessage.className = "error-message";
            errorMessage.style.fontFamily = 'monospace';
            errorMessage.style.color = "red";
            errorMessage.innerText = message;
            div.appendChild(errorMessage);
            if (inputField.getAttribute("class") === "files") {
                inputField.parentNode.append(errorMessage);
            } else {
                inputField.after(div);
            }
        });
    });
}