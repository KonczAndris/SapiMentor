'use strict';

document.getElementById('search-button').addEventListener('click', () => {
    searchInUsersTable();
});

function searchInUsersTable() {
    showLoadingModal();

    const searchinput = document.getElementById('filter-input');
    const filter = searchinput.value.toLowerCase();
    const queryString = 'filter=' + encodeURIComponent(filter);
    fetch('/users/search?' + queryString, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
    }).then(response => {
        if (response.ok) {
            return response.text();
        } else {
            throw new Error('Request failed');
        }
    }).then(data => {
        window.location.href = '/users/search?' + queryString;
    }).catch(error => {
        console.error('Error:', error);
        hideLoadingModal();
    });
}

function showLoadingModal() {
    var modal = document.getElementById("loading-modal");
    modal.style.display = "block";
}