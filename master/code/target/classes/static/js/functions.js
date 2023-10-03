document.addEventListener("DOMContentLoaded", function() {
    var h1Element = document.getElementById("index-text");
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

    if (!/^\d{10,}$/.test(phoneValue)){
        phoneInput.classList.add("highlight");
    } else {
        phoneInput.classList.remove("highlight");
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
        console.log(tagName);
        console.log(elementText);
        console.log(tag)
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

document.getElementById('upload-upload').addEventListener('click', function() {
    document.getElementById('imageUpload').click();
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
    //var selectedTopic = document.getElementById("topic-input").value;
    //console.log(selectedTopic.type);
    var selectedTopic = document.getElementById("topic-input").value;
    //console.log(typeof selectedTopic);

    if (selectedTopic !== "") {
        if (!isTopicAlreadyAdded(selectedTopic)) {
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
            // Vagy használhatsz egy másféle visszajelzést is
            alert("Ez a téma már hozzá van adva!");
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
    //console.log(data);
    // Elküldjük az adatokat a szervernek
    sendDataToServer(data);
}

function sendDataToServer(data) {
    // Állítsd be a rejtett mező értékét adataid alapján
    var profileTopicsDataItems = JSON.stringify(data);
    document.getElementById("profileTopicsDataItems").value = profileTopicsDataItems;
    console.log("Adatok: " + profileTopicsDataItems);

    // Most küldd el az űrlapot
    document.getElementById("skills-form").submit();
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
                    deleteRowAndDatabase(this);
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

function updateRoleStatus(){
    var selectedRole = document.querySelector('input[name="role"]:checked').value;
    console.log(selectedRole);
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

// Elküldjük a kérést az '/updateUserRoleStatus' végpontra
    fetch('http://localhost:8080/updateUserRoleStatus', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'X-CSRF-TOKEN': token
        },
        body: 'selectedRole=' + encodeURIComponent(selectedRole)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Hiba történt a kérés során.');
            }
            return response.json(); // Várunk egy JSON választ
        })
        .then(data => {
            // Kezeljük a választ, és jelenítsük meg az üzenetet
            console.log(data.message);
            location.reload();
        })
        .catch(error => {
            console.error('Hiba történt:', error);
        });
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

window.onload = showTopicsAndSkillsInModal;














