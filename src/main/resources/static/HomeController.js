console.log("HELLO")
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

import{
    addLog
} from "./Logs.js";

import {isInputValid} from "./InputValidation.js";

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
    console.log(isInputValid(amount));
    if(isInputValid(amount)) {
        inputField.value = "";
        handleSubmit(amount);
    }



});


inputField.addEventListener("keydown", function(event) {
    if (event.key === "Enter") {
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
async function handleSubmit(amount) {
    switch (choice) {
        case "ADD":
            const res = await fetch(`/add?add=${amount}`, {method: "POST"}).then(r => r.json())
            console.log("Saved successfully!");

            // if user enters an amount, send them back to daily view period
            refreshMainPageContent("DAILY");
            addLog(amount, res);
            console.log(res);
            break;


     case "GOAL" :
    fetch(`/add/goal?goal=${amount}`, {method: "POST"})
        .then(e => {
            console.log("Saved successfully!");

            // if updates goal, send them back to daily view period
            refreshMainPageContent("DAILY");
        })
        .catch(err => console.error("Error:", err));
    break;
}
}














