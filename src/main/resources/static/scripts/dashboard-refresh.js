import {isBeingRateLimited, redirectToLoginPage} from "./http-responses.js";
import {showMessage} from "./notification.js";

const waterDrankDisplay = document.querySelector("#water-drank");
const streakDisplay = document.querySelector("#streak");
const goalDisplay = document.querySelector("#goal");
const remainingDisplay = document.querySelector("#remaining");
const periodLabel = document.querySelector("#period-label");

const state = {
    waterDrank: 0,
    goal: 0,
    url: null,
    period: "DAILY"
};

// refresh all
export async function refreshMainPage(timeRange) {
    switch (timeRange) {
        case "DAILY": {
            state.period = "DAILY";
            state.url = "/stats/daily";
            break;
        }
        case "WEEKLY": {
            state.period = "WEEKLY";
            state.url = "/stats/weekly";
            break;
        }
        case "MONTHLY": {
            state.period = "MONTHLY";
            state.url = "/stats/monthly";
            break;
        }
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
            goalDisplay.innerHTML = state.goal;

    } catch(err) {
        showMessage("error", "Could not load your goal, please try again later.");
    }
}

// refresh water intake
export async function refreshWaterIntake(){
    try{
        const amountResponse = await fetch(state.url, {method: "GET"});

        if (!amountResponse.ok){
            redirectToLoginPage(amountResponse);
            if(isBeingRateLimited(amountResponse)) return;
            throw new Error();
        }
            const amountRes = await amountResponse.json();
            state.waterDrank = amountRes;

        switch(state.period){
            case "DAILY" : {
                waterDrankDisplay.innerHTML = state.waterDrank;
                periodLabel.classList.add("hidden");
                break;
            }
            case "WEEKLY" : {
                waterDrankDisplay.innerHTML = state.waterDrank;
                periodLabel.classList.remove("hidden");
                periodLabel.textContent = "Viewing average water drank per day past week";
                break;
            }
            case "MONTHLY": {
                waterDrankDisplay.innerHTML = state.waterDrank;
                periodLabel.classList.remove("hidden");
                periodLabel.textContent = "Viewing average water drank per day past month";
                break;
            }
        }
    } catch(err) {
        showMessage("error", "Could not load your water intake, please try again later.");
    }
}


// refresh progress bar
export function refreshProgressBar(waterDrank, goal) {
    if (goal <= 0) {
        document.querySelector(".progressBar").style = `--percent: 0;`;
        return;
    }
    let percent = Math.ceil((waterDrank * 100) / goal);
    if (percent > 100) percent = 100;
    if (percent < 0) percent = 0;
    document.querySelector(".progressBar").style = `--percent: ${percent};`;
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