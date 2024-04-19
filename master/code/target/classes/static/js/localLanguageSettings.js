function localLanguageSettings() {
    if (localStorage.getItem('language') === 'hungarian') {
        const englishTexts = document.querySelectorAll('.english-text');
        englishTexts.forEach(text => text.style.display = 'none');
    } else {
        const hungarianTexts = document.querySelectorAll('.hungarian-text');
        hungarianTexts.forEach(text => text.style.display = 'none');
    }
}

localLanguageSettings();

