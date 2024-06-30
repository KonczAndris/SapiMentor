// Menu displaying in mobile view
window.onload = function() {
    toggleDivs();
};

let isLeftVisible = false;

function toggleDivs() {
    const leftDiv = document.querySelector('.left');
    const rightDiv = document.querySelector('.right');
    const centerDiv = document.querySelector('.center');

    if (isLeftVisible) {
        rightDiv.classList.add('hidden');
        leftDiv.classList.remove('hidden');
    } else {
        leftDiv.classList.add('hidden');
        rightDiv.classList.remove('hidden');
    }
    isLeftVisible = !isLeftVisible;
}

// Back to Index page
document.addEventListener("DOMContentLoaded", function() {
    var h1Element = document.getElementById("sapimentor-logo");
    h1Element.addEventListener("click", function() {
        window.location.href = "/";
    });
});

// Message alert notification - redirection to Favorites page
document.querySelectorAll(".toMessenger-btn").forEach(function(element) {
    element.addEventListener("click", function(event) {
        event.preventDefault();
        event.stopPropagation();
        window.location.href = "/myGroup/favorites";
    });
});

//Info box redirection
document.addEventListener("DOMContentLoaded", function() {
    document.getElementById("profile-info-box").addEventListener("click", function () {
        window.location.href = "/profile";
    });
});
document.addEventListener("DOMContentLoaded", function() {
    document.getElementById("mentor-info-box").addEventListener("click", function () {
        window.location.href = "/myGroup";
    });
});
document.addEventListener("DOMContentLoaded", function() {
    document.getElementById("resources-info-box").addEventListener("click", function () {
        window.location.href = "/resources";
    });
});
document.addEventListener("DOMContentLoaded", function() {
    document.getElementById("profile-mini-info-box").addEventListener("click", function () {
        window.location.href = "/profile";
    });
});
document.addEventListener("DOMContentLoaded", function() {
    document.getElementById("mentor-mini-info-box").addEventListener("click", function () {
        window.location.href = "/myGroup";
    });
});
document.addEventListener("DOMContentLoaded", function() {
    document.getElementById("resources-mini-info-box").addEventListener("click", function () {
        window.location.href = "/resources";
    });
});

// Profile pic uploading modal
function setupUploadModal() {
    var modal = document.getElementById("uploadModal");

    var btn = document.getElementById("change-image-icon");
    var span = document.getElementsByClassName("close-upload")[0];
    btn.onclick = function() {
        modal.style.display = "flex";
    }
    span.onclick = function() {
        modal.style.display = "none";
    }
}

document.addEventListener('DOMContentLoaded', function () {
    const imageUploadInput = document.getElementById('imageUpload');
    const profileImage = document.getElementById('change-profile-image');
    const uploadModal = document.getElementById('uploadModal');
    let cropper;

    document.getElementById('upload-upload').addEventListener('click', function () {
        if (cropper) {
            console.log("destroy");
            cropper.destroy();
        }

        imageUploadInput.click();
    });

    if (imageUploadInput !== null) {
        imageUploadInput.addEventListener('change', function (event) {
            const selectedImage = event.target.files[0];
            const reader = new FileReader();

            reader.onload = function (e) {
                if (profileImage == null) {
                    let profileImage = document.getElementById('change-profile-image-sec');
                    profileImage.src = e.target.result;
                    profileImage.onload = function () {
                        if (cropper) {
                            cropper.destroy();
                        }

                        cropper = new Cropper(profileImage, {
                            aspectRatio: 1,
                            viewMode: 1,
                        });
                    };
                } else {
                    profileImage.src = e.target.result;
                    profileImage.onload = function () {
                        if (cropper) {
                            console.log("destroy");
                            cropper.destroy();
                        }
                        cropper = new Cropper(profileImage, {
                            aspectRatio: 1,
                            viewMode: 1,
                        });
                    };
                }
                uploadModal.style.display = 'block';
            };
            reader.readAsDataURL(selectedImage);
        });
    }

    if (document.getElementById('upload-save') !== null) {
        document.getElementById('upload-save').addEventListener('click', function (event) {
            var token = $("meta[name='_csrf']").attr("content");
            var header = $("meta[name='_csrf_header']").attr("content");
            showLoadingModal();
            if (cropper) {
                const croppedCanvas = cropper.getCroppedCanvas();
                if (!croppedCanvas) {
                    console.error('No cropped image data.');
                    return;
                }
                croppedCanvas.toBlob(function (blob) {
                    const formData = new FormData();
                    formData.append('image', blob, 'cropped-image.jpg', { type: 'image/jpeg' });
                    console.log(formData.getAll('image'));
                    fetch('/profile/upload-profile-image', {
                        method: 'POST',
                        body: formData,
                        headers: {
                            'X-CSRF-TOKEN': token
                        }
                    }).then(response => {
                        location.reload();
                    }).catch(error => {
                        console.error('An error occurred while uploading the image: ', error);
                    });
                }, 'image/jpeg');
            }
        });
    }

    document.querySelector('.close-upload').addEventListener('click', function () {
        uploadModal.style.display = 'none';
    });
});

