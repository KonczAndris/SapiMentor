const fs = require('fs');
const path = require('path');

const htmlFilePath = path.resolve(__dirname, './../../main/resources/templates/profile.html');
const htmlContent = fs.readFileSync(htmlFilePath, 'utf-8');

const { JSDOM } = require('jsdom');
const { window } = new JSDOM(htmlContent);

const { setupModal, setupMentorModal, setupUploadModal,
    setupSkillsModal, validatePhone, validateFirstName,
    validateLastName, validateYear, validateSpecialization, toggleDropdown,
    checkValidationAndSetOpacity } = require('./../../main/resources/static/js/functions.js');
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

    describe('Modal Functionality', () => {
        let htmlContent;

        beforeAll(() => {
            const htmlFilePath = path.resolve(__dirname, './../../main/resources/templates/profile.html');
            htmlContent = fs.readFileSync(htmlFilePath, 'utf-8');
        });

        beforeEach(() => {
            document.body.innerHTML = htmlContent;
            setupModal();
            setupMentorModal();
            setupUploadModal();
            setupSkillsModal();
        });

        test('Profile page modal displaying', () => {
            const uploadModalBtn = document.getElementById('change-image-icon');
            const uploadModal = document.getElementById('uploadModal');
            const mentorModalBtn = document.getElementById('myMentorBtn');
            const mentorModal = document.getElementById('mentorModal');
            const aboutModalBtn = document.getElementById('myBtn');
            const aboutModal = document.getElementById('myModal');
            const skillsModalBtn = document.getElementById('mySkillBtn');
            const skillsModal = document.getElementById('skillsModal');

            uploadModalBtn.click();
            expect(uploadModal.style.display).toBe('flex');
            mentorModalBtn.click();
            expect(mentorModal.style.display).toBe('flex');
            aboutModalBtn.click();
            expect(aboutModal.style.display).toBe('block');
            skillsModalBtn.click();
            expect(skillsModal.style.display).toBe('flex');
        });

        test('About Modal Validation and Functionality Testing', () => {
            const firstNameInput = document.getElementById("firstName-edit");
            const lastNameInput = document.getElementById("lastName-edit");
            const specializationInput = document.getElementById("specialization-edit");
            const yearInput = document.getElementById("year-edit");
            const phoneInput = document.getElementById("phone-edit");
            const editSaveButton = document.getElementById("edit-save");

            // Invalid Inputs
            firstNameInput.value = "123";
            validateFirstName();
            expect(firstNameInput.classList.contains("highlight")).toBeTruthy();

            lastNameInput.value = "Doe@";
            validateLastName();
            expect(lastNameInput.classList.contains("highlight")).toBeTruthy();

            specializationInput.value = "Math123";
            validateSpecialization();
            expect(specializationInput.classList.contains("highlight")).toBeTruthy();

            yearInput.value = "5";
            validateYear();
            expect(yearInput.classList.contains("highlight")).toBeTruthy();

            phoneInput.value = "123456789";
            validatePhone();
            expect(phoneInput.classList.contains("highlight")).toBeTruthy();

            checkValidationAndSetOpacity();
            expect(editSaveButton.style.opacity).toBe("0.5");
            expect(editSaveButton.style.pointerEvents).toBe("none");

            // Valid Inputs
            firstNameInput.value = "John";
            validateFirstName();
            expect(firstNameInput.classList.contains("highlight")).toBeFalsy();

            lastNameInput.value = "Doe";
            validateLastName();
            expect(lastNameInput.classList.contains("highlight")).toBeFalsy();

            specializationInput.value = "Mathematics";
            validateSpecialization();
            expect(specializationInput.classList.contains("highlight")).toBeFalsy();

            yearInput.value = "3";
            validateYear();
            expect(yearInput.classList.contains("highlight")).toBeFalsy();

            phoneInput.value = "1234567890";
            validatePhone();
            expect(phoneInput.classList.contains("highlight")).toBeFalsy();

            // Check if save button is enabled after valid inputs
            checkValidationAndSetOpacity();
            expect(editSaveButton.style.opacity).toBe("1");
            expect(editSaveButton.style.pointerEvents).toBe("all");
        });

        test('Skills Modal Functionality', () => {
            const skillsModalBtn = document.getElementById('mySkillBtn');
            const skillsModal = document.getElementById('skillsModal');
            const dropdownContent = document.getElementById('myDropdown');

            skillsModalBtn.click();
            expect(skillsModal.style.display).toBe('flex');
            expect(dropdownContent.classList.contains('active')).toBeFalsy();

            const dropdownBtn = document.querySelector('.dropbtn');
            toggleDropdown();
            expect(dropdownContent.classList.contains('active')).toBeTruthy();

            const dropdownLinks = document.querySelectorAll('.dropdown-content a');
            const topicInput = document.getElementById('topic-input');

            dropdownLinks[0].click();
            expect(topicInput.value).toBe(dropdownLinks[0].textContent);

            topicInput.value = 'New Topic';
            const addTopicBtn = document.getElementById('skills-add');

            addTopicBtn.click();

            addTopicBtn.click();

            // Assert that the error message is displayed
            const errorMessage = document.getElementById('error-message-for-skills');
            expect(getComputedStyle(errorMessage).display).toBe('block');
        });

    });

});