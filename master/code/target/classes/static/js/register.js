// Validate First Name
function validateFirstName() {
    var firstNameInput = document.getElementById("firstName");
    var firstNameValue = firstNameInput.value.trim();
    if (!/^[a-zA-ZÁÉÍÓÖŐÚÜŰáéíóöőúüű-]{2,20}$/.test(firstNameValue)) {
        firstNameInput.classList.add("highlight");
    } else {
        firstNameInput.classList.remove("highlight");
    }
}

// Validate Last Name
function validateLastName() {
    var lastNameInput = document.getElementById("lastName");
    var lastNameValue = lastNameInput.value.trim();
    if (!/^[a-zA-ZÁÉÍÓÖŐÚÜŰáéíóöőúüű-]{2,20}$/.test(lastNameValue)) {
        lastNameInput.classList.add("highlight");
    } else {
        lastNameInput.classList.remove("highlight");
    }
}

// Validate Email
function validateEmail() {
    var emailInput = document.getElementById("email");
    var emailValue = emailInput.value.trim();
    if (!emailValue.endsWith("@ms.sapientia.ro") && !emailValue.endsWith("@student.ms.sapientia.ro")) {
        emailInput.classList.add("highlight");
    }
    else {
        emailInput.classList.remove("highlight");
    }
}

// Validate Password
function validatePassword() {
    var passwordInput = document.getElementById("password");
    var passwordValue = passwordInput.value.trim();
    if (passwordValue.length < 8 || passwordValue.length > 20) {
        passwordInput.classList.add("highlight");
    } else {
        passwordInput.classList.remove("highlight");
    }
}

// Full registration method
document.addEventListener("DOMContentLoaded", function () {
    var firstNameInput = document.getElementById("firstName");
    var lastNameInput = document.getElementById("lastName");
    var emailInput = document.getElementById("email");
    var passwordInput = document.getElementById("password");
    var errorName = document.getElementById("invalid-name");
    var errorEmail = document.getElementById("invalid-email");
    var errorPassword = document.getElementById("invalid-password");
    var errorRegistration = document.getElementById("already-registered");

    firstNameInput.addEventListener("input", validateFirstName);
    lastNameInput.addEventListener("input", validateLastName);
    emailInput.addEventListener("input", validateEmail);
    passwordInput.addEventListener("input", validatePassword);

    var registerButton = document.getElementById("register-submit");
    registerButton.addEventListener("click", async function (event) {
        event.preventDefault();
        firstNameInput.classList.remove("highlight");
        lastNameInput.classList.remove("highlight");
        emailInput.classList.remove("highlight");
        passwordInput.classList.remove("highlight");

        validateFirstName();
        validateLastName();
        validateEmail();
        validatePassword();

        await new Promise(resolve => setTimeout(resolve, 1000));
        var hasErrors = false;

        if (firstNameInput.classList.contains("highlight") || lastNameInput.classList.contains("highlight")) {
            errorName.style.display = "block";
            hasErrors = true;
        }
        else{
            errorName.style.display = "none";
        }

        if (emailInput.classList.contains("highlight")) {
            errorEmail.style.display = "block";
            hasErrors = true;
        }
        else{
            errorEmail.style.display = "none";
        }
        if (passwordInput.classList.contains("highlight")) {
            errorPassword.style.display = "block";
            hasErrors = true;
        }
        else{
            errorPassword.style.display = "none";
        }

        if (!hasErrors) {
            var registerForm = document.querySelector("form");
            registerForm.submit();
        }
    });

});

// Jest testing exports
module.exports = {validateFirstName, validateLastName, validateEmail, validatePassword};