let profileimagesforProfilePage = [];
function handlereselectedimagesforProfilePage() {
    for (let i = 0; i < profileimagesforProfilePage.length; i++) {
        const commentprofileData = profileimagesforProfilePage[i];
        const commentprofileImage = commentprofileData[0];
        const commentprofileId = commentprofileData[1];

        var commentedProfileImg  = document.getElementById('commentProfileImgProfilePage-' + commentprofileId);
        if (commentprofileImage != null && commentedProfileImg != null ) {
            commentedProfileImg.src = 'data:image/jpeg;base64,' + commentprofileImage;
        }
    }
}

// Role selection modal
function setupMentorModal() {
    var mentorModal = document.getElementById("mentorModal");
    var mentorBtn = document.getElementById("myMentorBtn");
    var mentorClose = document.getElementsByClassName("close-mentor")[0];

    if (mentorBtn !== null) {
        mentorBtn.onclick = function() {
            mentorModal.style.display = "flex";
        };
    }

    mentorClose.onclick = function() {
        mentorModal.style.display = "none";
    };
}

let selectedRole = [];
function updateRoleStatus(){
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var selectedRoles = document.querySelectorAll('input[name="role"]:checked');
    var selectedRolesLabels = document.querySelectorAll('label.unchecked');
    let associatedInputs = [];

    if (selectedRolesLabels.length > 0) {
        selectedRolesLabels.forEach(function(label) {
            let associatedInputValue = label.querySelector('input[name="role"]').value;
            associatedInputs.push(associatedInputValue);
        });

        fetch('/deleteUserRoleStatus', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'X-CSRF-TOKEN': token
            },
            body: 'selectedRoleToDelete=' + encodeURIComponent(associatedInputs.toString())
        }).then(response => {
            if (!response.ok) {
                throw new Error('Error occurred while updating user role status');
            }
            return response.json();
        }).then(data => {
            var selectedRoleValues = Array.from(selectedRoles).map(role => role.value).join(',');
            fetch('/updateUserRoleStatus', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                    'X-CSRF-TOKEN': token
                },
                body: 'selectedRole=' + encodeURIComponent(selectedRoleValues)
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Error occurred while updating user role status');
                    }
                    return response.json(); // Várunk egy JSON választ
                })
                .then(data => {
                    console.log(data.message);
                    if (data.message === "NEM_MASOD") {
                        showErrorMessageInProfile("The user must be a 2nd year student at least\n" +
                            " to change their role.");
                    } else if (data.message === "MODOSITVA") {
                        location.reload();
                    }

                })
                .catch(error => {
                    console.error('Hiba történt:', error);
                });
        }).catch(error => {
            console.error('Hiba történt:', error);
        })

    } else {
        var selectedRoleValues = Array.from(selectedRoles).map(role => role.value).join(',');
        fetch('/updateUserRoleStatus', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'X-CSRF-TOKEN': token
            },
            body: 'selectedRole=' + encodeURIComponent(selectedRoleValues)
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Error occurred while updating user role status');
                }
                return response.json();
            })
            .then(data => {
                console.log(data.message);
                if (data.message === "NEM_MASOD") {
                    showErrorMessageInProfile("The user must be at least 2 years\n" +
                        " to change their role.");
                } else if (data.message === "MODOSITVA") {
                    location.reload();
                }
            })
            .catch(error => {
                console.error('Hiba történt:', error);
            });
    }
}

