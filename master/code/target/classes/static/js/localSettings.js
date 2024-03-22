 if (localStorage.getItem('theme') === 'dark') {
    const darkThemeLink = document.createElement('link');
    darkThemeLink.rel = 'stylesheet';
    darkThemeLink.href = '/css/darkTheme.css';
    document.head.appendChild(darkThemeLink);
}

if (localStorage.getItem('language') === 'hungarian') {
    const englishTexts = document.querySelectorAll('.english-text');
    englishTexts.forEach(text => text.style.display = 'none');
}
    else{
    const hungarianTexts = document.querySelectorAll('.hungarian-text');
    hungarianTexts.forEach(text => text.style.display = 'none');
}
