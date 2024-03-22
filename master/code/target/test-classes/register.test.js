const fs = require('fs');
const path = require('path');
const { JSDOM } = require('jsdom');
const puppeteer = require('puppeteer');

const htmlFilePath = path.resolve(__dirname, './../../main/resources/templates/register.html');
const htmlContent = fs.readFileSync(htmlFilePath, 'utf-8');

const { window } = new JSDOM(htmlContent);

const { validateFirstName, validateLastName, validateEmail, validatePassword } = require('./../../main/resources/static/js/register.js');
require('./../../main/resources/static/js/register.js');

// Register page load testing
describe('Register page loading', () => {
    test('Page is fully loaded', () => {
        const { document } = window;
        const registerForm = document.querySelector('form');
        const firstNameInput = document.querySelector('#firstName');
        const lastNameInput = document.querySelector('#lastName');
        const emailInput = document.querySelector('#email');
        const passwordInput = document.querySelector('#password');
        const registerButton = document.querySelector('#register-submit');

        expect(registerForm).toBeTruthy();
        expect(firstNameInput).toBeTruthy();
        expect(lastNameInput).toBeTruthy();
        expect(emailInput).toBeTruthy();
        expect(passwordInput).toBeTruthy();
        expect(registerButton).toBeTruthy();
    });
});

// First name validation testing
describe('validateFirst function', () => {
    describe('Valid first names', () => {
        var validFirstNames = [
            'Gábor-Pista',
            'Anna',
            'Péter',
            'Judit',
            'Miklós',
            'Eszter-Léna',
            'Bence',
            'Katalin',
            'Zoltán',
            'Júlia'
        ];

        validFirstNames.forEach((firstName) => {
            test(`"${firstName}" is a valid first name`, () => {
                document.body.innerHTML = `<input type="text" id="firstName" value="${firstName}" />`;
                validateFirstName();
                const firstNameInput = document.getElementById("firstName");
                expect(firstNameInput.classList.contains('highlight')).toBe(false);
            });
        });
    });

    describe('Invalid first names', () => {
        var invalidFirstNames = [
            '12345',
            'Doe123',
            'John-Doe.',
            'O’Connor',
            'Smith, Jr.',
            'S',
            'Лебедев',
            'Yamada山田',
            'Özdemir6',
            'Herrera-Sánchez-Herrera-Sánchez'
        ];

        invalidFirstNames.forEach((firstName) => {
            test(`"${firstName}" is an invalid first name`, () => {
                document.body.innerHTML = `<input type="text" id="firstName" value="${firstName}" />`;
                validateFirstName();
                const firstNameInput = document.getElementById("firstName");
                expect(firstNameInput.classList.contains('highlight')).toBe(true);
            });
        });
    });
});

// Last name validation testing
describe('validateLastName function', () => {
    describe('Valid last names', () => {
        var validLastNames = [
            'Kovács',
            'Nagy',
            'Tóth',
            'Szabó',
            'Horváth',
            'Kiss-Nagy',
            'Molnár',
            'Varga',
            'Farkas',
            'Balogh'
        ];

        validLastNames.forEach((lastName) => {
            test(`"${lastName}" is a valid last name`, () => {
                document.body.innerHTML = `<input type="text" id="lastName" value="${lastName}" />`;
                validateLastName();
                const lastNameInput = document.getElementById("lastName");
                expect(lastNameInput.classList.contains('highlight')).toBe(false);
            });
        });
    });

    describe('Invalid last names', () => {
        var invalidLastNames = [
            '12345',
            'Doe123',
            'John-Doe.',
            'O’Connor',
            'Smith, Jr.',
            'S',
            'Лебедев',
            'Yamada山田',
            'Özdemir6',
            'Herrera-Sánchez-Herrera-Sánchez'
        ];

        invalidLastNames.forEach((lastName) => {
            test(`"${lastName}" is an invalid last name`, () => {
                document.body.innerHTML = `<input type="text" id="lastName" value="${lastName}" />`;
                validateLastName();
                const lastNameInput = document.getElementById("lastName");
                expect(lastNameInput.classList.contains('highlight')).toBe(true);
            });
        });
    });
});

// Full registration testing
describe('Registration functionality', () => {
    test('Successful registration', async () => {
        const firstNameTestValue = "John";
        const lastNameTestValue = "Doe";
        const emailTestValue = "johndoe@student.ms.sapientia.ro";
        const passwordTestValue = "securePassword";

        document.body.innerHTML = htmlContent;

        // Populate input fields with test values
        const firstNameInput = document.getElementById('firstName');
        firstNameInput.value = firstNameTestValue;
        const lastNameInput = document.getElementById('lastName');
        lastNameInput.value = lastNameTestValue;
        const emailInput = document.getElementById('email');
        emailInput.value = emailTestValue;
        const passwordInput = document.getElementById('password');
        passwordInput.value = passwordTestValue;
        const registrationForm = document.querySelector('form');
        const registerButton = document.getElementById('register-submit');

        // Simulate user input and validate fields
        validateFirstName();
        validateLastName();
        validateEmail();
        validatePassword();

        // Assert that there are no validation errors
        expect(firstNameInput.classList.contains("highlight")).toBe(false);
        expect(lastNameInput.classList.contains("highlight")).toBe(false);
        expect(emailInput.classList.contains("highlight")).toBe(false);
        expect(passwordInput.classList.contains("highlight")).toBe(false);

        const submitSpy = jest.spyOn(registrationForm, 'dispatchEvent');

        // Simulate form submission
        registrationForm.dispatchEvent(new Event('submit'));

        // Wait for asynchronous tasks to complete
        await new Promise(resolve => setTimeout(resolve, 1000));

        // Assert that form submission was attempted
        expect(submitSpy).toHaveBeenCalled();
    });
});

// Redirection to Login page testing
describe('Thymeleaf redirection', () => {
    test('redirects to /login', () => {
        const { document } = window;
        const registerHTMLContent = fs.readFileSync(path.resolve(__dirname, './../../main/resources/templates/register.html'), 'utf-8');
        const registerDOM = new JSDOM(registerHTMLContent);
        const registerDocument = registerDOM.window.document;
        const loginLink = registerDocument.querySelector('.login-link');

        expect(loginLink).toBeTruthy();
        expect(loginLink.getAttribute('th:href')).toBe('@{/login}');
    });
});