function showErrorMessageInProfile(message) {
    var errorMessageElement = document.getElementById('error-message-modal-content-mentor');
    errorMessageElement.innerText = message;
}

// About Modal
function setupModal() {
    var modal = document.getElementById("myModal");
    var btn = document.getElementById("myBtn");
    var span = document.getElementsByClassName("close")[0];

    if (btn && modal) {
        btn.addEventListener("click", function() {
            modal.style.display = "block";
        });
    }

    if (span && modal) {
        span.addEventListener("click", function() {
            modal.style.display = "none";
        });
    }
}

function validateFirstName() {
    var firstNameInput = document.getElementById("firstName-edit");
    var firstNameValue = firstNameInput.value.trim();

    if (!/^[a-zA-Z]{1,20}$/.test(firstNameValue)) {
        firstNameInput.classList.add("highlight");
    } else {
        firstNameInput.classList.remove("highlight");
    }
}

function validateLastName() {
    var lastNameInput = document.getElementById("lastName-edit");
    var lastNameValue = lastNameInput.value.trim();

    if (!/^[a-zA-Zéáűúőíöü\s'-]{1,20}$/.test(lastNameValue)) {
        lastNameInput.classList.add("highlight");
    } else {
        lastNameInput.classList.remove("highlight");
    }
}

function validateSpecialization() {
    var specializationInput = document.getElementById("specialization-edit");
    var specializationValue = specializationInput.value.trim();

    if (!/^[a-zA-Z\s]{4,20}$/.test(specializationValue)) {
        specializationInput.classList.add("highlight");
    } else {
        specializationInput.classList.remove("highlight");
    }
}

function validateYear() {
    var yearInput = document.getElementById("year-edit");
    var yearValue = yearInput.value.trim();

    if (!/^[1-4]$/.test(yearValue)) {
        yearInput.classList.add("highlight");
    } else {
        yearInput.classList.remove("highlight");
    }
}

function validatePhone() {
    var phoneInput = document.getElementById("phone-edit");
    var phoneValue = phoneInput.value.trim();

    if (/^\d{1,9}$|^\d{11,}$/.test(phoneValue)) {
        phoneInput.classList.add("highlight");
    } else {
        phoneInput.classList.remove("highlight");
    }
}

function checkValidationAndSetOpacity() {
    var firstNameInput = document.getElementById("firstName-edit");
    var lastNameInput = document.getElementById("lastName-edit");
    var specializationInput = document.getElementById("specialization-edit");
    var yearInput = document.getElementById("year-edit");
    var phoneInput = document.getElementById("phone-edit");
    var editSaveButton = document.getElementById("edit-save");

    var isHighlighted = firstNameInput.classList.contains("highlight") ||
        lastNameInput.classList.contains("highlight") ||
        specializationInput.classList.contains("highlight") ||
        yearInput.classList.contains("highlight") ||
        phoneInput.classList.contains("highlight");

    if (isHighlighted) {
        editSaveButton.style.opacity = "0.5";
        editSaveButton.style.pointerEvents = 'none';
    } else {
        editSaveButton.style.opacity = "1";
        editSaveButton.style.pointerEvents = 'all';
    }
}

// Skills Modal
function setupSkillsModal() {
    var modal = document.getElementById("skillsModal");
    var btn = document.getElementById("mySkillBtn");
    var span = document.getElementsByClassName("close-skill")[0];

    if (modal && btn && span) {
    btn.onclick = function() {
        modal.style.display = "flex";
    }
    span.onclick = function() {
        modal.style.display = "none";
    }}
}

function selectTopic(topic) {
    const topicInput = document.getElementById('topic-input');
    topicInput.value = topic.textContent;
}

function toggleDropdown() {
    var dropdownContent = document.getElementById("myDropdown");
    dropdownContent.classList.toggle("active");

    var dropbtn = document.querySelector(".dropbtn");
    if (dropdownContent.classList.contains("active")) {
        dropbtn.style.borderRadius = "20px 20px 0 0";
    } else {
        dropbtn.style.borderRadius = "";
    }
}

document.addEventListener('DOMContentLoaded', function() {
    const topicLinks = document.querySelectorAll('.dropdown-content a');

    topicLinks.forEach(function(link) {
        link.addEventListener('click', function(event) {
            event.preventDefault();
            selectTopic(link);
        });
    });
});

function closeDropdown(selectedItem) {
    var dropdownContent = document.getElementById("myDropdown");
    dropdownContent.classList.remove("active");

    var dropbtn = document.querySelector(".dropbtn");
    dropbtn.style.borderRadius = "";
}

function selectTag(tag) {
    if (tag.classList.contains("topic-tag-display")) {
        tag.classList.remove("topic-tag-display");
        tag.classList.add("topic-tag-display-selected");
    } else if (tag.classList.contains("topic-tag-display-selected")) {
        tag.classList.remove("topic-tag-display-selected");
        tag.classList.add("topic-tag-display");
    }
}

function closeRow(button) {
    const row = button.closest('tr');
    row.style.display = 'none';
}

function removeTag(element) {
    element.remove();
    var elementText = element.textContent;
    var selectedTags = document.querySelectorAll(".topic-tag-display");
    selectedTags.forEach(function(tag) {
        var tagName = tag.textContent;
        if (tagName === elementText) {
            tag.style.removeProperty("display");
        }
    });
}

function deleteRow(button) {
    var row = button.closest('tr');
    if (row) {
        var nextRow = row.nextElementSibling;
        if (nextRow) {
            nextRow.remove();
        }
        row.remove();
    }
}

function deleteRowAndDatabase(button) {
    var row = button.closest('tr');
    if (row) {
        var topicId = row.querySelector("input[name='topicId']").value;
        if (topicId) {
            deleteTopicAndSkills(topicId);
        }
        var nextRow = row.nextElementSibling;
        if (nextRow) {
            nextRow.remove();
        }
        row.remove();
    }
}

function deleteTopicAndSkills(topicId) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    $.ajax({
        type: "POST",
        url: "/deleteTopicAndSkills",
        data: { topicId: topicId },
        success: function (response) {
            console.log("Sikeres törlés: " + response);
        },
        error: function (error) {
            console.error(error);
        }
    });
}

