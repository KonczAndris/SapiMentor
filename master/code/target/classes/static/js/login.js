// Validate Email
function validateUsername() {
    var usernameInput = document.getElementById("username");
    var usernameValue = usernameInput.value.trim();

    if (!usernameValue.endsWith("@ms.sapientia.ro") && !usernameValue.endsWith("@student.ms.sapientia.ro")
    && !usernameValue.endsWith("@admin.com")) {
        usernameInput.classList.add("highlight");
    }
    else {
        usernameInput.classList.remove("highlight");
    }
}

// Validate Password
function validatePassword() {
    var passwordInput = document.getElementById("password");
    var passwordValue = passwordInput.value.trim();

    if (passwordValue.length < 8 || passwordValue.length > 20){
        passwordInput.classList.add("highlight");
    } else {
        passwordInput.classList.remove("highlight");
    }
}

// Password visibility toggle
function passwordToggleVisibilityForLogin(inputId, iconId) {
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

// XMLHttpRequest for Checking Authentication
function performXMLHttpRequest() {
    var xhr = new XMLHttpRequest();
    xhr.open("GET", "/check-authentication", true);
    xhr.onload = function () {
        if (xhr.status === 200) {
            var isRedirected = JSON.parse(xhr.responseText).redirected;
            if (!isRedirected) {
                errorMessage.style.display = "block";
                event.stopPropagation();
            } else {
                errorMessage.style.display = "none";
                loginForm.submit();
            }
        }
    };
    xhr.send();
}

// Login form validation
document.addEventListener("DOMContentLoaded", function () {
    var usernameInput = document.getElementById("username");
    var passwordInput = document.getElementById("password");
    var errorMessage = document.getElementById("error-message"); // Added this line

    usernameInput.addEventListener("input", validateUsername);
    passwordInput.addEventListener("input", validatePassword);

    var loginButton = document.getElementById("login-submit");
    loginButton.addEventListener("click", async function (event) {
        event.preventDefault();
        usernameInput.classList.remove("highlight");
        passwordInput.classList.remove("highlight");

        validateUsername();
        validatePassword();
        await new Promise(resolve => setTimeout(resolve, 1000));

        if (usernameInput.classList.contains("highlight") || passwordInput.classList.contains("highlight")) {
            errorMessage.style.display = "block";
        } else {
            errorMessage.style.display = "none";
            var loginForm = document.querySelector("form");
            loginForm.submit();
        }
    });

    loginButton.addEventListener("click", function (event) {
        event.preventDefault();
        usernameInput.classList.remove("highlight");
        passwordInput.classList.remove("highlight");

        validateUsername();
        validatePassword();

        performXMLHttpRequest();
    });
});

// Language placeholder text
function setPlaceholdersBasedOnLanguage() {
    document.addEventListener('DOMContentLoaded', function() {
    const usernameInput = document.getElementById('username');
    const passwordInput = document.getElementById('password');
    const loginButton = document.getElementById('login-submit');

    if (localStorage.getItem('language') === 'hungarian') {
        usernameInput.placeholder = 'Email cím';
        passwordInput.placeholder = 'Jelszó';
        loginButton.value = 'Bejelentkezés';
    } else {
        usernameInput.placeholder = 'Enter Email';
        passwordInput.placeholder = 'Enter Password';
        loginButton.value = 'Login';
    }
    });
}

setPlaceholdersBasedOnLanguage();

// Jest testing exports
module.exports = {validateUsername, validatePassword, performXMLHttpRequest};
