'use strict';

let stompClient = null;
let IdForUserInSettingsPage = null;
function connectToWebSocketForSettingsPage() {
    var elementToGetUserIdInSettingsPage = document.querySelector('[id^="userIdForSettingsPage-"]');
    IdForUserInSettingsPage = elementToGetUserIdInSettingsPage.id.split("-")[1];

    if(window.location.href.includes("http://")){
        var socket = new SockJS('/ws');
        console.log("sima ws-t hasznal");
    }else {
        var socket = new SockJS('https://' + window.location.host + '/ws');
        console.log("wss-t hasznal");
    }
    stompClient = Stomp.over(socket);

    stompClient.connect({}, onConnectedForSettingsPage, onErrorInSettingsPage);
}

function onConnectedForSettingsPage() {
    stompClient.subscribe(`/user/${IdForUserInSettingsPage}/queue/messages`, onMessageReceivedNotificationInSettingsPage)
}

function onErrorInSettingsPage(error) {
    //connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    //connectingElement.style.color = 'red';
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
    // Select all span elements with class checkmark-setting
    var spanElements = document.querySelectorAll('.checkmark-setting');

    // Iterate over each span element
    spanElements.forEach(function(span) {
        // Add click event listener to the span element
        span.addEventListener('click', function () {
            // Get the associated checkbox element
            var checkboxId = this.previousElementSibling.id;
            var checkboxElement = document.getElementById(checkboxId);

            // Simulate a click on the checkbox element
            checkboxElement.click();
        });
    });
});

// Get all checkboxes within the language and theme divs
const languageCheckboxes = document.querySelectorAll('.language-div .topic-checkbox');
const themeCheckboxes = document.querySelectorAll('.theme-div .topic-checkbox');

// Function to handle checking only one checkbox within a category
// Function to handle checking only one checkbox within a category
function handleCheckboxCheck(checkboxes) {
    checkboxes.forEach(checkbox => {
        checkbox.addEventListener('change', function() {
            if (this.checked) {
                // Uncheck other checkboxes within the same category
                checkboxes.forEach(otherCheckbox => {
                    if (otherCheckbox !== this) {
                        otherCheckbox.checked = false;
                    }
                });
            } else {
                // If only one checkbox is checked, prevent unchecking it
                const checkedCount = Array.from(checkboxes).filter(checkbox => checkbox.checked).length;
                if (checkedCount === 0) {
                    this.checked = true;
                }
            }
        });
    });
}

// Call the function for language and theme checkboxes
handleCheckboxCheck(languageCheckboxes);
handleCheckboxCheck(themeCheckboxes);

const darkCheckbox = document.getElementById('dark');
const saveSettingsButton = document.getElementById('saveSettings');

// Function to apply or remove the dark theme
function applyDarkTheme() {
    if (darkCheckbox.checked) {
        const darkThemeLink = document.createElement('link');
        darkThemeLink.rel = 'stylesheet';
        darkThemeLink.href = '/css/darkTheme.css';
        darkThemeLink.id = 'darkTheme'; // Add an ID to the link element for easier removal later
        document.head.appendChild(darkThemeLink);
        // Save dark theme state locally
        localStorage.setItem('theme', 'dark');
    } else {
        const darkThemeLink = document.getElementById('darkTheme');
        if (darkThemeLink) {
            darkThemeLink.remove(); // Remove the dark theme CSS
        }
        // Remove dark theme state from local storage
        localStorage.removeItem('theme');
    }
}

const lightCheckbox = document.getElementById('light');
// Check if dark theme was selected previously and apply it
if (localStorage.getItem('theme') === 'dark') {
    darkCheckbox.checked = true;
    applyDarkTheme();
}
else{
 lightCheckbox.checked = true;
}

const hungarianCheckbox = document.getElementById('hungarian');
const englishCheckbox = document.getElementById('english');

// Function to apply language settings
function applyLanguageSettings() {
    const hungarianTextElements = document.querySelectorAll('.hungarian-text');
    const englishTextElements = document.querySelectorAll('.english-text');

    if (hungarianCheckbox.checked) {
        hungarianTextElements.forEach(element => {
            element.style.display = 'inline'; // Show Hungarian text
        });
        englishTextElements.forEach(element => {
            element.style.display = 'none'; // Hide English text
        });
        // Save language preference locally
        localStorage.setItem('language', 'hungarian');
    } else {
        hungarianTextElements.forEach(element => {
            element.style.display = 'none'; // Hide Hungarian text
        });
        englishTextElements.forEach(element => {
            element.style.display = 'inline'; // Show English text
        });
        // Save language preference locally
        localStorage.setItem('language', 'english');
    }
}

// Check if language preference was selected previously and apply it
const savedLanguage = localStorage.getItem('language');
if (savedLanguage === 'hungarian') {
    hungarianCheckbox.checked = true;
} else {
    englishCheckbox.checked = true;
}

// Event listener for the save button
saveSettingsButton.addEventListener('click', function() {
    applyDarkTheme();
    applyLanguageSettings();
    location.reload(); // Reload the page
});

function setPlaceholdersBasedOnLanguage() {
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
    }
}

// Hívjuk meg a függvényt az oldal betöltésekor
setPlaceholdersBasedOnLanguage();
