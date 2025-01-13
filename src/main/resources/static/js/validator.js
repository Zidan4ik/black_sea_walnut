function clearValid() {
    document.querySelectorAll(".error-message").forEach(function (element) {
        element.remove();
    });
    document.querySelectorAll(".errorMy").forEach(function (element) {
        element.classList.remove("errorMy");
    });
}

function validate(data) {
    Object.entries(data).forEach(function ([field, message]) {
        // console.log("Field:", field, "Message:", message);
        const inputField = document.getElementById(field + `_`);
        if (inputField) {
            inputField.classList.add("errorMy");
            let errorMessage = document.createElement("span");
            errorMessage.className = "error-message";
            errorMessage.style.color = "red";
            errorMessage.innerText = message;
            if (inputField.id === 'dateOfPublication_') {
                errorMessage.style.display = "block";
                inputField.parentNode.parentNode.appendChild(errorMessage);
            } else if (inputField.id === 'file_') {
                const imgElement = document.getElementById('img_');
                imgElement.parentNode.append(errorMessage);
            } else {
                inputField.parentNode.appendChild(errorMessage);
            }
        }
    });
}

function validate2(data) {
    Object.entries(data).forEach(function ([field, message]) {
        const inputField = document.querySelector(`[data-name=${field}]`);
        console.log(inputField);
        if (inputField) {
            // console.log("Field:", field, "Message:", message);

            inputField.classList.add("errorMy");
            let errorMessage = document.createElement("span");
            errorMessage.className = "error-message";
            errorMessage.style.color = "red";
            errorMessage.innerText = message;
            if (inputField.getAttribute("class")==="files") {
                inputField.parentNode.append(errorMessage);
            } else if(inputField.id === 'amount_'){
                let parentDiv = inputField.closest('div');
                if(parentDiv)
                    parentDiv.appendChild(errorMessage);
            }
            else {
                inputField.parentNode.appendChild(errorMessage);
            }
        }
    });
}
