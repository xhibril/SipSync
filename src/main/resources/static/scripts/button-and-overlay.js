const overlay = document.querySelector(".overlay");

export function showOverlay(bool){
  overlay.classList.toggle("active", bool);
}

export function enableBtn(btn){
    btn.disabled = false;
}

export function disableBtn(btn){
    btn.disabled = true;
}