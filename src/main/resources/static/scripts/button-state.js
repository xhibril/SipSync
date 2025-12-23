export function enableBtn(btn){
    btn.disabled = false;
}

export function disableBtn(btn){
    btn.disabled = true;
}

export function btnContent(btn, content){
    btn.textContent = content;
}


export function lockBtn(btn, text) {
    disableBtn(btn);
    btnContent(btn, text);
}

export function unlockBtn(btn, text = "Continue") {
    enableBtn(btn);
    btnContent(btn, text);
}
