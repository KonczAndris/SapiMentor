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

    if (confirmPasswordValue !== "" && doPasswordsMatch && isValidFormat) {
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
    } else {
        passwordInput.type = 'password';
    }

    const icon1 = document.getElementById('password-toggle-icon');
    const icon2 = document.getElementById('password-toggle-icon2');
    const passwordToggle = document.querySelector('.password-toggle');

    passwordToggle.classList.toggle('active');

    if (passwordToggle.classList.contains('active')) {
        icon1.classList.add('hidden');
        icon2.classList.remove('hidden');
    } else {
        icon1.classList.remove('hidden');
        icon2.classList.add('hidden');
    }
}

function confirmpasswordToggleVisibility(inputId, iconId) {
    const passwordInput = document.getElementById(inputId);
    const toggleIcon = document.getElementById(iconId);

    if (passwordInput.type === 'password') {
        passwordInput.type = 'text';
    } else {
        passwordInput.type = 'password';
    }

    const icon1 = document.getElementById('confirm-password-toggle-icon');
    const icon2 = document.getElementById('confirm-password-toggle-icon2');
    const passwordToggle = document.querySelector('.confirm-password-toggle');

    passwordToggle.classList.toggle('active');

    if (passwordToggle.classList.contains('active')) {
        icon1.classList.add('hidden');
        icon2.classList.remove('hidden');
    } else {
        icon1.classList.remove('hidden');
        icon2.classList.add('hidden');
    }
}

function setPlaceholdersBasedOnLanguage() {
    const passwordInput = document.getElementById('password');
    const confirmPasswordInput = document.getElementById('confirm-password');
    const changePasswordBtn = document.getElementById('change-password');

    if (localStorage.getItem('language') === 'hungarian') {
        passwordInput.placeholder = 'Jelszó';
        confirmPasswordInput.placeholder = 'Jelszó megerősítése';
        changePasswordBtn.innerText = 'Mentés';
    } else {
        passwordInput.placeholder = 'Password';
        confirmPasswordInput.placeholder = 'Confirm Password';
        changePasswordBtn.innerText = 'Save';
    }
}

setPlaceholdersBasedOnLanguage();