function saveSkills(button,skillCell,topicId) {
    var selectedTags = document.querySelectorAll(".topic-tag-display-selected");

    selectedTags.forEach(function(tag) {
        if (tag.classList.contains("topic-tag-display-selected")) {
            tag.style.display = "none";
            tag.classList.add("topic-tag-display")
        }
    });

    var selectedTagText = Array.from(selectedTags).map(function(tag) {
        return tag.textContent;
    }).join(", ");

    for (var i = 0; i < selectedTags.length; i++) {
        var skillTag = document.createElement("div");
        skillTag.className = "topic-tag";
        skillTag.textContent = selectedTags[i].textContent;
        skillTag.onclick = function() {
            removeTag(this);
        }
        skillCell.appendChild(skillTag);
    }
    selectedTags.forEach(function(tag) {
        tag.classList.remove("topic-tag-display-selected");
    });
}

var topicCounter = 0;
function isTopicAlreadyAdded(topicName) {
    var tableRows = document.querySelectorAll(".table-container table tbody tr");
    for (var i = 0; i < tableRows.length; i++) {
        var topicCell = tableRows[i].querySelector("td:first-child");
        if (topicCell && topicCell.textContent === topicName) {
            return true;
        }
    }
    return false;
}

function addSelectedTopic() {
    var errorMessageForAddingTopic = document.getElementById("error-message-for-skills");
    var selectedTopic = document.getElementById("topic-input").value;
    if (selectedTopic !== "") {
        if (!isTopicAlreadyAdded(selectedTopic)) {
            errorMessageForAddingTopic.style.display = "none";

            var row = document.createElement("tr");
            var topicId = "topic-" + topicCounter; // Egyedi témának egyedi azonosító

            var topicCell = document.createElement("td");
            topicCell.textContent = selectedTopic;

            var skillCell = document.createElement("td");
            skillCell.className = "scrollable-column";

            var funcButtons = document.createElement("div");
            funcButtons.className = "func-buttons";

            var topicSkills = document.createElement("tr");
            topicSkills.className = "tr-keywords " + topicId;

            var skillContainerTdElement = document.createElement("td");
            skillContainerTdElement.colSpan = 2;

            var addButton = document.createElement("button");
            addButton.className = "add-button green-button";
            addButton.textContent = "Add";
            addButton.onclick = function() {
                addSkillToTopic(selectedTopic,skillContainerTdElement, topicId, skillCell);
                const keywordsRow = document.querySelector('.' + topicId);
                keywordsRow.style.display = (keywordsRow.style.display === 'none' || keywordsRow.style.display === '') ? 'table-row' : 'none';
            };

            var deleteButton = document.createElement("button");
            deleteButton.className = "delete-button red-button";
            deleteButton.textContent = "Delete";
            deleteButton.onclick = function() {
                deleteRow(this);
            };

            funcButtons.appendChild(addButton);
            funcButtons.appendChild(deleteButton);

            var funcButtonsCell = document.createElement("td");
            funcButtonsCell.appendChild(funcButtons);

            var topicSkillsFuncButtons = document.createElement("div");
            topicSkillsFuncButtons.className = "func-buttons";

            var saveButton = document.createElement("button");
            saveButton.className = "add-button green-button";
            saveButton.id = "save-button";
            saveButton.textContent = "Save";
            saveButton.onclick = function() {
                saveSkills(this,skillCell, topicId);
            }

            var cancelButton = document.createElement("button");
            cancelButton.className = "delete-button red-button";
            cancelButton.textContent = "Cancel";
            cancelButton.onclick = function() {
                closeRow(this);
            };

            topicSkillsFuncButtons.appendChild(saveButton);
            topicSkillsFuncButtons.appendChild(cancelButton);

            var TopicSkillsfuncButtonsCell = document.createElement("td");
            TopicSkillsfuncButtonsCell.appendChild(topicSkillsFuncButtons);

            row.appendChild(topicCell);
            row.appendChild(skillCell);
            row.appendChild(funcButtonsCell);

            topicSkills.appendChild(skillContainerTdElement);
            topicSkills.appendChild(TopicSkillsfuncButtonsCell);

            var tableContainer = document.querySelector(".table-container table tbody");

            tableContainer.appendChild(row);
            tableContainer.appendChild(topicSkills);
            topicCounter++;
        } else {
            errorMessageForAddingTopic.style.display = "block";
        }
    }
}

