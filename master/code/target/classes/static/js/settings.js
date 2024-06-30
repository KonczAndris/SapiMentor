'use strict';

let stompClient = null;
let IdForUserInSettingsPage = null;
function connectToWebSocketForSettingsPage() {
    var elementToGetUserIdInSettingsPage = document.querySelector('[id^="userIdForSettingsPage-"]');
    IdForUserInSettingsPage = elementToGetUserIdInSettingsPage.id.split("-")[1];

    if(window.location.href.includes("http://")){
        var socket = new SockJS('/ws');
    }else {
        var socket = new SockJS('https://' + window.location.host + '/ws');
    }
    stompClient = Stomp.over(socket);
    stompClient.connect({}, onConnectedForSettingsPage, onErrorInSettingsPage);
}

function onConnectedForSettingsPage() {
    stompClient.subscribe(`/user/${IdForUserInSettingsPage}/queue/messages`, onMessageReceivedNotificationInSettingsPage)
}

function onErrorInSettingsPage(error) {
}

async function onMessageReceivedNotificationInSettingsPage(payload) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    document.querySelectorAll('.nbr-msg').forEach(item => {
        item.classList.remove('hiddenMsg');
        item.textContent = '';
    });
}

document.addEventListener('DOMContentLoaded', () => {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    connectToWebSocketForSettingsPage();

    var elementToGetProfileUserIdInSettingsPage = document.querySelector('[id^="userIdForSettingsPage-"]');
    var profileUserIdInSettingsPage = elementToGetProfileUserIdInSettingsPage.id.split("-")[1];

    fetch(`/settings/getSettingsProfileNotificationStatus?userId=${profileUserIdInSettingsPage}`, {
        method: "GET",
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'X-CSRF-TOKEN': token
        }
    }).then(response => {
        if (response.ok) {
            return response.text();
        } else {
            throw new Error('Request failed!');
        }
    }).then(data => {
        if (data === "OK") {
            document.querySelectorAll('.nbr-msg').forEach(item => {
                item.classList.remove('hiddenMsg');
                item.textContent = '';
            });
        }
    }).catch((error) => {
        console.log("Error:" + error);
    });
});


function showDeactivateModal() {
    document.getElementById("deactivateModal").style.display = "block";
}

function closeDeactivateModal() {
    document.getElementById("deactivateModal").style.display = "none";
}

function startDeactivatingUser() {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    fetch(`/settings/deactivateAccount`, {
        method: "POST",
        headers: {
            'X-CSRF-TOKEN': token
        }
    }).then(response => {
        if (response.ok) {
            return response.text();
        } else {
            throw new Error('Request failed!');
        }
    }).then(data => {
        if (data === "DELETED") {
            window.location.href = "/login";
        } else {
            window.location.href = "/settings";
        }
    }).catch((error) => {
        window.location.href = "/settings";
        console.log("Error:" + error);
    });
}

document.addEventListener("DOMContentLoaded", function () {
    var spanElements = document.querySelectorAll('.checkmark-setting');
    spanElements.forEach(function(span) {
        span.addEventListener('click', function () {
            var checkboxId = this.previousElementSibling.id;
            var checkboxElement = document.getElementById(checkboxId);
            checkboxElement.click();
        });
    });
});

const languageCheckboxes = document.querySelectorAll('.language-div .topic-checkbox');
const themeCheckboxes = document.querySelectorAll('.theme-div .topic-checkbox');

function handleCheckboxCheck(checkboxes) {
    checkboxes.forEach(checkbox => {
        checkbox.addEventListener('change', function() {
            if (this.checked) {
                checkboxes.forEach(otherCheckbox => {
                    if (otherCheckbox !== this) {
                        otherCheckbox.checked = false;
                    }
                });
            } else {
                const checkedCount = Array.from(checkboxes).filter(checkbox => checkbox.checked).length;
                if (checkedCount === 0) {
                    this.checked = true;
                }
            }
        });
    });
}

handleCheckboxCheck(languageCheckboxes);
handleCheckboxCheck(themeCheckboxes);

const darkCheckbox = document.getElementById('dark');
const saveSettingsButton = document.getElementById('saveSettings');

function applyDarkTheme() {
    if (darkCheckbox.checked) {
        const darkThemeLink = document.createElement('link');
        darkThemeLink.rel = 'stylesheet';
        darkThemeLink.href = '/css/darkTheme.css';
        darkThemeLink.id = 'darkTheme';
        document.head.appendChild(darkThemeLink);
        localStorage.setItem('theme', 'dark');
    } else {
        const darkThemeLink = document.getElementById('darkTheme');
        if (darkThemeLink) {
            darkThemeLink.remove();
        }
        localStorage.removeItem('theme');
    }
}

document.addEventListener('DOMContentLoaded', function() {
    const lightCheckbox = document.getElementById('light');
if (localStorage.getItem('theme') === 'dark') {
    darkCheckbox.checked = true;
    applyDarkTheme();
}
else{
 lightCheckbox.checked = true;
}
    if (localStorage.getItem('theme') === 'dark') {
        darkCheckbox.checked = true;
        applyDarkTheme();
    } else {
        lightCheckbox.checked = true;
    }
});

const hungarianCheckbox = document.getElementById('hungarian');
const englishCheckbox = document.getElementById('english');

function applyLanguageSettings() {
    const hungarianTextElements = document.querySelectorAll('.hungarian-text');
    const englishTextElements = document.querySelectorAll('.english-text');

    if (hungarianCheckbox.checked) {
        hungarianTextElements.forEach(element => {
            element.style.display = 'inline';
        });
        englishTextElements.forEach(element => {
            element.style.display = 'none';
        });
        // Save language preference locally
        localStorage.setItem('language', 'hungarian');
    } else {
        hungarianTextElements.forEach(element => {
            element.style.display = 'none';
        });
        englishTextElements.forEach(element => {
            element.style.display = 'inline';
        });
        localStorage.setItem('language', 'english');
    }
}

document.addEventListener('DOMContentLoaded', function() {
    const savedLanguage = localStorage.getItem('language');
if (savedLanguage === 'hungarian') {
    hungarianCheckbox.checked = true;
} else {
    englishCheckbox.checked = true;
} });

function showPreLoadModal() {
    var modal = document.getElementById("preloader");
    modal.style.display = "block";
}

document.addEventListener('DOMContentLoaded', function() {
    saveSettingsButton.addEventListener('click', function() {
        applyDarkTheme();
        applyLanguageSettings();
        if (localStorage.getItem('theme') === 'dark') {
            showPreLoadModal();
        }
        location.reload();
    });
} );

function setPlaceholdersBasedOnLanguage() {
    document.addEventListener('DOMContentLoaded', function() {
    const nameInput = document.getElementById('name');
    const emailInput = document.getElementById('email');
    const messageTextarea = document.getElementById('message');

    if (localStorage.getItem('language') === 'hungarian') {
        nameInput.placeholder = 'Teljes név';
        emailInput.placeholder = 'Email cím';
        messageTextarea.placeholder = 'Üzenet';
    } else {
        nameInput.placeholder = 'Your Name';
        emailInput.placeholder = 'Your Email';
        messageTextarea.placeholder = 'Your Message';
    } });
}

setPlaceholdersBasedOnLanguage();

// Jest testing exports
module.exports = {onMessageReceivedNotificationInSettingsPage,
    applyDarkTheme,
    applyLanguageSettings };