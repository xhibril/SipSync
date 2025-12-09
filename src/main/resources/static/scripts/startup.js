const displayStreak = document.querySelector("#displayStreak");

import {refreshMainPage} from "./display.js";
import {addLog, logsFound} from "./logs.js";
import {showMessage} from "./validation.js";

refreshMainPage("DAILY");
checkTodayLogsOnLoad();
checkStreakOnLoad();


// check and calculate the streak on start up
export async function checkStreakOnLoad() {
    try {
        const streakResponse = await fetch("/check/streak");
        if(!streakResponse.ok) {
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
        const logsResponse = await fetch("/logs/today");
        if (!logsResponse.ok){
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















