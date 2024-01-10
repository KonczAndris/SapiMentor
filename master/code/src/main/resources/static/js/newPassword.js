function validatePassword() {
    var passwordInput = document.getElementById("password");
    var passwordValue = passwordInput.value.trim();
    var confirmPasswordInput = document.getElementById("confirm-password");
    var confirmPasswordValue = confirmPasswordInput.value.trim();
    var changePasswordBtn = document.getElementById("change-password");

    var isValidFormat = passwordValue.length >= 8 && passwordValue.length <= 20;
    var doPasswordsMatch = passwordValue === confirmPasswordValue;


    if (isValidFormat ) {
        passwordInput.classList.remove("highlight");

    } else  {
        passwordInput.classList.add("highlight");
    }

    if (confirmPasswordValue !== "" && doPasswordsMatch) {
        confirmPasswordInput.classList.remove("highlight");
        changePasswordBtn.disabled = !doPasswordsMatch;
    } else if (confirmPasswordValue === "") {
        confirmPasswordInput.classList.remove("highlight");
    } else {
        confirmPasswordInput.classList.add("highlight");
        changePasswordBtn.disabled = true;
    }

}

function validateConfirmPassword() {
    var passwordInput = document.getElementById("password");
    var confirmPasswordInput = document.getElementById("confirm-password");
    var changePasswordBtn = document.getElementById("change-password");
    
    var passwordValue = passwordInput.value.trim();
    var confirmPasswordValue = confirmPasswordInput.value.trim();

    var isValidFormat = passwordValue.length >= 8 && passwordValue.length <= 20;
    var doPasswordsMatch = passwordValue === confirmPasswordValue;

    if (isValidFormat && doPasswordsMatch) {
        confirmPasswordInput.classList.remove("highlight");
        changePasswordBtn.disabled = !doPasswordsMatch;
    } else {
        confirmPasswordInput.classList.add("highlight");
        changePasswordBtn.disabled = true;
    }
}

function passwordToggleVisibility(inputId, iconId) {
    const passwordInput = document.getElementById(inputId);
    const toggleIcon = document.getElementById(iconId);

    if (passwordInput.type === 'password') {
        passwordInput.type = 'text';
        toggleIcon.src = '/img/eye-slash.png';
    } else {
        passwordInput.type = 'password';
        toggleIcon.src = '/img/eye.png';
    }
}

