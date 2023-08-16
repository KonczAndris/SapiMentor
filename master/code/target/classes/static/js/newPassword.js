function validatePassword() {
    var passwordInput = document.getElementById("password");
    var passwordValue = passwordInput.value.trim();

    if (passwordValue.length < 8 || passwordValue.length > 20) {
        passwordInput.classList.add("highlight");
    } else {
        passwordInput.classList.remove("highlight");
    }
}

function validateConfirmPassword() {
    var passwordInput = document.getElementById("password");
    var confirmPasswordInput = document.getElementById("confirm-password");
    
    var passwordValue = passwordInput.value.trim();
    var confirmPasswordValue = confirmPasswordInput.value.trim();

    var isValidFormat = passwordValue.length >= 8 && passwordValue.length <= 20;
    var doPasswordsMatch = passwordValue === confirmPasswordValue;

    if (isValidFormat && doPasswordsMatch) {
        confirmPasswordInput.classList.remove("highlight");
    } else {
        confirmPasswordInput.classList.add("highlight");
    }
}

function passwordToggleVisibility(inputId, iconId) {
    const passwordInput = document.getElementById(inputId);
    const toggleIcon = document.getElementById(iconId);

    if (passwordInput.type === 'password') {
        passwordInput.type = 'text';
        toggleIcon.src = /*[[@{/img/eye-slash.png}]]*/ '';
    } else {
        passwordInput.type = 'password';
        toggleIcon.src = /*[[@{/img/eye.png}]]*/ '';
    }
}

