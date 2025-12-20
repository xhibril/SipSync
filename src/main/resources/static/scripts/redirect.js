export function redirectToLoginPage(response){
    if(response.status === 401){
        window.location.href = "/login";
    }
}