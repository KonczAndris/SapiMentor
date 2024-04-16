const fs = require('fs');
const path = require('path');
const { JSDOM } = require('jsdom');

const { localSettings  } = require('./../../main/resources/static/js/localSettings.js');

describe('localSettings', () => {
    const localStorageMock = (() => {
        let store = {};
        return {
            getItem: key => store[key],
            setItem: (key, value) => store[key] = value,
            clear: () => store = {}
        };
    })();

    Object.defineProperty(window, 'localStorage', { value: localStorageMock });

    describe('dark theme', () => {
        test('should not append dark theme CSS file to document head if theme is not dark', () => {
            localStorage.setItem('theme', 'light');
            localSettings();

            const linkElement = document.querySelector('link[href="/css/darkTheme.css"]');
            expect(linkElement).toBeNull();
        });

        test('should append dark theme CSS file to document head if theme is dark', () => {
            localStorage.setItem('theme', 'dark');
            localSettings();

            const linkElement = document.querySelector('link[href="/css/darkTheme.css"]');
            expect(linkElement).not.toBeNull();
        });
    });

    // Test language logic
    describe('language', () => {
        test('should hide English texts if language is Hungarian', () => {
            localStorage.setItem('language', 'hungarian');
            document.body.innerHTML = '<div class="english-text"></div>';

            localSettings();

            const englishTextElement = document.querySelector('.english-text');
            expect(englishTextElement.style.display).toBe('none');

        });

        test('should hide Hungarian texts if language is not Hungarian', () => {
            localStorage.setItem('language', 'english');
            document.body.innerHTML = '<div class="hungarian-text"></div>';

            localSettings();

            const hungarianTextElement = document.querySelector('.hungarian-text');
            expect(hungarianTextElement.style.display).toBe('none');
        });
    });
});