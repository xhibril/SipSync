
import {refreshMainPage} from "./display.js";
import {addLog} from "./logs.js";
import {validateNumInputs, showMessage} from "./validation.js";
import {redirectToLoginPage} from "./redirect.js";

const quickAddBtns = document.querySelectorAll("[data-addvalue]");
const quickGoalBtns = document.querySelectorAll("[data-goalvalue]");
const quickAddBtnsContainer = document.querySelector(".quickAddAmount");
const quickAddGoalContainer = document.querySelector(".quickAddGoal");
const inputType = document.querySelectorAll('input[name="actionType"]');
const amountInput = document.querySelector(".input");
const submitBtn = document.querySelector("#amountSubmitBtn");

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
                const addResponse = await fetch(`/add?amount=${amount}`, {method: "POST"});

                if(!addResponse.ok){

                    redirectToLoginPage(addResponse);
                    throw new Error("Server returned an error.");
                    return;
                }
                    const addRes = await addResponse.json();
                    addLog(amount, addRes.id, addRes.time);  // display log to user

                    // if user enters an amount, send them back to daily view period
                    await refreshMainPage("DAILY");
                    showMessage("success", "Water intake added.");

            } catch (err) {
                showMessage("error", "Could not add your water intake. Please try again.");
            }
            break;

        case "GOAL":
            try {
                const goalResponse = await fetch(`/add/goal?goal=${amount}`, {method: "POST"});
                if(!goalResponse.ok){

                    redirectToLoginPage(goalResponse);
                    throw new Error("Server returned an error.");
                    return;
                }
                    await refreshMainPage("DAILY");
                    showMessage("success", "Goal added.");

            } catch (err) {
                showMessage("error", "Could not add your goal. Please try again.");
            }
            break;
    }
}