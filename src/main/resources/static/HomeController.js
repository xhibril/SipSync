console.log("test25455555");
const quickAddBtns  = document.querySelectorAll("[data-addvalue]");
const quickGoalBtns = document.querySelectorAll("[data-goalvalue]");

const quickAddBtnsContainer = document.querySelector(".quickAddAmount");
const quickAddGoalContainer = document.querySelector(".quickAddGoal");

const inputType = document.querySelectorAll('input[name="actionType"]');
const inputField = document.querySelector(".input");

const submitBtn = document.querySelector("#amountSubmitBtn");

import {
refreshMainPageContent
} from "./Display.js";




let choice = "ADD"
// show correct content depending on input type
inputType.forEach(radio => {
    radio.addEventListener("change", () => {
        choice = radio.value;

        switch (choice) {
            case "ADD":
                inputField.placeholder = "Enter amount";

                quickAddGoalContainer.style.display = "none";
                quickAddBtnsContainer.style.display = "flex";
            break;

            case "GOAL":
                inputField.placeholder = "Enter goal";

                quickAddGoalContainer.style.display = "flex";
                quickAddBtnsContainer.style.display = "none";
                break;
        }
    });
});


// handler for inputs
submitBtn.addEventListener("click", () => {
    const amount = parseInt(inputField.value);
    if (isNaN(amount) || amount <= 0) return;
    inputField.value = "";
    handleSubmit(amount);
});


inputField.addEventListener("keydown", (e) => {
    if (e.key === "Enter") {
        submitBtn.click();
        inputField.value = "";
    }
});


// quick add amount btns handler
quickAddBtns.forEach(btn =>{
    btn.addEventListener("click", ()=>{
        const value = Number(btn.dataset.addvalue);
        console.log(value);
        handleSubmit(value)
    });
});

// quick set goal btns handler
quickGoalBtns.forEach(btn =>{
    btn.addEventListener("click", ()=>{
        const value = Number(btn.dataset.goalvalue);
        console.log(value);
        handleSubmit(value);
    });
});





// handles user inputs
function handleSubmit(amount) {
    switch (choice) {
        case "ADD":
            fetch(`/add?add=${amount}`, {method: "POST"})
                .then(e  => {
                    console.log("Saved successfully!");
                    refreshMainPageContent();
                })


                .catch(err => console.error("Error:", err));
            break;

        case "GOAL":
            fetch(`/add/goal?goal=${amount}`, {method: "POST"})
                .then(e  => {
                    console.log("Saved successfully!");
                    refreshMainPageContent();
                })
                .catch(err => console.error("Error:", err));
            break;
    }
}














