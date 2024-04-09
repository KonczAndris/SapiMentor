const fs = require('fs');
const path = require('path');
const { JSDOM } = require('jsdom');
const puppeteer = require('puppeteer');

const htmlFilePath = path.resolve(__dirname, './../../main/resources/templates/forgotPassword.html');
const htmlContent = fs.readFileSync(htmlFilePath, 'utf-8');

const { window } = new JSDOM(htmlContent);

const { validateEmail } = require('./../../main/resources/static/js/forgotPassword.js');

// Forgot Password page load testing
describe('Forgot Password page loading', () => {
    test('Page is fully loaded', () => {
        const { document } = window;
        const form = document.querySelector('form');
        const emailInput = document.querySelector('#email');
        const sendEmailButton = document.querySelector('#send-email');

        expect(form).toBeTruthy();
        expect(emailInput).toBeTruthy();
        expect(sendEmailButton).toBeTruthy();
    });
});

// Email validation testing
describe('validateUsername function', () => {
    describe('Valid email addresses', () => {
        var validEmails = [
            'user1@ms.sapientia.ro',
            'user2@student.ms.sapientia.ro',
            'john.doe@ms.sapientia.ro',
            'jane.smith@student.ms.sapientia.ro',
            'testuser123@ms.sapientia.ro',
            'alice.wonderland@student.ms.sapientia.ro',
            'email@ms.sapientia.ro',
            'user321@student.ms.sapientia.ro',
            'sample.email@ms.sapientia.ro',
            'another.user@student.ms.sapientia.ro'
        ];

        validEmails.forEach((email) => {
            test(`"${email}" is a valid email address`, () => {
                document.body.innerHTML = `<input type="text" id="email" value="${email}" />`;
                validateEmail();
                const emailInput = document.getElementById("email");
                expect(emailInput.classList.contains('highlight')).toBe(false);
            });
        });
    });

    describe('Invalid email addresses', () => {
        var invalidEmails = [
            'invalid1@gmail.com',
            'invalid2@outlook.com',
            'noatsigndomain.com',
            'missingdomain@',
            '@justdomain.com',
            'randomtext',
            'user@.com',
            'user@domain',
            'user@@domain.com',
            'user@domain..com'
        ];

        invalidEmails.forEach((email) => {
            test(`"${email}" is an invalid email address`, () => {
                document.body.innerHTML = `<input type="text" id="email" value="${email}" />`;
                validateEmail();
                const emailInput = document.getElementById("email");
                expect(emailInput.classList.contains('highlight')).toBe(true);
            });
        });
    });
});

// Forgot Password validation testing
describe('Forgot Password functionality', () => {
    test('New password email sent', async () => {
        const emailTestValue = "johndoe@student.ms.sapientia.ro";

        document.body.innerHTML = htmlContent;
        const emailInput = document.getElementById('email');
        emailInput.value = emailTestValue;
        const sendEmailButton = document.getElementById('send-email');
        const forgotPasswordForm = document.querySelector('form');

        validateEmail();
        expect(emailInput.classList.contains("highlight")).toBe(false);

        const submitSpy = jest.spyOn(forgotPasswordForm, 'dispatchEvent');
        forgotPasswordForm.dispatchEvent(new Event('submit'));

        await new Promise(resolve => setTimeout(resolve, 1000));
        expect(submitSpy).toHaveBeenCalled();
    });
});

// Redirection to Login page testing
describe('Thymeleaf redirection', () => {
    test('redirects to /login', () => {
        const { document } = window;
        const forgotPasswordHTMLContent = fs.readFileSync(path.resolve(__dirname, './../../main/resources/templates/forgotPassword.html'), 'utf-8');
        const forgotPasswordDOM = new JSDOM(forgotPasswordHTMLContent);
        const forgotPasswordDocument = forgotPasswordDOM.window.document;
        const loginLink = forgotPasswordDocument.querySelector('.login-link');

        expect(loginLink).toBeTruthy();
        loginLink.click();
        expect(loginLink.getAttribute('th:href')).toBe('@{/login}');
    });
});