<<<<<<< HEAD
function setupModifyUsersModal(userId) {
    var modalId = "usersModifyModal-" + userId;
    var modal = document.getElementById(modalId);

    if (modal) {
        var btnId = "modifyIcon";
        var btn1 = document.getElementById(btnId);
        modal.style.display = "flex";
    }
}

function closeModifyModal(id) {
    var modal = document.getElementById(id);
    if (modal) {
        modal.style.display = "none";
    } else {
        console.error("Modal with id '" + id + "' not found.");
    }
}

function saveModifiedUserDataToServer(userId) {
    var data = [];

    const firstName = document.getElementById("userFirstName-edit-modify-" + userId).value;
    const lastName = document.getElementById("userLastName-edit-modify-" + userId).value;
    const email = document.getElementById("userEmail-edit-modify-" + userId).value;
    const enabled = document.getElementById("userEnabled-edit-modify-" + userId).value;
    const specialization = document.getElementById("userSpecialization-edit-modify-" + userId).value;
    const year = document.getElementById("userYear-edit-modify-" + userId).value;
    const phoneNumber = document.getElementById("userPhoneNumber-edit-modify-" + userId).value;

    data.push({
        user_id: userId,
        first_Name: firstName,
        last_Name: lastName,
        email: email,
        enabled: enabled,
        specialization: specialization,
        year: year,
        phoneNumber: phoneNumber
});
    sendModifiedUserDataToServer(data);
}

function sendModifiedUserDataToServer(data) {
    showLoadingModal()
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var formData = new FormData();

    formData.append("user_id", data[0].user_id);
    formData.append("first_Name", data[0].first_Name);
    formData.append("last_Name", data[0].last_Name);
    formData.append("email", data[0].email);
    formData.append("enabled", data[0].enabled);
    formData.append("year", data[0].year);
    formData.append("specialization", data[0].specialization);
    formData.append("phoneNumber", data[0].phoneNumber);

    fetch('/users/modifyUser', {
        method: 'POST',
        headers: {
            'X-CSRF-TOKEN': token
        },
        body: formData
    }).then(response => {

        if (response.ok) {
            return response.text();
        } else {
            return response.text();
        }
    }).then(data => {

        if (data === "User modified successfully!") {
            location.reload();
        }
    }).catch(error => {
        hideLoadingModal()
        console.error('An error occurred:', error);
    });
=======
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
>>>>>>> 184af3abdee9f2cff58213e60a38293ffe3fb228
}