function addSkillToTopic(selectedTopic, skillContainer, topicId, skillCell) {
    while (skillContainer.firstChild) {
        skillContainer.removeChild(skillContainer.firstChild);
    }

    var divs = skillCell.querySelectorAll("div");

    fetch(`/getSkills?selectedTopic=${selectedTopic}`)
        .then(response => response.json())
        .then(data => {
            if (data && data.length > 0) {
                data.forEach(skill => {
                    var skillTagDisplay = document.createElement("div");
                    skillTagDisplay.className = "topic-tag-display";
                    skillTagDisplay.textContent = skill.skill;
                    skillTagDisplay.onclick = function() {
                        selectTag(this);
                    }
                    divs.forEach(function(div) {
                        if (div.textContent === skill.skill) {
                            skillTagDisplay.style.display = "none";
                        }
                    });
                    skillContainer.appendChild(skillTagDisplay);
                });
            } else {
                var noSkillsMessage = document.createElement("p");
                noSkillsMessage.textContent = "There are no skills under the selected topic.";
                skillContainer.appendChild(noSkillsMessage);
            }
        })
        .catch(error => {
            console.error('An error occurred while querying skills: ', error);
        });
}

function saveDataToServer() {
    var tableRows = document.querySelectorAll("#skillsModalTableContainer table tbody tr");
    var data = [];

    tableRows.forEach(function(row) {
        var topic = row.querySelector("td:first-child").textContent;
        var skills = [];
        row.querySelectorAll(".topic-tag").forEach(function(tag) {
            skills.push(tag.textContent);
        });

        var hiddenIdField = row.querySelector("input[name='topicId']");
        if (hiddenIdField && topic !== '' && skills.length > 0) {
            var topicId = hiddenIdField.value;
            data.push({ id: topicId, topic: topic, skills: skills });
        }
        if (!hiddenIdField && topic !== '' && skills.length > 0) {
            data.push({id:'', topic: topic, skills: skills });
        }
    });
    console.log(data);
    showLoadingModal();
    sendDataToServer(data);
}

