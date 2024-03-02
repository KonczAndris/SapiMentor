const { validateUsername, validatePassword } = require('./../../main/resources/static/js/login.js');

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
            test(`"${email}" should not have highlight class`, () => {
                document.body.innerHTML = `<input type="text" id="username" value="${email}" />`;
                validateUsername();
                const usernameInput = document.getElementById("username");
                expect(usernameInput.classList.contains('highlight')).toBe(false);
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
                document.body.innerHTML = `<input type="text" id="username" value="${email}" />`;
                validateUsername();
                const usernameInput = document.getElementById("username");
                expect(usernameInput.classList.contains('highlight')).toBe(true);
            });
        });
    });
});

describe('validatePassword function', () => {
    describe('Valid passwords', () => {
        const validPasswords = [
            'validpassword123',
            'VALIDPASSWORD',
            'VALID!123',
            'LongValidPassword123',
            '12345678'
        ];

        validPasswords.forEach((password) => {
            test(`"${password}" is a valid password`, () => {
                document.body.innerHTML = `<input type="password" id="password" value="${password}" />`;
                validatePassword();
                const passwordInput = document.getElementById("password");
                expect(passwordInput.classList.contains('highlight')).toBe(false);
            });
        });
    });

    describe('Invalid passwords', () => {
        const invalidPasswords = [
            'short',
            'waytoolongpasswordwhichismorethan20characters',
            'abcdefg',
            'No',
            '123'
        ];

        invalidPasswords.forEach((password) => {
            test(`"${password}" is an invalid password`, () => {
                document.body.innerHTML = `<input type="password" id="password" value="${password}" />`;
                validatePassword();
                const passwordInput = document.getElementById("password");
                expect(passwordInput.classList.contains('highlight')).toBe(true);
            });
        });
    });
});
