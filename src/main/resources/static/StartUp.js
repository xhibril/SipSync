console.log("h");
const displayStreak = document.querySelector("#displayStreak");


checkStreakOnStartUp();

async function checkStreakOnStartUp(){
   const streakRes = await fetch("/check/streak").then(r=>r.json())
    displayStreak.innerHTML = streakRes;

}
