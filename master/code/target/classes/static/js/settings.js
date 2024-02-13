function showDeactivateModal() {
    document.getElementById("deactivateModal").style.display = "block";
}

function closeDeactivateModal() {
    document.getElementById("deactivateModal").style.display = "none";
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

// Event listener for the save button
saveSettingsButton.addEventListener('click', function() {
    applyDarkTheme();
    location.reload(); // Reload the page
});

const lightCheckbox = document.getElementById('light');
// Check if dark theme was selected previously and apply it
if (localStorage.getItem('theme') === 'dark') {
    darkCheckbox.checked = true;
    applyDarkTheme();
}

else{
 lightCheckbox.checked = true;
}