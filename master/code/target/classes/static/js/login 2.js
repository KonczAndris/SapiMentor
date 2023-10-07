function validateUsername() {
    var usernameInput = document.getElementById("username");
    var usernameValue = usernameInput.value.trim();

    if (!usernameValue.endsWith("@student.ms.sapientia.ro")) {
        usernameInput.classList.add("highlight");
    } else {
        usernameInput.classList.remove("highlight");
    }
}

function validatePassword() {
    var passwordInput = document.getElementById("password");
    var passwordValue = passwordInput.value.trim();

    if (passwordValue.length < 8 || passwordValue.length > 20){
        passwordInput.classList.add("highlight");
    } else {
        passwordInput.classList.remove("highlight");
    }
}

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
    });

});

