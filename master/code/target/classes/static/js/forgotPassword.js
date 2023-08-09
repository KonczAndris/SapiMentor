function validateEmail() {
    var emailInput = document.getElementById("email");
    var emailValue = emailInput.value.trim();

    if (!emailValue.endsWith("@student.ms.sapientia.ro")) {
        emailInput.classList.add("highlight");
    } else {
        emailInput.classList.remove("highlight");
    }
}

document.addEventListener("DOMContentLoaded", function () {
    var emailInput = document.getElementById("email");
    var errorEmail = document.getElementById("invalid-email");

    emailInput.addEventListener("input", validateEmail);

    var sendEmailButton = document.getElementById("send-email");
    sendEmailButton.addEventListener("click", async function (event) {
        event.preventDefault();
        emailInput.classList.remove("highlight");

        validateEmail();

        await new Promise(resolve => setTimeout(resolve, 1000));

        if (emailInput.classList.contains("highlight")) {
            errorEmail.style.display = "block";
        }
        else{
            var forgotPasswordForm = document.querySelector("form");
            forgotPasswordForm.submit();
        }
    });
});