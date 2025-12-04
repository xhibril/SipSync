console.log("logs");
const logs = document.querySelector(".logs");
const noLogsFound = document.querySelector("#noLogsGif");
console.log(noLogsFound);
console.log(logs);

// add log to "Today's Logs"
export function addLog(amount){
    const p = document.createElement("p");

    p.textContent = amount;
    p.contentEditable = true;

    p.addEventListener("keydown", (e) => {
        if (e.key === "Enter") {
            let newValue = p.textContent;

            updateAmount(newValue);
        }
    })
    logs.appendChild(p);


}

async function updateAmount(newValue){
    console.log(newValue);
    await fetch(`/update/amount?amount=${newValue}`, {method: "POST"})
        .then(console.log("Updated success"))
        .catch(err =>{
            console.log(err);
        })

}



export function logsFound(bool){
    console.log(bool);
    if(bool === true) {

        noLogsFound.classList.add('hidden');
        logs.classList.remove('hidden');

    } else {
        noLogsFound.classList.remove('hidden');
        logs.classList.add('hidden');

    }
}


