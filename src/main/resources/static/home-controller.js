const quickAddBtns = document.querySelectorAll("[data-addvalue]");
const quickGoalBtns = document.querySelectorAll("[data-goalvalue]");
const quickAddBtnsContainer = document.querySelector(".quickAddAmount");
const quickAddGoalContainer = document.querySelector(".quickAddGoal");
const inputType = document.querySelectorAll('input[name="actionType"]');
const amountInput = document.querySelector(".input");
const submitBtn = document.querySelector("#amountSubmitBtn");

import {refreshMainPage, refreshGoal, refreshProgressBar} from "./display.js";
import {addLog} from "./logs.js";
import {validateNumInputs, displayErrorMessage} from "./validation.js";


let message;

let choice = "ADD"
// show correct content depending on input type
inputType.forEach(radio => {
    radio.addEventListener("change", () => {
        choice = radio.value;

        switch (choice) {
            case "ADD":
                amountInput.placeholder = "Enter amount";

                quickAddGoalContainer.classList.add('hidden');
                quickAddBtnsContainer.classList.remove('hidden');
                break;

            case "GOAL":
                amountInput.placeholder = "Enter goal";

                quickAddGoalContainer.classList.remove('hidden');
                quickAddBtnsContainer.classList.add('hidden');
                break;
        }
    });
});


// handler for inputs
submitBtn.addEventListener("click", () => {
    const amount = parseInt(amountInput.value);
    if (validateNumInputs(amount)) {
        amountInput.value = "";
        handleSubmit(amount);
    }
});


amountInput.addEventListener("keydown", function (event) {
    if (event.key === "Enter") {
        submitBtn.click();
    }
});


// quick add amount btns handler
quickAddBtns.forEach(btn => {
    btn.addEventListener("click", () => {
        const value = Number(btn.dataset.addvalue);
        console.log(value);
        handleSubmit(value)
    });
});

// quick set goal btns handler
quickGoalBtns.forEach(btn => {
    btn.addEventListener("click", () => {
        const value = Number(btn.dataset.goalvalue);
        console.log(value);
        handleSubmit(value);
    });
});


// handles user inputs
async function handleSubmit(amount) {
    switch (choice) {
        case "ADD":
            try {
                const response = await fetch(`/add?add=${amount}`, {method: "POST"})
                const res = await response.json();
                addLog(amount, res, res.time);  // display log to user

                // if user enters an amount, send them back to daily view period
                refreshMainPage("DAILY");

            } catch (err) {
                let message = "Something went wrong on our end, please try again later."
                console.log(err);
                displayErrorMessage(true, message);
            }
            break;

        case "GOAL" :
            try {
                await fetch(`/add/goal?goal=${amount}`, {method: "POST"});
                refreshMainPage("DAILY");

            } catch (err) {
                message = "Couldn't update your goal, please try again later.";
                displayErrorMessage(true, message);
            }
            break;
    }
}