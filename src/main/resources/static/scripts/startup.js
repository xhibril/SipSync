const displayStreak = document.querySelector("#displayStreak");

import {refreshMainPage} from "./display.js";
import {addLog, logsFound} from "./logs.js";
import {showMessage} from "./validation.js";
import {redirectToLoginPage} from "./redirect.js";

refreshMainPage("DAILY");
checkTodayLogsOnLoad();
checkStreakOnLoad();


// check and calculate the streak on start up
export async function checkStreakOnLoad() {
    try {
        const streakResponse = await fetch("/check/streak");
        if(!streakResponse.ok) {
            redirectToLoginPage(streakResponse);
            return;
           throw new Error("Server returned an error.");
        }
            const streakRes = await streakResponse.json();
            displayStreak.innerHTML = streakRes;

    } catch(err){
        showMessage("error", "Could not load your streak. Please try again later.");
    }
}

// load all of today's logs on start up
export async function checkTodayLogsOnLoad() {
    try {
        const logsResponse = await fetch("/logs");
        if (!logsResponse.ok){
            redirectToLoginPage(logsResponse);
            return;
            throw new Error("Server returned an error.");
        }
            const logsRes = await logsResponse.json();
            if (logsRes.length > 0) {
                logsRes.forEach(log => {
                    addLog(log.amount, log.id, log.time);
                });
                logsFound(true);
            } else {
                logsFound(false);
            }
    } catch (err){
        showMessage("error", "Could not load your daily logs. Please try again later.");
    }
}















