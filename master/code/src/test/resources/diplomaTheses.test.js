const fs = require('fs');
const path = require('path');
const { JSDOM } = require('jsdom');

const $ = require('jquery');
global.$ = jest.fn();

jest.mock('jquery', () => {
    return () => ({
        ready: jest.fn(),
        trigger: jest.fn(),
    });
});

const htmlFilePath = path.resolve(__dirname, './../../main/resources/templates/diplomaTheses.html');
const htmlContent = fs.readFileSync(htmlFilePath, 'utf-8');

const { window } = new JSDOM(htmlContent);
const { document } = window;

global.document = document;
global.window = window;

document.addEventListener = jest.fn();

const { searchTableForTesting } = require('./../../main/resources/static/js/diplomaTheses.js');

describe('searchTableForTesting', () => {
    test('should filter table rows based on input value and selected checkboxes', () => {
        document.body.innerHTML = `
        <input type="text" id="filter-input">
        <table id="dataTable">
            <tr><td>0</td><td>Jack</td><td>...</td><td>Informatics</td></tr>
            <tr><td>1</td><td>Rose</td><td>...</td><td>Languages</td></tr>
            <tr><td>2</td><td>Tom</td><td>...</td><td>Informatics</td></tr>
            <tr><td>3</td><td>Jack</td><td>...</td><td>Informatics</td></tr>
            <tr><td>4</td><td>Peter</td><td>...</td><td>Engineering</td></tr>
        </table>
    `;

        const diplomaTestValue = "Jack";
        const selectedValues = ['Informatics', 'Languages'];

        const searchInput = document.getElementById('filter-input');
        searchInput.value = diplomaTestValue;
        const table = document.getElementById('dataTable');

        searchTableForTesting(searchInput, table, selectedValues);

        const rows = document.querySelectorAll('#dataTable tr');
        expect(rows.length).toBe(5);
        expect(rows[0].style.display).toBe('');
        expect(rows[1].style.display).toBe('none');
        expect(rows[2].style.display).toBe('none');
        expect(rows[3].style.display).toBe('');
        expect(rows[4].style.display).toBe('none');});
});
