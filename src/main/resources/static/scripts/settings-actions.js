import {isBeingRateLimited, redirectToLoginPage} from "./http-responses.js";
import {refreshMainPage} from "./dashboard-refresh.js";
import {showMessage} from "./notification.js";
import {lockBtn, unlockBtn} from "./button-state.js";

const settingsMenu = document.querySelector("#settings-menu");
const overlay = document.querySelector("#app-overlay");
const settingsBtn = document.querySelector("#settings-btn");
const resetDataBtn = document.querySelector("#data-reset");
const dailyBtn = document.querySelector("#view-daily");
const weeklyBtn = document.querySelector("#view-weekly");
const monthlyBtn = document.querySelector("#view-monthly");
const amountDisplay = document.querySelector("#water-drank");
const periodLabelDisplay = document.querySelector("#period-label");
const logoutBtn = document.querySelector("#logout-btn");

const state = {
    timeRange: "DAILY"
}

overlay.addEventListener("click", () => {
    settingsMenu.classList.remove("active");
    overlay.classList.remove("active");
});

settingsBtn.addEventListener("click", () => {
    settingsMenu.classList.toggle("active");
    overlay.classList.toggle("active");
})

resetDataBtn.addEventListener("click", () => {
    deleteUserData();
})

dailyBtn.addEventListener("click", () => {
    state.timeRange = "DAILY";
    view("/stats/daily");
})

weeklyBtn.addEventListener("click", () => {
    state.timeRange = "WEEKLY";
    view("/stats/weekly", "Viewing weekly average water intake");
})

monthlyBtn.addEventListener("click", () => {
    state.timeRange = "MONTHLY";
    view("/stats/monthly", "Viewing monthly average water intake");
})

logoutBtn.addEventListener("click", ()=>{
    logout();
})
async function deleteUserData() {
    try {
        const resetDataResponse = await fetch("/data/reset", {method: "POST"});
        if (!resetDataResponse.ok){
            redirectToLoginPage(resetDataResponse);
            if(isBeingRateLimited(resetDataResponse)) return;
            throw new Error();
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

async function view(url, label){
    try{
        const response = await fetch(url);
        if(!response.ok){
            redirectToLoginPage(response);
            throw new Error();
        }
        const res = await response.json();
        amountDisplay.innerHTML = res + " mL";
        await refreshMainPage(state.timeRange);

        if(label){
            periodLabelDisplay.classList.remove('hidden');
            periodLabelDisplay.textContent = label;
        } else {
        periodLabelDisplay.classList.add("hidden");
    }
    } catch (err){
        showMessage("error", "Could not load data. Please try again later.");
    }
}

async function logout(){
    lockBtn(logoutBtn, "Logging in...");
    try{
        const logoutResponse = await fetch("/api/logout", {method:"POST"});
        if(!logoutResponse.ok) {
            if(isBeingRateLimited(logoutResponse)) return;
            throw new Error();
        }
        window.location.href = "/login";
    } catch(err){
        showMessage("error", "Could not log you out. Please try again.");
    } finally{
        unlockBtn(logoutBtn);
    }
}


