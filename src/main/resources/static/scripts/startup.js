const displayStreak = document.querySelector("#displayStreak");

import {refreshMainPage} from "./display.js";
import {addLog, logsFound} from "./logs.js";
import {showMessage} from "./validation.js";

refreshMainPage("DAILY");
checkTodayLogsOnLoad();
checkStreakOnLoad();

export async function checkStreakOnLoad() {
    try {
        const streakResponse = await fetch("/check/streak");
        const streakRes = await streakResponse.json();
        displayStreak.innerHTML = streakRes;
    } catch(err){
        showMessage(true, "Failed to load your streak, please try again later.");
    }
}

export async function checkTodayLogsOnLoad() {
    try {
        const logsResponse = await fetch("/logs/today");
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
        showMessage(true, "Failed to load your daily logs, please try again later.");
    }
}