function sendDataToServer(data) {
    var profileTopicsDataItems = JSON.stringify(data);
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    fetch('/saveProfileTopics', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'X-CSRF-TOKEN': token
        },
        body: 'profileTopicsDataItems=' + encodeURIComponent(profileTopicsDataItems)
    }).then(response => {
        if (response.ok) {
            return response.text();
        } else {
            return response.text();
        }
    }).then(data => {
        console.log(data);
        if (data === "Success") {
            location.reload();
        }
    })
        .catch(error => {
            console.error('Hiba történt:', error);
        });
}

function showTopicsAndSkillsInModal() {
    fetch("/getUserTopicsAndSkills")
        .then(function (response) {
            return response.json();
        })
        .then(function (data) {
            var userTopics = data;
            userTopics.forEach(function (topic) {
                var row = document.createElement("tr");
                var topicId = "topic-" + topicCounter;
                var topicIdCell = document.createElement("input")
                topicIdCell.type = "hidden";
                topicIdCell.name = "topicId";
                topicIdCell.value = topic.id;
                var topicCell = document.createElement("td");
                topicCell.textContent = topic.topic;
                var skillCell = document.createElement("td");
                skillCell.className = "scrollable-column";

                topic.tags.forEach(function (skill) {
                    var skillTag = document.createElement("div");
                    skillTag.className = "topic-tag";
                    skillTag.textContent = skill;
                    skillTag.onclick = function () {
                        removeTag(this);
                    }
                    skillCell.appendChild(skillTag);
                });

                var funcButtons = document.createElement("div");
                funcButtons.className = "func-buttons";

                var topicSkills = document.createElement("tr");
                topicSkills.className = "tr-keywords " + topicId;

                var skillContainerTdElement = document.createElement("td");
                skillContainerTdElement.colSpan = 2;

                var addButton = document.createElement("button");
                addButton.className = "add-button";
                addButton.textContent = "Add";
                addButton.onclick = function () {
                    addSkillToTopic(topic.topic, skillContainerTdElement, topicId, skillCell);
                    const keywordsRow = document.querySelector('.' + topicId);
                    keywordsRow.style.display = (keywordsRow.style.display === 'none' || keywordsRow.style.display === '') ? 'table-row' : 'none';
                }

                var deleteButton = document.createElement("button");
                deleteButton.className = "delete-button";
                deleteButton.textContent = "Delete";

                deleteButton.onclick = function () {
                    if (this.textContent === "Delete") {
                        this.textContent = "Sure?";
                        this.style.backgroundColor = "rgb(238, 84, 76)";
                        document.body.addEventListener('click', outsideClickHandler.bind(null, this));
                    } else {
                        deleteRowAndDatabase(this);
                        this.style.backgroundColor = "rgb(253, 124, 107)";
                        document.body.removeEventListener('click', outsideClickHandler);
                    }
                }

                function outsideClickHandler(clickedButton, event) {
                    if (!clickedButton.contains(event.target)) {
                        clickedButton.textContent = "Delete";
                        clickedButton.style.backgroundColor = "";
                        document.body.removeEventListener('click', outsideClickHandler);
                    }
                }


                funcButtons.appendChild(addButton);
                funcButtons.appendChild(deleteButton);

                var funcButtonsCell = document.createElement("td");
                funcButtonsCell.appendChild(funcButtons);

                var topicSkillsFuncButtons = document.createElement("div");
                topicSkillsFuncButtons.className = "func-buttons";

                var saveButton = document.createElement("button");
                saveButton.className = "add-button green-button";
                saveButton.id = "save-button";
                saveButton.textContent = "Save";
                saveButton.onclick = function () {
                    saveSkills(this, skillCell, topicId);
                }

                var cancelButton = document.createElement("button");
                cancelButton.className = "delete-button red-button";
                cancelButton.textContent = "Cancel";
                cancelButton.onclick = function () {
                    closeRow(this);
                }

                topicSkillsFuncButtons.appendChild(saveButton);
                topicSkillsFuncButtons.appendChild(cancelButton);

                var TopicSkillsfuncButtonsCell = document.createElement("td");
                TopicSkillsfuncButtonsCell.appendChild(topicSkillsFuncButtons);

                row.appendChild(topicCell);
                row.appendChild(skillCell);
                row.appendChild(funcButtonsCell);
                row.appendChild(topicIdCell);

                topicSkills.appendChild(skillContainerTdElement);
                topicSkills.appendChild(TopicSkillsfuncButtonsCell);

                var tableContainer = document.querySelector(".table-container table tbody");

                tableContainer.appendChild(row);
                tableContainer.appendChild(topicSkills);

                topicCounter++;
            });
        })
        .catch(function (error) {
            console.error("Hiba történt az adatok lekérése közben:", error);
        });
}

