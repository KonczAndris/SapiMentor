'use strict';

document.getElementById('search-button').addEventListener('click', () => {
    //console.log('search button clicked');
    searchInUsersTable();
});

function searchInUsersTable() {
    // ide a loading modalt
    showLoadingModal();

    const searchinput = document.getElementById('filter-input');
    const filter = searchinput.value.toLowerCase();
    //const checkboxes = document.querySelectorAll('#topic-myCheckboxes input[type="checkbox"]:checked');
    //const selectedValues = Array.from(checkboxes).map(checkbox => checkbox.value.trim().toUpperCase());
    // console.log("Keresési szöveg: " + filter);
    // console.log("Mik vonnak bepipalva: " + selectedValues);
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
        //console.log(data);
        window.location.href = '/users/search?' + queryString;

    }).catch(error => {
        console.error('Error:', error);
        hideLoadingModal();
    });
}

function showLoadingModal() {
    var modal = document.getElementById("loading-modal");
    modal.style.display = "block"; // Megjelenítjük a modal ablakot
}