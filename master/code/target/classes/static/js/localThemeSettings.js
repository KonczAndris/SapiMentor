function localThemeSettings() {
    if (localStorage.getItem('theme') === 'dark') {
        // Create a link element for the dark theme CSS file
        const darkThemeLink = document.createElement('link');
        darkThemeLink.rel = 'stylesheet';
        darkThemeLink.type = 'text/css';
        darkThemeLink.href = '/css/darkTheme.css';

        // Append the dark theme CSS file to the document head
        document.head.appendChild(darkThemeLink);
    }
}

    localThemeSettings();

// Export the function
module.exports = {localSettings: localThemeSettings};