document.addEventListener('DOMContentLoaded', function() {
    var errorMessageForSkills = document.getElementById('error-message-for-skills');
    if (errorMessageForSkills !== null){
        errorMessageForSkills.style.display = 'none';
    }

    var checkboxes = document.querySelectorAll('input[type="checkbox"][name="role"]');

    checkboxes.forEach(function(checkbox) {
        if (checkbox.checked) {
            var label = checkbox.closest('label');
            label.classList.add('checked');
        }
        checkbox.addEventListener('change', function() {
            var label = this.closest('label');

            if (this.checked) {
                label.classList.remove('unchecked');
                label.classList.add('checked');
            } else {
                label.classList.remove('checked');
                label.classList.add('unchecked');
            }
        });
    });
});

// Closing modals
function closeModalOnClickOutside() {
    var modal1 = document.getElementById("skillsModal");
    var modal2 = document.getElementById("myModal");
    var modal3 = document.getElementById("mentorModal");
    var modal4 = document.getElementById("uploadModal");

    window.addEventListener("click", function(event) {
        if (event.target == modal1) {
            modal1.style.display = "none";
        }
        if (event.target == modal2) {
            modal2.style.display = "none";
        }
        if (event.target == modal3) {
            modal3.style.display = "none";
        }
        if (event.target == modal4) {
            modal4.style.display = "none";
        }
    });
}

// Comment Section
document.addEventListener('DOMContentLoaded', function() {
    var seeCommentsButton = document.getElementById('showCommentsButton');
    if (seeCommentsButton) {
        seeCommentsButton.addEventListener('click', function() {
            var token = $("meta[name='_csrf']").attr("content");
            var header = $("meta[name='_csrf_header']").attr("content");
            var url2 = '/getSelectedUsersImagesForProfilePage';

            fetch(url2, {
                method: "GET",
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                    'X-CSRF-TOKEN': token
                }
            }).then(response => {
                // Check if the response is ok
                if (response.ok) {
                    return response.json();
                } else {
                    throw new Error('Something went wrong');
                }
            }).then(data => {
                profileimagesforProfilePage = data.selectedUserImagesForProfilePage;
                handlereselectedimagesforProfilePage();
            }).catch(error => {
                console.log("Error: ", error);
            });
        });
    }
});

