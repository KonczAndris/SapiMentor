const fs = require('fs');
const path = require('path');

const htmlFilePath = path.resolve(__dirname, './../../main/resources/templates/profile.html');
const htmlContent = fs.readFileSync(htmlFilePath, 'utf-8');

const { JSDOM } = require('jsdom');
const { window } = new JSDOM(htmlContent);

const { setupModal, setupAboutModal } = require('./../../main/resources/static/js/functions.js');
require('./../../main/resources/static/js/functions.js');

describe('Profile page modals', () => {
    test('Modals default hidden state', () => {
        const { document } = window;
        const aboutModal = document.querySelector('#myModal');
        const skillsModal = document.querySelector('#skillsModal');
        const mentorModal = document.querySelector('#mentorModal');
        const uploadModal = document.querySelector('#uploadModal');

        expect(aboutModal).toBeTruthy();
        expect(skillsModal).toBeTruthy();
        expect(mentorModal).toBeTruthy();
        expect(uploadModal).toBeTruthy();

        const aboutModalDisplay = window.getComputedStyle(aboutModal).getPropertyValue('display');
        const skillsModalDisplay = window.getComputedStyle(skillsModal).getPropertyValue('display');
        const mentorModalDisplay = window.getComputedStyle(mentorModal).getPropertyValue('display');
        const uploadModalDisplay = window.getComputedStyle(uploadModal).getPropertyValue('display');

        expect(aboutModalDisplay).toBe('none');
        expect(skillsModalDisplay).toBe('none');
        expect(mentorModalDisplay).toBe('none');
        expect(uploadModalDisplay).toBe('none');
    });

    test('About modal functionality', () => {
        const { document } = window;
        const aboutModal = document.querySelector('#myModal');
        const aboutButton = document.querySelector('#myBtn');

        aboutButton.click();

        const aboutModalDisplay = window.getComputedStyle(aboutModal).getPropertyValue('display');
        expect(aboutModalDisplay).toBe('block');
    });
});

