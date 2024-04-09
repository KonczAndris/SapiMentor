function localSettings() {
    if (localStorage.getItem('theme') === 'dark') {
        // Create a link element for the dark theme CSS file
        const darkThemeLink = document.createElement('link');
        darkThemeLink.rel = 'stylesheet';
        darkThemeLink.type = 'text/css';
        darkThemeLink.href = '/css/darkTheme.css';

        // Append the dark theme CSS file to the document head
        document.head.appendChild(darkThemeLink);
    }

    if (localStorage.getItem('language') === 'hungarian') {
        const englishTexts = document.querySelectorAll('.english-text');
        englishTexts.forEach(text => text.style.display = 'none');
    } else {
        const hungarianTexts = document.querySelectorAll('.hungarian-text');
        hungarianTexts.forEach(text => text.style.display = 'none');
    }
}

    localSettings();

// Export the function
module.exports = {localSettings};
