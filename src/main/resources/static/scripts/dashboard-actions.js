import {refreshMainPage} from "./dashboard-refresh.js";
import {addLog} from "./logs.js";
import {validateNumInputs} from "./validation.js";
import {showMessage} from "./notification.js";
import {isBeingRateLimited, redirectToLoginPage} from "./http-responses.js";

const goal = document.querySelector("#goal");
const quickAddBtns = document.querySelectorAll("[data-addvalue]");
const inputField = document.querySelector("#amount-input");
const submitBtn = document.querySelector("#submit-btn");
const dailyBtn = document.querySelector("#view-daily");
const weeklyBtn = document.querySelector("#view-weekly");
const monthlyBtn = document.querySelector("#view-monthly");

// add goal / edit goal
goal.addEventListener("keydown", async (e) => {
    if (e.key !== "Enter") return;
        e.preventDefault();
        goal.blur();
        let amount = parseInt(goal.textContent.trim(), 10);

        if (!validateNumInputs(amount)){
            goal.innerHTML = 0;
            showMessage("error", "Please enter a valid goal.");
            return;
        }
        goal.innerHTML = amount;
        await handleGoal(amount);
});


// quick add amount btns handler
quickAddBtns.forEach(btn => {
    btn.addEventListener("click", () => {
        const value = Number(btn.dataset.addvalue);
        handleAdd(value);
    });
});

inputField.addEventListener("keydown", function (event) {
    if (event.key === "Enter") {
        submitBtn.click();
    }
});

submitBtn.addEventListener("click", () => {
    const amount = parseInt(inputField.value);
    if (validateNumInputs(amount)) {
        inputField.value = "";
        handleAdd(amount);
    }
});


dailyBtn.addEventListener("click", async () => {
    await refreshMainPage("DAILY");
});

weeklyBtn.addEventListener("click", async () => {
    await refreshMainPage("WEEKLY");
});

monthlyBtn.addEventListener("click", async () => {
    await refreshMainPage("MONTHLY");
});


// handles user inputs
async function handleAdd(amount) {
    try {
        const addResponse = await fetch(`/log/add?amount=${amount}`, {method: "POST"});

        if (!addResponse.ok) {
            redirectToLoginPage(addResponse);
            if (isBeingRateLimited(addResponse)) return;
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
}


async function handleGoal(amount) {
    try {
        const goalResponse = await fetch(`/goal/add?goal=${amount}`, {method: "POST"});

        if (!goalResponse.ok) {
            redirectToLoginPage(goalResponse);
            if (isBeingRateLimited(goalResponse)) return;
            throw new Error();
        }
        await refreshMainPage("DAILY");
        showMessage("success", "Goal added.");
    } catch (err) {
        showMessage("error", "Could not add your goal. Please try again.");
    }
}