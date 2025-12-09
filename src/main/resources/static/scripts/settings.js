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
    viewDaily();
    timeRangeType("DAILY");
})

weeklyBtn.addEventListener("click", () => {
    viewWeekly();
    timeRangeType("WEEKLY");
})

monthlyBtn.addEventListener("click", () => {
    viewMonthly();
    timeRangeType("MONTHLY");
})


async function deleteUserData() {

    try {
        const resetDataResponse = await fetch("/reset/data", {method: "POST"});
        if (!resetDataResponse.ok){
            throw new Error("Server returned an error.");
        }
            showMessage("success", "Data deleted. Refreshing...");
            // fresh reload
            setTimeout(() => {
                window.location.href = window.location.pathname + "?t=" + Date.now();
            }, 2000);

    } catch (err) {
        showMessage("error", "Could not delete user data. Please try again later.");
    }
}


async function viewDaily() {
    try {
        const todayResponse = await fetch("/today");
        if (!todayResponse.ok){
            throw new Error("Server returned an error.");
        }
            const todayRes = await todayResponse.json();
            amountDisplay.innerHTML = todayRes + " mL";
            await refreshMainPage(timeRange);

    } catch (err) {
        showMessage("error", "Could not load daily data. Please try again later.");
    }
}

async function viewWeekly() {
    try {
        const weeklyResponse = await fetch("/weekly");
        if (!weeklyResponse.ok){
            throw new Error("Server returned an error.");
        }
            const weeklyRes = await weeklyResponse.json();

            amountDisplay.innerHTML = weeklyRes + " mL";
            await refreshMainPage(timeRange);
            periodLabelDisplay.classList.remove("hidden");
            periodLabelDisplay.textContent = "Viewing weekly average intake";

    } catch (err) {
        showMessage("error", "Could not load weekly data, please try again later.");
    }
}

async function viewMonthly() {
    try {
        const monthlyResponse = await fetch("/monthly");
        if(!monthlyResponse.ok){
            throw new Error("Server returned an error.");
        }
            const monthlyRes = await monthlyResponse.json();
            amountDisplay.innerHTML = monthlyRes + " mL";
            await refreshMainPage(timeRange);
            periodLabelDisplay.classList.remove("hidden");
            periodLabelDisplay.textContent = "Viewing monthly average intake";

    } catch (err) {
        showMessage("error", "Could not load monthly data, please try again later.");
    }
}