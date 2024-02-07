document.addEventListener("DOMContentLoaded", function() {
    var h1Element = document.getElementById("sapimentor-logo");
    h1Element.addEventListener("click", function() {
        window.location.href = "/";
    });

});

function setupMentorModal() {
    var mentorModal = document.getElementById("mentorModal");

    var mentorBtn = document.getElementById("myMentorBtn");
    var mentorClose = document.getElementsByClassName("close-mentor")[0];

    mentorBtn.onclick = function() {
        mentorModal.style.display = "flex";
    };

    mentorClose.onclick = function() {
        mentorModal.style.display = "none";
    };
}

function setupModal() {
    var modal = document.getElementById("myModal");
    var btn = document.getElementById("myBtn");
    var span = document.getElementsByClassName("close")[0];

    if (modal && btn && span) {
        btn.onclick = function() {
            modal.style.display = "block";
        };
        span.onclick = function() {
            modal.style.display = "none";
        };
    }
}

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


function selectTopic(topic) {
    const topicInput = document.getElementById('topic-input');
    topicInput.value = topic.textContent;
}

document.addEventListener('DOMContentLoaded', function() {
    const topicLinks = document.querySelectorAll('.dropdown-content a');

    topicLinks.forEach(function(link) {
        link.addEventListener('click', function(event) {
            event.preventDefault(); // Prevent the link from navigating
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
    const row = button.closest('tr'); // Find the closest parent row element
    row.style.display = 'none'; // Hide the row
}

function toggleDropdown() {
    var dropdownContent = document.getElementById("myDropdown");
    dropdownContent.classList.toggle("active");

    var dropbtn = document.querySelector(".dropbtn");
    if (dropdownContent.classList.contains("active")) {
        dropbtn.style.borderRadius = "20px 20px 0 0";
    } else {
        dropbtn.style.borderRadius = ""; // Reset to default value
    }
}

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

    // Check if any field is highlighted or empty (except for the phone number)
    var isHighlighted = firstNameInput.classList.contains("highlight") ||
        lastNameInput.classList.contains("highlight") ||
        specializationInput.classList.contains("highlight") ||
        yearInput.classList.contains("highlight") ||
        phoneInput.classList.contains("highlight");

    // Set opacity of the edit-save button
    if (isHighlighted) {
        editSaveButton.style.opacity = "0.5";
        editSaveButton.style.pointerEvents = 'none';
    } else {
        editSaveButton.style.opacity = "1";
        editSaveButton.style.pointerEvents = 'all';
    }
}

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

function removeTag(element) {
    element.remove();

    var elementText = element.textContent;
    var selectedTags = document.querySelectorAll(".topic-tag-display");
    selectedTags.forEach(function(tag) {
        var tagName = tag.textContent;
        //console.log(tagName);
        //console.log(elementText);
        //console.log(tag)
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
        //console.log("Torolni az adatbazisbol: " + topicId);

        if (topicId) {
            // Törölom az adott témát és skilleket az adatbázisból
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
    // CSRF token beolvasása a meta tag-ből
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    // console.log("Torles kezdete:");
    // console.log(token);
    // console.log(header);
    // console.log(topicId);
    // console.log("Torles vege");
    // Hívj meg egy AJAX kérést a backend végpontra, hogy törölje az adatokat
    // Például jQuery-t vagy a fetch API-t használhatod

    // AJAX kérés elküldése a tokennel
    $.ajax({
        type: "POST",
        url: "/deleteTopicAndSkills",
        data: { topicId: topicId },
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
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

    //console.log("1:" + selectedTags);
    // Fűzd össze az összes tartalmat egy szövegként
    var selectedTagText = Array.from(selectedTags).map(function(tag) {
        return tag.textContent;
    }).join(", "); // Vesszővel elválasztva a tag-eket
    //console.log("2:" + selectedTagText);

    for (var i = 0; i < selectedTags.length; i++) {
        var skillTag = document.createElement("div");
        skillTag.className = "topic-tag";
        skillTag.textContent = selectedTags[i].textContent;
        skillTag.onclick = function() {
            removeTag(this);
        }
        skillCell.appendChild(skillTag);
    }
    //console.log(skillCell);
    selectedTags.forEach(function(tag) {
        tag.classList.remove("topic-tag-display-selected");
    });

    //console.log("3:" + selectedTags)
}

// CROPPER
document.addEventListener('DOMContentLoaded', function () {
    const imageUploadInput = document.getElementById('imageUpload');
    const profileImage = document.getElementById('change-profile-image');
    const uploadModal = document.getElementById('uploadModal');
    let cropper; // Define cropper variable outside the event listeners

    // Event listener for the "Select" button in the modal
    document.getElementById('upload-upload').addEventListener('click', function () {
        // Clear previous cropper instance if it exists
        if (cropper) {
            console.log("destroy");
            cropper.destroy();
        }

        // Show the file input dialog
        imageUploadInput.click();
    });

    // Event listener for file input change
    imageUploadInput.addEventListener('change', function (event) {
        const selectedImage = event.target.files[0];

        console.log("Igen:",selectedImage);
        const reader = new FileReader();

        // Read the selected image as a data URL
        reader.onload = function (e) {

            console.log("Igen1:",profileImage);
            // Set the data URL as the source of the profile image
            if (profileImage == null) {
                let profileImage = document.getElementById('change-profile-image-sec');
                profileImage.src = e.target.result;

                // Initialize Cropper only when the image is loaded
                profileImage.onload = function () {
                    if (cropper) {
                        console.log("destroy");
                        cropper.destroy();
                    }

                    cropper = new Cropper(profileImage, {
                        aspectRatio: 1, // You can set the aspect ratio as needed
                        viewMode: 1, // Set the view mode to restrict the cropped area to the canvas
                    });
                    console.log("Igen2", cropper);
                };
            } else {
                profileImage.src = e.target.result;

                // Initialize Cropper only when the image is loaded
                profileImage.onload = function () {
                    if (cropper) {
                        console.log("destroy");
                        cropper.destroy();
                    }

                    cropper = new Cropper(profileImage, {
                        aspectRatio: 1, // You can set the aspect ratio as needed
                        viewMode: 1, // Set the view mode to restrict the cropped area to the canvas
                    });
                    console.log("Igen2", cropper);
                };
            }


            // Show the upload modal with the cropper
            uploadModal.style.display = 'block';


        };

        reader.readAsDataURL(selectedImage);
    });

    // Event listener for the "Upload" button in the modal
    document.getElementById('upload-save').addEventListener('click', function (event) {
        //event.preventDefault();
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");

        console.log(document.getElementById('imageUpload').files[0]);

        if (cropper) {
            //console.log("cropper", cropper);
            // Get the cropped data URL from the cropper
            //const croppedDataURL = cropper.getCroppedCanvas().toDataURL('image/jpeg');

            const croppedCanvas = cropper.getCroppedCanvas();
            if (!croppedCanvas) {
                // Handle the case where the user hasn't cropped an image
                console.error('No cropped image data.');
                return;
            }

            croppedCanvas.toBlob(function (blob) {
                const formData = new FormData();
                formData.append('image', blob, 'cropped-image.jpg', { type: 'image/jpeg' });
                console.log(formData.getAll('image'));
                fetch('/upload-profile-image', {
                    method: 'POST',
                    body: formData,
                    headers: {
                        'X-CSRF-TOKEN': token
                    }
                }).then(response => {
                    location.reload();
                }).catch(error => {
                    console.error('An error occurred while uploading the image:', error);
                });
                // Hide the upload modal
                // uploadModal.style.display = 'none';
                // location.reload();
            }, 'image/jpeg');

            // Perform the necessary steps to upload the cropped image as the profile picture
            // You can use AJAX to send the croppedDataURL to the server for further processing

            // For demonstration purposes, you can log the data URL to the console




        }
    });

    // Event listener for the modal close button
    document.querySelector('.close-upload').addEventListener('click', function () {
        // Hide the upload modal without saving
        uploadModal.style.display = 'none';
    });
});

// Egyedi témák számolására
var topicCounter = 0;


function isTopicAlreadyAdded(topicName) {
    var tableRows = document.querySelectorAll(".table-container table tbody tr");
    //console.log(tableRows);
    for (var i = 0; i < tableRows.length; i++) {
        var topicCell = tableRows[i].querySelector("td:first-child");
        //console.log(topicCell);
        if (topicCell && topicCell.textContent === topicName) {
            return true; // A téma már hozzá van adva
        }
    }
    return false; // A téma nincs hozzáadva
}

//JavaScript rész a Skills Modal-hoz
function addSelectedTopic() {
    var errorMessageForAddingTopic = document.getElementById("error-message-for-skills");
    //var selectedTopic = document.getElementById("topic-input").value;
    //console.log(selectedTopic.type);
    var selectedTopic = document.getElementById("topic-input").value;
    //console.log(typeof selectedTopic);

    if (selectedTopic !== "") {
        if (!isTopicAlreadyAdded(selectedTopic)) {
            errorMessageForAddingTopic.style.display = "none";
            //console.log("Elso: " + topicCounter)
            // var topicTag = document.createElement("div");
            // topicTag.className = "topic-tag";
            // topicTag.textContent = selectedTopic;
            // topicTag.onclick = function() {
            //     removeTag(this);
            // };
            //
            // var tagocska = document.createElement("div");
            // tagocska.className = "topic-tag";
            // tagocska.textContent = "igen";
            // tagocska.onclick = function() {
            //     removeTag(this);
            // };


            var row = document.createElement("tr");
            var topicId = "topic-" + topicCounter; // Egyedi témának egyedi azonosító

            var topicCell = document.createElement("td");
            topicCell.textContent = selectedTopic;

            var skillCell = document.createElement("td");
            skillCell.className = "scrollable-column";
            // skillCell.appendChild(tagocska);
            // skillCell.appendChild(topicTag);

            var funcButtons = document.createElement("div");
            funcButtons.className = "func-buttons";

            var topicSkills = document.createElement("tr");
            topicSkills.className = "tr-keywords " + topicId; // Egyedi azonosító hozzáadása a class-hoz

            var skillContainerTdElement = document.createElement("td");
            skillContainerTdElement.colSpan = 2;



            var addButton = document.createElement("button");
            addButton.className = "add-button";
            addButton.textContent = "Add";
            addButton.onclick = function() {
                addSkillToTopic(selectedTopic,skillContainerTdElement, topicId, skillCell);
                // Az egyedi azonosító alapján keresünk rá a sorra
                const keywordsRow = document.querySelector('.' + topicId);
                keywordsRow.style.display = (keywordsRow.style.display === 'none' || keywordsRow.style.display === '') ? 'table-row' : 'none';
            };

            var deleteButton = document.createElement("button");
            deleteButton.className = "delete-button";
            deleteButton.textContent = "Delete";
            deleteButton.onclick = function() {
                deleteRow(this);
            };

            funcButtons.appendChild(addButton);
            funcButtons.appendChild(deleteButton);

            var funcButtonsCell = document.createElement("td");
            funcButtonsCell.appendChild(funcButtons);

            //console.log(topicSkills)
            var topicSkillsFuncButtons = document.createElement("div");
            topicSkillsFuncButtons.className = "func-buttons";

            var saveButton = document.createElement("button");
            saveButton.className = "add-button";
            saveButton.id = "save-button";
            saveButton.textContent = "Save";
            saveButton.onclick = function() {
                saveSkills(this,skillCell, topicId);
            }

            var cancelButton = document.createElement("button");
            cancelButton.className = "delete-button";
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

            topicCounter++; // Növeljük az egyedi azonosító számot
        } else {

            // itt kell andrisnak a hibauzenetet elhelyezze ugy ahogy o szeretne
            //alert("Ez a téma már hozzá van adva!");
            errorMessageForAddingTopic.style.display = "block";
        }
    }
}


//leges legszenvedosebb resz
function addSkillToTopic(selectedTopic, skillContainer, topicId, skillCell) {
    // Először törölom a korábban megjelenített skilleket
    while (skillContainer.firstChild) {
        skillContainer.removeChild(skillContainer.firstChild);
    }

    var divs = skillCell.querySelectorAll("div");

    // Itt lekérdezem a kiválasztott témához tartozó skilleket a szerverről
    fetch(`/getSkills?selectedTopic=${selectedTopic}`)
        .then(response => response.json())
        .then(data => {
            //console.log(data);
            // Itt dolgozom fel a visszakapott skilleket
            if (data && data.length > 0) {
                data.forEach(skill => {
                    var skillTagDisplay = document.createElement("div");
                    skillTagDisplay.className = "topic-tag-display";
                    skillTagDisplay.textContent = skill.skill;
                    skillTagDisplay.onclick = function() {
                        selectTag(this);
                    }
                    // console.log(skillTagDisplay);
                    // console.log(divs);
                    divs.forEach(function(div) {
                        if (div.textContent === skill.skill) {
                            //skillTagDisplay.classList.add("topic-tag-display");
                            skillTagDisplay.style.display = "none";
                        }
                    });
                    skillContainer.appendChild(skillTagDisplay);
                });

                //console.log(skillContainer);
            } else {
                // Ha nincs találat, akkor kiir egy üzenetet
                var noSkillsMessage = document.createElement("p");
                noSkillsMessage.textContent = "There are no skills under the selected topic.";
                skillContainer.appendChild(noSkillsMessage);
            }
        })
        .catch(error => {
            console.error('An error occurred while querying skills:', error);
        });
}

function saveDataToServer() {
    // Gyűjtsd össze az adatokat a táblából
    var tableRows = document.querySelectorAll("#skillsModalTableContainer table tbody tr");
    var data = [];

    //console.log(tableRows);

    tableRows.forEach(function(row) {
        var topic = row.querySelector("td:first-child").textContent;
        var skills = [];
        row.querySelectorAll(".topic-tag").forEach(function(tag) {
            skills.push(tag.textContent);
        });
        // console.log("Skillek:" + skills);
        // console.log("Topic:" + topic);


        // ID megszerzése a rejtett mezőből
        var hiddenIdField = row.querySelector("input[name='topicId']");
        if (hiddenIdField && topic !== '' && skills.length > 0) {
            var topicId = hiddenIdField.value;
            data.push({ id: topicId, topic: topic, skills: skills });
            //console.log("topicId értéke: " + topicId);
        }
        if (!hiddenIdField && topic !== '' && skills.length > 0) {
            data.push({id:'', topic: topic, skills: skills });
        }
    });
    console.log(data);
    // Elküldjük az adatokat a szervernek
    sendDataToServer(data);
}

function sendDataToServer(data) {
    // Állítsd be a rejtett mező értékét adataid alapján
    var profileTopicsDataItems = JSON.stringify(data);

    //document.getElementById("profileTopicsDataItems").value = profileTopicsDataItems;
    //console.log("Adatok: " + profileTopicsDataItems);
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
                // throw new Error('Hiba történt a válaszban');
            }
        }).then(data => {
            // ezt is andrisnak
            // Kell kezelni a valaszt es megjeleniteni a hibauzeneteket
            console.log(data);
            if (data === "Success") {
                location.reload();
            }
        })
        .catch(error => {
            console.error('Hiba történt:', error);
        });

    // Most küldd el az űrlapot
    //document.getElementById("skills-form").submit();
}


function showTopicsAndSkillsInModal() {
    //console.log(topicCounter)
// Fetch az adatbázisból, például egy aszinkron AJAX kérés segítségével
    fetch("/getUserTopicsAndSkills") // Cseréld le a megfelelő végpontra és kérési metódusra
        .then(function (response) {
            // Válasz JSON formátumban van
            return response.json();
        })
        .then(function (data) {
            // Az adatok beérkezése után itt folytasd
            var userTopics = data; // data tartalmazza az adatokat
            //console.log(userTopics);


            // Adatok hozzáadása a táblázathoz
            userTopics.forEach(function (topic) {
                var row = document.createElement("tr");
                // Egyedi azonosító hozzáadása a class-hoz
                var topicId = "topic-" + topicCounter;

                var topicIdCell = document.createElement("input")
                topicIdCell.type = "hidden";
                topicIdCell.name = "topicId";
                //console.log(topic.id);
                topicIdCell.value = topic.id;
                //console.log(topicIdCell);

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
                // Egyedi azonosító hozzáadása a class-hoz
                topicSkills.className = "tr-keywords " + topicId;

                var skillContainerTdElement = document.createElement("td");
                skillContainerTdElement.colSpan = 2;

                var addButton = document.createElement("button");
                addButton.className = "add-button";
                addButton.textContent = "Add";
                addButton.onclick = function () {
                    addSkillToTopic(topic.topic, skillContainerTdElement, topicId, skillCell);
                    // Az egyedi azonosító alapján keresünk rá a sorra
                    const keywordsRow = document.querySelector('.' + topicId);
                    keywordsRow.style.display = (keywordsRow.style.display === 'none' || keywordsRow.style.display === '') ? 'table-row' : 'none';
                }

                var deleteButton = document.createElement("button");
                deleteButton.className = "delete-button";
                deleteButton.textContent = "Delete";

                deleteButton.onclick = function () {
                    if (this.textContent === "Delete") {
                        this.textContent = "Sure?";
                        this.style.backgroundColor = "rgb(234, 80, 80)"; // Change to the "Sure?" color
                        document.body.addEventListener('click', outsideClickHandler.bind(null, this));
                    } else {
                        deleteRowAndDatabase(this);
                        this.style.backgroundColor = "rgb(245, 125, 125)"; // Change to the "Delete" color
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
                saveButton.className = "add-button";
                saveButton.id = "save-button";
                saveButton.textContent = "Save";
                saveButton.onclick = function () {
                    saveSkills(this, skillCell, topicId);
                }

                var cancelButton = document.createElement("button");
                cancelButton.className = "delete-button";
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

                topicCounter++; // Növeljük az egyedi azonosító számot
            });
        })
        .catch(function (error) {
            console.error("Hiba történt az adatok lekérése közben:", error);
        });
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
            //console.log(associatedInputValue);
        });
        console.log("associatedInputs: " +  associatedInputs.toString());

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
            return response.json(); // Várunk egy JSON választ
        }).then(data => {
            // Kezeljük a választ, és jelenítsük meg az üzenetet
            console.log(data.message);
            console.log("igen1" + selectedRoles);
            var selectedRoleValues = Array.from(selectedRoles).map(role => role.value).join(',');
            // selectedRoles.forEach(function(role) {
            //     selectedRole.push(role.value);
            //     //console.log(role.value);
            // });
            console.log("igen2" +selectedRoleValues);


// Elküldjük a kérést az '/updateUserRoleStatus' végpontra
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
                    // Kezeljük a választ, és jelenítsük meg az üzenetet
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
            //location.reload();
        }).catch(error => {
            console.error('Hiba történt:', error);
        })

    } else {
        console.log("igen1" + selectedRoles);
        var selectedRoleValues = Array.from(selectedRoles).map(role => role.value).join(',');
        // selectedRoles.forEach(function(role) {
        //     selectedRole.push(role.value);
        //     //console.log(role.value);
        // });
        console.log("igen2" +selectedRoleValues);


// Elküldjük a kérést az '/updateUserRoleStatus' végpontra
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
                // Kezeljük a választ, és jelenítsük meg az üzenetet
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


document.addEventListener('DOMContentLoaded', function() {
    var errorMessageForSkills = document.getElementById('error-message-for-skills');
    errorMessageForSkills.style.display = 'none';

    var checkboxes = document.querySelectorAll('input[type="checkbox"][name="role"]');

    checkboxes.forEach(function(checkbox) {
        if (checkbox.checked) {
            var label = checkbox.closest('label'); // Megkeressük a checkboxhoz tartozó címkét
            label.classList.add('checked');
        }
        checkbox.addEventListener('change', function() {
            var label = this.closest('label'); // Megkeressük a checkboxhoz tartozó címkét

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

let profileimagesforProfilePage = [];

var seeCommentsButton = document.getElementById('showCommentsButton');
seeCommentsButton.addEventListener('click', function() {
   //console.log("Igen");
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
        if (response.ok) {
            //console.log("Igen: " + response);
            return response.json();
        } else {
            throw new Error('Something went wrong');
        }
    }).then(data => {
        //console.log("Data: ", data);
        profileimagesforProfilePage = data.selectedUserImagesForProfilePage;
        //console.log("Igen: " + profileimagesforSelectedUsers);
        // setInterval(() => {
        //     handlereselectedimagesforProfilePage();
        // }, 500); // 2000 milliszekundum = 2 másodperc
        handlereselectedimagesforProfilePage();
    }).catch(error => {
        console.log("Error: ", error);
    });
});

function handlereselectedimagesforProfilePage() {

    //console.log("Profile images hossz : ", profileimages.length);
    for (let i = 0; i < profileimagesforProfilePage.length; i++) {
        //console.log("Profile images : ", profileimages[i]);
        const commentprofileData = profileimagesforProfilePage[i];
        const commentprofileImage = commentprofileData[0];
        const commentprofileId = commentprofileData[1];

        //console.log("Profil image: ", commentprofileImage);
        //console.log("Profil id: ", commentprofileId);

        var commentedProfileImg  = document.getElementById('commentProfileImgProfilePage-' + commentprofileId);
        //console.log("Profile image div: ", commentedProfileImg);
        if (commentprofileImage != null && commentedProfileImg != null ) {
            commentedProfileImg.src = 'data:image/jpeg;base64,' + commentprofileImage;
        }

    }
}


setupMentorModal();
setupModal();
setupSkillsModal();
setupUploadModal();
closeModalOnClickOutside();
validateFirstName();
validateLastName();
validateSpecialization();
validateYear();
validatePhone();
addSelectedTopic();
//updateRoleStatus();
window.onload = showTopicsAndSkillsInModal();
