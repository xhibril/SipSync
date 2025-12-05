export function isInputValid(...input){
    for (let value of input){
        value = Number(value)
        if(isNaN(value) || value <= 0) {
            return false;
        }
    }
    return true;

}