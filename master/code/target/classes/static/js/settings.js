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