// Mentor or Mentee check at MyGroup redirection
function notMenteeOrMentor() {
    var alertModal = document.createElement('div');
    alertModal.classList.add('alertModal');
    alertModal.innerHTML = `
        <div style="position: fixed; top: 0; left: 0; width: 100%; height: 100%; background-color: rgba(0,0,0,0.5); display: flex; justify-content: center; align-items: center;  user-select: none;">
            <div class="alertModal-content" style="background-color: white; padding: 20px; border-radius: 25px; text-align: center;  box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
                -webkit-animation-name: animatetop;
                -webkit-animation-duration: 0.4s;
                animation-name: animatetop;
                animation-duration: 0.4s;">
                <p>Please select a role first.</p>
                <p>You must be a <strong>MENTOR</strong> or a <strong>MENTEE</strong> to enter the My Group page. You can modify your role on the Profile page!</p>
                <button onclick="toProfile()" style="padding: 10px 20px; background-color: rgb(22, 175, 132); color: white; border: none; border-radius: 20px; cursor: pointer;"
                onmouseover="this.style.backgroundColor = 'rgb(30, 214, 162)'; this.style.boxShadow = '2px 2px 2px rgba(0, 0, 0, 0.5)'"
                onmouseout="this.style.backgroundColor = 'rgb(22, 175, 132)'; this.style.boxShadow = 'none'">To Profile</button>
                <button onclick="closeModal()" style="padding: 10px 20px; background-color: rgb(233, 99, 81); color: white; border: none; border-radius: 20px; cursor: pointer;"
                onmouseover="this.style.backgroundColor = 'rgb(233, 99, 81)'; this.style.boxShadow = '2px 2px 2px rgba(0, 0, 0, 0.5)'"
                onmouseout="this.style.backgroundColor = 'rgb(233, 99, 81)'; this.style.boxShadow = 'none'">Close</button>
            </div>
        </div>
    `;
    document.body.appendChild(alertModal);
}

function toProfile() {
    window.location.href = "/profile";
}

function closeModal() {
    var modal = document.querySelector('.alertModal');
    modal.parentNode.removeChild(modal);
}

function redirectToMyGroup() {
    window.location.href = "/myGroup";
}

//Preload
const loader = document.getElementById('preloader');
let loaderStartTime = Date.now();

// Function to hide the loader
function hideLoader() {
    const elapsedTime = Date.now() - loaderStartTime;
    const minimumDisplayTime = 1200;

    // Check if the loader has been displayed for at least 2 seconds
    if (elapsedTime >= minimumDisplayTime) {
        loader.style.display = 'none';
    } else {
        // If the minimum time hasn't elapsed yet, wait until it does
        setTimeout(hideLoader, minimumDisplayTime - elapsedTime);
    }
}

if (loader) {
    loader.style.display = 'block';
} else {
    console.error('Loader element not found.');
}

// Call hideLoader function after window load event
window.addEventListener('load', hideLoader);

// Function calls
document.addEventListener("DOMContentLoaded", function() {
    setupMentorModal();
});

document.addEventListener("DOMContentLoaded", function() {
    setupModal();
});

document.addEventListener("DOMContentLoaded", function() {
    setupSkillsModal();
});

document.addEventListener("DOMContentLoaded", function() {
    setupUploadModal();
});
closeModalOnClickOutside();
document.addEventListener("DOMContentLoaded", function() {
    validateFirstName();
});
document.addEventListener("DOMContentLoaded", function() {
    validateLastName();
});
document.addEventListener("DOMContentLoaded", function() {
    validateSpecialization();
});
document.addEventListener("DOMContentLoaded", function() {
    validateYear();
});
document.addEventListener("DOMContentLoaded", function() {
    validatePhone();
});
document.addEventListener("DOMContentLoaded", function() {
    addSelectedTopic();
});

if (window.location.href.includes("profile")){
    window.onload = showTopicsAndSkillsInModal();
}

function scrollToBottomInProfilePage() {
    var commentSection = document.getElementById('comment-section-container');
    commentSection.style.display = 'block';
    if (commentSection) {
        commentSection.scrollIntoView({ behavior: 'smooth', block: 'end' });
    }
}

document.addEventListener('DOMContentLoaded', function() {
    var showCommentsButton = document.getElementById('showCommentsButton');
    if (showCommentsButton) {
        showCommentsButton.addEventListener('click', scrollToBottomInProfilePage);
    } else {
        console.error('Element with id "showCommentsButton" not found.');
    }
});

module.exports = {setupModal, setupMentorModal, setupUploadModal, setupSkillsModal, validatePhone, validateFirstName, validateLastName, validateYear, validateSpecialization, checkValidationAndSetOpacity, toggleDropdown};
