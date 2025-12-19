import {redirectToLoginPage} from "./redirect.js";

const waterDrankDisplay = document.querySelector("#amount");
const goalDisplay = document.querySelector("#goal");
const remainingDisplay = document.querySelector("#displayRemaining");
const streakDisplay = document.querySelector("#displayStreak");
const periodLabelDisplay = document.querySelector("#period-label");

import {showMessage} from "./validation.js";

let waterDrank = 0;
let goal = 0;
let url;

export async function refreshMainPage(timeRange) {
    switch (timeRange) {
        case "DAILY":{
            url = "/today";
            // hide viewing "weekly / monthly avg" label
            periodLabelDisplay.classList.add("hidden");
            break;
        }
        case "WEEKLY": url = "/weekly"; break;
        case "MONTHLY": url = "/monthly"; break;
    }

    await refreshWaterIntake();
    await refreshGoal();

    refreshProgressBar(waterDrank, goal);
    refreshRemaining();
}

export async function refreshGoal(){
    try{
        const goalResponse = await fetch("/goal");

        if(!goalResponse.ok){
            redirectToLoginPage(goalResponse);
            return;
            throw new Error("Server returned an error.");
        }
            const goalRes = await goalResponse.json();
            goal = goalRes;
            goalDisplay.innerHTML = goal + " mL";

    } catch(err) {
        showMessage("error", "Could not load your goal, please try again later.");
    }
}

export async function refreshWaterIntake(){
    try{
        const amountResponse = await fetch(url);

        if (!amountResponse.ok){
            redirectToLoginPage(amountResponse);
            return;
            throw new Error("Server returned an error.");
        }
            const amountRes = await amountResponse.json();
            waterDrank = amountRes;
            waterDrankDisplay.innerHTML = waterDrank + " mL";

    } catch(err) {
        showMessage("error", "Could not load your water intake, please try again later.");
    }
}


export function refreshProgressBar(waterDrank, goal) {
    if (goal <= 0) {
        document.querySelector(".progressBar").style.backgroundSize = `0% 100%, 100% 100%`;
        return;
    }
    let percent = Math.ceil((waterDrank * 100) / goal);
    if (percent > 100) percent = 100;
    if (percent < 0) percent = 0;

    document.querySelector(".progressBar").style.backgroundSize = `${percent}% 100%, 100% 100%`;
}


export function refreshRemaining(){
    // check remaining and increment streak if reached
    let rem = calculateRemaining(waterDrank, goal);
    remainingDisplay.innerHTML = rem;

    if (rem === 0 && goal !== 0) {
        incrementStreak();
    }
}

function calculateRemaining(amountDrank, goal) {
    if (amountDrank >= goal) return 0;
    return (goal - amountDrank);
}


async function incrementStreak() {
    try {
        const incrementStreakResponse = await fetch("/streak/increment", {method: "POST"});
        if(!incrementStreakResponse.ok){
            redirectToLoginPage(incrementStreakResponse);
            return;
            throw new Error("Server returned an error.");
        }
            const streakRes = await incrementStreakResponse.json();
            streakDisplay.innerHTML = streakRes;

    } catch (err){
        showMessage("error", "Could not update your streak, please try again later.");
    }
}