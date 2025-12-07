const settingsMenu = document.querySelector(".settingsMenu");
const overlay = document.querySelector(".overlay");
const settingsBtn = document.querySelector(".settingsBtn");
const resetDataBtn = document.querySelector("#resetDataBtn");
const dailyBtn = document.querySelector("#dailyViewBtn");
const weeklyBtn = document.querySelector("#weeklyViewBtn");
const monthlyBtn = document.querySelector("#monthlyViewBtn");
const amountDisplay = document.querySelector("#amount");


import {refreshMainPage,
} from "./display.js";

export let timeRange = "DAILY"

export function timeRangeType(type){
    timeRange = type;
}

settingsBtn.addEventListener("click", ()=>{

    settingsMenu.classList.toggle("active");
    overlay.classList.toggle("active");
})

resetDataBtn.addEventListener("click", ()=>{
     deleteUserData();

})

dailyBtn.addEventListener("click", ()=>{
    timeRangeType("DAILY");
    viewDaily();

})
weeklyBtn.addEventListener("click", ()=>{
    timeRangeType("WEEKLY");
    viewWeekly();


})

monthlyBtn.addEventListener("click", ()=>{
    timeRangeType("MONTHLY");
    viewMonthly();


})




async function deleteUserData() {

    try {
        await fetch(`/reset/data`, {method: "POST"});
        console.log("Data reset completed");
        refreshMainPage("DAILY");

    } catch (err) {
        console.log(err);
    }
}


    async function viewDaily(){

try {
    const res = await fetch("/today").then(r => r.json())
    const goalRes = await fetch("/goal").then(r => r.json())
    amountDisplay.innerHTML = res + " mL";

    refreshMainPage(timeRange);
}catch (err){
    console.log(err);
}
}

async function viewWeekly(){

    try {
        const res = await fetch("/weekly").then(r => r.json())
        const goalRes = await fetch("/goal").then(r => r.json())
        amountDisplay.innerHTML = res + " mL";

        refreshMainPage(timeRange);
    }catch (err){
        console.log(err);
    }
}

async function viewMonthly(){

    try {
        const res = await fetch("/monthly").then(r => r.json())
        const goalRes = await fetch("/goal").then(r => r.json())
        amountDisplay.innerHTML = res + " mL";

        refreshMainPage(timeRange);
    }catch (err){
        console.log(err);
    }
}