import {refreshMainPage} from "./dashboard-refresh.js";
import {addLog} from "./logs.js";
import {validateNumInputs} from "./validation.js";
import {showMessage} from "./notification.js";
import {rateLimited, redirectToLoginPage} from "./http-responses.js";

const actionTypeRadios = document.querySelectorAll('input[name="actionType"]');
const quickAddBtns = document.querySelectorAll("[data-addvalue]");
const quickGoalBtns = document.querySelectorAll("[data-goalvalue]");
const quickAddBtnsContainer = document.querySelector("#quick-add-amount-container");
const quickAddGoalContainer = document.querySelector("#quick-add-goal-container");
const amountInput = document.querySelector("#amount-input");
const submitBtn = document.querySelector("#submit-btn");

const state = {
    choice: "ADD"
}
// show correct content depending on input type
actionTypeRadios.forEach(radio => {
    radio.addEventListener("change", () => {
        state.choice = radio.value;

        switch (state.choice) {
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
        handleSubmit(value)
    });
});

// quick set goal btns handler
quickGoalBtns.forEach(btn => {
    btn.addEventListener("click", () => {
        const value = Number(btn.dataset.goalvalue);
        handleSubmit(value);
    });
});


// handles user inputs
async function handleSubmit(amount) {
    switch (state.choice) {
        case "ADD":
            try {
                const addResponse = await fetch(`/log/add?amount=${amount}`, {method: "POST"});

                if(!addResponse.ok){
                    redirectToLoginPage(addResponse);
                    rateLimited(addResponse);
                    throw new Error();
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
                const goalResponse = await fetch(`/goal/add?goal=${amount}`, {method: "POST"});
                if(!goalResponse.ok){
                    redirectToLoginPage(goalResponse);
                    rateLimited(goalResponse);
                    throw new Error();
                }
                    await refreshMainPage("DAILY");
                    showMessage("success", "Goal added.");
            } catch (err) {
                showMessage("error", "Could not add your goal. Please try again.");
            }
            break;
    }
}