const settingsMenu = document.querySelector(".settingsMenu");
const overlay = document.querySelector(".overlay");
const settingsBtn = document.querySelector(".settingsBtn");
const resetDataBtn = document.querySelector("#resetDataBtn");
const dailyBtn = document.querySelector("#dailyViewBtn");
const weeklyBtn = document.querySelector("#weeklyViewBtn");
const monthlyBtn = document.querySelector("#monthlyViewBtn");
const amountDisplay = document.querySelector("#amount");
const periodLabelDisplay = document.querySelector("#period-label");

import {refreshMainPage,} from "./display.js";
import {checkStreakOnLoad, checkTodayLogsOnLoad} from "./startup.js";
import {showMessage} from "./validation.js";

export let timeRange = "DAILY"
export function timeRangeType(type) {
    timeRange = type;
}

settingsBtn.addEventListener("click", () => {
    settingsMenu.classList.toggle("active");
    overlay.classList.toggle("active");
})

resetDataBtn.addEventListener("click", () => {
    deleteUserData();
})

dailyBtn.addEventListener("click", () => {
    timeRangeType("DAILY");
    viewDaily();
})

weeklyBtn.addEventListener("click", () => {
    timeRangeType("WEEKLY");
    viewWeekly();
})

monthlyBtn.addEventListener("click", () => {
    timeRangeType("MONTHLY");
    viewMonthly();
})


async function deleteUserData() {

    try {
        await fetch(`/reset/data`, {method: "POST"});
        refreshMainPage("DAILY");

        // reload the streak and today's logs from start up js file
        checkStreakOnLoad();
        checkTodayLogsOnLoad();
        showMessage("success", "Data deleted.");

    } catch (err) {
        showMessage("error", "Failed to delete user data, please try again later.");
    }
}


async function viewDaily() {

    try {
        const todayResponse = await fetch("/today");
        const todayRes = await todayResponse.json();

        amountDisplay.innerHTML = todayRes + " mL";
        refreshMainPage(timeRange);
    } catch (err) {
        showMessage("error", "Failed to load daily data, please try again later.");
    }
}

async function viewWeekly() {

    try {
        const weeklyResponse = await fetch("/weekly");
        const weeklyRes = await weeklyResponse.json();

        amountDisplay.innerHTML = weeklyRes + " mL";
        refreshMainPage(timeRange);
        periodLabelDisplay.classList.remove("hidden");
        periodLabelDisplay.textContent = "Viewing weekly average intake";
    } catch (err) {
        showMessage("error", "Failed to load weekly data, please try again later.");
    }
}

async function viewMonthly() {

    try {
        const monthlyResponse = await fetch("/monthly");
        const monthlyRes = await monthlyResponse.json();

        amountDisplay.innerHTML = monthlyRes + " mL";
        refreshMainPage(timeRange);
        periodLabelDisplay.classList.remove("hidden");
        periodLabelDisplay.textContent = "Viewing monthly average intake";
    } catch (err) {
        showMessage("error", "Failed to load monthly, please try again later.");
    }
}