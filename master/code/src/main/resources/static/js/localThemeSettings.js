function localThemeSettings() {
    if (localStorage.getItem('theme') === 'dark') {
        const darkThemeLink = document.createElement('link');
        darkThemeLink.rel = 'stylesheet';
        darkThemeLink.type = 'text/css';
        darkThemeLink.href = '/css/darkTheme.css';
        document.head.appendChild(darkThemeLink);
    }
}
    localThemeSettings();

if (typeof module !== 'undefined'){
    module.exports = {localSettings: localThemeSettings};
}

