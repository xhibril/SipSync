import {isBeingRateLimited, redirectToLoginPage} from "./http-responses.js";
import {showMessage} from "./notification.js";

const waterDrankDisplay = document.querySelector("#water-drank");
const goalDisplay = document.querySelector("#goal");
const remainingDisplay = document.querySelector("#water-remaining");
const streakDisplay = document.querySelector("#streak");
const periodLabelDisplay = document.querySelector("#period-label");


const state = {
    waterDrank: 0,
    goal: 0,
    url: null
};

// refresh all
export async function refreshMainPage(timeRange) {
    switch (timeRange) {
        case "DAILY":{
            state.url = "/stats/daily";
            // hide viewing "weekly / monthly avg" label
            periodLabelDisplay.classList.add("hidden");
            break;
        }
        case "WEEKLY": state.url = "/stats/weekly"; break;
        case "MONTHLY": state.url = "/stats/monthly"; break;
    }

    await refreshWaterIntake();
    await refreshGoal();

    refreshProgressBar(state.waterDrank, state.goal);
    refreshRemaining();
}

// refresh goal
export async function refreshGoal(){
    try{
        const goalResponse = await fetch("/goal/get");

        if(!goalResponse.ok){
            redirectToLoginPage(goalResponse);
            if(isBeingRateLimited(goalResponse)) return;
            throw new Error();
        }
            const goalRes = await goalResponse.json();
            state.goal = goalRes;
            goalDisplay.innerHTML = state.goal + " mL";

    } catch(err) {
        showMessage("error", "Could not load your goal, please try again later.");
    }
}

// refresh water intake
export async function refreshWaterIntake(){
    try{
        const amountResponse = await fetch(state.url);

        if (!amountResponse.ok){
            redirectToLoginPage(amountResponse);
            if(isBeingRateLimited(amountResponse)) return;
            throw new Error();
        }
            const amountRes = await amountResponse.json();
            state.waterDrank = amountRes;
            waterDrankDisplay.innerHTML = state.waterDrank + " mL";

    } catch(err) {
        showMessage("error", "Could not load your water intake, please try again later.");
    }
}


// refresh progress bar
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
    let rem = calculateRemaining(state.waterDrank, state.goal);
    remainingDisplay.innerHTML = rem;

    if (rem === 0 && state.goal !== 0) {
        incrementStreak();
    }
}

function calculateRemaining(amountDrank, goal) {
    if (amountDrank >= goal) return 0;
    return (goal - amountDrank);
}

// increase streak if goal is reached
async function incrementStreak() {
    try {
        const incrementStreakResponse = await fetch("/streak/increment", {method: "POST"});
        if(!incrementStreakResponse.ok){
            redirectToLoginPage(incrementStreakResponse);
            if(isBeingRateLimited(incrementStreakResponse)) return;
            throw new Error();
        }
            const streakRes = await incrementStreakResponse.json();
            streakDisplay.innerHTML = streakRes;

    } catch (err){
        showMessage("error", "Could not update your streak, please try again later.");
    }
}